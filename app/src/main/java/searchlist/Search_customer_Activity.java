package searchlist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import activity.AddNewCustomerActivity;
import activity.AddNewCustomer_Export_Activity;
import activity.CustomUtility;
import activity.GPSTracker;
import activity.MapsActivity;
import bean.RouteDetailBean;
import bean.RouteHeaderBean;
import database.DatabaseHelper;

public class Search_customer_Activity extends AppCompatActivity {
    CustomUtility customutility = null;
    HashSet<CustomerSearch> hashSet = null;
    String route_name, total_distance,
            customer_number = "",
            customer_name,
            partner,
            district_code,
            district_txt,
            route_code,
            partner_class,
            land1,
            land_txt,
            state_code,
            state_txt,
            taluka_code,
            taluka_txt,
            address,
            email,
            mob_no,
            tel_number,
            pincode,
            contact_person,
            distributor_code,
            distributor_name,
            phone_number,
            latitude,
            longitude,
            vkorg,
            vtweg,
            ktokd,
            customer_category = " ";
    boolean counter = false;
    double employee_latitude, employee_longitude, customer_latitude, customer_longitude;
    GPSTracker gps = null;
    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    EditText editsearch;
    ArrayList<CustomerSearch> arraylist = new ArrayList<CustomerSearch>();
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview);
        Search_customer_Activity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gps = new GPSTracker(Search_customer_Activity.this);
        hashSet = new HashSet<CustomerSearch>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer List");
        customutility = new CustomUtility();
        if (gps.canGetLocation()) {
            employee_latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
            employee_longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));
        } else {
            gps.showSettingsAlert();
        }

        Intent intent = getIntent();
        route_name = intent.getStringExtra("route_name");
        route_code = intent.getStringExtra("route_code");

        if (route_name != null) {
            setRouteDetail();  // current route plan
            setNewAddedCustomer(); // new added customer in route
        }
        list = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, arraylist);
        list.setAdapter(adapter);
        editsearch = (EditText) findViewById(R.id.search);
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void setNewAddedCustomer() {
        Location mylocation = new Location("");
        Location dest_location = new Location("");
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = null;
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NEW_ADDED_CUSTOMER
                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'"
                    + " AND " + DatabaseHelper.KEY_BUDAT + " = '" + customutility.getCurrentDate() + "'";
//                    + " AND " + DatabaseHelper.KEY_INTRESTED + " = '" + "HOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    customer_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    customer_number = cursor.getString(cursor.getColumnIndex("kunnr"));
                    partner = cursor.getString(cursor.getColumnIndex("partner"));
                    route_code = cursor.getString(cursor.getColumnIndex("route_code"));
                    route_name = cursor.getString(cursor.getColumnIndex("route_name"));
                    partner_class = cursor.getString(cursor.getColumnIndex("partner_class"));
                    land1 = cursor.getString(cursor.getColumnIndex("land1"));
                    land_txt = cursor.getString(cursor.getColumnIndex("land_txt"));
                    state_code = cursor.getString(cursor.getColumnIndex("state_code"));
                    state_txt = cursor.getString(cursor.getColumnIndex("state_txt"));
                    district_code = cursor.getString(cursor.getColumnIndex("district_code"));
                    district_txt = cursor.getString(cursor.getColumnIndex("district_txt"));
                    taluka_code = cursor.getString(cursor.getColumnIndex("taluka_code"));
                    taluka_txt = cursor.getString(cursor.getColumnIndex("taluka_txt"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                    email = cursor.getString(cursor.getColumnIndex("email"));
                    mob_no = cursor.getString(cursor.getColumnIndex("mob_no"));
                    tel_number = cursor.getString(cursor.getColumnIndex("tel_number"));
                    pincode = cursor.getString(cursor.getColumnIndex("pincode"));
                    contact_person = cursor.getString(cursor.getColumnIndex("contact_person"));
                    distributor_code = cursor.getString(cursor.getColumnIndex("distributor_code"));
                    distributor_name = cursor.getString(cursor.getColumnIndex("distributor_name"));
                    phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));
                    vkorg = cursor.getString(cursor.getColumnIndex("vkorg"));
                    vtweg = cursor.getString(cursor.getColumnIndex("vtweg"));
                    ktokd = cursor.getString(cursor.getColumnIndex("ktokd"));
                    customer_category = cursor.getString(cursor.getColumnIndex("customer_category"));
                    String str_lat = cursor.getString(cursor.getColumnIndex("latitude"));
                    String str_long = cursor.getString(cursor.getColumnIndex("longitude"));
                    Log.d("str_lat", "" + str_lat + str_long);
                    mylocation.setLatitude(employee_latitude);
                    mylocation.setLongitude(employee_longitude);

                    if (str_lat != null && str_long != null) {
                        customer_latitude = Double.valueOf(str_lat).doubleValue();
                        customer_longitude = Double.valueOf(str_long).doubleValue();
                        dest_location.setLatitude(customer_latitude);
                        dest_location.setLongitude(customer_longitude);
                    }
                    if (employee_latitude != 0.0 &&
                            employee_longitude != 0.0 &&
                            customer_latitude != 0.0 &&
                            customer_longitude != 0.0) {
                        Float distance = mylocation.distanceTo(dest_location); // in meter
                        distance = distance / 1000; // in km
                        total_distance = String.format(Locale.US,"%.2f", distance) + "" + "km Away";
                    } else {
                        total_distance = "";
                    }
                    CustomerSearch cs = new CustomerSearch(
                            customer_number,
                            customer_name,
                            partner,
                            district_code,
                            district_txt,
                            total_distance,
                            route_code,
                            route_name,
                            partner_class,
                            land1,
                            land_txt,
                            state_code,
                            state_txt,
                            taluka_code,
                            taluka_txt,
                            address,
                            email,
                            mob_no,
                            tel_number,
                            pincode,
                            contact_person,
                            distributor_code,
                            distributor_name,
                            phone_number,
                            str_lat,
                            str_long,
                            vkorg,
                            vtweg,
                            customer_category,
                            ktokd
                    );
                    // Binds all strings into an array
                    arraylist.add(cs);
                    RouteDetailBean routeDetailBean = new RouteDetailBean();
                    routeDetailBean.setList_routeDetail(arraylist);
                    if (str_lat != null && str_long != null) {
                        customer_latitude = Double.parseDouble(str_lat);
                        customer_longitude = Double.parseDouble(str_long);
                    }
                    //   Log.d("new_added_search",""+vkorg +"--"+ vtweg) ;
                    RouteHeaderBean.setRoute_code(route_code);
                    RouteHeaderBean.setRoute_name(route_name);
                    RouteHeaderBean.setVkorg(vkorg);
                    RouteHeaderBean.setVtweg(vtweg);
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if(db!=null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }
        }
    }

    public void setRouteDetail() {
        Location mylocation = new Location("");
        Location dest_location = new Location("");
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor = null;
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'"
                    + " AND " + DatabaseHelper.KEY_BUDAT + " = '" + customutility.getCurrentDate() + "'";
//                    + " AND " + DatabaseHelper.KEY_INTRESTED + " = '" + "HOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    customer_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    customer_number = cursor.getString(cursor.getColumnIndex("kunnr"));
                    partner = cursor.getString(cursor.getColumnIndex("partner"));
                    route_code = cursor.getString(cursor.getColumnIndex("route_code"));
                    route_name = cursor.getString(cursor.getColumnIndex("route_name"));
                    partner_class = cursor.getString(cursor.getColumnIndex("partner_class"));
                    land1 = cursor.getString(cursor.getColumnIndex("land1"));
                    land_txt = cursor.getString(cursor.getColumnIndex("land_txt"));
                    state_code = cursor.getString(cursor.getColumnIndex("state_code"));
                    state_txt = cursor.getString(cursor.getColumnIndex("state_txt"));
                    district_code = cursor.getString(cursor.getColumnIndex("district_code"));
                    district_txt = cursor.getString(cursor.getColumnIndex("district_txt"));
                    taluka_code = cursor.getString(cursor.getColumnIndex("taluka_code"));
                    taluka_txt = cursor.getString(cursor.getColumnIndex("taluka_txt"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                    email = cursor.getString(cursor.getColumnIndex("email"));
                    mob_no = cursor.getString(cursor.getColumnIndex("mob_no"));
                    tel_number = cursor.getString(cursor.getColumnIndex("tel_number"));
                    pincode = cursor.getString(cursor.getColumnIndex("pincode"));
                    contact_person = cursor.getString(cursor.getColumnIndex("contact_person"));
                    distributor_code = cursor.getString(cursor.getColumnIndex("distributor_code"));
                    distributor_name = cursor.getString(cursor.getColumnIndex("distributor_name"));
                    phone_number = cursor.getString(cursor.getColumnIndex("phone_number"));
                    vkorg = cursor.getString(cursor.getColumnIndex("vkorg"));
                    vtweg = cursor.getString(cursor.getColumnIndex("vtweg"));
                    ktokd = cursor.getString(cursor.getColumnIndex("ktokd"));
                    int i = db.delete(dataHelper.TABLE_NEW_ADDED_CUSTOMER, dataHelper.KEY_PHONE_NUMBER + " = '" + phone_number + "'", null);
                    if ((vkorg.equalsIgnoreCase("1200")) && (!ktokd.equalsIgnoreCase("9999"))) {
                        if (partner.equalsIgnoreCase("D") ||
                                partner.equalsIgnoreCase("P")) {
                            counter = true;
                        }
                    }
                    String str_lat = cursor.getString(cursor.getColumnIndex("latitude"));
                    String str_long = cursor.getString(cursor.getColumnIndex("longitude"));
                    Log.d("str_lat", "" + str_lat + str_long);
                    mylocation.setLatitude(employee_latitude);
                    mylocation.setLongitude(employee_longitude);
                    if (str_lat != null && str_long != null) {
                        customer_latitude = Double.valueOf(str_lat).doubleValue();
                        customer_longitude = Double.valueOf(str_long).doubleValue();
                        dest_location.setLatitude(customer_latitude);
                        dest_location.setLongitude(customer_longitude);
                    }
                    if (employee_latitude != 0.0 &&
                            employee_longitude != 0.0 &&
                            customer_latitude != 0.0 &&
                            customer_longitude != 0.0) {
                        Float distance = mylocation.distanceTo(dest_location); // in meter
                        distance = distance / 1000; // in km
                        total_distance = String.format(Locale.US,"%.2f", distance) + "" + "km Away";
                    } else {
                        total_distance = "";
                    }
                    CustomerSearch cs = new CustomerSearch(
                            customer_number,
                            customer_name,
                            partner,
                            district_code,
                            district_txt,
                            total_distance,
                            route_code,
                            route_name,
                            partner_class,
                            land1,
                            land_txt,
                            state_code,
                            state_txt,
                            taluka_code,
                            taluka_txt,
                            address,
                            email,
                            mob_no,
                            tel_number,
                            pincode,
                            contact_person,
                            distributor_code,
                            distributor_name,
                            phone_number,
                            str_lat,
                            str_long,
                            vkorg,
                            vtweg,
                            customer_category,
                            ktokd);
                    arraylist.add(cs);
                    RouteDetailBean routeDetailBean = new RouteDetailBean();
                    routeDetailBean.setList_routeDetail(arraylist);
                    if (str_lat != null && str_long != null) {
                        customer_latitude = Double.parseDouble(str_lat);
                        customer_longitude = Double.parseDouble(str_long);
                    }
                    RouteHeaderBean.setRoute_code(route_code);
                    RouteHeaderBean.setRoute_name(route_name);
                    RouteHeaderBean.setVkorg(vkorg);
                    RouteHeaderBean.setVtweg(vtweg);
                    RouteHeaderBean.setKtokd(ktokd);
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
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = null;
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_route_map_menu:
                intent = new Intent(getApplicationContext(), MapsActivity.class);
                // sending data to new activity
                intent.putExtra("route_name", route_name);
                startActivity(intent);
                return true;
            case R.id.action_add_customer:
                if (!counter) {
                    if (CustomUtility.isDateTimeAutoUpdate(this) && CustomUtility.CheckGPS(this)) {
                        Search_customer_Activity.this.finish();
                        if (vkorg.equalsIgnoreCase("1100") ||
                                vkorg.equalsIgnoreCase("1201") ||
                                vkorg.equalsIgnoreCase("1098") ||
                                vkorg.equalsIgnoreCase("1099") ||
                                vkorg.equalsIgnoreCase("1097")
                        ) {
                            intent = new Intent(getApplicationContext(), AddNewCustomer_Export_Activity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), AddNewCustomerActivity.class);
                        }
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "You can't add customer in this route , please contact to your coordinator", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route, menu);
        return true;
    }
}
