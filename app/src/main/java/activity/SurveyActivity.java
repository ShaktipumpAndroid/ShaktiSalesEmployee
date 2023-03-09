package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.BuildConfig;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.WebURL;

public class SurveyActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Button btn_save,
            btn_front_view, btn_inner_view, btn_visiting_view, btn_owner_photo, btn_other_view;
    Context context;
    EditText editText_remark;
    String userid, username, customer_name, phone_number, route_code, remark,
            outer_view = "",
            inner_view = "",
            other_view = "",
            owner_view = "",
            card_view = "";
    ImageView imageInner = null;
    ImageView imageOwner = null;
    ImageView imageFront = null;
    ImageView imageVisiting = null;
    View layoutVisiting = null;
    View layoutFront = null;
    View layoutInner = null;
    View layoutOwner = null;
    boolean outer_flag = false,
            inner_flag = false,
            other_flag = false,
            owner_flag = false,
            card_flag = false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SurveyActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private Uri NewfileUri;
    private TextInputLayout inputLayoutName;
    private Uri fileUri; // file url to store image

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


//    void getCamera() {
//
//        startActivityForResult(AndroidCameraExample.getIntent(context, "save"), 100);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        context = this;
        SurveyActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Survey");


        Bundle bundle = getIntent().getExtras();

        customer_name = bundle.getString("customer_name");
        phone_number = bundle.getString("phone_number");
        route_code = bundle.getString("route_code");

        btn_save = (Button) findViewById(R.id.btn_survey_save);
        btn_front_view = (Button) findViewById(R.id.btn_front_view);
        btn_inner_view = (Button) findViewById(R.id.btn_inner_view);
        btn_visiting_view = (Button) findViewById(R.id.btn_visiting_view);
        btn_owner_photo = (Button) findViewById(R.id.btn_owner_photo);
        //  btn_other_view = (Button) findViewById(R.id.btn_other_view);


        imageInner = (ImageView) findViewById(R.id.imageInner);
        imageOwner = (ImageView) findViewById(R.id.imageOwner);
        imageFront = (ImageView) findViewById(R.id.imageFront);
        imageVisiting = (ImageView) findViewById(R.id.imageVisiting);


        layoutVisiting = findViewById(R.id.layoutVisiting);
        layoutFront = findViewById(R.id.layoutFront);
        layoutInner = findViewById(R.id.layoutInner);
        layoutOwner = findViewById(R.id.layoutOwner);


        editText_remark = (EditText) findViewById(R.id.text_survey_remark);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");
        LoginBean.username = pref.getString("key_ename", "username");


        userid = LoginBean.getUseid();
        username = LoginBean.getUsername();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validatePhoto()) {
                    if (submitForm()) {


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Data Save alert !");
                        alertDialog.setMessage("Do you want to save data ?");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                save_survey_sap();
//
                                editText_remark.setText("");
                                imageInner.setImageResource(android.R.color.transparent);
                                imageOwner.setImageResource(android.R.color.transparent);
                                imageFront.setImageResource(android.R.color.transparent);
                                imageVisiting.setImageResource(android.R.color.transparent);


                                layoutFront.setVisibility(View.INVISIBLE);
                                layoutInner.setVisibility(View.INVISIBLE);
                                layoutVisiting.setVisibility(View.INVISIBLE);
                                layoutOwner.setVisibility(View.INVISIBLE);

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


                        //}
                    }
                }
            }
        });


        btn_front_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCamera();

//                outer_flag = true;
//                inner_flag = false;
//                other_flag = false;
//                owner_flag = false;
//                card_flag = false;

                editor.putString("key_outer_flag", "TRUE");
                editor.putString("key_inner_flag", "FALSE");
                editor.putString("key_other_flag", "FALSE");
                editor.putString("key_owner_flag", "FALSE");
                editor.putString("key_card_flag", "FALSE");

                editor.commit(); //


            }
        });


        btn_inner_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openCamera();
//
//                inner_flag = true;
//                outer_flag = false;
//                other_flag = false;
//                owner_flag = false;
//                card_flag = false;


                editor.putString("key_outer_flag", "FALSE");
                editor.putString("key_inner_flag", "TRUE");
                editor.putString("key_other_flag", "FALSE");
                editor.putString("key_owner_flag", "FALSE");
                editor.putString("key_card_flag", "FALSE");

                editor.commit(); //

            }
        });


        btn_visiting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCamera();

//                    card_flag = true;
//                    outer_flag = false;
//                    inner_flag = false;
//                    other_flag = false;
//                    owner_flag = false;


                editor.putString("key_outer_flag", "FALSE");
                editor.putString("key_inner_flag", "FALSE");
                editor.putString("key_other_flag", "FALSE");
                editor.putString("key_owner_flag", "FALSE");
                editor.putString("key_card_flag", "TRUE");

                editor.commit(); //


            }
        });


        btn_owner_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openCamera();
//                    owner_flag = true;
//                    outer_flag = false;
//                    inner_flag = false;
//                    other_flag = false;
//                    card_flag = false;


                editor.putString("key_outer_flag", "FALSE");
                editor.putString("key_inner_flag", "FALSE");
                editor.putString("key_other_flag", "FALSE");
                editor.putString("key_owner_flag", "TRUE");
                editor.putString("key_card_flag", "FALSE");

                editor.commit(); //


            }
        });


//        btn_other_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (submitForm() == true) {
//
//                    outer_flag = false;
//                    inner_flag = false;
//                    other_flag = true;
//                    owner_flag = false;
//                    card_flag = false;
//
//                    getCamera();
//
//                }
//            }
//        });


    }

    public void save_survey_sap() {


        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();
        remark = editText_remark.getText().toString();

        // insert data in survey table  for send data in sap
        dataHelper.insertSurvey
                (userid.trim(),
                        customUtility.getCurrentDate(),
                        customUtility.getCurrentTime(),
                        remark,
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        outer_view, // outer view
                        inner_view, // inner view
                        other_view, // other view
                        owner_view, // owner view
                        card_view, // card view
                        phone_number);


        // insert data in survey view table
        dataHelper.insertViewSurvey(username,
                customUtility.getCurrentDate(),
                remark,
                outer_view, // outer view
                inner_view, // inner view
                other_view, // other view
                owner_view, // owner view
                card_view, // card view
                phone_number,
                "mobile_app");


// update service center survey
        dataHelper.updateServiceCenterSurvey(phone_number, String.valueOf(latitude) + "," + String.valueOf(longitude));


        // survey
        //  new Capture_employee_gps_location(this,"9",phone_number);

        Toast.makeText(context, "Survey saved successfully", Toast.LENGTH_LONG).show();
        SyncSurveyInBackground();


    }

    public void SyncSurveyInBackground() {

        Intent i = new Intent(SurveyActivity.this, SyncDataService.class);
        startService(i);


//        progressDialog = ProgressDialog.show(SurveyActivity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(SurveyActivity.this))
//                {
//
//                    Intent i = new Intent(SurveyActivity.this, SyncDataService.class);
//                    i.putExtra("sync_data", "sync_survey");
//                    startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "Survey Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . Survey saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();


    }

    public boolean submitForm() {

        boolean value;
        if (
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

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }

    private boolean validateComment() {
        if (editText_remark.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Please Enter Survey Remark");

            requestFocus(editText_remark);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    private boolean validatePhoto() {
        if (outer_view.isEmpty() &&
                inner_view.isEmpty() &&
                owner_view.isEmpty() &&
                card_view.isEmpty()) {
            Toast.makeText(this, "Please Take at least One Photo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /***************************** new camera code *****************************************/

    public void openCamera() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        // ********** code comment for api 26 ************************
        // *  intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // ********** code comment for api 26 ************************


        // ******* add new code for api 26 *************
        File dir = new File(fileUri.getPath());


        NewfileUri = FileProvider.getUriForFile(SurveyActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                dir);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, NewfileUri);

        // ******* add new code for api 26 *************


        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

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


                if (pref.getString("key_outer_flag", "survey").equalsIgnoreCase("TRUE")) {
                    outer_view = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }


                if (pref.getString("key_inner_flag", "survey").equalsIgnoreCase("TRUE")) {
                    inner_view = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

                if (pref.getString("key_card_flag", "survey").equalsIgnoreCase("TRUE")) {
                    card_view = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }


                if (pref.getString("key_owner_flag", "survey").equalsIgnoreCase("TRUE")) {
                    owner_view = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }



                /* show image preview */

                options.inSampleSize = 4;
                final Bitmap bitmap_preview = BitmapFactory.decodeFile(fileUri.getPath(), options);


                ByteArrayOutputStream stream_preview = new ByteArrayOutputStream();
                bitmap_preview.compress(Bitmap.CompressFormat.PNG, 100, stream_preview);


                View layoutVisiting = findViewById(R.id.layoutVisiting);
                View layoutFront = findViewById(R.id.layoutFront);
                View layoutInner = findViewById(R.id.layoutInner);
                View layoutOwner = findViewById(R.id.layoutOwner);


                if (pref.getString("key_outer_flag", "survey").equalsIgnoreCase("TRUE")) {

                    imageFront.setImageBitmap(bitmap_preview);
                    layoutFront.setVisibility(View.VISIBLE);

                }

                if (pref.getString("key_inner_flag", "survey").equalsIgnoreCase("TRUE")) {

                    imageInner.setImageBitmap(bitmap_preview);
                    layoutInner.setVisibility(View.VISIBLE);
                }

                if (pref.getString("key_card_flag", "survey").equalsIgnoreCase("TRUE")) {

                    //Log.d("image2",""+""+ bitmap_preview);

                    imageVisiting.setImageBitmap(bitmap_preview);
                    layoutVisiting.setVisibility(View.VISIBLE);
                }

                if (pref.getString("key_owner_flag", "survey").equalsIgnoreCase("TRUE")) {

                    imageOwner.setImageBitmap(bitmap_preview);
                    layoutOwner.setVisibility(View.VISIBLE);
                }


// delete file from phone
                File file = new File(fileUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


//                outer_view = "",
//                        inner_view = "",
//                        other_view = "",
//                        owner_view = "",
//                        card_view = ""


//                if  (validatePhoto() == true )
//                {
//
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                    alertDialog.setTitle("Data Save alert !");
//                    alertDialog.setMessage("Do you want to save data ?");
//
//                    // On pressing Settings button
//                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
////                            save_no_order_comment();
////                            editText_comment.setText("");
//                        }
//                    });
//
//
//                    // on pressing cancel button
//
//                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();
//
//                }
            }

        }
    }


    /*****************************  end camera code *****************************************/


}
