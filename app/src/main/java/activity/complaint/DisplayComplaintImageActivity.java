package activity.complaint;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import database.DatabaseHelper;

public class DisplayComplaintImageActivity extends AppCompatActivity {
    Context mContext;
    String cmp_no = "", cmp_posnr = "", image_name = "", cmp_category = "";
    ImageView complaintImage;
    TextView imageText;
    String image_data = "";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_complaint_image);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        cmp_no = bundle.getString("cmp_no");
        cmp_posnr = bundle.getString("cmp_posnr");
        cmp_category = bundle.getString("cmp_category");
        image_name = bundle.getString("image_name");

//

        String title = "Complaint No. " + cmp_no;
        getSupportActionBar().setTitle(title);


        // Log.d("cmp_no1"  ,cmp_no + cmp_posnr );

        complaintImage = (ImageView) findViewById(R.id.complaintImage);
        imageText = (TextView) findViewById(R.id.imageText);


        imageText.setText(image_name);
        get_complaint_image();


        //image_data = "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";


        if (image_data != null && !image_data.isEmpty()) {


            byte[] encodeByte = Base64.decode(image_data, Base64.DEFAULT);

            final Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            Log.e("SIZE5678", "&&&&" + encodeByte.length);

            complaintImage.setImageBitmap(bitmap);


        }

    }

    public void get_complaint_image() {

        DatabaseHelper database = new DatabaseHelper(mContext);


        SQLiteDatabase db = database.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;
        try {


            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_IMAGE
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'" + " AND " +
                    DatabaseHelper.KEY_CATEGORY + " = '" + cmp_category + "'" + " AND " +
                    DatabaseHelper.KEY_POSNR + " = '" + cmp_posnr + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {


                    //    Log.d("posnr1",""+cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));

//                    complaintImage.setKey_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
//                    complaintImage.setCmpno(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CMPNO)));
//                    complaintImage.setPosnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_POSNR)));
//                    complaintImage.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CATEGORY)));

                    image_data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_IMAGE));

                    Log.d("image_taken5", "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_IMAGE)));


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

}
