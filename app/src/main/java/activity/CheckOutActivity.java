package activity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.util.TextUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.CustomerDetailBean;
import bean.LoginBean;
import database.DatabaseHelper;
import model.ImageModel;
import webservice.Constants;

public class CheckOutActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    Context context;
    Spinner spinner, spinner_status;
    List<String> list = null;
    List<String> status_list = null;
    DatePickerDialog datePickerDialog;
    EditText editText_comment, editText_follow_up_date;
    double latitude, longitude;
    String str, audio_record = "";
    boolean start_recording = false;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    GPSTracker gps = null;
    DatabaseHelper dataHelper = null;
    ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    String userid, help_name, conversion_status, no_order_comment, customer_name = "", phone_number, route_code, username, city_name = "";
    int index, index_status,index1;
    Button btn_check_out_save, btn_end_recording, btn_start_recording;
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutName;
    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";
    String root;
    File dir;
    String photo1="", photo2="",photo3="",photo4="",photo5="",photo6="",photo7="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        context = this;
        CheckOutActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner_status = (Spinner) findViewById(R.id.status);

        btn_check_out_save = (Button) findViewById(R.id.btn_check_out_save);
        btn_end_recording = (Button) findViewById(R.id.end_recording);
        btn_start_recording = (Button) findViewById(R.id.start_recording);


        editText_comment = (EditText) findViewById(R.id.text_check_out_comment);
        editText_follow_up_date = (EditText) findViewById(R.id.editText_follow_up_date);


// add by mayank in 1.5 version
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");
        LoginBean.username = pref.getString("key_ename", "username");
// add by mayank in 1.5 version


        userid = LoginBean.getUseid();
        username = LoginBean.getUsername();

        userid = pref.getString("key_username", "userid");
        username = pref.getString("key_ename", "username");


        list = new ArrayList<String>();
        status_list = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();

        dataHelper = new DatabaseHelper(context);

        customer_name = bundle.getString("customer_name");
        phone_number = bundle.getString("phone_number");
        route_code = bundle.getString("route_code");
        city_name = bundle.getString("city_name");

        gps = new GPSTracker(this);

        getSupportActionBar().setTitle("Check Out - " + customer_name);


        selectCheckOutHelp();



//        spinner = (Spinner) findViewById(R.id.spinner);


        //*********************   activity type spinner  ****************************************//

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                help_name = spinner.getSelectedItem().toString();

                String currentString = help_name;
                try {
                    if(!currentString.equalsIgnoreCase("Select Activity"))
                    {
                    String[] separated = currentString.split("--");
                    if(separated[1] != null) {
                        index1 = Integer.parseInt(separated[1]);
                    }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }


               // Toast.makeText(context, index, Toast.LENGTH_LONG).show();

                if( index1 != 906 && index1 != 505 && index1 != 427 && index1 != 503 && index1 != 524 && index1 != 536 && index1 != 501 && index1 != 502 && index1 != 606 && index1 != 930 && index1 != 929 ) {

                    deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name));

                    CustomUtility.setSharedPreference(context, "ServiceCenterTRN" + customer_name, "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner.setPrompt("Select Activity");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        //*********************   Conversion status spinner *******************************//

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index_status = arg0.getSelectedItemPosition();
                conversion_status = spinner_status.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_status.setPrompt("Conversion Status");
        status_list.clear();
        status_list.add("Select Conversion Status");
        status_list.add("Converted");
        status_list.add("Not Interested");
        status_list.add("Inprocess");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_item_optional, status_list);

        // Drop down layout style - list view with radio button
        dataAdapterStatus.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner_status.setAdapter(dataAdapterStatus);


        //********************************** follow up activity   **********************************//


        editText_follow_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CheckOutActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                editText_follow_up_date.setText(dayOfMonth + "."
                                        + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();


            }
        });


        //******************************************************************************************//

        btn_end_recording.setTextColor(Color.parseColor("#ffffff"));


        btn_check_out_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {


                    imageModelArrayList = CustomUtility.getArrayList(CheckOutActivity.this);


                    if(index1 == 930 || index1 == 929 || index1 == 906 || index1 == 505 || index1 == 427 || index1 == 503 || index1 == 524 || index1 == 536 || index1 == 501 || index1 == 502 || index1 == 606) {
                        if (imageModelArrayList!=null && imageModelArrayList.size()>0) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("Data Save alert !");
                            alertDialog.setMessage("Do you want to save data ?");

                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    save_check_out();

                                    editText_comment.setText("");
                                    editText_follow_up_date.setText("");
                                    spinner.setSelection(0);
                                    spinner_status.setSelection(0);

                                }
                            });

                            // on pressing cancel button

                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else {
                            Toast.makeText(context, "Please Select Photos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Data Save alert !");
                        alertDialog.setMessage("Do you want to save data ?");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                save_check_out();

                                editText_comment.setText("");
                                editText_follow_up_date.setText("");
                                spinner.setSelection(0);
                                spinner_status.setSelection(0);

                            }
                        });

                        // on pressing cancel button

                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }

                }
            }
        });


        btn_start_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateComment()) {
                    start_recording();
                }
            }
        });


        btn_end_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateComment()) {
                    stop_recording();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper db = new DatabaseHelper(this);
       Log.e("CheckInOut====>", String.valueOf(db.getCheckInOut().size()));
    }

    public void stop_recording() {

        Log.e("media","****"+mediaRecorder);

        if (mediaRecorder != null) {

            try {

                    try {
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    } catch(RuntimeException stopException) {
                        // handle cleanup here
                    }

                start_recording = false;

                btn_end_recording.setEnabled(false);
                btn_end_recording.setBackgroundColor(Color.parseColor("#8b8b8c"));
                btn_end_recording.setTextColor(Color.parseColor("#ffffff"));


                Toast.makeText(CheckOutActivity.this, "Recording stop",
                        Toast.LENGTH_LONG).show();


                File file = new File(AudioSavePathInDevice);

                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));

                Log.e("FILESIZE","&&&&"+file_size);

                if (file.exists()) {

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    BufferedInputStream in = null;
                    try {

                        in = new BufferedInputStream(new FileInputStream(AudioSavePathInDevice));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    int read;
                    byte[] buff = new byte[1024];
                    try {
                        if (in != null) {
                            while ((read = in.read(buff)) != -1) {
                                out.write(buff, 0, read);
                            }
                        }
                        out.flush();
                    byte[] audioBytes = out.toByteArray();
                    audio_record = Base64.encodeToString(audioBytes, Base64.DEFAULT);

                    } catch (OutOfMemoryError | IOException e) {
                        e.printStackTrace();
                    }

                    // Log.d("audio_save",""+audio_record);

                    file.delete(); // delete file from location

                }
            } catch (Exception e) {

                e.printStackTrace();


            }
        }

    }


    @SuppressLint("WrongConstant")
    public void start_recording() {


        btn_start_recording.setEnabled(false);
        btn_start_recording.setBackgroundColor(Color.parseColor("#8b8b8c"));
        btn_start_recording.setTextColor(Color.parseColor("#ffffff"));

        btn_end_recording.setEnabled(true);
        btn_end_recording.setBackgroundColor(Color.parseColor("#0179b6"));
        btn_end_recording.setTextColor(Color.parseColor("#ffffff"));


        start_recording = true;
        //  android:background="@layout/rounded_corner_blue"
        if (checkPermission()) {

            try {

                if (mediaRecorder != null) {
                    mediaRecorder.release();
                }
              /* String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
               File file = new File(filepath, "/" +
                       "Audio_" + String.valueOf(System.currentTimeMillis()) + ".3gp");

               if(!file.exists()){
                   file.mkdirs();
               }*/


               /* AudioSavePathInDevice = getFilename();*/
                        /*Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                "Audio_" + String.valueOf(System.currentTimeMillis()) + ".3gp";*/

                AudioSavePathInDevice = getFilename();

                Log.e("PATH","****"+ AudioSavePathInDevice);

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                mediaRecorder.setAudioEncodingBitRate(128000);
                mediaRecorder.setAudioSamplingRate(16000);
                mediaRecorder.setOutputFile(AudioSavePathInDevice);
                mediaRecorder.setAudioChannels(1);
                mediaRecorder.setMaxDuration(90000);
                mediaRecorder.setMaxFileSize(4000000);
                mediaRecorder.prepare();
                mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                    @Override
                    public void onInfo(MediaRecorder mr, int what, int extra) {
                        if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                            Toast.makeText(context, "Recording stops. Limit reached", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                mediaRecorder.start();

            } catch (IllegalStateException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            Toast.makeText(CheckOutActivity.this, "Recording started",
                    Toast.LENGTH_LONG).show();


        } else {
            requestPermission();
        }


    }

   /* protected void onPause() {
        super.onPause();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }*/

    private String getFilename()
    {
        root = context.getExternalFilesDir(null).getAbsolutePath();

        dir = new File(root + "/" + "ShkSales_Audio");


        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return (dir + "/" + System.currentTimeMillis() + ".3gp");
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    public void save_check_out() {



             if(imageModelArrayList!=null && imageModelArrayList.size()>0) {
                 for (int i = 0; i < imageModelArrayList.size(); i++) {

                     if(imageModelArrayList.get(i).getFlag().equals(Constants.photo1)){
                         photo1 = imageModelArrayList.get(i).getImage();
                     }
                     if(imageModelArrayList.get(i).getFlag().equals(Constants.photo2)){
                         photo2 = imageModelArrayList.get(i).getImage();
                     }

                     if(imageModelArrayList.get(i).getFlag().equals(Constants.photo3)){
                         photo3 = imageModelArrayList.get(i).getImage();
                     }

                 }


             }
       latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        // save offline check in check out data

        dataHelper = new DatabaseHelper(context);

        // Log.d("check_out_userid",userid);
        if (index_status == 0) {
            conversion_status = "";
        }



        if(index1 != 906 && index1 != 505 && index1 != 427 && index1 != 503 && index1 != 524 && index1 != 536 && index1 != 501 && index1 != 502 && index1 != 606 && index1 != 930 && index1 != 929 ) {

          dataHelper.insertCheckInOut(userid,
                    new CustomUtility().getCurrentDate(), // check in date
                    new CustomUtility().getCurrentTime(),
                    "",// check in time
                    Double.toString(latitude).trim(), // check in lat
                    "",
                    Double.toString(longitude).trim(),
                    "",
                    editText_comment.getText().toString(),
                    help_name,
                    CustomerDetailBean.getRoute_code(),
                    audio_record,
                    "NOT",
                    customer_name, city_name,
                    "",
                    editText_follow_up_date.getText().toString(),
                    conversion_status,
                    photo1,
                    photo2,
                    photo3,
                    photo4,
                    photo5,
                    photo6,
                    photo7,
                    CustomerDetailBean.getPhone_number()); // customer number


            Log.e("doesTableExist123====>", String.valueOf(dataHelper.doesTableExist( pref.getString("key_username", "userid"))));


            /*insert value to visit history table*/

            dataHelper.insertvisitHistory(new CustomUtility().getCurrentDate(),
                    new CustomUtility().getCurrentTime(),
                    editText_comment.getText().toString(),
                    username,
                    phone_number,
                    "",
                    "visit",
                    audio_record);

            /* end insert value to visit history table*/


            btn_check_out_save.setEnabled(false);
            btn_check_out_save.setBackgroundColor(Color.parseColor("#8b8b8c"));
            btn_check_out_save.setTextColor(Color.parseColor("#ffffff"));


            // Check out
            // new Capture_employee_gps_location(context,"11",phone_number);


            Toast.makeText(context, "Check Out saved successfully", Toast.LENGTH_LONG).show();

            SyncCheckInOutInBackground();
        }


    }

    public void SyncCheckInOutInBackground() {
        Intent i = new Intent(CheckOutActivity.this, SyncDataService.class);
        startService(i);

    }


    private boolean submitForm() {
        boolean value;
        if ((validateActivityType()) &&
                (validateComment()) &&
                CustomUtility.CheckGPS(this) &&
                validateDate() &&
                validateAudio() &&
                followUpDate()
        ) {
            value = true;
        } else {
            value = false;
        }
        return value;
    }


    public boolean validateAudio() {
        if (TextUtils.isEmpty(audio_record)) {

            if (start_recording) {
                Toast.makeText(this, "Please Stop Recording", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Please Start Recording", Toast.LENGTH_SHORT).show();
            }


            return false;
        }

        return true;
    }


    private boolean validateActivityType() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Activity Type", Toast.LENGTH_SHORT).show();
            value = false;
        } else {
            value = true;
        }

        return value;
    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }


    private boolean validateComment() {

        if (editText_comment.getText().toString().isEmpty()) {

            inputLayoutName.setError("Please Enter Check Out Comment");
            requestFocus(editText_comment);
            return false;

        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean followUpDate() {

        //
        if ((!editText_follow_up_date.getText().toString().isEmpty()) &&
                (index_status == 0)) {
            Toast.makeText(this, "Please Enter Conversion Status", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    public void selectCheckOutHelp() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        list.clear();
        list.add("Select Activity");

        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String help_code = "3"; // 3 is use for check out search help

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SEARCH_HELP
                    + " where  help_code = " + "'" + help_code + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    @SuppressLint("Range") String help_name = cursor.getString(cursor.getColumnIndex("help_name"));
                    list.add(help_name);

                }

                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                db.close();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.menu_attachment, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(index1 == 906 ) {
            if (id == R.id.action_complaint_attachment) {

                String comment = editText_comment.getText().toString();
                String folwupdt = editText_follow_up_date.getText().toString();

                latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
                longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

                if (!TextUtils.isEmpty(comment)){
                    if (!TextUtils.isEmpty(help_name)){
                        if (!TextUtils.isEmpty(audio_record)){
                        if (!TextUtils.isEmpty(folwupdt)){
                        if (!TextUtils.isEmpty(conversion_status)){

                            Intent intent = new Intent(getApplicationContext(), ServiceCenterTrainingImgActivity.class);
                            intent.putExtra("lat", Double.toString(latitude));
                            intent.putExtra("lng", Double.toString(longitude));
                            intent.putExtra("comment", comment);
                            intent.putExtra("phn", phone_number);
                            intent.putExtra("helpnm", help_name);
                            intent.putExtra("audio", audio_record);
                            intent.putExtra("customer_name", customer_name);
                            intent.putExtra("citynm", city_name);
                            intent.putExtra("folowupdt", folwupdt);
                            intent.putExtra("conv_sta", conversion_status);
                            startActivity(intent);
                            return true;
                        }
                        else{
                            Toast.makeText(context, "Select Conversion Status", Toast.LENGTH_SHORT).show();
                        }

                        }
                        else{
                            Toast.makeText(context, "Please Enter Follow Up Date", Toast.LENGTH_SHORT).show();
                        }


                        }
                        else{
                            Toast.makeText(context, "Please Record Audio", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(context, "Please Select Activity", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(context, "Please Enter Comment", Toast.LENGTH_SHORT).show();
                }


            }
        }
        else if(index1 == 930 || index1 == 929 || index1 == 505 || index1 == 427 || index1 == 503 || index1 == 524 || index1 == 536 || index1 == 501 || index1 == 502 || index1 == 606) {
            if (id == R.id.action_complaint_attachment) {

                String comment = editText_comment.getText().toString();
                String folwupdt = editText_follow_up_date.getText().toString();

                latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
                longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

                Intent intent = new Intent(getApplicationContext(), OtherImgActivity.class);
                intent.putExtra("lat", Double.toString(latitude));
                intent.putExtra("lng", Double.toString(longitude));
                intent.putExtra("comment", comment);
                intent.putExtra("phn", phone_number);
                intent.putExtra("helpnm", help_name);
                intent.putExtra("audio", audio_record);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("citynm", city_name);
                intent.putExtra("folowupdt", folwupdt);
                intent.putExtra("conv_sta", conversion_status);
                startActivity(intent);
                return true;
            }
        }
        else{
            Toast.makeText(context, "Only for Service center training, Choupal meet, Exibition, Kisan meet, Kanopy, Demo Van, Road Show, Mehanic Meet, Demo  activity", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {

        CheckOutActivity.this.onSuperBackPressed();


    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(CheckOutActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0) {

                boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (StoragePermission && RecordPermission) {

                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    public void onSuperBackPressed() {

        if (start_recording) {
            stop_recording();
        }
        // stopRecording();
        super.onBackPressed();
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
