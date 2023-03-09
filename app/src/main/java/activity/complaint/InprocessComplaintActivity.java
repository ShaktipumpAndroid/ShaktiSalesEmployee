package activity.complaint;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import activity.CustomUtility;
import activity.GPSTracker;
import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import searchlist.complaint.PendingReason;
import searchlist.complaint.SearchComplaintAction;
import searchlist.complaint.SearchComplaintActionListViewAdapter;

public class InprocessComplaintActivity extends AppCompatActivity {


    EditText editText_action, editText_follow_up_date;
    Button btn_save;
    ListView list;
    String cmp_no, action,pending_cmp_category,pending_cmp_category_id;
    String userid;
    HashSet<String> hashSet = null;
    Context mContext;
    List<String> list_pen_reason = null;
    Spinner pend_cmp_reason;
    int index_pend_cmp_category;
    TextView info;
    DatePickerDialog datePickerDialog;
    SearchComplaintActionListViewAdapter adapter;
    ArrayList<SearchComplaintAction> arraylist = new ArrayList<SearchComplaintAction>();
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutName, input_layout_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprocess_complaint);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        hashSet = new HashSet<String>();

        pend_cmp_reason = (Spinner) findViewById(R.id.pend_cmp_reason);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        input_layout_date = (TextInputLayout) findViewById(R.id.input_layout_date);
        editText_action = (EditText) findViewById(R.id.text_complaint_action);

        editText_follow_up_date = (EditText) findViewById(R.id.editText_follow_up_date);

        btn_save = (Button) findViewById(R.id.btn_save);

        list = (ListView) findViewById(R.id.listview);


        userid = LoginBean.getUseid();

        Bundle bundle = getIntent().getExtras();

        cmp_no = bundle.getString("cmpno");


        list_pen_reason = new ArrayList<String>();

        getSupportActionBar().setTitle("Action on Complaint :" + " " + cmp_no);


        getComplaint();
//

        getComplaintAction();

        getPendingReason();



        adapter = new SearchComplaintActionListViewAdapter(this, arraylist);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitForm()) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            save_complaint_action();

                            editText_action.setText("");
                            editText_follow_up_date.setText("");
                            InprocessComplaintActivity.this.finish();
                        }
                    });

                    // on pressing cancel button

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }
            }
        });


        editText_follow_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(InprocessComplaintActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                editText_follow_up_date.setText(dayOfMonth + "."
                                        + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);

                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }


                datePickerDialog.show();


            }
        });


        ArrayAdapter<String> dataAdapter_cmp_category = new ArrayAdapter<String>(this, R.layout.spinner_item, list_pen_reason);
        // Drop down layout style - list view with radio button
        dataAdapter_cmp_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        pend_cmp_reason.setAdapter(dataAdapter_cmp_category);


        pend_cmp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_pend_cmp_category = arg0.getSelectedItemPosition();

                pending_cmp_category = pend_cmp_reason.getSelectedItem().toString();

                Log.e("name","&&&&"+pending_cmp_category);

                getPendingReasonid(pending_cmp_category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    public void getPendingReason() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_pen_reason.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PENDING_REASON;


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("Count","****"+cursor.getCount());

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
                    list_pen_reason.add(cursor.getString(cursor.getColumnIndex("name")));
                }

                hashSet.clear();
                hashSet.addAll(list_pen_reason);
                list_pen_reason.clear();
                list_pen_reason.add("Select Pending Reason");
                list_pen_reason.addAll(hashSet);


                db.setTransactionSuccessful();


            }
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


    }

    public void getPendingReasonid(String pend_reason_name) {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PENDING_REASON + " WHERE " + DatabaseHelper.KEY_NAME + " = '" + pend_reason_name + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("COUNT","&&&"+cursor.getCount());
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    pending_cmp_category_id = cursor.getString(cursor.getColumnIndex("cmp_pen_re"));
                }


                db.setTransactionSuccessful();

            }
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


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean category() {
        if (TextUtils.isEmpty(pending_cmp_category_id)) {

            Toast.makeText(getApplicationContext(), "Select Pending Reason", Toast.LENGTH_SHORT).show();


            return false;
        }

        return true;

    }


    public boolean submitForm() {
        boolean value;
        if (category() &&
                validateComment() &&
                        validateFollowUpDate() &&
                        validateDate() &&
                        CustomUtility.CheckGPS(this)

        ) {
            value = true;
        } else {
            value = false;
        }
        return value;

    }

    private boolean validateComment() {
        if (editText_action.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Please Enter Action");

            requestFocus(editText_action);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFollowUpDate() {
        if (editText_follow_up_date.getText().toString().trim().isEmpty()) {
            input_layout_date.setError("Please Enter Follow Up Data");

            requestFocus(editText_follow_up_date);
            return false;
        } else {
            input_layout_date.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }


    public void save_complaint_action() {

        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();
        action = editText_action.getText().toString();

        // dataHelper.deleteDSREntry();

        dataHelper.insertInprocessComplaint
                (userid,
                        LoginBean.getUsername(),
                        cmp_no,
                        editText_follow_up_date.getText().toString(), //follow_up_date
                        action,
                        pending_cmp_category_id,
                        customUtility.getCurrentDate(),
                        customUtility.getCurrentTime(),
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        "REPLY");


        // change complaint status to Reply
        setUpdateComplaintHeader(cmp_no);


        // new Capture_employee_gps_location(this,"5","");

        Toast.makeText(mContext, "Inprocess Complaint saved successfully", Toast.LENGTH_LONG).show();
        SyncInprocessComplaintInBackground();

    }

    public void SyncInprocessComplaintInBackground() {
        Intent i = new Intent(InprocessComplaintActivity.this, SyncDataService.class);
        startService(i);

    }

    public void setUpdateComplaintHeader(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(mContext);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "REPLY" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if(db!=null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }


        }
    }



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



    public void getComplaintAction() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();;
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
