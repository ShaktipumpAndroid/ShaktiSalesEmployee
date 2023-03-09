package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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

import adapter.GoodStockAdapter;
import bean.LoginBean;
import model.GoodsStockrRepModel;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class StockDetailRep_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;
    private RecyclerView mRecyclerView;
    private GoodStockAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<GoodsStockrRepModel> goodsList;
    LinearLayout lin1,lin2;
    private EditText editsearch;
    private String matnr,qty,sernr,arktx,lgort = "0001";
    private Button btnSelection;
    private TextView stk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsrecp);

        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        goodsList = new ArrayList<GoodsStockrRepModel>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Stock Details Report");

        stk = (TextView) findViewById(R.id.stk);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        btnSelection = (Button) findViewById(R.id.btnShow);
        btnSelection.setVisibility(View.GONE);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


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

                        new GetStochdetReportList_Task().execute();

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


    private class GetStochdetReportList_Task extends AsyncTask<String, String, String> {
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
                    arktx = jo.getString("maktx");

                    GoodsStockrRepModel st = new GoodsStockrRepModel(matnr,qty,arktx,sernr);

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
                mAdapter = new GoodStockAdapter(goodsList);

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
    }
}