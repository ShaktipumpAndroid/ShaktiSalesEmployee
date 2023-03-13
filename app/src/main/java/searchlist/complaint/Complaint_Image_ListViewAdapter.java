package searchlist.complaint;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activity.CameraActivity;
import activity.CustomUtility;
import activity.complaint.DisplayComplaintImageActivity;
import bean.ComplaintImage;
import database.DatabaseHelper;
import other.CameraUtils;
import other.PermissionsIntent;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "resource"})
public class Complaint_Image_ListViewAdapter extends BaseAdapter{

    private static final int CAMERA_IMAGE_REQUEST_CODE = 104;
    private static final int RESULT_OK = -1;
    LayoutInflater inflater;
    String  image_name, image_item;
    Context mContext;
    String cmp_cat, cmp_no;
    Uri fileUri = null;
    String audio_record = "";

    // ViewHolder holder;
    String flag = "null";
    int PERMISSION_ALL = 1;


    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
//    android.os.Handler mHandler = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String mString = (String) msg.obj;
//            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
//        }
//    };


    private final List<Complaint_Image_Name> complaintSearchlist;

    private final ArrayList<ComplaintImage> arraylist_ImageTaken;

    public Complaint_Image_ListViewAdapter(Context context, List<Complaint_Image_Name> complaintSearchlist, String cmp_category, List<ComplaintImage> list_complaintImageTaken, String lv_cmpno) {
        mContext = context;
        cmp_no = lv_cmpno;
        cmp_cat = cmp_category;
        this.complaintSearchlist = complaintSearchlist;
        inflater = LayoutInflater.from(mContext);
        ArrayList<Complaint_Image_Name> arraylist = new ArrayList<>();
        arraylist.addAll(complaintSearchlist);
        this.arraylist_ImageTaken = new ArrayList<>();
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
            holder.image_name = view.findViewById(R.id.image_name);
            holder.complete_icon = view.findViewById(R.id.complete_icon);
            view.setTag(holder);


        holder.image_name.setText(complaintSearchlist.get(position).getName());
        image_item = "101" + complaintSearchlist.get(position).getItem();

        for (int i = 0; i < arraylist_ImageTaken.size(); i++) {
              if (arraylist_ImageTaken.get(i).getPosnr().equalsIgnoreCase(image_item)) {
                holder.complete_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.done, 0);
            }
        }

        view.setOnClickListener(view1 -> {

            Log.e("Image",""+ image_name);
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
        });
        notifyDataSetChanged();
        return view;
    }


    //start of new code for live photo 07-02-2023
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showConfirmationCamera(String image_name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = CustomUtility.checkPermission(mContext);
            if (items[item].equals("Take Photo")) {

                if (result) {
                    openCamera(image_name);
                }

            }  else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void openCamera(String image_name) {

        String camera= "0";
        if (CameraUtils.checkPermissions(mContext)) {

            if(image_name.equals("SiteÂ ImageÂ / FarmerÂ Image")){
                camera ="1";
            }

            Intent intent = new Intent(mContext, CameraActivity.class);
            intent.putExtra("cust_name",cmp_no);
            intent.putExtra("FrontCamera",camera);
            ((Activity) mContext).startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);
        }
    }

    public String onActivityResult(int requestCode, int resultCode, Intent data ) throws IOException {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_IMAGE_REQUEST_CODE) {
                try {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        try {


                            Log.e("resultCode ","Anj"+ resultCode);
                            Bitmap bitmap;

                            try {
                                ;
                                //data.getExtras().getString("data")
                                fileUri = Uri.parse( data.getStringExtra("data"));
                                Log.e("Filepath==>","file "+ fileUri);
                                flag = "X";
                                if (fileUri != null) {

                                    bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(fileUri));

                                    int count = bitmap.getByteCount();

                                    Log.e("Count", "&&&&&" + count);

                                    // OutputStream out;
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    audio_record = Base64.encodeToString(byteArray, Base64.DEFAULT);


                                    Log.e("audio_record", "&&&&" + audio_record);

                                    DatabaseHelper dataHelper = new DatabaseHelper(mContext);
                                    dataHelper.insertComplaintImage
                                            (cmp_no,
                                                    image_item,
                                                    cmp_cat,
                                                    audio_record);

                                } else {
                                    Toast.makeText(mContext, "Invalid File Location", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(mContext, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                            ((Activity) mContext).finish();

                        } catch (Exception e) {
                            Toast.makeText(mContext, "Invalid File Selection", Toast.LENGTH_LONG).show();

                        }
                    });
                    // on pressing cancel button

                    alertDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

                    // Showing Alert Message
                    alertDialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return audio_record;
    }

    public static class ViewHolder {
        TextView image_name, complete_icon;
    }
}