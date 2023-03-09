package searchlist.complaint;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static activity.OtherImgActivity.MEDIA_TYPE_IMAGE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.iceteck.silicompressorr.SiliCompressor;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import activity.CameraActivity;
import activity.CustomUtility;
import activity.OtherImgActivity;
import activity.complaint.DisplayComplaintImageActivity;
import bean.ComplaintImage;
import database.DatabaseHelper;
import other.CameraUtils;
import other.PermissionsIntent;

public class Complaint_Image_ListViewAdapter extends BaseAdapter{

    // Declare Variables
    String videoData = "";
    private static final int FILE_SELECT_CODE = -10;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int GALLERY_PDF_REQUEST_CODE = 102;
    private static final int GALLERY_VIDEO_REQUEST_CODE = 103;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 104;
    private static final int RESULT_OK = -1;
    LayoutInflater inflater;
    String str_cmp_no = "", image_name, image_item;
    SharedPreferences pref;
    Context mContext;
    String cmp_cat, cmp_no;
    Uri fileUri = null;
    String audio_record = "";
    String file_path;
    // ViewHolder holder;
    Intent data;
    String flag = "null";
    int PERMISSION_ALL = 1;


    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<Complaint_Image_Name> complaintSearchlist = null;
    private ArrayList<Complaint_Image_Name> arraylist;

    private ArrayList<ComplaintImage> arraylist_ImageTaken = null;

    public Complaint_Image_ListViewAdapter(Context context, List<Complaint_Image_Name> complaintSearchlist, String cmp_category, List<ComplaintImage> list_complaintImageTaken, String lv_cmpno) {
        mContext = context;
        cmp_no = lv_cmpno;
        cmp_cat = cmp_category;
        this.complaintSearchlist = complaintSearchlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Complaint_Image_Name>();
        this.arraylist.addAll(complaintSearchlist);
        this.arraylist_ImageTaken = new ArrayList<ComplaintImage>();
        this.arraylist_ImageTaken.addAll(list_complaintImageTaken);

    }

    public static void check_Permission(final Context context) {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionsIntent.WRITE_EXTERNAL_STORAGE_PERMISSION);


        } else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionsIntent.WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public Complaint_Image_Name getItem(int position) {
        return complaintSearchlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_complaint_image, null);
            holder.image_name = (TextView) view.findViewById(R.id.image_name);
            holder.complete_icon = (TextView) view.findViewById(R.id.complete_icon);
            view.setTag(holder);


        holder.image_name.setText(complaintSearchlist.get(position).getName());
        image_item = "101" + complaintSearchlist.get(position).getItem();

        for (int i = 0; i < arraylist_ImageTaken.size(); i++) {
              if (arraylist_ImageTaken.get(i).getPosnr().equalsIgnoreCase(image_item)) {
                holder.complete_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_item = "101" + complaintSearchlist.get(position).getItem();
                image_name = complaintSearchlist.get(position).getName();
                cmp_cat = complaintSearchlist.get(position).getCategory();
                 for (int i = 0; i < arraylist_ImageTaken.size(); i++) {
                    flag = "null";
                    if (arraylist_ImageTaken.get(i).getPosnr().equalsIgnoreCase(image_item)) {
                        flag = "X";
                        break;
                    }
                }
                 if (flag.equalsIgnoreCase("X")) {
                    Intent intent = new Intent(mContext, DisplayComplaintImageActivity.class);
                    intent.putExtra("cmp_no", cmp_no);
                    intent.putExtra("cmp_posnr", image_item);
                    intent.putExtra("cmp_category", cmp_cat);
                    intent.putExtra("image_name", image_name);
                    mContext.startActivity(intent);
                } else {
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        check_Permission(mContext);
                    } else {
                        image_name = complaintSearchlist.get(position).getName();
                        image_item = "101" + complaintSearchlist.get(position).getItem();
                         if (ActivityCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                             showConfirmationCamera(image_name);
                         } else {
                            if (!hasPermissions(mContext, PERMISSIONS)) {
                                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
                            }
                        }
                    }
                }
            }
        });
        notifyDataSetChanged();
        return view;
    }


    //start of new code for live photo 07-02-2023
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showConfirmationCamera(String name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(mContext);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                    }

                }  else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(mContext)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile1(MEDIA_TYPE_IMAGE,"ShaktiPump", "ImagePumpShakti" +System.currentTimeMillis());

            fileUri = CameraUtils.getOutputMediaFileUri(mContext.getApplicationContext(), file);
            Log.e( "PATH","flie"+ file );
            Log.e( "PATH","fileUri"+ fileUri);
            intent.putExtra(MediaStore.Images.Media.DATE_ADDED, fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            ((Activity) mContext).startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);

        }
    }

    public String onActivityResult(int requestCode, int resultCode, Intent lv_data, String lv_cmp_category, String lv_cmp_no) throws IOException {
        Log.e("resultCode ","Anj"+ resultCode);

        cmp_cat = lv_cmp_category;
        cmp_no = lv_cmp_no;
        data = lv_data;
        image_item = image_item;

        Log.e( "PATH","fileUri"+ fileUri );
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_IMAGE_REQUEST_CODE) {
                try {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                flag = "X";
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                final int REQUIRED_SIZE = 200;
                                int scale = 1;
                                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                                    scale *= 2;
                                Log.e("Scale", "&&&&" + scale);
                                options.inSampleSize = scale;
                                options.inJustDecodeBounds = false;
                                Bitmap bitmap;

                                try {
                                    if (fileUri != null) {

                                        bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(fileUri));

                                        int count = bitmap.getByteCount();

                                        Log.e("Count", "&&&&&" + count);

                                        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                                        if (count <= 100000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 200000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 300000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 400000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 500000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 600000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 700000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 800000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 900000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                                        } else if (count <= 1000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                        } else if (count <= 2000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                        } else if (count <= 3000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayBitmapStream);
                                        } else if (count <= 4000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                        } else if (count <= 5000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayBitmapStream);
                                        } else if (count <= 6000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                        } else if (count <= 7000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayBitmapStream);
                                        } else if (count <= 8000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                        } else if (count <= 9000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
                                        } else if (count <= 10000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                        } else if (count <= 20000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                        } else if (count <= 30000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, byteArrayBitmapStream);
                                        } else if (count <= 40000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                        } else if (count <= 50000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
                                        } else if (count <= 60000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                        } else if (count <= 70000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                        } else if (count <= 80000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                        } else if (count <= 90000000) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayBitmapStream);
                                        } else {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayBitmapStream);
                                        }
                                        // OutputStream out;

                                        byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                        long size = byteArray.length;

                                        Log.e("SIZE1234", "&&&&" + size);

                                        Log.e("BYTEARRAY", "&&&&" + byteArray.toString());

                                        audio_record = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                        Log.e("audio_record", "&&&&" + audio_record);

                                        DatabaseHelper dataHelper = new DatabaseHelper(mContext);
                                        dataHelper.insertComplaintImage
                                                (cmp_no,
                                                        image_item,
                                                        cmp_cat,
                                                        audio_record);

                                        Log.e("CAtegory1", "&&&&&&" + cmp_cat);
                                        Log.e("CMPNO1", "&&&&&&" + cmp_no);
                                        Log.e("AUDIORECORD1", "&&&&&&" + audio_record);
                                        Log.e("IMAGE_ITEM1", "&&&&&&" + image_item);

                                    } else {
                                        Toast.makeText(mContext, "Invalid File Location", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //  }
                                Toast.makeText(mContext, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                                ((Activity) mContext).finish();

                            } catch (Exception e) {
                                Toast.makeText(mContext, "Invalid File Selection", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                    // on pressing cancel button

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GALLERY_PDF_REQUEST_CODE) {
                System.out.println("SELECT_VIDEO");

                Uri selectedImageUri = data.getData();
                File myFileP = new File(mContext.getFilesDir(), selectedImageUri.getAuthority());
                myFileP.getPath();
                int size = (int) myFileP.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(myFileP));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
                audio_record = encoded;

            }
        }
        return audio_record;
    }

    public class ViewHolder {
        TextView image_name, complete_icon;
    }
}