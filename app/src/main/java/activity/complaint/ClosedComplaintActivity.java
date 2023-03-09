package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.Locale;

import database.DatabaseHelper;
import searchlist.complaint.SearchClosedComplaintListViewAdapter;
import searchlist.complaint.SearchComplaint;
import webservice.SAPWebService;

public class ClosedComplaintActivity extends AppCompatActivity {

    EditText editsearch;
    ListView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    SearchClosedComplaintListViewAdapter adapter;
    ArrayList<SearchComplaint> arraylist = new ArrayList<SearchComplaint>();
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closure_complaint);
        mContex = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Closed Complaint");

        list = (ListView) findViewById(R.id.listview);


        getComplaint();

        // Pass results to ListViewAdapter Class
        adapter = new SearchClosedComplaintListViewAdapter(this, arraylist);


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

            case R.id.action_video_list:


                return true;


        }
        return super.onOptionsItemSelected(item);
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
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "CONFIRMED" + "'";


            cursor = db.rawQuery(selectQuery, null);

            Log.d("comp", "" + cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    SearchComplaint sc = new SearchComplaint();
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

}
