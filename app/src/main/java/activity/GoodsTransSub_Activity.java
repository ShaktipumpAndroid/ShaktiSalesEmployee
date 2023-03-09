package activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.GoodRecpAdapter;
import bean.LoginBean;
import model.GoodsRecpModel;
import webservice.CustomHttpClient;
import webservice.WebURL;


public class GoodsTransSub_Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private Context context;
    private TextView submit;
    private String tiet_docno_txt,tiet_trans_txt,tiet_bok_dt_txt,tiet_mob_no_txt,tiet_lr_no_txt,tiet_rmrk_txt,doc_no,doc_itm,doc_yr,matnr,sernr,matnrnm;
    private TextInputEditText tiet_docno,tiet_trans,tiet_bok_dt,tiet_mob_no,tiet_lr_no,tiet_rmrk,tiet_sernr,tiet_matnr;
    private TextInputLayout til_sernr;

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
        setContentView(R.layout.activity_goodstransdet);

        context = this;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            doc_no = bundle.getString("doc_no");
            doc_itm = bundle.getString("doc_item");
            doc_yr = bundle.getString("doc_year");
            matnr = bundle.getString("matnr");
            matnrnm = bundle.getString("matnrnm");
            sernr = bundle.getString("sernr");
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transport Details Submit");


        submit = (TextView) findViewById(R.id.submit);

        tiet_docno = (TextInputEditText) findViewById(R.id.tiet_docno);
        tiet_matnr = (TextInputEditText) findViewById(R.id.tiet_matnr);
        tiet_sernr = (TextInputEditText) findViewById(R.id.tiet_sernr);
        til_sernr = (TextInputLayout) findViewById(R.id.til_sernr);
        tiet_trans = (TextInputEditText) findViewById(R.id.tiet_trans);
        tiet_bok_dt = (TextInputEditText) findViewById(R.id.tiet_bok_dt);
        tiet_mob_no = (TextInputEditText) findViewById(R.id.tiet_mob_no);
        tiet_lr_no = (TextInputEditText) findViewById(R.id.tiet_lr_no);
        tiet_rmrk = (TextInputEditText) findViewById(R.id.tiet_rmrk);

        tiet_docno.setText(doc_no+"/"+doc_itm+"/"+doc_yr);
        tiet_matnr.setText(matnr+"/"+matnrnm);
        if(TextUtils.isEmpty(sernr))
        {
            til_sernr.setVisibility(View.GONE);
        }
        else {
            til_sernr.setVisibility(View.VISIBLE);
            tiet_sernr.setText(sernr);
        }



        tiet_bok_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;

                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

                            String selectedDate = i2 + "." + i1 + "." + i;
                            Date date1 = sdf.parse(selectedDate);

                            TextView tvDt = (TextView) findViewById(R.id.tiet_bok_dt);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Booking Date");
                datePickerDialog.show();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tiet_trans_txt = tiet_trans.getText().toString();
                tiet_bok_dt_txt = tiet_bok_dt.getText().toString();
                tiet_lr_no_txt = tiet_lr_no.getText().toString();
                tiet_mob_no_txt = tiet_mob_no.getText().toString();
                tiet_rmrk_txt = tiet_rmrk.getText().toString();

                if (!TextUtils.isEmpty(tiet_trans_txt))
                {
                    if (!TextUtils.isEmpty(tiet_bok_dt_txt))
                    {
                        if (!TextUtils.isEmpty(tiet_lr_no_txt))
                        {
                    if (CustomUtility.isOnline(context)) {
                        new SynctransDetData().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet Connection...., Please try again", Toast.LENGTH_SHORT).show();
                    }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please Enter LR No.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please Select Booking Date", Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Enter Transporter Name", Toast.LENGTH_SHORT).show();
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


    private class SynctransDetData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_no = null;
            String docno_item = null;
            String docno_year = null;
            String invc_done = null;
            String obj2 = null;

                JSONArray ja_invc_data = new JSONArray();

                    JSONObject jsonObj = new JSONObject();

                    try {

                        jsonObj.put("PARAM", "post");
                        jsonObj.put("DOCNO", doc_no);
                        jsonObj.put("DOCITM", doc_itm);
                        jsonObj.put("DOCYEAR", doc_yr);
                        jsonObj.put("ZTRANSNAME", tiet_trans_txt);
                        jsonObj.put("ZBOOKDATE", CustomUtility.formateDate(tiet_bok_dt_txt));
                        jsonObj.put("ZMOBILENO", tiet_mob_no_txt);
                        jsonObj.put("ZLRNO", tiet_lr_no_txt);
                        jsonObj.put("REMARK", tiet_rmrk_txt);

                        ja_invc_data.put(jsonObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                param1_invc.add(new BasicNameValuePair("transport", String.valueOf(ja_invc_data)));
                Log.e("DATA", "$$$$" + param1_invc.toString());

                System.out.println(param1_invc.toString());

                try {

                    obj2 = CustomHttpClient.executeHttpPost1(WebURL.TRANS_ENTRY, param1_invc);

                    Log.e("OUTPUT1", "&&&&" + obj2);

                    if (obj2 != "") {

                        JSONObject object = new JSONObject(obj2);
                        String obj1 = object.getString("data_return");


                        JSONArray ja = new JSONArray(obj1);


                        Log.e("OUTPUT2", "&&&&" + ja.toString());

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);


                            docno_no = jo.getString("docno");
                            docno_item = jo.getString("docitm");
                            docno_year = jo.getString("docyear");
                            invc_done = jo.getString("return");


                            if (invc_done.equalsIgnoreCase("Y")) {

                                Message msg = new Message();
                                msg.obj = "Transporter Details of "+ docno_no+"/"+docno_item+"/"+docno_year;
                                mHandler1.sendMessage(msg);

                                progressDialog.dismiss();
                                finish();

                            } else{

                                Message msg = new Message();
                                msg.obj =  "Transporter Details of "+ docno_no+"/"+docno_item+"/"+docno_year+" not submitted";
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