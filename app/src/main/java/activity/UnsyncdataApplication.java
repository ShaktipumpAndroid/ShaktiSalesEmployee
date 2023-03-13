package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.Locale;

import searchlist.UnSyncDataListViewAdapter;
import searchlist.UnsyncData;
import syncdata.SyncDataToSAP_New;
import webservice.SAPWebService;

public class UnsyncdataApplication extends AppCompatActivity {

    EditText editsearch;
    ListView list;
    ArrayList<UnsyncData> arraylist = new ArrayList<UnsyncData>();
    SAPWebService con = null;
    Context mContex;
    String count_dsr_entry = "",
            count_frwdapp_entry= "",
    count_take_order = "",
            count_survey = "",
            count_no_order = "",
            count_add_new_customer = "",
            count_check_in_out = "",
            count_attendance = "",
            count_clouser_complaint = "",
            count_pending_complaint;
    UnSyncDataListViewAdapter adapter;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(UnsyncdataApplication.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyncdata);

        mContex = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("UnSynced Data");

        list = (ListView) findViewById(R.id.listview);

        setUnsyncdata();


        // Pass results to ListViewAdapter Class
        adapter = new UnSyncDataListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        con = new SAPWebService();

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:
                onBackPressed();
                //callWebPage();
                return true;

            case R.id.action_menu_unsync:

                syncing_data();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unsync, menu);
        return true;
    }

    public void setUnsyncdata() {
        UnsyncData usd = null;

        Bundle bundle = getIntent().getExtras();

        count_dsr_entry = bundle.getString("count_dsr_entry");
        count_attendance = bundle.getString("count_attendance");
        count_check_in_out = bundle.getString("count_check_in_out");
        count_survey = bundle.getString("count_survey");
        count_no_order = bundle.getString("count_no_order");
        count_take_order = bundle.getString("count_order");
        count_add_new_customer = bundle.getString("count_add_new_customer");
        count_clouser_complaint = bundle.getString("count_clouser_complaint");
        count_pending_complaint = bundle.getString("count_pending_complaint");
        count_frwdapp_entry = bundle.getString("count_frwd_app");


        if (!TextUtils.isEmpty(count_attendance)) {
            usd = new UnsyncData();
            usd.setActivity_name("Attendance");
            usd.setValue(count_attendance);
            arraylist.add(usd);
        }


        if (!TextUtils.isEmpty(count_dsr_entry)) {

            usd = new UnsyncData();
            usd.setActivity_name("DSR Entry");
            usd.setValue(count_dsr_entry);
            arraylist.add(usd);
        }

        if (!TextUtils.isEmpty(count_frwdapp_entry)) {

            usd = new UnsyncData();
            usd.setActivity_name("Froward for Approval Entry");
            usd.setValue(count_frwdapp_entry);
            arraylist.add(usd);
        }


        if (!TextUtils.isEmpty(count_take_order)) {

            usd = new UnsyncData();
            usd.setActivity_name("Order");
            usd.setValue(count_take_order);
            arraylist.add(usd);
        }

        if (!TextUtils.isEmpty(count_no_order)) {

            usd = new UnsyncData();
            usd.setActivity_name("No Order");
            usd.setValue(count_no_order);
            arraylist.add(usd);
        }


        if (!TextUtils.isEmpty(count_survey)) {

            usd = new UnsyncData();
            usd.setActivity_name("Survey");
            usd.setValue(count_survey);
            arraylist.add(usd);
        }

        if (!TextUtils.isEmpty(count_check_in_out)) {

            usd = new UnsyncData();
            usd.setActivity_name("Check In-Out");
            usd.setValue(count_check_in_out);
            arraylist.add(usd);
        }

        if (!TextUtils.isEmpty(count_add_new_customer)) {

            usd = new UnsyncData();
            usd.setActivity_name("New Customer Added");
            usd.setValue(count_add_new_customer);
            arraylist.add(usd);
        }


        if (!TextUtils.isEmpty(count_pending_complaint)) {

            usd = new UnsyncData();
            usd.setActivity_name("Pending Complaint");
            usd.setValue(count_pending_complaint);
            arraylist.add(usd);
        }


        if (!TextUtils.isEmpty(count_clouser_complaint)) {

            usd = new UnsyncData();
            usd.setActivity_name("Clouser Complaint");
            usd.setValue(count_clouser_complaint);
            arraylist.add(usd);
        }


    }

    public void syncing_data() {


        progressDialog = ProgressDialog.show(UnsyncdataApplication.this, "", "Sync Offline Data !");

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isOnline(UnsyncdataApplication.this)) {

                    new SyncDataToSAP_New().SendDataToSap(mContex);

                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    arraylist.clear();

                    UnsyncdataApplication.this.finish();

                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();

    }

}
