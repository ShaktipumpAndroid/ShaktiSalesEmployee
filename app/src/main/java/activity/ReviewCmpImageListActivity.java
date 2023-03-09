package activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import bean.CmpReviewImageBean;
import database.DatabaseHelper;


public class ReviewCmpImageListActivity extends AppCompatActivity {

    Context context;
    CmpReviewImageBean cmpReviewImageBean;
    TextView photo_1, photo_2, photo_3, photo_4, photo_5, photo_6, photo_7, photo_8, photo_9, photo_10, photo_11, photo_12, photo_13, photo_14, photo_15;

    String photo_1_txt, photo_2_txt, photo_3_txt, photo_4_txt, photo_5_txt, photo_6_txt, photo_7_txt, photo_8_txt, photo_9_txt, photo_10_txt, photo_11_txt, photo_12_txt, photo_13_txt, photo_14_txt, photo_15_txt, cmp_no_txt;

    DatabaseHelper db;


    private Toolbar mToolbar;
    private Uri fileUri; // file url to store image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cmp_images);

        context = this;

        db = new DatabaseHelper(context);

        Bundle bundle = getIntent().getExtras();

        cmp_no_txt = bundle.getString("cmpno");

        cmpReviewImageBean = new CmpReviewImageBean();

        cmpReviewImageBean = db.getReviewCmpImage(cmp_no_txt);

        getLayout();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review Complaint Images");


        setData();


        photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image1");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);


            }
        });


        photo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image2");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image3");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image4");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image5");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image6");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image7");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image8");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });


        photo_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image9");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image10");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);


            }
        });

        photo_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image11");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image12");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });


        photo_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image13");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);


            }
        });

        photo_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image14");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

        photo_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, ShowDocument.class);
                intent.putExtra("photo", "image15");
                intent.putExtra("cmpno", cmp_no_txt);
                startActivity(intent);

            }
        });

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


    public void getLayout() {

        photo_1 = (TextView) findViewById(R.id.photo_1);

        photo_2 = (TextView) findViewById(R.id.photo_2);

        photo_3 = (TextView) findViewById(R.id.photo_3);
        photo_4 = (TextView) findViewById(R.id.photo_4);
        photo_5 = (TextView) findViewById(R.id.photo_5);
        photo_6 = (TextView) findViewById(R.id.photo_6);
        photo_7 = (TextView) findViewById(R.id.photo_7);
        photo_8 = (TextView) findViewById(R.id.photo_8);
        photo_9 = (TextView) findViewById(R.id.photo_9);

        photo_10 = (TextView) findViewById(R.id.photo_10);
        photo_11 = (TextView) findViewById(R.id.photo_11);

        photo_12 = (TextView) findViewById(R.id.photo_12);

        photo_13 = (TextView) findViewById(R.id.photo_13);
        photo_14 = (TextView) findViewById(R.id.photo_14);

        photo_15 = (TextView) findViewById(R.id.photo_15);

        photo_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_6.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_7.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_8.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_9.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_10.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_11.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_12.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_13.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_14.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
        photo_15.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);


    }

    private void setData() {

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


        if (photo_1_txt.equalsIgnoreCase("")) {
            photo_1.setVisibility(View.GONE);
        }
        if (photo_2_txt.equalsIgnoreCase("")) {
            photo_2.setVisibility(View.GONE);
        }
        if (photo_3_txt.equalsIgnoreCase("")) {
            photo_3.setVisibility(View.GONE);
        }
        if (photo_4_txt.equalsIgnoreCase("")) {
            photo_4.setVisibility(View.GONE);
        }
        if (photo_5_txt.equalsIgnoreCase("")) {
            photo_5.setVisibility(View.GONE);
        }
        if (photo_6_txt.equalsIgnoreCase("")) {
            photo_6.setVisibility(View.GONE);
        }
        if (photo_7_txt.equalsIgnoreCase("")) {
            photo_7.setVisibility(View.GONE);
        }
        if (photo_8_txt.equalsIgnoreCase("")) {
            photo_8.setVisibility(View.GONE);
        }
        if (photo_9_txt.equalsIgnoreCase("")) {
            photo_9.setVisibility(View.GONE);
        }
        if (photo_10_txt.equalsIgnoreCase("")) {
            photo_10.setVisibility(View.GONE);
        }
        if (photo_11_txt.equalsIgnoreCase("")) {
            photo_11.setVisibility(View.GONE);
        }
        if (photo_12_txt.equalsIgnoreCase("")) {
            photo_12.setVisibility(View.GONE);
        }
        if (photo_13_txt.equalsIgnoreCase("")) {
            photo_13.setVisibility(View.GONE);
        }
        if (photo_14_txt.equalsIgnoreCase("")) {
            photo_14.setVisibility(View.GONE);
        }
        if (photo_15_txt.equalsIgnoreCase("")) {
            photo_15.setVisibility(View.GONE);
        }


    }


}
