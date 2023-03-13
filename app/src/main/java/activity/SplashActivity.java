package activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import bean.LoginBean;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 3000;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;


    @Override
    // ** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContex = this;

        // ******* Create SharedPreferences *******/
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        openNextActivty();

    }


    private void openNextActivty() {
        new Handler().postDelayed(() -> {

            if (pref.getString("key_login", "login").equalsIgnoreCase("Y")) {
                LoginBean.setLogin(pref.getString("key_username", "userid"), pref.getString("key_ename", "username"));
                Intent   i = new Intent(SplashActivity.this, MainActivity1.class);
                startActivity(i);
            } else {
                Intent  i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}