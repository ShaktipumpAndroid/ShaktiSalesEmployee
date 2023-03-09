package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

public class DisplayStockActivity extends AppCompatActivity {
    Context mContext;
    TextView tv_werks, tv_matnr, tv_stock, tv_maktx, tv_date;
    CustomUtility customutility;
    String matnr = "", stock = "", plant_name = "", maktx = "", color = "", date_time = "";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stock);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Stock Detail");

        customutility = new CustomUtility();

        tv_werks = (TextView) findViewById(R.id.werks);
        tv_matnr = (TextView) findViewById(R.id.matnr);
        tv_stock = (TextView) findViewById(R.id.stock);
        tv_maktx = (TextView) findViewById(R.id.maktx);
        tv_date = (TextView) findViewById(R.id.date);


        Intent i = getIntent();
        stock = i.getStringExtra("plant_stock");
        plant_name = i.getStringExtra("plant_name");
        matnr = i.getStringExtra("plant_matnr");
        maktx = i.getStringExtra("plant_maktx");
        color = i.getStringExtra("plant_color");

        Log.e("COLOR", "&&&&&" + color);

        customutility.getCurrentDate();

        tv_werks.setText(plant_name);
        tv_matnr.setText(matnr);

        if (color.trim().equalsIgnoreCase("GREEN")) {
            tv_stock.setText(stock);
            tv_stock.setTextColor(Color.WHITE);
            tv_stock.setBackgroundColor(Color.GREEN);
        } else if (color.trim().equalsIgnoreCase("YELLOW")) {
            tv_stock.setText(stock);
            tv_stock.setTextColor(Color.BLACK);
            tv_stock.setBackgroundColor(Color.YELLOW);
        } else if (color.trim().equalsIgnoreCase("BLACK")) {
            tv_stock.setText(stock);
            tv_stock.setTextColor(Color.WHITE);
            tv_stock.setBackgroundColor(Color.BLACK);
        } else if (color.trim().equalsIgnoreCase("RED")) {
            tv_stock.setText(stock);
            tv_stock.setTextColor(Color.WHITE);
            tv_stock.setBackgroundColor(Color.RED);
        } else {
            tv_stock.setText(stock);
            tv_stock.setTextColor(Color.BLACK);
            tv_stock.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.black_border_text));
        }

        tv_maktx.setText(maktx);

        date_time = customutility.getCurrentDate() + "     " + customutility.getCurrentTime();
        tv_date.setText(date_time);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
