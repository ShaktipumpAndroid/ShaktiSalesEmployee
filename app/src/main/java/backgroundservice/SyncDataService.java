package backgroundservice;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import activity.CustomUtility;
import database.DatabaseHelper;
import syncdata.SyncDataToSAP_New;
import webservice.SAPWebService;

/**
 * Created by shakti on 12/12/2016.
 */
public class SyncDataService extends Service {

    Context mContex;
    String sync_data = null;
    DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    SharedPreferences pref;
    SAPWebService con = null;
    SharedPreferences.Editor editor;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContex, mString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContex = getApplicationContext();
        // Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
        databaseHelper = new DatabaseHelper(mContex);
        con = new SAPWebService();
        pref = mContex.getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Worker().execute();
            }
        }).start();

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //  Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
    }

    /******************** sync data in background process ********************************/
    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            String data = null;
            try {
                if (CustomUtility.isOnline(getApplicationContext())) {
                    new SyncDataToSAP_New().SendDataToSap(mContex);
                   //download_complaint_data();
                    Message msg = new Message();
                    msg.obj = "Data Successfully Send to Server";
                    mHandler.sendMessage(msg);
                    databaseHelper.deleteImage();
                } else {
                    Message msg = new Message();
                    msg.obj = "No internet Connection . Data saved in offline";
                    mHandler.sendMessage(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
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
