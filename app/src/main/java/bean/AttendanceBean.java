package bean;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by shahid on 30-06-2016.
 */
public class AttendanceBean implements Serializable {
    public static final String IN = "IN";
    public static final String OUT = "OUT";
    private static final String PREF_NAME = "AttendancePref";

    public String TYPE = "";
    public String PERNR = "";
    public String BEGDA = "";
    public String SERVER_DATE_IN = "";
    public String SERVER_TIME_IN = "";
    public String SERVER_DATE_OUT = "";
    public String SERVER_TIME_OUT = "";
    public String IN_ADDRESS = "";
    public String OUT_ADDRESS = "";
    public String IN_TIME = "";
    public String OUT_TIME = "";
    public String WORKING_HOURS = "";
    public String IMAGE_DATA = "";
    public long CURRENT_MILLIS = 0;

    public String IN_LAT_LONG = "";
    public String OUT_LAT_LONG = "";
    public String IN_FILE_NAME = "";
    public String IN_FILE_LENGTH = "";
    public String IN_FILE_VALUE = "";
    public String OUT_FILE_NAME = "";
    public String OUT_FILE_LENGTH = "";
    public String OUT_FILE_VALUE = "";

    public String IN_STATUS = "";
    public String OUT_STATUS = "";
    public String IN_IMAGE = "";
    public String OUT_IMAGE = "";
    public String KEY_ID = "";


    public AttendanceBean() {

    }

    public AttendanceBean(JSONObject json) {
        if (json != null) {
            //    AttendanceBean bean = new AttendanceBean();
            this.PERNR = json.optString("PERNR", "");
            this.BEGDA = json.optString("BEGDA", "");
            this.SERVER_DATE_IN = json.optString("SERVER_DATE_IN", "");
            this.SERVER_TIME_IN = json.optString("SERVER_TIME_IN", "");
            this.SERVER_DATE_OUT = json.optString("SERVER_DATE_OUT", "");
            this.SERVER_TIME_OUT = json.optString("SERVER_TIME_OUT", "");
            this.IN_ADDRESS = json.optString("IN_ADDRESS", "");
            this.IN_TIME = json.optString("IN_TIME", "");
            this.OUT_ADDRESS = json.optString("OUT_ADDRESS", "");
            this.OUT_TIME = json.optString("OUT_TIME", "");
            this.WORKING_HOURS = json.optString("WORKING_HOURS", "");
            this.IN_LAT_LONG = json.optString("IN_LAT_LONG", "");
            this.IN_FILE_VALUE = "";
            this.OUT_LAT_LONG = json.optString("OUT_LAT_LONG", "");
            this.OUT_FILE_VALUE = "";
            this.IN_IMAGE = json.optString("IN_IMAGE", "");
            this.OUT_IMAGE = json.optString("OUT_IMAGE", "");


            if (this.SERVER_DATE_IN.equalsIgnoreCase("00000000")) {
                this.SERVER_DATE_IN = "";
            }
            if (this.SERVER_DATE_OUT.equalsIgnoreCase("00000000")) {
                this.SERVER_DATE_OUT = "";
            }
            if (this.BEGDA.equalsIgnoreCase("00000000")) {
                this.BEGDA = "";
            }


            if (this.SERVER_TIME_IN.equalsIgnoreCase("000000")) {
                this.SERVER_TIME_IN = "";
            }
            if (this.SERVER_TIME_OUT.equalsIgnoreCase("000000")) {
                this.SERVER_TIME_OUT = "";
            }

            if (this.IN_TIME.equalsIgnoreCase("000000")) {
                this.IN_TIME = "";
            }
            if (this.OUT_TIME.equalsIgnoreCase("000000")) {
                this.OUT_TIME = "";
            }
        }
    }


  /*  public AttendanceBean(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.TYPE = sharedPreferences.getString("TYPE","");
        this.PERNR = sharedPreferences.getString("PERNR","");
        this.BEGDA = sharedPreferences.getString("BEGDA", "");
        this.SERVER_DATE_IN = sharedPreferences.getString("SERVER_DATE_IN", "");
        this.SERVER_TIME_IN = sharedPreferences.getString("SERVER_TIME_IN", "");
        this.SERVER_DATE_OUT = sharedPreferences.getString("SERVER_DATE_OUT", "");
        this.SERVER_TIME_OUT = sharedPreferences.getString("SERVER_TIME_OUT", "");
        this.IN_ADDRESS = sharedPreferences.getString("IN_ADDRESS", "");
        this.OUT_ADDRESS = sharedPreferences.getString("OUT_ADDRESS", "");
        this.IN_TIME = sharedPreferences.getString("IN_TIME", "");
        this.OUT_TIME = sharedPreferences.getString("OUT_TIME", "");
        this.WORKING_HOURS = sharedPreferences.getString("WORKING_HOURS", "");
        this.IMAGE_DATA = sharedPreferences.getString("IMAGE_DATA", "");
        this.CURRENT_MILLIS  = sharedPreferences.getLong("CURRENT_MILLIS",0);
        this.IN_LAT_LONG = sharedPreferences.getString("IN_LAT_LONG", "");
        this.OUT_LAT_LONG = sharedPreferences.getString("OUT_LAT_LONG", "");
        this.IN_FILE_NAME = sharedPreferences.getString("IN_FILE_NAME", "");
        this.IN_FILE_LENGTH = sharedPreferences.getString("IN_FILE_LENGTH", "");
        this.IN_FILE_VALUE = sharedPreferences.getString("IN_FILE_VALUE", "");
        this.OUT_FILE_NAME = sharedPreferences.getString("OUT_FILE_NAME", "");
        this.OUT_FILE_LENGTH = sharedPreferences.getString("OUT_FILE_LENGTH", "");
        this.OUT_FILE_VALUE = sharedPreferences.getString("OUT_FILE_VALUE", "");
        this.IN_STATUS = sharedPreferences.getString("IN_STATUS", "");
        this.OUT_STATUS = sharedPreferences.getString("OUT_STATUS", "");
    }*/

    public static void updatePreferenceString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void removeAttendanceData(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }
}
