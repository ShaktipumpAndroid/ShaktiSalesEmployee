package activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.Arrays;

import bean.CmpReviewImageBean;
import database.DatabaseHelper;

public class ShowDocument1 extends AppCompatActivity {

    Context context;
    String string_image = "";
    String data = "";
    String image = "";
    byte[] encodeByte;
    CmpReviewImageBean cmpReviewImageBean;
    String cmpno;
    DatabaseHelper db;
    Bitmap bitmap;

    ImageView imageView;
    String string_title = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);

        context = this;

        db = new DatabaseHelper(context);

        Bundle bundle = getIntent().getExtras();

        image = bundle.getString("photo");
        cmpno = bundle.getString("cmpno");
        data = bundle.getString("data");


        cmpReviewImageBean = new CmpReviewImageBean();

        cmpReviewImageBean = db.getReviewCmpImage(cmpno);

        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Image");


        imageView = (ImageView) findViewById(R.id.imageView);

        Log.e("STRING_IMAGE1", "&&&&&" + string_image);


       /* photo_16_txt = cmpReviewImageBean.getPhoto15();
        photo_17_txt = cmpReviewImageBean.getPhoto15();*/

        if (image.equalsIgnoreCase("PDF_1")) {
            string_image = data;
        }
        if (image.equalsIgnoreCase("PDF_2")) {
            string_image = data;
        }


        if (string_image != null && !string_image.isEmpty()) {

            encodeByte = Base64.decode(string_image, Base64.DEFAULT);
            Log.e("encodeByte1", "&&&&&" + Arrays.toString(encodeByte));
            //Log.e("encodeByte1","&&&&&"+encodeByte);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("bitmap1", "&&&&&" + bitmap);

            imageView.setImageBitmap(bitmap);

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
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
