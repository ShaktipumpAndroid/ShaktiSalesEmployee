package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.Locale;

import database.DatabaseHelper;
import searchlist.MaterialAnalysisListViewAdapter;
import searchlist.MaterialAnalysisSearch;
import webservice.SAPWebService;

public class MaterialAnalysisActivity extends AppCompatActivity {
    CustomUtility customutility = null;
    MaterialAnalysisListViewAdapter adapter;
    SAPWebService con = null;
    ListView list;
    SharedPreferences pref;
    CustomUtility customUtility;
    Context context;
    SharedPreferences.Editor editor;
    DatabaseHelper dataHelper;
    ArrayList<MaterialAnalysisSearch> arraylist = new ArrayList<MaterialAnalysisSearch>();
    EditText editsearch;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MaterialAnalysisActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_analysis);
        context = this;
        MaterialAnalysisActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(context);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        con = new SAPWebService();
        list = (ListView) findViewById(R.id.listview);
        getSupportActionBar().setTitle("Material Analysis");

        customUtility = new CustomUtility();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        if (!pref.getString("key_download_stock_date", "date").equalsIgnoreCase(customUtility.getCurrentDate())) {
            syncing_material_analysis();
        }


        selectMaterialData();


        // Pass results to ListViewAdapter Class
        adapter = new MaterialAnalysisListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_material_analysis, menu);
        return true;
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
            case R.id.action_menu_material_analysis:
                /**************************** sync material data **************************************************/

                dataHelper.deleteMaterialAnalysis();
                syncing_material_analysis();


                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void syncing_material_analysis() {

        progressDialog = ProgressDialog.show(MaterialAnalysisActivity.this, "", "Download Material Analysis...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(MaterialAnalysisActivity.this)) {


                    try {

                        con.getMaterialAnalysis(MaterialAnalysisActivity.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                        editor.putString("key_download_stock_date", customUtility.getCurrentDate());
                        editor.commit(); //


                        MaterialAnalysisActivity.this.finish();
                        Intent intent = new Intent(MaterialAnalysisActivity.this, MaterialAnalysisActivity.class);
                        startActivity(intent);
                        //selectTargetData() ;

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Log.d("exce", "" + e);
                    }
                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);


                }


            }

        }.start();

    }

    public void selectMaterialData() {
        dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_MATERIAL_ANALYSIS;


            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


//                    String begda    = cursor.getString(cursor.getColumnIndex("matnr"));
//                    String endda    = cursor.getString(cursor.getColumnIndex("maktx"));
//                    String fr_pernr = cursor.getString(cursor.getColumnIndex("extwg"));
//
//
//                    String fr_ename = cursor.getString(cursor.getColumnIndex("plant"));
//                    String fr_department = cursor.getString(cursor.getColumnIndex("indicator"));
//                    String fr_target = cursor.getString(cursor.getColumnIndex("delivery_time"));
//                    String fr_net_sale = cursor.getString(cursor.getColumnIndex("kbetr"));
//                    String fr_position = cursor.getString(cursor.getColumnIndex("konwa"));

                    MaterialAnalysisSearch ms = new MaterialAnalysisSearch();

                    ms.setMatnr(cursor.getString(cursor.getColumnIndex("matnr")));
                    ms.setMaktx(cursor.getString(cursor.getColumnIndex("maktx")));
                    ms.setExtwg(cursor.getString(cursor.getColumnIndex("extwg")));
                    ms.setPlant(cursor.getString(cursor.getColumnIndex("plant")));
                    ms.setIndicator(cursor.getString(cursor.getColumnIndex("indicator")));
                    ms.setDelivery_time(cursor.getString(cursor.getColumnIndex("delivery_time")));
                    ms.setKbetr(cursor.getString(cursor.getColumnIndex("kbetr")));
                    ms.setKonwa(cursor.getString(cursor.getColumnIndex("konwa")));

                    arraylist.add(ms);

                    // Log.d("target" , "" +  fr_target);


//
//                    lv_target = Double.parseDouble(fr_target);
//                    lv_sale = Double.parseDouble(fr_net_sale);
//
//                    total_targrt = total_targrt +  lv_target;
//                    total_sale   =  total_sale + lv_sale;


                }

            }


            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {

            if(db!=null) {
                db.endTransaction();
            }
            if (cursor != null) {
                cursor.close();
            }
            if(db!=null) {
                // End the transaction.
                db.close();
            }
            // Close database

        }


    }

}
