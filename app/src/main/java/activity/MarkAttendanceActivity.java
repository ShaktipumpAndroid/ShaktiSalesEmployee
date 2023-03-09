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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import backgroundservice.SyncDataService;
import bean.AttendanceBean;
import bean.LoginBean;
import database.DatabaseHelper;
import other.CameraUtils;
import webservice.WebURL;

public class MarkAttendanceActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE ;
    Context mContext;
    String Attendance_Mark = "", latLong;
    DatabaseHelper db = null;
    String imageStoragePath;
    Button in_attendance, out_attendance;
    TextView tv_intime, tv_outtime;
    Button sync;
    File mFile;
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
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MarkAttendanceActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;

    private Uri fileUri; // file url to store image
    private Uri NewfileUri; // file url to store image

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                WebURL.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.US).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }


        return mediaFile;
    }

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


        //  sync = (Button) findViewById(R.id.sync);

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
        // Sync Data

        new Capture_employee_gps_location(this, "2","");


        Toast.makeText(mContext, "In Attendance Marked", Toast.LENGTH_LONG).show();



    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_FIRST_USER) {
//            String url = data.getStringExtra("url");
//            if (requestCode == 100) {
//                if (!TextUtils.isEmpty(url)) {
//                    try {
//                        mFile = new File(url);
//                        boolean hi = mFile.isFile();
//                        String path = mFile.getPath();
//
//
//                        if (Attendance_Mark.equals("IN")) {
//
//
//                            attendanceBean.IN_FILE_NAME = mFile.getName();
//                            attendanceBean.IN_FILE_VALUE = mFile.getPath();
//
//// Log.d("in_file_name", "" + attendanceBean.IN_FILE_NAME + "----" + attendanceBean.IN_FILE_VALUE);
//
//                            if (mFile.getPath() != null) {
//
//
////
////                                BitmapFactory.Options options = new BitmapFactory.Options();
//////                                // down sizing image as it throws OutOfMemory Exception for larger
//////                                // images
////                                options.inSampleSize = 8;
//////
////                                final Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath(), options);
//////
//////
////////                                //            imgPreview.setImageBitmap(bitmap);
//////
////                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                                byte[] byteArray = stream.toByteArray();
////
////                                String encodedImage = Base64.encodeToString(byteArray , Base64.DEFAULT);
//
//                               /* convert image to string*/
//                                attendanceBean.IN_IMAGE = CustomUtility.CompressImage(mFile.getPath()) ;
//
//                              //  Log.d("in_image", "" + attendanceBean.IN_IMAGE  );
//
//
//                                File file = new File( mFile.getPath() );
//                                if ( file.exists())
//                                {
//                                    file.delete();
//                                }
//
//
//                            }
//
//                           // Log.d("in_file_name", "" + attendanceBean.IN_FILE_NAME + "----" + attendanceBean.IN_FILE_VALUE);
//
//
//                            saveLocally();
//                        }
//
//
//
//
//
//                        if (Attendance_Mark.equals("OUT")) {
//
//                            attendanceBean.OUT_FILE_NAME = mFile.getName();
//                            attendanceBean.OUT_FILE_VALUE = mFile.getPath();
//
//                          //  Log.d("out_file_name", "" + attendanceBean.OUT_FILE_NAME + "----" + attendanceBean.OUT_FILE_VALUE);
//
//
//
//                               /* convert image to string*/
//                            attendanceBean.OUT_IMAGE = CustomUtility.CompressImage(mFile.getPath()) ;
//
//                           // Log.d("out_image", "" + attendanceBean.OUT_IMAGE  );
//
//                            updateLocally();
//
//
//                            File file = new File( mFile.getPath() );
//                            if ( file.exists())
//                            {
//                                file.delete();
//                            }
//
//
//
//                        }
//
//
//                        saveToDB(path);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }
//    }

    public void SyncAttendanceInBackground() {


        Intent i = new Intent(MarkAttendanceActivity.this, SyncDataService.class);
        startService(i);


//
//        progressDialog = ProgressDialog.show(MarkAttendanceActivity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(MarkAttendanceActivity.this))
//                {
//
//                    Intent i = new Intent(MarkAttendanceActivity.this, SyncDataService.class);
//                    i.putExtra("sync_data", "sync_mark_attendance");
//                    startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "Attendance Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . Attendance saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();


    }

    void updateLocally() {

        DatabaseHelper dataHelper = new DatabaseHelper(mContext);

        // Log.d("mark_person1",""+ LoginBean.getUseid());

        attendanceBean.PERNR = LoginBean.getUseid();
        attendanceBean.BEGDA = customutility.getCurrentDate();
        attendanceBean.SERVER_DATE_OUT = customutility.getCurrentDate();
        attendanceBean.SERVER_TIME_OUT = customutility.getCurrentTime();
        //String latlong[] = latLong.split(",");

        //attendanceBean.OUT_ADDRESS = getCompleteAddressString(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        attendanceBean.OUT_ADDRESS = "";


        attendanceBean.OUT_TIME = customutility.getCurrentTime();
       /* long attendanceDifference = System.currentTimeMillis() - attendanceBean.CURRENT_MILLIS;
        long second = (attendanceDifference / 1000) % 60;
        long minute = (attendanceDifference / (1000 * 60)) % 60;
        long hour = (attendanceDifference / (1000 * 60 * 60)) % 24;
        String time = String.format(Locale.US,"%02d:%02d:%02d", hour, minute, second);
        attendanceBean.WORKING_HOURS = time;*/
        attendanceBean.IMAGE_DATA = "";
        attendanceBean.OUT_LAT_LONG = latLong;
        // attendanceBean.OUT_FILE_NAME = mFile.getName();
        // attendanceBean.OUT_FILE_VALUE = mFile.getPath();


        dataHelper.updateMarkAttendance(attendanceBean);
        SyncAttendanceInBackground();
        //Out Attendance
        new Capture_employee_gps_location(this, "3", "");

        //   MainActivity.mainActivity.mydb.updateAttendance(attendanceBean);

        Toast.makeText(mContext, "Out Attendance Marked", Toast.LENGTH_LONG).show();

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.US);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //   Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                //     Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //    Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    void saveToDB(String file) {
        refereshView(file);
    }

    void refereshView(String file) {
        try {
            if (!TextUtils.isEmpty(file)) {
                //   ImageLoaderUniversal.ImageLoadSquare(mContext,fileInitial+file, mViewHolder.image, ImageLoaderUniversal.option_normal_Image.get());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***************************** new camera code *****************************************/

    public void openCamera() {


        if (CameraUtils.checkPermissions(mContext)) {

            /*
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
*/


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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*****************************  end camera code *****************************************/

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /********************* add new camera code **********************/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (resultCode == RESULT_OK) {

           Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);

            byte[] byteArray = byteArrayBitmapStream.toByteArray();

            if (requestCode == 100)   // 100 =  in attendance
            {

                attendanceBean.IN_IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);

                saveLocally();

                deleteDirectory(new File(Objects.requireNonNull(fileUri.getPath())));
            }


            if (requestCode == 200)   // 200 =  out attendance
            {
                attendanceBean.OUT_IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);

                updateLocally();

                deleteDirectory(new File(Objects.requireNonNull(fileUri.getPath())));
            }

            MarkAttendanceActivity.this.finish();
        }

    }*/

    /********************* end new camera code **********************/

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
