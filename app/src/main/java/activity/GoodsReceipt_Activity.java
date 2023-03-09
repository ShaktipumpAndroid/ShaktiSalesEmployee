package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.GoodRecpAdapter;
import bean.LoginBean;
import database.DatabaseHelper;
import model.GoodsIssModel;
import model.GoodsRecpModel;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.os.Environment.getExternalStorageDirectory;


public class GoodsReceipt_Activity extends AppCompatActivity {

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
    private String docno,docitm,docyer,matnr,qty,sernr,arktx,sendno,sendno_typ,sendnm,recno,recno_typ,recnm,lgort = "0001",type;


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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSelection = (Button) findViewById(R.id.btnShow);
        stk = (TextView) findViewById(R.id.stk);
        stk.setVisibility(View.VISIBLE);
        btnSelection.setText("Add Material");

        goodsList = new ArrayList<GoodsRecpModel>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Goods Receipts");

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Storage Location");
        builder.setCancelable(false);
        final String[] stock = {"Fresh Stock", "Return Stock"};
        int checkedItem = 0;
        builder.setSingleChoiceItems(stock, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                if(stock[which].equalsIgnoreCase("Fresh Stock"))
                {
                    lgort = "0001";
                }
                else{
                    lgort = "0002";
                }

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                if(!TextUtils.isEmpty(lgort)) {
                    if (CustomUtility.isOnline(context)) {

                        goodsList.clear();
                        mRecyclerView.setAdapter(null);

                        new GetGoodsReceiptList_Task().execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select storage location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
               finish();
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


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

                        new SyncGoodsRecpData().execute();

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


            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.GOODS_RECP, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("srv_stock_in");


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
                    type = "R";

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

    private class SyncGoodsRecpData extends AsyncTask<String, String, String> {

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

            CustomUtility customUtility = new CustomUtility();

            ArrayList<GoodsRecpModel> stList1 = new ArrayList<GoodsRecpModel>();

            stList1.clear();

            List<GoodsRecpModel> stList = ((GoodRecpAdapter) mAdapter)
                    .getGoodsist();

            for (int i = 0; i < stList.size(); i++) {
                GoodsRecpModel singleGoods = stList.get(i);
                if (singleGoods.isSelected()) {
                    stList1.add(stList.get(i));
                }

            }


            if (stList1.size() != 0) {

                JSONArray ja_invc_data = new JSONArray();

                for(int i = 0; i < stList1.size() ; i++) {

                    JSONObject jsonObj = new JSONObject();

                    try {

                        jsonObj.put("param", "post");
                        if(TextUtils.isEmpty(lgort))
                        {
                            jsonObj.put("lgort", "0001");
                        }
                        else{
                            jsonObj.put("lgort", lgort);
                        }
                        jsonObj.put("lgort", lgort);
                        jsonObj.put("objs", CustomUtility.getSharedPreferences(context,"objs"));
                        jsonObj.put("erdat", customUtility.getCurrentDate());
                        jsonObj.put("matnr", stList1.get(i).getMatnr());
                        jsonObj.put("sernr", stList1.get(i).getSernr());
                        jsonObj.put("qty", stList1.get(i).getQty());
                        jsonObj.put("ref_docno", stList1.get(i).getDocno());
                        jsonObj.put("ref_docitm", stList1.get(i).getDocitm());
                        jsonObj.put("ref_docyear", stList1.get(i).getDocyear());
                        jsonObj.put("sender", stList1.get(i).getSender());
                        jsonObj.put("sender_type", stList1.get(i).getSender_typ());
                        jsonObj.put("receiver", stList1.get(i).getReceiver());
                        jsonObj.put("receiver_type", stList1.get(i).getReceiver_typ());


                        ja_invc_data.put(jsonObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                param1_invc.add(new BasicNameValuePair("grn", String.valueOf(ja_invc_data)));
                Log.e("DATA", "$$$$" + param1_invc.toString());

                System.out.println(param1_invc.toString());

                try {

                    obj2 = CustomHttpClient.executeHttpPost1(WebURL.GOODS_RECP, param1_invc);

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