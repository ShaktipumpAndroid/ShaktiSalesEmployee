package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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

import adapter.GoodCancAdapter;
import adapter.GoodRecpAdapter;
import bean.LoginBean;
import model.GoodsCanModel;
import model.GoodsRecpModel;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class GoodsTransDet_Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private GoodRecpAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<GoodsRecpModel> goodsList;
    LinearLayout lin1,lin2;
    LoginBean lb;
    TextView stk;

    private Button btnSelection;
    private EditText editsearch;
    private Context context;
    private String docno,docitm,docyer,matnr,qty,sernr,arktx,sendno,sendno_typ,sendnm,recno,recno_typ,recnm,type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsrecp);

        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSelection = (Button) findViewById(R.id.btnShow);
        stk = (TextView) findViewById(R.id.stk);
        stk.setVisibility(View.GONE);
        btnSelection.setVisibility(View.GONE);

        goodsList = new ArrayList<GoodsRecpModel>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transport Details Update");

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

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

    private class GetGoodsReceiptList_Task extends AsyncTask<String, String, String> {
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
            param.add(new BasicNameValuePair("OBJS",  CustomUtility.getSharedPreferences(context,"objs")));
            param.add(new BasicNameValuePair("PARAM", "get"));

            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.TRANS_ENTRY, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("srv_get_transport");


                JSONArray ja = new JSONArray(obj1);


                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);

                    docno = jo.getString("docno");
                    docitm = jo.getString("docitm");
                    docyer = jo.getString("docyear");

                    matnr = jo.getString("matnr");
                    qty = jo.getString("qty");
                    sernr = jo.getString("sernr");
                    arktx = jo.getString("arktx");
                    sendno = jo.getString("sender");
                    sendno_typ = jo.getString("sender_type");
                    recno = jo.getString("receiver");
                    recno_typ = jo.getString("receiver_type");
                    sendnm = jo.getString("sender_nm");
                    recnm = jo.getString("receiver_nm");
                    type = "T";

                    GoodsRecpModel st = new GoodsRecpModel(docno,docitm,docyer,matnr,qty,arktx,sernr,sendno,sendno_typ,sendnm,recno,recno_typ,recnm, false);

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
                mAdapter = new GoodRecpAdapter(goodsList,type,context);

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



    @Override
    protected void onResume() {
        super.onResume();
        if (CustomUtility.isOnline(context)) {

            goodsList.clear();
            mRecyclerView.setAdapter(null);

            new GetGoodsReceiptList_Task().execute();

        }
        else {
            Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}