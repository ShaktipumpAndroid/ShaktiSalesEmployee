package activity.complaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.DatabaseHelper;
import searchlist.complaint.SearchComplaint;
import searchlist.complaint.SearchPendingComplaintListViewAdapter;
import webservice.SAPWebService;

public class  DealerPendCompActivity extends AppCompatActivity {

    EditText editsearch;
    ListView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    SearchPendingComplaintListViewAdapter adapter;
    ArrayList<SearchComplaint> arraylist = new ArrayList<SearchComplaint>();
    Spinner filter_txt;
    private Toolbar mToolbar;
    List<String> list1 = null;
    int filter_txt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaint);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Dealer Pending Complaint");

        list = (ListView) findViewById(R.id.listview);
        mContex = this;

        list1 = new ArrayList<String>();
        filter_txt = (Spinner) findViewById(R.id.filter_txt);
        getUserTypeValue();

       getdealpendComplaint();


        con = new SAPWebService();

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
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
                        getdealpendComplaint();
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 1)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getdealpendComplaint1(0,6);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 2)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getdealpendComplaint1(7,9);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 3)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getdealpendComplaint1(10,14);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 4)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getdealpendComplaint1(15,19);
                    }
                    else{
                        Toast.makeText(mContex, "Data not Available", Toast.LENGTH_SHORT).show();
                    }

                }

                if(filter_txt1 == 5)
                {
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                        getdealpendComplaint1(20,100);
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


        if (id == android.R.id.home) {
            onBackPressed();
            //callWebPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("Range")
    public void getdealpendComplaint() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
           String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " = '" + "07" + "'"
                    + " AND (" + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' )"
                    + " AND (" + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' )";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("comp",""+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    SearchComplaint sc = new SearchComplaint();
                    sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                    sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                    sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    sc.setPending_reason(complainReason(cursor.getString(cursor.getColumnIndex("pen_res"))));

                    sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                    sc.setMob_no1(cursor.getString(cursor.getColumnIndex("alt_mob_no")));
                    sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                    sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                    sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                    sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));
                    //sc.setPenres(cursor.getString(cursor.getColumnIndex("cmp_pen_re")));

                    arraylist.add(sc);

                    // Pass results to ListViewAdapter Class
                    adapter = new SearchPendingComplaintListViewAdapter(this, arraylist,"D");


                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);

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

    public void getdealpendComplaint1(int d1,int d2) {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
         String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " = '" + "07" + "'"
                    + " AND (" + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' )"
                    + " AND (" + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' )";

            cursor = db.rawQuery(selectQuery, null);

            // Log.d("comp",""+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    SearchComplaint sc = new SearchComplaint();
                    int penday= Integer.parseInt(cursor.getString(cursor.getColumnIndex("penday")));

                    if(penday >= d1 && penday <= d2) {
                        sc.setCmpno(cursor.getString(cursor.getColumnIndex("cmpno")));
                        sc.setCmpdt(cursor.getString(cursor.getColumnIndex("cmpdt")));
                        sc.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        sc.setMob_no(cursor.getString(cursor.getColumnIndex("mob_no")));
                        sc.setMob_no1(cursor.getString(cursor.getColumnIndex("alt_mob_no")));

                        sc.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                        sc.setDistributor_code(cursor.getString(cursor.getColumnIndex("distributor_code")));
                        sc.setDistributor_name(cursor.getString(cursor.getColumnIndex("distributor_name")));
                        sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                        sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                        sc.setEpc(cursor.getString(cursor.getColumnIndex("epc")));
                        sc.setPenday(cursor.getString(cursor.getColumnIndex("penday")));
                        //sc.setPenres(cursor.getString(cursor.getColumnIndex("cmp_pen_re")));

                        arraylist.add(sc);

                        // Pass results to ListViewAdapter Class
                        adapter = new SearchPendingComplaintListViewAdapter(this, arraylist,"D");


                        // Binds the Adapter to the ListView
                        list.setAdapter(adapter);

                    }

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



    @Override
    public void onRestart() {
        DealerPendCompActivity.this.finish();
        super.onRestart();

    }


}
