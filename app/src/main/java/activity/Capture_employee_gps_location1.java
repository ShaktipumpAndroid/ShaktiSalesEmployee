package activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.DecimalFormat;

import bean.LoginBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 12/16/2016.
 */
public class Capture_employee_gps_location1 {

    DatabaseHelper dataHelper;


    public Capture_employee_gps_location1(Context context, double lat, double lng, String event, String phone_number) {


//*************  get gps location *******************************
     /*   GPSTracker gps = new GPSTracker(context);*/
        String latitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(lat)));
        String longitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(lng)));

        dataHelper = new DatabaseHelper(context);

      SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        LoginBean.userid = pref.getString("key_username", "userid");


/*//*************  get mobile tower location *******************************

        Log.e("DATE","&&&"+new CustomUtility().getCurrentDate());
        Log.e("TIME","&&&"+new CustomUtility().getCurrentTime());
        Log.e("LAT","&&&"+latitude);
        Log.e("LNG","&&&"+longitude);*/

     //   Toast.makeText(context, "Location" + lat + lng, Toast.LENGTH_SHORT).show();

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
