package activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumps.shakti.shaktisalesemployee.BuildConfig;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;

import bean.LoginBean;
import model.VersionResponse;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 3000;

    Intent i;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    String versionName = "0.0";
    String newVersion = "0.0";

    @Override
    // ** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContex = this;
        // ******* Create SharedPreferences *******/

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        versionName = BuildConfig.VERSION_NAME;

        deleteCache(mContex);

        new Worker1().execute();


        new Handler().postDelayed(() -> {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                SplashActivity.this.finish();
                Intent i = new Intent(SplashActivity.this, UpdateActivity.class);
                startActivity(i);

            } else {

                if (pref.getString("key_login", "login").equalsIgnoreCase("Y")) {

                    LoginBean lb = new LoginBean();
                    LoginBean.setLogin(pref.getString("key_username", "userid"), pref.getString("key_ename", "username"));

                    i = new Intent(SplashActivity.this, MainActivity1.class);
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(i);
                SplashActivity.this.finish();

            }
        }, SPLASH_TIME_OUT);
    }



    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }



    public void getVersion() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<VersionResponse> call = apiService.getVersionCode();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VersionResponse> call, @NonNull Response<VersionResponse> response) {
                try {
                    VersionResponse dashResponse = response.body();
                    if (dashResponse != null) {

                        newVersion = dashResponse.getVersion();
                        Log.e("VERSION", "&&&&" + newVersion);

                        // Toast.makeText(SplashActivity.this, "Success...", Toast.LENGTH_SHORT).show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VersionResponse> call, @NonNull Throwable t) {

                Toast.makeText(SplashActivity.this, "FAILED...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class Worker1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isOnline(mContex)) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    getVersion();


                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}