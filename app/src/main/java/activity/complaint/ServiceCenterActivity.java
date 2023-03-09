package activity.complaint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.Locale;

import activity.CustomUtility;
import database.DatabaseHelper;
import searchlist.complaint.SearchServiceCenter;
import searchlist.complaint.SearchServiceCenterListViewAdapter;
import webservice.SAPWebService;

public class ServiceCenterActivity extends AppCompatActivity {
    Context mContex;
    EditText editsearch;
    ListView list;
    SAPWebService con = null;
    SearchServiceCenterListViewAdapter adapter;
    ArrayList<SearchServiceCenter> arraylist = new ArrayList<SearchServiceCenter>();
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ServiceCenterActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        mContex = this;

        progressDialog = new ProgressDialog(mContex);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Service Center");

        list = (ListView) findViewById(R.id.listview);

        con = new SAPWebService();

        getServiceCenter();

        // Pass results to ListViewAdapter Class
        adapter = new SearchServiceCenterListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


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
                return true;


            case R.id.action_menu_material_analysis:
                download_service_center();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    public void getServiceCenter() {


        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.

        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SERVICE_CENTER;


            cursor = db.rawQuery(selectQuery, null);

            //  Log.d("comp", "" + cursor.getCount());

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    SearchServiceCenter sc = new SearchServiceCenter();
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setKunnr(cursor.getString(cursor.getColumnIndex("kunnr")));
                    sc.setName1(cursor.getString(cursor.getColumnIndex("customer_name")));
                    sc.setContact_person(cursor.getString(cursor.getColumnIndex("contact_person")));
                    sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));
                    sc.setTelf1(cursor.getString(cursor.getColumnIndex("mob_no")));
                    sc.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    sc.setLat_long(cursor.getString(cursor.getColumnIndex("lat_long")));

                    arraylist.add(sc);

                }
            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {

            if(db!=null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }

            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database
        }
    }

    public void download_service_center() {

        progressDialog = ProgressDialog.show(ServiceCenterActivity.this, "", "Download Service Center List...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(ServiceCenterActivity.this)) {


                    try {

                        con.getServiceCenterList(ServiceCenterActivity.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


//                    editor.putString("key_download_stock_date", customUtility.getCurrentDate());
//                    editor.commit(); //


                        ServiceCenterActivity.this.finish();
                        Intent intent = new Intent(ServiceCenterActivity.this, ServiceCenterActivity.class);
                        startActivity(intent);
                        //selectTargetData() ;

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Log.d("exce", "" + e);
                    }
                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);


                }


            }

        }.start();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_material_analysis, menu);
        return true;
    }

}
