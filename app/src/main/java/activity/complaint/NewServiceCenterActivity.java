package activity.complaint;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import activity.CustomUtility;
import activity.GPSTracker;
import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;

public class NewServiceCenterActivity extends AppCompatActivity {
    Spinner spinner_customer_type,
            spinner_country,
            spinner_state,
            spinner_district,
            spinner_taluka;
    Button btn_customer_save;
    ArrayAdapter<String> dataAdapter_district;
    ArrayAdapter<String> dataAdapter_taluka;
    ArrayAdapter<String> dataAdapter_state;
    SAPWebService con = null;
    Context context;
    HashSet<String> hashSet = null;
    List<String> list_customer_type = null,
            list_customer_class = null,
            list_country = null,
            list_state = null,
            list_district = null,
            list_taluka = null;
    EditText edittext_customer_name,
            edittext_mobile_no,
            edittext_landline,
            edittext_email,
            edittext_aadharcard,
            edittext_pancard,
            edittext_tinno,
            edittext_market,
            edittext_contact_person_name,
            edittext_contact_person_phone,
            edittext_address,
            edittext_pincode;
    String route_name,
            route_code,
            userid,
            country,
            state,
            district,
            taluka,
            customer_type,
            customer_class,
            primary_partner,
            interesting,
            vkorg,
            vtweg,
            ktokd,


    email;
    TextInputLayout input_customer_name,
            input_mobile_no,
            input_landline,
            input_email,
            input_aadharcard,
            input_pancard,
            input_tinno,
            input_market,
            input_contact_person_name,
            input_contact_person_phone,
            input_address,
            input_pincode;
    int
            index_customer_type,
            index_country,
            index_state,
            index_district,
            index_taluka;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(NewServiceCenterActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_center);
        context = this;
        progressDialog = new ProgressDialog(context);

        NewServiceCenterActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Service Center");


        list_customer_type = new ArrayList<String>();
        list_customer_class = new ArrayList<String>();
        list_country = new ArrayList<String>();
        list_state = new ArrayList<String>();
        list_district = new ArrayList<String>();
        list_taluka = new ArrayList<String>();
        hashSet = new HashSet<String>();
        userid = LoginBean.getUseid();
        con = new SAPWebService();

        input_customer_name = (TextInputLayout) findViewById(R.id.input_customer_name);
        input_mobile_no = (TextInputLayout) findViewById(R.id.input_mobile_no);
        input_landline = (TextInputLayout) findViewById(R.id.input_landline);
        input_email = (TextInputLayout) findViewById(R.id.input_email);
        input_aadharcard = (TextInputLayout) findViewById(R.id.input_aadharcard);
        input_pancard = (TextInputLayout) findViewById(R.id.input_pancard);
        input_tinno = (TextInputLayout) findViewById(R.id.input_tin);
        input_market = (TextInputLayout) findViewById(R.id.input_market);
        input_contact_person_name = (TextInputLayout) findViewById(R.id.input_contact_person_name);
        input_contact_person_phone = (TextInputLayout) findViewById(R.id.input_contact_person_phone);
        input_address = (TextInputLayout) findViewById(R.id.input_address);
        input_pincode = (TextInputLayout) findViewById(R.id.input_pincode);


        edittext_customer_name = (EditText) findViewById(R.id.et_customer_name);
        edittext_mobile_no = (EditText) findViewById(R.id.et_mobile_no);
        edittext_landline = (EditText) findViewById(R.id.et_landline);
        edittext_email = (EditText) findViewById(R.id.et_email);
        edittext_aadharcard = (EditText) findViewById(R.id.et_aadharcard);
        edittext_pancard = (EditText) findViewById(R.id.et_pancard);
        edittext_tinno = (EditText) findViewById(R.id.et_tinno);
        edittext_market = (EditText) findViewById(R.id.et_market);
        edittext_contact_person_name = (EditText) findViewById(R.id.et_contact_person_name);
        edittext_contact_person_phone = (EditText) findViewById(R.id.et_contact_person_phone);
        edittext_address = (EditText) findViewById(R.id.et_address);
        edittext_pincode = (EditText) findViewById(R.id.et_pincode);
        // edittext_dob                  = (EditText)findViewById(R.id.et_dob);


        spinner_customer_type = (Spinner) findViewById(R.id.spinner_customer_type);

        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        spinner_taluka = (Spinner) findViewById(R.id.spinner_taluka);

        btn_customer_save = (Button) findViewById(R.id.btn_customer_save);


        email = edittext_email.getText().toString().toLowerCase().trim();


        spinner_customer_type.setPrompt("Customer Type");
        spinner_country.setPrompt("Country");
        spinner_state.setPrompt("State");
        spinner_district.setPrompt("District");
        spinner_taluka.setPrompt("Taluka");




        getSearchHelp();
        getPartnerTypeHelp();

        ArrayAdapter<String> dataAdapter_customer_type = new ArrayAdapter<String>(this, R.layout.spinner_item, list_customer_type);

        // Drop down layout style - list view with radio button
        dataAdapter_customer_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_customer_type.setAdapter(dataAdapter_customer_type);


        ArrayAdapter<String> dataAdapter_country = new ArrayAdapter<String>(this, R.layout.spinner_item, list_country);
        dataAdapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(dataAdapter_country);


        dataAdapter_state = new ArrayAdapter<String>(this, R.layout.spinner_item, list_state);
        dataAdapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner_state.setAdapter(dataAdapter_state);


        dataAdapter_district = new ArrayAdapter<String>(this, R.layout.spinner_item, list_district);
        dataAdapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner_district.setAdapter(dataAdapter_district);


        dataAdapter_taluka = new ArrayAdapter<String>(this, R.layout.spinner_item, list_taluka);
        dataAdapter_taluka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  spinner_taluka.setAdapter(dataAdapter_taluka);


        spinner_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index_customer_type = arg0.getSelectedItemPosition();

                customer_type = spinner_customer_type.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        btn_customer_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (submitForm()) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to save data ?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            saveNewAddedCustomer();
                            //   clearScreenNewAddedCustomer();
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

            case R.id.action_menu_material_analysis:
                download_location_data();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void getPartnerTypeHelp() {


        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_customer_class.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PARTNER_TYPE_CLASS_HELP;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    list_customer_type.add(cursor.getString(cursor.getColumnIndex("partner_text")));
                    list_customer_class.add(cursor.getString(cursor.getColumnIndex("partner_class_text")));

                }

                hashSet.clear();
                hashSet.addAll(list_customer_type);
                list_customer_type.clear();
                list_customer_type.add("Select Customer Type");
                list_customer_type.addAll(hashSet);


                hashSet.clear();
                hashSet.addAll(list_customer_class);
                list_customer_class.clear();
                list_customer_class.add("Select Customer Class");
                list_customer_class.addAll(hashSet);


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database

        }


    }

    private boolean submitForm() {
        boolean validation;
        if (
                validateCustomerType() &&
                        validateCustomerName() &&
                        validateMobileNo() &&
                        validateContactPerson() &&
                        validateAddress() &&

                        validateCountry() &&
                        validateState() &&
                        validateDistrict() &&
                        validateTluka() &&
                        isValidEmail() &&
                        CustomUtility.isDateTimeAutoUpdate(this) &&
                        CustomUtility.CheckGPS(this)
        ) {
            validation = true;
        } else {
            validation = false;
        }
        return validation;
    }

    public boolean validateMobileNo() {
        if (edittext_mobile_no.getText().toString().trim().isEmpty()) {
            input_mobile_no.setError("Please Enter Mobile Number");

            requestFocus(edittext_mobile_no);
            return false;
        } else {
            input_mobile_no.setErrorEnabled(false);
        }


        int len = edittext_mobile_no.getText().toString().trim().length();
        if (len < 10) {
            input_mobile_no.setError("Please Enter Valid Mobile No");
            requestFocus(edittext_mobile_no);
            return false;
        }


        return true;
    }

    private boolean isValidEmail() {
        if (!TextUtils.isEmpty(email)) {
            input_email.setError("Please Enter Valid Mail address");
            requestFocus(edittext_email);
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return true;
    }

    public boolean validateContactPerson() {
        if (edittext_contact_person_name.getText().toString().trim().isEmpty()) {
            input_contact_person_name.setError("Please Enter Contact Person Name");

            requestFocus(edittext_contact_person_name);
            return false;
        } else {
            input_contact_person_name.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateAddress() {
        if (edittext_address.getText().toString().trim().isEmpty()) {
            input_address.setError("Please Enter Address");

            requestFocus(edittext_address);
            return false;
        } else {
            input_address.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateCustomerType() {
        if (index_customer_type == 0) {
            Toast.makeText(this, "Select Customer Type", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public boolean validateCountry() {
        if (index_country == 0) {
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public boolean validateState() {
        if (index_state == 0) {
            Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean validateDistrict() {
        if (index_district == 0) {
            Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean validateTluka() {
        if (index_taluka == 0) {
            Toast.makeText(this, "Select Taluka", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean validateCustomerName() {
        if (edittext_customer_name.getText().toString().trim().isEmpty()) {
            input_customer_name.setError("Please Enter Customer Name");

            requestFocus(edittext_customer_name);
            return false;
        } else {
            input_customer_name.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void saveNewAddedCustomer() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);

        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        String added_at_latlong = String.valueOf(latitude) + "," + String.valueOf(longitude);


        String country_code, state_code, district_code, taluka_code;
        String CurrentString = customer_type;
        String[] separated;
        separated = CurrentString.split("--");
        customer_type = separated[1];


        separated = country.split("--");
        country = separated[0];
        country_code = separated[1];

        separated = state.split("--");
        state = separated[0];
        state_code = separated[1];

        separated = district.split("--");
        district = separated[0];
        district_code = separated[1];

        separated = taluka.split("--");
        taluka = separated[0];
        taluka_code = separated[1];


        dataHelper.insertNewAddedCustomer(userid,

                route_code,
                route_name,
                customer_type,
                "07",

                edittext_customer_name.getText().toString().toUpperCase(),
                country_code,//jo.getString("land1"),
                country,//jo.getString("land_txt"),
                state_code,//jo.getString("state_code"),
                state,//jo.getString("state_txt"),
                district_code,//jo.getString("district_code"),
                district,//jo.getString("district_txt"),
                taluka_code,//jo.getString("taluka_code"),
                taluka,//jo.getString("taluka_txt"),
                edittext_address.getText().toString().toLowerCase(),
                edittext_email.getText().toString().toLowerCase(),
                edittext_mobile_no.getText().toString().toLowerCase(),
                edittext_landline.getText().toString().toLowerCase(), // tel phone no
                edittext_aadharcard.getText().toString().toLowerCase(), // aadhar card
                edittext_pancard.getText().toString().toLowerCase(), //pan card
                edittext_tinno.getText().toString().toLowerCase(), // tin no
                edittext_market.getText().toString().toLowerCase(), // market
                "",//edittext_dob.getText().toString().toLowerCase().trim(), // dob

                edittext_pincode.getText().toString().toLowerCase(),

                edittext_contact_person_name.getText().toString().toLowerCase(),
                edittext_contact_person_phone.getText().toString().toLowerCase(),
                "",//jo.getString("distributor_code"),
                primary_partner,//jo.getString("distributor_name")
                "HOT",
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                added_at_latlong,
                "1200",
                "02");


        //   new SyncDataToSAP().SyncNewAddedCustomerToSap(context); ;


        // Add New Customer In Route
        //
        //
        // new Capture_employee_gps_location(this,"6",edittext_mobile_no.getText().toString().toLowerCase());

        Toast.makeText(context, "New Service Center saved successfully", Toast.LENGTH_LONG).show();
        SyncNewAdddedCustomerInBackground();


        NewServiceCenterActivity.this.finish();
    }

    public void SyncNewAdddedCustomerInBackground() {

        Intent i = new Intent(NewServiceCenterActivity.this, SyncDataService.class);
        startService(i);

    }

    public void clearScreenNewAddedCustomer() {

        edittext_customer_name.setText("");
        edittext_mobile_no.setText("");
        edittext_landline.setText("");
        edittext_email.setText("");
        edittext_aadharcard.setText("");
        edittext_pancard.setText("");
        edittext_tinno.setText("");
        edittext_market.setText("");
        edittext_contact_person_name.setText("");
        edittext_contact_person_phone.setText("");
        edittext_address.setText("");
        edittext_pincode.setText("");
        // edittext_dob .setText("");

        country = "";
        state = "";
        district = "";
        taluka = "";
        customer_type = "";
        customer_class = "";
        primary_partner = "";
        interesting = "";

        spinner_customer_type.setSelection(0);

        spinner_country.setSelection(0);
        spinner_district.setSelection(0);
        spinner_taluka.setSelection(0);
        spinner_state.setSelection(0);
        spinner_taluka.setSelection(0);

    }

    public void getSearchHelp() {

        getCountry();


        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index_country = arg0.getSelectedItemPosition();
                country = spinner_country.getSelectedItem().toString();


                list_state.clear();


                if (index_country != 0) {
                    getState();
                }


/********************************* select district based on state *********************************/

                spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        index_state = arg0.getSelectedItemPosition();
                        state = spinner_state.getSelectedItem().toString();
                        list_district.clear();


                        if (index_state != 0) {
                            getCity();
                        }


/********************************* select taluka based on district *********************************/
                        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {
                                index_district = arg0.getSelectedItemPosition();
                                district = spinner_district.getSelectedItem().toString();

                                list_taluka.clear();


                                if (index_district != 0) {
                                    getTehsil();

                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }

                        });

/********************************* select taluka *********************************/
                        spinner_taluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {
                                index_taluka = arg0.getSelectedItemPosition();
                                taluka = spinner_taluka.getSelectedItem().toString();

//                                if (customer_type.equalsIgnoreCase("Retailer--R") || customer_type.equalsIgnoreCase("Secondary Customer--S")) {
//                                    getAreaDistributor();
//
//                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void getCountry() {


        {

            DatabaseHelper dataHelper = new DatabaseHelper(this);
            Cursor cursor = null;

            SQLiteDatabase db = dataHelper.getReadableDatabase();
            // Start the transaction.
            db.beginTransactionNonExclusive();
            try {
                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_COUNTRY;


                cursor = db.rawQuery(selectQuery, null);

                //   Log.d("comp",""+cursor.getCount());

                if (cursor.getCount() > 0) {
                    list_country.clear();


                    while (cursor.moveToNext()) {

                        list_country.add(cursor.getString(cursor.getColumnIndex("land_txt")) + "--" + cursor.getString(cursor.getColumnIndex("land1")));
                        //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));

                    }
                }

                hashSet.addAll(list_country);
                list_country.clear();
                list_country.add(" Select Country");
                list_country.addAll(hashSet);


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

    public void getState() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String[] separated;
            String country_code, country_name;
            separated = country.split("--");
            country_name = separated[0];
            country_code = separated[1];


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_STATE
                    + " WHERE " + DatabaseHelper.KEY_LAND1 + " = '" + country_code + "'";


            cursor = db.rawQuery(selectQuery, null);

            Log.d("cmp_country", "" + country);

            if (cursor.getCount() > 0) {
                Log.d("state", "" + cursor.getCount());
                list_state.clear();


                while (cursor.moveToNext()) {

                    list_state.add(cursor.getString(cursor.getColumnIndex("state_txt")) + "--" + cursor.getString(cursor.getColumnIndex("state_code")));

                    spinner_state.setAdapter(dataAdapter_state);
                }
            }


            hashSet.clear();
            hashSet.addAll(list_state);
            list_state.clear();
            list_state.add(" Select State");
            list_state.addAll(hashSet);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_material_analysis, menu);
        return true;
    }

    public void download_location_data() {

        progressDialog = ProgressDialog.show(NewServiceCenterActivity.this, "", "Download Location Data ...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(NewServiceCenterActivity.this)) {


                    try {

                        con.getLocationData(NewServiceCenterActivity.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


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

    public void getCity() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {


            String[] separated;
            String country_code, country_name, state_code, state_name;

            separated = country.split("--");
            country_name = separated[0];
            country_code = separated[1];

            separated = state.split("--");
            state_name = separated[0];
            state_code = separated[1];


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CITY
                    + " WHERE " + DatabaseHelper.KEY_LAND1 + " = '" + country_code + "'"
                    + " AND " + DatabaseHelper.KEY_STATE_CODE + " = '" + state_code + "'";


            cursor = db.rawQuery(selectQuery, null);

            //   Log.d("comp",""+cursor.getCount());

            if (cursor.getCount() > 0) {
                //  Log.d("state",""+cursor.getCount());
                list_district.clear();


                while (cursor.moveToNext()) {

                    list_district.add(cursor.getString(cursor.getColumnIndex("district_txt")) + "--" + cursor.getString(cursor.getColumnIndex("district_code")));

                    spinner_district.setAdapter(dataAdapter_district);
                }
            }

            hashSet.clear();
            hashSet.addAll(list_district);
            list_district.clear();
            list_district.add(" Select District ");
            list_district.addAll(hashSet);

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

    public void getTehsil() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {

            String[] separated;
            String country_code, country_name, state_code, state_name, district_name, district_code;

            separated = country.split("--");
            country_name = separated[0];
            country_code = separated[1];

            separated = state.split("--");
            state_name = separated[0];
            state_code = separated[1];


            separated = district.split("--");
            district_name = separated[0];
            district_code = separated[1];


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_TEHSIL

                    + " WHERE " + DatabaseHelper.KEY_LAND1 + " = '" + country_code + "'"
                    + " AND " + DatabaseHelper.KEY_STATE_CODE + " = '" + state_code + "'"
                    + " AND " + DatabaseHelper.KEY_DISTRICT_CODE + " = '" + district_code + "'";


            cursor = db.rawQuery(selectQuery, null);

            //   Log.d("comp",""+cursor.getCount());

            if (cursor.getCount() > 0) {
                //  Log.d("state",""+cursor.getCount());
                list_taluka.clear();


                while (cursor.moveToNext()) {

                    list_taluka.add(cursor.getString(cursor.getColumnIndex("taluka_txt")) + "--" + cursor.getString(cursor.getColumnIndex("taluka_code")));

                    spinner_taluka.setAdapter(dataAdapter_taluka);
                }
            }

            hashSet.clear();
            hashSet.addAll(list_taluka);
            list_taluka.clear();
            list_taluka.add(" Select Tehsil ");
            list_taluka.addAll(hashSet);

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

