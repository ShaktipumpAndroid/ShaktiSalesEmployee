package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.json.JSONObject;

import webservice.CustomHttpClient;
import webservice.WebURL;
import webservice.dialog4;


public class DeviceStatusActivity extends AppCompatActivity {

    Context mContext;
    dialog4 yourDialog;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(DeviceStatusActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };

    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(DeviceStatusActivity.this, mString, Toast.LENGTH_LONG).show();

        }
    };
    private ProgressDialog progressDialog;
    private DeviceStatusActivity activity;
    TextView deviceno,cust_nam,custphno,operatornam,deviceonline,motorstatus;
    String deviceno_txt,cust_nam_txt,custphno_txt,operatornam_txt,msg_txt;
    String controller = "";
    boolean deviceonline_txt,motorstatus_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_devicestatus);
        mContext = this;

        progressDialog = new ProgressDialog(mContext);

        yourDialog = new dialog4(activity);
        yourDialog.show();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deviceno = (TextView) findViewById(R.id.device_no);
        cust_nam = (TextView) findViewById(R.id.cust_nam);
        custphno = (TextView) findViewById(R.id.cust_mb);
        operatornam = (TextView) findViewById(R.id.operator);
        deviceonline = (TextView) findViewById(R.id.dev_status);
        motorstatus = (TextView) findViewById(R.id.motr_stats);


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void searchWord(String textString) {

        if (!textString.equals("")) {
            new GetDeviceDeails_Task().execute(textString);
        } else {
            Toast.makeText(mContext, "Please Enter Controller Id.", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetDeviceDeails_Task extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            //progress = newworkorder ProgressDialog(context);
            progressDialog = ProgressDialog.show(mContext, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {


            String obj2 = null;
            String contollerid = params[0]+"-0";

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost(WebURL.DEVICE_DETAILS+"?DeviceNo="+contollerid);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {

                    JSONObject object = new JSONObject(obj2);
                    String status = object.getString("status");



                    if (status.equalsIgnoreCase("true")) {

                        JSONObject objet = new JSONObject(object.getString("response"));

                                 msg_txt = object.getString("message");
                                 deviceno_txt = objet.getString("DeviceNo");
                                 cust_nam_txt = objet.getString("CustomerName");
                                 custphno_txt = objet.getString("CustomerPhoneNo");
                                 operatornam_txt = objet.getString("OperatorName");
                                 deviceonline_txt = Boolean.parseBoolean(objet.getString("IsLogin"));
                                 motorstatus_txt = Boolean.parseBoolean(objet.getString("PumpStatus"));


                        Message msg = new Message();
                        msg.obj = msg_txt;
                        mHandler.sendMessage(msg);

                        yourDialog.dismiss();

                    } else {
                        msg_txt = object.getString("message");
                        Message msg = new Message();
                        msg.obj = msg_txt;
                        mHandler1.sendMessage(msg);


                    }
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                }

            } catch (Exception e) {
                e.printStackTrace();
                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
            }

            return obj2;

        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here

            deviceno .setText(deviceno_txt);
            cust_nam.setText(cust_nam_txt);
            custphno.setText(custphno_txt);
            operatornam.setText(operatornam_txt);

            if(deviceonline_txt)
            {
                deviceonline.setText("Online");
                deviceonline.setTextColor(Color.parseColor("#00FF00"));
            }
            else
            {
                deviceonline.setText("Offline");
                deviceonline.setTextColor(Color.parseColor("#FF0000"));
            }

            if(motorstatus_txt)
            {
                motorstatus.setText("Online");
                motorstatus.setTextColor(Color.parseColor("#00FF00"));
            }
            else
            {
                motorstatus.setText("Offline");
                motorstatus.setTextColor(Color.parseColor("#FF0000"));
            }

              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };  // dismiss dialog
        }
    }

}

