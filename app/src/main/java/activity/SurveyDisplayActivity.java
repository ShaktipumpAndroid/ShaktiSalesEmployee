package activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import database.DatabaseHelper;

public class SurveyDisplayActivity extends AppCompatActivity {
    Context context;
    TextView survey_date, survey_by, remark;
    String card_view = "",
            inner_view = "",
            owner_view = "",
            outer_view = "",
            phone_number,
            download_from;
    View layoutVisiting = null;
    View layoutFront = null;
    View layoutInner = null;
    View layoutOwner = null;
    ImageView imageInner = null;
    ImageView imageOwner = null;
    ImageView imageFront = null;
    ImageView imageVisiting = null;
    private Toolbar mToolbar;

    public static byte[] hexStringToByteArray(String hex) {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_display);

        context = this;
        SurveyDisplayActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        imageVisiting = (ImageView) findViewById(R.id.imageVisiting);

        survey_date = (TextView) findViewById(R.id.tv_survey_date);
        survey_by = (TextView) findViewById(R.id.tv_survey_by);
        remark = (TextView) findViewById(R.id.tv_remark);


        imageInner = (ImageView) findViewById(R.id.imageInner);
        imageOwner = (ImageView) findViewById(R.id.imageOwner);
        imageFront = (ImageView) findViewById(R.id.imageFront);
        imageVisiting = (ImageView) findViewById(R.id.imageVisiting);


        layoutVisiting = findViewById(R.id.layoutVisiting);
        layoutFront = findViewById(R.id.layoutFront);
        layoutInner = findViewById(R.id.layoutInner);
        layoutOwner = findViewById(R.id.layoutOwner);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" View Survey ");


        Bundle bundle = getIntent().getExtras();
        phone_number = bundle.getString("phone_number");


        getSurvey();


        //    Log.d("download_from",download_from );

        if (download_from != null && !download_from.isEmpty()) {

            if (download_from.equalsIgnoreCase("mobile_app")) {
                setMobileImage();
            }


            if (download_from.equalsIgnoreCase("sap")) {
                setSapImage();
            }

        }


    }

    public void setMobileImage() {


        if (card_view != null && !card_view.isEmpty()) {

            byte[] encodeByte = Base64.decode(card_view, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);


            imageVisiting.setImageBitmap(bitmap);
            layoutVisiting.setVisibility(View.VISIBLE);
        }


        if (outer_view != null && !outer_view.isEmpty()) {

            byte[] encodeByte = Base64.decode(outer_view, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            imageFront.setImageBitmap(bitmap);
            layoutFront.setVisibility(View.VISIBLE);
        }


        if (inner_view != null && !inner_view.isEmpty()) {

            byte[] encodeByte = Base64.decode(inner_view, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            imageInner.setImageBitmap(bitmap);
            layoutInner.setVisibility(View.VISIBLE);
        }

        if (owner_view != null && !owner_view.isEmpty()) {

            byte[] encodeByte = Base64.decode(owner_view, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            imageOwner.setImageBitmap(bitmap);
            layoutOwner.setVisibility(View.VISIBLE);
        }


    }

    public void setSapImage() {

        if (card_view != null && !card_view.isEmpty()) {
            byte[] bytes = hexStringToByteArray(card_view);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageVisiting.setImageBitmap(bitmap);
            layoutVisiting.setVisibility(View.VISIBLE);
        }

        if (outer_view != null && !outer_view.isEmpty()) {
            byte[] bytes = hexStringToByteArray(outer_view);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageFront.setImageBitmap(bitmap);
            layoutFront.setVisibility(View.VISIBLE);
        }


        if (inner_view != null && !inner_view.isEmpty()) {
            byte[] bytes = hexStringToByteArray(inner_view);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageInner.setImageBitmap(bitmap);
            layoutInner.setVisibility(View.VISIBLE);
        }

        if (owner_view != null && !owner_view.isEmpty()) {
            byte[] bytes = hexStringToByteArray(owner_view);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageOwner.setImageBitmap(bitmap);
            layoutOwner.setVisibility(View.VISIBLE);
        }


    }

    public void getSurvey() {

        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_VIEW_SURVEY
                    + " WHERE " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'";


            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    card_view = cursor.getString(cursor.getColumnIndex("card_view"));
                    outer_view = cursor.getString(cursor.getColumnIndex("outer_view"));
                    inner_view = cursor.getString(cursor.getColumnIndex("inner_view"));
                    owner_view = cursor.getString(cursor.getColumnIndex("owner_view"));
                    download_from = cursor.getString(cursor.getColumnIndex("download_from"));


                    survey_by.setText(cursor.getString(cursor.getColumnIndex("ename")));
                    survey_date.setText(cursor.getString(cursor.getColumnIndex("budat")));
                    remark.setText(cursor.getString(cursor.getColumnIndex("comment")));

                    //Log.d("image",""+""+ string_image );
                }


            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database

        }

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


}
