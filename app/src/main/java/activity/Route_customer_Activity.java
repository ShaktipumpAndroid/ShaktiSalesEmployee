package activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import customlist.RouteCustomList;
import database.DatabaseHelper;

public class Route_customer_Activity extends AppCompatActivity {
    ArrayList<String> arrayList = null;
    ArrayList<String> arrayList1 = null;
    HashSet<String> hashSet = null;
    HashSet<String> hashSet1 = null;
    ArrayList<String> routemMaplist = null;
    String route_name;
    GPSTracker gps = null;
    ListView list;
    double employee_latitude, employee_longitude, customer_latitude, customer_longitude;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_customer);

        arrayList = new ArrayList<String>();
        arrayList1 = new ArrayList<String>();
        routemMaplist = new ArrayList<String>();
        gps = new GPSTracker(Route_customer_Activity.this);
        hashSet = new HashSet<String>();
        hashSet1 = new HashSet<String>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer List");


        // check if GPS enabled
        if (gps.canGetLocation()) {

            employee_latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
            employee_longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        Intent i = getIntent();
        route_name = i.getStringExtra("route_name");

        if (route_name != null) {

            Toast.makeText(this, route_name, Toast.LENGTH_LONG).show();

            setRouteDetail();

            RouteCustomList adapter = new
                    RouteCustomList(Route_customer_Activity.this, arrayList, arrayList1);
            list = (ListView) findViewById(R.id.route_list_view);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(Route_customer_Activity.this, "You Clicked at " + arrayList.get(position), Toast.LENGTH_SHORT).show();

                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(getApplicationContext(), Route_customer_Activity.class);
                    // sending data to new activity
                    i.putExtra("route_name", arrayList.get(position));
                    startActivity(i);

                }
            });
        }


    }

    public void setRouteDetail() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.d("count_db", "" + cursor.getCount());
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    double dist = distance(employee_latitude, employee_longitude, customer_latitude, customer_longitude);


                    String str = String.format(Locale.US,"%.2f", dist);

                    Log.d("Distance", "" + str);

                    String partner_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    String str_lat = cursor.getString(cursor.getColumnIndex("latitude"));
                    String str_long = cursor.getString(cursor.getColumnIndex("longitude"));


                    customer_latitude = Double.parseDouble(str_lat);
                    customer_longitude = Double.parseDouble(str_long);


                    arrayList.add(partner_name + "" + str + "KM" + "" + "Away");

                    hashSet.addAll(arrayList);
                    arrayList.clear();
                    arrayList.addAll(hashSet);

                    arrayList1.add("");


                }


                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
            }
            // End the transaction.
            if(db!=null) {
                db.close();
            }
            // Close database
        }


    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_route_map_menu:

                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                // sending data to new activity
                i.putExtra("route_name", route_name);
                startActivity(i);

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
