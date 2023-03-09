package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.io.FileOutputStream;

import bean.CameraFileBean;

//import com.shaktipumps.shakti.shaktisalesemployee.R;

public class AndroidCameraExample extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture;

    private TextView capture, cancel, done;

    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    //private String sh_data;
    private int cameraId = 1;
    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            hideButton();
            mPreview.refreshCamera(mCamera);
        }
    };
    private Bitmap m_bitmap;
    private OnClickListener doneListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            File pictureFile = save(m_bitmap);

            //Log.d("save",m_bitmap+"---"+m_bitmap.);
            String url = pictureFile.getAbsolutePath();
            //SharedPreference.setSharedPreference(myContext, sh_data, url);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("url", url);
            setResult(Activity.RESULT_FIRST_USER, returnIntent);
            finish();

        }
    };
    private OnClickListener captrureListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.takePicture(null, null, mPicture);
        }
    };

    private static Bitmap rotate(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Intent getIntent(Context context, String value) {
        Intent intent = new Intent(context, AndroidCameraExample.class);
        //	SharedPreference.setSharedPreference(context,"key",value);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_capture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        //sh_data = SharedPreference.getSharedPreferences(myContext,"key");
        initialize();
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {

        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {


            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
            }
            mCamera = Camera.open(findFrontFacingCamera());  // comment by mayank


            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }
    }

    private void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);


        capture = (TextView) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        cancel = (TextView) findViewById(R.id.button_ChangeCamera);
        cancel.setOnClickListener(cancelListener);

        done = (TextView) findViewById(R.id.done);
        done.setOnClickListener(doneListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private boolean hasCamera(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private PictureCallback getPictureCallback() {
        PictureCallback picture = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if (data != null) {
                    m_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    showButton();
                }

            }
        };
        return picture;
    }

    private File save(Bitmap m_bitmap) {

        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "MTCC");
        myDir.mkdirs();
        File file = new File(myDir, "croped_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (file.exists()) file.delete();

        Log.d("set_file_name", "" + file);


        CameraFileBean.setFile(file);
        try {

            Bitmap finalBitmap = rotate(m_bitmap, -90);
            FileOutputStream out = new FileOutputStream(file);

            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void showButton() {
        capture.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.VISIBLE);
        done.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        capture.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        done.setVisibility(View.INVISIBLE);
    }


}