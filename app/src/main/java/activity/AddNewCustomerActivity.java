package activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import android.os.StrictMode;
import android.text.TextUtils;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import backgroundservice.SyncDataService;
import bean.CustomerDetailBean;
import bean.LoginBean;
import bean.NewAddedCustomerBean;
import bean.RouteDetailBean;
import bean.RouteHeaderBean;
import database.DatabaseHelper;
import searchlist.CustomerSearch;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class AddNewCustomerActivity extends AppCompatActivity {
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
            vtweg,
            ktokd,
            email;
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
            input_pincode;
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
    Spinner spinner_customer_type,
            spinner_customer_class,
            spinner_country,
            spinner_state,
            spinner_district,
            spinner_taluka,
            spinner_primary_partner,
            spinner_interesting;
    //       input_dob;
    Button btn_customer_save;
    //   edittext_dob;
    List<String> list_customer_type = null,
            list_customer_class = null,
            list_country = null,
            list_state = null,
            list_district = null,
            list_taluka = null,
            list_primary_partner = null,
            list_interesting = null;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(AddNewCustomerActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    String sync_data_name = null;
    String sync_data_value = null;
    String sync_key_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        context = this;

        AddNewCustomerActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        // edittext_dob                  = (EditText)findViewById(R.id.et_dob);


        email = edittext_email.getText().toString().toLowerCase().trim();


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
        getSupportActionBar().setTitle("Add New Customer In Route");

        route_name = RouteHeaderBean.getRoute_name();
        route_code = RouteHeaderBean.getRoute_code();
        vkorg = RouteHeaderBean.getVkorg();
        vtweg = RouteHeaderBean.getVtweg();
        ktokd = RouteHeaderBean.getKtokd();
        Log.d("route_header", "" + route_name + "--" + route_code + "--" + vkorg + "--" + vtweg + "--" + ktokd);


        spinner_customer_type.setPrompt("Customer Type");
        spinner_customer_class.setPrompt("Customer Class");
        spinner_country.setPrompt("Country");
        spinner_state.setPrompt("State");
        spinner_district.setPrompt("District");
        spinner_taluka.setPrompt("Taluka");
        spinner_primary_partner.setPrompt("Distributor");
        spinner_interesting.setPrompt("Interesting");


        getRouteDetail();
        getPartnerTypeHelp();
        ArrayAdapter<String> dataAdapter_customer_type = new ArrayAdapter<String>(this, R.layout.spinner_item, list_customer_type);
        dataAdapter_customer_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_customer_type.setAdapter(dataAdapter_customer_type);
        dataAdapter_customer_class = new ArrayAdapter<String>(this, R.layout.spinner_item, list_customer_class);
        dataAdapter_customer_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapter_country = new ArrayAdapter<String>(this, R.layout.spinner_item, list_country);
        dataAdapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(dataAdapter_country);
        dataAdapter_state = new ArrayAdapter<String>(this, R.layout.spinner_item, list_state);
        dataAdapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_district = new ArrayAdapter<String>(this, R.layout.spinner_item, list_district);
        dataAdapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_taluka = new ArrayAdapter<String>(this, R.layout.spinner_item, list_taluka);
        dataAdapter_taluka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_primary_partner = new ArrayAdapter<String>(this, R.layout.spinner_item, list_primary_partner);
        dataAdapter_primary_partner.setDropDownViewResource(R.layout.spinner_layout);
        dataAdapter_interesting = new ArrayAdapter<String>(this, R.layout.spinner_item, list_interesting);
        dataAdapter_interesting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_interesting.setAdapter(dataAdapter_interesting);

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
                            clearScreenNewAddedCustomer();
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
        spinner_customer_class.setSelection(0);
        spinner_country.setSelection(0);
        spinner_district.setSelection(0);
        spinner_taluka.setSelection(0);
        spinner_state.setSelection(0);
        spinner_taluka.setSelection(0);
        spinner_primary_partner.setSelection(0);
        spinner_interesting.setSelection(0);


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
                        validateIntresting() &&
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
        if ((customer_type.equalsIgnoreCase("Retailer--R") || customer_type.equalsIgnoreCase("Secondary Customer--S")) &&
                index_primary_partner == 0) {

            if ((customer_type.equalsIgnoreCase("Retailer--R"))) {
                Toast.makeText(this, "Select Distributor", Toast.LENGTH_SHORT).show();
            }

            if ((customer_type.equalsIgnoreCase("Secondary Customer--S"))) {
                Toast.makeText(this, "Primary Customer", Toast.LENGTH_SHORT).show();
            }


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
        //db.beginTransactionNonExclusive();();
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
            // Close database

        }


    }

    public void getPartnerClassHelp() {


        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_customer_class.clear();
        // Start the transaction.
        //db.beginTransactionNonExclusive();();
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
            // Close database

        }


    }

    public void getAreaDistributor() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        list_primary_partner.clear();
        // Start the transaction.
        //db.beginTransactionNonExclusive();();
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = null;

            // Log.d("bean",""+vkorg+"--"+vtweg);


            if (vkorg.equals("1200") && vtweg.equals("11")) {
//            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_AREA_DISTRIBUTOR +
//                    " where  taluka_txt = " + "'" + taluka + "'" ;
//

                selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_AREA_DISTRIBUTOR;


            } else {
                selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_AREA_DISTRIBUTOR
                        + " where  state_txt = " + "'" + state + "'";
            }


            Cursor cursor = db.rawQuery(selectQuery, null);


            // Log.d("bean1",""+cursor.getCount()+"--"+vtweg+"--"+taluka);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    list_primary_partner.add(cursor.getString(cursor.getColumnIndex("distributor_name")) +
                            "--" + cursor.getString(cursor.getColumnIndex("distributor_code")));


                }


                spinner_primary_partner.setVisibility(View.VISIBLE);
                spinner_primary_partner.setAdapter(dataAdapter_primary_partner);

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
                    //input_dob.setEnabled(true);
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


                if (customer_type.equalsIgnoreCase("Retailer--R") || customer_type.equalsIgnoreCase("Secondary Customer--S")) {

                    spinner_primary_partner.setVisibility(View.VISIBLE);
                    getAreaDistributor();
                } else {
                    primary_partner = "";
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

                list_country.add(routeDetail.get(i).getLand_txt() + "--" + routeDetail.get(i).getLand1());

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
                        if (country.equalsIgnoreCase(routeDetail.get(i).getLand_txt() + "--" + routeDetail.get(i).getLand1())) {
                            list_state.add(routeDetail.get(i).getState_txt() + "--" + routeDetail.get(i).getState_code());
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
                                if (country.equalsIgnoreCase(routeDetail.get(i).getLand_txt() + "--" + routeDetail.get(i).getLand1()) &&
                                        state.equalsIgnoreCase(routeDetail.get(i).getState_txt() + "--" + routeDetail.get(i).getState_code())) {
                                    list_district.add(routeDetail.get(i).getDistrict_txt() + "--" + routeDetail.get(i).getDistrict_code());
                                    spinner_district.setVisibility(View.VISIBLE);
                                    spinner_district.setAdapter(dataAdapter_district);
                                    if (customer_type.equalsIgnoreCase("Retailer--R") || customer_type.equalsIgnoreCase("Secondary Customer--S")) {
                                        getAreaDistributor();
                                    }
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

                                        if ((country.equalsIgnoreCase(routeDetail.get(i).getLand_txt() + "--" + routeDetail.get(i).getLand1())) &&
                                                (state.equalsIgnoreCase(routeDetail.get(i).getState_txt() + "--" + routeDetail.get(i).getState_code())) &&
                                                (district.equalsIgnoreCase(routeDetail.get(i).getDistrict_txt() + "--" + routeDetail.get(i).getDistrict_code()))) {
                                            list_taluka.add(routeDetail.get(i).getTaluka_txt() + "--" + routeDetail.get(i).getTaluka_code());
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

                                    if (customer_type.equalsIgnoreCase("Retailer--R") || customer_type.equalsIgnoreCase("Secondary Customer--S")) {
                                        getAreaDistributor();
                                    }
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
                    interesting = spinner_interesting.getSelectedItem().toString();
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

        separated = customer_class.split("--");

        customer_class = separated[1];

        Log.d("blank_save", "" + route_name + "" + route_code);
        dataHelper.insertNewAddedCustomer(userid,
                route_code,
                route_name,
                customer_type,
                customer_class,
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
                interesting,
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                added_at_latlong,
                vkorg,
                vtweg);
//        Toast.makeText(context, "New Customer saved successfully", Toast.LENGTH_LONG).show();
//        SyncNewAdddedCustomerInBackground();
        if (CustomUtility.isOnline(context)) {
            SyncNewAddedCustomerOnServer();
        } else {
            Toast.makeText(context, "No internet connection, data saved in DB", Toast.LENGTH_LONG).show();
//            SyncNewAdddedCustomerInBackground();
        }
    }

    private void SyncNewAddedCustomerOnServer() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        DatabaseHelper db = new DatabaseHelper(context);
        int keyId = db.getLatestNewAddedCustomerKeyID();
        NewAddedCustomerBean newAddedCustomerBeen = db.getLatestNewAddedCustomer(keyId);

        progressDialog = ProgressDialog.show(context, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                    if (null != newAddedCustomerBeen) {
                        if (CustomUtility.isOnline(context)) {
                            JSONArray ja_invc_data = new JSONArray();
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("key_id", newAddedCustomerBeen.getKey_id());
                                jsonObj.put("pernr", newAddedCustomerBeen.getPernr());
                                jsonObj.put("start_date", newAddedCustomerBeen.getBudat());
                                jsonObj.put("route_code", newAddedCustomerBeen.getRoute_code());
                                jsonObj.put("route_name", newAddedCustomerBeen.getRoute_name());

                                jsonObj.put("partner", newAddedCustomerBeen.getPartner());
                                jsonObj.put("partner_class", newAddedCustomerBeen.getPartner_class());
                                jsonObj.put("latitude", newAddedCustomerBeen.getLatitude());
                                jsonObj.put("longitude", newAddedCustomerBeen.getLongitude());
                                jsonObj.put("partner_name", newAddedCustomerBeen.getPartner_name());

                                jsonObj.put("land1", newAddedCustomerBeen.getLand1());
                                jsonObj.put("land1_txt", newAddedCustomerBeen.getLand_txt());
                                jsonObj.put("state_code", newAddedCustomerBeen.getState_code());
                                jsonObj.put("state_txt", newAddedCustomerBeen.getState_txt());
                                jsonObj.put("district_code", newAddedCustomerBeen.getDistrict_code());
                                jsonObj.put("district_txt", newAddedCustomerBeen.getDistrict_txt());
                                jsonObj.put("taluka_code", newAddedCustomerBeen.getTaluka_code());
                                jsonObj.put("taluka_txt", newAddedCustomerBeen.getTaluka_txt());

                                jsonObj.put("address", newAddedCustomerBeen.getAddress());
                                jsonObj.put("email", newAddedCustomerBeen.getEmail());
                                jsonObj.put("mobile", newAddedCustomerBeen.getMob_no());
                                jsonObj.put("tel_number", newAddedCustomerBeen.getTel_number());
                                jsonObj.put("pincode", newAddedCustomerBeen.getPincode());
                                jsonObj.put("contact_person", newAddedCustomerBeen.getContact_person());
                                jsonObj.put("contact_person_phone", newAddedCustomerBeen.getContact_person_phone());

                                jsonObj.put("distributor_code", newAddedCustomerBeen.getDistributor_code());
                                jsonObj.put("distributor_name", newAddedCustomerBeen.getDistributor_name());
                                jsonObj.put("phone_number", newAddedCustomerBeen.getPhone_number());
                                jsonObj.put("panno", newAddedCustomerBeen.getPan_card());
                                jsonObj.put("aadharno", newAddedCustomerBeen.getAadhar_card());
                                jsonObj.put("tin_no", newAddedCustomerBeen.getTin_no());
                                jsonObj.put("market_plance", newAddedCustomerBeen.getMarket_place());
                                jsonObj.put("dob", newAddedCustomerBeen.getDob());
                                jsonObj.put("interested", newAddedCustomerBeen.getIntrested());
                                jsonObj.put("time", newAddedCustomerBeen.getTime());
                                jsonObj.put("add_latlong", newAddedCustomerBeen.getAdded_at_latlong());
                                ja_invc_data.put(jsonObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
                            param1_invc.add(new BasicNameValuePair("NEW_ADDED_CUSTOEMR", String.valueOf(ja_invc_data)));
                            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1_invc);
                            if (!obj2.equalsIgnoreCase("")) {
                                JSONObject jo_success = new JSONObject(obj2);
                                JSONArray ja_success = jo_success.getJSONArray("data_success");
                                for (int i = 0; i < ja_success.length(); i++) {
                                    JSONObject jo = ja_success.getJSONObject(i);
                                    sync_data_name = jo.getString("sync_data");
                                    sync_key_id = jo.getString("key_id");
                                    if (sync_data_name.equalsIgnoreCase("NEW_ADDED_CUSTOMER")) {
                                        db.updateUnsyncData(DatabaseHelper.TABLE_NEW_ADDED_CUSTOMER, sync_key_id);
                                    } else {
                                        db.deleteAlreadyExistNewAddedCustomer(sync_key_id);
                                    }
                                    Message msg = new Message();
                                    msg.obj = sync_data_name;
                                    mHandler.sendMessage(msg);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Message msg = new Message();
                            msg.obj = "No internet connection, data saved in DB";
                            mHandler.sendMessage(msg);
                        }
                    }
                    progressDialog.dismiss();
                    /*runOnUiThread(() -> {
                        getRouteDetailData();
                    });*/
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    private void getRouteDetailData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        DatabaseHelper dataHelper = new DatabaseHelper(context);

        progressDialog = ProgressDialog.show(context, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {
                        if (CustomUtility.isOnline(context)) {
                            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                            param.add(new BasicNameValuePair("PERNR", userid));
                            String obj = CustomHttpClient.executeHttpPost1(WebURL.ROUTE_DETAIL, param);
                            if (obj != null) {
                                JSONObject jsonObj = new JSONObject(obj);
                                JSONArray ja = jsonObj.getJSONArray("route_detail");
                                dataHelper.deleteRouteDetail();
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);
                                    dataHelper.insertRouteDetail(userid,
                                            jo.getString("doc_date"),
                                            jo.getString("route_code"),
                                            jo.getString("route_name"),
                                            jo.getString("kunnr"),
                                            jo.getString("partner"),
                                            jo.getString("partner_class"),
                                            jo.getString("latitude"),
                                            jo.getString("longitude"),
                                            jo.getString("partner_name"),
                                            jo.getString("land1"),
                                            jo.getString("land_txt"),
                                            jo.getString("state_code"),
                                            jo.getString("state_txt"),
                                            jo.getString("district_code"),
                                            jo.getString("district_txt"),
                                            jo.getString("taluka_code"),
                                            jo.getString("taluka_txt"),
                                            jo.getString("address"),
                                            jo.getString("email"),
                                            jo.getString("mob_no"),
                                            jo.getString("tel_number"),
                                            jo.getString("pincode"),
                                            jo.getString("contact_person"),
                                            jo.getString("distributor_code"),
                                            jo.getString("distributor_name"),
                                            jo.getString("phone_number"),
                                            jo.getString("vkorg"),
                                            jo.getString("vtweg"),
                                            jo.getString("interested"),
                                            jo.getString("ktokd")
                                    );
                                }
                                /*********************************** parsing partner type & class json ************************/
                                JSONArray partner_type_class = jsonObj.getJSONArray("partner_type_class");
                                dataHelper.deletePartnerTypeClassHelp();
                                for (int i = 0; i < partner_type_class.length(); i++) {
                                    JSONObject jo = partner_type_class.getJSONObject(i);
                                    dataHelper.insertPartnerTypeClassHelp(
                                            jo.getString("partner"),
                                            jo.getString("partner_txt"),
                                            jo.getString("partner_class"),
                                            jo.getString("partner_class_txt"));
                                }
/***********************************   parsing area distributor j son *************************/
                                JSONArray area_distributor = jsonObj.getJSONArray("area_distributor");
                                dataHelper.deleteAreaDistributor();
                                for (int i = 0; i < area_distributor.length(); i++) {
                                    JSONObject jo = area_distributor.getJSONObject(i);
                                    dataHelper.insertAreaDistributor(
                                            jo.getString("distributor_code"),
                                            jo.getString("distributor_name"),
                                            jo.getString("land_txt"),
                                            jo.getString("state_txt"),
                                            jo.getString("district_txt"),
                                            jo.getString("taluka_txt")
                                    );
                                    //Log.d("bean2",""+jo.getString("distributor_name")+"--"+jo.getString("state_txt"));
                                }
/***********************************   parsing adhoc order customer json *************************/
                                JSONArray adhoc_order_customer = jsonObj.getJSONArray("adhoc_order_customer");
                                //   Log.d("adhoc_order_customer",""+adhoc_order_customer  ) ;
                                dataHelper.deleteAdhocOrderCustomer();
                                for (int i = 0; i < adhoc_order_customer.length(); i++) {
                                    JSONObject jo = adhoc_order_customer.getJSONObject(i);
                                    dataHelper.insertAdhocOrderCustomer(
                                            jo.getString("pernr"),
                                            jo.getString("phone_number"),
                                            jo.getString("partner_name"),
                                            jo.getString("partner_class"),
                                            jo.getString("partner_type"),
                                            jo.getString("country"),
                                            jo.getString("district")
                                    );
                                }
                                /*********************************** parsing partner view survey  ************************/
                                JSONArray view_survey = jsonObj.getJSONArray("view_survey");
                                dataHelper.deleteSurveyView();
                                for (int i = 0; i < view_survey.length(); i++) {
                                    JSONObject jo = view_survey.getJSONObject(i);
                                    dataHelper.insertViewSurvey(
                                            jo.getString("ename"),
                                            jo.getString("erdat"),
                                            jo.getString("remark"),
                                            jo.getString("outer_url"),
                                            jo.getString("inner_url"),
                                            jo.getString("other_url"),
                                            jo.getString("owner_url"),
                                            jo.getString("card_url"),
                                            jo.getString("phone_number"),
                                            "sap"
                                    );
                                }
                            }
                        }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    public void SyncNewAdddedCustomerInBackground() {
        Intent i = new Intent(AddNewCustomerActivity.this, SyncDataService.class);
        startService(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
