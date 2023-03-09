package activity;

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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.Locale;

import database.DatabaseHelper;
import searchlist.VisitHistory;
import searchlist.VisitHistoryListViewAdapter;

public class VisitHistoryActivity extends AppCompatActivity {
    String ename,
            budat,
            time,
            comment,
            phone_number1,
            phone_number,
            partner_name,
            visit,
            audio_record;
    ArrayList<VisitHistory> arraylist = new ArrayList<VisitHistory>();
    ListView list;
    VisitHistoryListViewAdapter adapter;
    EditText editsearch;
    VisitHistoryListViewAdapter adapte;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_visit_history);
        setContentView(R.layout.search_listview_by_person);
        VisitHistoryActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Visit History");


        Bundle bundle = getIntent().getExtras();
        phone_number = bundle.getString("phone_number");
        getVisitHistory();


        list = (ListView) findViewById(R.id.listview);

        // Pass results to ListViewAdapter Class
        adapter = new VisitHistoryListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

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


    public void getVisitHistory() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        db.beginTransactionNonExclusive();

        try {

            String selectQuery = "SELECT * FROM " + dataHelper.TABLE_VISIT_HISTORY
                    + " WHERE " + DatabaseHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'";
            // +  " WHERE " +  DatabaseHelper.KEY_PHONE_NUMBER +  " = '" + phone_number  + "'"  + " ORDER BY id DESC " ;


            //ORDER BY gpoint DESC

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                Log.d("phone_number", "" + phone_number1);

                while (cursor.moveToNext()) {


                    phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));
                    partner_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    ename = cursor.getString(cursor.getColumnIndex("ename"));
                    budat = cursor.getString(cursor.getColumnIndex("budat"));
                    time = cursor.getString(cursor.getColumnIndex("time_in"));
                    comment = cursor.getString(cursor.getColumnIndex("comment"));
                    visit = cursor.getString(cursor.getColumnIndex("visit"));
                    audio_record = cursor.getString(cursor.getColumnIndex("audio_record"));

                    VisitHistory vh = new VisitHistory();

                    vh.setPhone_number(phone_number);
                    vh.setPartner_name(partner_name);
                    vh.setEname(ename);
                    vh.setBudat(budat);
                    vh.setTime(time);
                    vh.setComment(comment);
                    vh.setVisit(visit);
                    vh.setAudio_record(audio_record);

                    arraylist.add(vh);


                }


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database
        }


    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
