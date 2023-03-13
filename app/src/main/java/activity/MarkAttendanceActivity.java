package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;

import backgroundservice.SyncDataService;
import bean.AttendanceBean;
import bean.LoginBean;
import database.DatabaseHelper;
import other.CameraUtils;

public class MarkAttendanceActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE ;
    Context mContext;
    String Attendance_Mark = "", latLong;
    DatabaseHelper db = null;
    Button in_attendance, out_attendance;
    TextView tv_intime, tv_outtime;
    String Userid;
    AttendanceBean attendanceBean;
    public static final int BITMAP_SAMPLE_SIZE = 4;
    CustomUtility customutility;
    DatabaseHelper dataHelper = null;
    GPSTracker gps;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Toolbar mToolbar;
    private Uri fileUri; // file url to store image


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mark Attendance");
        dataHelper = new DatabaseHelper(this);



        customutility = new CustomUtility();
        db = new DatabaseHelper(mContext);

        Userid = LoginBean.getUseid();
        in_attendance = (Button) findViewById(R.id.in_attendance);
        out_attendance = (Button) findViewById(R.id.out_attendance);
        tv_intime = (TextView) findViewById(R.id.tv_intime);
        tv_outtime = (TextView) findViewById(R.id.tv_outtime);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");
        LoginBean.username = pref.getString("key_ename", "username");

        getAttendnace();

        tv_intime.setText(attendanceBean.SERVER_DATE_IN + "  " + attendanceBean.IN_TIME);
        tv_outtime.setText(attendanceBean.SERVER_DATE_OUT + "  " + attendanceBean.OUT_TIME);

        if (TextUtils.isEmpty(attendanceBean.IN_TIME)) {
            tv_intime.setVisibility(View.INVISIBLE);
        } else {
            tv_intime.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(attendanceBean.OUT_TIME)) {
            tv_outtime.setVisibility(View.INVISIBLE);
        } else {
            tv_outtime.setVisibility(View.VISIBLE);
        }

        in_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attendanceBean =  db.getMarkAttendanceByDate(customutility.getCurrentDate(),Userid);

                if (!hasPermissions(mContext, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MarkAttendanceActivity.this, PERMISSIONS, PERMISSION_ALL);
                } else {

                    if (isValidate() && CustomUtility.CheckGPS(mContext)) {

                        if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
                            Attendance_Mark = "IN";

                            CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

                            openCamera();
                        } else {
                            in_attendance.setEnabled(false);
                            CustomUtility.ShowToast("In Attendance Already Marked", mContext);
                        }
                    }
                }
            }
        });

        out_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                attendanceBean =  db.getMarkAttendanceByDate(customutility.getCurrentDate(),Userid);


                if (!hasPermissions(mContext, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MarkAttendanceActivity.this, PERMISSIONS, PERMISSION_ALL);
                } else {

                    if (isValidate() && CustomUtility.CheckGPS(mContext)) {

                        if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
                            CustomUtility.ShowToast("Please Mark In Attendance First.", mContext);
                        } else {

                            // dsr validation
                            if (check_dsr_entry()) {

                                if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_OUT)) {
                                    out_attendance.setEnabled(true);
                                    Attendance_Mark = "OUT";

                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 200;
                                    openCamera();


                                } else {
                                    out_attendance.setEnabled(false);
                                    in_attendance.setEnabled(false);
                                    CustomUtility.ShowToast("Attendance Already Marked", mContext);
                                }
                            }
                        }

                    }

                }

            }
        });

    }

    public boolean check_dsr_entry() {

        if (!TextUtils.isEmpty(attendanceBean.SERVER_DATE_OUT)) {
            return true;
        }

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + dataHelper.TABLE_DSR_ENTRY
                    + " WHERE " + dataHelper.KEY_PERNR + " = '" + LoginBean.userid + "'"
                    + " AND " + DatabaseHelper.KEY_BUDAT + " = '" + customutility.getCurrentDate() + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() <= 0) {
                //  Toast.makeText(this,"Please Enter DSR Comment First",Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("DSR alert !");
                alertDialog.setMessage("Please Fill DSR Entry");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


                return false;
            }


        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }


        return true;
    }

    public boolean isValidate() {
        if (CustomUtility.isDateTimeAutoUpdate(mContext)) {

        } else {
            CustomUtility.showSettingsAlert(mContext);
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {//  RouteActivity.this.finish();
            onBackPressed();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveLocally() {


        gps = new GPSTracker(mContext);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        latLong = "" + latitude + "," + longitude;

        DatabaseHelper dataHelper = new DatabaseHelper(mContext);


        attendanceBean.PERNR = LoginBean.getUseid();
        attendanceBean.BEGDA = customutility.getCurrentDate();
        attendanceBean.SERVER_DATE_IN = customutility.getCurrentDate();
        attendanceBean.SERVER_TIME_IN = customutility.getCurrentTime();
        attendanceBean.IN_ADDRESS = "";
        attendanceBean.IN_TIME = customutility.getCurrentTime();
        attendanceBean.SERVER_DATE_OUT = "";
        attendanceBean.SERVER_TIME_OUT = "";
        attendanceBean.OUT_ADDRESS = "";
        attendanceBean.OUT_TIME = "";
        attendanceBean.WORKING_HOURS = "";
        attendanceBean.IMAGE_DATA = "";
        attendanceBean.CURRENT_MILLIS = System.currentTimeMillis();
        attendanceBean.IN_LAT_LONG = latLong;
        attendanceBean.OUT_LAT_LONG = "";
        attendanceBean.OUT_FILE_NAME = "";
        attendanceBean.OUT_FILE_LENGTH = "";
        attendanceBean.OUT_FILE_VALUE = "";

        dataHelper.insertMarkAttendance(attendanceBean);
        SyncAttendanceInBackground();

        new Capture_employee_gps_location(this, "2","");

        Toast.makeText(mContext, "In Attendance Marked", Toast.LENGTH_LONG).show();



    }



    public void SyncAttendanceInBackground() {
        Intent i = new Intent(MarkAttendanceActivity.this, SyncDataService.class);
        startService(i);
    }

    void updateLocally() {

        DatabaseHelper dataHelper = new DatabaseHelper(mContext);

        attendanceBean.PERNR = LoginBean.getUseid();
        attendanceBean.BEGDA = customutility.getCurrentDate();
        attendanceBean.SERVER_DATE_OUT = customutility.getCurrentDate();
        attendanceBean.SERVER_TIME_OUT = customutility.getCurrentTime();
        attendanceBean.OUT_ADDRESS = "";

        attendanceBean.OUT_TIME = customutility.getCurrentTime();
        attendanceBean.IMAGE_DATA = "";
        attendanceBean.OUT_LAT_LONG = latLong;


        dataHelper.updateMarkAttendance(attendanceBean);
        SyncAttendanceInBackground();
        //Out Attendance
        new Capture_employee_gps_location(this, "3", "");
        Toast.makeText(mContext, "Out Attendance Marked", Toast.LENGTH_LONG).show();

    }


     /***************************** new camera code *****************************************/

    public void openCamera() {

        if (CameraUtils.checkPermissions(mContext)) {
            camraLauncher.launch(new Intent(MarkAttendanceActivity.this, CameraActivity.class)
                    .putExtra("cust_name", LoginBean.getUsername())
                    .putExtra("FrontCamera","1"));

        }
    }
    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());
                            Bitmap bitmap = BitmapFactory.decodeFile(bundle.get("data").toString());
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();

                            if (CAMERA_CAPTURE_IMAGE_REQUEST_CODE == 100)   // 100 =  in attendance
                            {

                                attendanceBean.IN_IMAGE = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                saveLocally();

                                deleteDirectory(new File(bundle.get("data").toString()));
                            }


                            if (CAMERA_CAPTURE_IMAGE_REQUEST_CODE == 200)   // 200 =  out attendance
                            {
                                attendanceBean.OUT_IMAGE = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                updateLocally();

                                deleteDirectory(new File(bundle.get("data").toString()));
                            }

                          //  MarkAttendanceActivity.this.finish();
                        }

                    }
                }
            });



    public void getAttendnace() {
        attendanceBean = db.getMarkAttendanceByDate(customutility.getCurrentDate(),Userid);
    }

    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }

}
