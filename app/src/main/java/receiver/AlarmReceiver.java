package receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import activity.Capture_employee_gps_location;
import activity.CustomUtility;
import syncdata.SyncDataToSAP_New;


/**
 * Created by shakti on 10-Aug-18.
 */
public class AlarmReceiver extends BroadcastReceiver {
    Context mContex;

    //  GsmCellLocation cellLocation ;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

        mContex = context;

        int notificationId = intent.getIntExtra("notificationId", 0);

        new Capture_employee_gps_location(context, "0", "");


        new Worker().execute();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);


    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isOnline(mContex)) {
                    new SyncDataToSAP_New().SendDataToSap(mContex);
                }

            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
        }


    }
}