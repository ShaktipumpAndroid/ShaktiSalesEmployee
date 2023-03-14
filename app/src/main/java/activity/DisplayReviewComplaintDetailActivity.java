package activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import activity.complaint.PendingComplainPhotoGridActivity;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class DisplayReviewComplaintDetailActivity extends AppCompatActivity {
    String userid, cmp_stand_category, remark_txt, msg_type, msg1, stand_remark_no, image_name = "", image_name1 = "", image_name2 = "", image_name3 = "", image_name4 = "", image_name5 = "", image_name6 = "", image_name7 = "", image_name8 = "", image_name9 = "", image_name10 = "", image_name11 = "", image_name12 = "", image_name13 = "", image_name14 = "";
    Context mContext;
    Spinner cmp_stand_remark;
    DatabaseHelper db;
    EditText remark;
    int index_cmp_category;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(DisplayReviewComplaintDetailActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    TextView cmp_code, cmp_no, cmp_customer_name, cmp_cust_no, mat_no, cmp_date, mat_grp, prod_date, serial_no, item_no, prod_plant, cmp_cls_date, reason, cls_reason, lst_action, mat_desc, save, cmp_images;
    String cmp_code_txt, cmp_no_txt, cust_name_txt, cust_no_txt, mat_no_txt, cmp_date_txt, mat_grp_txt, prod_date_txt, serial_no_txt, item_no_txt, prod_plant_txt, cmp_cls_date_txt, reason_txt, cls_reason_txt, lst_action_txt, mat_desc_txt;
    private Toolbar mToolbar;
    private String mUserID, obj;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_review_complaint_detail);


        mContext = this;

        progressDialog = new ProgressDialog(mContext);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        userid = LoginBean.getUseid();

        db = new DatabaseHelper(mContext);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        cmp_code_txt = bundle.getString("company_code");
        cmp_no_txt = bundle.getString("comp_no");
        Log.e("cmpno", "%%%%" + cmp_no_txt);
        cust_name_txt = bundle.getString("dealer_name");
        cust_no_txt = bundle.getString("dealer_no");
        mat_no_txt = bundle.getString("mat_no");
        cmp_date_txt = bundle.getString("comp_date");
        mat_grp_txt = bundle.getString("mat_grp");
        prod_date_txt = bundle.getString("prod_date");
        serial_no_txt = bundle.getString("sear_no");
        item_no_txt = bundle.getString("item_no");
        prod_plant_txt = bundle.getString("prod_plant");
        cmp_cls_date_txt = bundle.getString("comp_cls_date");
        reason_txt = bundle.getString("reason");
        cls_reason_txt = bundle.getString("close_reason");
        lst_action_txt = bundle.getString("last_action");
        mat_grp_txt = bundle.getString("mat_grp");
        mat_desc_txt = bundle.getString("mat_des");

        getLayout();
        setData();

        getSupportActionBar().setTitle("Complaint No :" + " " + cmp_no_txt);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomUtility.isOnline(mContext)) {
                    // Write Your Code What you want to do

                    checkDataValtidation();

                } else {
                    Toast.makeText(mContext, "No internet Connection ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cmp_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mIntent = new Intent(mContext, PendingComplainPhotoGridActivity.class);
                mIntent.putExtra("Complain_number",cmp_no_txt);
                mContext.startActivity(mIntent);

                /*if (CustomUtility.isOnline(mContext)) {
                    // Write Your Code What you want to do
                    if (db.isEmpty(db.TABLE_REVIEW_COMPLAINT_IMAGES)) {
                        ReviewComplaintImageList();
                    } else {
                        if (db.isRecordExist(db.TABLE_REVIEW_COMPLAINT_IMAGES, db.KEY_CMPNO, cmp_no_txt)) {

                            Intent intent = new Intent(mContext, ReviewCmpImageListActivity.class);
                            intent.putExtra("cmpno", cmp_no_txt);
                            startActivity(intent);
                        } else {
                            ReviewComplaintImageList();
                        }
                    }

                } else {

                    if (db.isRecordExist(db.TABLE_REVIEW_COMPLAINT_IMAGES, db.KEY_CMPNO, cmp_no_txt)) {

                        Intent intent = new Intent(mContext, ReviewCmpImageListActivity.class);
                        intent.putExtra("cmpno", cmp_no_txt);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });

        cmp_stand_remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {

                index_cmp_category = arg0.getSelectedItemPosition();

                //    Log.d("cmp_category", "" + index_cmp_category);
                cmp_stand_category = cmp_stand_remark.getSelectedItem().toString();

                if (cmp_stand_category.equalsIgnoreCase("Wrong Selection")) {
                    stand_remark_no = "1";
                } else if (cmp_stand_category.equalsIgnoreCase("Pump / Motor details not available in complaint")) {
                    stand_remark_no = "2";
                } else if (cmp_stand_category.equalsIgnoreCase("Goodwill claim")) {
                    stand_remark_no = "3";
                } else if (cmp_stand_category.equalsIgnoreCase("Customer Fault")) {
                    stand_remark_no = "4";
                } else if (cmp_stand_category.equalsIgnoreCase("Field Issue")) {
                    stand_remark_no = "5";
                } else if (cmp_stand_category.equalsIgnoreCase("Manufacturing Issue")) {
                    stand_remark_no = "6";
                } else if (cmp_stand_category.equalsIgnoreCase("Wrong Defect type mentioned by service team")) {
                    stand_remark_no = "7";
                } else if (cmp_stand_category.equalsIgnoreCase("Wrong attachments")) {
                    stand_remark_no = "8";
                } else if (cmp_stand_category.equalsIgnoreCase("Incomplete Information")) {
                    stand_remark_no = "9";
                } else if (cmp_stand_category.equalsIgnoreCase("Complaint closed by service team")) {
                    stand_remark_no = "10";
                } else if (cmp_stand_category.equalsIgnoreCase("Pump set fall down in Bore well")) {
                    stand_remark_no = "12";
                } else if (cmp_stand_category.equalsIgnoreCase("Pumpset operate in wrong application")) {
                    stand_remark_no = "13";
                } else if (cmp_stand_category.equalsIgnoreCase("Service center bill not attached")) {
                    stand_remark_no = "14";
                } else if (cmp_stand_category.equalsIgnoreCase("Customer bill not attached")) {
                    stand_remark_no = "15";
                } else if (cmp_stand_category.equalsIgnoreCase("Approval not attached")) {
                    stand_remark_no = "16";
                } else if (cmp_stand_category.equalsIgnoreCase("Photo not attached")) {
                    stand_remark_no = "17";
                } else if (cmp_stand_category.equalsIgnoreCase("Serial number photo not attached")) {
                    stand_remark_no = "18";
                } else if (cmp_stand_category.equalsIgnoreCase("Damage parts photo not attached")) {
                    stand_remark_no = "19";
                } else if (cmp_stand_category.equalsIgnoreCase("Product photo not attached")) {
                    stand_remark_no = "20";
                } else if (cmp_stand_category.equalsIgnoreCase("Complain closed with wrong amount")) {
                    stand_remark_no = "21";
                } else if (cmp_stand_category.equalsIgnoreCase("Out of warranty claim")) {
                    stand_remark_no = "22";
                } else if (cmp_stand_category.equalsIgnoreCase("Product serial number differ from Complain")) {
                    stand_remark_no = "23";
                } else if (cmp_stand_category.equalsIgnoreCase("Excess amount passed")) {
                    stand_remark_no = "24";
                } else if (cmp_stand_category.equalsIgnoreCase("Controller Issue")) {
                    stand_remark_no = "25";
                } else if (cmp_stand_category.equalsIgnoreCase("Parameter issue")) {
                    stand_remark_no = "26";
                } else if (cmp_stand_category.equalsIgnoreCase("Solar Structure / Module  Damaged")) {
                    stand_remark_no = "27";
                } else if (cmp_stand_category.equalsIgnoreCase("ALL PART DAMAGE DUE TO FOREIGN PARTICLE INSERT / MUD / SAND")) {
                    stand_remark_no = "28";
                } else if (cmp_stand_category.equalsIgnoreCase("CORRUGATED SPRING DAMAGED/WORNOUT/DEFORMED")) {
                    stand_remark_no = "29";
                } else if (cmp_stand_category.equalsIgnoreCase("COUPLING NOT TIGHTENED")) {
                    stand_remark_no = "30";
                } else if (cmp_stand_category.equalsIgnoreCase("ELECTRIC CONNECTION LOOSE /IMPROPER")) {
                    stand_remark_no = "31";
                } else if (cmp_stand_category.equalsIgnoreCase("CONECTOR DAMAGE /LOOSE")) {
                    stand_remark_no = "32";
                } else if (cmp_stand_category.equalsIgnoreCase("IMBALANCE  COUPLING")) {
                    stand_remark_no = "33";
                } else if (cmp_stand_category.equalsIgnoreCase("IMPROPER HANDLING IN TRANSIT AT DEALER / CUSOMER END / SERVI")) {
                    stand_remark_no = "34";
                } else if (cmp_stand_category.equalsIgnoreCase("IMPROPER INSTALLTION")) {
                    stand_remark_no = "35";
                } else if (cmp_stand_category.equalsIgnoreCase("IMPROPER PACKING")) {
                    stand_remark_no = "36";
                } else if (cmp_stand_category.equalsIgnoreCase("LEAKAGE FROM GASKET/ FLANGE/SUCTION /DISCHARGE PIPE /NRV")) {
                    stand_remark_no = "37";
                } else if (cmp_stand_category.equalsIgnoreCase("LOOSE STUD / LOOSE BOLT /NUT LOOSE")) {
                    stand_remark_no = "38";
                } else if (cmp_stand_category.equalsIgnoreCase("MATERIAL DEFECT")) {
                    stand_remark_no = "39";
                } else if (cmp_stand_category.equalsIgnoreCase("MOTOR BEARING FAILURE DUE TO WATER INSERTION")) {
                    stand_remark_no = "40";
                } else if (cmp_stand_category.equalsIgnoreCase("MOTOR ROTATION AGAINST THE PRESCRIBED")) {
                    stand_remark_no = "41";
                } else if (cmp_stand_category.equalsIgnoreCase("MOTOR TRIPPING DUE TO LOW VOLTAGE")) {
                    stand_remark_no = "42";
                } else if (cmp_stand_category.equalsIgnoreCase("NEGATIVE SUCTION")) {
                    stand_remark_no = "43";
                } else if (cmp_stand_category.equalsIgnoreCase("NO FOOT VALVE RESULTING INTO FORIGN PARTCLE INSERTION")) {
                    stand_remark_no = "44";
                } else if (cmp_stand_category.equalsIgnoreCase("NON STANARD / NON PRESCRIBED PIPING")) {
                    stand_remark_no = "45";
                } else if (cmp_stand_category.equalsIgnoreCase("NOT MAINTAINED D10/D5")) {
                    stand_remark_no = "46";
                } else if (cmp_stand_category.equalsIgnoreCase("ON/ OFF SWITCH NOT WORKING")) {
                    stand_remark_no = "47";
                } else if (cmp_stand_category.equalsIgnoreCase("OUR PUMP SET WORKING FINE PROBLEM RELATED TO CUSTOMER /SITE")) {
                    stand_remark_no = "48";
                } else if (cmp_stand_category.equalsIgnoreCase("OUTER PIPE DAMAGE /LEAKAGE FROM OUTER SEAM LINE")) {
                    stand_remark_no = "49";

                } else if (cmp_stand_category.equalsIgnoreCase("PART MISSING")) {
                    stand_remark_no = "50";
                } else if (cmp_stand_category.equalsIgnoreCase("PIPE LINE CHOCK")) {
                    stand_remark_no = "51";
                } else if (cmp_stand_category.equalsIgnoreCase("PIPE SIZE IS LESS THAN SUCTION /DISCHARGE HOLE SIZE")) {
                    stand_remark_no = "52";
                } else if (cmp_stand_category.equalsIgnoreCase("PRESSURE SWITCH PROBLEM")) {
                    stand_remark_no = "53";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP INSTALLED WITHOUT VIBRATION PAD")) {
                    stand_remark_no = "54";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP JAM DUE TO FOREIGN PARTICLE INSERT / MUD / SAND")) {
                    stand_remark_no = "55";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP NOISE / NOISY DUE TO CAVITATION")) {
                    stand_remark_no = "56";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP RUN WITH DOWN FLOAT /FLOT NOT MAINTAINED")) {
                    stand_remark_no = "57";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP VIBRATION")) {
                    stand_remark_no = "58";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMP WORN OUT /MOTOR WORN OUT/OUT OF WARRANTY")) {
                    stand_remark_no = "59";
                } else if (cmp_stand_category.equalsIgnoreCase("PUMPED FLUID BEYOND RECOMMENDED TEMPERATURE")) {
                    stand_remark_no = "60";
                } else if (cmp_stand_category.equalsIgnoreCase("RADIAL SLEEVE BROCKEN")) {
                    stand_remark_no = "61";
                } else if (cmp_stand_category.equalsIgnoreCase("REPAIRED AS GOODWILL WARANTY")) {
                    stand_remark_no = "62";
                } else if (cmp_stand_category.equalsIgnoreCase("START STOP MORE THEN PRESCRIBED")) {
                    stand_remark_no = "63";
                } else if (cmp_stand_category.equalsIgnoreCase("SUCTION LIFT TOO LARGE")) {
                    stand_remark_no = "64";
                } else if (cmp_stand_category.equalsIgnoreCase("UN RECOMMENDED FLUID BEING PUMPED")) {
                    stand_remark_no = "65";
                } else if (cmp_stand_category.equalsIgnoreCase("NOT COUNT CATERGORY /NOT READY TO SEND / PAY / OTHER")) {
                    stand_remark_no = "66";
                } else if (cmp_stand_category.equalsIgnoreCase("OK")) {
                    stand_remark_no = "98";
                } else if (cmp_stand_category.equalsIgnoreCase("Other")) {
                    stand_remark_no = "99";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void checkDataValtidation() {

        Log.e("cmpstndremark", "&&&" + cmp_stand_category);
        Log.e("remark_txt", "&&&" + remark_txt);

        remark_txt = remark.getText().toString();

        try {

            if (cmp_stand_category == null || cmp_stand_category.equalsIgnoreCase("") || cmp_stand_category.equalsIgnoreCase("Select Reason")) {

                Toast.makeText(mContext, "Please Select Standard Remark for Review Complaint.", Toast.LENGTH_SHORT).show();

            } else {

                if (cmp_stand_category.equalsIgnoreCase("other") && !TextUtils.isEmpty(remark_txt)) {
                    if (CustomUtility.isOnline(mContext)) {

                        SaveReviewComplaintList();

                    } else {
                        Toast.makeText(mContext, "No Internet Connection Found, You Are In Offline Mode.", Toast.LENGTH_SHORT).show();

                    }
                } else if (!cmp_stand_category.equalsIgnoreCase("other")) {
                    if (CustomUtility.isOnline(mContext)) {

                        SaveReviewComplaintList();

                    } else {
                        Toast.makeText(mContext, "No Internet Connection Found, You Are In Offline Mode.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(mContext, "Please Enter Remark for Submit Review Complaint.", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }


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

    public void getLayout() {

        cmp_code = (TextView) findViewById(R.id.cmp_code);
        cmp_no = (TextView) findViewById(R.id.cmp_no);
        cmp_customer_name = (TextView) findViewById(R.id.cmp_customer_name);
        cmp_cust_no = (TextView) findViewById(R.id.cmp_cust_no);
        mat_no = (TextView) findViewById(R.id.mat_no);
        cmp_date = (TextView) findViewById(R.id.cmp_date);
        mat_grp = (TextView) findViewById(R.id.mat_grp);
        prod_date = (TextView) findViewById(R.id.prod_date);
        serial_no = (TextView) findViewById(R.id.serial_no);
        item_no = (TextView) findViewById(R.id.item_no);
        prod_plant = (TextView) findViewById(R.id.prod_plant);
        cmp_cls_date = (TextView) findViewById(R.id.cmp_cls_date);
        reason = (TextView) findViewById(R.id.reason);
        cls_reason = (TextView) findViewById(R.id.cls_reason);
        lst_action = (TextView) findViewById(R.id.lst_action);
        mat_desc = (TextView) findViewById(R.id.mat_desc);

        cmp_images = (TextView) findViewById(R.id.cmp_images);


        save = (TextView) findViewById(R.id.save);


        cmp_stand_remark = (Spinner) findViewById(R.id.cmp_stand_remark);

        remark = (EditText) findViewById(R.id.remark);

    }

    public void setData() {

        cmp_code.setText(cmp_code_txt);
        cmp_no.setText(cmp_no_txt);
        cmp_customer_name.setText(cust_name_txt);
        cmp_cust_no.setText(cust_no_txt);
        mat_no.setText(mat_no_txt);
        cmp_date.setText(cmp_date_txt);
        mat_grp.setText(mat_grp_txt);
        prod_date.setText(prod_date_txt);
        serial_no.setText(serial_no_txt);
        item_no.setText(item_no_txt);
        prod_plant.setText(prod_plant_txt);
        cmp_cls_date.setText(cmp_cls_date_txt);
        reason.setText(reason_txt);
        cls_reason.setText(cls_reason_txt);
        lst_action.setText(lst_action_txt);
        mat_desc.setText(mat_desc_txt);

    }


    private void SaveReviewComplaintList() {

        mUserID = LoginBean.getUseid();

        try {

            progressDialog = ProgressDialog.show(mContext, "Loading...", "please wait, Data Send to Server !");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair("pernr", mUserID));
                    param.add(new BasicNameValuePair("objs", CustomUtility.getSharedPreferences(mContext,"objs")));
                    param.add(new BasicNameValuePair("std_reason", stand_remark_no));
                    param.add(new BasicNameValuePair("posnr", item_no_txt));
                    param.add(new BasicNameValuePair("remark", remark_txt));
                    param.add(new BasicNameValuePair("cmpno", cmp_no_txt));
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        obj = CustomHttpClient.executeHttpPost1(WebURL.SAVE_REVIEW_COMPLAINT, param);

                        Log.e("DATA", "&&&&" + obj);

                        if (obj != "") {

                            final JSONArray jo_success = new JSONArray(obj);

                            for (int i = 0; i < jo_success.length(); i++) {


                                JSONObject jo = jo_success.getJSONObject(i);

                                msg_type = jo.getString("MSGTYP");
                                msg1 = jo.getString("MSG");

                                if (msg_type.equalsIgnoreCase("S")) {
                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Message msg = new Message();
                                            msg.obj = msg1;
                                            mHandler.sendMessage(msg);
                                        }
                                    });

                                    db.deleteReviewCmpImages(cmp_no_txt);

                                    finish();
                                } else if (msg_type.equalsIgnoreCase("E")) {
                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Message msg = new Message();
                                            msg.obj = msg1;
                                            mHandler.sendMessage(msg);
                                        }
                                    });

                                }

                            }

                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                                }

                            });
                        }
                    } catch (Exception e) {

                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        e.printStackTrace();

                    }
//
                }
            }).start();


        } catch (Exception e) {

              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

        }

    }



}
