package activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import bean.LoginBean;
import database.DatabaseHelper;
import searchlist.RoutePlanListViewAdapter;
import searchlist.RoutePlanSearch;

public class RoutePlanSearchActivity extends AppCompatActivity {
    Context context;
    HashSet<RoutePlanSearch> hashSet = null;
    ListView list;
    RoutePlanListViewAdapter adapter;
    EditText editsearch;
    ArrayList<RoutePlanSearch> arraylist = new ArrayList<RoutePlanSearch>();
    ArrayList<RoutePlanSearch> arraylist1 = new ArrayList<RoutePlanSearch>();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);
        context = this;
        RoutePlanSearchActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route Plan ");

        hashSet = new HashSet<RoutePlanSearch>();
        list = (ListView) findViewById(R.id.listview);

        getRoutePlan();


        // Pass results to ListViewAdapter Class
        adapter = new RoutePlanListViewAdapter(this, arraylist);

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


    public void getRoutePlan() {


        String new_record = "", old_record = "";

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = null;
        db.beginTransactionNonExclusive();

        Date dt1 = null;

        DateFormat format2 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");


        try {


//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
//                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'"
//                    + " AND " +DatabaseHelper.KEY_BUDAT  + " = '" + customutility.getCurrentDate() + "'" ;

            String userid = LoginBean.getUseid();

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + dataHelper.KEY_PERNR + " = '" + userid + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    String route_name = cursor.getString(cursor.getColumnIndex("route_name"));
                    String route_date = cursor.getString(cursor.getColumnIndex("budat"));


                    try {
                        dt1 = format1.parse(route_date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String finalDay = format2.format(dt1);

                    new_record = route_date + "--" + route_name;


                    RoutePlanSearch rs = new RoutePlanSearch();


                    if (!new_record.equalsIgnoreCase(old_record)) {
                        rs.setRoute_name(route_name);
                        rs.setDate(route_date);
                        rs.setDay(finalDay);
                        arraylist.add(rs);
                    }

                    old_record = route_date + "--" + route_name;

                }


                db.setTransactionSuccessful();

            }


        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                // End the transaction.
            }


            if (cursor != null) {
                cursor.close();
            }
            if(db!=null) {
                db.close();
            }
            // Close database
        }


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
        }
        return super.onOptionsItemSelected(item);
    }


}

