package activity;

/**
 * Created by shakti on 10/3/2016.
 */

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import activity.complaint.ComplaintDashboard;
import bean.LocalConvenienceBean;
import bean.LoginBean;
import bean.vkbean.LatLongBeanVK;
import database.DatabaseHelper;
import models.DistanceResponse;
import models.Element;
import other.CameraUtils;
import rest.DistanceApiClient;
import rest.RestUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import webservice.CustomHttpClient;
import webservice.WebURL;


//import com.shaktipumps.shakti.material_design.R;


/*public class HomeFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {*/

@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ProgressDialog progressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    TextView btn_unsync;
    TextView tv_notification, start, end, offlinedata;

    boolean start_photo_flag = false,
            end_photo_flag = false;

    float mFlotDistanceKM = 0;
    String allLatLong = "";

    String count_dsr_entry = "",
            count_frwdapp_entry = "",
            count_order = "",
            count_no_order = "",
            count_survey = "",
            count_add_new_customer = "",
            count_check_in_out = "",
            count_attendance = "",
            count_clouser_complaint,
            count_pending_complaint;

    String from_lat;
    String from_lng;
    String to_lat;
    String to_lng;

    String start_photo_text, end_photo_text;
    public static final int BITMAP_SAMPLE_SIZE = 8;

    LocalConvenienceBean localConvenienceBean;
    LocationManager locationManager;

    List<LatLongBeanVK> mLatLongBeanVKList;

    private final NewLocationUpdatesService mNewLocationUpdatesService = null;

    Button btn_route, btn_target, btn_dsr, btn_report, btn_attendance, btn_adhoc_order, btn_complaint, btn_goods_recp, btn_goods_iss, btn_goods_can, btn_stk_det_rep;
    Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
        }
    };

    String current_start_date, current_end_date, current_start_time, current_end_time;

    FusedLocationProviderClient fusedLocationClient;
    protected LocationRequest locationRequest;
    protected Location location;

    private CustomUtility customutility = null;

    DatabaseHelper dataHelper;
    String fullAddress = null;
    String fullAddress1 = null;
    String distance1 = null;
    String startphoto = null;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 100;
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static String imageStoragePath;
    TextView photo1, photo2;

    LoginBean lb;
    String mUserID;

    public HomeFragment() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customutility = new CustomUtility();
        progressDialog = new ProgressDialog(getActivity());
        dataHelper = new DatabaseHelper(getActivity());
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        mUserID = pref.getString("key_username", "userid");

        lb = new LoginBean();
        mLatLongBeanVKList = new ArrayList<>();
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!checkPlayServices()) {

            Toast.makeText(getActivity(), "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }
        //InappUpdate
        appUpdateManager = AppUpdateManagerFactory.create(getActivity());
        checkUpdate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        deleteCache(getActivity());


        btn_route = rootView.findViewById(R.id.btn_route);

        btn_target = rootView.findViewById(R.id.btn_target);

        btn_dsr = rootView.findViewById(R.id.dsr_entry);

        btn_adhoc_order = rootView.findViewById(R.id.adhoc_order);

        btn_report = rootView.findViewById(R.id.report);


        btn_unsync = rootView.findViewById(R.id.unsync_data);

        btn_attendance = rootView.findViewById(R.id.btn_attendance);

        tv_notification = rootView.findViewById(R.id.tv_notification);

        start = rootView.findViewById(R.id.start);
        end = rootView.findViewById(R.id.end);
        offlinedata = rootView.findViewById(R.id.offlinedata);

        btn_complaint = rootView.findViewById(R.id.complaint);

        btn_goods_recp = rootView.findViewById(R.id.goods_recp);
        btn_goods_iss = rootView.findViewById(R.id.goods_iss);
        btn_goods_can = rootView.findViewById(R.id.goods_trans_det);
        btn_stk_det_rep = rootView.findViewById(R.id.stck_det_rep);
        //System.out.println();

        start.setOnClickListener(this);
        end.setOnClickListener(this);
        offlinedata.setOnClickListener(this);

        if (CustomUtility.getSharedPreferences(getActivity(), "localconvenience").equalsIgnoreCase("0")) {
            changeButtonVisibility(false, 0.5f, end);
        } else {
            changeButtonVisibility(false, 0.5f, start);
        }

        // get unsync data count

        tv_notification.setText(String.valueOf(getUnsyncData()));
        if (getUnsyncData() <= 0) {
            tv_notification.setTextColor(Color.parseColor("#ffffff"));
        }


        btn_attendance.setOnClickListener(view -> {


            if (CustomUtility.CheckGPS(getActivity()) && validateDate()) {
                Intent intent = new Intent(getActivity(), MarkAttendanceActivity.class);
                startActivity(intent);
            }
        });

        btn_route.setOnClickListener(view -> {
            //getDistanceFromGoogleAPI();
            if (CustomUtility.CheckGPS(getActivity()) && validateDate()) {
                Intent intent = new Intent(getActivity(), RouteActivity.class);
                startActivity(intent);
            }
        });

        btn_target.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TargetActivity.class);
            startActivity(intent);
        });

        btn_dsr.setOnClickListener(view -> {
            if (CustomUtility.CheckGPS(getActivity()) && validateDate()) {
                Intent intent = new Intent(getActivity(), DsrEntryActivity.class);
                startActivity(intent);
            }
        });

        btn_adhoc_order.setOnClickListener(view -> {
            if (CustomUtility.CheckGPS(getActivity()) && validateDate()) {
                Intent intent = new Intent(getActivity(), AdhocOrderActivity.class);
                startActivity(intent);
            }
        });

        btn_complaint.setOnClickListener(view -> {
            if (validateDate()) {
                Intent intent = new Intent(getActivity(), ComplaintDashboard.class);
                startActivity(intent);
            }
        });

        btn_goods_recp.setOnClickListener(view -> {
            if (CustomUtility.isOnline(getActivity())) {
                Intent intent = new Intent(getActivity(), GoodsReceipt_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Please check your internet connection, and try again...", Toast.LENGTH_SHORT).show();
            }
        });

        btn_goods_iss.setOnClickListener(view -> {
            if (CustomUtility.isOnline(getActivity())) {
                Intent intent = new Intent(getActivity(), GoodsIssue_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Please check your internet connection, and try again...", Toast.LENGTH_SHORT).show();
            }
        });

        btn_goods_can.setOnClickListener(view -> {
            if (CustomUtility.isOnline(getActivity())) {
                Intent intent = new Intent(getActivity(), GoodsTransDet_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Please check your internet connection, and try again...", Toast.LENGTH_SHORT).show();
            }
        });

        btn_stk_det_rep.setOnClickListener(view -> {
            if (CustomUtility.isOnline(getActivity())) {
                Intent intent = new Intent(getActivity(), StockDetailRep_Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Please check your internet connection, and try again...", Toast.LENGTH_SHORT).show();
            }
        });

        btn_unsync.setOnClickListener(view -> {
            count_dsr_entry = "";
            count_attendance = "";
            count_check_in_out = "";
            count_survey = "";
            count_no_order = "";
            count_order = "";
            count_add_new_customer = "";
            getUnsyncData();
            //Log.d("count_dsr_entry",count_dsr_entry);
            Intent intent = new Intent(getActivity(), UnsyncdataApplication.class);
            intent.putExtra("count_dsr_entry", count_dsr_entry);
            intent.putExtra("count_attendance", count_attendance);
            intent.putExtra("count_frwd_app", count_frwdapp_entry);
            intent.putExtra("count_check_in_out", count_check_in_out);
            intent.putExtra("count_survey", count_survey);
            intent.putExtra("count_no_order", count_no_order);
            intent.putExtra("count_order", count_order);
            intent.putExtra("count_add_new_customer", count_add_new_customer);
            intent.putExtra("count_clouser_complaint", count_clouser_complaint);
            intent.putExtra("count_pending_complaint", count_pending_complaint);
            startActivity(intent);
        });

        btn_report.setOnClickListener(view -> {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading..");
            new Thread(() -> {
                if (CustomUtility.isOnline(getActivity())) {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    try {
                        Intent mIntent = new Intent(Intent.ACTION_VIEW);
                        mIntent.setData(Uri.parse(WebURL.DASHBOARD));
                        startActivity(mIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.obj = "Not found any Browser App";
                        mHandler.sendMessage(msg);
                    }
                } else {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Message msg = new Message();
                    msg.obj = "No Internet Connection Found, You Are In Offline Mode.";
                    mHandler.sendMessage(msg);
                }
            }).start();
        });
        return rootView;
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
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    private boolean validateDate() {
        if (!CustomUtility.isDateTimeAutoUpdate(getActivity())) {

            CustomUtility.showSettingsAlert(getActivity());
            return false;
        }
        return true;
    }

    public int getUnsyncData() {

        DatabaseHelper dh = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dh.getReadableDatabase();
        String userid = LoginBean.getUseid();
        String selectQuery ;
        Cursor cursor = null;
        int total_unsync = 0;

        //*****************************get dsr entry count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DSR_ENTRY
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = cursor.getCount();
                count_dsr_entry = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //*****************************get frwd app entry count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FRWD_APP_CMP
                    + " WHERE " + DatabaseHelper.KEY_AWT_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = cursor.getCount();
                count_frwdapp_entry = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //*****************************get attendance count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_MARK_ATTENDANCE
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_attendance = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end attendance count ****************************************/

       //*****************************get order count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ADHOC_FINAL
                    + " WHERE " + DatabaseHelper.KEY_PERSON + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_order = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end order count ****************************************/


        //*****************************get no order count ****************************************/
        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NO_ORDER
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                total_unsync = total_unsync + cursor.getCount();
                count_no_order = String.valueOf(cursor.getCount());

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end no count ****************************************/


        //*****************************get survey count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SURVEY
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_survey = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end survey count ****************************************/


        //*****************************get check in out count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CHECK_IN_OUT
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_check_in_out = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end check in out count ****************************************/


        //*****************************get Add new customer count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NEW_ADDED_CUSTOMER
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_add_new_customer = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** end Add new customer  count ****************************************/


        //***************************** get clouser request count ****************************************/
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CLOSE_COMPLAINT
                    + " WHERE " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                total_unsync = total_unsync + cursor.getCount();
                count_clouser_complaint = String.valueOf(cursor.getCount());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** get clouser request count ****************************************/


        //***************************** get pending complaint count ****************************************/
        try {


            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT
                    + " WHERE " + DatabaseHelper.KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                total_unsync = total_unsync + cursor.getCount();
                count_pending_complaint = String.valueOf(cursor.getCount());

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        //***************************** get pending complaint count  ****************************************/
        finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }


        return total_unsync;
    }

    @Override
    public void onResume() {

        // get unsync data count
        super.onResume();

        tv_notification.setText(String.valueOf(getUnsyncData()));
        if (getUnsyncData() <= 0) {
            tv_notification.setTextColor(Color.parseColor("#ffffff"));
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
            return checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    public void startLocationUpdates() {
        start_photo_text = "";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                final Double[] lat = new Double[1];
                final Double[] lng = new Double[1];
                from_lat = "";
                from_lng = "";
                to_lat = "";
                to_lng = "";
                fullAddress = "";
                fullAddress1 = "";
                try {
                    current_start_date = customutility.getCurrentDate();
                    current_start_time = customutility.getCurrentTime();
                    if (location != null) {
                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                        lat[0] = location.getLatitude();
                        lng[0] = location.getLongitude();
                    } else {
                        LocationCallback mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        //TODO: UI updates.
                                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                                        lat[0] = location.getLatitude();
                                        lng[0] = location.getLongitude();
                                    }
                                }
                            }
                        };

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, mLocationCallback, null);
                    }
                    progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait !");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something here
                            if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng)) {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.custom_dialog1);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setAttributes(lp);

                                final TextInputEditText etlat = dialog.findViewById(R.id.tiet_lat);
                                final TextInputEditText etlng = dialog.findViewById(R.id.tiet_lng);
                                final TextInputEditText etadd = dialog.findViewById(R.id.tiet_add);
                                final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                                final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                                photo1 = dialog.findViewById(R.id.photo1);
                                final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                                final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);

                                Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                if (location != null) {
                                    try {
                                        addresses = geo.getFromLocation(lat[0], lng[0], 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (addresses != null) {
                                    if (addresses.isEmpty()) {
                                        etadd.setText("Please try Again, Waiting for Location");
                                    } else {
                                        etadd.setText(addresses.get(0).getAddressLine(0));
                                    }
                                }

                                etlat.setText(from_lat);
                                etlng.setText(from_lng);

                                ettxt1.setText("Current Location");
                                ettxt2.setText(Html.fromHtml(getString(R.string.confirm)));

                                // Toast.makeText(getActivity(), "from_lat="+from_lat+"\nfrom_lng="+from_lng, Toast.LENGTH_SHORT).show();

                                photo1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (start_photo_text == null || start_photo_text.isEmpty()) {
                                            showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                                        }
                                    }
                                });

                                etcncl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                etconfm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mNewLocationUpdatesService != null) { // add null checker
                                            mNewLocationUpdatesService.requestLocationUpdates();
                                        }
//                                        if (CustomUtility.isOnline(getActivity())) {
                                        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(LoginBean.getUseid(),
                                                current_start_date,
                                                "",
                                                current_start_time,
                                                "",
                                                from_lat,
                                                "",
                                                from_lng,
                                                "",
                                                "",
                                                "",
                                                "",
                                                start_photo_text,
                                                ""
                                        );
                                        dataHelper.insertLocalconvenienceData(localConvenienceBean);
                                        //   startLocation();
                                        CustomUtility.setSharedPreference(getActivity(), "localconvenience", "1");
                                        changeButtonVisibility(false, 0.5f, start);
                                        changeButtonVisibility(true, 1f, end);
                                        Toast.makeText(getActivity(), "Your Journey will be Started...", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
//                                        } else {
//                                            Toast.makeText(getActivity(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
//                                        }
                                    }
                                });
                                dialog.show();
                            } else {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                Toast.makeText(getActivity(), "Please wait for your current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog != null)
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                }
            }
        });
    }

    public float getDistanceFromGoogleAPI(String allLatLong) {
        float fFinalDistance = 0;
        String url = "null";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        if (CustomUtility.isOnline(getActivity())) {
            try {
//********************************** check app version ********************************************
                //********************************** check logout session ********************************************
                ///   url = WebURL.DISTANCE_MAP_CAL_API + "77.227434,28.610981;77.212021,28.616679"+"?alternatives=true&rtype=0&geometries=polyline&overview=full&exclude=&steps=true&region=ind";
                url = WebURL.DISTANCE_MAP_CAL_API + allLatLong + "?alternatives=true&rtype=0&geometries=polyline&overview=full&exclude=&steps=true&region=ind";

                System.out.println("Vikas_url==>>" + url);
                /*url = WebURL.DISTANCE_MAP_CAL_API + "?MUserId=" + pref.getString("key_muserid", "invalid_muserid") +
                        "&ClientId=" + clientid;*/
//************************************ get device details ******************************************

                String obj = CustomHttpClient.executeHttpGet(url);
                Log.d("home_url", "" + url);
                Log.d("home_obj", "" + obj);
                if (obj != null) {
                    try {
                        JSONObject joJ = new JSONObject(obj);
                        String jo11Routes = joJ.getString("routes");

                        JSONArray ja = new JSONArray(jo11Routes);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject join = ja.getJSONObject(i);
                            String mDistanceMeter = join.getString("distance");
                            float mfmf = Float.parseFloat(mDistanceMeter);
                            float fFFF = (float) (mfmf * 0.001);
                            fFinalDistance = fFFF;
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progressDialog.dismiss();
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fFinalDistance;
    }

    public void startLocationUpdates1() {
        end_photo_text = "";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                from_lat = " ";
                from_lng = " ";
                to_lat = " ";
                to_lng = " ";
                fullAddress = "";
                fullAddress1 = "";
                startphoto = "";
                try {
                    localConvenienceBean = dataHelper.getLocalConvinienceData();
                    current_start_date = localConvenienceBean.getBegda();
                    current_start_time = localConvenienceBean.getFrom_time();

                    current_end_date = customutility.getCurrentDate();
                    current_end_time = customutility.getCurrentTime();

                    from_lat = localConvenienceBean.getFrom_lat();
                    from_lng = localConvenienceBean.getFrom_lng();
                    startphoto = localConvenienceBean.getPhoto1();
                    if (location != null) {
                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                    } else {
                        LocationCallback mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                                    }
                                }
                            }
                        };

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, mLocationCallback, null);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                fullAddress = localConvenienceBean.getStart_loc();
                if (CustomUtility.isOnline(getActivity())) {
                    if (mLatLongBeanVKList.size() > 0)
                        mLatLongBeanVKList.clear();
                    progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait !");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng) && !TextUtils.isEmpty(to_lat) && !TextUtils.isEmpty(to_lng)) {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                getDistanceInfo(from_lat, from_lng, to_lat, to_lng, startphoto, mFlotDistanceKM, allLatLong);
                            } else {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                Toast.makeText(getActivity(), "Please wait for your current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);
                    if (mNewLocationUpdatesService != null) { // add null checker
                        mNewLocationUpdatesService.removeLocationUpdates();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Connect to Internet...,Your Data is Saved to the Offline Mode.", Toast.LENGTH_SHORT).show();
                    LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(LoginBean.getUseid(), current_start_date,
                            current_end_date,
                            current_start_time,
                            current_end_time,
                            from_lat,
                            to_lat,
                            from_lng,
                            to_lng,
                            fullAddress,
                            fullAddress1,
                            distance1,
                            startphoto,
                            end_photo_text
                    );
                    dataHelper.updateLocalconvenienceData(localConvenienceBean);
                    CustomUtility.setSharedPreference(getActivity(), "localconvenience", "0");
                    changeButtonVisibility(false, 0.5f, end);
                    changeButtonVisibility(true, 1f, start);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start:
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    if (CustomUtility.isOnline(getActivity())) {
                    startLocationUpdates();
//                    } else {
//                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    buildAlertMessageNoGps();
                }

                break;

            case R.id.end:
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startLocationUpdates1();
                } else {
                    buildAlertMessageNoGps1();
                }
                break;

            case R.id.offlinedata:
                Intent intnt = new Intent(getActivity(), OfflineDataConveyance.class);
                startActivity(intnt);
                break;
        }
    }

    private void buildAlertMessageNoGps() {
//        if (CustomUtility.isOnline(getActivity())) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        startLocationUpdates();
//                        startLocationUpdates1();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
//        } else {
//            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }
    }

    private void buildAlertMessageNoGps1() {
        if (CustomUtility.isOnline(getActivity())) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                           /* android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
                            // Setting Dialog Title
                            alertDialog.setTitle("Confirmation");
                            // Setting Dialog Message
                            alertDialog.setMessage("Press Confirm will start your Journey");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
*/
                            //getGpsLocation();
                            //startLocationUpdates1();
/*

                                }
                            });
                            // on pressing cancel button
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

*/
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    getActivity().finish();
                    System.exit(0);

                }
                break;
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(getActivity()).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } /*else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }*/

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void changeButtonVisibility(boolean state, float alphaRate, TextView txtSubmiteOrderID) {
        txtSubmiteOrderID.setEnabled(state);
        txtSubmiteOrderID.setAlpha(alphaRate);
    }


    private void getDistanceInfo(String lat1, String lon1, String lat2, String lon2, String strtpht, float mFlotDistanceKM, String allLatLong) {
        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY


        Map<String, String> mapQuery = new HashMap<>();

        mapQuery.put("origins", lat1 + "," + lon1);
        mapQuery.put("destinations", lat2 + "," + lon2);
        mapQuery.put("units", "metric");
        mapQuery.put("mode", "driving");
        mapQuery.put("key", "AIzaSyAohhwZ11LRwoxsS8lJ0VHGkA4L-cwjWmw");

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                if (response.body() != null &&
                        response.body().getRows() != null &&
                        response.body().getRows().size() > 0 &&
                        response.body().getRows().get(0) != null &&
                        response.body().getRows().get(0).getElements() != null &&
                        response.body().getRows().get(0).getElements().size() > 0 &&
                        response.body().getRows().get(0).getElements().get(0) != null &&
                        response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
                        response.body().getRows().get(0).getElements().get(0).getDuration() != null) {

                    try {

                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        ;

                        Element element = response.body().getRows().get(0).getElements().get(0);
                        fullAddress = response.body().getOriginAddresses().get(0);
                        fullAddress1 = response.body().getDestinationAddresses().get(0);
                        distance1 = element.getDistance().getText();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog2);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);

                        final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                        final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                        final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                        final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                        final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                        final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                        final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                        final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);
                        final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                        final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                        final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                        final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                        photo2 = dialog.findViewById(R.id.photo2);
                        ettrvlmod.requestFocus();

                        etstrdt.setText(current_start_date + " " + current_start_time);
                        etstrlatlng.setText(from_lat + "," + from_lng);
                        etenddt.setText(current_end_date + " " + current_end_time);
                        etendlatlng.setText(to_lat + "," + to_lng);
                        etstrlocadd.setText(fullAddress);
                        etendlocadd.setText(fullAddress1);
                        ettotdis.setText(distance1);

                        ettxt1.setText("Local Conveyance Details");
                        ettxt2.setText("Press Confirm will end your Journey");

                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (end_photo_text == null || end_photo_text.isEmpty()) {

                                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                                }
                            }
                        });

                        etcncl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        etconfm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String travel_mode = ettrvlmod.getText().toString();

                                if (CustomUtility.isOnline(getActivity())) {
                                    if (!TextUtils.isEmpty(travel_mode) && !travel_mode.equals("")) {

                                        progressDialog = ProgressDialog.show(getActivity(), "", "Sending Data to server..please wait !");

                                        new Thread(new Runnable() {
                                            public void run() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(LoginBean.getUseid(), current_start_date,
                                                                current_end_date,
                                                                current_start_time,
                                                                current_end_time,
                                                                from_lat,
                                                                to_lat,
                                                                from_lng,
                                                                to_lng,
                                                                fullAddress,
                                                                fullAddress1,
                                                                distance1,
                                                                startphoto,
                                                                end_photo_text
                                                        );

                                                        dataHelper.updateLocalconvenienceData(localConvenienceBean);

                                                        // SyncLocalConveneinceDataToSap(travel_mode, current_end_date, current_end_time, mFlotDistanceKM, allLatLong);
//                                                        SyncLocalConveneinceDataToSap(travel_mode, current_start_date, current_start_time, distance1, allLatLong);
                                                        SyncLocalConveneinceDataToSap(travel_mode, current_end_date, current_end_time, distance1, allLatLong);
                                                    }
                                                });
                                            }

                                            ;
                                        }).start();

                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(getActivity(), "Please Enter Travel Mode.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        dialog.show();

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        ;
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

                Log.e("Failed", "&&&", t);

                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                ;

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
       /* location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Log.e("Latitude : ","Longitued" + location.getLatitude()+"  "+location.getLongitude());
        }*/

    }

    // public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm, float mFlotDistanceKM, String allLatLong)
    public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm, String mFlotDistanceKM, String allLatLong) {

        String docno_sap = null;
        String invc_done = null;

        DatabaseHelper db = new DatabaseHelper(this.getActivity());

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        param_invc = db.getLocalConvinienceData(endat, endtm);

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", CustomUtility.formateDate(param_invc.getBegda()));
            jsonObj.put("endda", CustomUtility.formateDate(param_invc.getEndda()));

            jsonObj.put("start_time", CustomUtility.formateTime(param_invc.getFrom_time()));
            jsonObj.put("end_time", CustomUtility.formateTime(param_invc.getTo_time()));

            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            jsonObj.put("start_location", param_invc.getStart_loc());
            jsonObj.put("end_location", param_invc.getEnd_loc());
            // jsonObj.put("distance", param_invc.getDistance());
            jsonObj.put("distance", mFlotDistanceKM);
            jsonObj.put("TRAVEL_MODE", mode);
            jsonObj.put("LAT_LONG", allLatLong);
            jsonObj.put("PHOTO1", param_invc.getPhoto1());
            jsonObj.put("PHOTO2", param_invc.getPhoto2());

            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));


        //System.out.println(param1_invc.toString());

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.LOCAL_CONVENIENVCE, param1_invc);

            if (obj2 != "") {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");
                    if (invc_done.equalsIgnoreCase("S")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail(endat, endtm);
                        CustomUtility.setSharedPreference(getActivity(), "localconvenience", "0");
                        changeButtonVisibility(false, 0.5f, end);
                        changeButtonVisibility(true, 1f, start);

                    } else if (invc_done.equalsIgnoreCase("E")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            ;
        }
    }

    @Override
    public void onDestroy() {


        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {


        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onStop();
    }


    public void showConfirmationGallery(final String keyimage, final String name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, (IntentSenderForResultStarter) this, HomeFragment.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        Bitmap bitmap ;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {

                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                    if (start_photo_flag) {
                        start_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO1);
                    }

                    if (end_photo_flag) {
                        end_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO2);
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                File file = new File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getActivity(), "Update success!" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }

    }

    public void setIcon(String key) {

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (start_photo_text == null || start_photo_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (end_photo_text == null || end_photo_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


        }

    }


    public void setFlag(String key) {
        start_photo_flag = false;
        end_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                start_photo_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                end_photo_flag = true;
                break;

        }

    }

    public void openCamera(String keyimage) {

        if (CameraUtils.checkPermissions(getActivity())) {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

            Log.e("PATH", "&&&" + fileUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }


}