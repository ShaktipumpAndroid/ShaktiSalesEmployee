package activity;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;
import com.shaktipumps.shakti.shaktisalesemployee.BuildConfig;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import bean.LoginBean;
import database.DatabaseHelper;
import syncdata.SyncDataToSAP_New;
import webservice.SAPWebService;
import webservice.WebURL;


public class MainActivity1 extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, SharedPreferences.OnSharedPreferenceChangeListener {
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private static final String TAG = MainActivity1.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    FusedLocationProviderClient fusedLocationProviderClient;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;
    // Tracks the bound state of the service.
    private boolean mBound = false;
    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    ProgressDialog progressBar;
    CustomUtility customUtility;
    ProgressDialog dialog;
    Context mContex;
    String versionName = "0.0";
    DatabaseHelper dataHelper = null;
    SAPWebService con = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String newVersion = "0.0";
    String logout_value = "";
    String current_date = "null", current_time;
    String latitude = "0.0";
    String longitude = "0.0";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MainActivity1.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public  static  DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myReceiver = new MyReceiver();
        mContex = this;
        con = new SAPWebService();
         db = new DatabaseHelper(MainActivity1.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        fusedLocationProviderClient = getFusedLocationProviderClient(this);

        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);


        deleteCache(mContex);

        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        customUtility = new CustomUtility();

        dataHelper = new DatabaseHelper(this);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        // int versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

        // dataHelper.deletecmpattach();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");

        LoginBean.username = pref.getString("key_ename", "username");


        // check background service is running or not

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        if (checkPlayServices()) {


        } else {
            Toast.makeText(mContex, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

/******* Create SharedPreferences *******/


        if (!pref.getString("key_sync_date", "date").equalsIgnoreCase(customUtility.getCurrentDate())) {
            // download data one time ia day
            download();
            editor.putString("key_sync_date", customUtility.getCurrentDate());  // Saving string
            editor.commit(); //
        } else {
            syncOfflineData("1");
            Toast.makeText(this, "Welcome,  " + LoginBean.getUsername() + " \n    App Version " + versionName, Toast.LENGTH_LONG).show();
        }

// set login person name to navigation drawer

        TextView tv = (TextView) findViewById(R.id.ename);
        tv.setText("Welcome,  " + LoginBean.getUsername() + "   V " + versionName);

        // delete data from mobile which is save in sap
        delete_data_save_in_sap();


        // display the first navigation drawer view on app launch
        displayView(0);

     }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_download_all:
                // sync all data
                download();
                return true;
            case R.id.action_sync_offline:
                // sync all data
                syncOfflineData("1");
                return true;
            case android.R.id.home:
                // onBackPressed();
                return true;
            case R.id.action_signout:
                new AlertDialog.Builder(this)
                        .setTitle("Sign Out Alert !")
                        .setMessage("Do you want to Sign Out this application ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // logout

                                // syncOfflineData("16");
                                logOut();

                                dialog.cancel();
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // user doesn't want to logout
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncOfflineData(final String event) {
        progressDialog = ProgressDialog.show(MainActivity1.this, "", "Sync Offline Data !");
        GPSTracker gps = new GPSTracker(mContex);
        latitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude())));
        longitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude())));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CustomUtility.isOnline(MainActivity1.this)) {
                    if (latitude != null && longitude != null) {
                        dataHelper.insertEmployeeGPSActivity(
                                LoginBean.userid,
                                new CustomUtility().getCurrentDate(),
                                new CustomUtility().getCurrentTime(),
                                event,
                                latitude,
                                longitude,
                                mContex,
                                "");
                    }
                    new SyncDataToSAP_New().SendDataToSap(mContex);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);
                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void logOut() {
        progressDialog = ProgressDialog.show(MainActivity1.this, "", "Sync Offline Data !");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CustomUtility.isOnline(MainActivity1.this)) {
                 CustomUtility.logoutUser(MainActivity1.this);

                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Message msg = new Message();
                    msg.obj = "No internet Connection. Log Out Failed";
                    mHandler.sendMessage(msg);
                }
            }
        }
        ).start();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        Intent intent = null;
        Context ctx = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                //   title = getString(R.string.title_home);
                break;
            case 1:
                intent = new Intent(MainActivity1.this, AttendanceReportActivity.class);
                startActivity(intent);
                // title = getString(R.string.title_friends);
                break;
            case 2:
                intent = new Intent(MainActivity1.this, DeviceStatusActivity.class);
                startActivity(intent);
                // title = getString(R.string.title_friends);
                break;
            case 3:
                intent = new Intent(MainActivity1.this, RoutePlanSearchActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(MainActivity1.this, MaterialAnalysisActivity.class);
                startActivity(intent);
                break;
            case 5:
                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity1.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Intent intent = new Intent(MainActivity1.this, FileActivity.class);
                            intent.putExtra("call_portal", "Files & Folder");
                            startActivity(intent);
                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();


                break;


            case 6:
                intent = new Intent(MainActivity1.this, VideoSearchActivity.class);
                startActivity(intent);
                break;

            case 7:

                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity1.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebURL.CABLE_SELECTION));
                            startActivity(browserIntent);

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();

                break;


            case 8:

                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity1.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebURL.PIPE_SELECTION));
                            startActivity(browserIntent);

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();

                break;


            case 9:

                //   shakti chat app
//                 ctx=this; // or you can replace **'this'** with your **ActivityName.this**
//                try {
//                    Intent i = ctx.getPackageManager().getLaunchIntentForPackage("com.shaktichat.shaktichatapp");
//                    ctx.startActivity(i);
//                } catch (Exception e)
//                {
//                    Toast.makeText(this,"Please Install Shakti Chat App From Play Store",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.shaktichat.shaktichatapp")));
//                }
//


                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity1.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebURL.SOLAR_CABLE_SELECTION));
                            startActivity(browserIntent);

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();


                break;


//  Shakti Pump Selector
            case 10:

                ctx = this;
                try {
                    Intent i = ctx.getPackageManager().getLaunchIntentForPackage("net.vsx.spaix4mobile.shakti");
                    ctx.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Please Install Shakti Pump Selector App From Play  Store", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.vsx.spaix4mobile.shakti")));
                }

                break;


            //  Shakti Employee (HR)
            case 11:

                ctx = this;
                try {
                    Intent i = ctx.getPackageManager().getLaunchIntentForPackage("shakti.shakti_employee");
                    ctx.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Please Install Shakti Employee App From Play Store", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=shakti.shakti_employee")));
                }

                break;


            //  Shakti Canteen app
            case 12:

                ctx = this;
                try {
                    Intent i = ctx.getPackageManager().getLaunchIntentForPackage("com.mcrlogitech.shaktipump");
                    ctx.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Please Install Shakti Canteen App From Play Store", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mcrlogitech.shaktipump")));
                }

                break;


            case 13:

                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity1.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.shaktipumps.com/faq.php"));
                            startActivity(browserIntent);

                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();

                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(" V  " + versionName);
            getSupportActionBar().setIcon(R.drawable.new_logo);

        }
    }


    public void download() {


        // creating progress bar dialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        // progressBar.setCancelable(true);
        progressBar.setMessage("Downloading Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        //reset progress bar and filesize status
        progressBarStatus = 0;
        fileSize = 0;


        new Thread(new Runnable() {
            public void run() {


                if (CustomUtility.isOnline(MainActivity1.this)) {


                    while (progressBarStatus < 100) {
                        // performing operation

                        try {
                            // Get Attendance Data
                            progressBarStatus = con.getAttendanceData(MainActivity1.this);
                            progressBarStatus = 20;

                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                            /************** sync material */

                            dataHelper.deleteAdhocOrder();
                            progressBarStatus = con.getMaterialDetail(MainActivity1.this);

                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                            /************** sync material */


                            // get target vs achievement
                            progressBarStatus = con.getTargetData(MainActivity1.this);

                            // visit history
                            progressBarStatus = con.getVisitHistory(MainActivity1.this);


                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                            // get search help
                            progressBarStatus = con.getSearchHelp(MainActivity1.this);


                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                            // get route plan
                            progressBarStatus = con.getRouteDetail(MainActivity1.this);


                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();


                } else {
                    progressBar.dismiss();
                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                }


            }

        }).start();


    }


    public  static  void delete_data_save_in_sap() {

        db.deleteSurvey();
        db.deleteEmployeeGPSActivity();
        db.deleteCheckInOut();
        db.deleteAdhocOrderFinal();
        db.deleteNoOrder();
        db.deleteDSREntry();
        db.deleteNewAddedCustomer();
        db.deleteMarkAttendance();
        // db.deleteLocalconvenienceDetail();
        db.deleteComplaintInprocessAction();
      /*  db.deleteCustomerComplaintHeader();
        db.deleteCustomerComplaintDetail();*/
        db.deleteClouserComplaint();
        db.deleteComplaintImage();
        db.deleteComplaintAudio();
        db.deleteComplaintStart();
        db.deleteImage2();
        db.deleteReviewCmpImages();

    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!checkPermissions()) {
                            requestPermissions();
                        } else if (mService != null) { // add null checker
                            mService.requestLocationUpdates();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        mContex.bindService(new Intent(mContex, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(mContex).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(mContex).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            mContex.unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                //Toast.makeText(MainActivity.this, Utils.getLocationText(location),
                //      Toast.LENGTH_SHORT).show();
            }
        }
    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContex);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) mContex, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                                findViewById(R.id.lin1),
                                R.string.permission_denied_explanation,
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    private boolean checkPermissions() {

        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.lin1),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity1.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity1.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
