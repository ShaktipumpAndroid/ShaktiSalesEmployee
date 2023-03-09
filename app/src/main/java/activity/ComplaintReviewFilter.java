package activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import adapter.Adapter_report_list;
import bean.LoginBean;
import webservice.CustomHttpClient;
import webservice.NotifyInterface;
import webservice.WebURL;

public class ComplaintReviewFilter extends AppCompatActivity {
    Context context;
    RecyclerView complaint_list;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    Adapter_report_list adapterEmployeeList;
    ArrayList<JSONArray> ja_success = new ArrayList<JSONArray>();
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManagerSubCategory;

    private TextView save;
    private String mUserID, obj;
    private EditText review_from_date, review_to_date, plant_code, complaint_no;
    private String mReviewFromDate, mReviewToDate, mPlantCode, mComplaintNo;

    private String cmp_no_txt, fromdate_txt, todate_txt, plant_code_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_filter);
        context = this;

      /*  Bundle bundle = getIntent().getExtras();
        cmp_no_txt = bundle.getString("cmpno");`
        plant_code_txt = bundle.getString("plantcode");
        fromdate_txt = bundle.getString("fromdate");
        todate_txt = bundle.getString("todate");
*/
        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(context);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review Complaint");


        getLayout();

        review_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        review_from_date.setText(i2 + "/" + i1 + "/" + i);
                        mReviewFromDate = review_from_date.getText().toString().trim();
                        parseDateToddMMyyyy(mReviewFromDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Completion Date");
                datePickerDialog.show();
            }
        });

        review_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        review_to_date.setText(i2 + "/" + i1 + "/" + i);
                        mReviewToDate = review_to_date.getText().toString().trim();
                        parseDateToddMMyyyy1(mReviewToDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Completion Date");
                datePickerDialog.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomUtility.isOnline(context)) {
                    // Write Your Code What you want to do
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        checkDataValtidation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "No internet Connection ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void checkDataValtidation() {

        mReviewFromDate = review_from_date.getText().toString();
        mReviewToDate = review_to_date.getText().toString();
        mPlantCode = plant_code.getText().toString();
        mComplaintNo = complaint_no.getText().toString().toUpperCase();

        try {
            if (mReviewFromDate == null || mReviewFromDate.equalsIgnoreCase("") || mReviewFromDate.equalsIgnoreCase(null)) {

                review_from_date.setFocusable(true);
                review_from_date.requestFocus();

                if (progressDialog != null)
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                ;

                Toast.makeText(context, "Please Enter From Date for Review Complaint List.", Toast.LENGTH_SHORT).show();


            } else if (mReviewToDate == null || mReviewToDate.equalsIgnoreCase("") || mReviewToDate.equalsIgnoreCase(null)) {
                review_to_date.setFocusable(true);
                review_to_date.requestFocus();

                if (progressDialog != null)
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                ;


                Toast.makeText(context, "Please Enter To date for Review Complaint List.", Toast.LENGTH_SHORT).show();


            } else {

                if (CustomUtility.isOnline(context)) {
                    GetReviewComplaintList();
                } else {
                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }


    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            fromdate_txt = outputFormat.format(date);
            Log.e("Out", "put" + fromdate_txt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            todate_txt = outputFormat.format(date);
            Log.e("In", "put" + todate_txt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    public void getLayout() {

        complaint_list = findViewById(R.id.complaint_list);

        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);


        save = (TextView) findViewById(R.id.done);

        review_from_date = (EditText) findViewById(R.id.review_date);
        review_to_date = (EditText) findViewById(R.id.review_date1);
        plant_code = (EditText) findViewById(R.id.plant_code);
        complaint_no = (EditText) findViewById(R.id.complaint_no);

        complaint_no.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        save = (TextView) findViewById(R.id.done);
        review_from_date.setFocusable(false);
        review_to_date.setFocusable(false);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (CustomUtility.isOnline(context)) {
            // Write Your Code What you want to do
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);

                checkDataValtidation();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, "No internet Connection ", Toast.LENGTH_SHORT).show();
        }

    }


    private void GetReviewComplaintList() {

        mUserID = LoginBean.getUseid();

        mPlantCode = plant_code.getText().toString();
        mComplaintNo = complaint_no.getText().toString().toUpperCase();

        try {

            progressDialog = ProgressDialog.show(context, "Loading...", "please wait !");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair("pernr", mUserID));
                    param.add(new BasicNameValuePair("objs", CustomUtility.getSharedPreferences(context, "objs")));
                    param.add(new BasicNameValuePair("fromdate", fromdate_txt));
                    param.add(new BasicNameValuePair("todate", todate_txt));
                    param.add(new BasicNameValuePair("plant", mPlantCode));
                    param.add(new BasicNameValuePair("cmpno", mComplaintNo));
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        obj = CustomHttpClient.executeHttpPost1(WebURL.REVIEW_COMPLAINT, param);

                        Log.e("DATA", "&&&&" + obj);

                        if (obj != "") {

                            if ((progressDialog != null) && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
                            final JSONArray jo_success = new JSONArray(obj);

                            if (jo_success.length() > 0) {

                                ja_success.clear();

                                for (int i = 0; i < jo_success.length(); i++) {
                                    Log.e("SIZE", "&&&&" + jo_success.length());


                                    ja_success.add(jo_success);


                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            lin1.setVisibility(View.VISIBLE);
                                            complaint_list.setVisibility(View.VISIBLE);
                                            lin2.setVisibility(View.GONE);

                                            adapterEmployeeList = new Adapter_report_list(context, ja_success, new NotifyInterface() {
                                                @Override
                                                public void onSuccessNotify() {
                                                    adapterEmployeeList.notifyDataSetChanged();
                                                }
                                            });
                                            layoutManagerSubCategory = new LinearLayoutManager(context);
                                            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                                            complaint_list.setLayoutManager(layoutManagerSubCategory);
                                            complaint_list.setAdapter(adapterEmployeeList);
                                            adapterEmployeeList.notifyDataSetChanged();

                                        }

                                    });


                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        if ((progressDialog != null) && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                            progressDialog = null;
                                        }
                                        ;
                                        lin1.setVisibility(View.GONE);
                                        complaint_list.setVisibility(View.GONE);
                                        lin2.setVisibility(View.VISIBLE);

                                    }

                                });
                            }

                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    if ((progressDialog != null) && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                    ;
                                    lin1.setVisibility(View.GONE);
                                    complaint_list.setVisibility(View.GONE);
                                    lin2.setVisibility(View.VISIBLE);

                                }

                            });
                        }
                    } catch (Exception e) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        e.printStackTrace();

                    }
//
                }
            }).start();


        } catch (Exception e) {

            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            ;

        }

    }

}

