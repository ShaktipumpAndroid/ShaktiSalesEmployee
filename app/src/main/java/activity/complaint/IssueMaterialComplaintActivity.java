package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import activity.CustomUtility;
import database.DatabaseHelper;
import searchlist.complaint.SearchClouserComplaintListViewAdapter;
import searchlist.complaint.SearchComplaint;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

import static android.os.Environment.getExternalStorageDirectory;

public class IssueMaterialComplaintActivity extends AppCompatActivity {


    Context mContex;
    private Toolbar mToolbar;
    private Spinner move1_txt;
    private EditText qty1_txt;
    List<String> list = null;
    private Button save;
    private int enterqty;
    private double frreshqty;
    private String cmpno,cust_code,cust_name,matnr,matnr_name,vend_code,vend_name,move,qty,mslbqty,freshqty,movement_text;
    private TextView cmpno_txt,matnr_txt,matnr_name_txt,cust_code_txt,cust_name_txt,vend_code_txt,vend_name_txt,move_txt,qty_txt,mslbqty_txt,freshqty_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_issue_material);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        list = new ArrayList<String>();

        getUserTypeValue();

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Complaint Issue Material");

        mContex = this;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                cmpno      = null;
                cust_code  = null;
                cust_name  = null;
                matnr      = null;
                matnr_name = null;
                vend_code  = null;
                vend_name  = null;
                move       = null;
                qty        = null;
                mslbqty    = null;
                freshqty   = null;

            } else {

                cmpno      = extras.getString("cmpno");
                cust_code  = extras.getString("cust_code");
                cust_name  = extras.getString("cust_name");
                matnr      = extras.getString("matnr");
                matnr_name = extras.getString("matnr_name");
                vend_code  = extras.getString("vend_code");
                vend_name  = extras.getString("vend_name");
                move       = extras.getString("move");
                qty        = extras.getString("qty");
                mslbqty    = extras.getString("mslbqty");
                freshqty   = extras.getString("freshqty");

            }
        } else {

            cmpno      = (String) savedInstanceState.getSerializable("cmpno");
            cust_code  = (String) savedInstanceState.getSerializable("cust_code");
            cust_name  = (String) savedInstanceState.getSerializable("cust_name");
            matnr      = (String) savedInstanceState.getSerializable("matnr");
            matnr_name = (String) savedInstanceState.getSerializable("matnr_name");
            vend_code  = (String) savedInstanceState.getSerializable("vend_code");
            vend_name  = (String) savedInstanceState.getSerializable("vend_name");
            move       = (String) savedInstanceState.getSerializable("move");
            qty        = (String) savedInstanceState.getSerializable("qty");
            mslbqty    = (String) savedInstanceState.getSerializable("mslbqty");
            freshqty   = (String) savedInstanceState.getSerializable("freshqty");

        }

        cmpno_txt     = (TextView) findViewById(R.id.cmp_no);
        matnr_txt     = (TextView) findViewById(R.id.material_no);
        matnr_name_txt = (TextView) findViewById(R.id.material_name);
        vend_code_txt = (TextView) findViewById(R.id.vendor_no);
        vend_name_txt = (TextView) findViewById(R.id.vendor_name);
        cust_code_txt = (TextView) findViewById(R.id.cust_no);
        cust_name_txt = (TextView) findViewById(R.id.cust_name);
        qty_txt = (TextView) findViewById(R.id.qty1);
        mslbqty_txt = (TextView) findViewById(R.id.mslbqty);
        freshqty_txt = (TextView) findViewById(R.id.freshqty);
        move_txt = (TextView) findViewById(R.id.move);

        move1_txt = (Spinner) findViewById(R.id.movement);
        qty1_txt = (EditText) findViewById(R.id.qty);
        save = (Button) findViewById(R.id.save);

        qty1_txt.setText("1");


        cmpno_txt.setText("Complain No. " + cmpno);
        matnr_txt.setText(matnr);
        matnr_name_txt.setText(matnr_name);
        vend_code_txt.setText(vend_code);
        vend_name_txt.setText(vend_name);
        cust_code_txt.setText(cust_code);
        cust_name_txt.setText(cust_name);
        qty_txt.setText(qty);
        move_txt.setText(move);
        mslbqty_txt.setText(mslbqty);
        freshqty_txt.setText(freshqty);





        move1_txt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                movement_text = move1_txt.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        move1_txt.setPrompt("Select Movement Type");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContex, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        move1_txt.setAdapter(dataAdapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    enterqty = Integer.parseInt(qty1_txt.getText().toString());
                    frreshqty = Double.parseDouble(mslbqty_txt.getText().toString());
                    Log.e("QTY456","&&&&"+enterqty);
                    Log.e("QTY123","&&&&"+frreshqty);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                if (CustomUtility.isOnline(mContex)) {
                    if (!TextUtils.isEmpty(movement_text) && !movement_text.equalsIgnoreCase("Select Movement Type"))
                    {
                        if(!TextUtils.isEmpty(qty1_txt.getText().toString()) && !qty1_txt.getText().toString().equalsIgnoreCase("0"))
                        {
                            if(enterqty <= frreshqty)
                            {
                                new SyncInstallationData().execute();
                            }
                            else{
                                Toast.makeText(mContex, "Please enter less then or equal to available fresh quantity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(mContex, "Please enter quantity", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(mContex, "Please select movement type", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();

                }


            }
        });

        
    }

    public void getUserTypeValue() {
        list.add("Select Movement Type");
        list.add("IN");
        list.add("OUT");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();
            //callWebPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class SyncInstallationData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContex);
            progressDialog = ProgressDialog.show(mContex, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;
            String date = null;
            String time = null;

            DatabaseHelper db = new DatabaseHelper(mContex);

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();


                date = new CustomUtility().getCurrentDate();
                time = new CustomUtility().getCurrentTime();

            try {

                jsonObj.put("matnr", matnr);
                jsonObj.put("lifnr", vend_code);
                jsonObj.put("bwart", movement_text);
                jsonObj.put("qty", qty1_txt.getText().toString());
                jsonObj.put("cmpno", cmpno);
                jsonObj.put("erdate", CustomUtility.formateDate(date));
                jsonObj.put("ertime", CustomUtility.formateTime(time));



                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("norm_data", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.NORM_DATA_ENTRY, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_success");

                    JSONArray ja = new JSONArray(obj1);

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        //docno_sap = jo.getString("matnr");
                        invc_done = jo.getString("return");

                        if (invc_done.equalsIgnoreCase("Y")) {

                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler.sendMessage(msg);

                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler.sendMessage(msg);

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

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContex, mString, Toast.LENGTH_LONG).show();
        }
    };


}
