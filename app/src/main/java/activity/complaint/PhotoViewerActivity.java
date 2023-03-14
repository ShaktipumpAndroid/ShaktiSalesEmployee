package activity.complaint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import activity.CustomUtility;
import model.ComplaintImageModel;
import webservice.Constants;

public class PhotoViewerActivity extends AppCompatActivity {

    private Context mContext;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView imageView;
    ComplaintImageModel.Response complaintImageModel;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer_show);


        initView();
        listner();
    }

    private void initView() {
        mContext = this;
       // complaintImageModel = (ComplaintImageModel.Response) getIntent().getSerializableExtra(Constants.complaintImageModel);
        imageView = findViewById(R.id.imageView);
        toolbar = findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray(Constants.complaintImageModel);

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(bmp);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.photo_gallary));

    }

    private void listner() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}