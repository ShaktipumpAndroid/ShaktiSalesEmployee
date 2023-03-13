package activity.complaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import activity.CameraActivity;
import activity.CustomUtility;
import activity.OtherImgActivity;
import bean.ComplaintImage;
import bean.LoginBean;
import database.DatabaseHelper;
import model.ImageModel;
import other.PathUtil;
import searchlist.complaint.Complaint_Image_ListViewAdapter;
import searchlist.complaint.Complaint_Image_Name;
import webservice.Constants;

public class ComplaintImageActivity extends AppCompatActivity implements Complaint_Image_ListViewAdapter.ImageSelectionListner {
    private static final int REQUEST_CODE_PERMISSION = 123;
    Context mContext;
    ListView list;
    ArrayList<Complaint_Image_Name> arraylist = new ArrayList<>();
    String cmp_no = "", cmp_category = "", category_name = "";
    Complaint_Image_ListViewAdapter adapter;
    String  image_item = "";
    ArrayList<ComplaintImage> list_complaintImageTaken;
    DatabaseHelper dataHelper;

    int Index;
    Complaint_Image_Name  complaintImage;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_image);
        mContext = this;
        dataHelper = new DatabaseHelper(mContext);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        list = findViewById(R.id.listview);
        setSupportActionBar(mToolbar);
        list_complaintImageTaken = new ArrayList<>();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Image");
        cmp_no = PathUtil.getSharedPreferences(mContext, "cmp_no");
        cmp_category = PathUtil.getSharedPreferences(mContext, "cmp_category");
        String CurrentString = cmp_category;
        String[] separated;
        separated = CurrentString.split("--");
        category_name = separated[0];
        cmp_category = separated[1];

        check_complaint_image();
        getComplaint_Image();

    }

    @Override
    protected void onResume() {
        super.onResume();
          databaseHelper = new DatabaseHelper(this);
       Log.e("ComplaintImageSize", String.valueOf(databaseHelper.getComplaintImage().size()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    public void getComplaint_Image() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String lv_category, lv_image_item;
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COMPLAINT_IMAGE_NAME
                    + " WHERE " + DatabaseHelper.KEY_CATEGORY + " = '" + cmp_category + "'";
            cursor = db.rawQuery(selectQuery, null);
            // Log.d("comp",""+cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Complaint_Image_Name image_name = new Complaint_Image_Name();
                    image_name.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                    image_name.setItem(cursor.getString(cursor.getColumnIndex("image_item")));
                    image_name.setName(cursor.getString(cursor.getColumnIndex("cmp_image")));
                    arraylist.add(image_name);
                    adapter = new Complaint_Image_ListViewAdapter(this, arraylist);
                    list.setAdapter(adapter);
                    adapter.ImgSelectionListner(ComplaintImageActivity.this);
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database
        }
    }



    @SuppressLint("Range")
    public void check_complaint_image() {
        list_complaintImageTaken.clear();
        DatabaseHelper database = new DatabaseHelper(mContext);
        SQLiteDatabase db = database.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {
            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_IMAGE
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'" + " AND " +
                    DatabaseHelper.KEY_CATEGORY + " = '" + cmp_category + "'";
            cursor = db.rawQuery(selectQuery, null);
            // Log.d("cmp_image_dtl",""+cursor.getCount());
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ComplaintImage complaintImage = new ComplaintImage();
                    complaintImage.setKey_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
                    complaintImage.setCmpno(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CMPNO)));
                    complaintImage.setPosnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));
                    complaintImage.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CATEGORY)));
                    list_complaintImageTaken.add(complaintImage);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public void ImgSelectionLis(int position, Complaint_Image_Name complaintImageName) {

        if ( !(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) || !(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    REQUEST_CODE_PERMISSION);


        }else {
            Index = position;
            complaintImage = complaintImageName;
             image_item = "101" +complaintImageName.getItem();
            openCamera();
        }

    }


    private void openCamera() {
        camraLauncher.launch(new Intent(ComplaintImageActivity.this, CameraActivity.class).putExtra("cust_name",PathUtil.getSharedPreferences(mContext,"cust_name")).putExtra("FrontCamera","0"));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());


                            dataHelper.insertComplaintImage
                                    (cmp_no,
                                            image_item,
                                            cmp_category,
                                            bundle.get("data").toString());
                            updateValue();
                            Log.e("ComplaintImageSize", String.valueOf(databaseHelper.getComplaintImage().size()));

                        }

                    }
                }
            });

    private void updateValue() {
        Complaint_Image_Name complaintImageName = new Complaint_Image_Name();
        complaintImageName.setName(complaintImage.getName());
        complaintImageName.setItem(complaintImage.getItem());
        complaintImageName.setCategory(complaintImage.getCategory());
        complaintImageName.setImgSelected(true);
        arraylist.set(Index,complaintImageName);
        adapter.notifyDataSetChanged();
    }
}
