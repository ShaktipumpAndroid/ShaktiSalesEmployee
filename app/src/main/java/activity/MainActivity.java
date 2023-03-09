package activity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import bean.LoginBean;
import database.DatabaseHelper;
import receiver.AlarmReceiver;
import syncdata.SyncDataToSAP_New;
import webservice.SAPWebService;
import webservice.WebURL;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private static String TAG = MainActivity.class.getSimpleName();
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
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MainActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private PendingIntent pendingIntent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContex = this;
        con = new SAPWebService();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        try {
            Intent i = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(pendingIntent); // cancel any existing alarms
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // int versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        LoginBean.userid = pref.getString("key_username", "userid");
        LoginBean.username = pref.getString("key_ename", "username");
        triggerAlarmManager();

/******* Create SharedPreferences *******/
        if (!pref.getString("key_sync_date", "date").equalsIgnoreCase(customUtility.getCurrentDate())) {
            download();
            editor.putString("key_sync_date", customUtility.getCurrentDate());  // Saving string
            editor.commit(); //
        } else {
            syncOfflineData("1");
            Toast.makeText(this, "Welcome,  " + LoginBean.getUsername() + " \n    App Version " + versionName, Toast.LENGTH_LONG).show();
        }
        TextView tv = (TextView) findViewById(R.id.ename);
        tv.setText("Welcome,  " + LoginBean.getUsername() + "   V " + versionName);
        delete_data_save_in_sap();
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
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Sync Offline Data !");
        GPSTracker gps = new GPSTracker(mContex);
        latitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude())));
        longitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude())));
        //  new Capture_employee_gps_location(mContex,"1","");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CustomUtility.isOnline(MainActivity.this)) {
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
                    ;


                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    ;


                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();


    }

    public void logOut() {


        progressDialog = ProgressDialog.show(MainActivity.this, "", "Sync Offline Data !");


        new Thread(new Runnable() {
            @Override
            public void run() {


                if (CustomUtility.isOnline(MainActivity.this)) {


//           new Capture_employee_gps_location(mContex, "16", "");

                    editor.putString("key_logout", "logout");
                    editor.commit();


                    new SyncDataToSAP_New().SendDataToSap(mContex);

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    ;

                    delete_data_save_in_sap();


//             new SyncDataToSAP_New().SendDataToSap(mContex);

//                // stop back ground services
//                stopService(new Intent(MainActivity.this, TimeService.class));
//
//                new DatabaseHelper(MainActivity.this).deleteLogin();
//                LoginBean.setLogin("", "");
//
//
//                /************ Deleting Key value from SharedPreferences *****************/
//                editor.remove("key_sync_date");
//                editor.remove("key_login");
//                editor.remove("key_username");
//                editor.remove("key_ename");
//                editor.remove("key_logout");
//
//                editor.commit(); // commit changes
                    //                 if ((progressDialog != null) && progressDialog.isShowing()) {

//                OnBackPressed();


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    new SyncDataToSAP_New().SendDataToSap(mContex);
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    OnBackPressed();
//                }
//            }).start();


                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    ;

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
        // String title = getString(R.string.app_name);
        //String title = "shakti_white";

        Intent intent = null;
        Context ctx = null;
        switch (position) {


            case 0:
                fragment = new HomeFragment();
                //   title = getString(R.string.title_home);
                break;
            case 1:

                intent = new Intent(MainActivity.this, AttendanceReportActivity.class);
                startActivity(intent);


                // title = getString(R.string.title_friends);
                break;

            case 2:

                intent = new Intent(MainActivity.this, DeviceStatusActivity.class);
                startActivity(intent);


                // title = getString(R.string.title_friends);
                break;
            case 3:
                intent = new Intent(MainActivity.this, RoutePlanSearchActivity.class);
                startActivity(intent);

                break;

            case 4:
                intent = new Intent(MainActivity.this, MaterialAnalysisActivity.class);
                startActivity(intent);

                break;


            case 5:

                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            Intent intent = new Intent(MainActivity.this, FileActivity.class);
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
                intent = new Intent(MainActivity.this, VideoSearchActivity.class);
                startActivity(intent);
                break;

            case 7:

                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity.this)) {
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
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebURL.PIPE_SELECTION));
                            startActivity(browserIntent);
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

            case 9:
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebURL.SOLAR_CABLE_SELECTION));
                            startActivity(browserIntent);
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
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity.this)) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.shaktipumps.com/faq.php"));
                            startActivity(browserIntent);
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
                if (CustomUtility.isOnline(MainActivity.this)) {
                    while (progressBarStatus < 100) {
                        // performing operation
                        try {
                            // Get Attendance Data
                            progressBarStatus = con.getAttendanceData(MainActivity.this);
                            progressBarStatus = 20;
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                            /************** sync material */
                            dataHelper.deleteAdhocOrder();
                            progressBarStatus = con.getMaterialDetail(MainActivity.this);
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                            /************** sync material */
                            // get target vs achievement
                            progressBarStatus = con.getTargetData(MainActivity.this);
                            // visit history
                            progressBarStatus = con.getVisitHistory(MainActivity.this);
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                            // get search help
                            progressBarStatus = con.getSearchHelp(MainActivity.this);
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                            // get route plan
                            progressBarStatus = con.getRouteDetail(MainActivity.this);
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
                    //                  performing operation if file is downloaded,
                    // sleeping for 1 second after operation completed
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

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("backgroundservice.TimeService".equals(service.service.getClassName())) {
                return true;
            }
            if ("backgroundservice.AndroidService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void delete_data_save_in_sap() {
        DatabaseHelper db = new DatabaseHelper(mContex);

        db.deleteSurvey();
        db.deleteEmployeeGPSActivity();
      //  db.deleteCheckInOut();
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

        // register GCM registration complete receiver
       /* LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));*/

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
       /* LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));*/

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
      /*  LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));*/

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
      /*  LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));*/

        // clear the notification area when the app is opened
        // NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /*public void showNotification(String message) {


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle("Shakti Sales Employee")
                        .setContentText(message);
//

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notificationIntent = new Intent(this, Notification.class);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

//        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }*/

    public void triggerAlarmManager() {
        try {

            int alarmTriggerTime = 10;
            // get a Calendar object with current time
            Calendar cal = Calendar.getInstance();
            // add alarmTriggerTime seconds to the calendar object
            cal.add(Calendar.SECOND, alarmTriggerTime);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
//        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

//        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
//                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);


            //public  final long INTERVAL_FIFTEEN_MINUTES = 15 * 60 * 1000;
            long INTERVAL_FIFTEEN_MINUTES = 15 * 60 * 1000;

//        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + alarmTriggerTime,
//                alarmTriggerTime , pendingIntent);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        //Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }

   /* private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


// read phone call log
                // insert_call_log();


//                if ( CustomUtility.isOnline(mContex)    ){
//
//                    try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
                StrictMode.setThreadPolicy(policy);
                newVersion = Jsoup.connect(
                        "https://play.google.com/store/apps/details?id=com.shaktipumps.shakti.shaktisalesemployee&hl=en")
                        .timeout(30000)
                        .referrer("http://www.google.com").get()
                        .select("div[itemprop=softwareVersion]").first()
                        .ownText();


//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                Log.d("newVersion", newVersion + "--" + versionName);
                if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                    MainActivity.this.finish();
                    Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                    startActivity(i);

                }


//                }


            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
        }


    }*/


   /*  public void insert_call_log() {


//        DatabaseHelper db = new DatabaseHelper(mContex);
//
//        StringBuffer sb = new StringBuffer();
//        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
//        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
//        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
//        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
//        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
//        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
//
//        while (managedCursor.moveToNext()) {
//
//            String phNumber = managedCursor.getString(number);
//
//            String callType = managedCursor.getString(type);
//            String callDate = managedCursor.getString(date);
//
//            String  phName = managedCursor.getString(name)+"  ";
//
//
//            Date callDayTime = new Date(Long.valueOf(callDate));
//
//            String callDuration = managedCursor.getString(duration);
//            String dir = null;
//
//
//            SimpleDateFormat simpleDateFormat  ;
//
//
//            simpleDateFormat =  new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
//
//            current_date = simpleDateFormat.format(callDayTime);
//
//            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//            current_time = simpleDateFormat.format(callDayTime);
//
//
//
//
//if (current_date.equalsIgnoreCase( new CustomUtility().getCurrentDate()))
//{
//
//    int dircode = Integer.parseInt(callType);
//
//    switch (dircode) {
//        case CallLog.Calls.OUTGOING_TYPE:
//            dir = "OUTGOING";
//            break;
//        case CallLog.Calls.INCOMING_TYPE:
//            dir = "INCOMING";
//            break;
//
//        case CallLog.Calls.MISSED_TYPE:
//            dir = "MISSED";
//            break;
//
//    }
//
//
//    GPSTracker gps = new GPSTracker(this);
//    double latitude = gps.getLatitude();
//    double longitude = gps.getLongitude();
//
//
//    db.insertCallLog(
//            LoginBean.userid,
//            phNumber,
//            phName,
//            current_date,
//            current_time,
//            dir,
//            callDuration,
//            String.valueOf(latitude) + "," + String.valueOf(longitude)
//    );
//
//
//}
//
//        }


    }*/
}
