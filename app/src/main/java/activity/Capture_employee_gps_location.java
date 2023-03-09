package activity;

import android.content.Context;

import android.content.SharedPreferences;
import android.util.Log;

import java.text.DecimalFormat;

import bean.LoginBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 12/16/2016.
 */
public class Capture_employee_gps_location {

    DatabaseHelper dataHelper;


    public Capture_employee_gps_location(Context context, String event, String phone_number) {


//*************  get gps location *******************************
        GPSTracker gps = new GPSTracker(context);
        String latitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude())));
        String longitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude())));

        dataHelper = new DatabaseHelper(context);

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        LoginBean.userid = pref.getString("key_username", "userid");


//*************  get mobile tower location *******************************

        Log.e("DATE","&&&"+new CustomUtility().getCurrentDate());
        Log.e("TIME","&&&"+new CustomUtility().getCurrentTime());
        Log.e("LAT","&&&"+latitude);
        Log.e("LNG","&&&"+longitude);

        dataHelper.insertEmployeeGPSActivity(
                LoginBean.userid,
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                event,
                latitude,
                longitude,
                context,
                phone_number);

    }

}
