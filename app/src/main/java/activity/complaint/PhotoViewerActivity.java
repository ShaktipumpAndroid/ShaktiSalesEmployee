package activity.complaint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import activity.CustomUtility;
import adapter.PendingComplainGridListAdapter;
import bean.vkbean.ComplainDetailListResponse;
import bean.vkbean.RemarkResponseList;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class PhotoViewerActivity extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressDialog;


    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView imgBackID;
    private TextView txtHeaderID;
private String textRemarkValue;
    private  String mHeaderTittle= "";
    private  String mComplainNO= "";
    private  String mStatusValue= "";

    private  String mUserID= "";
   // private BaseRequest baseRequest;
   private ImageView imageView;

    String keyImage = "",cmp_no = "";
    private Intent mmIntent;
    private Dialog dialog ;
    byte[] encodeByte;
    Bitmap bitmap;

    ArrayList<String> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer_show);
        mContext = this;
        mmIntent = getIntent();
        initView();
    }

    private void initView() {

        Bundle bundle = getIntent().getExtras();
        keyImage = bundle.getString("key");

        imgBackID = findViewById(R.id.imgBackID);
        txtHeaderID = findViewById(R.id.txtHeaderID);
        imageView = findViewById(R.id.imageView);

        encodeByte = Base64.decode(keyImage, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imageView.setImageBitmap(bitmap);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        mUserID = CustomUtility.getSharedPreferences(mContext,"userID");









        txtHeaderID.setText("Photo Gallery");

        initClickEvent();
      //  callgetCompalinAllListAPI();



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

    private void initClickEvent() {

        imgBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();

        }
    };


}