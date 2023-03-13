package activity.complaint;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import activity.GPSTracker;
import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import other.CameraUtils;
import other.PathUtil;
import searchlist.complaint.SearchComplaint;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ComplaintDetailActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String imageStoragePath;
    private static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 999;
    private static int SAVE_DATA = 999;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder = null;
    String userid;
    String action, cmp_edit = " ", cmp_epc = " ";
    EditText editText_action;
    List<String> list_cmp_category = null,
            list_cmp_defect_type = null,
            list_cmp_relat_to = null,
            list_cmp_closer_reason_1 = null,
            list_cmp_closer_reason_2 = null,
            list_cmp_closer_reason_3 = null;
    String audio_record = "null";
    int index_cmp_category, index_cmp_defect_type1, index_cmp_defect_type2, index_cmp_defect_type3, index_cmp_relt_to1, index_cmp_relt_to2, index_cmp_relt_to3,
            index_cmp_close_reason_1, index_cmp_close_reason_2, index_cmp_close_reason_3,
            index_cmp_paymnt_1, index_cmp_paymnt_2, index_cmp_paymnt_3;
    String image1 = "null", image2 = "null", image3 = "null";
    Context mContext;
    HashSet<String> hashSet = null;
    TextView cmp_reason_1, cmp_reason_2, cmp_reason_3, pending_complaint, btn_cmp_forward, btn_appr_complaint, btn_close_complaint, btn_image1, btn_image2, btn_image3, btn_serail_history1,
            btn_serail_history2, btn_serail_history3, btn_audio_record, txtFeedbackFormYesValueID, txtFeedbackFormNoValueID;

    LinearLayout lvlYesViewID, lvlNoViewID;
    ImageView imgYesID, imgNoID;
    String mBillNumber;

    EditText edt_feedbackID;
    int mCheckYesno = 3;//1 - Yes , 2 - No

    Spinner spinner_cmp_category, spinner_cmp_close_reason_1/*,spinner_cmp_payment_1*/, spinner_cmp_close_reason_2, spinner_cmp_close_reason_3,
    /*spinner_cmp_payment_2,spinner_cmp_payment_3,*/spinner_cmp_defect_type1, spinner_cmp_defect_type2, spinner_cmp_defect_type3, spinner_cmp_relt_to_1, spinner_cmp_relt_to_2, spinner_cmp_relt_to_3;
    String cmp_no = "", cmp_extwg_1 = "", cmp_extwg_2 = "", cmp_extwg_3 = "", cmp_category = "", from = "",
            cmp_defect_type1, cmp_defect_type2, cmp_defect_type3, cmp_relt_to1, cmp_relt_to2, cmp_relt_to3, cmp_close_reason_1, cmp_close_reason_2,
            cmp_close_reason_3,/* cmp_payment_1, cmp_payment_2, cmp_payment_3,*/
            cmp_posnr1 = "", cmp_posnr2 = "", cmp_posnr3 = "", lv_sernr_1 = "",
            line1 = "", line2 = "", line3 = "", lv_sernr_2 = "", lv_sernr_3 = "", lv_matnr_1 = "", lv_matnr_2 = "", lv_matnr_3 = "",
            lv_history1 = "NO", lv_history2 = "NO", lv_history3 = "NO";
    String audio_status = "ON";
    Context mContex;
    EditText cmp_customer_name, cmp_warranty_1,
            cmp_address, cmp_matnr_1, cmp_sernr_1,
            cmp_warranty_2, cmp_matnr_2, cmp_maktx_2, cmp_sernr_2,
            cmp_warranty_3, cmp_matnr_3, cmp_maktx_3, cmp_sernr_3, pay_by_customer_1, pay_by_dealer_1,
            pay_by_company_1, pay_to_freelancer_1, pay_by_dea_1,
            pay_by_com_1, pay_to_freelancer_2, pay_to_freelancer_3,
            pay_by_customer_2, pay_by_dealer_2, pay_by_dea_2,
            pay_by_com_2, pay_by_company_2, pay_by_customer_3,
            pay_by_dealer_3, pay_by_dea_3, pay_by_com_3, pay_by_company_3;

    TextView cmp_maktx_1;
   // EditText cmp_maktx_1;

    EditText foc_amt_1, foc_amt_2, foc_amt_3;

    EditText bill_no_1, bill_no_2, bill_no_3;
    EditText bill_dt_1, bill_dt_2, bill_dt_3;
    EditText warn_cond_1, warn_cond_2, warn_cond_3;
    EditText warn_exp_dt_1, warn_exp_dt_2, warn_exp_dt_3;
    EditText insu_txt_1, insu_txt_2, insu_txt_3;
    DatabaseHelper dataHelper = null;
    LinearLayout Layout_item_2, Layout_item_3;
    SharedPreferences pref;
    String chat_address = "", chat_dealer = "", chat_customer = "", chat_reason = "";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutName, input_layout_date;
    private Uri fileUri;
    private ProgressDialog progressDialog;

    String yes_no_status;
    String mCustomer_feedback;

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                WebURL.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.US).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }


        return mediaFile;
    }

    String comAMT = "";
    String ponserID = "";

    public String getComplaintVKAmount() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDTL_VK
                    + " WHERE (" + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "')";

            cursor = db.rawQuery(selectQuery, null);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            int tdDate = Integer.parseInt(df.format(c));

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    SearchComplaint sc = new SearchComplaint();

                    //comAMT= Integer.parseInt(cursor.getString(cursor.getColumnIndex("company")));
                    comAMT = cursor.getString(cursor.getColumnIndex("company"));
                    ponserID = cursor.getString(cursor.getColumnIndex("posnr"));

                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {

            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            db.close();
            // Close database

        }
        return comAMT;

    }

    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(mContext);
        setSupportActionBar(mToolbar);
        userid = LoginBean.getUseid();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Layout_item_2 = (LinearLayout) this.findViewById(R.id.item_2);
        Layout_item_3 = (LinearLayout) this.findViewById(R.id.item_3);

        hashSet = new HashSet<String>();

        pending_complaint = (TextView) findViewById(R.id.pending_complaint);
        btn_close_complaint = (TextView) findViewById(R.id.close_complaint);

        btn_image1 = (TextView) findViewById(R.id.capture_image_1);
        btn_image2 = (TextView) findViewById(R.id.capture_image_2);
        btn_image3 = (TextView) findViewById(R.id.capture_image_3);

        edt_feedbackID = findViewById(R.id.edt_feedbackID);


        btn_serail_history1 = (TextView) findViewById(R.id.serail_history_1);
        btn_serail_history2 = (TextView) findViewById(R.id.serail_history_2);
        btn_serail_history3 = (TextView) findViewById(R.id.serail_history_3);

        txtFeedbackFormYesValueID = (TextView) findViewById(R.id.txtFeedbackFormYesValueID);
        txtFeedbackFormNoValueID = (TextView) findViewById(R.id.txtFeedbackFormNoValueID);


        lvlYesViewID = findViewById(R.id.lvlYesViewID);
        lvlNoViewID = findViewById(R.id.lvlNoViewID);

        imgYesID = findViewById(R.id.imgYesID);
        imgNoID = findViewById(R.id.imgNoID);

        bill_no_1 = (EditText) findViewById(R.id.bill_no_1);
        bill_no_2 = (EditText) findViewById(R.id.bill_no_2);
        bill_no_3 = (EditText) findViewById(R.id.bill_no_3);


        bill_dt_1 = (EditText) findViewById(R.id.bill_dt_1);
        bill_dt_2 = (EditText) findViewById(R.id.bill_dt_2);
        bill_dt_3 = (EditText) findViewById(R.id.bill_dt_3);

        warn_cond_1 = (EditText) findViewById(R.id.warn_cond_1);
        warn_cond_2 = (EditText) findViewById(R.id.warn_cond_2);
        warn_cond_3 = (EditText) findViewById(R.id.warn_cond_3);

        warn_exp_dt_1 = (EditText) findViewById(R.id.warn_exp_dt_1);
        warn_exp_dt_2 = (EditText) findViewById(R.id.warn_exp_dt_2);
        warn_exp_dt_3 = (EditText) findViewById(R.id.warn_exp_dt_3);

        insu_txt_1 = (EditText) findViewById(R.id.insu_txt_1);
        insu_txt_2 = (EditText) findViewById(R.id.insu_txt_2);
        insu_txt_3 = (EditText) findViewById(R.id.insu_txt_3);


        btn_audio_record = (TextView) findViewById(R.id.audio_recording);
        btn_cmp_forward = (TextView) findViewById(R.id.cmp_forward);
        btn_appr_complaint = (TextView) findViewById(R.id.appr_complaint);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        editText_action = (EditText) findViewById(R.id.text_complaint_action);

        cmp_customer_name = (EditText) findViewById(R.id.cmp_customer_name);
        cmp_address = (EditText) findViewById(R.id.cmp_address);


        pay_by_customer_1 = (EditText) findViewById(R.id.pay_by_customer_1);
        pay_by_dealer_1 = (EditText) findViewById(R.id.pay_by_dealer_1);
        pay_by_company_1 = (EditText) findViewById(R.id.pay_by_company_1);
        pay_to_freelancer_1 = (EditText) findViewById(R.id.pay_to_freelancer_1);
        pay_by_dea_1 = (EditText) findViewById(R.id.pay_by_dea_1);
        pay_by_com_1 = (EditText) findViewById(R.id.pay_by_com_1);
        foc_amt_1 = (EditText) findViewById(R.id.foc_amt_1);


        pay_by_customer_2 = (EditText) findViewById(R.id.pay_by_customer_2);
        pay_by_dealer_2 = (EditText) findViewById(R.id.pay_by_dealer_2);
        pay_by_company_2 = (EditText) findViewById(R.id.pay_by_company_2);
        pay_to_freelancer_2 = (EditText) findViewById(R.id.pay_to_freelancer_2);
        pay_by_dea_2 = (EditText) findViewById(R.id.pay_by_dea_2);
        pay_by_com_2 = (EditText) findViewById(R.id.pay_by_com_2);
        foc_amt_2 = (EditText) findViewById(R.id.foc_amt_2);

        pay_by_customer_3 = (EditText) findViewById(R.id.pay_by_customer_3);
        pay_by_dealer_3 = (EditText) findViewById(R.id.pay_by_dealer_3);
        pay_by_company_3 = (EditText) findViewById(R.id.pay_by_company_3);
        pay_to_freelancer_3 = (EditText) findViewById(R.id.pay_to_freelancer_3);
        pay_by_dea_3 = (EditText) findViewById(R.id.pay_by_dea_3);
        pay_by_com_3 = (EditText) findViewById(R.id.pay_by_com_3);
        foc_amt_3 = (EditText) findViewById(R.id.foc_amt_3);


        //spinner_cmp_payment_1   = (Spinner) findViewById(R.id.cmp_payment_by_1 );
        //spinner_cmp_payment_2   = (Spinner) findViewById(R.id.cmp_payment_by_2 );
        //spinner_cmp_payment_3   = (Spinner) findViewById(R.id.cmp_payment_by_3 );


        spinner_cmp_category = (Spinner) findViewById(R.id.cmp_category);


        spinner_cmp_defect_type1 = (Spinner) findViewById(R.id.cmp_defect_type_1);
        spinner_cmp_defect_type2 = (Spinner) findViewById(R.id.cmp_defect_type_2);
        spinner_cmp_defect_type3 = (Spinner) findViewById(R.id.cmp_defect_type_3);

        spinner_cmp_relt_to_1 = (Spinner) findViewById(R.id.cmp_relt_to_1);
        spinner_cmp_relt_to_2 = (Spinner) findViewById(R.id.cmp_relt_to_2);
        spinner_cmp_relt_to_3 = (Spinner) findViewById(R.id.cmp_relt_to_3);


        spinner_cmp_close_reason_1 = (Spinner) findViewById(R.id.cmp_close_reason_1);
        spinner_cmp_close_reason_2 = (Spinner) findViewById(R.id.cmp_close_reason_2);
        spinner_cmp_close_reason_3 = (Spinner) findViewById(R.id.cmp_close_reason_3);


        cmp_matnr_1 = (EditText) findViewById(R.id.material_code_1);
        cmp_maktx_1 =  findViewById(R.id.material_name_1);
        cmp_sernr_1 = (EditText) findViewById(R.id.serial_no_1);
        cmp_reason_1 = (TextView) findViewById(R.id.cmp_reason_1);
        cmp_warranty_1 = (EditText) findViewById(R.id.warranty_1);

        cmp_matnr_2 = (EditText) findViewById(R.id.material_code_2);
        cmp_maktx_2 = (EditText) findViewById(R.id.material_name_2);
        cmp_reason_2 = (TextView) findViewById(R.id.cmp_reason_2);
        cmp_sernr_2 = (EditText) findViewById(R.id.serial_no_2);
        cmp_warranty_2 = (EditText) findViewById(R.id.warranty_2);

        cmp_matnr_3 = (EditText) findViewById(R.id.material_code_3);
        cmp_maktx_3 = (EditText) findViewById(R.id.material_name_3);
        cmp_sernr_3 = (EditText) findViewById(R.id.serial_no_3);
        cmp_reason_3 = (TextView) findViewById(R.id.cmp_reason_3);
        cmp_warranty_3 = (EditText) findViewById(R.id.warranty_3);


        //cmp_amount_1            = (EditText) findViewById(R.id.amount_1 );

        list_cmp_category = new ArrayList<String>();
        list_cmp_defect_type = new ArrayList<String>();
        list_cmp_relat_to = new ArrayList<String>();
        list_cmp_closer_reason_1 = new ArrayList<String>();
        list_cmp_closer_reason_2 = new ArrayList<String>();
        list_cmp_closer_reason_3 = new ArrayList<String>();
        /*list_cmp_payment_by = new ArrayList<String>();*/

        dataHelper = new DatabaseHelper(mContext);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();

        cmp_no = bundle.getString("cmpno");
        cmp_epc = bundle.getString("epc");
        from = bundle.getString("from");

        if (from != null && from.equalsIgnoreCase("D")) {
            btn_appr_complaint.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("Comp No:" + " " + cmp_no);


        getComplaintHeader();
        getComplaintDetail();

        getComplaintCategory();
        getComplaintDefect();
        getComplaintRelatto();

        getCloserReason_item_1();
        getCloserReason_item_2();
        getCloserReason_item_3();

        getPendingComplaint();

        takeImage();

        serialNumberHistory();

        audioRecording();

        pay_by_company_1.setText(getComplaintVKAmount());

        // * disable close button
        // Log.d("cmp_edit", ""+  cmp_edit);


        if (!cmp_edit.equalsIgnoreCase("Y")) {
            btn_close_complaint.setEnabled(false);
            pending_complaint.setEnabled(false);
            btn_audio_record.setEnabled(false);
            btn_cmp_forward.setEnabled(false);
            btn_appr_complaint.setEnabled(false);
        }


        spinner_cmp_category.setPrompt("Category");
        spinner_cmp_defect_type1.setPrompt("Defect Type");
        spinner_cmp_defect_type2.setPrompt("Defect Type");
        spinner_cmp_defect_type3.setPrompt("Defect Type");

        spinner_cmp_relt_to_1.setPrompt("Complaint Related to");
        spinner_cmp_relt_to_2.setPrompt("Complaint Related to");
        spinner_cmp_relt_to_3.setPrompt("Complaint Related to");

        spinner_cmp_close_reason_1.setPrompt("Closer Reason");
        spinner_cmp_close_reason_2.setPrompt("Closer Reason");
        spinner_cmp_close_reason_3.setPrompt("Closer Reason");


       /* list_cmp_payment_by.clear();
        list_cmp_payment_by.add("Payment By");
        list_cmp_payment_by.add("Return By");*/

        /*ArrayAdapter<String> dataAdapter_paymnet_by = new ArrayAdapter<String>(this,R.layout.spinner_item,  list_cmp_payment_by );
        // Drop down layout style - list view with radio button
        dataAdapter_paymnet_by.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_cmp_payment_1.setAdapter(dataAdapter_paymnet_by);
        spinner_cmp_payment_2.setAdapter(dataAdapter_paymnet_by);
        spinner_cmp_payment_3.setAdapter(dataAdapter_paymnet_by);*/


        // list_cmp_category.add("Category");

        ArrayAdapter<String> dataAdapter_cmp_category = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_category);
        // Drop down layout style - list view with radio button
        dataAdapter_cmp_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_cmp_category.setAdapter(dataAdapter_cmp_category);


        // list_cmp_defect_type.add("Defect Type");

        ArrayAdapter<String> dataAdapter_defect_type = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_defect_type);
        // Drop down layout style - list view with radio button
        dataAdapter_defect_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_cmp_defect_type1.setAdapter(dataAdapter_defect_type);
        spinner_cmp_defect_type2.setAdapter(dataAdapter_defect_type);
        spinner_cmp_defect_type3.setAdapter(dataAdapter_defect_type);


        ArrayAdapter<String> dataAdapter_relt_to = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_relat_to);
        // Drop down layout style - list view with radio button
        dataAdapter_relt_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_cmp_relt_to_1.setAdapter(dataAdapter_relt_to);
        spinner_cmp_relt_to_2.setAdapter(dataAdapter_relt_to);
        spinner_cmp_relt_to_3.setAdapter(dataAdapter_relt_to);


        // list_cmp_close_reason.add("Closer Reason");

        ArrayAdapter<String> dataAdapter_close_reason_1 = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_closer_reason_1);
        dataAdapter_close_reason_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cmp_close_reason_1.setAdapter(dataAdapter_close_reason_1);

        ArrayAdapter<String> dataAdapter_close_reason_2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_closer_reason_2);
        dataAdapter_close_reason_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cmp_close_reason_2.setAdapter(dataAdapter_close_reason_2);

        ArrayAdapter<String> dataAdapter_close_reason_3 = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cmp_closer_reason_3);
        dataAdapter_close_reason_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cmp_close_reason_3.setAdapter(dataAdapter_close_reason_3);


        get_spinner_value();

        btn_close_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomer_feedback = edt_feedbackID.getText().toString().trim();

                if(mCheckYesno == 3)
                {
                    Toast.makeText(mContext, "Please select customer feedback form.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(mCustomer_feedback.equalsIgnoreCase("NO")) {
                        mCustomer_feedback = "NO";
                        if (WebURL.IC_PHOTO_CHECK == 1) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("Data Save alert !");
                            alertDialog.setMessage("Do you want to save data ?");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    save_closure_action();
                                    //editText_action.setText("");
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
                        } else {
                            if (submitForm()) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                                alertDialog.setTitle("Data Save alert !");
                                alertDialog.setMessage("Do you want to save data ?");
                                // On pressing Settings button
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        save_closure_action();
                                        //editText_action.setText("");
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
                            }
                        }
                    }
                    else
                    {
                        mCustomer_feedback = "YES";
                        if (WebURL.IC_PHOTO_CHECK == 1) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("Data Save alert !");
                            alertDialog.setMessage("Do you want to save data ?");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    save_closure_action();
                                    //editText_action.setText("");
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
                        } else {
                            if (submitForm()) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                                alertDialog.setTitle("Data Save alert !");
                                alertDialog.setMessage("Do you want to save data ?");

                                // On pressing Settings button
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        save_closure_action();
                                        //editText_action.setText("");
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
                            }
                        }
                    }
                }






            }
        });


        btn_cmp_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), ComplaintForwardActivity.class);
                intent.putExtra("cmp_no", cmp_no);
                intent.putExtra("epc", cmp_epc);
                startActivity(intent);


            }
        });

        btn_appr_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm1()) {
                    save_frwd_action();
                    Intent intent = new Intent(getApplicationContext(), ComplaintFrwdAprActivity.class);
                    intent.putExtra("cmp_no", cmp_no);
                    startActivity(intent);
                }
            }
        });

       /* File root = getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/SSales/"); //it is my root directory
        File billno = new File (root.getAbsolutePath() + "/SSales/" + cmp_no); // it is my sub folder directory .. it can vary..
        try
        {
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            if(!billno.exists())
            {
                billno.mkdirs();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }*/

        cmp_sernr_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sss = cmp_sernr_1.getText().toString();
                WebURL.Serial_no = sss;
                getCompalinIBASEAPI(sss);
                // showDialog(mContext, "String msg");
            }
        });

        lvlYesViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgYesID.setImageDrawable(getDrawable(R.drawable.ic_chk_selected));
                    imgNoID.setImageDrawable(getDrawable(R.drawable.ic_chk_unselected));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtFeedbackFormYesValueID.setTextColor(getColor(R.color.blue_fb));
                        txtFeedbackFormNoValueID.setTextColor(getColor(R.color.black));
                    }
                }
                mCheckYesno = 1;
                edt_feedbackID.setVisibility(View.GONE);
                // showDialog(mContext, "String msg");
            }
        });

        lvlNoViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgYesID.setImageDrawable(getDrawable(R.drawable.ic_chk_unselected));
                    imgNoID.setImageDrawable(getDrawable(R.drawable.ic_chk_selected));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtFeedbackFormYesValueID.setTextColor(getColor(R.color.black));
                        txtFeedbackFormNoValueID.setTextColor(getColor(R.color.blue_fb));
                    }
                }
                mCheckYesno = 2;
                edt_feedbackID.setVisibility(View.VISIBLE);
                // showDialog(mContext, "String msg");
            }
        });
    }


    //showDialog(mContext, controllerSerno, matorSerno, setSerno);
    public void showDialog(Context mContext, String controllerSerno, String matorSerno, String setSerno) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ibase_layout_data_dialog);

        TextView txtDataValue1ID = (TextView) dialog.findViewById(R.id.txtDataValue1ID);
        TextView txtDataValue2ID = (TextView) dialog.findViewById(R.id.txtDataValue2ID);
        TextView txtDataValue3ID = (TextView) dialog.findViewById(R.id.txtDataValue3ID);

        txtDataValue1ID.setText(setSerno);
        txtDataValue2ID.setText(matorSerno);
        txtDataValue3ID.setText(controllerSerno);
        // txtDataValue1ID.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);


        ImageView a = dialog.findViewById(R.id.a);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void getCompalinIBASEAPI(String mSerialnumber) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("BILL", mBillNumber));//kunur it means user id  BILL , SET

        param.add(new BasicNameValuePair("SET", mSerialnumber));//kunur it means user id
        //  param.add(new BasicNameValuePair("status", mStatusValue));///Pending Complaint

        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.IBASE_INFO, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);
/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                    //      JSONObject jo = new JSONObject(obj);
                    //  JSONArray ja = new JSONArray(obj);
                    // JSONObject jo = ja.getJSONObject(0);

                   /* try {
                        Gson gson = new Gson();
                        //////////////add model class here
                        progressDialog.dismiss();
                        InstallarCustListModel mInstallarCustListModel = gson.fromJson(obj, InstallarCustListModel.class);
                        getInstallarCustListModel(mInstallarCustListModel);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    // JSONObject jo = new JSONObject(obj);
                    JSONArray ja = new JSONArray(obj);
                    JSONObject join = ja.getJSONObject(0);

                    String controllerSerno = join.getString("controllerSerno");
                    String matorSerno = join.getString("matorSerno");
                    String setSerno = join.getString("setSerno");
                    //String setMatno = join.getString("setMatno");


                    runOnUiThread(new Runnable() {
                        public void run() {

                            showDialog(mContext, controllerSerno, matorSerno, setSerno);
                            // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                               /* mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);*/
                            progressDialog.dismiss();
                        }

                    });


                } catch (Exception ex) {
                    ex.printStackTrace();
                    // dismiss the progress dialog
                    progressDialog.dismiss();
                }
            }

        }.start();
    }


    @SuppressLint("Range")
    public void getComplaintHeader() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";


            cursor = db.rawQuery(selectQuery, null);

            //  Log.d("comp_dtl",""+cursor.getCount() + "--" + cmp_no);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    //  cmp_date.setText(cursor.getString(cursor.getColumnIndex("cmpdt")));
                    //cmp_dealername.setText(cursor.getString(cursor.getColumnIndex("distributor_name")));
                    cmp_customer_name.setText(cursor.getString(cursor.getColumnIndex("customer_name")));


                    cmp_address.setText(cursor.getString(cursor.getColumnIndex("address")));

                    cmp_edit = cursor.getString(cursor.getColumnIndex("edit"));

                    Log.e("EDIT$$", "%%%%%" + cmp_edit);

                    // cmp_customer_mobile.setText(cursor.getString(cursor.getColumnIndex("mob_no")));


                    chat_dealer = cursor.getString(cursor.getColumnIndex("distributor_name"));
                    chat_customer = cursor.getString(cursor.getColumnIndex("customer_name"));
                    chat_address = cursor.getString(cursor.getColumnIndex("address"));
                    cmp_epc = cursor.getString(cursor.getColumnIndex("epc"));


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }
            // Close database

        }

    }

    @SuppressLint("Range")
    public void getComplaintDetail() {


        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDTL
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);

            Log.d("comp_dtl1", "" + cursor.getCount() + "--" + cmp_no);

            if (cursor.getCount() > 0) {


                if (cursor.getCount() == 1) {
                    Layout_item_2.setVisibility(LinearLayout.GONE);
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }

                if (cursor.getCount() == 2) {
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }


                while (cursor.moveToNext()) {

                    index = index + 1;

                    // txtFeedbackFormYesValueID.setText("");////feedback form data yes no
                    // txtFeedbackFormNoValueID.setText("");////feedback form data yes no

                    if (index == 1) {

                        line1 = "000001";

                        cmp_posnr1 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_1.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_1.setText(""+cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_1.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        WebURL.Serial_no = cursor.getString(cursor.getColumnIndex("sernr"));
                        cmp_warranty_1.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_1.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        bill_no_1.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        mBillNumber = cursor.getString(cursor.getColumnIndex("vbeln"));

                        bill_dt_1.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_1.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_1.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_1.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        cmp_extwg_1 = cursor.getString(cursor.getColumnIndex("extwg"));


                        lv_matnr_1 = cursor.getString(cursor.getColumnIndex("matnr"));
                        lv_sernr_1 = cursor.getString(cursor.getColumnIndex("sernr"));

                        lv_history1 = cursor.getString(cursor.getColumnIndex("sernr_history"));


                        if (lv_history1.equalsIgnoreCase("NO")) {
                            btn_serail_history1.setVisibility(TextView.GONE);

                        }
                        // Layout_item_2.setVisibility(LinearLayout.GONE);

                    }


                    if (index == 2) {

                        line2 = "000002";

                        cmp_posnr2 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_2.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_2.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_2.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        WebURL.Serial_no = cursor.getString(cursor.getColumnIndex("sernr"));
                        cmp_warranty_2.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_2.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_2 = cursor.getString(cursor.getColumnIndex("extwg"));
                        bill_no_2.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        mBillNumber = cursor.getString(cursor.getColumnIndex("vbeln"));
                        bill_dt_2.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_2.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_2.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_2.setText(cursor.getString(cursor.getColumnIndex("war_dat")));

                        lv_matnr_2 = cursor.getString(cursor.getColumnIndex("matnr"));
                        lv_sernr_2 = cursor.getString(cursor.getColumnIndex("sernr"));
                        lv_history2 = cursor.getString(cursor.getColumnIndex("sernr_history"));


                        if (lv_history2.equalsIgnoreCase("NO")) {
                            btn_serail_history2.setVisibility(TextView.GONE);

                        }


                    }


                    if (index == 3) {

                        line3 = "000003";

                        cmp_posnr3 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_3.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_3.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_3.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        WebURL.Serial_no = cursor.getString(cursor.getColumnIndex("sernr"));
                        cmp_warranty_3.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_3.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_3 = cursor.getString(cursor.getColumnIndex("extwg"));
                        bill_no_3.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        mBillNumber = cursor.getString(cursor.getColumnIndex("vbeln"));
                        bill_dt_3.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_3.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_3.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_3.setText(cursor.getString(cursor.getColumnIndex("war_dat")));

                        lv_matnr_3 = cursor.getString(cursor.getColumnIndex("matnr"));
                        lv_sernr_3 = cursor.getString(cursor.getColumnIndex("sernr"));
                        lv_history3 = cursor.getString(cursor.getColumnIndex("sernr_history"));


                        if (lv_history3.equalsIgnoreCase("NO")) {
                            btn_serail_history3.setVisibility(TextView.GONE);

                        }


                    }


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }
            // Close database

        }

    }

    public void getComplaintCategory() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_category.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_CATEGORY;


            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
//                    list_cmp_category.add(cursor.getString(cursor.getColumnIndex("category")));
                    @SuppressLint("Range") String catType = cursor.getString(cursor.getColumnIndex("category"));
                    if(catType.equalsIgnoreCase("freelancer--12") || catType.equalsIgnoreCase("service center--1") ||
                            catType.equalsIgnoreCase("sales return--3") || catType.equalsIgnoreCase("burglary claims--16") ||
                            catType.equalsIgnoreCase("transportation claim--9") || catType.equalsIgnoreCase("Nature claims--15") ||
                            catType.equalsIgnoreCase("goodwill claim--8") || catType.equalsIgnoreCase("field--2") ||
                            catType.equalsIgnoreCase("others--5") || catType.equalsIgnoreCase("perameter setting--13") ||
                            catType.contains("service engineer")){
                        list_cmp_category.add(catType);
                    }
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_category);
                list_cmp_category.clear();
                list_cmp_category.add("Select Category");
                list_cmp_category.addAll(hashSet);


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    @SuppressLint("Range")
    public void getComplaintDefect() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_defect_type.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_DEFECT;


            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
                    list_cmp_defect_type.add(cursor.getString(cursor.getColumnIndex("defect")));
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_defect_type);
                list_cmp_defect_type.clear();
                list_cmp_defect_type.add("Select Defect Type");
                list_cmp_defect_type.addAll(hashSet);


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    @SuppressLint("Range")
    public void getComplaintRelatto() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_relat_to.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_RELT_TO;


            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
                    list_cmp_relat_to.add(cursor.getString(cursor.getColumnIndex("relt_to")));
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_relat_to);
                list_cmp_relat_to.clear();
                list_cmp_relat_to.add("Select Complaint Related to");
                list_cmp_relat_to.addAll(hashSet);


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    @SuppressLint("Range")
    public void getCloserReason_item_1() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_closer_reason_1.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_CLOSER
                    + " WHERE " + DatabaseHelper.KEY_EXTWG + " = '" + cmp_extwg_1 + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            //  Log.d("comp_1",""+cursor.getCount() + "--" +  cmp_extwg_1);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    // Log.d("comp_1",""+ cmp_extwg_1 + "--" + cursor.getString(cursor.getColumnIndex("closer_reason")) );

                    list_cmp_closer_reason_1.add(cursor.getString(cursor.getColumnIndex("closer_reason")));
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_closer_reason_1);
                list_cmp_closer_reason_1.clear();
                list_cmp_closer_reason_1.add("Select Closer Reason");
                list_cmp_closer_reason_1.addAll(hashSet);

                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    @SuppressLint("Range")
    public void getCloserReason_item_2() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_closer_reason_2.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_CLOSER
                    + " WHERE " + DatabaseHelper.KEY_EXTWG + " = '" + cmp_extwg_2 + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

//            Log.d("comp_2",""+cursor.getCount() + "--" +  cmp_extwg_2);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    // Log.d("comp_2",""+ cmp_extwg_2 + "--" + cursor.getString(cursor.getColumnIndex("closer_reason")) );

                    list_cmp_closer_reason_2.add(cursor.getString(cursor.getColumnIndex("closer_reason")));
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_closer_reason_2);
                list_cmp_closer_reason_2.clear();
                list_cmp_closer_reason_2.add("Select Closer Reason");
                list_cmp_closer_reason_2.addAll(hashSet);

                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }
        }


    }

    @SuppressLint("Range")
    public void getCloserReason_item_3() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_cmp_closer_reason_3.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_CLOSER
                    + " WHERE " + DatabaseHelper.KEY_EXTWG + " = '" + cmp_extwg_3 + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            //   Log.d("comp_dtl12",""+cursor.getCount() + "--" +  cmp_extwg_1);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
                    list_cmp_closer_reason_3.add(cursor.getString(cursor.getColumnIndex("closer_reason")));
                }

                hashSet.clear();
                hashSet.addAll(list_cmp_closer_reason_3);
                list_cmp_closer_reason_3.clear();
                list_cmp_closer_reason_3.add("Select Closer Reason");
                list_cmp_closer_reason_3.addAll(hashSet);

                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }
        }


    }

    public void getPendingComplaint() {


        pending_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, InprocessComplaintActivity.class);
                intent.putExtra("cmpno", cmp_no);
                mContext.startActivity(intent);


            }
        });

    }

    public void takeImage() {

        btn_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
                openCamera();
            }
        });


        btn_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 200;
                openCamera();
            }
        });


        btn_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 300;
                openCamera();
            }
        });


    }

    public boolean submitForm() {
        boolean value;
        if (category() &&
                validateAttachment() &&
                payment1() &&
                closure_reason1() &&
                defect_type1() &&
                cmpln_relt_to1() &&
                //  image1()                          &&


                payment2() &&
                closure_reason2() &&
                defect_type2() &&
                cmpln_relt_to2() &&
                //image2()                          &&

                payment3() &&
                closure_reason3() &&
                defect_type3() &&
                cmpln_relt_to3() &&
                //image3()                          &&

                validateComment() &&
                CustomUtility.CheckGPS(this) &&
                validateDate()
            // validateAudio()
        ) {
            value = true;
        } else {
            value = false;
        }
        return value;

    }

    public boolean submitForm1() {
        boolean value;
        if (category() &&
                validateAttachment() &&

                payment11() &&
                defect_type1() &&
                cmpln_relt_to1() &&

                payment21() &&
                defect_type2() &&
                cmpln_relt_to2() &&

                payment31() &&
                defect_type3() &&
                cmpln_relt_to3() &&

                CustomUtility.CheckGPS(this) &&
                validateDate()
        ) {
            value = true;
        } else {
            value = false;
        }
        return value;

    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }

    private boolean validateAudio() {
        if (audio_record.equalsIgnoreCase("null")) {

            Toast.makeText(getApplicationContext(), "Please Record Audio ", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private boolean validateAttachment() {

//
//        1                    Service Center
//        2                    Field
//        3                    Sales Return
//        4                    Packing
//
//        9                    Transportation Claim
//        10                   Transportation Return
//        12                   Freelancer


        String lv_category = "null";


        String CurrentString = cmp_category;
        String[] separated;
        separated = CurrentString.split("--");
        lv_category = separated[0];
        lv_category = separated[1];


        Log.d("cmp_category55", lv_category);
        DatabaseHelper database = new DatabaseHelper(mContext);


        SQLiteDatabase db = database.getReadableDatabase();

        String selectQuery = null;
        Cursor cursor = null;
        try {
            if (lv_category.equalsIgnoreCase("1") ||
                    lv_category.equalsIgnoreCase("9") ||
                    lv_category.equalsIgnoreCase("2") ||
                    lv_category.equalsIgnoreCase("12") ||
                    lv_category.equalsIgnoreCase("3") ||
                    lv_category.equalsIgnoreCase("10") ||
                    lv_category.equalsIgnoreCase("4")) {


//            selectQuery = "SELECT * FROM " +  DatabaseHelper.TABLE_ZCMPLN_IMAGE
//                    +  " WHERE " + DatabaseHelper.KEY_CMPNO  +  " = '" + cmp_no  + "'" ;

                selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLN_IMAGE
                        + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'" + " AND " +
                        DatabaseHelper.KEY_CATEGORY + " = '" + lv_category + "'";


                cursor = db.rawQuery(selectQuery, null);

                //Log.d ("attach",""+cursor.getCount());
                if (cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Attach Photo", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }


        return true;
    }

    private boolean validateComment() {
        if (editText_action.getText().toString().isEmpty()) {
            inputLayoutName.setError("Please Enter Closer Comment");

            requestFocus(editText_action);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean category() {
        if (index_cmp_category == 0) {
            Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean payment1() {
        if (line1.equalsIgnoreCase("000001")) {
            if (pay_by_customer_1.getText().toString().isEmpty() &&
                    pay_by_dealer_1.getText().toString().isEmpty() &&
                    pay_by_company_1.getText().toString().isEmpty() &&
                    pay_to_freelancer_1.getText().toString().isEmpty()
            ) {

                Toast.makeText(getApplicationContext(), "Enter Payment Amount for 1st item", Toast.LENGTH_SHORT).show();


                return false;
            }

        }
        return true;

    }

    private boolean payment2() {

        if (line2.equalsIgnoreCase("000002")) {


            if (pay_by_customer_2.getText().toString().isEmpty() &&
                    pay_by_dealer_2.getText().toString().isEmpty() &&
                    pay_by_company_2.getText().toString().isEmpty() &&
                    pay_to_freelancer_2.getText().toString().isEmpty()
            ) {

                Toast.makeText(getApplicationContext(), "Enter Payment Amount for 2nd item", Toast.LENGTH_SHORT).show();


                return false;
            }
        }
        return true;

    }

    private boolean payment3() {


        if (line3.equalsIgnoreCase("000003")) {

            if (pay_by_customer_3.getText().toString().isEmpty() &&
                    pay_by_dealer_3.getText().toString().isEmpty() &&
                    pay_by_company_3.getText().toString().isEmpty() &&
                    pay_to_freelancer_3.getText().toString().isEmpty()
            ) {
                Toast.makeText(getApplicationContext(), "Enter Payment Amount for 3rd item", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        return true;

    }

    private boolean payment11() {
        if (line1.equalsIgnoreCase("000001")) {


            if (pay_by_com_1.getText().toString().isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter Return by Company Payment Amount for 1st item", Toast.LENGTH_SHORT).show();


                return false;
            }

        }
        return true;

    }

    private boolean payment21() {

        if (line2.equalsIgnoreCase("000002")) {


            if (pay_by_com_2.getText().toString().isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter Return by Company Payment Amount for 2nd item", Toast.LENGTH_SHORT).show();


                return false;
            }
        }
        return true;

    }

    private boolean payment31() {


        if (line3.equalsIgnoreCase("000003")) {

            if (pay_by_com_2.getText().toString().isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter Return by Company Payment Amount for 3rd item", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        return true;

    }

    private boolean closure_reason1() {
        if (line1.equalsIgnoreCase("000001")) {
            if (index_cmp_close_reason_1 == 0) {
                Toast.makeText(this, "Select close reason in 1st item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_close_reason_1);
                return false;
            }
        }
        return true;

    }

    private boolean defect_type1() {
        if (line1.equalsIgnoreCase("000001")) {
            if (index_cmp_defect_type1 == 0) {
                Toast.makeText(this, "Select defect type in 1st item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_defect_type1);
                return false;
            }
        }

        return true;
    }

    private boolean cmpln_relt_to1() {
        if (line1.equalsIgnoreCase("000001")) {
            if (index_cmp_relt_to1 == 0) {
                Toast.makeText(this, "Select complain related to in 1st item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_relt_to_1);
                return false;
            }
        }

        return true;
    }

    private boolean image1() {
        if (line1.equalsIgnoreCase("000001")) {
            if (image1.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Capture Photo in 1st item", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }

    private boolean image2() {
        if (line2.equalsIgnoreCase("000002")) {
            if (image2.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Capture Photo in 2nd item", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }

    private boolean image3() {
        if (line3.equalsIgnoreCase("000003")) {
            if (image3.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Capture Photo in 3rd item", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }

    private boolean closure_reason2() {
        if (line2.equalsIgnoreCase("000002")) {
            if (index_cmp_close_reason_2 == 0) {
                Toast.makeText(this, "Select close reason in 2nd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_close_reason_2);
                return false;
            }
        }
        return true;

    }

    private boolean defect_type2() {
        if (line2.equalsIgnoreCase("000002")) {
            if (index_cmp_defect_type2 == 0) {
                Toast.makeText(this, "Select defect type in 2nd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_defect_type2);
                return false;
            }
        }
        return true;
    }

    private boolean cmpln_relt_to2() {
        if (line2.equalsIgnoreCase("000002")) {
            if (index_cmp_relt_to2 == 0) {
                Toast.makeText(this, "Select complain related to in 2nd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_relt_to_2);
                return false;
            }
        }

        return true;
    }

    private boolean closure_reason3() {
        if (line3.equalsIgnoreCase("000003")) {
            if (index_cmp_close_reason_3 == 0) {
                Toast.makeText(this, "Select close reason in 3rd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_close_reason_3);
                return false;
            }

        }

        return true;

    }

    private boolean defect_type3() {


        if (line3.equalsIgnoreCase("000003")) {
            if (index_cmp_defect_type3 == 0) {
                Toast.makeText(this, "Select defect type in 3rd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_defect_type3);
                return false;
            }
        }
        return true;
    }

    private boolean cmpln_relt_to3() {
        if (line3.equalsIgnoreCase("000003")) {
            if (index_cmp_relt_to3 == 0) {
                Toast.makeText(this, "Select complain related to in 3rd item", Toast.LENGTH_SHORT).show();
                requestFocus(spinner_cmp_relt_to_3);
                return false;
            }
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void save_closure_action() {

        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();
        //  action = editText_action.getText().toString();


        if (line1.equalsIgnoreCase("000001")) {
            dataHelper.insertClosureRequestComplaint
                    (userid,
                            cmp_no,
                            cmp_posnr1,
                            cmp_category,
                            pay_by_dealer_1.getText().toString(),
                            pay_by_customer_1.getText().toString(),
                            pay_by_company_1.getText().toString(),
                            mCustomer_feedback,
                            yes_no_status,
                            pay_to_freelancer_1.getText().toString(),
                            pay_by_dea_1.getText().toString(),
                            pay_by_com_1.getText().toString(),
                            cmp_close_reason_1,
                            cmp_defect_type1,
                            cmp_relt_to1,
                            foc_amt_1.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude));

        }

        if (line2.equalsIgnoreCase("000002")) {
            dataHelper.insertClosureRequestComplaint
                    (userid,
                            cmp_no,
                            cmp_posnr2,
                            cmp_category,
                            pay_by_dealer_2.getText().toString(),
                            pay_by_customer_2.getText().toString(),
                            pay_by_company_2.getText().toString(),
                            mCustomer_feedback,
                            yes_no_status,
                            pay_to_freelancer_2.getText().toString(),
                            pay_by_dea_2.getText().toString(),
                            pay_by_com_2.getText().toString(),
                            cmp_close_reason_2,
                            cmp_defect_type2,
                            cmp_relt_to2,
                            foc_amt_2.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude));

        }


        if (line3.equalsIgnoreCase("000003")) {
            dataHelper.insertClosureRequestComplaint
                    (userid,
                            cmp_no,
                            cmp_posnr3,
                            cmp_category,
                            pay_by_dealer_3.getText().toString(),
                            pay_by_customer_3.getText().toString(),
                            pay_by_company_3.getText().toString(),
                            pay_to_freelancer_3.getText().toString(),
                            mCustomer_feedback,
                            yes_no_status,
                            pay_by_dea_3.getText().toString(),
                            pay_by_com_3.getText().toString(),
                            cmp_close_reason_3,
                            cmp_defect_type3,
                            cmp_relt_to3,
                            foc_amt_3.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude));

        }


        // Closer Comment status in  inprocess table

        action = editText_action.getText().toString();

        dataHelper.insertInprocessComplaint
                (userid,
                        LoginBean.getUsername(),
                        cmp_no,
                        "",
                        action,
                        "",
                        customUtility.getCurrentDate(),
                        customUtility.getCurrentTime(),
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        "CLOSURE");


        // change complaint status to clouser
        setUpdateComplaintHeader(cmp_no);


// set value 100 for clear screen on restart event
        SAVE_DATA = 100;

        Toast.makeText(mContext, "Closure request saved successfully", Toast.LENGTH_LONG).show();
        SyncClosureComplaintInBackground();


        ComplaintDetailActivity.this.finish();
    }

    public void save_frwd_action() {

        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();

        if (line1.equalsIgnoreCase("000001")) {
            dataHelper.insertfrwdappcmp
                    (userid,
                            cmp_no,
                            cmp_posnr1,
                            cmp_category,
                            pay_by_dealer_1.getText().toString(),
                            pay_by_customer_1.getText().toString(),
                            pay_by_company_1.getText().toString(),
                            pay_to_freelancer_1.getText().toString(),
                            pay_by_dea_1.getText().toString(),
                            pay_by_com_1.getText().toString(),
                            cmp_defect_type1,
                            cmp_relt_to1,
                            foc_amt_1.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            "",
                            "");

        }

        if (line2.equalsIgnoreCase("000002")) {
            dataHelper.insertfrwdappcmp
                    (userid,
                            cmp_no,
                            cmp_posnr2,
                            cmp_category,
                            pay_by_dealer_2.getText().toString(),
                            pay_by_customer_2.getText().toString(),
                            pay_by_company_2.getText().toString(),
                            pay_to_freelancer_2.getText().toString(),
                            pay_by_dea_2.getText().toString(),
                            pay_by_com_2.getText().toString(),
                            cmp_defect_type2,
                            cmp_relt_to2,
                            foc_amt_2.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            "",
                            "");

        }


        if (line3.equalsIgnoreCase("000003")) {
            dataHelper.insertfrwdappcmp
                    (userid,
                            cmp_no,
                            cmp_posnr3,
                            cmp_category,
                            pay_by_dealer_3.getText().toString(),
                            pay_by_customer_3.getText().toString(),
                            pay_by_company_3.getText().toString(),
                            pay_to_freelancer_3.getText().toString(),
                            pay_by_dea_3.getText().toString(),
                            pay_by_com_3.getText().toString(),
                            cmp_defect_type3,
                            cmp_relt_to3,
                            foc_amt_3.getText().toString(),
                            customUtility.getCurrentDate(),
                            customUtility.getCurrentTime(),
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            "",
                            "");

        }

    }

//    @Override
//    public void onRestart() {
//       //
//
//
//        if( SAVE_DATA != 999 )
//        {
//            ComplaintDetailActivity.this.finish();
//        }
//
//        super.onRestart();
//
//    }

    public void SyncClosureComplaintInBackground() {
        Intent i = new Intent(ComplaintDetailActivity.this, SyncDataService.class);
        startService(i);

    }

    public void get_spinner_value() {

        //* complaint category
        spinner_cmp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_category = arg0.getSelectedItemPosition();


                //    Log.d("cmp_category", "" + index_cmp_category);
                cmp_category = spinner_cmp_category.getSelectedItem().toString();
                //deleteFiles(getExternalStorageDirectory().getAbsolutePath() + "/SSales/" + cmp_no);

                dataHelper.deleteImage1(cmp_no);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //*************************  defect type ********************************************** //


        spinner_cmp_defect_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_defect_type1 = arg0.getSelectedItemPosition();
                cmp_defect_type1 = spinner_cmp_defect_type1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_defect_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_defect_type2 = arg0.getSelectedItemPosition();
                cmp_defect_type2 = spinner_cmp_defect_type2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_defect_type3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_defect_type3 = arg0.getSelectedItemPosition();
                cmp_defect_type3 = spinner_cmp_defect_type3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //*************************  complaint related to********************************************** //


        spinner_cmp_relt_to_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_relt_to1 = arg0.getSelectedItemPosition();
                cmp_relt_to1 = spinner_cmp_relt_to_1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_relt_to_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_relt_to2 = arg0.getSelectedItemPosition();
                cmp_relt_to2 = spinner_cmp_relt_to_2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_relt_to_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_relt_to3 = arg0.getSelectedItemPosition();
                cmp_relt_to3 = spinner_cmp_relt_to_3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //*************************  closure reason ********************************************** //


        spinner_cmp_close_reason_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_close_reason_1 = arg0.getSelectedItemPosition();
                cmp_close_reason_1 = spinner_cmp_close_reason_1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_close_reason_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_close_reason_2 = arg0.getSelectedItemPosition();
                cmp_close_reason_2 = spinner_cmp_close_reason_2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_cmp_close_reason_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_close_reason_3 = arg0.getSelectedItemPosition();
                cmp_close_reason_3 = spinner_cmp_close_reason_3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //*************************** payment by
       /* spinner_cmp_payment_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_paymnt_1 = arg0.getSelectedItemPosition();
                cmp_payment_1 =   spinner_cmp_payment_1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_cmp_payment_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_paymnt_2 = arg0.getSelectedItemPosition();
                cmp_payment_2 =   spinner_cmp_payment_2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinner_cmp_payment_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_paymnt_3 = arg0.getSelectedItemPosition();
                cmp_payment_3 =   spinner_cmp_payment_3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


    }

    public void setUpdateComplaintHeader(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(mContext);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "CLOSURE" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if (db != null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        String cmpln_image = "";

        if (resultCode == RESULT_OK) {


            Date dt = new Date();
            int hours = dt.getHours();
            int minutes = dt.getMinutes();
            int seconds = dt.getSeconds();

            String time = "" + hours + minutes + seconds;

            String fDate = new SimpleDateFormat("yyyyMMdd").format(dt);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;


            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            cmpln_image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.d("requestCode", "" + requestCode + "" + cmpln_image);

            DatabaseHelper dataHelper = new DatabaseHelper(this);
            if (requestCode == 100) {
                dataHelper.insertComplaintImage(
                                cmp_no,
                                cmp_posnr1,
                                "",
                                cmpln_image);
                image1 = "DONE";
            }


            if (requestCode == 200) {
                dataHelper.insertComplaintImage(cmp_no,
                                cmp_posnr2,
                                "",
                                cmpln_image);
                image2 = "DONE";
            }

            if (requestCode == 300) {
                dataHelper.insertComplaintImage
                        (
                                cmp_no,
                                cmp_posnr3,
                                "",
                                cmpln_image);
                image3 = "DONE";
            }


            File file = new File(fileUri.getPath());
            if (file.exists()) {
                file.delete();
            }


        }


        SAVE_DATA = 999;
    }

    public void openCamera() {

        if (CameraUtils.checkPermissions(mContext)) {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public void serialNumberHistory() {

        //Log.d("his",""+lv_sernr_1+lv_matnr_1 ) ;


        btn_serail_history1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ComplaintDetailActivity.this, SerailNoHistoryActivity.class);

                intent.putExtra("lv_download", "null");
                intent.putExtra("cmp_no", cmp_no);
                intent.putExtra("lv_sernr", lv_sernr_1);
                intent.putExtra("lv_matnr", lv_matnr_1);


                startActivity(intent);


            }
        });


        btn_serail_history2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ComplaintDetailActivity.this, SerailNoHistoryActivity.class);

                intent.putExtra("lv_download", "null");
                intent.putExtra("cmp_no", cmp_no);
                intent.putExtra("lv_sernr", lv_sernr_2);
                intent.putExtra("lv_matnr", lv_matnr_2);

                startActivity(intent);


            }
        });


        btn_serail_history3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ComplaintDetailActivity.this, SerailNoHistoryActivity.class);


                intent.putExtra("lv_download", "null");
                intent.putExtra("cmp_no", cmp_no);
                intent.putExtra("lv_sernr", lv_sernr_3);
                intent.putExtra("lv_matnr", lv_matnr_3);

                startActivity(intent);


            }
        });


    }

    public void audioRecording() {


        btn_audio_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (audio_status) {
                    case "ON":


//                        start_recording();
//
//                        btn_audio_record.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.stop_recording,0, 0);
//                        btn_audio_record.setText("Stop \nRecording");
//
//                        audio_status = "OFF";
//

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("Audio Recording Alert !");
                        alertDialog.setMessage("Do you want to Start Recording ?");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                start_recording();

                                btn_audio_record.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.stop_recording, 0, 0);
                                btn_audio_record.setText("Stop \nRecording");

                                audio_status = "OFF";


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


                        break;


                    case "OFF":


                        saveAudio();

//                        btn_audio_record.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.start_recording,0, 0);
//                        btn_audio_record.setText("Start \nRecording");
//
//
//                        audio_status = "ON";
//
                        break;

                }


            }
        });


    }

    public void start_recording() {


//        btn_start_recording.setEnabled(false);
//        btn_start_recording .setBackgroundColor(Color.parseColor("#8b8b8c"));
//        btn_start_recording.setTextColor(Color.parseColor("#ffffff"));


        if (checkPermission()) {

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            "Audio_" + String.valueOf(System.currentTimeMillis()) + ".3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            Toast.makeText(ComplaintDetailActivity.this, "Recording started",
                    Toast.LENGTH_LONG).show();


        } else {
            requestPermission();
        }


    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ComplaintDetailActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public void stop_recording() {

        if (mediaRecorder != null) {

            mediaRecorder.stop();


            Toast.makeText(ComplaintDetailActivity.this, "Recording stop",
                    Toast.LENGTH_LONG).show();


            File file = new File(AudioSavePathInDevice);

            if (file.exists()) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                BufferedInputStream in = null;
                try {

                    in = new BufferedInputStream(new FileInputStream(AudioSavePathInDevice));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                int read;
                byte[] buff = new byte[1024];
                try {
                    if (in != null) {
                        while ((read = in.read(buff)) > 0) {
                            out.write(buff, 0, read);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] audioBytes = out.toByteArray();
                audio_record = Base64.encodeToString(audioBytes, Base64.DEFAULT);

                //Log.d("audio_save",""+audio_record);

                file.delete(); // delete file from location

            }

        }

    }

    public void saveAudio() {


//
//        String	pernr,
//        String	cmpno,
//        String	budat,
//        String	time,
//        String audio_record)


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Audio Recording Alert !");
        alertDialog.setMessage("Do you want to Stop Recording ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                stop_recording();

                action = editText_action.getText().toString();

                // Log.d("cmp_sync",pref.getString("key_username", "user") +"--"+action) ;


                dataHelper.insertComplaintAudio
                        (pref.getString("key_username", "userid"),
                                cmp_no,
                                new CustomUtility().getCurrentDate(),
                                new CustomUtility().getCurrentTime(),
                                audio_record,
                                chat_dealer,
                                chat_customer,
                                chat_address,
                                action);


                // action

//                Toast.makeText(mContext, "Audio Recording saved successfully", Toast.LENGTH_LONG).show();
//
//                SyncCheckInOutInBackground();


                btn_audio_record.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.stop_recording, 0, 0);
                btn_audio_record.setText("Stop \nRecording");
                btn_audio_record.setEnabled(false);
                audio_status = "OFF";


            }
        });

        // on pressing cancel button

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                btn_audio_record.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.start_recording, 0, 0);
                btn_audio_record.setText("Start \nRecording");


                audio_status = "ON";


                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


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
                //callWebPage();
                return true;

          /*  case R.id.action_complaint_attachment_pdf:
                PathUtil.setSharedPreference(mContext, "cmp_no", cmp_no);
                Intent intent = new Intent(getApplicationContext(), ComplaintPDFActivity.class);
                startActivity(intent);
                return true;
*/
            case R.id.action_complaint_attachment:
                if (category()) {
                    PathUtil.setSharedPreference(mContext, "cmp_no", cmp_no);
                    PathUtil.setSharedPreference(mContext, "cmp_category", cmp_category);
                    PathUtil.setSharedPreference(mContext, "cust_name", cmp_customer_name.getText().toString());

//**********************************  old attachment *************************************
                    Intent intent1 = new Intent(getApplicationContext(), ComplaintImageActivity.class);
                    //Log.d("cmp_category1",cmp_no+"--"+cmp_category);
                    startActivity(intent1);
//**********************************  old attachment **************************************
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachment, menu);
        return true;
    }


}
