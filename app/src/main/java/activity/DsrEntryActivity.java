package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;


public class DsrEntryActivity extends AppCompatActivity {
    Button btn_dsr_save;
    Spinner spinner;
    int index;
    String Document_date;
    String userid, dsr_agenda,dsr_outcomes, dsr_activity_type, help_name, save_data;
    EditText editText_agenda,editText_outcomes;
    SAPWebService con = null;
    List<String> list = null;
    String[] presidents;
    TextView tvstatus;
    Context context;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(DsrEntryActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private TextInputLayout inputLayoutAgenda,inputLayoutOutcomes;


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dsr_menu, menu);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsrentry);
        context = this;
        progressDialog = new ProgressDialog(context);
        DsrEntryActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        userid = LoginBean.getUseid();
        list = new ArrayList<String>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_dsr_save = (Button) findViewById(R.id.btn_dsr_save);

        inputLayoutAgenda = (TextInputLayout) findViewById(R.id.input_layout_agenda);
        inputLayoutOutcomes = (TextInputLayout) findViewById(R.id.input_layout_outcomes);



//   get spinner value data

        selectDsrEntryHelp();


        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("DSR Entry");


        spinner = (Spinner) findViewById(R.id.spinner);

        editText_agenda = (EditText) findViewById(R.id.text_dsr_agenda);
        editText_outcomes = (EditText) findViewById(R.id.text_dsr_outcomes);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                dsr_activity_type = spinner.getSelectedItem().toString();
                help_name = dsr_activity_type;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner.setPrompt("Select Activity");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);

        // Drop down layout style - list view with radio button
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        btn_dsr_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitForm()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            save_dsr_comment_sap();
                            editText_agenda.setText("");
                            editText_outcomes.setText("");
                            dsr_activity_type = "";
                            dsr_activity_type = "";
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    private boolean submitForm() {
        boolean value;
        if (/*(validateActivityType()) &&*/
                (validateComment()) &&
                        CustomUtility.CheckGPS(this) &&
                        validateDate()
        ) {
            value = true;
        } else {
            value = false;
        }
        return value;
    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }

    //String.valueOf(index)

    private boolean validateComment() {
        if (editText_agenda.getText().toString().trim().isEmpty()) {
            inputLayoutAgenda.setError("Please Enter DSR Agenda");

            requestFocus(editText_agenda);
            return false;
        }
        else if(editText_outcomes.getText().toString().trim().isEmpty())
        {
            inputLayoutOutcomes.setError("Please Enter DSR Outcomes");

            requestFocus(editText_outcomes);
            return false;
        }
        else {
            inputLayoutAgenda.setErrorEnabled(false);
            inputLayoutAgenda.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateActivityType() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Activity Type", Toast.LENGTH_SHORT).show();
            //  inputLayoutName.setError("Please Select Activity Type");
            value = false;
        } else {
            value = true;
        }

        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();

            return true;

//            case R.id.action_dsr_menu:
//                /**************************** sync dsr data **************************************************/
//                syncing_dsr_data();
//                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    public void syncing_dsr_data() {

        progressDialog = ProgressDialog.show(DsrEntryActivity.this, "", "Syncing DSR Data...!");

        new Thread() {

            public void run() {

                try {

                    con.getSearchHelp(DsrEntryActivity.this);
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    DsrEntryActivity.this.finish();
                    Intent intent = new Intent(DsrEntryActivity.this, SalesTargetActivity.class);
                    startActivity(intent);


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

    public void selectDsrEntryHelp() {


        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        list.add("Select Activity");

        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String help_code = "1"; // 1 is use for dsr search help

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SEARCH_HELP
                    + " where  help_code = " + "'" + help_code + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    String help_name = cursor.getString(cursor.getColumnIndex("help_name"));
                    list.add(help_name);

                }

                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void save_dsr_comment_sap() {

        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        DatabaseHelper dataHelper = new DatabaseHelper(this);
        CustomUtility customUtility = new CustomUtility();
        dsr_agenda = editText_agenda.getText().toString();
        dsr_outcomes = editText_outcomes.getText().toString();
        // dataHelper.deleteDSREntry();
        dataHelper.insertDsrEntry
                (userid,
                        customUtility.getCurrentDate(),
                        customUtility.getCurrentTime(),

                        help_name,
                        dsr_agenda,
                        dsr_outcomes,
                        String.valueOf(latitude),
                        String.valueOf(longitude));


        //  new Capture_employee_gps_location(this,"5","");

        Toast.makeText(context, "DSR saved successfully", Toast.LENGTH_LONG).show();
        SyncDsrEntryInBackground();

    }

    public void SyncDsrEntryInBackground() {


//
//        progressDialog = ProgressDialog.show(DsrEntryActivity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(DsrEntryActivity.this))
//                {
//
//                    Intent i = new Intent(DsrEntryActivity.this, SyncDataService.class);
//                    i.putExtra("sync_data", "sync_dsr_entry");
//                    startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "DSR Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . DSR saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();
//

        Intent i = new Intent(DsrEntryActivity.this, SyncDataService.class);
        startService(i);


    }
}


