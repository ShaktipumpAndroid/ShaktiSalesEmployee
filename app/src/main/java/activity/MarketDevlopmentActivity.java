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

import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;

public class MarketDevlopmentActivity extends AppCompatActivity {
    TableLayout tableLayout = null;
    SAPWebService con = null;
    Context context;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MarketDevlopmentActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_devlopment);
        context = this;

        progressDialog = new ProgressDialog(context);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        setSupportActionBar(mToolbar);
        con = new SAPWebService();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity Target");


        selectActivityTarget();
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
                syncing_target_data();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void selectActivityTarget() {

        /*  start target*/

        int lv_target, lv_achv, total_achv = 0, total_target = 0;


        // Add header row

        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Emp.Name", "Activity", "Target", "Achievement"};


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
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ACTIVITY_TARGET + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + LoginBean.userid.trim() + "'";
            //String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ACTIVITY_TARGET + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + "3894" + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.d("activity", "" + cursor.getCount() + "--" + LoginBean.userid);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    // Read columns data

                    String begda = cursor.getString(cursor.getColumnIndex("begda"));
                    String endda = cursor.getString(cursor.getColumnIndex("endda"));
                    String pernr = cursor.getString(cursor.getColumnIndex("pernr"));
                    String ename = cursor.getString(cursor.getColumnIndex("ename"));

                    String activity_code = cursor.getString(cursor.getColumnIndex("activity_code"));
                    String activity_name = cursor.getString(cursor.getColumnIndex("activity_name"));
                    String indv_act_target = cursor.getString(cursor.getColumnIndex("indv_act_target"));
                    String indv_act_achievement = cursor.getString(cursor.getColumnIndex("indv_act_achievement"));

                    String hrcy_act_target = cursor.getString(cursor.getColumnIndex("hrcy_act_target"));
                    String hrcy_act_achievement = cursor.getString(cursor.getColumnIndex("hrcy_act_achievement"));


                    lv_target = Integer.parseInt(hrcy_act_target);
                    lv_achv = Integer.parseInt(hrcy_act_achievement);

                    total_target = total_target + lv_target;
                    total_achv = total_achv + lv_achv;


                    // dara rows
                    TableRow row = new TableRow(this);

                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {ename + "", activity_name, hrcy_act_target, hrcy_act_achievement};


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


            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                db.close();
            }

        }


/***************************Table footer ***************************************************/
        TableRow rowFooter = new TableRow(this);
        rowFooter.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowFooter.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


//
        String[] footerText = {"Total", "", String.valueOf(total_target), String.valueOf(total_achv), "",};


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


    }

    public void syncing_target_data() {

        progressDialog = ProgressDialog.show(MarketDevlopmentActivity.this, "", "Downloading Activity Target !");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(MarketDevlopmentActivity.this)) {


                    try {

                        con.getTargetData(MarketDevlopmentActivity.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        MarketDevlopmentActivity.this.finish();
                        Intent intent = new Intent(MarketDevlopmentActivity.this, MarketDevlopmentActivity.class);
                        startActivity(intent);
                        selectActivityTarget();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.target_menu, menu);
        return true;
    }
}
