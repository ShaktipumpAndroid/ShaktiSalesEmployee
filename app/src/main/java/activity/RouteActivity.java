package activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.HashSet;

import bean.LoginBean;
import customlist.RouteCustomList;
import database.DatabaseHelper;
import searchlist.Search_customer_Activity;

public class RouteActivity extends AppCompatActivity {
    ListView list;
    String versionName;
    Context context;
    ArrayList<String> arrayList = null;
    ArrayList<String> arrayList1 = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        context = this;
        arrayList = new ArrayList<String>();
        arrayList1 = new ArrayList<String>();
        HashSet<String> hashSet = new HashSet<String>();
        HashSet<String> hashSet1 = new HashSet<String>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Route       " + " V " + BuildConfig.VERSION_NAME);
        getSupportActionBar().setTitle("Route");
        DatabaseHelper dataHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        db.beginTransactionNonExclusive();
        try {
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
//            +  " WHERE " + DatabaseHelper.KEY_BUDAT  + " = '" + new CustomUtility().getCurrentDate() + "'";
            String userid = LoginBean.getUseid();
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ROUTE_DETAIL
                    + " WHERE " + DatabaseHelper.KEY_BUDAT + " = '" + new CustomUtility().getCurrentDate() + "'"
                    + " AND " + DatabaseHelper.KEY_PERNR + " = '" + userid + "'";

            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("count_db", "" + cursor.getCount() + cursor);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String route_name = cursor.getString(cursor.getColumnIndex("route_name"));
                    CustomUtility.capitalize(route_name);
                    arrayList.add(route_name);
                    hashSet.addAll(arrayList);
                    arrayList.clear();
                    arrayList.addAll(hashSet);
                    arrayList1.add("");
                }
                db.setTransactionSuccessful();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                //  alertDialog.setTitle("Data Save alert !");
                alertDialog.setMessage("Today route is not assigned , Please contact administrator.");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RouteActivity.this.finish();
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if(db != null) {
                db.endTransaction();
                db.close();
            }
        }
        RouteCustomList adapter = new RouteCustomList(RouteActivity.this, arrayList, arrayList1);
        list = (ListView) findViewById(R.id.route_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getApplicationContext(), Search_customer_Activity.class);
            i.putExtra("route_name", arrayList.get(position));
            startActivity(i);
        });
    }

    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                RouteActivity.this.finish();
                onBackPressed();
                return true;
            case R.id.action_target_menu:

            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
