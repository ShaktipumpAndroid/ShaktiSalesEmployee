package activity.complaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.Objects;

import database.DatabaseHelper;
import searchlist.complaint.SearchComplaintAction;
import searchlist.complaint.SearchComplaintActionListViewAdapter;

public class ComplaintActionDisplayActivity extends AppCompatActivity {
    Context mContex;
    ListView list;
    String cmp_no,pendingid;
    SearchComplaintActionListViewAdapter adapter;
    ArrayList<SearchComplaintAction> arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_action_display);
        Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        list = findViewById(R.id.listview);
        mContex = this;


        Bundle bundle = getIntent().getExtras();

        cmp_no = bundle.getString("cmpno");

        getSupportActionBar().setTitle("Complaint No :" + cmp_no + " " + "Action");


        getComplaint();

        //getPendingReasonname();
//

        getComplaintAction();

        adapter = new SearchComplaintActionListViewAdapter(this, arraylist);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


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
    public void getComplaint() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        arraylist.clear();

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);

            Log.d("comp_inprocess", "" + cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    // Log.d("cmp_person",""+cursor.getString(cursor.getColumnIndex("ename")));

                    SearchComplaintAction sc = new SearchComplaintAction();
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setPending_reason(cursor.getString(cursor.getColumnIndex("name")));
                    pendingid = cursor.getString(cursor.getColumnIndex("name"));
                    sc.setFollow_up_date(cursor.getString(cursor.getColumnIndex("follow_up_date")));
                    sc.setDate(cursor.getString(cursor.getColumnIndex("cr_date")));
                    sc.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                    sc.setStatus(cursor.getString(cursor.getColumnIndex("cmpln_status")));

                    arraylist.add(sc);

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


    @SuppressLint("Range")
    public void getComplaintAction() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COMPLAINT_ACTION
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";


            cursor = db.rawQuery(selectQuery, null);

            //Log.d("comp_action",""+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    //    Log.d("cmp_person",""+cursor.getString(cursor.getColumnIndex("ename")));

                    SearchComplaintAction sc = new SearchComplaintAction();
                    sc.setPernr(cursor.getString(cursor.getColumnIndex("pernr")));
                    sc.setEname(cursor.getString(cursor.getColumnIndex("ename")));
                    sc.setFollow_up_date(cursor.getString(cursor.getColumnIndex("follow_up_date")));
                    sc.setDate(cursor.getString(cursor.getColumnIndex("cr_date")));
                    sc.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                    sc.setStatus(cursor.getString(cursor.getColumnIndex("cmpln_status")));

                    arraylist.add(sc);

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
}
