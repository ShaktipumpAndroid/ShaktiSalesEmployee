package activity.complaint;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import database.DatabaseHelper;
import searchlist.complaint.Complaint_Attachment_Name;


public class ShowDocument1 extends AppCompatActivity {

    Context context;
    String string_image = "";
    String key = "",cmp_no = "";
    byte[] encodeByte;
    Bitmap bitmap;
    DatabaseHelper db;
    ImageView imageView;
    String string_title = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document1);

        context = this;

        db = new DatabaseHelper(context);

        //Toolbar cod


        imageView = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        cmp_no = bundle.getString("cmpno");

        if (key != null) {
            switch (key) {

                case "image1":
                    string_title = "Photo 1";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image2":
                    string_title = "Photo 2";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image3":
                    string_title = "Photo 3";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image4":
                    string_title = "Photo 4";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image5":
                    string_title = "Photo 5";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image6":
                    string_title = "Photo 6";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image7":
                    string_title = "Photo 7";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image8":
                    string_title = "Photo 8";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image9":
                    string_title = "Photo 9";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image10":
                    string_title = "Photo 10";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image11":
                    string_title = "Photo 11";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image12":
                    string_title = "Photo 12";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image13":
                    string_title = "Photo 13";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image14":
                    string_title = "Photo 14";
                    getComplaint_Image(key,cmp_no);
                    break;

                case "image15":
                    string_title = "Photo 15";
                    getComplaint_Image(key,cmp_no);
                    break;

            }
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(string_title);


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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void getComplaint_Image(String key1, String cmp_no1) {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
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

                    if(key.equalsIgnoreCase("image1"))
                    {
                        string_image =   cursor.getString(cursor.getColumnIndex("image1"));
                    }
                    if(key.equalsIgnoreCase("image2"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image2"));
                    }
                    if(key.equalsIgnoreCase("image3"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image3"));
                    }
                    if(key.equalsIgnoreCase("image4"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image4"));
                    }
                    if(key.equalsIgnoreCase("image5"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image5"));
                    }
                    if(key.equalsIgnoreCase("image6"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image6"));
                    }
                    if(key.equalsIgnoreCase("image7"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image7"));
                    }
                    if(key.equalsIgnoreCase("image8"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image8"));
                    }
                    if(key.equalsIgnoreCase("image9"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image9"));
                    }
                    if(key.equalsIgnoreCase("image10"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image10"));
                    }
                    if(key.equalsIgnoreCase("image11"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image11"));
                    }
                    if(key.equalsIgnoreCase("image12"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image12"));
                    }
                    if(key.equalsIgnoreCase("image13"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image13"));
                    }
                    if(key.equalsIgnoreCase("image14"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image14"));
                    }
                    if(key.equalsIgnoreCase("image15"))
                    {
                        string_image =  cursor.getString(cursor.getColumnIndex("image15"));
                    }

                    Log.e("IMAGE","&&&&"+string_image);

                    if (string_image != null && !string_image.isEmpty()) {
                        encodeByte = Base64.decode(string_image, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        imageView.setImageBitmap(bitmap);
                    }

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



}