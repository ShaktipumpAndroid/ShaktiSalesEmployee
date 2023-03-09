package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import bean.LoginBean;
import database.DatabaseHelper;
import searchlist.complaint.PendAppComplaintListViewAdapter;
import searchlist.complaint.SearchComplaint;
import searchlist.complaint.SearchComplaintListViewAdapter;
import webservice.SAPWebService;

public class AwaitingForApprovalActivity extends AppCompatActivity {
    EditText editsearch;
    ListView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    String userid;
    Spinner filter_txt;
    PendAppComplaintListViewAdapter adapter;
    ArrayList<SearchComplaint> arraylist = new ArrayList<SearchComplaint>();
    int filter_txt1 = 0;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(AwaitingForApprovalActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    List<String> list1 = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_search);

        mContex = this;
        progressDialog = new ProgressDialog(mContex);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setTitle(bundle.getString("complaint"));

        list1 = new ArrayList<String>();

        list = (ListView) findViewById(R.id.listview);

        filter_txt = (Spinner) findViewById(R.id.filter_txt);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("key_username", "userid");


        //  Log.d("d_key", ""+ pref.getString("key_download_complaint","date"));
        if (!pref.getString("key_download_complaint", "date").equalsIgnoreCase(new CustomUtility().getCurrentDate())) {
            downloadComplaint();
        }

        getComplaint();

        //downloadComplaint();

      /*  // Pass results to ListViewAdapter Class
        adapter = new SearchComplaintListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
*/

        con = new SAPWebService();

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        filter_txt = (Spinner) findViewById(R.id.filter_txt);
        filter_txt.setVisibility(View.GONE);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter != null) {
                    adapter.filter(text);
                }
                else{
                    Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                }
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

            case R.id.action_video_list:

                downloadComplaint();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    public void getComplaint() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();


        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " != '" + "CLOSURE" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "Y" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_PERNR + " = '" + userid + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    SearchComplaint sc = new SearchComplaint();

                    sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                    sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                    sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));

                    sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                    sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                    sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                    sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));
                    sc.setAwait_apr_pernr(cursor.getString(cursor.getColumnIndex("awtpernr")));
                    sc.setAwait_apr_pernr_nm(cursor.getString(cursor.getColumnIndex("awtpernrnm")));
                    sc.setPend_apr_pernr(cursor.getString(cursor.getColumnIndex("pendpernr")));
                    sc.setPend_apr_pernr_nm(cursor.getString(cursor.getColumnIndex("pendpernrnm")));
                    sc.setAwait_apr_remark(cursor.getString(cursor.getColumnIndex("awtrmrk")));
                    sc.setPend_apr_remark(cursor.getString(cursor.getColumnIndex("pendrmrk")));
                    sc.setPend_approval(cursor.getString(cursor.getColumnIndex("pendapr")));
                    sc.setAwait_approval(cursor.getString(cursor.getColumnIndex("awtapr")));

                    arraylist.add(sc);

                    // Pass results to ListViewAdapter Class
                    adapter = new PendAppComplaintListViewAdapter(this, arraylist,"A");

                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);
                }

            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            db.close();
            // Close database

        }


    }



    public void downloadComplaint() {

        progressDialog = ProgressDialog.show(AwaitingForApprovalActivity.this, "", "Download Complaint...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(AwaitingForApprovalActivity.this)) {

                    try {

                        con.getCustomerComplaint(AwaitingForApprovalActivity.this);

                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        editor.putString("key_download_complaint", new CustomUtility().getCurrentDate());
                        editor.commit(); //

                        AwaitingForApprovalActivity.this.finish();
                        Intent intent = new Intent(mContex, AwaitingForApprovalActivity.class);
                        intent.putExtra("complaint", "New Complaint");
                        startActivity(intent);


                    } catch (Exception e) {

                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        Log.d("exce", "" + e);
                    }
                } else {

                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);


                }


            }

        }.start();

    }

    @Override
    public void onRestart() {
        AwaitingForApprovalActivity.this.finish();
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onRestart();

    }

    @Override
    protected void onPause() {
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onDestroy();
    }
}
