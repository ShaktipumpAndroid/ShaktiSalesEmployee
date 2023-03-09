package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

public class UpdateActivity extends AppCompatActivity {
    TextView tv_click,tv_click1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        tv_click = (TextView) findViewById(R.id.textClick);
        tv_click1 = (TextView) findViewById(R.id.textClick1);
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }


            }
        });

        tv_click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              finish();

            }
        });

    }
}
