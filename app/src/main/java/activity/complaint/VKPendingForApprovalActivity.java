package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import adapter.PendingComplainListAdapter;
import bean.vkbean.ComplainAllResponse;
import database.DatabaseHelper;
import searchlist.complaint.PendAppComplaintListViewAdapter;
import searchlist.complaint.SearchComplaint;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

public class VKPendingForApprovalActivity extends AppCompatActivity {
    private Context mContext;
    private ProgressDialog progressDialog;
    private List<ComplainAllResponse> mComplainAllResponse;
    RecyclerView rclyPendingComplainList;
    private ImageView imgBackID;
    private TextView txtHeaderID;
    private  String mHeaderTittle= "";
    private  String mStatusValue= "";
    private String mUserID;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //private BaseRequest baseRequest;

    private Intent mmIntent;

    private PendingComplainListAdapter mPendingComplainListAdapter;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(VKPendingForApprovalActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_search_vk);

        mContext = this;
        mmIntent = getIntent();
        initView();
    }

    private void initView() {

     //   mUserID = CustomUtility.getSharedPreferences(mContext,"userID");

        mHeaderTittle = mmIntent.getStringExtra("complaint");
        mStatusValue = mmIntent.getStringExtra("StatusValue");
        mStatusValue = "02";

        mComplainAllResponse = new ArrayList<>();

        imgBackID = findViewById(R.id.imgBackID);
        txtHeaderID = findViewById(R.id.txtHeaderID);
        rclyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
        rclyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));

       // txtHeaderID.setText(mHeaderTittle);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        mUserID = pref.getString("key_username", "userid");



        initClickEvent();
        callgetCompalinAllListAPI();
    }

    private void initClickEvent() {

        imgBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void callgetCompalinAllListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        //param.add(new BasicNameValuePair("kunnr", mUserID));//kunur it means user id
        param.add(new BasicNameValuePair("pernr", mUserID));//kunur it means user id
      //  param.add(new BasicNameValuePair("status", mStatusValue));///Pending Complaint
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));
        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));
        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_ALL_LIST_VK_PAGE, param);
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

                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        if(mComplainAllResponse.size()>0)
                            mComplainAllResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        System.out.println("ja==>>"+ja.get(0));

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            ComplainAllResponse mmComplainAllResponse = new ComplainAllResponse();

                            mmComplainAllResponse.setCmpno(join.getString("cmpno"));
                            mmComplainAllResponse.setCmpdt(join.getString("cmpdt"));
                            mmComplainAllResponse.setCaddress(join.getString("caddress"));
                            mmComplainAllResponse.setMblno(join.getString("mblno"));
                            mmComplainAllResponse.setMblno1(join.getString("mblno1"));
                            mmComplainAllResponse.setCstname(join.getString("cstname"));
                            mmComplainAllResponse.setKunnr(join.getString("kunnr"));
                            mmComplainAllResponse.setName1(join.getString("name1"));
                            mmComplainAllResponse.setPernr(join.getString("pernr"));
                            mmComplainAllResponse.setEname(join.getString("ename"));
                            mmComplainAllResponse.setLand(join.getString("land"));
                            mmComplainAllResponse.setCity(join.getString("city"));
                            mmComplainAllResponse.setRegio(join.getString("regio"));
                            mmComplainAllResponse.setTehsil(join.getString("tehsil"));
                            mmComplainAllResponse.setCatgry(join.getString("catgry"));
                            mmComplainAllResponse.setEdit(join.getString("edit"));
                            mmComplainAllResponse.setCatgry1(join.getString("catgry1"));
                            mmComplainAllResponse.setDeln0(join.getString("deln0"));
                            mmComplainAllResponse.setDelname(join.getString("delname"));
                            mmComplainAllResponse.setEpc(join.getString("epc"));
                            mmComplainAllResponse.setLifnr(join.getString("lifnr"));
                            mmComplainAllResponse.setCmpPenRe(join.getString("cmp_pen_re"));
                            mmComplainAllResponse.setFdate(join.getString("fdate"));
                            mmComplainAllResponse.setAwaitAprPernr(join.getString("await_apr_pernr"));
                            mmComplainAllResponse.setAwaitAprPernrNm(join.getString("await_apr_pernr_nm"));
                            mmComplainAllResponse.setPendAprPernr(join.getString("pend_apr_pernr"));
                            mmComplainAllResponse.setPendAprPernrNm(join.getString("pend_apr_pernr_nm"));
                            mmComplainAllResponse.setAwaitApproval(join.getString("await_approval"));
                            mmComplainAllResponse.setPendApproval(join.getString("pend_approval"));
                            mmComplainAllResponse.setAwaitAprRemark(join.getString("await_apr_remark"));
                            mmComplainAllResponse.setPendAprRemark(join.getString("pend_apr_remark"));
                            mmComplainAllResponse.setScStatus(join.getString("sc_status"));
                            mmComplainAllResponse.setSengno(join.getString("sengno"));
                         //   customer_gps.setCustomer_name(jo.has("CustomerName") ? jo.getString("CustomerName") : "");
                           // mmComplainAllResponse.setAtt_cmpno(join.getString("att_cmpno"));
                            mmComplainAllResponse.setAtt_cmpno(join.has("att_cmpno") ? join.getString("att_cmpno") : "");
                            mmComplainAllResponse.setAtt_posnr(join.has("att_posnr") ? join.getString("att_posnr") : "");
                           // mmComplainAllResponse.setAtt_posnr(join.getString("att_posnr"));

                            mComplainAllResponse.add(mmComplainAllResponse);

                        }


                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                                mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse, mStatusValue);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);
                                progressDialog.dismiss();
                            }

                        });

/*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Intent mIntent = new Intent(ActivityPODSearchInfo.this, LrtransportList.class);
                                    mIntent.putExtra("InvoiceList", (Serializable) mLrInvoiceResponse);
                                    startActivity(mIntent);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        });*/

                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                               /* mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);*/
                                progressDialog.dismiss();
                            }

                        });
                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    // dismiss the progress dialog
                    progressDialog.dismiss();
                }
            }

        }.start();
    }

}