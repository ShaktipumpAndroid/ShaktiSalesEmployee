package activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktisalesemployee.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import activity.complaint.ComplaintSearchActivity;
import adapter.GoodIssuAdapter;
import bean.LoginBean;
import model.GoodsIssModel;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class GoodsIssue_Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private Context context;
    private TextInputEditText etcmpno,tiet_stk_rec;
    private Spinner tiet_per_typ,tiet_stk_rec1;
    private TextView submit,strg_loc,ok,ok1;
    private String cmpno,receiver,receiver_nm,spinner_tiet_per_typ,spinner_tiet_stk_rec,lgort = "0001";
    private RadioButton radiocust,radiowithcust;
    private CheckBox radioexpbom;
    private RadioGroup groupradio,groupradio1;
    private LinearLayout lin,lin0;
    private TextInputLayout til_stk_rec;
    List<String> list = null;
    int index1;
    ArrayList<String> receiver1;
    ArrayAdapter<String> dataAdapter,dataAdapter1;


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
        setContentView(R.layout.activity_good_issue);

        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        list = new ArrayList<String>();
        receiver1 = new ArrayList<String>();

        getUserTypeValue();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Goods Issue");

        etcmpno = findViewById(R.id.tiet_cmp_no);
        radiocust = findViewById(R.id.radiocust);
        radiowithcust = findViewById(R.id.radiowithcust);
        strg_loc = findViewById(R.id.strg_loc);
        radioexpbom =  findViewById(R.id.exp_bom);
        tiet_stk_rec = findViewById(R.id.tiet_stk_rec);
        tiet_stk_rec1 = findViewById(R.id.tiet_stk_rec1);

        submit  = findViewById(R.id.submit);
        ok  = findViewById(R.id.ok);
       // ok1  = findViewById(R.id.ok1);
        groupradio = findViewById(R.id.groupradio);
        groupradio1 = findViewById(R.id.groupradio1);
        lin = findViewById(R.id.lin);
        lin0 = findViewById(R.id.lin0);
        tiet_per_typ = findViewById(R.id.tiet_per_typ);
        til_stk_rec = findViewById(R.id.til_stk_rec);


        lin0.setVisibility(View.GONE);
        tiet_stk_rec1.setVisibility(View.GONE);

        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiocust) {
                    // do your stuff
                    etcmpno.setText("");
                    radioexpbom.setVisibility(View.VISIBLE);
                    strg_loc.setVisibility(View.GONE);
                    groupradio1.setVisibility(View.GONE);
                    til_stk_rec.setVisibility(View.VISIBLE);
                    tiet_stk_rec1.setVisibility(View.GONE);
                    tiet_stk_rec.setEnabled(false);
                    lin.setVisibility(View.VISIBLE);
                    lin0.setVisibility(View.GONE);
                    radioexpbom.setChecked(true);
                    tiet_stk_rec.setText("");


                } else if (checkedId == R.id.radiowithcust) {
                    // do your stuff
                    receiver1.clear();
                    list.clear();
                    getUserTypeValue();
                    radioexpbom.setVisibility(View.GONE);
                    strg_loc.setVisibility(View.VISIBLE);
                    groupradio1.setVisibility(View.VISIBLE);
                    til_stk_rec.setVisibility(View.GONE);
                    tiet_stk_rec1.setVisibility(View.VISIBLE);
                    lin.setVisibility(View.GONE);
                    lin0.setVisibility(View.VISIBLE);
                    radioexpbom.setChecked(false);

                    tiet_per_typ.setPrompt("Select Person Type");
                    // Creating adapter for spinner
                    dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item_center, list);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                    // attaching data adapter to spinner
                    tiet_per_typ.setAdapter(dataAdapter);
                }
            }
        });

        groupradio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiofreshstk) {

                    lgort = "0001";


                } else if (checkedId == R.id.radioreturnstk) {

                    lgort = "0002";
                }
            }
        });


        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etcmpno.getText().toString()))
                {
                    if (CustomUtility.isOnline(context)) {
                        new GetGoodsIssuStockRecList_Task().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }

                }
                 else {
                    Toast.makeText(getApplicationContext(), "Please Enter Complaint No.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(radiocust.isChecked())
                {
                    if (!TextUtils.isEmpty(etcmpno.getText().toString()) && !TextUtils.isEmpty(tiet_stk_rec.getText().toString()))
                    {
                        if (CustomUtility.isOnline(context)) {
                            Intent intent = new Intent(context, GoodsIssue_Activity1.class);
                            intent.putExtra("cmp_no", etcmpno.getText().toString());
                            intent.putExtra("stock_rec", tiet_stk_rec.getText().toString());
                            intent.putExtra("rec_typ", spinner_tiet_per_typ);
                            intent.putExtra("lgort", lgort);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No internet connection...., Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter complaint no. and stock receiver", Toast.LENGTH_SHORT).show();
                    }
                }

                if(radiowithcust.isChecked()) {

                    if (!TextUtils.isEmpty(tiet_per_typ.getSelectedItem().toString()) && !tiet_per_typ.getSelectedItem().toString().equalsIgnoreCase("Select Person Type")) {
                        if (!TextUtils.isEmpty(tiet_stk_rec1.getSelectedItem().toString()) && !tiet_stk_rec1.getSelectedItem().toString().equalsIgnoreCase("Select Stock Receiver")) {

                            if (CustomUtility.isOnline(context)) {
                                Intent intent = new Intent(context, GoodsIssue_Activity1.class);
                                intent.putExtra("cmp_no", "");
                                intent.putExtra("stock_rec", tiet_stk_rec1.getSelectedItem().toString());
                                intent.putExtra("rec_typ", spinner_tiet_per_typ);
                                intent.putExtra("lgort", lgort);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "No internet connection...., Please try again", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter stock receiver", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please person type", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        tiet_per_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index1 = arg0.getSelectedItemPosition();
                receiver1.clear();
                spinner_tiet_per_typ = tiet_per_typ.getSelectedItem().toString();
                if(spinner_tiet_per_typ.equalsIgnoreCase("Service Center"))
                {
                    spinner_tiet_per_typ = "SC";
                    if (CustomUtility.isOnline(context)) {
                        receiver1.clear();
                        new GetGoodsIssuStockRecList_Task1().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }


                }
                else if(spinner_tiet_per_typ.equalsIgnoreCase("Service Engineer"))
                {
                    spinner_tiet_per_typ = "SE";
                    if (CustomUtility.isOnline(context)) {
                        receiver1.clear();
                        new GetGoodsIssuStockRecList_Task1().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }


                }
                else if(spinner_tiet_per_typ.equalsIgnoreCase("Freelancer")){
                    spinner_tiet_per_typ = "FL";
                    if (CustomUtility.isOnline(context)) {
                        receiver1.clear();
                        new GetGoodsIssuStockRecList_Task1().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(context, "Please select stock receiver", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        tiet_per_typ.setPrompt("Select Person Type");
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        tiet_per_typ.setAdapter(dataAdapter);

        tiet_stk_rec1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index1 = arg0.getSelectedItemPosition();
                spinner_tiet_stk_rec = tiet_stk_rec1.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });





    }


    public void getUserTypeValue() {
        list.add("Select Person Type");
        list.add("Service Center");
        list.add("Service Engineer");
        list.add("Freelancer");
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


    private class GetGoodsIssuStockRecList_Task extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

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
            if(radiocust.isChecked())
            {
                param.add(new BasicNameValuePair("CMPNO", etcmpno.getText().toString()));
            }
            else{
                param.add(new BasicNameValuePair("CMPNO", ""));
            }
            param.add(new BasicNameValuePair("OBJS",  CustomUtility.getSharedPreferences(context,"objs")));
            if(radioexpbom.isChecked())
            {
                param.add(new BasicNameValuePair("BOM", "X" ));
            }
            else{
                param.add(new BasicNameValuePair("BOM",  ""));
            }
            param.add(new BasicNameValuePair("PARAM", "get"));


            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.STOCK_RECEIVER, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("srv_receiver");


                JSONArray ja = new JSONArray(obj1);


                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);

                    cmpno = jo.getString("cmpno");
                    receiver = jo.getString("receiver");
                    receiver_nm = jo.getString("receiver_nm");

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

            if(!TextUtils.isEmpty(receiver)) {

                tiet_stk_rec.setText(receiver+"-"+receiver_nm);

            }
            else{

                Message msg = new Message();
                msg.obj = "Please enter correct complint no.";
                mHandler1.sendMessage(msg);

            }

            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private class GetGoodsIssuStockRecList_Task1 extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

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
            param.add(new BasicNameValuePair("HELP_TYPE", spinner_tiet_per_typ));
            param.add(new BasicNameValuePair("PARAM", "get"));

            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.STOCK_RECEIVER, param);

                JSONObject object = new JSONObject(login_selec);
                String obj1 = object.getString("srv_receiver");


                JSONArray ja = new JSONArray(obj1);

                receiver1.add("Select Stock Receiver");

                for (int j = 0; j < ja.length(); j++) {
                    JSONObject jo = ja.getJSONObject(j);

                    cmpno = jo.getString("cmpno");
                    receiver = jo.getString("receiver");
                    receiver_nm = jo.getString("receiver_nm");

                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                    receiver1.add(receiver+"-"+ receiver_nm);


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

            if(!TextUtils.isEmpty(receiver)) {

                tiet_stk_rec1.setPrompt("Select Stock Recevier");
                // Creating adapter for spinner
                dataAdapter1 = new ArrayAdapter<String>(context, R.layout.spinner_item_center, receiver1);

                // Drop down layout style - list view with radio button
                dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);

                // attaching data adapter to spinner
                tiet_stk_rec1.setAdapter(dataAdapter1);
            }
            else{

                Message msg = new Message();
                msg.obj = "Please try after sometime";
                mHandler1.sendMessage(msg);

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