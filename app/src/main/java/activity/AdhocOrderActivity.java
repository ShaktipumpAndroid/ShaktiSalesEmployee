package activity;

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

import java.util.ArrayList;
import java.util.Locale;

import bean.LoginBean;
import database.DatabaseHelper;
import searchlist.AdhocOrderListViewAdapter;
import searchlist.CustomerSearchAdhocOrder;

public class AdhocOrderActivity extends AppCompatActivity {
    GPSTracker gps = null;
    CustomUtility customutility = null;
    ListView list;
    AdhocOrderListViewAdapter adapter;
    EditText editsearch;
    String phone_number = null,
            partner_name = null,
            partner_class = null,
            partner_type = null,
            country = null,
            district = null;
    ArrayList<CustomerSearchAdhocOrder> arraylist = new ArrayList<CustomerSearchAdhocOrder>();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_adhoc_order);

        setContentView(R.layout.search_listview);
        AdhocOrderActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Adhoc Order");

        gps = new GPSTracker(this);
//        hashSet = new HashSet<CustomerSearch>();
        customutility = new CustomUtility();

        getCustomerForAdhocOrder();

        list = (ListView) findViewById(R.id.listview);

        // Pass results to ListViewAdapter Class
        adapter = new AdhocOrderListViewAdapter(this, arraylist);

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

    public void getCustomerForAdhocOrder() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = null;
        //db.beginTransactionNonExclusive();();
        db.beginTransactionNonExclusive();

        try {


//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
//                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'"
//                    + " AND " +DatabaseHelper.KEY_BUDAT  + " = '" + customutility.getCurrentDate() + "'" ;
//

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ADHOC_ORDER_CUSTOMER
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + LoginBean.userid + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));
                    partner_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    partner_class = cursor.getString(cursor.getColumnIndex("partner_class"));
                    partner_type = cursor.getString(cursor.getColumnIndex("partner"));
                    country = cursor.getString(cursor.getColumnIndex("land_txt"));
                    district = cursor.getString(cursor.getColumnIndex("district_txt"));

                    CustomerSearchAdhocOrder cs = new CustomerSearchAdhocOrder();

                    cs.setPhone_number(phone_number);
                    cs.setPartner_name(partner_name);
                    cs.setPartner_class(partner_class);
                    cs.setPartner_type(partner_type);
                    cs.setCountry(country);
                    cs.setDistrict(district);


                    arraylist.add(cs);


                }


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }



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
