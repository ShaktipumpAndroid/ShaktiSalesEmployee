package activity.complaint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;

import activity.CustomUtility;
import bean.ComplaintImage;
import database.DatabaseHelper;
import other.PathUtil;
import searchlist.complaint.Complaint_Attachment_Name;
import webservice.SAPWebService;

public class ComplaintAttachmentActivity extends AppCompatActivity {

    Context mContext;
    ArrayList<Complaint_Attachment_Name> arraylist = new ArrayList<Complaint_Attachment_Name>();
    String cmp_no = "";

    private Handler progressBarHandler = new Handler();
    private Toolbar mToolbar;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    SAPWebService con = null;
    TextView image1,image2,image3,image4,image5,image6,image7,image8,image9,image10,image11,image12,image13,image14,image15;

    DatabaseHelper db;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ComplaintAttachmentActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview_complaint_image1);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        db = new DatabaseHelper(mContext);

        con = new SAPWebService();

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Image");

        image1 = (TextView) findViewById(R.id.photo1);
        image2 = (TextView) findViewById(R.id.photo2);
        image3 = (TextView) findViewById(R.id.photo3);
        image4 = (TextView) findViewById(R.id.photo4);
        image5 = (TextView) findViewById(R.id.photo5);
        image6 = (TextView) findViewById(R.id.photo6);
        image7 = (TextView) findViewById(R.id.photo7);
        image8 = (TextView) findViewById(R.id.photo8);
        image9 = (TextView) findViewById(R.id.photo9);
        image10 = (TextView) findViewById(R.id.photo10);
        image11 = (TextView) findViewById(R.id.photo11);
        image12 = (TextView) findViewById(R.id.photo12);
        image13 = (TextView) findViewById(R.id.photo13);
        image14 = (TextView) findViewById(R.id.photo14);
        image15 = (TextView) findViewById(R.id.photo15);


        cmp_no = PathUtil.getSharedPreferences(mContext, "cmpno");

        Log.e("cmpno","&&&"+cmp_no);

     /*   if(CustomUtility.isOnline(mContext))
        {
            if (db.isRecordExist(db.TABLE_CMP_ATTACHMENT, db.KEY_CMPNO, cmp_no)) {
                progressBarStatus = 20;
                getComplaint_Image(cmp_no);
            }
            else {
                download(cmp_no);
                getComplaint_Image(cmp_no);
            }
        }
        else{
            progressBarStatus = 20;
            getComplaint_Image(cmp_no);
            Toast.makeText(mContext, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
        }
*/
        if (db.isRecordExist(db.TABLE_CMP_ATTACHMENT, db.KEY_CMPNO, cmp_no)) {
            progressBarStatus = 20;
            getComplaint_Image(cmp_no);
        }
        else {
           // download(cmp_no);
            getComplaint_Image(cmp_no);
        }

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image1");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image2");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });


        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image3");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });


        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image4");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image5");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image6");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image7");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image8");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image9");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image10");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image11");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image12");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image13");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image14");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

        image15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_display_image = new Intent(mContext, ShowDocument1.class);
                Bundle extras = new Bundle();
                extras.putString("key", "image15");
                extras.putString("cmpno", cmp_no);
                i_display_image.putExtras(extras);
                startActivity(i_display_image);

            }
        });

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


    @SuppressLint("Range")
    public void getComplaint_Image(String cmp_no1) {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CMP_ATTACHMENT
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no1 + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    Complaint_Attachment_Name attach_name = new Complaint_Attachment_Name();

                    attach_name.setImage1(cursor.getString(cursor.getColumnIndex("image1")));
                    attach_name.setImage2(cursor.getString(cursor.getColumnIndex("image2")));
                    attach_name.setImage3(cursor.getString(cursor.getColumnIndex("image3")));
                    attach_name.setImage4(cursor.getString(cursor.getColumnIndex("image4")));
                    attach_name.setImage5(cursor.getString(cursor.getColumnIndex("image5")));
                    attach_name.setImage6(cursor.getString(cursor.getColumnIndex("image6")));
                    attach_name.setImage7(cursor.getString(cursor.getColumnIndex("image7")));
                    attach_name.setImage8(cursor.getString(cursor.getColumnIndex("image8")));
                    attach_name.setImage9(cursor.getString(cursor.getColumnIndex("image9")));
                    attach_name.setImage10(cursor.getString(cursor.getColumnIndex("image10")));
                    attach_name.setImage11(cursor.getString(cursor.getColumnIndex("image11")));
                    attach_name.setImage12(cursor.getString(cursor.getColumnIndex("image12")));
                    attach_name.setImage13(cursor.getString(cursor.getColumnIndex("image13")));
                    attach_name.setImage14(cursor.getString(cursor.getColumnIndex("image14")));
                    attach_name.setImage15(cursor.getString(cursor.getColumnIndex("image15")));
                    arraylist.add(attach_name);

                    for(int i= 0; i < arraylist.size(); i++ )
                    {
                        if (!TextUtils.isEmpty(arraylist.get(i).getImage1())) {
                            image1.setVisibility(View.VISIBLE);
                            image1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);

                        }
                        else{
                            image1.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(arraylist.get(i).getImage2())) {
                            image2.setVisibility(View.VISIBLE);
                            image2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image2.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(arraylist.get(i).getImage3())) {
                            image3.setVisibility(View.VISIBLE);
                            image3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image3.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(arraylist.get(i).getImage4())) {
                            image4.setVisibility(View.VISIBLE);
                            image4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image4.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage5())) {
                            image5.setVisibility(View.VISIBLE);
                            image5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image5.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage6())) {
                            image6.setVisibility(View.VISIBLE);
                            image6.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image6.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage7())) {
                            image7.setVisibility(View.VISIBLE);
                            image7.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image7.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage8())) {
                            image8.setVisibility(View.VISIBLE);
                            image8.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image8.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage9())) {
                            image9.setVisibility(View.VISIBLE);
                            image9.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image9.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage10())) {
                            image10.setVisibility(View.VISIBLE);
                            image10.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image10.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage11())) {
                            image11.setVisibility(View.VISIBLE);
                            image11.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image11.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage12())) {
                            image12.setVisibility(View.VISIBLE);
                            image12.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image12.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(arraylist.get(i).getImage13())) {
                            image13.setVisibility(View.VISIBLE);
                            image13.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image13.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(arraylist.get(i).getImage14())) {
                            image14.setVisibility(View.VISIBLE);
                            image14.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image14.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(arraylist.get(i).getImage15())) {
                            image15.setVisibility(View.VISIBLE);
                            image15.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
                        }
                        else{
                            image15.setVisibility(View.GONE);
                        }



                    }


                }

            }
            progressBarStatus = 100;
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            progressBarStatus = 100;
            e.printStackTrace();

        } finally {
            progressBarStatus = 100;
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            db.close();
            // Close database

        }


    }


   /* public void download(String cmpno1) {


        // creating progress bar dialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        // progressBar.setCancelable(true);
        progressBar.setMessage("Downloading Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        //reset progress bar and filesize status
        progressBarStatus = 0;

        new Thread(new Runnable() {
            public void run() {


                if (CustomUtility.isOnline(ComplaintAttachmentActivity.this)) {


                    while (progressBarStatus < 100) {

                        try {
                                progressBarStatus = 20;
                                progressBarStatus = con.getCmpAttachData(ComplaintAttachmentActivity.this, cmpno1);


                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {

                                    progressBar.setProgress(progressBarStatus);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    //                  performing operation if file is downloaded,
                    // sleeping for 1 second after operation completed
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();


                } else {
                    progressBar.dismiss();
                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                }


            }

        }).start();


    }
*/


}
