package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;
import webservice.WebURL;

public class NoOrderActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Spinner spinner;
    EditText editText_comment;
    SAPWebService con = null;
    List<String> list = null;
    String no_order_image = "";
    Button btn_no_order_save;
    File mFile;
    String st_img;
    Context context;
    String userid, help_name, no_order_comment, customer_name, phone_number, route_code, username;
    int index;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(NoOrderActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutName;


    private Uri fileUri; // file url to store image
    private Uri NewfileUri;


    // ;


//    void getCamera() {
//
//                startActivityForResult(AndroidCameraExample.getIntent(context, "save"), 100);
//    }


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
//                         no_order_image = CustomUtility.CompressImage(mFile.getPath());
//                        Log.d("No_order_image", "" + mFile.getPath() );
//
//
//                        if  (validatePhoto() == true )
//                        {
//
//
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                            alertDialog.setTitle("Data Save alert !");
//                            alertDialog.setMessage("Do you want to save data ?");
//
//                            // On pressing Settings button
//                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    save_no_order_comment();
//                                    editText_comment.setText("");
//                                }
//                            });
//
//
//                            // on pressing cancel button
//
//                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                            // Showing Alert Message
//                            alertDialog.show();
//
//
//                        }
//
//
//
//
//                        }
//                 catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//                }
//            }
//        }
//    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_order);

        context = this;

        NoOrderActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("No Order");

        spinner = (Spinner) findViewById(R.id.spinner);
        btn_no_order_save = (Button) findViewById(R.id.btn_no_order_save);
        editText_comment = (EditText) findViewById(R.id.text_no_order_comment);

        list = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();

        customer_name = bundle.getString("customer_name");
        phone_number = bundle.getString("phone_number");
        route_code = bundle.getString("route_code");


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");
        LoginBean.username = pref.getString("key_ename", "username");


        userid = LoginBean.getUseid();
        username = LoginBean.getUsername();


        //   get spinner value data
        selectNoOrderHelp();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                help_name = spinner.getSelectedItem().toString();

                // Toast.makeText(DsrEntryActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner.setPrompt("Select Reason");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);

        // Drop down layout style - list view with radio button
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        btn_no_order_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitForm()) {

                    //  getCamera();

                    openCamera();
                }


            }
        });


    }

    //no_order_image
    private boolean submitForm() {
        boolean value;
        if ((validateNoOrderReason()) &&

                (validateComment()) &&

                CustomUtility.CheckGPS(this) &&
                validateDate()
        ) {
            value = true;
        } else {
            value = false;
        }
        return value;
    }

    private boolean validateNoOrderReason() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select No Order Reason", Toast.LENGTH_SHORT).show();
            //  inputLayoutName.setError("Please Select Activity Type");
            value = false;
        } else {
            value = true;
        }

        return value;
    }

    private boolean validateComment() {
        if (editText_comment.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Please Enter No Order Comment");

            requestFocus(editText_comment);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoto() {
        if (no_order_image.isEmpty()) {
            Toast.makeText(this, "Please Take Photo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void selectNoOrderHelp() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        list.add("Select Reason");

        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String help_code = "2"; // 2 is use for no order search help

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
                // End the transaction.
                db.close();
                // Close database
            }

        }

    }

    public void save_no_order_comment() {


        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();
        no_order_comment = editText_comment.getText().toString();
        // dataHelper.deleteDSREntry();
        dataHelper.insertNoOrderData
                (userid,
                        new CustomUtility().getCurrentDate(),
                        new CustomUtility().getCurrentTime(),
                        help_name,
                        no_order_comment,
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        phone_number,
                        route_code,
                        no_order_image);


        /*insert value to visit history table*/

        dataHelper.insertvisitHistory(customUtility.getCurrentDate(),
                customUtility.getCurrentTime(),
                no_order_comment,
                username,
                phone_number,
                "",
                "No Order",
                "");

        /* end insert value to visit history table*/


        // no order
        // new Capture_employee_gps_location(this,"8",phone_number);

        Toast.makeText(context, "No Order saved successfully", Toast.LENGTH_LONG).show();

        SyncNoOrderInBackground();
    }

    public void SyncNoOrderInBackground() {


        Intent i = new Intent(NoOrderActivity.this, SyncDataService.class);
        startService(i);


//
//        progressDialog = ProgressDialog.show(NoOrderActivity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(NoOrderActivity.this))
//                {
//
//
//                     Intent i = new Intent(NoOrderActivity.this, SyncDataService.class);
//                     i.putExtra("sync_data", "sync_no_order_entry");
//                     startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "No Order Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . No Order saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:

                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /***************************** new camera code *****************************************/

    public void openCamera() {

        camraLauncher.launch(new Intent(NoOrderActivity.this, CameraActivity.class).putExtra("cust_name", customer_name));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();

                            Bitmap bitmap = BitmapFactory.decodeFile(bundle.get("data").toString());


                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            no_order_image = Base64.encodeToString(byteArray, Base64.DEFAULT);


                            if (validatePhoto()) {


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle("Data Save alert !");
                                alertDialog.setMessage("Do you want to save data ?");

                                // On pressing Settings button
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        save_no_order_comment();
                                        editText_comment.setText("");
                                        spinner.setSelection(0);

                                        btn_no_order_save.setEnabled(false);
                                        btn_no_order_save.setBackgroundColor(Color.parseColor("#8b8b8c"));
                                        btn_no_order_save.setTextColor(Color.parseColor("#ffffff"));


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

// delete file from memory card
                            File file = new File(fileUri.getPath());
                            if (file.exists()) {
                                file.delete();
                            }

                        }

                    }
                }
            });

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                Date dt = new Date();
                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();

                String time = "" + hours + minutes + seconds;

                String fDate = new SimpleDateFormat("yyyyMMdd").format(dt);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;


                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                no_order_image = Base64.encodeToString(byteArray, Base64.DEFAULT);


                if (validatePhoto()) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            save_no_order_comment();
                            editText_comment.setText("");
                            spinner.setSelection(0);

                            btn_no_order_save.setEnabled(false);
                            btn_no_order_save.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            btn_no_order_save.setTextColor(Color.parseColor("#ffffff"));


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

// delete file from memory card
                File file = new File(fileUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


            }

        }
    }


    /*****************************  end camera code *****************************************/


}
