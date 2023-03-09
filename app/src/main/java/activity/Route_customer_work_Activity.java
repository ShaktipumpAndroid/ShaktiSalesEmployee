package activity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Random;

import backgroundservice.SyncDataService;
import bean.CustomerDetailBean;
import bean.LoginBean;
import database.DatabaseHelper;
import searchlist.RouteCustomerWorkView;
import searchlist.RouteCustomerWorkViewAdapter;


public class Route_customer_work_Activity extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    String help_name, userid, username;
    DatabaseHelper dataHelper = null;
    CustomUtility customutility = null;
    String customer_name = "", city_name = "", phone_number, route_code, partner_type;
    double latitude, longitude;
    String db_check_time_in, db_check_time_out = " ";
    boolean counter;
    Geocoder geocoder;
    RouteCustomerWorkViewAdapter adapter;
    ArrayList<RouteCustomerWorkView> arraylist = new ArrayList<RouteCustomerWorkView>();
    EditText checkOut_comment;
    ListView list_new;
    int index;
    List<String> list = null;
    TextView txtRoute_name, txtPartner_name, txtAddress, txtContact_person, txtMob_no, txtTel_number,
            txtEmail, callMobile, callPhone, sendMail;
    Context context;
    AlertDialog dialog;
    Spinner spinner;
    String str, audio_record = "";
    GPSTracker
            gps = null;
    Button check_out, check_in, no_order, take_order, history, survey, btn_suyvey_display, btn_mou;
    CustomerDetailBean customerdetailbean = null;
    boolean flag_check_in = false, flag_check_out = false;
    View layoutImageView = null;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder = null;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;
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
            Toast.makeText(Route_customer_work_Activity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private TextInputLayout inputLayoutName;

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
        setContentView(R.layout.activity_route_customer_work);
        context = this;

        progressDialog = new ProgressDialog(context);
        list = new ArrayList<String>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Sales Target");

        // Locate the TextViews in activity_route_customer_work
//        txtRoute_name = (TextView) findViewById(R.id.route_value);
//        txtPartner_name = (TextView) findViewById(R.id.name_value);
//        txtAddress = (TextView) findViewById(R.id.adress_value);
//        txtContact_person = (TextView) findViewById(R.id.cp_value);
//        txtMob_no = (TextView) findViewById(R.id.mobile_value);
//        txtTel_number = (TextView) findViewById(R.id.contact_ph);
//        txtEmail = (TextView) findViewById(R.id.email_value);

//        callMobile = (TextView) findViewById(R.id.mobile_call);
//        callPhone = (TextView) findViewById(R.id.contact_ph_call);
//        sendMail = (TextView) findViewById(R.id.mail_send);


// add by mayank in 1.5 version
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        CheckinOutValidation();
        //LoginBean.userid = pref.getString("key_username", "userid");
        // LoginBean.username = pref.getString("key_ename", "username");
// add by mayank in 1.5 version


        userid = LoginBean.getUseid();
        username = LoginBean.getUsername();

        gps = new GPSTracker(this);
        customutility = new CustomUtility();


        check_in = (Button) findViewById(R.id.btn_check_in);
        check_out = (Button) findViewById(R.id.btn_check_out);
        no_order = (Button) findViewById(R.id.btn_no_order);
        take_order = (Button) findViewById(R.id.btn_tack_order);
        survey = (Button) findViewById(R.id.btn_survey);
        btn_suyvey_display = (Button) findViewById(R.id.btn_suyvey_display);
        btn_mou = (Button) findViewById(R.id.btn_mou);

        layoutImageView = findViewById(R.id.layout_suyvey_display);

// Load the results into the TextViews

//
//        txtRoute_name.setText(customerdetailbean.getRoute_name().toLowerCase());
//        txtPartner_name.setText(customerdetailbean.getPartner_name().toLowerCase());
//        txtAddress.setText(customerdetailbean.getAddress().toLowerCase());
//        txtContact_person.setText(customerdetailbean.getContact_person().toLowerCase());
//        txtMob_no.setText(customerdetailbean.getMob_no().toLowerCase());
//        txtTel_number.setText(customerdetailbean.getTel_number().toLowerCase());
//        txtEmail.setText(customerdetailbean.getEmail().toLowerCase());

        if (!TextUtils.isEmpty(CustomerDetailBean.getPartner_name())) {
            customer_name = CustomerDetailBean.getPartner_name().toLowerCase();
        }


        if (!TextUtils.isEmpty(CustomerDetailBean.getPhone_number())) {
            phone_number = CustomerDetailBean.getPhone_number().toLowerCase();

        }

        if (!TextUtils.isEmpty(CustomerDetailBean.getRoute_code())) {
            route_code = CustomerDetailBean.getRoute_code().toLowerCase();
        }

        if (!TextUtils.isEmpty(CustomerDetailBean.getPartner())) {
            partner_type = CustomerDetailBean.getPartner().toLowerCase();
        }

        if (!TextUtils.isEmpty(CustomerDetailBean.getDistrict_txt())) {
            city_name = CustomerDetailBean.getDistrict_txt().toLowerCase();
        }

        //Log.d("Type",""+customerdetailbean.getPartner().toLowerCase());

        if (!TextUtils.isEmpty(CustomerDetailBean.getLongitude())) {
            if (!CustomerDetailBean.getLongitude().equals("0.0")) {
                survey.setEnabled(false);
                survey.setBackgroundColor(Color.parseColor("#8b8b8c"));
                survey.setTextColor(Color.parseColor("#ffffff"));
            }
        }


        if (!TextUtils.isEmpty(CustomerDetailBean.getPartner_name())) {

            getSupportActionBar().setTitle(customer_name);

            CheckinOutValidation();
            create_layout();

            getCheckIn();

            getCheckOutPrompt();
            getNoOrder();

            getTakeOrder();

            // callMobile();
            // callPhoneNo();
            // sendMail();
            getHistory();
            getSurvey();


        }

    }

    public void getSurvey() {


        surveyValidation();


        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Route_customer_work_Activity.this.finish();
                Intent intent = new Intent(Route_customer_work_Activity.this, SurveyActivity.class);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("route_code", route_code);
                startActivity(intent);

            }
        });


        getSurveyImageSaveInSAP();

        btn_suyvey_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Route_customer_work_Activity.this, SurveyDisplayActivity.class);
                intent.putExtra("phone_number", phone_number);
                startActivity(intent);

            }
        });


        if (partner_type.equalsIgnoreCase("D") || partner_type.equalsIgnoreCase("P")) {
            btn_mou.setVisibility(View.VISIBLE);
        }


        btn_mou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = ProgressDialog.show(context, "", "Loading..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(context)) {

                            if ((progressDialog != null) && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Intent intent = new Intent(Route_customer_work_Activity.this, MouActivity.class);
                            intent.putExtra("phone_number", phone_number);
                            intent.putExtra("mou", "0.0");
                            intent.putExtra("billing", "0.0");
                            intent.putExtra("outstanding", "0.0");
                            intent.putExtra("backlog", "0.0");
                            startActivity(intent);


                        } else {
                            if ((progressDialog != null) && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            Message msg = new Message();
                            msg.obj = "No Internet Connection Found";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();

            }
        });


    }

    public void getSurveyImageSaveInSAP() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_VIEW_SURVEY
                    + " WHERE " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'";


            cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                //layoutImageView.setVisibility(View.VISIBLE);
                btn_suyvey_display.setVisibility(View.VISIBLE);

            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }
            // Close database

        }
    }










  /*  public void callMobile() {

        callMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + customerdetailbean.getMob_no()

                ));
                context.startActivity(intent);



if (!customerdetailbean.getMob_no().equals("") && !customerdetailbean.getMob_no().equals(null)) {
    // no order
              new Capture_employee_gps_location(context,"12",phone_number);
}

            }
        });

    }*/


   /* public void callPhoneNo() {

        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + customerdetailbean.getTel_number()));
                context.startActivity(intent);

                if (!customerdetailbean.getTel_number().equals("") && !customerdetailbean.getTel_number().equals(null)) {
                    // no order
                    new Capture_employee_gps_location(context,"12",phone_number);
                }


            }
        });

    }*/

    public void create_layout() {

        RouteCustomerWorkView tv;
        tv = new RouteCustomerWorkView();


        tv.setField("Route :");
        tv.setValue(CustomerDetailBean.getRoute_name().toLowerCase());
        arraylist.add(tv);

        tv = new RouteCustomerWorkView();
        tv.setField("Name :");
        tv.setValue(CustomerDetailBean.getPartner_name().toLowerCase());
        arraylist.add(tv);


        tv = new RouteCustomerWorkView();
        tv.setField("Address :");

        // CustomUtility.capitalize()
        tv.setValue(CustomerDetailBean.getAddress().toLowerCase());

        arraylist.add(tv);

        tv = new RouteCustomerWorkView();
        tv.setField("Person :");
        tv.setValue(CustomerDetailBean.getContact_person().toLowerCase());
        arraylist.add(tv);

        tv = new RouteCustomerWorkView();
        tv.setField("Mobile No :");
        tv.setValue(CustomerDetailBean.getMob_no().toLowerCase());
        arraylist.add(tv);

        tv = new RouteCustomerWorkView();
        tv.setField("Contact Ph:");
        tv.setValue(CustomerDetailBean.getTel_number());

        arraylist.add(tv);

        tv = new RouteCustomerWorkView();
        tv.setField("Email :");
        tv.setValue(CustomerDetailBean.getEmail().toLowerCase());
        arraylist.add(tv);


        list_new = (ListView) findViewById(R.id.listview);

        // Pass results to ListViewAdapter Class
        adapter = new RouteCustomerWorkViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list_new.setAdapter(adapter);

    }

    public void getHistory() {

        history = (Button) findViewById(R.id.btn_history);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Route_customer_work_Activity.this, VisitHistoryActivity.class);
                intent.putExtra("phone_number", phone_number);
                startActivity(intent);


            }

        });
    }

    public void sendMail() {

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!CustomerDetailBean.getEmail().equals("") && !CustomerDetailBean.getEmail().equals(null)) {
                    // no order
                    new Capture_employee_gps_location(context, "13", phone_number);


                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{CustomerDetailBean.getEmail()});
                    // i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    // i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Route_customer_work_Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }

            }


        });


    }

    public void getNoOrder() {


        noOrderValidation();

        no_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasPermissions(mContext, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(Route_customer_work_Activity.this, PERMISSIONS, PERMISSION_ALL);
                } else {


                    Route_customer_work_Activity.this.finish();

                    Intent intent = new Intent(Route_customer_work_Activity.this, NoOrderActivity.class);
                    intent.putExtra("customer_name", customer_name);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("route_code", route_code);
                    startActivity(intent);
                }
            }
        });


    }

    public void getTakeOrder() {
        take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Route_customer_work_Activity.this, TakeOrderActivity.class);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("route_code", route_code);
                intent.putExtra("partner_type", partner_type);

                intent.putExtra("order_type", "Take Order");
                startActivity(intent);
            }
        });
    }

    @SuppressLint("Range")
    public void CheckinOutValidation() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;

        try {
            db.beginTransactionNonExclusive();

            Log.e("123", "1" + userid);
            Log.e("123", "2" + new CustomUtility().getCurrentDate());
            Log.e("123", "3" + CustomerDetailBean.getPhone_number());

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CHECK_IN_OUT
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_DATE_IN + " = '" + new CustomUtility().getCurrentDate() + "'"
                    + " AND " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + CustomerDetailBean.getPhone_number() + "'";


            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {

                Log.e("123", "4" + cursor.getCount());


                while (cursor.moveToNext()) {

                    db_check_time_in = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TIME_IN));

                    db_check_time_out = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TIME_OUT));

                    Log.e("123", "5" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TIME_IN)));
                    Log.e("123", "6" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TIME_OUT)));

                    if (db_check_time_in != null) {
                        if (db_check_time_out != null) {
                            check_in.setEnabled(false);
                            check_in.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            check_in.setTextColor(Color.parseColor("#ffffff"));
                            check_out.setEnabled(false);
                            check_out.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            check_out.setTextColor(Color.parseColor("#ffffff"));
                        }
                        if (db_check_time_out == null) {
                            check_in.setEnabled(false);
                            check_in.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            check_in.setTextColor(Color.parseColor("#ffffff"));
                            check_out.setEnabled(true);
                            check_out.setBackgroundColor(Color.parseColor("#0179b6"));
                            check_out.setTextColor(Color.parseColor("#ffffff"));

                        }

                    } else {
                        check_in.setEnabled(true);
                        check_out.setEnabled(false);
                        check_out.setBackgroundColor(Color.parseColor("#8b8b8c"));
                        check_out.setTextColor(Color.parseColor("#ffffff"));
                        counter = true;
                    }
                }

            }

            db.setTransactionSuccessful();
            cursor.close();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            //   db.endTransaction();
            // End the transaction.
            // cursor.close();
            if (db != null) {
                db.endTransaction();
//            db.endTransaction();
                db.close();
                // Close database
            }
        }

    }

    public void getCheckIn() {
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CustomUtility.CheckGPS(getApplicationContext()) && validateDate()) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Check In alert !");
                    alertDialog.setMessage("Do you want to Check In ?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            // start_recording();
                            if (!hasPermissions(mContext, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(Route_customer_work_Activity.this, PERMISSIONS, PERMISSION_ALL);
                            } else {
                                saveCheckin();
                            }
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
        });

    }

    public void start_recording() {


        if (checkPermission()) {

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            "Audio_" + System.currentTimeMillis() + ".3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


//          Toast.makeText(Route_customer_work_Activity.this, "Recording started",
//                  Toast.LENGTH_LONG).show();


        } else {
            requestPermission();
        }


    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public void saveCheckin() {

        latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        // save offline check in check out data

        dataHelper = new DatabaseHelper(context);
        //    dataHelper.deleteCheckInOut();
        dataHelper.insertCheckInOut(userid,
                new CustomUtility().getCurrentDate(), // check in date
                new CustomUtility().getCurrentTime(),
                "",// check in time
                Double.toString(latitude).trim(), // check in lat
                "",
                Double.toString(longitude).trim(),
                "",
                "",
                "",
                CustomerDetailBean.getRoute_code(),
                "",
                "NOT", "", "", "", "", "", "", "", "", "",
                "", "","",
                CustomerDetailBean.getPhone_number()); // customer number


        flag_check_in = true;

        // Check In
        //new Capture_employee_gps_location(context, "10",customerdetailbean.getPhone_number());

        Toast.makeText(Route_customer_work_Activity.this, "Check In at -"
                + new CustomUtility().getCurrentTime(), Toast.LENGTH_LONG).show();

        if (counter = true) {
            check_in.setEnabled(false);
            check_in.setBackgroundColor(Color.parseColor("#8b8b8c"));
            check_in.setTextColor(Color.parseColor("#ffffff"));
            check_out.setEnabled(true);
            check_out.setBackgroundColor(Color.parseColor("#0179b6"));
            counter = false;
        }

    }

    public void ViewDemo() {


        AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setTitle("Check Out Comment :");


        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.check_out_prompt_layout, null);

        checkOut_comment = (EditText) dialogView
                .findViewById(R.id.editTextDialogUserInput);
        inputLayoutName = (TextInputLayout) dialogView.findViewById(R.id.input_layout_name);


        Dialog.setView(dialogView);

        // do not write code in onclick, it is again over ride below
        Dialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });


        dialog = Dialog.create();
        dialog.show();

        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#0179b6"));
        pbutton.setTextColor(Color.parseColor("#FFFFFF"));
//
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (submitForm()) {

                    // stopRecording();

                    latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
                    longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

                    // save offline check in check out data

                    dataHelper = new DatabaseHelper(context);


                  /*  dataHelper.updateCheckInOut(userid,
                            new CustomUtility().getCurrentDate(),
                            new CustomUtility().getCurrentTime(),
                            Double.toString(latitude),
                            Double.toString(longitude),
                            CustomerDetailBean.getPhone_number(),
                            checkOut_comment.getText().toString(),
                            help_name,
                            audio_record,
                            customer_name,
                            city_name,
                            "", //follow_up_date
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    );*/
                    dataHelper.updateCheckInOut(userid,
                            new CustomUtility().getCurrentDate(), // check in date
                            new CustomUtility().getCurrentTime(),
                            "",// check in time
                            Double.toString(latitude), // check in lat
                            "",
                            Double.toString(longitude),
                            "",
                            checkOut_comment.getText().toString(),
                            help_name,
                            CustomerDetailBean.getRoute_code(),
                            audio_record,
                            "NOT",
                            customer_name, city_name,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            CustomerDetailBean.getPhone_number());



                    /*insert value to visit history table*/

                    dataHelper.insertvisitHistory(new CustomUtility().getCurrentDate(),
                            new CustomUtility().getCurrentTime(),
                            checkOut_comment.getText().toString(),
                            username,
                            CustomerDetailBean.getPhone_number(),
                            "",
                            "visit",
                            audio_record);

                    /* end insert value to visit history table*/


                    flag_check_out = true;


                    // Check out
                    new Capture_employee_gps_location(context, "11", CustomerDetailBean.getPhone_number());


                    // new SyncDataToSAP().SyncCheckInOutToSap(context) ;

                    SyncCheckInOutInBackground();

                    check_in.setEnabled(false);
                    check_in.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    check_in.setTextColor(Color.parseColor("#ffffff"));
                    check_out.setEnabled(false);
                    check_out.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    check_out.setTextColor(Color.parseColor("#ffffff"));


                    dialog.dismiss();


                }

            }
        });


        selectCheckOutHelp();

        spinner = (Spinner) dialogView.findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                help_name = spinner.getSelectedItem().toString();

                // Toast.makeText(Route_customer_work_Activity.this, String.valueOf(index), Toast.LENGTH_LONG).show();

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

    }

    public void SyncCheckInOutInBackground() {


        Intent i = new Intent(Route_customer_work_Activity.this, SyncDataService.class);
        startService(i);


//        progressDialog = ProgressDialog.show(Route_customer_work_Activity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(Route_customer_work_Activity.this))
//                {
//
//
//                 Intent i = new Intent(Route_customer_work_Activity.this, SyncDataService.class);
//                 i.putExtra("sync_data", "sync_check_in_out_entry");
//                 startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "Check in-out Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . Check in-out saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();

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
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    public void getCheckOutPrompt() {

        //check_out = (Button) findViewById(R.id.btn_check_out);


        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasPermissions(mContext, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(Route_customer_work_Activity.this, PERMISSIONS, PERMISSION_ALL);
                } else {

                    Intent intent = new Intent(Route_customer_work_Activity.this, CheckOutActivity.class);
                    intent.putExtra("customer_name", customer_name);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("city_name", city_name);

                    intent.putExtra("route_code", route_code);
                    startActivity(intent);

                    //   ViewDemo();
                }
            }

        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

                onBackPressed();

                return true;

            case R.id.action_route_map_menu:


                if (!CustomerDetailBean.getLatitude().equals("0.0")) {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    // sending data to new activity

                    i.putExtra("partner_name", CustomerDetailBean.getPartner_name().trim());
                    i.putExtra("partner_latitude", CustomerDetailBean.getLatitude().trim());
                    i.putExtra("partner_longitude", CustomerDetailBean.getLongitude().trim());
                    i.putExtra("single_map", "single_map");

                    startActivity(i);
                } else {

                    CustomUtility.ShowToast("No Location found.", this);

                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_work, menu);
        return true;
    }

    private boolean submitForm() {
        boolean value;
        // validateAudio()
        value = (validateActivityType()) &&
                (validateComment()) &&
                CustomUtility.CheckGPS(this) &&
                validateDate();
        return value;
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
        if (checkOut_comment.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Check Out Comment", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Route_customer_work_Activity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public boolean validateAudio() {


        AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setTitle("Audio Recording:");


        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.check_out_prompt_layout, null);

        checkOut_comment = (EditText) dialogView
                .findViewById(R.id.editTextDialogUserInput);
        inputLayoutName = (TextInputLayout) dialogView.findViewById(R.id.input_layout_name);


        Dialog.setView(dialogView);

        // do not write code in onclick, it is again over ride below
        Dialog.setPositiveButton("Start Recording",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });


        dialog = Dialog.create();
        dialog.show();

        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        pbutton.setBackgroundColor(Color.parseColor("#0179b6"));
        pbutton.setTextColor(Color.parseColor("#FFFFFF"));
//
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  dialog.dismiss();


            }
        });


        Dialog.setNegativeButton("Stop Recording",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });


        return true;
    }

    public void stopRecording() {

        if (mediaRecorder != null) {

            mediaRecorder.stop();


            File file = new File(AudioSavePathInDevice);

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
                        while ((read = in.read(buff)) > 0) {
                            out.write(buff, 0, read);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] audioBytes = out.toByteArray();
                audio_record = Base64.encodeToString(audioBytes, Base64.DEFAULT);

                // Log.d("audio_save",""+audio_record);

                file.delete(); // delete file from location


            }


        }
//      Toast.makeText(Route_customer_work_Activity.this, "Recording Completed",
//              Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*if (TextUtils.isEmpty(db_check_time_out)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog.setTitle("Check Out Alert");
            // Setting Dialog Message
            alertDialog.setMessage("Please Press Check Out");
            // On pressing Settings button
            alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();


                }
            });

            alertDialog.show();

        } else {
            Route_customer_work_Activity.this.onSuperBackPressed();
        }*/


    }

    @Override
    protected void onResume() {
        userid = LoginBean.getUseid();
        username = LoginBean.getUsername();
        CheckinOutValidation();
        super.onResume();

    }

    public void surveyValidation() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;


        try {
            db.beginTransactionNonExclusive();


            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SURVEY


                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    //   + " AND " +DatabaseHelper.KEY_DATE_IN  + " = '" +customutility.getCurrentDate() + "'"
                    + " AND " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'";


            cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                //  Log.d("getCount","" + cursor.getCount() ) ;

                survey.setEnabled(false);
                survey.setBackgroundColor(Color.parseColor("#8b8b8c"));
                survey.setTextColor(Color.parseColor("#ffffff"));


                // db.setTransactionSuccessful();

            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            //   db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void noOrderValidation() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;


        try {
            db.beginTransactionNonExclusive();


            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NO_ORDER
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_BUDAT + " = '" + new CustomUtility().getCurrentDate() + "'"
                    + " AND " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'";


            cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                //  Log.d("getCount","" + cursor.getCount() ) ;

                no_order.setEnabled(false);
                no_order.setBackgroundColor(Color.parseColor("#8b8b8c"));
                no_order.setTextColor(Color.parseColor("#ffffff"));


                // db.setTransactionSuccessful();

            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            //   db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }


}
