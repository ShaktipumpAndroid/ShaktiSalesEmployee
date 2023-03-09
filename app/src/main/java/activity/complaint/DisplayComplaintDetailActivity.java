package activity.complaint;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.HashSet;

import backgroundservice.SyncDataService;
import bean.LoginBean;
import database.DatabaseHelper;

public class DisplayComplaintDetailActivity extends AppCompatActivity {
    String userid;
    Context mContext;
    HashSet<String> hashSet = null;
    TextView cmp_reason_1, cmp_reason_2, cmp_reason_3, pending_complaint, btn_close_complaint,
            tv_cmp_category, tv_cmp_close_reason_1, tv_cmp_close_reason_2, tv_cmp_close_reason_3,
            tv_cmp_defect_type1, tv_cmp_defect_type2, tv_cmp_defect_type3,tv_cmp_relt_to1, tv_cmp_relt_to2, tv_cmp_relt_to3;
    String cmp_no = "", cmp_extwg_1 = "", cmp_extwg_2 = "", cmp_extwg_3 = "", cmp_category = "",

    /*cmp_payment_1, cmp_payment_2, cmp_payment_3,*/cmp_posnr1 = "", cmp_posnr2 = "", cmp_posnr3 = "",
            complaint_display = "";
    /*tv_cmp_payment_by_1,tv_cmp_payment_by_2,tv_cmp_payment_by_3*/
    Context mContex;
    EditText cmp_customer_name, cmp_warranty_1,
            cmp_address, cmp_matnr_1, cmp_maktx_1, cmp_sernr_1,
            cmp_warranty_2, cmp_matnr_2, cmp_maktx_2, cmp_sernr_2,
            cmp_warranty_3, cmp_matnr_3, cmp_maktx_3, cmp_sernr_3, pay_by_customer_1, pay_by_dealer_1,
            pay_by_company_1, pay_freelancer_1, pay_freelancer_2, pay_freelancer_3,
            pay_by_customer_2, pay_by_dealer_2, pay_by_company_2,
            pay_by_customer_3, pay_by_dealer_3, pay_by_company_3, pay_by_dea_1, pay_by_com_1, pay_by_dea_2, pay_by_com_2, pay_by_dea_3, pay_by_com_3;
    LinearLayout Layout_item_2, Layout_item_3;
    EditText bill_no_1,bill_no_2,bill_no_3;
    EditText bill_dt_1,bill_dt_2,bill_dt_3;
    EditText warn_cond_1,warn_cond_2,warn_cond_3;
    EditText warn_exp_dt_1,warn_exp_dt_2,warn_exp_dt_3;
    EditText insu_txt_1,insu_txt_2,insu_txt_3;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_complaint_detail);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        userid = LoginBean.getUseid();


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Layout_item_2 = (LinearLayout) this.findViewById(R.id.item_2);
        Layout_item_3 = (LinearLayout) this.findViewById(R.id.item_3);

        hashSet = new HashSet<String>();


        pending_complaint = (TextView) findViewById(R.id.pending_complaint);
        btn_close_complaint = (TextView) findViewById(R.id.close_complaint);


        cmp_customer_name = (EditText) findViewById(R.id.cmp_customer_name);
        cmp_address = (EditText) findViewById(R.id.cmp_address);


        bill_no_1 = (EditText) findViewById(R.id.bill_no_1);
        bill_no_2 = (EditText) findViewById(R.id.bill_no_2);
        bill_no_3 = (EditText) findViewById(R.id.bill_no_3);


        bill_dt_1 = (EditText) findViewById(R.id.bill_dt_1);
        bill_dt_2 = (EditText) findViewById(R.id.bill_dt_2);
        bill_dt_3 = (EditText) findViewById(R.id.bill_dt_3);

        warn_cond_1 = (EditText) findViewById(R.id.warn_cond_1);
        warn_cond_2 = (EditText) findViewById(R.id.warn_cond_2);
        warn_cond_3 = (EditText) findViewById(R.id.warn_cond_3);

        warn_exp_dt_1 = (EditText) findViewById(R.id.warn_exp_dt_1);
        warn_exp_dt_2 = (EditText) findViewById(R.id.warn_exp_dt_2);
        warn_exp_dt_3 = (EditText) findViewById(R.id.warn_exp_dt_3);

        insu_txt_1 = (EditText) findViewById(R.id.insu_txt_1);
        insu_txt_2 = (EditText) findViewById(R.id.insu_txt_2);
        insu_txt_3 = (EditText) findViewById(R.id.insu_txt_3);


        pay_by_customer_1 = (EditText) findViewById(R.id.pay_by_customer_1);
        pay_by_dealer_1 = (EditText) findViewById(R.id.pay_by_dealer_1);
        pay_by_company_1 = (EditText) findViewById(R.id.pay_by_company_1);
        pay_freelancer_1 = (EditText) findViewById(R.id.pay_to_freelancer_1);
        pay_by_dea_1 = (EditText) findViewById(R.id.pay_by_dea_1);
        pay_by_com_1 = (EditText) findViewById(R.id.pay_by_com_1);


        pay_by_customer_2 = (EditText) findViewById(R.id.pay_by_customer_2);
        pay_by_dealer_2 = (EditText) findViewById(R.id.pay_by_dealer_2);
        pay_by_company_2 = (EditText) findViewById(R.id.pay_by_company_2);
        pay_freelancer_2 = (EditText) findViewById(R.id.pay_to_freelancer_2);
        pay_by_dea_2 = (EditText) findViewById(R.id.pay_by_dea_2);
        pay_by_com_2 = (EditText) findViewById(R.id.pay_by_com_2);

        pay_by_customer_3 = (EditText) findViewById(R.id.pay_by_customer_3);
        pay_by_dealer_3 = (EditText) findViewById(R.id.pay_by_dealer_3);
        pay_by_company_3 = (EditText) findViewById(R.id.pay_by_company_3);
        pay_freelancer_3 = (EditText) findViewById(R.id.pay_to_freelancer_3);
        pay_by_dea_3 = (EditText) findViewById(R.id.pay_by_dea_3);
        pay_by_com_3 = (EditText) findViewById(R.id.pay_by_com_3);


        tv_cmp_category = (TextView) findViewById(R.id.cmp_category);
       /* tv_cmp_payment_by_1   = (TextView) findViewById(R.id.cmp_payment_by_1 );
        tv_cmp_payment_by_2   = (TextView) findViewById(R.id.cmp_payment_by_2 );
        tv_cmp_payment_by_3   = (TextView) findViewById(R.id.cmp_payment_by_3 );*/


        tv_cmp_defect_type1 = (TextView) findViewById(R.id.cmp_defect_type_1);
        tv_cmp_defect_type2 = (TextView) findViewById(R.id.cmp_defect_type_2);
        tv_cmp_defect_type3 = (TextView) findViewById(R.id.cmp_defect_type_3);


        tv_cmp_relt_to1 = (TextView) findViewById(R.id.cmp_relt_to_1);
        tv_cmp_relt_to2 = (TextView) findViewById(R.id.cmp_relt_to_2);
        tv_cmp_relt_to3 = (TextView) findViewById(R.id.cmp_relt_to_3);


        tv_cmp_close_reason_1 = (TextView) findViewById(R.id.cmp_close_reason_1);
        tv_cmp_close_reason_2 = (TextView) findViewById(R.id.cmp_close_reason_2);
        tv_cmp_close_reason_3 = (TextView) findViewById(R.id.cmp_close_reason_3);


        cmp_matnr_1 = (EditText) findViewById(R.id.material_code_1);
        cmp_maktx_1 = (EditText) findViewById(R.id.material_name_1);
        cmp_sernr_1 = (EditText) findViewById(R.id.serial_no_1);
        cmp_reason_1 = (TextView) findViewById(R.id.cmp_reason_1);
        cmp_warranty_1 = (EditText) findViewById(R.id.warranty_1);

        cmp_matnr_2 = (EditText) findViewById(R.id.material_code_2);
        cmp_maktx_2 = (EditText) findViewById(R.id.material_name_2);
        cmp_reason_2 = (TextView) findViewById(R.id.cmp_reason_2);
        cmp_sernr_2 = (EditText) findViewById(R.id.serial_no_2);
        cmp_warranty_2 = (EditText) findViewById(R.id.warranty_2);

        cmp_matnr_3 = (EditText) findViewById(R.id.material_code_3);
        cmp_maktx_3 = (EditText) findViewById(R.id.material_name_3);
        cmp_sernr_3 = (EditText) findViewById(R.id.serial_no_3);
        cmp_reason_3 = (TextView) findViewById(R.id.cmp_reason_3);
        cmp_warranty_3 = (EditText) findViewById(R.id.warranty_3);


        Bundle bundle = getIntent().getExtras();

        cmp_no = bundle.getString("cmpno");

        complaint_display = bundle.getString("complaint_display");


        getSupportActionBar().setTitle("Complaint No :" + " " + cmp_no);


        getComplaintHeader();
        getComplaintDetail();


        if (complaint_display.equalsIgnoreCase("serail_no")) {
            getSerialNumberHeader();
            getSerialNumberDetail();
        }


        if (complaint_display.equalsIgnoreCase("clouser")) {
            getClouserComplaintDetail();
        }


        if (complaint_display.equalsIgnoreCase("closed")) {
            getClosedComplaint();
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

    public void getComplaintHeader() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";


            cursor = db.rawQuery(selectQuery, null);

            //   Log.d("comp_dtl",""+cursor.getCount() + "--" + cmp_no);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));


                    cmp_customer_name.setText(cursor.getString(cursor.getColumnIndex("customer_name")));


                    cmp_address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    tv_cmp_category.setText(cursor.getString(cursor.getColumnIndex("category")));


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }

    }

    public void getSerialNumberHeader() {

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SERAIL_NUMBER_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";


            cursor = db.rawQuery(selectQuery, null);

            //   Log.d("comp_dtl",""+cursor.getCount() + "--" + cmp_no);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    //   Log.d("video",cursor.getString(cursor.getColumnIndex("video_name")));


                    cmp_customer_name.setText(cursor.getString(cursor.getColumnIndex("customer_name")));


                    cmp_address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    tv_cmp_category.setText(cursor.getString(cursor.getColumnIndex("category")));


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }

    }


    public void getClosedComplaint() {


        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDTL
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);

            //  Log.d("comp_dtl1",""+cursor.getCount() + "--" + cmp_no);

            if (cursor.getCount() > 0) {


                if (cursor.getCount() == 1) {
                    Layout_item_2.setVisibility(LinearLayout.GONE);
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }

                if (cursor.getCount() == 2) {
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }


                while (cursor.moveToNext()) {

                    index = index + 1;


                    if (index == 1) {
                        cmp_posnr1 = cursor.getString(cursor.getColumnIndex("posnr"));

                        tv_cmp_defect_type1.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_defect_type1.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                        //tv_cmp_payment_by_1.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));

                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));

                        bill_no_1.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_1.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_1.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_1.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_1.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                    if (index == 2) {
                        cmp_posnr2 = cursor.getString(cursor.getColumnIndex("posnr"));


                        tv_cmp_defect_type2.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_2.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        //tv_cmp_payment_by_2.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));

                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));

                        bill_no_2.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_2.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_2.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_2.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_2.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                    if (index == 3) {
                        cmp_posnr3 = cursor.getString(cursor.getColumnIndex("posnr"));

                        tv_cmp_defect_type3.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_3.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));

                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));

                        bill_no_3.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_3.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_3.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_3.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_3.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }


    }

    public void getComplaintDetail() {


        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDTL
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {


                if (cursor.getCount() == 1) {
                    Layout_item_2.setVisibility(LinearLayout.GONE);
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }

                if (cursor.getCount() == 2) {
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }


                while (cursor.moveToNext()) {

                    //  Log.d("comp_no",""+cursor.getString(cursor.getColumnIndex("cmpno")) + "--" + cmp_no);


                    index = index + 1;


                    if (index == 1) {
                        cmp_posnr1 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_1.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_1.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_1.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_1.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_1.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_1 = cursor.getString(cursor.getColumnIndex("extwg"));

                        tv_cmp_defect_type1.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_1.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));

                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));

                        bill_no_1.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_1.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_1.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_1.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_1.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                    if (index == 2) {
                        cmp_posnr2 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_2.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_2.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_2.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_2.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_2.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_2 = cursor.getString(cursor.getColumnIndex("extwg"));

                        tv_cmp_defect_type2.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_2.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));

                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));

                        bill_no_2.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_2.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_2.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_2.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_2.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                    if (index == 3) {
                        cmp_posnr3 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_3.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_3.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_3.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_3.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_3.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_3 = cursor.getString(cursor.getColumnIndex("extwg"));


                        tv_cmp_defect_type3.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_3.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        bill_no_3.setText(cursor.getString(cursor.getColumnIndex("vbeln")));
                        bill_dt_3.setText(cursor.getString(cursor.getColumnIndex("fkdat")));
                        insu_txt_3.setText(cursor.getString(cursor.getColumnIndex("insurance_txt")));
                        warn_cond_3.setText(cursor.getString(cursor.getColumnIndex("warranty_condition")));
                        warn_exp_dt_3.setText(cursor.getString(cursor.getColumnIndex("war_dat")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                    }


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }

    }


    public void getSerialNumberDetail() {


        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SERAIL_NUMBER_ZCMPLNHDTL
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {


                if (cursor.getCount() == 1) {
                    Layout_item_2.setVisibility(LinearLayout.GONE);
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }

                if (cursor.getCount() == 2) {
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }


                while (cursor.moveToNext()) {

                    //  Log.d("comp_no",""+cursor.getString(cursor.getColumnIndex("cmpno")) + "--" + cmp_no);


                    index = index + 1;


                    if (index == 1) {
                        cmp_posnr1 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_1.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_1.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_1.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_1.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_1.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_1 = cursor.getString(cursor.getColumnIndex("extwg"));

                        tv_cmp_defect_type1.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_1.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                        //tv_cmp_payment_by_1.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));
                     /*   pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("dea")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("com")));*/


                    }


                    if (index == 2) {
                        cmp_posnr2 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_2.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_2.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_2.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_2.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_2.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_2 = cursor.getString(cursor.getColumnIndex("extwg"));


                        tv_cmp_defect_type2.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_2.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                        //tv_cmp_payment_by_2.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_2.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_2.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_2.setText(cursor.getString(cursor.getColumnIndex("company")));
                       /* pay_freelancer_2.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_2.setText(cursor.getString(cursor.getColumnIndex("dea")));
                        pay_by_com_2.setText(cursor.getString(cursor.getColumnIndex("com")));*/


                    }


                    if (index == 3) {
                        cmp_posnr3 = cursor.getString(cursor.getColumnIndex("posnr"));
                        cmp_matnr_3.setText(cursor.getString(cursor.getColumnIndex("matnr")));
                        cmp_maktx_3.setText(cursor.getString(cursor.getColumnIndex("maktx")));
                        cmp_sernr_3.setText(cursor.getString(cursor.getColumnIndex("sernr")));
                        cmp_warranty_3.setText(cursor.getString(cursor.getColumnIndex("warranty")));
                        cmp_reason_3.setText(cursor.getString(cursor.getColumnIndex("reason")));
                        cmp_extwg_3 = cursor.getString(cursor.getColumnIndex("extwg"));


                        tv_cmp_defect_type3.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_close_reason_3.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                        //tv_cmp_payment_by_3.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_3.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_3.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_3.setText(cursor.getString(cursor.getColumnIndex("company")));
                       /* pay_freelancer_3.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_3.setText(cursor.getString(cursor.getColumnIndex("dea")));
                        pay_by_com_3.setText(cursor.getString(cursor.getColumnIndex("com")));*/


                    }


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }

    }


    public void getClouserComplaintDetail() {


        int index = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();

        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_CLOSE_COMPLAINT
                    + " WHERE " + DatabaseHelper.KEY_CMPNO + " = '" + cmp_no + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {


                if (cursor.getCount() == 1) {
                    Layout_item_2.setVisibility(LinearLayout.GONE);
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }

                if (cursor.getCount() == 2) {
                    Layout_item_3.setVisibility(LinearLayout.GONE);
                }


                while (cursor.moveToNext()) {

                    index = index + 1;

                    tv_cmp_category.setText(cursor.getString(cursor.getColumnIndex("category")));

                    //  if (cursor.getString(cursor.getColumnIndex("posnr")).equalsIgnoreCase("000001"))
                    if (index == 1) {

                        //Log.d("cmp_pay_freelancer",""+ cursor.getString(cursor.getColumnIndex("pay_freelancer")));


                        tv_cmp_close_reason_1.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_defect_type1.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_relt_to1.setText(cursor.getString(cursor.getColumnIndex("relt_to")));


                        //tv_cmp_payment_by_1.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_1.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_1.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_1.setText(cursor.getString(cursor.getColumnIndex("company")));
                        pay_freelancer_1.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_1.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_1.setText(cursor.getString(cursor.getColumnIndex("re_company")));


                    }


                    // if (cursor.getString(cursor.getColumnIndex("posnr")).equalsIgnoreCase("000002"))
                    if (index == 2) {
                        tv_cmp_close_reason_2.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_defect_type2.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_relt_to2.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        //tv_cmp_payment_by_2.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_2.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_2.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_2.setText(cursor.getString(cursor.getColumnIndex("company")));
                        pay_freelancer_2.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_2.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_2.setText(cursor.getString(cursor.getColumnIndex("re_company")));
                    }


                    //  if (cursor.getString(cursor.getColumnIndex("posnr")).equalsIgnoreCase("000003"))
                    if (index == 3) {

                        tv_cmp_close_reason_3.setText(cursor.getString(cursor.getColumnIndex("closer_reason")));
                        tv_cmp_defect_type3.setText(cursor.getString(cursor.getColumnIndex("defect")));
                        tv_cmp_relt_to3.setText(cursor.getString(cursor.getColumnIndex("relt_to")));

                        //tv_cmp_payment_by_3.setText(cursor.getString(cursor.getColumnIndex("payment_by")));

                        pay_by_customer_3.setText(cursor.getString(cursor.getColumnIndex("customer")));
                        pay_by_dealer_3.setText(cursor.getString(cursor.getColumnIndex("dealer")));
                        pay_by_company_3.setText(cursor.getString(cursor.getColumnIndex("company")));
                        pay_freelancer_3.setText(cursor.getString(cursor.getColumnIndex("pay_freelancer")));
                        pay_by_dea_3.setText(cursor.getString(cursor.getColumnIndex("re_dealer")));
                        pay_by_com_3.setText(cursor.getString(cursor.getColumnIndex("re_company")));
                    }


                }
            }

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            // End the transaction.
            db.close();
            // Close database

        }

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void SyncClosureComplaintInBackground() {
        Intent i = new Intent(DisplayComplaintDetailActivity.this, SyncDataService.class);
        startService(i);

    }


}
