package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.util.TextUtils;

import database.DatabaseHelper;

public class RoutePlanDetailActivity extends AppCompatActivity {
    Context context;
    String route_date, route_name;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan_detail);

        context = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route Plan Detail");




        Intent i = getIntent();
        route_date = i.getStringExtra("route_date");
        route_name = i.getStringExtra("route_name");

        getRoutePlan();


    }

    public void getRoutePlan() {


/**********************************processing table data *****************************************/

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(context);
        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Customer Code", "Customer Name", "Country", "State", "City", "Taluka", "Address"};

        for (String c : headerText) {
            TextView tv = new TextView(context);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(50, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);


        DatabaseHelper dataHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = null;
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + DatabaseHelper.KEY_BUDAT + " = '" + route_date + "'"
                    + " AND " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'";


            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Read columns data


                    String partner_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    String partner_number = cursor.getString(cursor.getColumnIndex("phone_number"));

                    String country_txt = cursor.getString(cursor.getColumnIndex("land_txt"));
                    String state_txt = cursor.getString(cursor.getColumnIndex("state_txt"));
                    String district_txt = cursor.getString(cursor.getColumnIndex("district_txt"));
                    String taluka_txt = cursor.getString(cursor.getColumnIndex("taluka_txt"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String mob_no = cursor.getString(cursor.getColumnIndex("mob_no"));

                    if (!TextUtils.isEmpty(partner_name)) {
                        partner_name = partner_name.toLowerCase();
                    }

                    if (!TextUtils.isEmpty(address)) {
                        address = address.toLowerCase();
                    }


                    // dara rows
                    TableRow row = new TableRow(context);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {partner_number + "", partner_name, country_txt, state_txt, district_txt, taluka_txt, address};


                    for (String text : colText) {
                        TextView tv = new TextView(context);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.LEFT);
                        tv.setTextSize(14);
                        // tv.setPadding(5, 5, 5, 5);
                        tv.setPadding(40, 30, 10, 5);
                        tv.setText(text);
                        row.addView(tv);

                    }
                    tableLayout.addView(row);


                    //              draw seprator between rows
                    View line = new View(context);
                    line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                    line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    tableLayout.addView(line);


                }

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            // End the transaction.

            if (cursor != null) {
                cursor.close();
            }

            if(db!=null) {
                db.close();
            }
            // Close database

        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_target_menu:
//                /**************************** sync target data **************************************************/
//                download_routeplan();


        }
        return super.onOptionsItemSelected(item);
    }

//
//    public void download_routeplan()
//    {
//
//        progressDialog = ProgressDialog.show(context, "", "Downloading Route Plan ...!");
//
//        new Thread() {
//
//            public void run() {
//
//                try {
//
//                    // insert route plan
//                    SAPWebService con = new SAPWebService();
//                    con.getRouteRetail(context);
//
//                    // select attendance data
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    RoutePlanDetailActivity.this.finish();
//
//
//                    Intent intent = new Intent(RoutePlanDetailActivity.this,RoutePlanDetailActivity.class);
//                    startActivity(intent);
//                    getRoutePlan();
//
//
//                } catch (Exception e) {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Log.d("exce", "" + e);
//                }
//
//            }
//
//        }.start();
//
//
//
//    }


}
