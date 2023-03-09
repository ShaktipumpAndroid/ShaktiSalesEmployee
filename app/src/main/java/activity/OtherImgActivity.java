package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import database.DatabaseHelper;
import model.ImageModel;
import other.CameraUtils;
import webservice.Constants;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class OtherImgActivity extends AppCompatActivity {


    public static final int MEDIA_TYPE_IMAGE = 1;

    Context mContext;
    DatabaseHelper dataHelper;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    File file;
    ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private static final String GALLERY_DIRECTORY_NAME_COMMON = "SurfaceCamera";
//    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";

    String imageStoragePath, customer_name, lat, lng, helpnm, comment, phn, audio, citynm, folowupdt, conv_sta, photo1_text, photo2_text, photo3_text;
    TextView photo1, photo2, photo3;
    boolean photo1_flag = false,
            photo2_flag = false,
            photo3_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheract_image);
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
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);

        setData();

        photo1.setOnClickListener(view -> {


            if (photo1_text == null || photo1_text.isEmpty()) {

                showConfirmationGallery(Constants.photo1, "PHOTO_1");

            } else {

                showConfirmationAlert(Constants.photo1, photo1_text, "PHOTO_1");

            }
        });

        photo2.setOnClickListener(view -> {


            if (photo2_text == null || photo2_text.isEmpty()) {
                showConfirmationGallery(Constants.photo2, "PHOTO_2");
            } else {

                showConfirmationAlert(Constants.photo2, photo2_text, "PHOTO_2");

            }

        });

        photo3.setOnClickListener(view -> {


            if (photo3_text == null || photo3_text.isEmpty()) {
                showConfirmationGallery(Constants.photo3, "PHOTO_3");
            } else {
                showConfirmationAlert(Constants.photo3, photo3_text, "PHOTO_3");
            }

        });


    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

            camraLauncher.launch(new Intent(OtherImgActivity.this, CameraActivity.class).putExtra("cust_name", customer_name));

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

                            ImageModel imageModel = new ImageModel();
                            if (photo1_flag) {
                                imageModel.setFlag(Constants.photo1);
                                imageModel.setImage(bundle.get("data").toString());
                                photo1_text = bundle.get("data").toString();
                                setIcon(Constants.photo1);
                            }
                            if(photo2_flag){
                                imageModel.setFlag(Constants.photo2);
                                imageModel.setImage(bundle.get("data").toString());
                                photo2_text = bundle.get("data").toString();
                                setIcon(Constants.photo2);
                            }
                            if(photo3_flag){
                                imageModel.setFlag(Constants.photo3);
                                imageModel.setImage(bundle.get("data").toString());
                                photo3_text = bundle.get("data").toString();
                                setIcon(Constants.photo3);
                            }

                            imageModelArrayList.add(imageModel);
                            CustomUtility.saveArrayList(OtherImgActivity.this,imageModelArrayList);

                          Log.e("imageModelArrayList", String.valueOf(imageModelArrayList.size()));
                        }

                    }
                }
            });

    //save Image arraylist in SharedPreferences
  

    private void setData() {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
      imageModels = CustomUtility.getArrayList(OtherImgActivity.this);

        if(imageModels!=null && imageModels.size()>0) {
            for (int i = 0; i < imageModels.size(); i++) {

                if (imageModels.get(i).getFlag().equals(Constants.photo1)) {
                    photo1_text = imageModels.get(i).getImage();
                    setIcon(Constants.photo1);
                }
                if (imageModels.get(i).getFlag().equals(Constants.photo2)) {
                    photo2_text = imageModels.get(i).getImage();
                    setIcon(Constants.photo2);
                }
                if (imageModels.get(i).getFlag().equals(Constants.photo3)) {
                    photo3_text = imageModels.get(i).getImage();
                    setIcon(Constants.photo3);
                }
            }
        }
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


    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Display", (dialog, which) -> {


            displayImage(keyimage, data);
        });

        alertDialog.setNegativeButton("Change", (dialog, which) -> showConfirmationGallery(keyimage, name));

        // Showing Alert Message
        alertDialog.show();
    }


    private void displayImage(String key, String data) {

        Intent i_display_image = new Intent(mContext, ShowDocument2.class);
        Bundle extras = new Bundle();
        extras.putString("customer_name", customer_name);
        extras.putString("key", key);
        extras.putString("image_path", data);

        //CustomUtility.setSharedPreference(mContext, "data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = CustomUtility.checkPermission(mContext);
            if (items[item].equals("Take Photo")) {

                if (result) {
                    openCamera(name);
                    setFlag(keyimage);
                }

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void Save() {
        if (!TextUtils.isEmpty(photo1_text)) {
            if (!TextUtils.isEmpty(photo2_text)) {
                if (!TextUtils.isEmpty(photo3_text)) {
                    CustomUtility.setSharedPreference(mContext, "ServiceCenterTRN" + customer_name, "1");
                } else {
                    Toast.makeText(this, "Please Select Participants Contact Detail photo.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Select Participants Photo.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please Select Sales Activity Site Photo.", Toast.LENGTH_SHORT).show();
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
                conv_sta, photo1_text, photo2_text, photo3_text, "", "", "", "");*/
       /* dataHelper.insertCheckInOut(pref.getString("key_username", "userid"),
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
                "",
                "",
                "",
                "",
                CustomerDetailBean.getPhone_number());

        Log.e("doesTableExist123====>", String.valueOf(dataHelper.doesTableExist( pref.getString("key_username", "userid"))));
*/
    }


    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;


        switch (key) {

            case Constants.photo1:
                photo1_flag = true;
                break;
            case Constants.photo2:
                photo2_flag = true;
                break;
            case Constants.photo3:
                photo3_flag = true;
                break;

        }

    }

    public void setIcon(String key) {


        switch (key) {

            case Constants.photo1:
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case Constants.photo2:
                if (photo2_text == null || photo2_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

            case Constants.photo3:
                if (photo3_text == null || photo3_text.isEmpty()) {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
