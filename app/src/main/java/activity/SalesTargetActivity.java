package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;

public class SalesTargetActivity extends AppCompatActivity {
    ArrayList<String> al = null;
    Boolean data_status;
    ;
    int progressBarStatus;
    TableLayout tableLayout = null;
    double total_targrt, total_sale;
    SAPWebService con = null;
    ;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SalesTargetActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    //get user id of person
    private Toolbar mToolbar;
    Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_target);
        context = this;

        tableLayout = (TableLayout) findViewById(R.id.tablelayout);

        progressDialog = new ProgressDialog(context);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        con = new SAPWebService();
        String userid = LoginBean.getUseid();

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Target");


        selectSalesTargetData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.target_menu, menu);
        return true;
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
            case R.id.action_target_menu:
                /**************************** sync target data **************************************************/
                syncing_target_data();


                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /***************************select target data from sqlite database*******************************/

    public Boolean selectSalesTargetData() {

        /*  start target*/

        double lv_target, lv_sale;


        // Add header row

        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Emp.Code", "Emp.Name", "Target", "Sale (Lac)", "Department", "Position"};


        for (String c : headerText) {
            TextView tv = new TextView(this);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(42, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);


        // Get data from sqlite database and add them to the table
        // Open the database for reading
        // Create DatabaseHelper instance

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_TARGET
                    + " WHERE " + DatabaseHelper.KEY_TO_PERNR + " = '" + LoginBean.userid + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                data_status = true;

                while (cursor.moveToNext()) {

                    // Read columns data

                    String begda = cursor.getString(cursor.getColumnIndex("begda"));
                    String endda = cursor.getString(cursor.getColumnIndex("endda"));
                    String fr_pernr = cursor.getString(cursor.getColumnIndex("fr_pernr"));


                    String fr_ename = cursor.getString(cursor.getColumnIndex("fr_ename"));
                    String fr_department = cursor.getString(cursor.getColumnIndex("fr_department"));
                    String fr_target = cursor.getString(cursor.getColumnIndex("fr_target"));
                    String fr_net_sale = cursor.getString(cursor.getColumnIndex("fr_net_sale"));
                    String fr_position = cursor.getString(cursor.getColumnIndex("fr_position"));

                    // Log.d("target" , "" +  fr_target);


                    lv_target = Double.parseDouble(fr_target);
                    lv_sale = Double.parseDouble(fr_net_sale);

                    total_targrt = total_targrt + lv_target;
                    total_sale = total_sale + lv_sale;


                    // dara rows
                    TableRow row = new TableRow(this);

                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {fr_pernr + "", fr_ename, fr_target, fr_net_sale, fr_department, fr_position};


                    for (String text : colText) {


                        TextView tv = new TextView(this);

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
//          draw seprator between rows
                    View line = new View(this);
                    line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                    line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    tableLayout.addView(line);

                }

            } else {

                data_status = false;
            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


/***************************Table footer ***************************************************/
        TableRow rowFooter = new TableRow(this);
        rowFooter.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowFooter.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        DecimalFormat precision = new DecimalFormat("0.00");
        precision.format(total_targrt);
        precision.format(total_sale);


        String[] footerText = {"Total", "", precision.format(total_targrt), precision.format(total_sale), "",};


        for (String c : footerText) {
            TextView tv = new TextView(this);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(45, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowFooter.addView(tv);
        }


        tableLayout.addView(rowFooter);


        return data_status;
    }

    /***************************** sync target data **************************************************/
    public void syncing_target_data() {


        progressDialog = ProgressDialog.show(SalesTargetActivity.this, "", "Downloading Target Data ...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(SalesTargetActivity.this)) {


                    try {

                        con.getTargetData(SalesTargetActivity.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        SalesTargetActivity.this.finish();
                        Intent intent = new Intent(SalesTargetActivity.this, SalesTargetActivity.class);
                        startActivity(intent);
                        selectSalesTargetData();

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

}
