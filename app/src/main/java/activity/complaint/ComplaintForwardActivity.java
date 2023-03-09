package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import adapter.ExpandableListAdapter;
import bean.ComplaintForward;
import searchlist.complaint.SearchForwardComplaintListViewAdapter;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ComplaintForwardActivity extends AppCompatActivity {
    Context mContext;
    String cmp_no,cmp_epc;
    ProgressDialog progressDialog;
    ListView list;
    EditText editsearch;
    SearchForwardComplaintListViewAdapter adapter;
    ArrayList<ComplaintForward> arraylist;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ComplaintForward complaintForward;
    String forward_to = "null";
    SharedPreferences pref;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ComplaintForwardActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_forward);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Spinner_list = new ArrayList<String>();

        editsearch = (EditText) findViewById(R.id.search);
        list = (ListView) findViewById(R.id.listview);


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Bundle bundle = getIntent().getExtras();
        cmp_no = bundle.getString("cmp_no");
        cmp_epc = bundle.getString("epc");
        arraylist = new ArrayList<ComplaintForward>();

        pref = mContext.getSharedPreferences("MyPref", 0);


//        cmp_posnr = bundle.getString("cmp_posnr");
//        cmp_category = bundle.getString("cmp_category");
//        image_name = bundle.getString("image_name");
//

        String title = "Complaint No. " + cmp_no;
        getSupportActionBar().setTitle(title);


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub


                forward_to = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

//                Toast.makeText(
//                        getApplicationContext(),
//                         listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();


                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("Please wait..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);

                System.out.println("WebURL.Serial_no==>>"+WebURL.Serial_no);

                new Worker().execute();

                return false;
            }
        });
//********************************************************************************
//
//        spinner = (Spinner) findViewById(R.id.spinner);
//
//
//
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3)
//            {
//                index = arg0.getSelectedItemPosition();
//                forward_to = spinner.getSelectedItem().toString();
//
//
//
//
//
//
//                progressDialog = new ProgressDialog( mContext);
//                progressDialog.setMessage("Loading..."); // Setting Message
//                progressDialog.setTitle("Please wait..."); // Setting Title
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//
//
//                new Worker().execute();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });
//
//
//
//
//
//        Spinner_list.clear();
//
//        Spinner_list.add("Forward to");
//        Spinner_list.add("Service Engineer");
//        Spinner_list.add("Service Center");
//        Spinner_list.add("Service Partner");
//        spinner.setPrompt("Forward to");
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Spinner_list);
//
//        // Drop down layout style - list view with radio button
//        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);


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

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Click Here to Forward");

       /* // Adding child data
        List<String> forward = new ArrayList<String>();
        forward.add("Service Engineer");
        forward.add("Service Partner");
        forward.add("Service Center");
        forward.add("Installer");*/

        // Adding child data
        List<String> forward = new ArrayList<String>();
        forward.add("Service Center");
        forward.add("Service Engineer");
        forward.add("Freelancer");
      /*  forward.add("Solar Installation Partner");
        forward.add("Service Support center");
        forward.add("call center");*/


        listDataChild.put(listDataHeader.get(0), forward); // Header, Child data

    }

    public void get_device() {

        String url = "null";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();

        param.add(new BasicNameValuePair("pernr", pref.getString("key_username", "userid")));
        param.add(new BasicNameValuePair("forward_to", forward_to));
        param.add(new BasicNameValuePair("cmpno", cmp_no));

      //  param.add(new BasicNameValuePair("sernr", WebURL.Serial_no));

       // Toast.makeText(mContext, "WebURL.Serial_no==>>"+WebURL.Serial_no, Toast.LENGTH_SHORT).show();



        if (CustomUtility.isOnline(mContext)) {

            try {

                String obj = CustomHttpClient.executeHttpPost1(WebURL.FORWARD_TO, param);


                if (!obj.equalsIgnoreCase("")) {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    arraylist.clear();


                    JSONObject jsonObj = new JSONObject(obj);
                    JSONArray ja_forward = jsonObj.getJSONArray("complaint_forward");


                    for (int i = 0; i < ja_forward.length(); i++) {

                        JSONObject jo_forward = ja_forward.getJSONObject(i);

                        complaintForward = new ComplaintForward();

                        complaintForward.setPartner_code(jo_forward.getString("partner_code"));
                        complaintForward.setPartner_name(jo_forward.getString("partner_name"));
                        arraylist.add(complaintForward);

                    }


                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    Message msg = new Message();
                    msg.obj = "Connection Error";
                    mHandler.sendMessage(msg);

                }


            } catch (Exception e) {
                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                Message msg = new Message();
                msg.obj = "Connection Error";
                mHandler.sendMessage(msg);
            }

        } else {
              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


            Message msg = new Message();
            msg.obj = "No Internet Connection";
            mHandler.sendMessage(msg);
        }

    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {

                get_device();

                runOnUiThread(

                        new Runnable() {

                            @Override
                            public void run() {

                                //  Log.d("forward_to1",forward_to);

                                        adapter = new SearchForwardComplaintListViewAdapter(mContext, arraylist, forward_to, cmp_no,cmp_epc);

                                        list.setAdapter(adapter);

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
                        });


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
              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
        }


    }
}
