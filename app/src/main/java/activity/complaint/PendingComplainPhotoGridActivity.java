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

import adapter.PendingComplainRemarkListAdapter;
import bean.vkbean.ComplainDetailListResponse;
import bean.vkbean.RemarkResponseList;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class PendingComplainPhotoGridActivity extends AppCompatActivity {

    private Context mContext;

    //private List<ComplainDetailListResponse> mComplainDetailListResponse;

    private RelativeLayout rlvBottomViewID;
    private ImageView imgBackID;
    private TextView txtHeaderID;
    private String textRemarkValue;
    private  String mHeaderTittle= "";
    private  String mComplainNO= "";
    private  String mStatusValue= "";
    private  String mMobileNumber= "";
    private  String mUserID= "";
    //private BaseRequest baseRequest;
    public TextView txtBTNActionID, txtBTNPendingID, txtBTNClodeID, txtBTNUploadID;
    private RecyclerView rclyPendingComplainList;

    private PendingComplainGridListAdapter mPendingComplainGridListAdapter;

    private Intent mmIntent;
    private Dialog dialog ;

    ArrayList<String> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complain_gridview);
        mContext = this;
        mmIntent = getIntent();
        initView();
    }

    private void initView() {

        mUserID = CustomUtility.getSharedPreferences(mContext,"userID");



        rclyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
      //  rclyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));

        imgBackID = findViewById(R.id.imgBackID);
        txtHeaderID = findViewById(R.id.txtHeaderID);

        rlvBottomViewID =  findViewById(R.id.rlvBottomViewID);
        txtBTNUploadID =  findViewById(R.id.txtBTNUploadID);
       // txtBTNSaveID =  findViewById(R.id.txtBTNSaveID);

        txtBTNActionID = findViewById(R.id.txtBTNActionID);

        rlvBottomViewID.setVisibility(View.GONE);

        mMobileNumber = mmIntent.getStringExtra("mobile_number");
        mComplainNO = mmIntent.getStringExtra("Complain_number");
        mHeaderTittle = mmIntent.getStringExtra("complaint");
        mStatusValue = mmIntent.getStringExtra("StatusValue");

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rclyPendingComplainList.setLayoutManager(staggeredGridLayoutManager);

        txtHeaderID.setText("Photo Gallery");

        initClickEvent();
      //  callgetCompalinAllListAPI();

        getCmpAttachDataVK(mContext,  mComplainNO);

      /*  if(mStatusValue.equalsIgnoreCase("01"))
        {
            rlvBottomViewID.setVisibility(View.VISIBLE);
        }
        else
        {
            rlvBottomViewID.setVisibility(View.GONE);
        }*/

    }

    private void initClickEvent() {

        imgBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


       txtBTNActionID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, "Action optiin working commimg soon", Toast.LENGTH_SHORT).show();
              initRemarkViewBox(view);
            }
        });



       txtBTNUploadID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(mContext, InstReportImageActivity.class);
                intent.putExtra("inst_id", mComplainDetailListResponse.get(0).getCmpno());
                intent.putExtra("cust_name", mComplainDetailListResponse.get(0).getCloserReason());
                intent.putExtra("StatusCheck", mStatusValue);*/

              //  mContext.startActivity(intent);

            }
        });



    }

    private void initRemarkViewBox(View view) {

        EditText editText;
        TextView txtBTNRemarkID;
          dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_remark_as_complain);

        editText = dialog.findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = dialog.findViewById(R.id.txtBTNRemarkID);
        dialog.setCancelable(true);

        dialog.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(PendingComplainDetailActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_remark_as_complain, viewGroup, false);
        editText = findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = findViewById(R.id.txtBTNRemarkID);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();*/




        txtBTNRemarkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textRemarkValue = editText.getText().toString().trim();
                if(!textRemarkValue.equalsIgnoreCase(""))
                {
                   // callInsertRemarkAPI(textRemarkValue);
                    new callInsertRemarkAPI().execute();
                }
                else
                {
                    Toast.makeText(mContext, "Please enter remark!", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class callInsertRemarkAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;




            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {


                // jsonObj.put("project_no", projno);
                jsonObj.put("cmpno",mComplainNO);
                // jsonObj.put("beneficiary",ben);
                //  jsonObj.put("mobno",mLrInvoiceResponse.get(0).getMobno());
                jsonObj.put("kunnr", mUserID);
                jsonObj.put("action", textRemarkValue);

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("action", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                      //  finish();
                    }
                    else
                    {

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                      //  finish();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            dialog.dismiss();
            progressDialog.dismiss();  // dismiss dialog


        }
    }


    public int getCmpAttachDataVK(Context context, String cmpno) {

        int progressBarStatus = 0;

       // DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        //param.add(new BasicNameValuePair("CMPNO", cmpno));
        param.add(new BasicNameValuePair("CMPNO", cmpno));
        param.add(new BasicNameValuePair("POSNR", "000001"));
      //  param.add(new BasicNameValuePair("POSNR", mStatusValue));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CMPLN_ATTACHMENT, param);

            if (obj != null) {

                System.out.println("Vihu_obj==>>"+obj);

                String Image1 = null,Image2 = null,Image3 = null,Image4 = null,Image5 = null,Image6 = null,Image7 = null,Image8 = null,Image9 = null,
                        Image10 = null,Image11 = null,Image12 = null,Image13 = null, Image14 = null,Image15 = null;

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("complaint_attachment");

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);
                    Log.e("DATA","&&&&"+jo.length());

                    if(!jo.getString("image_1").equalsIgnoreCase(""))
                    {
                        Image1 = resizeBase64Image(jo.getString("image_1"));
                        al.add(Image1);
                    }
                    else{
                        Image1 = "";
                    }
                    if(!jo.getString("image_2").equalsIgnoreCase("")) {
                        Image2 = resizeBase64Image(jo.getString("image_2"));
                        al.add(Image2);
                    }
                    else{
                        Image2 = "";
                    }
                    if(!jo.getString("image_3").equalsIgnoreCase("")) {
                        Image3 = resizeBase64Image(jo.getString("image_3"));
                        al.add(Image3);
                    }
                    else{
                        Image3 = "";
                    }
                    if(!jo.getString("image_4").equalsIgnoreCase("")) {
                        Image4 = resizeBase64Image(jo.getString("image_4"));
                        al.add(Image4);
                    }
                    else{
                        Image4 = "";
                    }
                    if(!jo.getString("image_5").equalsIgnoreCase("")) {
                        Image5 = resizeBase64Image(jo.getString("image_5"));
                        al.add(Image5);
                    }
                    else{
                        Image5 = "";
                    }
                    if(!jo.getString("image_6").equalsIgnoreCase("")) {
                        Image6 = resizeBase64Image(jo.getString("image_6"));
                        al.add(Image6);
                    }
                    else{
                        Image6 = "";
                    }
                    if(!jo.getString("image_7").equalsIgnoreCase("")) {
                        Image7 = resizeBase64Image(jo.getString("image_7"));
                        al.add(Image7);
                    }
                    else{
                        Image7 = "";
                    }
                    if(!jo.getString("image_8").equalsIgnoreCase("")) {
                        Image8 = resizeBase64Image(jo.getString("image_8"));
                        al.add(Image8);
                    }
                    else{
                        Image8 = "";
                    }
                    if(!jo.getString("image_9").equalsIgnoreCase("")) {
                        Image9 = resizeBase64Image(jo.getString("image_9"));
                        al.add(Image9);
                    }
                    else{
                        Image9 = "";
                    }
                    if(!jo.getString("image_10").equalsIgnoreCase("")) {
                        Image10 = resizeBase64Image(jo.getString("image_10"));
                        al.add(Image10);
                    }
                    else{
                        Image10 = "";
                    }
                    if(!jo.getString("image_11").equalsIgnoreCase("")) {
                        Image11 = resizeBase64Image(jo.getString("image_11"));
                        al.add(Image11);
                    }
                    else{
                        Image11 = "";
                    }
                    if(!jo.getString("image_12").equalsIgnoreCase("")) {
                        Image12 = resizeBase64Image(jo.getString("image_12"));
                        al.add(Image12);
                    }
                    else{
                        Image12 = "";
                    }
                    if(!jo.getString("image_13").equalsIgnoreCase("")) {
                        Image13 = resizeBase64Image(jo.getString("image_13"));
                        al.add(Image13);
                    }
                    else{
                        Image13 = "";
                    }
                    if(!jo.getString("image_14").equalsIgnoreCase("")) {
                        Image14 = resizeBase64Image(jo.getString("image_14"));
                        al.add(Image14);
                    }
                    else{
                        Image14 = "";
                    }
                    if(!jo.getString("image_15").equalsIgnoreCase("")) {
                        Image15 = resizeBase64Image(jo.getString("image_15"));
                        al.add(Image15);
                    }
                    else{
                        Image15 = "";
                    }

                /*    dataHelper.insertCmpattach(cmpno,
                            Image1,
                            Image2,
                            Image3,
                            Image4,
                            Image5,
                            Image6,
                            Image7,
                            Image8,
                            Image9,
                            Image10,
                            Image11,
                            Image12,
                            Image13,
                            Image14,
                            Image15

                    );*/
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        mPendingComplainGridListAdapter = new PendingComplainGridListAdapter(mContext, al, mStatusValue);
                        rclyPendingComplainList.setAdapter(mPendingComplainGridListAdapter);

                    }


                });

            }

            progressBarStatus = 100;

        } catch (Exception E) {
            E.printStackTrace();
            progressBarStatus = 100;
        }

        return progressBarStatus;
    }

    public String resizeBase64Image(String base64image) {

        Log.e("String","&&&"+base64image);

        byte[] encodeByte = Base64.decode(base64image, Base64.DEFAULT);
       /* BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;*/
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        Log.e("Image","&&&"+image);
        if (image.getHeight() <= 400 && image.getWidth() <= 400) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 720, 900, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);


        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }




}