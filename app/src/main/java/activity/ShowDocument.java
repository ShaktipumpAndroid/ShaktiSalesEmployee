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

public class ShowDocument extends AppCompatActivity {

    Context context;
    String string_image = "";
    String key = "";
    String data = "";
    String image = "";
    byte[] encodeByte;
    CmpReviewImageBean cmpReviewImageBean;
    String photo_1_txt, photo_2_txt, photo_3_txt, photo_4_txt, photo_5_txt, photo_6_txt, photo_7_txt, photo_8_txt, photo_9_txt, photo_10_txt, photo_11_txt, photo_12_txt, photo_13_txt, photo_14_txt, photo_15_txt, photo_16_txt, photo_17_txt, cmpno;
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


        cmpReviewImageBean = new CmpReviewImageBean();

        cmpReviewImageBean = db.getReviewCmpImage(cmpno);

        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Image");


        //string_image = "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";


        imageView = (ImageView) findViewById(R.id.imageView);

        Log.e("STRING_IMAGE1", "&&&&&" + string_image);


        photo_1_txt = cmpReviewImageBean.getPhoto1();
        photo_2_txt = cmpReviewImageBean.getPhoto2();
        photo_3_txt = cmpReviewImageBean.getPhoto3();
        photo_4_txt = cmpReviewImageBean.getPhoto4();
        photo_5_txt = cmpReviewImageBean.getPhoto5();
        photo_6_txt = cmpReviewImageBean.getPhoto6();
        photo_7_txt = cmpReviewImageBean.getPhoto7();
        photo_8_txt = cmpReviewImageBean.getPhoto8();
        photo_9_txt = cmpReviewImageBean.getPhoto9();
        photo_10_txt = cmpReviewImageBean.getPhoto10();
        photo_11_txt = cmpReviewImageBean.getPhoto11();
        photo_12_txt = cmpReviewImageBean.getPhoto12();
        photo_13_txt = cmpReviewImageBean.getPhoto13();
        photo_14_txt = cmpReviewImageBean.getPhoto14();
        photo_15_txt = cmpReviewImageBean.getPhoto15();
       /* photo_16_txt = cmpReviewImageBean.getPhoto15();
        photo_17_txt = cmpReviewImageBean.getPhoto15();*/

        if (image.equalsIgnoreCase("image1")) {
            string_image = photo_1_txt;
        }
        if (image.equalsIgnoreCase("image2")) {
            string_image = photo_2_txt;
        }
        if (image.equalsIgnoreCase("image3")) {
            string_image = photo_3_txt;
        }
        if (image.equalsIgnoreCase("image4")) {
            string_image = photo_4_txt;
        }
        if (image.equalsIgnoreCase("image5")) {
            string_image = photo_5_txt;
        }
        if (image.equalsIgnoreCase("image6")) {
            string_image = photo_6_txt;
        }
        if (image.equalsIgnoreCase("image7")) {
            string_image = photo_7_txt;
        }
        if (image.equalsIgnoreCase("image8")) {
            string_image = photo_8_txt;
        }
        if (image.equalsIgnoreCase("image9")) {
            string_image = photo_9_txt;
        }
        if (image.equalsIgnoreCase("image10")) {
            string_image = photo_10_txt;
        }
        if (image.equalsIgnoreCase("image11")) {
            string_image = photo_11_txt;
        }
        if (image.equalsIgnoreCase("image12")) {
            string_image = photo_12_txt;
        }
        if (image.equalsIgnoreCase("image13")) {
            string_image = photo_13_txt;
        }
        if (image.equalsIgnoreCase("image14")) {
            string_image = photo_14_txt;
        }
        if (image.equalsIgnoreCase("image15")) {
            string_image = photo_15_txt;
        }

        if (image.equalsIgnoreCase("PDF_1")) {
            string_image = "JVBERi0xLjMNCiXi48/TDQolUlNUWFBERjMgUGFyYW1ldGVyczogRFJTVA==";
        }
        if (image.equalsIgnoreCase("PDF_2")) {
            string_image = "JVBERi0xLjMNCiXi48/TDQolUlNUWFBERjMgUGFyYW1ldGVyczogRFJTVA==";
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
