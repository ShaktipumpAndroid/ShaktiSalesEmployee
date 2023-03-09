package activity.complaint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import activity.ComplaintReviewFilter;
import activity.CustomUtility;
import bean.LoginBean;
import database.DatabaseHelper;
import syncdata.SyncDataToSAP_New;
import webservice.SAPWebService;
import webservice.WebURL;


public class ComplaintDashboard extends AppCompatActivity {
    TextView btn_open_complaint, btn_review_complaint, btn_close_complaint, btn_new_servie_center, btn_old_servie_center,btn_cmpln_approved,
            btn_new_complaint_notification, btn_delar_pend_notification, btn_pending_complaint_notification,
            btn_closure_notification, btn_closer_request,btn_pending_for_app,btn_await_for_app, btn_pending_complaint,
            btn_delar_pend_cmp,btn_pending_for_app_notification,btn_await_for_app_notification,btn_cmpln_approved_notification, txt_complaint_Final_Approval;
    SAPWebService con = null;
    DatabaseHelper dataHelper = null;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ComplaintDashboard.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_dashboard);
        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(context);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Management");


        dataHelper = new DatabaseHelper(this);
        btn_open_complaint = (TextView) findViewById(R.id.open_complaint);
        btn_review_complaint = (TextView) findViewById(R.id.review_complaint);
        btn_close_complaint = (TextView) findViewById(R.id.close_complaint);
        btn_pending_complaint = (TextView) findViewById(R.id.pending_complaint);
        btn_closer_request = (TextView) findViewById(R.id.closer_request);
        btn_delar_pend_cmp = (TextView) findViewById(R.id.delar_pend_cmp);
        btn_pending_for_app = (TextView) findViewById(R.id.pending_for_app);
        btn_await_for_app = (TextView) findViewById(R.id.await_for_app);

        //btn_new_servie_center = (TextView) findViewById(R.id.new_service_center);
        btn_old_servie_center = (TextView) findViewById(R.id.old_service_center);
        btn_cmpln_approved = (TextView) findViewById(R.id.cmpln_approved1);


        btn_new_complaint_notification = (TextView) findViewById(R.id.new_complaint_notification);
        btn_delar_pend_notification   =(TextView) findViewById(R.id.delar_pend_notification);
        btn_pending_complaint_notification = (TextView) findViewById(R.id.pending_complaint_notification);
        btn_closure_notification = (TextView) findViewById(R.id.closure_notification);
        btn_pending_for_app_notification = (TextView) findViewById(R.id.pending_for_app_notification);
        btn_await_for_app_notification = (TextView) findViewById(R.id.await_for_app_notification);
        btn_cmpln_approved_notification = (TextView) findViewById(R.id.cmpln_approved_notification);
        txt_complaint_Final_Approval = (TextView) findViewById(R.id.txt_complaint_Final_Approval);

        con = new SAPWebService();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("key_username", "userid");


        Log.d("d_key", ""+ pref.getString("key_download_complaint","date"));
        if (!pref.getString("key_download_complaint", "date").equalsIgnoreCase(new CustomUtility().getCurrentDate())) {
            download_complaint_data();
        }


        btn_open_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewComplaint();
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, ComplaintSearchActivity.class);
                intent.putExtra("complaint", "New Complaint");
                startActivity(intent);
            }
        });

        btn_delar_pend_cmp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                getNewComplaint();
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, DealerPendCompActivity.class);
                intent.putExtra("complaint", "Dealer Complaint");
                startActivity(intent);
            }
        });


        btn_review_complaint.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                getNewComplaint();
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, ComplaintReviewFilter.class);
                intent.putExtra("complaint", "Review Complaint");
                startActivity(intent);
            }
        });


//        btn_close_complaint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getCloseComplaint();
//
//                Intent intent = new Intent(context, ClosedComplaintActivity.class);
//                intent.putExtra("complaint","Closed Complaint");
//                startActivity(intent);
//            }
//        });


        btn_pending_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // complaint count
                getPendingComplaint();
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, PendingComplaintActivity.class);

                startActivity(intent);
            }
        });

        btn_closer_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getClosureComplaint();
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, ClosureComplaintActivity.class);
                startActivity(intent);
            }
        });


        btn_old_servie_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

       /* btn_new_servie_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, NewServiceCenterActivity.class);
                startActivity(intent);
            }
        });*/

        btn_cmpln_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebURL.IC_PHOTO_CHECK = 0;
                Intent intent = new Intent(context, ApprovedCompActivity.class);
                startActivity(intent);
            }
        });

        btn_pending_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebURL.IC_PHOTO_CHECK = 0;
             //   Intent intent = new Intent(context, PendingForApprovalActivity.class);
                Intent intent = new Intent(context, VKPendingForApprovalActivity.class);
                intent.putExtra("complaint", "Pending for Complaint");
                startActivity(intent);
            }
        });

        txt_complaint_Final_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebURL.MAIN_USER_ID = pref.getString("key_username", "userid");
                con.getCustomerComplaintVKFinal(ComplaintDashboard.this);
                WebURL.IC_PHOTO_CHECK = 1;
             //   Intent intent = new Intent(context, PendingForApprovalActivity.class);
                Intent intent = new Intent(context, VKPendingForApprovalFinalActivity.class);
                intent.putExtra("complaint", "Pending for Closure");
                startActivity(intent);
            }
        });

        btn_await_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebURL.MAIN_USER_ID = pref.getString("key_username", "userid");
                Intent intent = new Intent(context, AwaitingForApprovalActivity.class);
                intent.putExtra("complaint", "Awaiting for Complaint");
                startActivity(intent);
            }
        });



        //***************** set notifications count *********************************

        btn_new_complaint_notification.setText(String.valueOf(getNewComplaint()));
        if (getNewComplaint() <= 0) {
            btn_new_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_delar_pend_notification.setText(String.valueOf( getDealerpendComplaint() ));
       if (getDealerpendComplaint() <= 0) {
            btn_delar_pend_notification.setTextColor(Color.parseColor("#ffffff"));
      }

        btn_pending_complaint_notification.setText(String.valueOf(getPendingComplaint()));
        if (getPendingComplaint() <= 0) {
            btn_pending_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_closure_notification.setText(String.valueOf(getClosureComplaint()));
        if (getClosureComplaint() <= 0) {
            btn_closure_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_pending_for_app_notification.setText(String.valueOf(getPendingforappComplaint()));
        if (getPendingforappComplaint() <= 0) {
            btn_pending_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_await_for_app_notification.setText(String.valueOf(getAwaitforappComplaint()));
        if (getAwaitforappComplaint() <= 0) {
            btn_await_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_cmpln_approved_notification.setText(String.valueOf(getCmplnapproved()));
        if (getCmplnapproved() <= 0) {
            btn_cmpln_approved_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        //***************** end notifications count *********************************
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

            case R.id.action_download_complaint:

                download_complaint_data();
                return true;

            case R.id.action_download_service_center:
                download_service_center_data();
                return true;

            case R.id.action_sync_offline:
                // sync all data
                syncOfflineData();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public int getNewComplaint() {
        int tot_new_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

           selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "'"
                   + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                   + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                   + " AND "+ DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' "
                   + " AND "+ DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' ";


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            int tdDate = Integer.parseInt(df.format(c));

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                @SuppressLint("Range") String fdate = cursor.getString(cursor.getColumnIndex("fdate"));
                @SuppressLint("Range") String save = cursor.getString(cursor.getColumnIndex("save_by"));

                int fdate1 = 0;
                if (!TextUtils.isEmpty(fdate)) {
                    fdate1 = Integer.parseInt(CustomUtility.formateDate(fdate));
                }

                if (fdate1 <= tdDate  && TextUtils.isEmpty(save)) {

                    tot_new_complaint = tot_new_complaint + 1;
                    //count_dsr_entry = String.valueOf(cursor.getCount()) ;
                } else if (fdate.equalsIgnoreCase("00.00.0000")  && TextUtils.isEmpty(save)) {
                    tot_new_complaint = tot_new_complaint + 1;
                }
            }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_new_complaint;
    }

    public int getCloseComplaint() {
        int tot_close_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "CONFIRMED" + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_close_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_close_complaint;
    }

    public int getPendingComplaint() {
        int tot_pending_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        int tdDate = Integer.parseInt(df.format(c));


        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND "+ DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' "
                    + " AND "+ DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' ";


            cursor = db.rawQuery(selectQuery, null);
            // Log.d("cmp_pending",""+cursor.getCount());
            if (cursor.getCount() > 0) {
                 while (cursor.moveToNext()) {

                @SuppressLint("Range") String fdate = cursor.getString(cursor.getColumnIndex("fdate"));
                @SuppressLint("Range") String save = cursor.getString(cursor.getColumnIndex("save_by"));

                int fdate1 = 0;
                if (!TextUtils.isEmpty(fdate) ) {
                    fdate1 = Integer.parseInt(CustomUtility.formateDate(fdate));
                }

                if (fdate1 > tdDate || !TextUtils.isEmpty(save)) {

                    tot_pending_complaint = tot_pending_complaint + 1;
                }

            }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_pending_complaint;
    }


    public int getCmplnapproved() {
        int tot_pending_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        int tdDate = Integer.parseInt(df.format(c));


        try {

             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "' "
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " AND " + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'";


            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {

                tot_pending_complaint = tot_pending_complaint + 1;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_pending_complaint;
    }

    public int getClosureComplaint() {
        int tot_closure_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "CLOSURE" + "'";



            cursor = db.rawQuery(selectQuery, null);

            // Log.d("closed_cmp",""+cursor.getCount());

            if (cursor.getCount() > 0) {

                // tot_closure_complaint =   tot_closure_complaint + cursor.getCount() ;


                tot_closure_complaint = cursor.getCount();


            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_closure_complaint;
    }

    @Override
    public void onResume() {

        //***************** set notifications count *********************************
        btn_new_complaint_notification.setText(String.valueOf(getNewComplaint()));
        if (getNewComplaint() <= 0) {
            btn_new_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
        }


//        btn_close_complaint_notification.setText(String.valueOf( getCloseComplaint() ));
//        if (getCloseComplaint() <= 0) {
//            btn_close_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
//        }


        btn_pending_complaint_notification.setText(String.valueOf(getPendingComplaint()));
        if (getPendingComplaint() <= 0) {
            btn_pending_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
        }


        btn_closure_notification.setText(String.valueOf(getClosureComplaint()));
        if (getClosureComplaint() <= 0) {
            btn_closure_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_delar_pend_notification.setText(String.valueOf( getDealerpendComplaint() ));
        if (getDealerpendComplaint() <= 0) {
            btn_delar_pend_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_pending_for_app_notification.setText(String.valueOf(getPendingforappComplaint()));
        if (getPendingforappComplaint() <= 0) {
            btn_pending_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_await_for_app_notification.setText(String.valueOf(getAwaitforappComplaint()));
        if (getAwaitforappComplaint() <= 0) {
            btn_await_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }


        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complaint, menu);
        return true;
    }

    public void download_complaint_data() {

        progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Download Complaint...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {


                    try {

                        con.getCustomerComplaint(ComplaintDashboard.this);

                     //   con.getCustomerComplaintVKFinal(ComplaintDashboard.this );

                        editor.putString("key_download_complaint", new CustomUtility().getCurrentDate());
                        editor.commit(); //
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

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

    public void download_service_center_data() {

        progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Download Service Center List...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {
                    try {
                        con.getServiceCenterList(ComplaintDashboard.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
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

    public void syncOfflineData() {

        progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Sync Offline Data !");


        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {


                    dataHelper.insertEmployeeGPSActivity(
                            LoginBean.userid,
                            new CustomUtility().getCurrentDate(),
                            new CustomUtility().getCurrentTime(),
                            "1",
                            "0.0",
                            "0.0",
                            context,
                            "");


                    new SyncDataToSAP_New().SendDataToSap(context);

                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();


    }


    public void setUpdateClouserComplaint(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "CLOSURE" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if(db!=null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }


        }
    }


    public void setUpdatePendingComplaint(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "REPLY" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if(db!=null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }


        }
    }

    public int getDealerpendComplaint() {
        int tot_dela_pend_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " = '" + "07" + "'"
                    + " AND (" + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' )"
                    + " AND (" + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' )";



            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_dela_pend_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_dela_pend_complaint;
    }

    public int getPendingforappComplaint() {
        int tot_pend_for_app_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " != '" + "CLOSURE" + "'"
                    + " AND " + DatabaseHelper.KEY_PEND_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "Y" + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_pend_for_app_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_pend_for_app_complaint;


    }

    public int getAwaitforappComplaint() {

        int tot_await_for_app_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " != '" + "CLOSURE" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "Y" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_PERNR + " = '" + userid + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_await_for_app_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_await_for_app_complaint;


    }
}
