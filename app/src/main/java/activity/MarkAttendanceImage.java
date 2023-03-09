package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.util.List;
import java.util.Locale;

import bean.AttendanceBean;
import bean.LoginBean;
import database.DatabaseHelper;

public class MarkAttendanceImage extends AppCompatActivity implements View.OnClickListener {
    public static final int IN = 1;
    public static final int OUT = 2;
    TextView btn_capture;
    ImageButton btn_back;
    DatabaseHelper db = null;
    ImageView btn_cancel, btn_save;
    String Userid;
    CustomUtility customutility;
    String fileInitial = "file://";
    AttendanceBean attendanceBean;
    String latLong = "";
    int ATTENDANCE_FOR = 0;
    File mFile;
    private ViewHolder mViewHolder;
    private Context mContext;

    public static Intent getIntent(Context context, String in_out, String latLong) {
        Intent intent = new Intent(context, MarkAttendanceImage.class);
        intent.putExtra("in_out", in_out);
        intent.putExtra("latLong", latLong);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance_image);
        mContext = this;

        String in_out = getIntent().getStringExtra("in_out");

        customutility = new CustomUtility();

        attendanceBean = new AttendanceBean();
        Userid = LoginBean.getUseid();
        openCamera(false);

        mViewHolder = new ViewHolder(this);
        latLong = getIntent().getStringExtra("latLong");
        mViewHolder.attedance_header.setText("Attendance " + in_out);

        db = new DatabaseHelper(mContext);
        attendanceBean = db.getMarkAttendanceByDate(customutility.getCurrentDate(),Userid);

        if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
            ATTENDANCE_FOR = IN;
        } else {
            ATTENDANCE_FOR = OUT;
        }


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.capture:
                displayImagePickerPopup();
                break;
            case R.id.back:
            case R.id.icn_cancel:
                finish();
                break;

            case R.id.icn_save:

/*                Toast.makeText(mContext,"Reached to clicked",Toast.LENGTH_LONG).show();
                saveLocally();*/


                if (isValidate()) {
                    Log.d("ATTENDANCE_FOR", String.valueOf(ATTENDANCE_FOR));

                    if (ATTENDANCE_FOR == IN) {
                        Toast.makeText(mContext, "You Clicked In", Toast.LENGTH_LONG).show();
                        saveLocally();
                    } else {
                        Toast.makeText(mContext, "You Clicked Out", Toast.LENGTH_LONG).show();
                        updateLocally();
                    }
                    if (CustomUtility.checkNetwork(mContext)) {
                        /*                 uploadAttendance();*/
                    } else {
                        //Utility.ShowToast("Saved Offline.",mContext);
                        CustomUtility.ShowToast("Attendance Marked", mContext);
                        finish();
                    }
                }

                break;
            case R.id.geo_tag:
                break;


        }


    }

    public void openCamera(boolean a) {
        startActivityForResult(AndroidCameraExample.getIntent(mContext, "save"), 100);
    }

    private void displayImagePickerPopup() {
        openCamera(false);
    }

    boolean isValidate() {
        if (CustomUtility.isDateTimeAutoUpdate(mContext)) {

        } else {
            CustomUtility.showSettingsAlert(mContext);
            return false;
        }

        String latLongStr = latLong;


        if (mFile == null) {
            CustomUtility.ShowToast("Please select image first", mContext);
            return false;
        } else if (TextUtils.isEmpty(latLongStr)) {
            CustomUtility.ShowToast("Please select location", mContext);
            return false;
        }
        String path = mFile.getPath();
        attendanceBean.PERNR = LoginBean.getUseid();
        Log.d("INOUT", String.valueOf(ATTENDANCE_FOR));

        if (ATTENDANCE_FOR == IN) {
            Log.d("Date", customutility.getCurrentDate());
            attendanceBean.BEGDA = customutility.getCurrentDate();
            attendanceBean.SERVER_DATE_IN = customutility.getCurrentDate();
            attendanceBean.SERVER_TIME_IN = customutility.getCurrentTime();
            String latlong[] = latLongStr.split(",");
            attendanceBean.IN_ADDRESS = getCompleteAddressString(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
            attendanceBean.IN_TIME = customutility.getCurrentTime();
            attendanceBean.SERVER_DATE_OUT = "";
            attendanceBean.SERVER_TIME_OUT = "";
            attendanceBean.OUT_ADDRESS = "";
            attendanceBean.OUT_TIME = "";
            attendanceBean.WORKING_HOURS = "";
            attendanceBean.IMAGE_DATA = "";
            attendanceBean.CURRENT_MILLIS = System.currentTimeMillis();
            attendanceBean.IN_LAT_LONG = latLongStr;
            attendanceBean.IN_FILE_NAME = mFile.getName();
            attendanceBean.IN_FILE_VALUE = mFile.getPath();
            attendanceBean.OUT_LAT_LONG = "";
            attendanceBean.OUT_FILE_NAME = "";
            attendanceBean.OUT_FILE_LENGTH = "";
            attendanceBean.OUT_FILE_VALUE = "";
            //     Utility.ShowToast(""+attendanceBean.IN_ADDRESS,mContext);
        } else if (ATTENDANCE_FOR == OUT) {
            attendanceBean.SERVER_DATE_OUT = customutility.getCurrentDate();
            attendanceBean.SERVER_TIME_OUT = customutility.getCurrentTime();
            String latlong[] = latLongStr.split(",");
            attendanceBean.OUT_ADDRESS = getCompleteAddressString(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
            attendanceBean.OUT_TIME = customutility.getCurrentTime();
            long attendanceDifference = System.currentTimeMillis() - attendanceBean.CURRENT_MILLIS;
            long second = (attendanceDifference / 1000) % 60;
            long minute = (attendanceDifference / (1000 * 60)) % 60;
            long hour = (attendanceDifference / (1000 * 60 * 60)) % 24;
            String time = String.format(Locale.US,"%02d:%02d:%02d", hour, minute, second);
            attendanceBean.WORKING_HOURS = time;
            attendanceBean.IMAGE_DATA = "";
            attendanceBean.OUT_LAT_LONG = latLongStr;
            attendanceBean.OUT_FILE_NAME = mFile.getName();
            attendanceBean.OUT_FILE_VALUE = mFile.getPath();
            //      Utility.ShowToast(""+attendanceBean.OUT_ADDRESS,mContext);
        }
        return true;
    }


    void saveLocally() {

        //  MainActivity.mainActivity.mydb.insertAttendance(attendanceBean);
        DatabaseHelper dataHelper = new DatabaseHelper(mContext);

        dataHelper.insertMarkAttendance(attendanceBean);

        Toast.makeText(mContext, "In Attendance Marked", Toast.LENGTH_LONG).show();
    }

    void updateLocally() {

        DatabaseHelper dataHelper = new DatabaseHelper(mContext);

        dataHelper.updateMarkAttendance(attendanceBean);


        //   MainActivity.mainActivity.mydb.updateAttendance(attendanceBean);
        Toast.makeText(mContext, "Out Attendance Marked", Toast.LENGTH_LONG).show();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_FIRST_USER) {
            String url = data.getStringExtra("url");
            if (requestCode == 100) {
                if (!TextUtils.isEmpty(url)) {
                    try {
                        mFile = new File(url);
                        boolean hi = mFile.isFile();
                        String path = mFile.getPath();
                        saveToDB(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    void saveToDB(String file) {
        refereshView(file);
    }

    void refereshView(String file) {
        try {
            if (!TextUtils.isEmpty(file)) {
                ImageLoaderUniversal.ImageLoadSquare(mContext, fileInitial + file, mViewHolder.image, ImageLoaderUniversal.option_normal_Image.get());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public class ViewHolder {
        TextView attedance_header/*, mGeoTag*/;
        TextView capture;
        ImageView image;
        ImageView mBack, mSave, mCancel;

        public ViewHolder(View.OnClickListener listenre) {
            attedance_header = (TextView) findViewById(R.id.attedance_header);
            capture = (TextView) findViewById(R.id.capture);
            image = (ImageView) findViewById(R.id.image);
            mBack = (ImageView) findViewById(R.id.back);
            mSave = (ImageView) findViewById(R.id.icn_save);
            mCancel = (ImageView) findViewById(R.id.icn_cancel);
            capture.setOnClickListener(listenre);
            mBack.setOnClickListener(listenre);
            mSave.setOnClickListener(listenre);
            mCancel.setOnClickListener(listenre);
        }
    }

}
