package activity.complaint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import activity.CustomUtility;
import bean.LoginBean;
import searchlist.complaint.SearchClouserComplaintListViewAdapter;
import searchlist.complaint.SearchComplaint;
import searchlist.complaint.SearchComplaint1;
import searchlist.complaint.SearchIssueMaterialListViewAdapter;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

public class IssueMaterialListComplaintActivity extends AppCompatActivity {

    EditText editsearch;
    RecyclerView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    SearchIssueMaterialListViewAdapter adapter;
    ArrayList<SearchComplaint1> arraylist = new ArrayList<SearchComplaint1>();
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManagerSubCategory;
    private LinearLayout lin1,lin2;
    private String cmpno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_issuelist_material);
        mContex = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Complaint Issue Material List");



        progressDialog = new ProgressDialog(mContex);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            cmpno = bundle.getString("cmpno");
        }

        list = (RecyclerView) findViewById(R.id.emp_list);


        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

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
    protected void onResume() {
        super.onResume();

        if (CustomUtility.isOnline(mContex)) {

            list.setAdapter(null);

            new GetInstallationDataList_Task().execute();

        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection....", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();
            //callWebPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetInstallationDataList_Task extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(mContex, "", "Please Wait...");

        }

        @Override
        protected String doInBackground(String... params) {
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.clear();
            param.add(new BasicNameValuePair("lifnr", LoginBean.getUseid()));


            String login_selec = null;


            try {

                login_selec = CustomHttpClient.executeHttpPost1(WebURL.NORMS_MATERIAL_LIST, param);

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return login_selec;

        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();


                try {

                    JSONObject object = new JSONObject(result);
                    String obj1 = object.getString("norms_data");

                    JSONArray ja = new JSONArray(obj1);

                    if (ja.length() > 0) {
                        for (int j = 0; j < ja.length(); j++) {
                            SearchComplaint1 searchComplaint1 = new Gson().fromJson(ja.get(j).toString(), SearchComplaint1.class);
                            JSONObject jo = ja.getJSONObject(j);


                            lin1.setVisibility(View.VISIBLE);
                            lin2.setVisibility(View.GONE);

                           /* searchComplaint1.setCustomer_code(jo.getString("customer_code"));
                            searchComplaint1.setCustomer_name(jo.getString("customer_name"));
                            searchComplaint1.setMatnr(jo.getString("matnr"));
                            searchComplaint1.setMaterial_name(jo.getString("material_name"));
                            searchComplaint1.setLifnr(jo.getString("lifnr"));
                            searchComplaint1.setName1(jo.getString("name1"));
                            searchComplaint1.setBwart(jo.getString("bwart"));
                            searchComplaint1.setQuantity(jo.getString("quantity"));
                            searchComplaint1.setMslbqty(jo.getString("mslbqty"));
                            searchComplaint1.setFreshqty(jo.getString("freshqty"));*/


                            arraylist.add(searchComplaint1);

                            list.setAdapter(null);

                            adapter = new SearchIssueMaterialListViewAdapter(mContex, arraylist ,cmpno);
                            layoutManagerSubCategory = new LinearLayoutManager(mContex);
                            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                            list.setLayoutManager(layoutManagerSubCategory);
                            list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();



                        }
                    } else {

                        lin1.setVisibility(View.GONE);
                        lin2.setVisibility(View.VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }

}
