package backgroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import activity.Capture_employee_gps_location;
import activity.CustomUtility;
import activity.GPSTracker;
import bean.EmployeeGPSActivityBean;
import database.DatabaseHelper;
import syncdata.SyncDataToSAP_New;

/**
 * Created by shakti on 12/3/2016.
 * capture lat long on every 15 min
 */


public class TimeService extends Service {
    // constant


   // public static final long NOTIFY_INTERVAL = 60 * 15 * 1000;    // 1000 = 1 second   15 min

    public static final long NOTIFY_INTERVAL =  20 * 1000;    // 1000 = 1 second   1 min

    ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = null;
    DatabaseHelper db = null;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }

        // cancel if already existed
        if (mTimer != null) {
            // Toast.makeText(this, "already running", Toast.LENGTH_LONG).show();

            mTimer.cancel();
        } else {
            // recreate new
            //  Toast.makeText(this, "create new command", Toast.LENGTH_LONG).show();
            mTimer = new Timer();

            employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
            db = new DatabaseHelper(getApplicationContext());


        }
        // schedule task


        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
//            }
//        }).start();


        // stopSelf();
        // return START_NOT_STICKY;
        //Toast.makeText(this, "start command", Toast.LENGTH_LONG).show();

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        //  isRunning = false;
        mTimer.cancel();
        // Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.shaktipumps.shakti.shaktisalesemployee";
        String channelName = "Shakti Sales Employee";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast


                   getLatLong();

                    // sync offline data in background  comment in 1.6 app version

                    new Worker().execute();

                }

            });
        }


    }
    private String getLatLong() {
        GPSTracker gps;
        gps = new GPSTracker(getApplicationContext());
        double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));


        new Capture_employee_gps_location(getApplicationContext(), "0", "");


        String latlong;
        latlong = latitude + "," + longitude;
        Log.d("background", "" + latlong);

        return latlong;
    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                // sync employee gps data to sap every 15 min
                if (CustomUtility.isOnline(getApplicationContext())) {

                    //  new SyncDataToSAP_New().SendEmployeeGPS(getApplicationContext());

                    new SyncDataToSAP_New().SendDataToSap(getApplicationContext());
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
