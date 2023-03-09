package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.shaktipumps.shakti.shaktisalesemployee.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

import bean.CustomerDetailBean;
import database.DatabaseHelper;
import other.CameraUtils;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class ServiceCenterTrainingImgActivity extends AppCompatActivity {

    public static final int BITMAP_SAMPLE_SIZE = 8;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    Context mContext;
    DatabaseHelper dataHelper;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";

    String imageStoragePath, customer_name,lat,lng,helpnm,comment,phn,audio,citynm,folowupdt,conv_sta, photo1_text, photo2_text, photo3_text, photo4_text, photo5_text, photo6_text, photo7_text;
    TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false,
            photo4_flag = false,
            photo5_flag = false,
            photo6_flag = false,
            photo7_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instreport_image);
        mContext = this;

        dataHelper = new DatabaseHelper(mContext);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        Bundle bundle = getIntent().getExtras();
        
        lat = bundle.getString("lat");
        lng = bundle.getString("lng");
        comment = bundle.getString("comment");
        phn = bundle.getString("phn");
        helpnm = bundle.getString("helpnm");
        audio = bundle.getString("audio");
        customer_name = bundle.getString("customer_name");
        citynm = bundle.getString("citynm");
        folowupdt = bundle.getString("folowupdt");
        conv_sta = bundle.getString("conv_sta");

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + "/SSAPP/TRAN/"); //it is my root directory

        File billno = new File(root.getAbsolutePath() + "/SSAPP/TRAN/" + customer_name); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo1 = (TextView) findViewById(R.id.photo1);
        photo2 = (TextView) findViewById(R.id.photo2);
        photo3 = (TextView) findViewById(R.id.photo3);
        photo4 = (TextView) findViewById(R.id.photo4);
        photo5 = (TextView) findViewById(R.id.photo5);
        photo6 = (TextView) findViewById(R.id.photo6);
        photo7 = (TextView) findViewById(R.id.photo7);

        setData();

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo1_text == null || photo1_text.isEmpty()) {

                    showConfirmationGallery("photo1", "PHOTO_1");
                } else {


                    showConfirmationAlert("photo1", photo1_text, "PHOTO_1");

                }
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo2_text == null || photo2_text.isEmpty()) {
                    showConfirmationGallery("photo2", "PHOTO_2");
                } else {

                    showConfirmationAlert("photo2", photo2_text, "PHOTO_2");

                }

            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo3_text == null || photo3_text.isEmpty()) {
                    showConfirmationGallery("photo3", "PHOTO_3");
                } else {

                    showConfirmationAlert("photo3", photo3_text, "PHOTO_3");

                }

            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photo4_text == null || photo4_text.isEmpty()) {
                    showConfirmationGallery("photo4", "PHOTO_4");
                } else {

                    showConfirmationAlert("photo4", photo4_text, "PHOTO_4");

                }

            }
        });

        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo5_text == null || photo5_text.isEmpty()) {
                    showConfirmationGallery("photo5", "PHOTO_5");
                } else {
                    showConfirmationAlert("photo5", photo5_text, "PHOTO_5");


                }

            }
        });

        photo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo6_text == null || photo6_text.isEmpty()) {
                    showConfirmationGallery("photo6", "PHOTO_6");
                } else {
                    showConfirmationAlert("photo6", photo6_text, "PHOTO_6");

                }

            }
        });

        photo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (photo7_text == null || photo7_text.isEmpty()) {
                    showConfirmationGallery("photo7", "PHOTO_7");
                } else {

                    showConfirmationAlert("photo7", photo7_text, "PHOTO_7");

                }

            }
        });



    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = CameraUtils.getOutputMediaFile1(MEDIA_TYPE_IMAGE,customer_name,name);

                if (file != null) {
                    imageStoragePath = file.getAbsolutePath();
                    Log.e("PATH","&&&"+imageStoragePath);
                }

                Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

                Log.e("PATH","&&&"+fileUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);



        }


    }


    private void setData() {

        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            photo1_text = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            photo2_text = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            photo3_text = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            photo4_text = file3.getAbsolutePath();
        }

        File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_5.jpg");
        if (file4.exists()) {
            photo5_text = file4.getAbsolutePath();
        }

        File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_6.jpg");
        if (file5.exists()) {
            photo6_text = file5.getAbsolutePath();
        }

        File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_7.jpg");
        if (file6.exists()) {
            photo7_text = file6.getAbsolutePath();
        }


        setIcon("photo1");
        setIcon("photo2");
        setIcon("photo3");
        setIcon("photo4");
        setIcon("photo5");
        setIcon("photo6");
        setIcon("photo7");



    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            try {

                Log.e("Count", "&&&&&" + imageStoragePath);

                Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                int count = bitmap.getByteCount();

                Log.e("Count", "&&&&&" + count);

                Log.e("IMAGEURI", "&&&&" + imageStoragePath);

                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);

                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                long size = byteArray.length;

                int count1 = bitmap.getByteCount();

                Log.e("Count1", "&&&&&" + count1);


                Log.e("SIZE1234", "&&&&" + size);

                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));


                if (photo1_flag == true) {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {
                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon("photo1");
                        // CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_1", photo1_text);
                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(mContext, customer_name + "PHOTO_1"));

                    }

                }

                if (photo2_flag == true) {
                    File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_2.jpg");
                    if (file1.exists()) {
                        photo2_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon("photo2");
                        // CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_2", photo2_text);
                        Log.e("SIZE2", "&&&&" + photo2_text);

                    }
                }
                if (photo3_flag == true) {
                    File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_3.jpg");
                    if (file2.exists()) {
                        photo3_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        // CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_3", photo3_text);
                        Log.e("SIZE3", "&&&&" + photo3_text);
                        setIcon("photo3");
                    }
                }

                if (photo4_flag == true) {
                    File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_4.jpg");
                    if (file3.exists()) {
                        photo4_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        // CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_4", photo4_text);
                        Log.e("SIZE4", "&&&&" + photo4_text);
                        setIcon("photo4");
                    }
                }

                if (photo5_flag == true) {
                    File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_5.jpg");
                    if (file4.exists()) {
                        photo5_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_5", photo5_text);
                        Log.e("SIZE5", "&&&&" + photo5_text);
                        setIcon("photo5");
                    }
                }

                if (photo6_flag == true) {
                    File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_6.jpg");
                    if (file5.exists()) {
                        photo6_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        // CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_6", photo6_text);
                        Log.e("SIZE6", "&&&&" + photo6_text);
                        setIcon("photo6");
                    }
                }

                if (photo7_flag == true) {
                    File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_7.jpg");
                    if (file6.exists()) {
                        photo7_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //CustomUtility.setSharedPreference(mContext, customer_name + "PHOTO_7", photo7_text);
                        Log.e("SIZE7", "&&&&" + photo7_text);
                        setIcon("photo7");
                    }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

             /*   File file = newworkorder File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }*/

        }

    }


    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Display", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Log.e("KEY", "&&&&" + keyimage);
                Log.e("DATA", "&&&&" + data);

                displayImage(keyimage, data);


            }
        });

        alertDialog.setNegativeButton("Change", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                showConfirmationGallery(keyimage, name);


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void displayImage(String key, String data) {
        Intent i_display_image = new Intent(mContext, ShowDocument2.class);
        Bundle extras = new Bundle();
        //saveData();
        extras.putString("customer_name", customer_name);
        extras.putString("key", key);
        extras.putString("data", "TRAN");

        CustomUtility.setSharedPreference(mContext, "data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(mContext);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                }  else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void Save() {



    /*    String pht1 = null, pht2 = null, pht3 = null, pht4 = null, pht5 = null, pht6 = null, pht7 = null;


        File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_1.jpg");
        if (file.exists()) {
            pht1 = file.getAbsolutePath();
        }

        File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_2.jpg");
        if (file1.exists()) {
            pht2 = file1.getAbsolutePath();
        }

        File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_3.jpg");
        if (file2.exists()) {
            pht3 = file2.getAbsolutePath();
        }

        File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_4.jpg");
        if (file3.exists()) {
            pht4 = file3.getAbsolutePath();
        }

        File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_5.jpg");
        if (file4.exists()) {
            pht5 = file4.getAbsolutePath();
        }

        File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_6.jpg");
        if (file5.exists()) {
            pht6 = file5.getAbsolutePath();
        }

        File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_name, "/IMG_PHOTO_7.jpg");
        if (file6.exists()) {
            pht7 = file6.getAbsolutePath();
        }*/

            if (!TextUtils.isEmpty(photo1_text)) {
                if (!TextUtils.isEmpty(photo2_text)) {
                    if (!TextUtils.isEmpty(photo3_text)) {
                        if (!TextUtils.isEmpty(photo4_text)) {
                            if (!TextUtils.isEmpty(photo5_text)) {
                            if (!TextUtils.isEmpty(photo6_text)) {
                            if (!TextUtils.isEmpty(photo7_text)) {

                                CustomUtility.setSharedPreference(mContext, "ServiceCenterTRN" + customer_name, "1");

                            }else {

                                Toast.makeText(this, "Please Select Other Image.", Toast.LENGTH_SHORT).show();
                            }

                            }else {

                                Toast.makeText(this, "Please Select Product Training Image.", Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(this, "Please Select Spare Parts Stock Image.", Toast.LENGTH_SHORT).show();
                        }
                        } else {

                            Toast.makeText(this, "Please Select Selfiy with Service Person.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(this, "Please Select Certificate Image.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(this, "Please Select Service Center Training Letter.", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(this, "Please Select Service Center Board Image.", Toast.LENGTH_SHORT).show();
            }

       /* dataHelper.updateCheckInOut(pref.getString("key_username", "userid"),
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                lat,
                lng,
                phn,
                comment,
                helpnm,
                audio,
                customer_name,
                citynm,
                folowupdt, //follow_up_date
                conv_sta,photo1_text,photo2_text,photo3_text,photo4_text,photo5_text,photo6_text,photo7_text);*/
        dataHelper.updateCheckInOut(pref.getString("key_username", "userid"),
                new CustomUtility().getCurrentDate(), // check in date
                new CustomUtility().getCurrentTime(),
                "",// check in time
                lat, // check in lat
                "",
                lng,
                "",
                comment,
                helpnm,
                CustomerDetailBean.getRoute_code(),
                audio,
                "NOT",
                customer_name, citynm,
                "",
                folowupdt,
                conv_sta,
                photo1_text,
                photo2_text,
                photo3_text,
                photo4_text,
                photo5_text,
                photo6_text,
                photo7_text,
                CustomerDetailBean.getPhone_number());
    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;
        photo4_flag = false;
        photo5_flag = false;
        photo6_flag = false;

        photo7_flag = false;


        switch (key) {

            case "photo1":
                photo1_flag = true;
                break;
            case "photo2":
                photo2_flag = true;
                break;
            case "photo3":
                photo3_flag = true;
                break;
            case "photo4":
                photo4_flag = true;
                break;
            case "photo5":
                photo5_flag = true;
                break;
            case "photo6":
                photo6_flag = true;
                break;
            case "photo7":
                photo7_flag = true;
                break;

        }

    }

    public void setIcon(String key) {


        switch (key) {

            case "photo1":
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case "photo2":
                if (photo2_text == null || photo2_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case "photo3":
                if (photo3_text == null || photo3_text.isEmpty()) {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case "photo4":
                if (photo4_text == null || photo4_text.isEmpty()) {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case "photo5":
                if (photo5_text == null || photo5_text.isEmpty()) {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case "photo6":
                if (photo6_text == null || photo6_text.isEmpty()) {
                    photo6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case "photo7":
                if (photo7_text == null || photo7_text.isEmpty()) {
                    photo7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
