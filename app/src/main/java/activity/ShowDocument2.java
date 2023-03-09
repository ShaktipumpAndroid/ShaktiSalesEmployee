package activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

public class ShowDocument2 extends AppCompatActivity {

    Context context;

    String key = "";

    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";

    String customer_nm = "";
    String typ = "";
    ImageView imageView;
    String string_title = "";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);

        context = this;

       // db = new DatabaseHelper(context);
        Bundle bundle = getIntent().getExtras();

        customer_nm = bundle.getString("customer_name");
        key = bundle.getString("key");

        typ = bundle.getString("type");


        imageView = (ImageView) findViewById(R.id.imageView);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(getIntent().getExtras()!=null){
        if(getIntent().getStringExtra("image_path")!=null && !getIntent().getStringExtra("image_path").isEmpty()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
            imageView.setImageBitmap(myBitmap);
        }}

      /*  switch (key) {
            case "photo1":
                string_title = "Service Center Board";
                File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_1.jpg");
                if (file.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);

                }

                break;
            case "photo2":
                string_title = "Service Center Training Letter";
                File file1 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_2.jpg");
                if (file1.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

                break;
            case "photo3":
                string_title = "Certificate Image";
                File file2 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_3.jpg");
                if (file2.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

                break;
            case "photo4":
                string_title = "Selfiy with the Service Person";
                File file3 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_4.jpg");
                if (file3.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file3.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
                break;
            case "photo5":
                string_title = "Spare Parts Stock Image";

                File file4 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_5.jpg");
                if (file4.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file4.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

                break;
            case "photo6":
                string_title = "Product Tarining Image";
                File file5 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME  + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_6.jpg");
                if (file5.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file5.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
                break;
            case "photo7":
                string_title = "Other Image";
                File file6 = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + customer_nm, "/IMG_PHOTO_7.jpg");
                if (file6.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file6.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
                break;
        }*/

        //Toolbar code

        getSupportActionBar().setTitle(string_title);

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
