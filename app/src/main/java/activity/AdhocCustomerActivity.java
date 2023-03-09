package activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.CustomerDetailBean;
import bean.LoginBean;
import bean.RouteDetailBean;
import bean.RouteHeaderBean;
import database.DatabaseHelper;
import searchlist.CustomerSearch;

public class AdhocCustomerActivity extends AppCompatActivity {
    Intent intent = null;
    HashSet<String> hashSet = null;
    CustomerDetailBean customerdetailbean = null;
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
            vtweg;
    ArrayList<CustomerSearch> routeDetail;
    Context context;
    ArrayAdapter<String> dataAdapter_district;
    ArrayAdapter<String> dataAdapter_taluka;
    ArrayAdapter<String> dataAdapter_state;
    ArrayAdapter<String> dataAdapter_primary_partner;
    ArrayAdapter<String> dataAdapter_interesting;
    ArrayAdapter<String> dataAdapter_customer_class;
    int index,
            index_customer_type,
            index_country,
            index_state,
            index_district,
            index_taluka,
            index_customer_class,
            index_primary_partner,
            index_interesting;
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
            input_pincode,
            input_dob;
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
            edittext_pincode,
            edittext_dob;
    Spinner spinner_customer_type,
            spinner_customer_class,
            spinner_country,
            spinner_state,
            spinner_district,
            spinner_taluka,
            spinner_primary_partner,
            spinner_interesting;
    Button btn_customer_save;
    List<String> list_customer_type = null,
            list_customer_class = null,
            list_country = null,
            list_state = null,
            list_district = null,
            list_taluka = null,
            list_primary_partner = null,
            list_interesting = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        hashSet = new HashSet<String>();
        userid = LoginBean.getUseid();
        list_customer_type = new ArrayList<String>();
        list_customer_class = new ArrayList<String>();
        list_country = new ArrayList<String>();
        list_state = new ArrayList<String>();
        list_district = new ArrayList<String>();
        list_taluka = new ArrayList<String>();
        list_primary_partner = new ArrayList<String>();
        list_interesting = new ArrayList<String>();



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
        // input_dob          =  (TextInputLayout)findViewById(R.id.input_dob);


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
        //    edittext_dob                  = (EditText)findViewById(R.id.et_dob);


        spinner_customer_type = (Spinner) findViewById(R.id.spinner_customer_type);
        spinner_customer_class = (Spinner) findViewById(R.id.spinner_customer_class);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        spinner_taluka = (Spinner) findViewById(R.id.spinner_taluka);
        spinner_primary_partner = (Spinner) findViewById(R.id.spinner_primary_partner);
        spinner_interesting = (Spinner) findViewById(R.id.spinner_interesting);
        btn_customer_save = (Button) findViewById(R.id.btn_customer_save);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Adhoc Customer");


//        Intent i = getIntent();
//        route_name = i.getStringExtra("route_name");
//        route_code = i.getStringExtra("route_code");


        spinner_customer_type.setPrompt("Customer Type");
        spinner_customer_class.setPrompt("Customer Class");
        spinner_country.setPrompt("Country");
        spinner_state.setPrompt("State");
        spinner_district.setPrompt("District");
        spinner_taluka.setPrompt("Taluka");
        spinner_primary_partner.setPrompt("Distributor");
        spinner_interesting.setPrompt("Interesting");

        route_name = RouteHeaderBean.getRoute_name();
        route_code = RouteHeaderBean.getRoute_code();
        vkorg = RouteHeaderBean.getVkorg();
        vtweg = RouteHeaderBean.getVtweg();


        // getRouteDetail();
        getPartnerTypeHelp();

        getAreaDistributor();

        // Creating adapter for spinner

        ArrayAdapter<String> dataAdapter_customer_type = new ArrayAdapter<String>(this, R.layout.spinner_item, list_customer_type);

        // Drop down layout style - list view with radio button
        dataAdapter_customer_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_customer_type.setAdapter(dataAdapter_customer_type);


        dataAdapter_customer_class = new ArrayAdapter<String>(this, R.layout.spinner_item, list_customer_class);
        dataAdapter_customer_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner_customer_class.setAdapter(dataAdapter_customer_class);


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


        dataAdapter_primary_partner = new ArrayAdapter<String>(this, R.layout.spinner_item, list_primary_partner);
        dataAdapter_taluka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_primary_partner.setAdapter(dataAdapter_primary_partner);


        dataAdapter_interesting = new ArrayAdapter<String>(this, R.layout.spinner_item, list_interesting);
        dataAdapter_interesting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_interesting.setAdapter(dataAdapter_interesting);


        btn_customer_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (submitForm()) {
                    saveNewAddedCustomer();

                    clearScreenNewAddedCustomer();
                }
            }
        });


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
        edittext_dob.setText("");

        country = "";
        state = "";
        district = "";
        taluka = "";
        customer_type = "";
        customer_class = "";
        primary_partner = "";
        interesting = "";

    }


    private boolean submitForm() {
        boolean validation;
        if (
                validateCustomerType() &&
                        validateCustomerClass() &&

                        validateCustomerName() &&
                        validateMobileNo() &&
                        validateContactPerson() &&
                        validateAddress() &&

                        validateCountry() &&
                        validateState() &&
                        validateDistrict() &&
                        validateTluka() &&
                        validatePrimarypartner() &&
                        validateIntresting()
        ) {
            validation = true;
        } else {
            validation = false;
        }
        return validation;
    }


    public boolean validateCustomerType() {
        if (index_customer_type == 0) {
            Toast.makeText(this, "Select Customer Type", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_customer_type);
            return false;
        }

        return true;
    }

    public boolean validateCustomerClass() {
        if (index_customer_class == 0) {
            Toast.makeText(this, "Select Customer Class", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_customer_class);
            return false;
        }

        return true;
    }

    public boolean validateCountry() {
        if (index_country == 0) {
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_country);
            return false;
        }

        return true;
    }


    public boolean validateState() {
        if (index_state == 0) {
            Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_state);
            return false;
        }

        return true;
    }

    public boolean validateDistrict() {
        if (index_district == 0) {
            Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_district);
            return false;
        }

        return true;
    }

    public boolean validateTluka() {
        if (index_taluka == 0) {
            Toast.makeText(this, "Select Taluka", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_taluka);
            return false;
        }

        return true;
    }

    public boolean validatePrimarypartner() {
        // if ( index_primary_partner == 0) {
        if (customer_type.equalsIgnoreCase("Retailer") &&
                index_primary_partner == 0) {
            Toast.makeText(this, "Select Distributor", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_primary_partner);
            return false;
        }


        return true;
    }


    public boolean validateIntresting() {
        if (index_interesting == 0) {
            Toast.makeText(this, "Select Intresting", Toast.LENGTH_SHORT).show();
            requestFocus(spinner_interesting);
            return false;
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


    public boolean validateMobileNo() {
        if (edittext_mobile_no.getText().toString().trim().isEmpty()) {
            input_mobile_no.setError("Please Enter Mobile Number");

            requestFocus(edittext_mobile_no);
            return false;
        } else {
            input_mobile_no.setErrorEnabled(false);
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
            if (db != null) {
                db.endTransaction();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }
        }


    }


    public void getPartnerClassHelp() {


        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_customer_class.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PARTNER_TYPE_CLASS_HELP
                    + " WHERE " + DatabaseHelper.KEY_PARTNER_TEXT + " = '" + customer_type + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {
                    list_customer_class.add(cursor.getString(cursor.getColumnIndex("partner_class_text")));
                }

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
            if (db != null) {
                db.endTransaction();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }

        }


    }


    public void getAreaDistributor() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_primary_partner.clear();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_AREA_DISTRIBUTOR;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    list_primary_partner.add(cursor.getString(cursor.getColumnIndex("distributor_name")));

                }

                hashSet.clear();
                hashSet.addAll(list_primary_partner);
                list_primary_partner.clear();
                list_primary_partner.add("Select Distributor");
                list_primary_partner.addAll(hashSet);


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
            }
            // End the transaction.
            if (db != null) {
                db.close();
            }

        }

    }


    public void getRouteDetail() {


        list_interesting.clear();
        list_interesting.add("Interesting :");
        list_interesting.add("HOT");
        list_interesting.add("COLD");


        spinner_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index_customer_type = arg0.getSelectedItemPosition();

                customer_type = spinner_customer_type.getSelectedItem().toString();
                // Toast.makeText(AddNewCustomerActivity.this,index,Toast.LENGTH_LONG).show();
                if (index_customer_type != 0) {


                    getPartnerClassHelp();


                    input_customer_name.setEnabled(true);
                    input_mobile_no.setEnabled(true);
                    input_landline.setEnabled(true);
                    input_email.setEnabled(true);
                    input_aadharcard.setEnabled(true);
                    input_pancard.setEnabled(true);
                    input_tinno.setEnabled(true);
                    input_market.setEnabled(true);
                    input_contact_person_name.setEnabled(true);
                    input_contact_person_phone.setEnabled(true);
                    input_dob.setEnabled(true);
                    input_address.setEnabled(true);
                    input_pincode.setEnabled(true);

                    spinner_interesting.setEnabled(true);
                    spinner_country.setEnabled(true);


                    spinner_customer_class.setAdapter(dataAdapter_customer_class);
                    spinner_customer_class.setVisibility(View.VISIBLE);


                } else {
                    spinner_interesting.setEnabled(false);
                    spinner_country.setEnabled(false);
                    spinner_customer_class.setVisibility(View.INVISIBLE);
                }


                if (customer_type.equalsIgnoreCase("Retailer")) {

                    spinner_primary_partner.setVisibility(View.VISIBLE);
                } else {
                    spinner_primary_partner.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner_customer_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index_customer_class = arg0.getSelectedItemPosition();

                customer_class = spinner_customer_class.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


//   set country , state , distrinct , taluka help

        RouteDetailBean routeDetailBean = new RouteDetailBean();
        routeDetail = new ArrayList<CustomerSearch>();


        routeDetail = routeDetailBean.getList_routeDetail();


        if (routeDetail.size() > 0) {
            list_country.clear();
            for (int i = 0; i < routeDetail.size(); i++) {

                list_country.add(routeDetail.get(i).getLand_txt());

            }

            hashSet.addAll(list_country);
            list_country.clear();
            list_country.add(" Select Country");
            list_country.addAll(hashSet);


/********************************* select state based on country *********************************/

            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    index_country = arg0.getSelectedItemPosition();
                    country = spinner_country.getSelectedItem().toString();


                    list_state.clear();

                    for (int i = 0; i < routeDetail.size(); i++) {
                        if (country.equalsIgnoreCase(routeDetail.get(i).getLand_txt())) {
                            list_state.add(routeDetail.get(i).getState_txt());
                            spinner_state.setVisibility(View.VISIBLE);
                            spinner_state.setAdapter(dataAdapter_state);
                        }
                    }

                    hashSet.clear();
                    hashSet.addAll(list_state);
                    list_state.clear();
                    list_state.add(" Select State");
                    list_state.addAll(hashSet);


/********************************* select district based on state *********************************/

                    spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            index_state = arg0.getSelectedItemPosition();
                            state = spinner_state.getSelectedItem().toString();
                            list_district.clear();
                            for (int i = 0; i < routeDetail.size(); i++) {
                                if (country.equalsIgnoreCase(routeDetail.get(i).getLand_txt()) &&
                                        state.equalsIgnoreCase(routeDetail.get(i).getState_txt())) {
                                    list_district.add(routeDetail.get(i).getDistrict_txt());
                                    spinner_district.setVisibility(View.VISIBLE);
                                    spinner_district.setAdapter(dataAdapter_district);
                                }


                            }

                            hashSet.clear();
                            hashSet.addAll(list_district);
                            list_district.clear();
                            list_district.add(" Select District");
                            list_district.addAll(hashSet);


/********************************* select taluka based on district *********************************/
                            spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                           int arg2, long arg3) {
                                    index_district = arg0.getSelectedItemPosition();
                                    district = spinner_district.getSelectedItem().toString();

                                    list_taluka.clear();

                                    for (int i = 0; i < routeDetail.size(); i++) {

                                        if ((country.equalsIgnoreCase(routeDetail.get(i).getLand_txt())) &&
                                                (state.equalsIgnoreCase(routeDetail.get(i).getState_txt())) &&
                                                (district.equalsIgnoreCase(routeDetail.get(i).getDistrict_txt()))) {
                                            list_taluka.add(routeDetail.get(i).getTaluka_txt());
                                            spinner_taluka.setVisibility(View.VISIBLE);
                                            spinner_taluka.setAdapter(dataAdapter_taluka);

                                        }
                                    }

                                    hashSet.clear();
                                    hashSet.addAll(list_taluka);
                                    list_taluka.clear();
                                    list_taluka.add(" Select Taluka");
                                    list_taluka.addAll(hashSet);

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


            spinner_primary_partner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    index_primary_partner = arg0.getSelectedItemPosition();

                    primary_partner = spinner_primary_partner.getSelectedItem().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


            spinner_interesting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    index_interesting = arg0.getSelectedItemPosition();

                    interesting = spinner_primary_partner.getSelectedItem().toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


        }
    }


    public void saveNewAddedCustomer() {
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        GPSTracker gps = new GPSTracker(this);
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        String added_at_latlong = String.valueOf(latitude) + "," + String.valueOf(longitude);


        Log.d("new_customer_route", "" + route_code);
        dataHelper.insertNewAddedCustomer(userid,

                route_code,
                route_name,
                "",//jo.getString("partner"),
                "",//jo.getString("partner_class"),

                edittext_customer_name.getText().toString().toLowerCase().trim(),
                "",//jo.getString("land1"),
                "",//jo.getString("land_txt"),
                "",//jo.getString("state_code"),
                "",//jo.getString("state_txt"),
                "",//jo.getString("district_code"),
                "",//jo.getString("district_txt"),
                "",//jo.getString("taluka_code"),
                "",//jo.getString("taluka_txt"),
                edittext_address.getText().toString().toLowerCase().trim(),
                edittext_email.getText().toString().toLowerCase().trim(),
                edittext_mobile_no.getText().toString().toLowerCase().trim(),
                edittext_landline.getText().toString().toLowerCase().trim(), // tel phone no
                edittext_aadharcard.getText().toString().toLowerCase().trim(), // aadhar card
                edittext_pancard.getText().toString().toLowerCase().trim(), //pan card
                edittext_tinno.getText().toString().toLowerCase().trim(), // tin no
                edittext_market.getText().toString().toLowerCase().trim(), // market
                edittext_dob.getText().toString().toLowerCase().trim(), // dob

                edittext_pincode.getText().toString().toLowerCase().trim(),

                edittext_contact_person_name.getText().toString().toLowerCase().trim(),
                edittext_contact_person_phone.getText().toString().toLowerCase().trim(),
                "",//jo.getString("distributor_code"),
                "",
                interesting,
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                added_at_latlong,
                vkorg,
                vtweg);


        //   new SyncDataToSAP().SyncNewAddedCustomerToSap(context); ;

        SyncNewAdddedCustomerInBackground();

    }


    public void SyncNewAdddedCustomerInBackground() {

        if (CustomUtility.isOnline(this)) {

            Intent i = new Intent(AdhocCustomerActivity.this, SyncDataService.class);
            i.putExtra("sync_data", "sync_new_customer_entry");
            startService(i);
            Toast.makeText(getApplicationContext(), "New Customer Sync Successfull", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "No internet Connection .New Customer save offline", Toast.LENGTH_SHORT).show();
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
//                AddNewCustomerActivity.this.finish();
//                intent = new Intent(getApplicationContext(), RouteActivity.class);
//                // sending data to new activity
//                startActivity(intent);
//                this.finish();
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onBackPressed() {
////        if (!mFragments.getSupportFragmentManager().popBackStackImmediate()) {
////            super.onBackPressed();
////        }
//
//      // super.onBackPressed();
//       AddNewCustomerActivity.this.finish();
//        intent = new Intent(getApplicationContext(), RouteActivity.class);
//        // sending data to new activity
//        startActivity(intent);
//
//    }


}
