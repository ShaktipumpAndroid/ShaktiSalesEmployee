package activity.complaint;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import activity.CustomUtility;
import adapter.PendingComplainGridListAdapter;
import model.ComplaintImageModel;
import webservice.WebURL;

public class PendingComplainPhotoGridActivity extends AppCompatActivity {

    private Context mContext;
    private String mComplainNO = "";
    private String mUserID = "";
    private RecyclerView pendingComplainPhotoList;

    NestedScrollView nestedScrollView;

    TextView noDataAvailable;

    ProgressBar progressbar;
    private PendingComplainGridListAdapter mPendingComplainGridListAdapter;
    private ProgressDialog progressDialog;
    Toolbar toolbar;
    private int lastPage = 1;
    private String mMaxoffset = "";

    List<ComplaintImageModel.Response> complaintImageModels = new ArrayList<>();
    List<ComplaintImageModel.Response> complaintImageArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complain_gridview);

        initView();
        listner();


    }


    private void initView() {
        mContext = this;
        mUserID = CustomUtility.getSharedPreferences(mContext, "userID");
        progressDialog = new ProgressDialog(this);

        pendingComplainPhotoList = findViewById(R.id.pendingComplainPhotoList);
        noDataAvailable = findViewById(R.id.noDataAvailable);
        toolbar = findViewById(R.id.toolbar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        progressbar = findViewById(R.id.progressbar);

        mComplainNO = getIntent().getStringExtra("Complain_number");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.complaintImages));

        if (CustomUtility.isOnline(mContext)) {
            getCmpAttachDataVK();
        } else {

            CustomUtility.ShowToast(getResources().getString(R.string.net_connection), mContext);
        }
        if (nestedScrollView != null) {
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                            lastPage = lastPage + 1;
                            if (Integer.parseInt(mMaxoffset) >= lastPage) {
                                if (CustomUtility.isOnline(mContext)) {
                                    getCmpAttachDataVK();
                                } else {

                                    CustomUtility.ShowToast(getResources().getString(R.string.net_connection), mContext);
                                }
                            } else {
                                CustomUtility.ShowToast("Sorry, no more data available!", mContext);
                            }
                        }
                    }
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void listner() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getCmpAttachDataVK() {
         if(lastPage==1) {
             progressDialog.setMessage(getResources().getString(R.string.Loading));
             progressDialog.setCancelable(false);
             progressDialog.setCanceledOnTouchOutside(false);
             progressDialog.show();
         }else {
             progressbar.setVisibility(View.VISIBLE);
         }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                WebURL.IMAGES_REVIEW_COMPLAINT + "?cmpno=" + mComplainNO + "&posnr=000001&page=" + lastPage, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Get test report response=" + response.toString());

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        progressbar.setVisibility(View.GONE);
                        ComplaintImageModel complaintImageModel = new Gson().fromJson(response.toString(), ComplaintImageModel.class);

                        if (complaintImageModel.getStatus() != null && !complaintImageModel.getStatus().isEmpty()
                                && complaintImageModel.getStatus().equals("true")) {

                            mMaxoffset = String.valueOf(Math.round(Double.parseDouble(complaintImageModel.getCount())));

                            complaintImageModels = complaintImageModel.getResponse();
                            complaintImageArraylist.addAll(complaintImageModels);
                            mPendingComplainGridListAdapter = new PendingComplainGridListAdapter(mContext, complaintImageArraylist);
                            pendingComplainPhotoList.setHasFixedSize(true);
                            pendingComplainPhotoList.setAdapter(mPendingComplainGridListAdapter);
                            mPendingComplainGridListAdapter.notifyDataSetChanged();

                            noDataAvailable.setVisibility(View.GONE);
                            pendingComplainPhotoList.setVisibility(View.VISIBLE);
                        } else {
                            noDataAvailable.setVisibility(View.VISIBLE);
                            pendingComplainPhotoList.setVisibility(View.GONE);
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                noDataAvailable.setVisibility(View.VISIBLE);
                pendingComplainPhotoList.setVisibility(View.GONE);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressbar.setVisibility(View.GONE);
            }
        });
// Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*
     */
/*private void initClickEvent() {

       txtBTNActionID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // Toast.makeText(mContext, "Action optiin working commimg soon", Toast.LENGTH_SHORT).show();
               initRemarkViewBox(view);
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


       txtBTNRemarkID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               textRemarkValue = editText.getText().toString().trim();
               if (!textRemarkValue.equalsIgnoreCase("")) {
                   // callInsertRemarkAPI(textRemarkValue);
                   new callInsertRemarkAPI().execute();
               } else {
                   Toast.makeText(mContext, "Please enter remark!", Toast.LENGTH_SHORT).show();
               }
           }
       });

   }*//*

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
                jsonObj.put("cmpno", mComplainNO);
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
                    System.out.println("jo11==>>" + jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //  finish();
                    } else {

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

*/


    public String resizeBase64Image(String base64image) {

        Log.e("String", "&&&" + base64image);

        byte[] encodeByte = Base64.decode(base64image, Base64.DEFAULT);
       /* BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;*/
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        Log.e("Image", "&&&" + image);
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