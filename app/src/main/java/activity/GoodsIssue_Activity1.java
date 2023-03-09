package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.GoodIssuAdapter;
import adapter.GoodRecpAdapter;
import bean.LoginBean;
import model.GoodsIssModel;
import model.GoodsRecpModel;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class GoodsIssue_Activity1 extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private GoodIssuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<GoodsIssModel> goodsList;
    LinearLayout lin1,lin2;
    LoginBean lb;

    private Button btnSelection;
    private String[] separated;
    private EditText editsearch;
    private Context context;
    private String docno,docitm,docyer,matnr,qty,sernr,arktx,sendno,sendnm,recno,recnm,cmp_no,stock_rec,sernr1,qty1,maktx,rec_typ,lgort;
    private TextView stk;


    android.os.Handler mHandler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsrecp);

        context = this;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            cmp_no = bundle.getString("cmp_no");
            stock_rec = bundle.getString("stock_rec");
            rec_typ = bundle.getString("rec_typ");
            lgort = bundle.getString("lgort");

            separated = stock_rec.split("-");
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSelection = (Button) findViewById(R.id.btnShow);
        stk = (TextView) findViewById(R.id.stk);
        btnSelection.setText("Issue Material");

       /* if(!TextUtils.isEmpty(lgort))
        {
            stk.setVisibility(View.VISIBLE);
            if(lgort.equalsIgnoreCase("0001"))
            {
                stk.setText("FRESH STOCK");
            }
            else{
                stk.setText("RETURN STOCK");
            }

        }*/

        goodsList = new ArrayList<GoodsIssModel>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Goods Issue");

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);


        if (CustomUtility.isOnline(context)) {

            goodsList.clear();
            mRecyclerView.setAdapter(null);

            new GetGoodsIssueList_Task().execute();

        }
        else {
            Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
        }

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                mAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        btnSelection.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                    if (CustomUtility.isOnline(context)) {

                                    new SyncGoodsIssData().execute();
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetGoodsIssueList_Task extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.clear();
            param.add(new BasicNameValuePair("PERNR", LoginBean.getUseid()));
            param.add(new BasicNameValuePair("CMPNO", cmp_no));
            param.add(new BasicNameValuePair("OBJS",  CustomUtility.getSharedPreferences(context,"objs")));
            if(TextUtils.isEmpty(lgort))
            {
                param.add(new BasicNameValuePair("LGORT", "0001"));
                stk.setText("FRESH STOCK");
            }
            else{
                param.add(new BasicNameValuePair("LGORT", lgort));
                if(lgort.equalsIgnoreCase("0001"))
                {
                    stk.setText("FRESH STOCK");
                }
                else{
                    stk.setText("RETURN STOCK");
                }
            }
            param.add(new BasicNameValuePair("PARAM", "get"));

            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.STOCK_ISSUE, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("srv_stock_issue");


                JSONArray ja = new JSONArray(obj1);


                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);


                    matnr = jo.getString("matnr");
                    qty = jo.getString("qty");
                    sernr = jo.getString("sernr");
                    maktx = jo.getString("maktx");
                    sernr1 = "";
                    qty1 = "";

                    GoodsIssModel st = new GoodsIssModel(matnr,qty,maktx,sernr,sernr1,qty1, false);

                    goodsList.add(st);



                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            return login_selec;

        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {


            if (goodsList != null && goodsList.size() > 0) {


                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                // create an Object for Adapter
                mAdapter = new GoodIssuAdapter(goodsList,context,cmp_no);

                // set the adapter object to the Recyclerview
                mRecyclerView.setAdapter(mAdapter);


            } else {

                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);

            }
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private class SyncGoodsIssData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            ArrayList<GoodsIssModel> stList1 = new ArrayList<GoodsIssModel>();

            List<GoodsIssModel> stList = ((GoodIssuAdapter) mAdapter)
                    .getgoodstist();

            for (int i = 0; i < stList.size(); i++) {
                GoodsIssModel singleGoods = stList.get(i);
                if ((singleGoods.isSelected())) {

                    stList1.add(stList.get(i));
                }
            }

            if (stList1.size() != 0) {

                JSONArray ja_invc_data = new JSONArray();

                for(int i = 0; i < stList1.size() ; i++) {

                    JSONObject jsonObj = new JSONObject();

                    try {
                        jsonObj.put("param", "post");
                        jsonObj.put("objs", CustomUtility.getSharedPreferences(context,"objs"));
                        jsonObj.put("cmpno", cmp_no);
                        jsonObj.put("sender", LoginBean.getUseid());
                        jsonObj.put("lgort", lgort);
                        if(CustomUtility.getSharedPreferences(context,"objs").equalsIgnoreCase("SRV_CNTR"))
                        {
                            jsonObj.put("sender_type", "SC" );
                        }
                        else{
                            jsonObj.put("sender_type", "");
                        }
                        jsonObj.put("receiver", separated[0]);
                        if(TextUtils.isEmpty(cmp_no))
                        {
                            jsonObj.put("receiver_type", rec_typ);
                        }
                        else {
                            jsonObj.put("receiver_type", "CUS");
                        }
                        jsonObj.put("matnr", stList1.get(i).getMatnr());
                        jsonObj.put("sernr", stList1.get(i).getSernr());
                        jsonObj.put("qty", stList1.get(i).getQty());
                        jsonObj.put("IN_SERNR", stList1.get(i).getSernr());
                        jsonObj.put("ISSUE_QTY", stList1.get(i).getQty());


                        ja_invc_data.put(jsonObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                param1_invc.add(new BasicNameValuePair("issue", String.valueOf(ja_invc_data)));
                Log.e("DATA", "$$$$" + param1_invc.toString());

                System.out.println(param1_invc.toString());

                try {

                    obj2 = CustomHttpClient.executeHttpPost1(WebURL.STOCK_ISSUE, param1_invc);

                    Log.e("OUTPUT1", "&&&&" + obj2);

                    if (obj2 != "") {

                        JSONObject object = new JSONObject(obj2);
                        String obj1 = object.getString("data_return");


                        JSONArray ja = new JSONArray(obj1);


                        Log.e("OUTPUT2", "&&&&" + ja.toString());

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);


                            docno_sap = jo.getString("mdocno");
                            invc_done = jo.getString("return");


                            if (invc_done.equalsIgnoreCase("X")) {

                                Message msg = new Message();
                                msg.obj = docno_sap;
                                mHandler1.sendMessage(msg);

                                progressDialog.dismiss();
                                finish();

                            } else{

                                Message msg = new Message();
                                msg.obj = docno_sap;
                                mHandler1.sendMessage(msg);

                                progressDialog.dismiss();
                                finish();
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(context, "Please Select Material", Toast.LENGTH_SHORT).show();
        }
            return obj2;
            }


        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}