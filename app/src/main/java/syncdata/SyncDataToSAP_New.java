package syncdata;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.BuildConfig;

import com.shaktipumps.shakti.shaktisalesemployee.app.Config;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import activity.CustomUtility;
import activity.LoginActivity;
import bean.AttendanceBean;
import bean.BeanProductFinal;
import bean.CheckInOutBean;
import bean.ClouserComplaint;
import bean.ComplaintAudio;
import bean.ComplaintImage;
import bean.ComplaintStart;
import bean.DsrEntryBean;
import bean.EmployeeGPSActivityBean;
import bean.ForwardForAppEntryBean;
import bean.InprocessComplaint;
import bean.LoginBean;
import bean.NewAddedCustomerBean;
import bean.NoOrderBean;
import bean.SurveyBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

/**
 * Created by shakti on 11/21/2016.
 */
public class  SyncDataToSAP_New {

    final ArrayList<NameValuePair> param2 = new ArrayList<NameValuePair>();
    final int MODE_PRIVATE = 0;
    DatabaseHelper dh;
    String sync_data_name = null;
    String sync_data_value = null;
    String sync_key_id = null;
    Context mContext = null;
    JSONArray ja_noOrder = null;
    JSONArray ja_takeOrder = null;
    JSONArray ja_empGPS = null;
    JSONArray ja_callLog = null;
    JSONArray ja_dsr_entry = null;
    JSONArray ja_check_in = null;
    JSONArray ja_new_customer = null;
    JSONArray ja_markAttendance = null;
    JSONArray ja_survey = null;
    JSONArray ja_inprocessComplaints = null;
    JSONArray ja_clouserComplaints = null;
    JSONArray ja_frwd_app_cmp = null;
    JSONArray ja_pend_app_cmp = null;
    JSONArray ja_complaintImage = null;
    JSONArray ja_complaintPdf = null;
    JSONArray ja_complaintAudio = null;
    JSONArray ja_complaintStart = null;
    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";
    SharedPreferences pref;
    SAPWebService con = null;
    SharedPreferences.Editor editor;

   /* android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };*/

    public void SendDataToSap(Context context) {
        this.mContext = context;
        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
        DatabaseHelper db = new DatabaseHelper(mContext);
        pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

/********************************* employee gps tracking *************************************/
        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
        employeeGPSActivityBeen = db.getEmployeeGpsActivity(mContext);

        if (employeeGPSActivityBeen.size() > 0) {
            //    Log.d("gpsdata", "" + employeeGPSActivityBeen.size());
            ja_empGPS = new JSONArray();
            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", employeeGPSActivityBeen.get(i).getKey_id());
                    jsonObj.put("pernr", employeeGPSActivityBeen.get(i).getPernr());
                    jsonObj.put("budat", employeeGPSActivityBeen.get(i).getBudat());
                    jsonObj.put("time", employeeGPSActivityBeen.get(i).getTime());
                    jsonObj.put("event", employeeGPSActivityBeen.get(i).getEvent());
                    jsonObj.put("latitude", employeeGPSActivityBeen.get(i).getLatitude());
                    jsonObj.put("longitude", employeeGPSActivityBeen.get(i).getLongitude());
                    jsonObj.put("phone_number", employeeGPSActivityBeen.get(i).getPhone_number());
                    jsonObj.put("cell_id", employeeGPSActivityBeen.get(i).getCell_id());
                    jsonObj.put("location_code", employeeGPSActivityBeen.get(i).getLocation_code());
                    jsonObj.put("mobile_country_code", employeeGPSActivityBeen.get(i).getMobile_country_code());
                    jsonObj.put("mobile_network_code", employeeGPSActivityBeen.get(i).getMobile_network_code());
                    ja_empGPS.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

/***********************************   check in check out **********************************/

        ArrayList<CheckInOutBean> checkInOutBeen = new ArrayList<CheckInOutBean>();
        checkInOutBeen = db.getCheckInOut();

        if (checkInOutBeen.size() > 0) {
            ja_check_in = new JSONArray();

            for (int i = 0; i < checkInOutBeen.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("key_id", checkInOutBeen.get(i).getKey_id());
                    jsonObj.put("pernr", checkInOutBeen.get(i).getPernr());
                    jsonObj.put("date_in", checkInOutBeen.get(i).getDate_in());
                    jsonObj.put("time_in", checkInOutBeen.get(i).getTime_in());
                    jsonObj.put("date_out", checkInOutBeen.get(i).getDate_out());
                    jsonObj.put("time_out", checkInOutBeen.get(i).getTime_out());

                    jsonObj.put("check_in_latitude", checkInOutBeen.get(i).getCheck_in_latitude());
                    jsonObj.put("check_out_latitude", checkInOutBeen.get(i).getCheck_out_latitude());
                    jsonObj.put("check_in_longitude", checkInOutBeen.get(i).getCheck_in_longitude());
                    jsonObj.put("check_out_longitude", checkInOutBeen.get(i).getCheck_out_longitude());
                    jsonObj.put("route_code", checkInOutBeen.get(i).getRoute_code());
                    jsonObj.put("comment", checkInOutBeen.get(i).getComment());
                    jsonObj.put("phone_number", checkInOutBeen.get(i).getPhone_number());
                    jsonObj.put("help_name", checkInOutBeen.get(i).getHelp_name());
                    jsonObj.put("audio_record", checkInOutBeen.get(i).getAudio_record());
                    jsonObj.put("audio_sap", checkInOutBeen.get(i).getAudio_record());

                    jsonObj.put("follow_up_date", checkInOutBeen.get(i).getFollow_up_date());
                    jsonObj.put("conversion_status", checkInOutBeen.get(i).getConversion_status());

                    jsonObj.put("SRV_CNT_BORD_IMG", CustomUtility.getBase64FromBitmap(mContext,checkInOutBeen.get(i).getPhoto1_text()));
                    jsonObj.put("SRV_CNT_TRN_LTR_IMG", CustomUtility.getBase64FromBitmap(mContext,checkInOutBeen.get(i).getPhoto2_text()));
                    jsonObj.put("CERTIFICATE_IMG", CustomUtility.getBase64FromBitmap(mContext,checkInOutBeen.get(i).getPhoto3_text()));
                    jsonObj.put("SLFY_SERV_PER", checkInOutBeen.get(i).getPhoto4_text());
                    jsonObj.put("SPR_PRT_STK_IMG", checkInOutBeen.get(i).getPhoto5_text());
                    jsonObj.put("PRD_TRN_IMG", checkInOutBeen.get(i).getPhoto6_text());
                    jsonObj.put("OTHR_IMG", checkInOutBeen.get(i).getPhoto7_text());
                    ja_check_in.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


/***************************** new added customer  ***********************************************/

        ArrayList<NewAddedCustomerBean> newAddedCustomerBeen = new ArrayList<NewAddedCustomerBean>();
        newAddedCustomerBeen = db.getNewAddedCustomer();
        //   Log.d("new_added", String.valueOf(newAddedCustomerBeen.size()));
        if (newAddedCustomerBeen.size() > 0) {
            ja_new_customer = new JSONArray();
            for (int i = 0; i < newAddedCustomerBeen.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    //   Log.d("new_customer_route",""+param.get(i).getRoute_code());
                    jsonObj.put("key_id", newAddedCustomerBeen.get(i).getKey_id());
                    jsonObj.put("pernr", newAddedCustomerBeen.get(i).getPernr());
                    jsonObj.put("start_date", newAddedCustomerBeen.get(i).getBudat());
                    jsonObj.put("route_code", newAddedCustomerBeen.get(i).getRoute_code());
                    jsonObj.put("route_name", newAddedCustomerBeen.get(i).getRoute_name());

                    jsonObj.put("partner", newAddedCustomerBeen.get(i).getPartner());
                    jsonObj.put("partner_class", newAddedCustomerBeen.get(i).getPartner_class());
                    jsonObj.put("latitude", newAddedCustomerBeen.get(i).getLatitude());
                    jsonObj.put("longitude", newAddedCustomerBeen.get(i).getLongitude());
                    jsonObj.put("partner_name", newAddedCustomerBeen.get(i).getPartner_name());

                    jsonObj.put("land1", newAddedCustomerBeen.get(i).getLand1());
                    jsonObj.put("land1_txt", newAddedCustomerBeen.get(i).getLand_txt());
                    jsonObj.put("state_code", newAddedCustomerBeen.get(i).getState_code());
                    jsonObj.put("state_txt", newAddedCustomerBeen.get(i).getState_txt());
                    jsonObj.put("district_code", newAddedCustomerBeen.get(i).getDistrict_code());
                    jsonObj.put("district_txt", newAddedCustomerBeen.get(i).getDistrict_txt());
                    jsonObj.put("taluka_code", newAddedCustomerBeen.get(i).getTaluka_code());
                    jsonObj.put("taluka_txt", newAddedCustomerBeen.get(i).getTaluka_txt());

                    jsonObj.put("address", newAddedCustomerBeen.get(i).getAddress());
                    jsonObj.put("email", newAddedCustomerBeen.get(i).getEmail());
                    jsonObj.put("mobile", newAddedCustomerBeen.get(i).getMob_no());
                    jsonObj.put("tel_number", newAddedCustomerBeen.get(i).getTel_number());
                    jsonObj.put("pincode", newAddedCustomerBeen.get(i).getPincode());
                    jsonObj.put("contact_person", newAddedCustomerBeen.get(i).getContact_person());
                    jsonObj.put("contact_person_phone", newAddedCustomerBeen.get(i).getContact_person_phone());

                    jsonObj.put("distributor_code", newAddedCustomerBeen.get(i).getDistributor_code());
                    jsonObj.put("distributor_name", newAddedCustomerBeen.get(i).getDistributor_name());
                    jsonObj.put("phone_number", newAddedCustomerBeen.get(i).getPhone_number());
                    jsonObj.put("panno", newAddedCustomerBeen.get(i).getPan_card());
                    jsonObj.put("aadharno", newAddedCustomerBeen.get(i).getAadhar_card());
                    jsonObj.put("tin_no", newAddedCustomerBeen.get(i).getTin_no());
                    jsonObj.put("market_plance", newAddedCustomerBeen.get(i).getMarket_place());
                    jsonObj.put("dob", newAddedCustomerBeen.get(i).getDob());
                    jsonObj.put("interested", newAddedCustomerBeen.get(i).getIntrested());
                    jsonObj.put("time", newAddedCustomerBeen.get(i).getTime());
                    jsonObj.put("add_latlong", newAddedCustomerBeen.get(i).getAdded_at_latlong());
                    ja_new_customer.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

/**************************** mark attendance data ***********************************************/

        ArrayList<AttendanceBean> attendanceBean = new ArrayList<AttendanceBean>();
        attendanceBean = db.getAllAttendance();
        if (attendanceBean.size() > 0) {
            ja_markAttendance = new JSONArray();

            for (int i = 0; i < attendanceBean.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                //AttendanceBean attendanceBean = attendanceBeen.get(i);
                try {

                    jsonObj.put("key_id", attendanceBean.get(i).KEY_ID);
                    jsonObj.put("PERNR", attendanceBean.get(i).PERNR);
                    jsonObj.put("BEGDA", attendanceBean.get(i).BEGDA);
                    //   jsonObj.put("SERVER_DATE_IN", attendanceBean.SERVER_DATE_IN);
                    // jsonObj.put("SERVER_TIME_IN", attendanceBean.SERVER_TIME_IN);
                    //  jsonObj.put("SERVER_DATE_OUT", attendanceBean.SERVER_DATE_OUT);
                    //  jsonObj.put("SERVER_TIME_OUT", attendanceBean.SERVER_TIME_OUT);
                    jsonObj.put("IN_TIME", attendanceBean.get(i).IN_TIME);
                    jsonObj.put("OUT_TIME", attendanceBean.get(i).OUT_TIME);
                    // jsonObj.put("WORKING_HOURS", attendanceBean.WORKING_HOURS);
                    jsonObj.put("IN_LAT_LONG", attendanceBean.get(i).IN_LAT_LONG);
                    jsonObj.put("OUT_LAT_LONG", attendanceBean.get(i).OUT_LAT_LONG);
                    jsonObj.put("IN_ADDRESS", attendanceBean.get(i).IN_ADDRESS);
                    jsonObj.put("OUT_ADDRESS", attendanceBean.get(i).OUT_ADDRESS);
                    jsonObj.put("IN_FILE_NAME", attendanceBean.get(i).IN_FILE_NAME);
                    jsonObj.put("OUT_FILE_NAME", attendanceBean.get(i).OUT_FILE_NAME);

                    jsonObj.put("DEVICE_NAME", CustomUtility.getDeviceName());
                    jsonObj.put("IMEI", CustomUtility.getDeviceId(context));
                    jsonObj.put("APP_VERSION", BuildConfig.VERSION_NAME);
                    jsonObj.put("API", Build.VERSION.SDK_INT);
                    jsonObj.put("API_VERSION", Build.VERSION.RELEASE);

//  ADD BY mayank
                    jsonObj.put("IN_IMAGE", attendanceBean.get(i).IN_IMAGE);
                    jsonObj.put("OUT_IMAGE", attendanceBean.get(i).OUT_IMAGE);
//  ADD BY mayank

                    if (!attendanceBean.get(i).SERVER_DATE_IN.equals("")) {
                        // save(attendanceBean.IN_FILE_NAME);
                    }

                    if (!attendanceBean.get(i).SERVER_DATE_OUT.equals("")) {
                        // save(attendanceBean.OUT_FILE_NAME);
                    }

                    ja_markAttendance.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

/***************************************** dsr entry********************/

        ArrayList<DsrEntryBean> dsrEntryBean = new ArrayList<DsrEntryBean>();
        dsrEntryBean = db.getDsrEntry();
        if (dsrEntryBean.size() > 0) {
            ja_dsr_entry = new JSONArray();
            for (int i = 0; i < dsrEntryBean.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    //    Log.d("json_dsr",""+param.get(i).getPernr()+"--"+ param.get(i).getDate());
                    jsonObj.put("key_id", dsrEntryBean.get(i).getKey_id());
                    jsonObj.put("pernr", dsrEntryBean.get(i).getPernr());
                    jsonObj.put("budat", dsrEntryBean.get(i).getDate());
                    jsonObj.put("help_name", dsrEntryBean.get(i).getHelp_name());
                    jsonObj.put("dsr_agenda", dsrEntryBean.get(i).getDsr_agenda());
                    jsonObj.put("dsr_comment", dsrEntryBean.get(i).getDsr_outcomes());
                    jsonObj.put("time", dsrEntryBean.get(i).getTime());
                    jsonObj.put("latitude", dsrEntryBean.get(i).getLatitude());
                    jsonObj.put("longitude", dsrEntryBean.get(i).getLongitude());

                    ja_dsr_entry.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        /***************************************** forwd for approval entry********************/

        ArrayList<ForwardForAppEntryBean> forwardForAppEntryBeans = new ArrayList<ForwardForAppEntryBean>();
        forwardForAppEntryBeans = db.getfrwdcmpapp();
        if (forwardForAppEntryBeans.size() > 0) {
            ja_frwd_app_cmp = new JSONArray();
            for (int i = 0; i < forwardForAppEntryBeans.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", forwardForAppEntryBeans.get(i).getKeyid());
                    jsonObj.put("cmpno", forwardForAppEntryBeans.get(i).getCmpno());
                    jsonObj.put("posnr", forwardForAppEntryBeans.get(i).getPosnr());
                    jsonObj.put("category", forwardForAppEntryBeans.get(i).getCategory());
                    jsonObj.put("dealer", forwardForAppEntryBeans.get(i).getDealer());
                    jsonObj.put("customer", forwardForAppEntryBeans.get(i).getCustomer());
                    jsonObj.put("company", forwardForAppEntryBeans.get(i).getCompany());
                    jsonObj.put("pay_freelancer", forwardForAppEntryBeans.get(i).getPay_freelancer());
                    jsonObj.put("re_del", forwardForAppEntryBeans.get(i).getPay_dealer());
                    jsonObj.put("re_comp", forwardForAppEntryBeans.get(i).getPay_company());
                    jsonObj.put("defect", forwardForAppEntryBeans.get(i).getDefect());
                    jsonObj.put("relt_to", forwardForAppEntryBeans.get(i).getRelat_to());
                    jsonObj.put("foc_amt", forwardForAppEntryBeans.get(i).getFoc_amt());
                    jsonObj.put("pend_apr_pernr", forwardForAppEntryBeans.get(i).getPendpernr());
                    jsonObj.put("await_apr_pernr", forwardForAppEntryBeans.get(i).getAwaitpernr());
                    jsonObj.put("await_apr_remark", forwardForAppEntryBeans.get(i).getAwaitrmrk());


                    ja_frwd_app_cmp.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        /***************************************** pend for approval entry********************/

        ArrayList<ForwardForAppEntryBean> forwardForAppEntryBeans1 = new ArrayList<ForwardForAppEntryBean>();
        forwardForAppEntryBeans1 = db.getpendcmpapp();
        if (forwardForAppEntryBeans1.size() > 0) {
            ja_pend_app_cmp = new JSONArray();
            for (int i = 0; i < forwardForAppEntryBeans1.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", forwardForAppEntryBeans1.get(i).getKeyid());
                    jsonObj.put("cmpno", forwardForAppEntryBeans1.get(i).getCmpno());
                    jsonObj.put("app_den_status", forwardForAppEntryBeans1.get(i).getStatus());
                    jsonObj.put("pend_apr_remark", forwardForAppEntryBeans1.get(i).getPendrmrk());
                    jsonObj.put("pend_apr_pernr", forwardForAppEntryBeans1.get(i).getPernr());

                    ja_pend_app_cmp.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

/****************************  no order **************************************/

        ArrayList<NoOrderBean> noOrderBean = new ArrayList<NoOrderBean>();
        noOrderBean = db.getNoOrderData();
        if (noOrderBean.size() > 0) {
            ja_noOrder = new JSONArray();
            for (int i = 0; i < noOrderBean.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", noOrderBean.get(i).getKey_id());
                    jsonObj.put("pernr", noOrderBean.get(i).getPernr());
                    jsonObj.put("budat", noOrderBean.get(i).getDate());
                    jsonObj.put("help_name", noOrderBean.get(i).getHelp_name());
                    jsonObj.put("comment", noOrderBean.get(i).getComment());
                    jsonObj.put("time", noOrderBean.get(i).getTime());
                    jsonObj.put("latitude", noOrderBean.get(i).getLatitude());
                    jsonObj.put("longitude", noOrderBean.get(i).getLongitude());
                    jsonObj.put("phone_number", noOrderBean.get(i).getPhone_number());
                    jsonObj.put("route_code", noOrderBean.get(i).getRoute_code());
                    jsonObj.put("IMAGE", noOrderBean.get(i).getImage());
                    ja_noOrder.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /************************************* take order *********************************************/

        ArrayList<BeanProductFinal> beanProductFinals = new ArrayList<BeanProductFinal>();
        beanProductFinals = db.getOrderList();
        if (beanProductFinals.size() > 0) {
            ja_takeOrder = new JSONArray();
            for (int i = 0; i < beanProductFinals.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", beanProductFinals.get(i).getKey_id());
                    jsonObj.put("phone_number", beanProductFinals.get(i).getPhone_number());
                    jsonObj.put("matnr", beanProductFinals.get(i).getMatnr());
                    jsonObj.put("extwg", beanProductFinals.get(i).getExtwg());
                    jsonObj.put("maktx", beanProductFinals.get(i).getMaktx());
                    jsonObj.put("kbetr", beanProductFinals.get(i).getKbetr());
                    jsonObj.put("menge", beanProductFinals.get(i).getMenge());
                    jsonObj.put("tot_kbetr", beanProductFinals.get(i).getTot_kbetr());
                    jsonObj.put("customer_name", beanProductFinals.get(i).getCustomer_name());
                    jsonObj.put("person", beanProductFinals.get(i).getPerson());
                    jsonObj.put("cr_date", beanProductFinals.get(i).getCr_date());
                    jsonObj.put("cr_time", beanProductFinals.get(i).getCr_time());
                    jsonObj.put("latitude", beanProductFinals.get(i).getLatitude());
                    jsonObj.put("longitude", beanProductFinals.get(i).getLongitude());
                    jsonObj.put("route_code", beanProductFinals.get(i).getRoute_code());
                    jsonObj.put("partner", beanProductFinals.get(i).getPartner_type());
                    ja_takeOrder.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /********************** survey ***********************************/

        ArrayList<SurveyBean> survey = new ArrayList<SurveyBean>();
        survey = db.getSurveyData();
        if (survey.size() > 0) {
            ja_survey = new JSONArray();
            for (int i = 0; i < survey.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", survey.get(i).getKey_id());
                    jsonObj.put("pernr", survey.get(i).getPernr());
                    jsonObj.put("budat", survey.get(i).getBudat());
                    jsonObj.put("remark", survey.get(i).getComment());
                    jsonObj.put("time", survey.get(i).getTime());
                    jsonObj.put("latitude", survey.get(i).getLatitude());
                    jsonObj.put("longitude", survey.get(i).getLongitude());
                    jsonObj.put("phone_number", survey.get(i).getPhone_number());
                    jsonObj.put("other_url", survey.get(i).getOther_view());
                    jsonObj.put("outer_url", survey.get(i).getOuter_view());
                    jsonObj.put("inner_url", survey.get(i).getInner_view());
                    jsonObj.put("owner_url", survey.get(i).getOwner_view());
                    jsonObj.put("card_url", survey.get(i).getCard_view());
                    ja_survey.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /***************************  end survey ************************/


        /********************** inprocess complaint ***********************************/

        ArrayList<InprocessComplaint> inprocessComplaints = new ArrayList<InprocessComplaint>();
        inprocessComplaints = db.getInprocessComplaintData();
        if (inprocessComplaints.size() > 0) {
            ja_inprocessComplaints = new JSONArray();
            for (int i = 0; i < inprocessComplaints.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", inprocessComplaints.get(i).getKey_id());
                    jsonObj.put("pernr", inprocessComplaints.get(i).getPernr());
                    jsonObj.put("cmpno", inprocessComplaints.get(i).getCmpno());
                    jsonObj.put("follow_up_date", inprocessComplaints.get(i).getFollow_up_date());
                    jsonObj.put("reason", inprocessComplaints.get(i).getReason());
                    jsonObj.put("CMP_PEN_RE", inprocessComplaints.get(i).getReasonid());
                    jsonObj.put("latitude", inprocessComplaints.get(i).getLatitude());
                    jsonObj.put("longitude", inprocessComplaints.get(i).getLongitude());
                    jsonObj.put("cr_date", inprocessComplaints.get(i).getCr_date());
                    jsonObj.put("cr_time", inprocessComplaints.get(i).getCr_time());
                    jsonObj.put("cmpln_status", inprocessComplaints.get(i).getCmpln_status());
                    ja_inprocessComplaints.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /***************************  inprocess complaint ************************/

        /********************** complaint clouser ***********************************/
        ArrayList<ClouserComplaint> clouserComplaints = new ArrayList<ClouserComplaint>();
        clouserComplaints = db.getClouserComplaintData();
        Log.e("DATA!@##", "&&&&" + clouserComplaints.toString());
        if (clouserComplaints.size() > 0) {
            ja_clouserComplaints = new JSONArray();
            for (int i = 0; i < clouserComplaints.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", clouserComplaints.get(i).getKey_id());
                    jsonObj.put("pernr", clouserComplaints.get(i).getPernr());
                    jsonObj.put("cmpno", clouserComplaints.get(i).getCmpno());
                    jsonObj.put("posnr", clouserComplaints.get(i).getPosnr());
                    jsonObj.put("reason", clouserComplaints.get(i).getReason());
                    jsonObj.put("category", clouserComplaints.get(i).getCategory());
                    /*jsonObj.put("payment_by", clouserComplaints.get(i).getPayment_by());*/
                    jsonObj.put("customer", clouserComplaints.get(i).getCustomer());
                    jsonObj.put("dealer", clouserComplaints.get(i).getDealer());
                    jsonObj.put("company", clouserComplaints.get(i).getCompany());
                    jsonObj.put("ZFEEDRMRK", clouserComplaints.get(i).getCustomerFeedback());
                    jsonObj.put("ZFEEDF", clouserComplaints.get(i).getCustomerFeedbackStatus());
                    jsonObj.put("closer_reson", clouserComplaints.get(i).getCloser_reason());
                    jsonObj.put("defect", clouserComplaints.get(i).getDefect());
                    jsonObj.put("cmpln_related_to", clouserComplaints.get(i).getCmpln_relt_to());
                    jsonObj.put("cr_date", clouserComplaints.get(i).getDate());
                    jsonObj.put("cr_time", clouserComplaints.get(i).getTime());
                    jsonObj.put("latitude", clouserComplaints.get(i).getLatitude());
                    jsonObj.put("longitude", clouserComplaints.get(i).getLongitude());
                    jsonObj.put("pay_freelancer", clouserComplaints.get(i).getPay_freelancer());
                   // jsonObj.put("re_company", clouserComplaints.get(i).getDea());
                    jsonObj.put("re_company", clouserComplaints.get(i).getCom());
                    jsonObj.put("re_dealer", clouserComplaints.get(i).getDea());
                    jsonObj.put("focamt", clouserComplaints.get(i).getFocamt());
                    ja_clouserComplaints.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        /********************** complaint clouser ***********************************/

        /**********************  complaint image  ***********************************/

        ArrayList<ComplaintImage> complaintImages = new ArrayList<ComplaintImage>();
        complaintImages = db.getComplaintImage();
        if (complaintImages.size() > 0) {
            ja_complaintImage = new JSONArray();
            for (int i = 0; i < complaintImages.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    String[] strParts = complaintImages.get(i).getPosnr().split("101");
                    String image_item = strParts[1];
                    jsonObj.put("key_id", complaintImages.get(i).getKey_id());
                    jsonObj.put("cmpno", complaintImages.get(i).getCmpno());
                    jsonObj.put("posnr", image_item);
                    jsonObj.put("category", complaintImages.get(i).getCategory());
                    jsonObj.put("image", CustomUtility.getBase64FromBitmap(mContext,complaintImages.get(i).getImage()));
                    ja_complaintImage.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**********************  complaint audio  ***********************************/
        ArrayList<ComplaintAudio> complaintAudio = new ArrayList<ComplaintAudio>();
        complaintAudio = db.getComplaintAudio();
        if (complaintAudio.size() > 0) {
            ja_complaintAudio = new JSONArray();
            for (int i = 0; i < complaintAudio.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", complaintAudio.get(i).getKey_id());
                    jsonObj.put("cmpno", complaintAudio.get(i).getCmpno());
                    jsonObj.put("pernr", complaintAudio.get(i).getPernr());
                    jsonObj.put("budat", complaintAudio.get(i).getBudat());
                    jsonObj.put("cr_time", complaintAudio.get(i).getTime());
                    jsonObj.put("audio_record", complaintAudio.get(i).getAudio_record());
                    jsonObj.put("audio_sap", complaintAudio.get(i).getAudio_record());
                    ja_complaintAudio.put(jsonObj);
                    db.deleteComplaintImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**********************  complaint start location  ***********************************/

        ArrayList<ComplaintStart> complaintStart = new ArrayList<ComplaintStart>();
        complaintStart = db.getComplaintStart();
        if (complaintStart.size() > 0) {
            ja_complaintStart = new JSONArray();
            for (int i = 0; i < complaintStart.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("key_id", complaintStart.get(i).getKey_id());
                    jsonObj.put("cmpno", complaintStart.get(i).getCmpno());
                    jsonObj.put("pernr", complaintStart.get(i).getPernr());
                    jsonObj.put("cr_date", complaintStart.get(i).getCr_date());
                    jsonObj.put("cr_time", complaintStart.get(i).getCr_time());
                    jsonObj.put("lat_long", complaintStart.get(i).getLat_long());
                    ja_complaintStart.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//************************* forward to other ****


/*********************************************************************************************/
/*                      start   send data to server
/********************************************************************************************/

        param1.add(new BasicNameValuePair("PERNR", pref.getString("key_username", "userid")));
        param1.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(mContext,"objs")));
        param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));
        param1.add(new BasicNameValuePair("ORDER_LIST", String.valueOf(ja_takeOrder)));
        param1.add(new BasicNameValuePair("NO_ORDER_ENTRY", String.valueOf(ja_noOrder)));
        param1.add(new BasicNameValuePair("DSR_ENTRY", String.valueOf(ja_dsr_entry)));
        param1.add(new BasicNameValuePair("NEW_ADDED_CUSTOEMR", String.valueOf(ja_new_customer)));
        param1.add(new BasicNameValuePair("mark_attendance", String.valueOf(ja_markAttendance)));
        param1.add(new BasicNameValuePair("CHECK_IN", String.valueOf(ja_check_in)));
        param1.add(new BasicNameValuePair("SURVEY", String.valueOf(ja_survey)));
        param1.add(new BasicNameValuePair("INPROCESS_COMPLAINT", String.valueOf(ja_inprocessComplaints)));
        param1.add(new BasicNameValuePair("CLOUSER_COMPLAINT", String.valueOf(ja_clouserComplaints)));
        param1.add(new BasicNameValuePair("COMPLAINT_IMAGE", String.valueOf(ja_complaintImage)));
        param1.add(new BasicNameValuePair("FORWARD_APPROVAL_COMPLAINT", String.valueOf(ja_frwd_app_cmp)));
        param1.add(new BasicNameValuePair("pending_approval_complaint", String.valueOf(ja_pend_app_cmp)));
        param1.add(new BasicNameValuePair("COMPLAINT_AUDIO", String.valueOf(ja_complaintAudio)));
        param1.add(new BasicNameValuePair("COMPLAINT_START", String.valueOf(ja_complaintStart)));

        SharedPreferences gcm_pref = context.getSharedPreferences(Config.SHARED_PREF, 0);
        param1.add(new BasicNameValuePair("PERSON_GCM_ID", gcm_pref.getString("regId", null)));
        if (pref.getString("key_logout", "logout_error").equalsIgnoreCase("logout")) {
            param1.add(new BasicNameValuePair(pref.getString("key_logout", "logout_error"), pref.getString("key_username", "userid")));
        }

        try {
            Log.e("SendDataToSap====>", String.valueOf(param1));
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1);
            //   Log.d("output_obj2", obj2);
            if (obj2 != "") {
                JSONObject jo_success = new JSONObject(obj2);
                JSONArray ja_success = jo_success.getJSONArray("data_success");
                for (int i = 0; i < ja_success.length(); i++) {
                    JSONObject jo = ja_success.getJSONObject(i);
                    sync_data_name = jo.getString("sync_data");
                    sync_key_id = jo.getString("key_id");
                    sync_data_value = jo.getString("value");
                    Log.d("success", "" + sync_data_name + "---" + sync_key_id + "---" + sync_data_value);
                    if (sync_data_name.equalsIgnoreCase("SURVEY")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_SURVEY, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("EMP_GPS")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_EMPLOYEE_GPS_ACTIVITY, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("CHECK_IN_OUT")) {
                        deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + "/SSAPP/TRAN/" + checkInOutBeen.get(i).getCustomer_name()));
                        CustomUtility.setSharedPreference(mContext, "ServiceCenterTRN" + checkInOutBeen.get(i).getCustomer_name(), "");
                        db.updateUnsyncData(DatabaseHelper.TABLE_CHECK_IN_OUT, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("TAKE_ORDER")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_ADHOC_FINAL, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("FORWARD_APPROVAL_COMPLAINT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_FRWD_APP_CMP, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("PENDING_APPROVAL_COMPLAINT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_PEND_APP_CMP, sync_key_id);
                        db.deletecmpattachment(forwardForAppEntryBeans1.get(i).getCmpno());
                        db.deletePendcmpdata(forwardForAppEntryBeans1.get(i).getCmpno());
                        db.deleteCustomerComplaintHeader(forwardForAppEntryBeans1.get(i).getCmpno());
                        db.deleteCustomerComplaintDetail(forwardForAppEntryBeans1.get(i).getCmpno());
                    }
                    if (sync_data_name.equalsIgnoreCase("NO_ORDER")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_NO_ORDER, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("DSR_COMMENT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_DSR_ENTRY, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("INPROCESS_COMPLAINT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("NEW_ADDED_CUSTOMER")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_NEW_ADDED_CUSTOMER, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("INPROCESS_COMPLAINT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("CLOUSER_COMPLAINT")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_CLOSE_COMPLAINT, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("COMPLAINT_IMAGE")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_ZCMPLN_IMAGE, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("COMPLAINT_AUDIO")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_COMPLAINT_AUDIO, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("COMPLAINT_START")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_COMPLAINT_DISTANCE, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("MARK_ATTENDANCE")) {
                        db.updateUnsyncData(DatabaseHelper.TABLE_MARK_ATTENDANCE, sync_key_id);
                    }
                    if (sync_data_name.equalsIgnoreCase("PLANS") &&
                            sync_data_value.equalsIgnoreCase("99999999")) {
                       CustomUtility.logoutUser(context);

                    }
                    if (sync_data_name.equalsIgnoreCase("LOGOUT")) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
                CustomUtility.deleteArrayList(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendEmployeeGPS(Context context) {
        /********************************* employee gps tracking *************************************/
        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
        DatabaseHelper db = new DatabaseHelper(mContext);
        employeeGPSActivityBeen = db.getEmployeeGpsActivity(mContext);


        if (employeeGPSActivityBeen.size() > 0) {
            //  Log.d("gpsdata", ""+employeeGPSActivityBeen.size());
            ja_empGPS = new JSONArray();

            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {

                    jsonObj.put("pernr", employeeGPSActivityBeen.get(i).getPernr());
                    jsonObj.put("objs",CustomUtility.getSharedPreferences(mContext,"objs"));
                    jsonObj.put("budat", employeeGPSActivityBeen.get(i).getBudat());
                    jsonObj.put("time", employeeGPSActivityBeen.get(i).getTime());
                    jsonObj.put("event", employeeGPSActivityBeen.get(i).getEvent());
                    jsonObj.put("latitude", employeeGPSActivityBeen.get(i).getLatitude());
                    jsonObj.put("longitude", employeeGPSActivityBeen.get(i).getLongitude());
                    jsonObj.put("phone_number", employeeGPSActivityBeen.get(i).getPhone_number());

                    ja_empGPS.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));


            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);


                String obj2 = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param1);

                //  Log.d("output_obj2", obj2);

                if (obj2 != "") {

                    JSONObject jo_success = new JSONObject(obj2);
                    JSONArray ja_success = jo_success.getJSONArray("data_success");

                    for (int i = 0; i < ja_success.length(); i++) {


                        JSONObject jo = ja_success.getJSONObject(i);

                        sync_data_name = jo.getString("sync_data");
                        sync_data_value = jo.getString("value");


                        // Log.d("success", "" + sync_data_name + "---" + sync_data_value);


                        if (sync_data_name.equalsIgnoreCase("EMP_GPS") &&
                                sync_data_value.equalsIgnoreCase("Y")) {
                            db.deleteEmployeeGPSActivity();
                        }


                        if (sync_data_name.equalsIgnoreCase("PLANS") &&
                                sync_data_value.equalsIgnoreCase("99999999")) {


                            SharedPreferences.Editor editor;
                            SharedPreferences pref;
                            pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                            editor = pref.edit();

                            // stop back groud services





                            new DatabaseHelper(context).deleteLogin();
                            LoginBean.setLogin("", "");

                            editor.remove("key_sync_date");
                            editor.remove("key_login");
                            editor.remove("key_username");
                            editor.remove("key_ename");
                            editor.remove("key_logout");

                            editor.commit(); // commit changes


                            System.exit(0);
                        }


                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    @SuppressLint("Range")
    public void getCheckInOutAudio() {

//       SharedPreferences pref ;
//        final int MODE_PRIVATE = 0;
//
        String msg = "", name = "", activity = "", key_id = " ", location = " ",
                latitude = "0.0", longitude = "0.0";


        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();

        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
//
//
//
//
//        param2.add(new BasicNameValuePair("username",pref.getString("chat_app_username","userid") ));
//        param2.add(new BasicNameValuePair("password",pref.getString("chat_app_password","password")));
//        param2.add(new BasicNameValuePair("group_id",pref.getString("chat_app_group_id","group_id")));
//        param2.add(new BasicNameValuePair("from_id",pref.getString("key_username","userid") ));
//        param2.add(new BasicNameValuePair("from_type","E" ));
//


        String selectQuery = null;
        Cursor cursor = null;


        //********************************  send check in check out  voice recording ***************
        try {


            // selectQuery = "SELECT * FROM " + TABLE_CHECK_IN_OUT;//+ " WHERE CONFIRM IS NOT NULL";
            selectQuery = "SELECT * FROM " + dh.TABLE_CHECK_IN_OUT

                    + " WHERE " + dh.KEY_PERNR + " = '" + pref.getString("key_username", "userid") + "'"
                    + " AND " + dh.KEY_CHAT_APP + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //   Log.d("db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {


                    key_id = cursor.getString(cursor.getColumnIndex(dh.KEY_ID));
                    msg = cursor.getString(cursor.getColumnIndex(dh.KEY_AUDIO_RECORD));


                    activity = cursor.getString(cursor.getColumnIndex(dh.KEY_HELP_NAME));

                    latitude = cursor.getString(cursor.getColumnIndex(dh.KEY_CHECK_OUT_LATITUDE));
                    longitude = cursor.getString(cursor.getColumnIndex(dh.KEY_CHECK_OUT_LONGITUDE));


                    String CurrentString = activity;
                    String[] separated;
                    separated = CurrentString.split("--");
                    activity = separated[0];


                    if (!latitude.equalsIgnoreCase("0.0")) {
                        location = "http://maps.google.com/maps?&z=20&q=loc:" + latitude + "," + longitude;
                    }
//


                    String msg1 = "Customer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_PARTNER_NAME)) + "\n" +
                            "Place :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRICT_TXT)) + "\n" +
                            "Activity :" + activity + "\n" +
                            "Comment :" + cursor.getString(cursor.getColumnIndex(dh.KEY_COMMENT)) + "\n" +

                            "Date :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DATE_OUT)) + "    " +
                            "Time :" + cursor.getString(cursor.getColumnIndex(dh.KEY_TIME_OUT)) + "\n" + "\n" +
                            "Location :" + location;

                    //   Log.d("msg1",msg1);


                    param2.add(new BasicNameValuePair("message", msg1));
                    param2.add(new BasicNameValuePair("attachment", msg));
                    param2.add(new BasicNameValuePair("file_name", "Shakti_" + pref.getString("key_username", "userid") + System.currentTimeMillis() + ".mp3"));

                    try {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);


                        // String obj2 = CustomHttpClient.executeHttpPost1("http://shakti.techinventions.in/shaktichat/api/chat/sendindividual", param2);


                        //String obj2 = CustomHttpClient.executeHttpPost1("http://shakti.techinventions.in/shaktichat/api/chat/sendgroupmsg", param2);
                        String obj2 = CustomHttpClient.executeHttpPost1(pref.getString("chat_app_api", "chat_app_api"), param2);

                        // Log.d("output_obj5", obj2);

                        if (obj2 != "") {

                            //  JSONObject jo_success = new JSONObject(obj2);

                            //  Log.d("output_obj6", "" +   jo_success);
                            dh.updateAudioChatApp(DatabaseHelper.TABLE_CHECK_IN_OUT, key_id);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cursor != null) {
                cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }


    }


    @SuppressLint("Range")
    public String getClouserComplaint(String cmp_no) {

        String selectQuery = null;
        String latitude = "0.0", longitude = "0.0", cmp_msg = "", location = "", payment_by = "",
                dealer = " ", company = " ", customer = " ", payment_txt = "", payment = " ";
        Cursor cmp_cursor = null;

        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();


        try {


            selectQuery = "SELECT * FROM " + dh.TABLE_CLOSE_COMPLAINT

                    + " WHERE " + dh.KEY_CMPNO + " = '" + cmp_no + "'";


            cmp_cursor = db.rawQuery(selectQuery, null);

            Log.d("msg_cmp", "" + cmp_cursor.getCount());

            if (cmp_cursor.getCount() > 0) {

                while (cmp_cursor.moveToNext()) {


                    latitude = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_LATITUDE));
                    longitude = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_LONGITUDE));

                    payment_by = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_PAYMENT_BY));

                    customer = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_CUSTOMER));
                    dealer = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_DEALER));
                    company = cmp_cursor.getString(cmp_cursor.getColumnIndex(dh.KEY_COMPANY));


                    if (!TextUtils.isEmpty(customer.trim())) {
                        payment_txt = "Customer ";
                    }


                    if (!TextUtils.isEmpty(dealer.trim())) {
                        payment_txt = "Dealer ";
                    }


                    if (!TextUtils.isEmpty(company.trim())) {
                        payment_txt = "Company ";
                    }


                    payment = payment + payment_by + " : " + payment_txt + " " + company + customer + dealer + "\n";

                    if (!latitude.equalsIgnoreCase("0.0")) {
                        location = "http://maps.google.com/maps?&z=20&q=loc:" + latitude + "," + longitude;
                    }

//                           cmp_msg = payment_by + " : " + payment_txt +" "+ company+ customer + dealer +"\n"+ "\n"+
//                                     "Location :" + location;


                    cmp_msg = payment + "\n" +
                            "Location :" + location;


                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cmp_cursor != null) {
                cmp_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }

        return cmp_msg;
    }

    @SuppressLint("Range")
    public String getComplaintHeader(String cmp_no) {

        String selectQuery = null;
        String latitude = "0.0", longitude = "0.0", cmp_msg = "", location = "", warranty = "";
        Cursor cmp_hdr_cursor = null;

        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();


        try {


            selectQuery = "SELECT * FROM " + dh.TABLE_ZCMPLNHDTL

                    + " WHERE " + dh.KEY_CMPNO + " = '" + cmp_no + "'";
//                    + " AND " +   dh.KEY_POSNR  + " = '" + cmp_posnr + "'" ;


            cmp_hdr_cursor = db.rawQuery(selectQuery, null);

            Log.d("msg_cmp_hdr", "" + cmp_hdr_cursor.getCount());

            if (cmp_hdr_cursor.getCount() > 0) {

                while (cmp_hdr_cursor.moveToNext()) {

                    warranty = warranty + " " + cmp_hdr_cursor.getString(cmp_hdr_cursor.getColumnIndex(dh.KEY_WARRANTY));


                    cmp_msg = "Warranty :" + warranty;
                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cmp_hdr_cursor != null) {
                cmp_hdr_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }

        return cmp_msg;
    }


    @SuppressLint("Range")
    public void getChatAppGroup() {

        String selectQuery = null, username = " ", password = " ", group_id = " ", api = "";

        Cursor chat_cursor = null;

        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();

        try {

            selectQuery = "SELECT * FROM " + dh.TABLE_CHAT_APP;

            // +  " WHERE " + dh.KEY_PERNR +  " = '" +  pref.getString("key_username","userid") +"'" ;


            chat_cursor = db.rawQuery(selectQuery, null);

            // Log.d("chat_cursor12",""+ chat_cursor.getCount());

            if (chat_cursor.getCount() > 0) {

                while (chat_cursor.moveToNext()) {


                    username = chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_USERNAME));
                    password = chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_PASSWORD));
                    group_id = chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_GROUP_ID));
                    api = chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_API));

                    Log.d("chat_api", username + "--" + password + "--" + group_id + "--" + api);


                    //  ************************ send check in out message to chat app ***************

                    getCheckInOutRecording(username, password, group_id, api);

                    // *********************** send complaint data to chat app ***********************

                    getComplaintRecording(username, password, group_id, api);


                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (chat_cursor != null) {
                chat_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }


// ******* update data which is send to chat app
        updateDataSendToChatApp();


    }

    @SuppressLint("Range")
    public void updateDataSendToChatApp() {

        String selectQuery = null;

        Cursor chat_cursor = null;

        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();

        try {

            selectQuery = "SELECT * FROM " + dh.TABLE_DATA_SYNC_CHAT_APP;


            chat_cursor = db.rawQuery(selectQuery, null);

            // Log.d("chat_cursor12",""+ chat_cursor.getCount());

            if (chat_cursor.getCount() > 0) {

                while (chat_cursor.moveToNext()) {


                    dh.updateDataSendToChatApp
                            (
                                    chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_TABLE_NAME)),
                                    chat_cursor.getString(chat_cursor.getColumnIndex(dh.KEY_SYNC_ID))
                            );

                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (chat_cursor != null) {
                chat_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }


//  delete those record which is send to chat app
        dh.deleteDataSyncToChatApp();
    }

    @SuppressLint("Range")
    public void getComplaintAudio() {

//        SharedPreferences pref ;
//        final int MODE_PRIVATE = 0;
//
        String msg = "", name = "", activity = "", key_id = " ", latitude = "0.0", longitude = "0.0", location = " ", cmp_msg = "", cmp_header = "";


        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();
        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);


        String selectQuery = null;
        Cursor cursor = null;

//************************************************* start send service voice recording *****************


        try {

            // selectQuery = "SELECT * FROM " + TABLE_CHECK_IN_OUT;//+ " WHERE CONFIRM IS NOT NULL";
            selectQuery = "SELECT * FROM " + dh.TABLE_COMPLAINT_AUDIO

                    + " WHERE " + dh.KEY_PERNR + " = '" + pref.getString("key_username", "userid") + "'"
                    + " AND " + dh.KEY_CHAT_APP + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //   Log.d("db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {


                    key_id = cursor.getString(cursor.getColumnIndex(dh.KEY_ID));
                    msg = cursor.getString(cursor.getColumnIndex(dh.KEY_AUDIO_RECORD));


                    cmp_msg = getClouserComplaint(cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)));
                    cmp_header = getComplaintHeader(cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)));

//                    if (!latitude.equalsIgnoreCase("0.0"))
//                    {
//                        location = "http://maps.google.com/maps?&z=20&q=loc:"+latitude+","+longitude ;
//                    }


                    String msg1 = "Complaint No :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)) + "\n" +
                            "Dealer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRIBUTOR_NAME)) + "\n" +
                            "Customer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_PARTNER_NAME)) + "\n" +
                            "Address :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRICT_TXT)) + "\n" +
                            "Closer Comment :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CLOSER_RESON)) + "\n" +

                            cmp_header + "\n" +
                            "Date :" + cursor.getString(cursor.getColumnIndex(dh.KEY_BUDAT)) + "    " +
                            "Time :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CR_TIME)) + "\n" +
                            cmp_msg;


//                            "Date :" + cursor.getString(cursor.getColumnIndex(dh.KEY_BUDAT)) +"    "+
//                            "Time :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CR_TIME)) +"\n"+ "\n"+
//                            "Location :" + location;


                    // Log.d("msg1",msg1);


                    param2.add(new BasicNameValuePair("message", msg1));
                    param2.add(new BasicNameValuePair("attachment", msg));
                    param2.add(new BasicNameValuePair("file_name", "Shakti_p" + pref.getString("key_username", "userid") + System.currentTimeMillis() + ".mp3"));

                    try {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);


                        // String obj2 = CustomHttpClient.executeHttpPost1("http://shakti.techinventions.in/shaktichat/api/chat/sendindividual", param2);


                        //String obj2 = CustomHttpClient.executeHttpPost1("http://shakti.techinventions.in/shaktichat/api/chat/sendgroupmsg", param2);
                        String obj2 = CustomHttpClient.executeHttpPost1(pref.getString("chat_app_api", "chat_app_api"), param2);
//
//                       // Log.d("output_obj5", obj2);
//
                        if (obj2 != "") {

                            //  JSONObject jo_success = new JSONObject(obj2);

                            //  Log.d("output_obj6", "" +   jo_success);
                            dh.updateComplaintAudio(DatabaseHelper.TABLE_COMPLAINT_AUDIO, key_id);

                        }
//

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cursor != null) {
                cursor.close();
            }
//  db.endTransaction();
            db.close();
            // Close database
        }


        //************************* end send service voice recording ******************************

    }
    @SuppressLint("Range")
    public void getComplaintRecording(String username, String password, String group_id, String api) {

//        SharedPreferences pref ;
//        final int MODE_PRIVATE = 0;
//
        String msg = "", name = "", activity = "", key_id = " ", latitude = "0.0", longitude = "0.0", location = " ", cmp_msg = "", cmp_header = "";


        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();
        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);


        String selectQuery = null;
        Cursor cursor = null;

//************************************************* start send service voice recording *****************


        try {


            selectQuery = "SELECT * FROM " + dh.TABLE_COMPLAINT_AUDIO

                    + " WHERE " + dh.KEY_PERNR + " = '" + pref.getString("key_username", "userid") + "'"
                    + " AND " + dh.KEY_CHAT_APP + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //   Log.d("db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {


                    key_id = cursor.getString(cursor.getColumnIndex(dh.KEY_ID));
                    msg = cursor.getString(cursor.getColumnIndex(dh.KEY_AUDIO_RECORD));


                    cmp_msg = getClouserComplaint(cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)));
                    cmp_header = getComplaintHeader(cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)));

//                    if (!latitude.equalsIgnoreCase("0.0"))
//                    {
//                        location = "http://maps.google.com/maps?&z=20&q=loc:"+latitude+","+longitude ;
//                    }


                    String msg1 = "Complaint No :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CMPNO)) + "\n" +
                            "Dealer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRIBUTOR_NAME)) + "\n" +
                            "Customer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_PARTNER_NAME)) + "\n" +
                            "Address :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRICT_TXT)) + "\n" +
                            "Closer Comment :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CLOSER_RESON)) + "\n" +

                            cmp_header + "\n" +
                            "Date :" + cursor.getString(cursor.getColumnIndex(dh.KEY_BUDAT)) + "    " +
                            "Time :" + cursor.getString(cursor.getColumnIndex(dh.KEY_CR_TIME)) + "\n" +
                            cmp_msg;


                    // Log.d("msg1",msg1);


                    param2.clear();

                    param2.add(new BasicNameValuePair("message", msg1));
                    param2.add(new BasicNameValuePair("attachment", msg));
                    param2.add(new BasicNameValuePair("file_name", "Shakti_p" + pref.getString("key_username", "userid") + System.currentTimeMillis() + ".mp3"));


                    param2.add(new BasicNameValuePair("username", username));
                    param2.add(new BasicNameValuePair("password", password));

                    param2.add(new BasicNameValuePair("group_id", group_id));

                    // param2.add(new BasicNameValuePair("group_id","99999001"));

                    param2.add(new BasicNameValuePair("from_id", pref.getString("key_username", "userid")));
                    param2.add(new BasicNameValuePair("from_type", "E"));


                    try {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);


                        String obj2 = CustomHttpClient.executeHttpPost1(api, param2);
//
//                       // Log.d("output_obj5", obj2);
//
                        if (obj2 != "") {


                            //                      dh.updateComplaintAudio(DatabaseHelper.TABLE_COMPLAINT_AUDIO,key_id);

                            dh.insertUnsyncDataChatApp(DatabaseHelper.TABLE_COMPLAINT_AUDIO, key_id);


                        }
//

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cursor != null) {
                cursor.close();
            }
//  db.endTransaction();
            db.close();
            // Close database
        }


        //************************* end send service voice recording ******************************

    }


    @SuppressLint("Range")
    public void getCheckInOutRecording(String username, String password, String group_id, String api) {

//       SharedPreferences pref ;
//        final int MODE_PRIVATE = 0;
//
        String msg = "", name = "", activity = "", key_id = " ", location = " ",
                latitude = "0.0", longitude = "0.0";


        dh = new DatabaseHelper(mContext);
        SQLiteDatabase db = dh.getReadableDatabase();

        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
//
//
//
//
//        param2.add(new BasicNameValuePair("username",pref.getString("chat_app_username","userid") ));
//        param2.add(new BasicNameValuePair("password",pref.getString("chat_app_password","password")));
//        param2.add(new BasicNameValuePair("group_id",pref.getString("chat_app_group_id","group_id")));
//        param2.add(new BasicNameValuePair("from_id",pref.getString("key_username","userid") ));
//        param2.add(new BasicNameValuePair("from_type","E" ));
//


        String selectQuery = null;
        Cursor cursor = null;


        //********************************  send check in check out  voice recording ***************
        try {


            selectQuery = "SELECT * FROM " + dh.TABLE_CHECK_IN_OUT

                    + " WHERE " + dh.KEY_PERNR + " = '" + pref.getString("key_username", "userid") + "'"
                    + " AND " + dh.KEY_CHAT_APP + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //   Log.d("db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {


                    key_id = cursor.getString(cursor.getColumnIndex(dh.KEY_ID));
                    msg = cursor.getString(cursor.getColumnIndex(dh.KEY_AUDIO_RECORD));


                    activity = cursor.getString(cursor.getColumnIndex(dh.KEY_HELP_NAME));

                    latitude = cursor.getString(cursor.getColumnIndex(dh.KEY_CHECK_OUT_LATITUDE));
                    longitude = cursor.getString(cursor.getColumnIndex(dh.KEY_CHECK_OUT_LONGITUDE));


                    String CurrentString = activity;
                    String[] separated;
                    separated = CurrentString.split("--");
                    activity = separated[0];


                    if (!latitude.equalsIgnoreCase("0.0")) {
                        location = "http://maps.google.com/maps?&z=20&q=loc:" + latitude + "," + longitude;
                    }
//


                    String msg1 = "Customer :" + cursor.getString(cursor.getColumnIndex(dh.KEY_PARTNER_NAME)) + "\n" +
                            "Place :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DISTRICT_TXT)) + "\n" +
                            "Activity :" + activity + "\n" +
                            "Comment :" + cursor.getString(cursor.getColumnIndex(dh.KEY_COMMENT)) + "\n" +

                            "Date :" + cursor.getString(cursor.getColumnIndex(dh.KEY_DATE_OUT)) + "    " +
                            "Time :" + cursor.getString(cursor.getColumnIndex(dh.KEY_TIME_OUT)) + "\n" + "\n" +
                            "Location :" + location;

                    //   Log.d("msg1",msg1);


                    param2.clear();

                    param2.add(new BasicNameValuePair("message", msg1));
                    param2.add(new BasicNameValuePair("attachment", msg));
                    param2.add(new BasicNameValuePair("file_name", "Shakti_p" + pref.getString("key_username", "userid") + System.currentTimeMillis() + ".mp3"));


                    param2.add(new BasicNameValuePair("username", username));
                    param2.add(new BasicNameValuePair("password", password));

                    param2.add(new BasicNameValuePair("group_id", group_id));

                    // param2.add(new BasicNameValuePair("group_id","99999001"));

                    param2.add(new BasicNameValuePair("from_id", pref.getString("key_username", "userid")));
                    param2.add(new BasicNameValuePair("from_type", "E"));


                    try {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        String obj2 = CustomHttpClient.executeHttpPost1(api, param2);


                        if (obj2 != "") {


                            //dh.updateAudioChatApp(DatabaseHelper.TABLE_CHECK_IN_OUT,key_id);

                            dh.insertUnsyncDataChatApp(DatabaseHelper.TABLE_CHECK_IN_OUT, key_id);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cursor != null) {
                cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }


    }


    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }



}


