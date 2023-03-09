package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;


import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;

import bean.CustomerDetailBean;
import database.DatabaseHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double employee_latitude, employee_longitude, customer_latitude, customer_longitude;
    CustomerDetailBean customerdetailbean = null;
    String route_name = null, partner_name = null;
    // GPSTracker class
    GPSTracker gps = null;
    private GoogleMap mMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GPSTracker(MapsActivity.this);
        customerdetailbean = new CustomerDetailBean();

        Intent i = getIntent();
        route_name = i.getStringExtra("route_name");


        partner_name = i.getStringExtra("partner_name");
        ;
        String str2 = "null";
        str2 = i.getStringExtra("partner_latitude");
        ;


        String str3 = "null";
        str3 = i.getStringExtra("partner_longitude");
        ;

        String str4 = "null";
        str4 = i.getStringExtra("single_map");
        ;


        if (("single_map").equals(str4)) {

            customer_latitude = Double.parseDouble(str2);
            customer_longitude = Double.parseDouble(str3);

        }


        // check if GPS enabled
        if (gps.canGetLocation()) {

            employee_latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
            employee_longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (route_name != null) {

            showAllcustomerOnMap();
        }


        if (customer_latitude != 0.0 && customer_longitude != 0.0) {

            showSinglecustomerOnMap();
        }


    }


    public void showSinglecustomerOnMap() {
        float zoomLevel;

        LatLng customer_location = new LatLng(customer_latitude, customer_longitude);
        mMap.addMarker(new MarkerOptions().position(customer_location).title(partner_name));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(customer_location));

        zoomLevel = 8;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customer_location, zoomLevel));

        if (employee_latitude != 0.0) {
            LatLng employee_location = new LatLng(employee_latitude, employee_longitude);
            mMap.addMarker(new MarkerOptions().position(employee_location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Your are here"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(employee_location));

            zoomLevel = 10; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(employee_location, zoomLevel));

        }


    }

    @SuppressLint("Range")
    public void showAllcustomerOnMap() {


        if (employee_latitude != 0.0) {
            // Add a marker in Sydney and move the camera
            LatLng employee_location = new LatLng(employee_latitude, employee_longitude);
            mMap.addMarker(new MarkerOptions().position(employee_location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Your are here"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(employee_location));
            float zoomLevel = 10; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(employee_location, zoomLevel));
        }


        DatabaseHelper dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + DatabaseHelper.KEY_ROUTE_NAME + " = '" + route_name + "'"
                    + " AND " + DatabaseHelper.KEY_BUDAT + " = '" + new CustomUtility().getCurrentDate() + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    partner_name = cursor.getString(cursor.getColumnIndex("partner_name"));
                    String str_lat = cursor.getString(cursor.getColumnIndex("latitude"));
                    String str_long = cursor.getString(cursor.getColumnIndex("longitude"));

                    customer_latitude = Double.parseDouble(str_lat);
                    customer_longitude = Double.parseDouble(str_long);

                    // Add a marker in Sydney and move the camera

                    if (customer_latitude != 0.0 && customer_longitude != 0.0) {
                        LatLng customer_location = new LatLng(customer_latitude, customer_longitude);
                        mMap.addMarker(new MarkerOptions().position(customer_location).title(partner_name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(customer_location));
                    }
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
}
