package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import webservice.CustomHttpClient;
import webservice.WebURL;

public class MouActivity extends AppCompatActivity {
    Context mContext;
    SharedPreferences pref;
    String phone_number = "", mou = "0.0", billing = "0.0", outstanding = "0.0", backlog = "0.0";
    TextView tv_mou, tv_billing, tv_outstanding, tv_backlog, tv_date;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mou);

        mContext = this;
        progressDialog = new ProgressDialog(mContext);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mou Details");


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();

        phone_number = bundle.getString("phone_number");
        mou = bundle.getString("mou");
        billing = bundle.getString("billing");
        outstanding = bundle.getString("outstanding");
        backlog = bundle.getString("backlog");


        tv_mou = (TextView) findViewById(R.id.mou);
        tv_billing = (TextView) findViewById(R.id.billing);
        tv_outstanding = (TextView) findViewById(R.id.outstanding);
        tv_backlog = (TextView) findViewById(R.id.backlog);
        tv_date = (TextView) findViewById(R.id.date);


        tv_billing.setText(billing);
        tv_mou.setText(mou);
        tv_backlog.setText(backlog);
        tv_outstanding.setText(outstanding);

        String date_time = new CustomUtility().getCurrentDate() + "             " + new CustomUtility().getCurrentTime();

        tv_date.setText(date_time);


        if (billing.equalsIgnoreCase("0.0")) {
            downloadMou();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.target_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_target_menu:
                /**************************** sync material data **************************************************/
                downloadMou();


                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadMou() {

        progressDialog = ProgressDialog.show(mContext, "", "Connecting to Server.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("pernr", pref.getString("key_username", "userid")));
        param.add(new BasicNameValuePair("kunnr", phone_number));


        new Thread(new Runnable() {


            @Override
            public void run() {


                if (CustomUtility.isOnline(mContext)) {


                    try {
                        String obj = "";


                        obj = CustomHttpClient.executeHttpPost1(WebURL.MOU_BILL, param);
                        //  Log.d("obj",obj);

                        if (!TextUtils.isEmpty(obj)) {

                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                            JSONObject jsonObj = new JSONObject(obj);
                            JSONArray ja_action = jsonObj.getJSONArray("mou_bill");


                            for (int i = 0; i < ja_action.length(); i++) {
                                JSONObject jo_action = ja_action.getJSONObject(i);

//                                Log.d("mou",jo_action.getString("bill")+
//
//
//                                        jo_action.getString("mou") + jo_action.getString("outstanding")
//                                ) ;


                                billing = jo_action.getString("bill");
                                mou = jo_action.getString("mou");
                                backlog = jo_action.getString("backlog");
                                outstanding = jo_action.getString("outstanding");
                                //  Log.d("mou1",mou+"--"+billing+"--"+outstanding);


                            }


                            if (!billing.equalsIgnoreCase("0.0")) {

                                MouActivity.this.finish();
                                Intent intent = new Intent(mContext, MouActivity.class);
                                intent.putExtra("phone_number", phone_number);
                                intent.putExtra("mou", mou);
                                intent.putExtra("billing", billing);
                                intent.putExtra("outstanding", outstanding);
                                intent.putExtra("backlog", backlog);
                                mContext.startActivity(intent);
                            }


                        }

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    }

                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    Message msg = new Message();
                    msg.obj = "Please on internet Connection for this function.";
                    mHandler.sendMessage(msg);


                }


            }
        }).start();


    }

}
