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

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;

public class AttendanceReportActivity extends AppCompatActivity {
    Context context;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        progressDialog = new ProgressDialog(context);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance Report");




        selectAttendanceData();

    }


    public void selectAttendanceData() {


/**********************************processing table data *****************************************/

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(context);
        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Date", "In Time", "Out Time", "Working Hrs", "Status", "Leave Type"};

        for (String c : headerText) {
            TextView tv = new TextView(context);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.CENTER);

            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(50, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        // Create DatabaseHelper instance

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + LoginBean.userid + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Read columns data

                    String begdat = cursor.getString(cursor.getColumnIndex("begdat"));
                    String indz = cursor.getString(cursor.getColumnIndex("indz"));
                    String iodz = cursor.getString(cursor.getColumnIndex("iodz"));
                    String totdz = cursor.getString(cursor.getColumnIndex("totdz"));
                    String atn_status = cursor.getString(cursor.getColumnIndex("atn_status"));
                    String leave_typ = cursor.getString(cursor.getColumnIndex("leave_typ"));
                    Log.d("leave_typ", " " + leave_typ);
                    // dara rows
                    TableRow row = new TableRow(context);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {begdat + "", indz, iodz, totdz, atn_status, leave_typ};


                    for (String text : colText) {
                        TextView tv = new TextView(context);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
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
                db.close();
            }
            // End the transaction.

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
            case R.id.action_target_menu:
                /**************************** sync target data **************************************************/
                syncing_attendancedata();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void syncing_attendancedata() {

        progressDialog = ProgressDialog.show(context, "", "Downloading Attendance Data...!");

        new Thread() {

            public void run() {

                try {

                    // insert attendance data
                    SAPWebService con = new SAPWebService();
                    con.getAttendanceData(context);
                    // select attendance data
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    AttendanceReportActivity.this.finish();


                    Intent intent = new Intent(AttendanceReportActivity.this, AttendanceReportActivity.class);
                    startActivity(intent);
                    selectAttendanceData();


                } catch (Exception e) {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    Log.d("exce", "" + e);
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
