package activity.complaint;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;

import activity.CustomUtility;
import adapter.Adapter_report_list;
import adapter.PendingComplainFinalListAdapter;
import adapter.PendingComplainListAdapter;
import bean.LoginBean;
import bean.vkbean.ComplainAllResponse;
import database.DatabaseHelper;
import searchlist.complaint.SearchComplaint;
import searchlist.complaint.SearchComplaintListViewAdapter;
import webservice.CustomHttpClient;
import webservice.NotifyInterface;
import webservice.SAPWebService;
import webservice.WebURL;

public class VKPendingForApprovalFinalActivity extends AppCompatActivity {
    EditText editsearch;
    ListView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    Spinner filter_txt;
    SearchComplaintListViewAdapter adapter;
    ArrayList<SearchComplaint> arraylist = new ArrayList<SearchComplaint>();
    int filter_txt1 = 0;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContex, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    List<String> list1 = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_search_final_vk);

        mContex = this;
        progressDialog = new ProgressDialog(mContex);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        WebURL.IC_PHOTO_CHECK = 1;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setTitle(bundle.getString("complaint"));

        list1 = new ArrayList<String>();

        list = (ListView) findViewById(R.id.listview);

        filter_txt = (Spinner) findViewById(R.id.filter_txt);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        //  Log.d("d_key", ""+ pref.getString("key_download_complaint","date"));
        if (!pref.getString("key_download_complaint", "date").equalsIgnoreCase(new CustomUtility().getCurrentDate())) {
            downloadComplaint();
        }

        getUserTypeValue();
        getComplaintVK(); // vikas change

      /*  // Pass results to ListViewAdapter Class
        adapter = new SearchComplaintListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
*/

        con = new SAPWebService();

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

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


        ArrayAdapter<String> dataAdapter_cmp_category = new ArrayAdapter<String>(this, R.layout.spinner_item, list1);
        // Drop down layout style - list view with radio button
        dataAdapter_cmp_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        filter_txt.setAdapter(dataAdapter_cmp_category);

        filter_txt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                filter_txt1 = arg0.getSelectedItemPosition();

                if(filter_txt1 == 0)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK();
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 1)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK(0,6);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 2)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK(7,9);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 3)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK(10,14);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 4)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK(15,19);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 5)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getComplaintVK(20,100);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void getUserTypeValue() {
        list1.add("All Complaints");
        list1.add("0-6 Days");
        list1.add("7-9 Days");
        list1.add("10-14 Days");
        list1.add("15-19 Days");
        list1.add("20 Days and Above");
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



    public void getComplaintVK() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String    selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR_VK
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )";


          /*  String    selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR_VK
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' "
                    + " AND " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' ";*/

            // +  " WHERE " +  DatabaseHelper.KEY_CMPLN_STATUS +  " != '" + "CONFIRMED" + "'"
//            + " AND " + DatabaseHelper.KEY_CMPLN_STATUS  + " != '" + "CLOSURE" + "'"
//            + " AND " + DatabaseHelper.KEY_CMPLN_STATUS  + " != '" + "IN PROCESS" + "'"
//            + " AND " + DatabaseHelper.KEY_CMPLN_STATUS  + " != '" + "REPLY" + "'" ;


            cursor = db.rawQuery(selectQuery, null);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            int tdDate = Integer.parseInt(df.format(c));


            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    SearchComplaint sc = new SearchComplaint();

                    String status= cursor.getString(cursor.getColumnIndex("cmpln_status"));
                    String fdate= cursor.getString(cursor.getColumnIndex("fdate"));
                    String save = cursor.getString(cursor.getColumnIndex("save_by"));
                    int fdate1 = 0;
                    if (!TextUtils.isEmpty(fdate)) {
                        fdate1 = Integer.parseInt(CustomUtility.formateDate(fdate));
                    }


                    if( status.equalsIgnoreCase("NEW") && fdate.equalsIgnoreCase("00.00.0000") && TextUtils.isEmpty(save)) {
                        sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                        sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                        sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                        sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));
                        sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                        sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                        sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                        sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                        sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                        sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                        sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));

                        arraylist.add(sc);

                        System.out.println("arraylist.size==>>"+arraylist.size());

                        // Pass results to ListViewAdapter Class
                        adapter = new SearchComplaintListViewAdapter(this, arraylist);

                        // Binds the Adapter to the ListView
                        list.setAdapter(adapter);
                    }
                    else if(status.equalsIgnoreCase("REPLY") && fdate1 <= tdDate && TextUtils.isEmpty(save))
                    {
                        sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                        sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                        sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));
                        sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                        sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                        sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                        sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                        sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                        sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                        sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                        sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));

                        arraylist.add(sc);

                        // Pass results to ListViewAdapter Class
                        adapter = new SearchComplaintListViewAdapter(this, arraylist);

                        // Binds the Adapter to the ListView
                        list.setAdapter(adapter);

                    }

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

    private String complainReason(String pen_res) {

        String complainReasonName =null;
        if( pen_res.equalsIgnoreCase("01")  )
        {
            complainReasonName = "Sales Return Case";
        }
        else if( pen_res.equalsIgnoreCase("02")  )
        {
            complainReasonName = "Pending from customer side";
        }
        else if( pen_res.equalsIgnoreCase("03")  )
        {
            complainReasonName = "Pending from Engineer side";
        }
        else if( pen_res.equalsIgnoreCase("04")  )
        {
            complainReasonName = "Pending from Service center side";
        }
        else if( pen_res.equalsIgnoreCase("05")  )
        {
            complainReasonName = "pending from spare parts side";
        }
        else if( pen_res.equalsIgnoreCase("06")  )
        {
            complainReasonName = "Closed";
        }
        else if( pen_res.equalsIgnoreCase("07")  )
        {
            complainReasonName = "Dealer Side Pending";
        }
        else if( pen_res.equalsIgnoreCase("08")  )
        {
            complainReasonName = "Transportation Pending";
        }
        else if( pen_res.equalsIgnoreCase("09")  )
        {
            complainReasonName = "Sales Team side Pending";
        }
        else if( pen_res.equalsIgnoreCase("10")  )
        {
            complainReasonName = "At ASC pending from cust. conf.";
        }
        else if( pen_res.equalsIgnoreCase("11")  )
        {
            complainReasonName = "Pending From Free Lancer";
        }
        else if( pen_res.equalsIgnoreCase("12")  )
        {
            complainReasonName = "Pending From Insuarance";
        }
        else if( pen_res.equalsIgnoreCase("13")  )
        {
            complainReasonName = "Pending from installation";
        }
        else if( pen_res.equalsIgnoreCase("14")  )
        {
            complainReasonName = "Spam Complaints";
        }

        return complainReasonName;
    }


    public void getComplaintVK(int d1,int d2) {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String  selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR_VK
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' "
                    + " AND " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' ";

            cursor = db.rawQuery(selectQuery, null);

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            int tdDate = Integer.parseInt(df.format(c));

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    SearchComplaint sc = new SearchComplaint();

                    int penday= Integer.parseInt(cursor.getString(cursor.getColumnIndex("penday")));

                    String status= cursor.getString(cursor.getColumnIndex("cmpln_status"));
                    String fdate= cursor.getString(cursor.getColumnIndex("fdate"));
                    String save = cursor.getString(cursor.getColumnIndex("save_by"));
                    int fdate1 = 0;
                    if (!TextUtils.isEmpty(fdate)) {
                        fdate1 = Integer.parseInt(CustomUtility.formateDate(fdate));
                    }


                    if(penday >= d1 && penday <= d2) {
                        if( status.equalsIgnoreCase("NEW") && fdate.equalsIgnoreCase("00.00.0000") && TextUtils.isEmpty(save)) {
                            sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                            sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                            sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                            sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));
                            sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                            sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                            sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                            sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                            sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                            sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                            sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                            sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));

                            arraylist.add(sc);
                            // Pass results to ListViewAdapter Class
                            adapter = new SearchComplaintListViewAdapter(this, arraylist);

                            // Binds the Adapter to the ListView
                            list.setAdapter(adapter);
                        }
                        else if(status.equalsIgnoreCase("REPLY") && fdate1 <= tdDate && TextUtils.isEmpty(save))
                        {
                            sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                            sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                            sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                            sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));
                            sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                            sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                            sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                            sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                            sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                            sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                            sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                            sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));

                            arraylist.add(sc);
                            // Pass results to ListViewAdapter Class
                            adapter = new SearchComplaintListViewAdapter(this, arraylist);

                            // Binds the Adapter to the ListView
                            list.setAdapter(adapter);

                        }


                    }

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

        progressDialog = ProgressDialog.show(mContex, "", "Download Complaint...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(mContex)) {

                    try {

                        con.getCustomerComplaintVKFinal(mContex);

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        editor.putString("key_download_complaint", new CustomUtility().getCurrentDate());
                        editor.commit(); //

                        finish();
                        Intent intent = new Intent(mContex, VKPendingForApprovalFinalActivity.class);
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
       finish();
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