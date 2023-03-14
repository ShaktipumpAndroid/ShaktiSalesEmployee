package webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import activity.CustomUtility;
import bean.BeanProduct;
import bean.LoginBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 10/19/2016.
 */

@SuppressWarnings("resource")
public class SAPWebService {

    public static final int MODE_PRIVATE = 0;
    ArrayList<String> al;
    String matnr, kunnr, vkorg, vtweg, extwg, maktx, kbetr, mtart, konwa;
    //get user id of person
    String userid = LoginBean.getUseid();
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    /**************************** create attendance table ***********************************************/
    // Create DatabaseHelper instance
    public int getAttendanceData(Context context) {
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            policy = new StrictMode.ThreadPolicy.Builder().build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(policy);
        }
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.ATTENDANCE_PAGE, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("attendance");
            dataHelper.deleteAttendance();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                dataHelper.insertAttendance(userid,
                        jo.getString("begdat"),
                        jo.getString("indz"),
                        jo.getString("iodz"),
                        jo.getString("totdz"),
                        jo.getString("atn_status"),
                        jo.getString("leave_typ"));
            }
            progressBarStatus = 20;
        } catch (Exception E) {
            progressBarStatus = 20;
        }
        return progressBarStatus;
    }

    /**************************** create route detail ***********************************************/
    // Create DatabaseHelper instance
    public int getRouteDetail(Context context) {
        Log.d("getRouteDetail","") ;
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.ROUTE_DETAIL, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("route_detail");
            dataHelper.deleteRouteDetail();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                dataHelper.insertRouteDetail(userid,
                        jo.getString("doc_date"),
                        jo.getString("route_code"),
                        jo.getString("route_name"),
                        jo.getString("kunnr"),
                        jo.getString("partner"),
                        jo.getString("partner_class"),
                        jo.getString("latitude"),
                        jo.getString("longitude"),
                        jo.getString("partner_name"),
                        jo.getString("land1"),
                        jo.getString("land_txt"),
                        jo.getString("state_code"),
                        jo.getString("state_txt"),
                        jo.getString("district_code"),
                        jo.getString("district_txt"),
                        jo.getString("taluka_code"),
                        jo.getString("taluka_txt"),
                        jo.getString("address"),
                        jo.getString("email"),
                        jo.getString("mob_no"),
                        jo.getString("tel_number"),
                        jo.getString("pincode"),
                        jo.getString("contact_person"),
                        jo.getString("distributor_code"),
                        jo.getString("distributor_name"),
                        jo.getString("phone_number"),
                        jo.getString("vkorg"),
                        jo.getString("vtweg"),
                        jo.getString("interested"),
                        jo.getString("ktokd")
                );
            }
            // parsing partner type & class json
            JSONArray partner_type_class = jsonObj.getJSONArray("partner_type_class");
            dataHelper.deletePartnerTypeClassHelp();
            for (int i = 0; i < partner_type_class.length(); i++) {
                JSONObject jo = partner_type_class.getJSONObject(i);
                dataHelper.insertPartnerTypeClassHelp(
                        jo.getString("partner"),
                        jo.getString("partner_txt"),
                        jo.getString("partner_class"),
                        jo.getString("partner_class_txt"));
            }
//   parsing area distributor j son *************************/
            JSONArray area_distributor = jsonObj.getJSONArray("area_distributor");
            dataHelper.deleteAreaDistributor();
            for (int i = 0; i < area_distributor.length(); i++) {
                JSONObject jo = area_distributor.getJSONObject(i);
                dataHelper.insertAreaDistributor(
                        jo.getString("distributor_code"),
                        jo.getString("distributor_name"),
                        jo.getString("land_txt"),
                        jo.getString("state_txt"),
                        jo.getString("district_txt"),
                        jo.getString("taluka_txt")
                );
                //Log.d("bean2",""+jo.getString("distributor_name")+"--"+jo.getString("state_txt"));
            }
// ***********************************   parsing adhoc order customer json *************************/
            JSONArray adhoc_order_customer = jsonObj.getJSONArray("adhoc_order_customer");
            //   Log.d("adhoc_order_customer",""+adhoc_order_customer  ) ;
            dataHelper.deleteAdhocOrderCustomer();
            for (int i = 0; i < adhoc_order_customer.length(); i++) {
                JSONObject jo = adhoc_order_customer.getJSONObject(i);
                dataHelper.insertAdhocOrderCustomer(
                        jo.getString("pernr"),
                        jo.getString("phone_number"),
                        jo.getString("partner_name"),
                        jo.getString("partner_class"),
                        jo.getString("partner_type"),
                        jo.getString("country"),
                        jo.getString("district")
                );
            }
            // *********************************** parsing partner view survey  ************************/
            JSONArray view_survey = jsonObj.getJSONArray("view_survey");
            dataHelper.deleteSurveyView();
            for (int i = 0; i < view_survey.length(); i++) {
                JSONObject jo = view_survey.getJSONObject(i);
                dataHelper.insertViewSurvey(
                        jo.getString("ename"),
                        jo.getString("erdat"),
                        jo.getString("remark"),
                        jo.getString("outer_url"),
                        jo.getString("inner_url"),
                        jo.getString("other_url"),
                        jo.getString("owner_url"),
                        jo.getString("card_url"),
                        jo.getString("phone_number"),
                        "sap"
                );
            }
            progressBarStatus = 100;
        } catch (Exception E) {
            progressBarStatus = 100;
        }
        return progressBarStatus;
    }

    /**************************** create target table ***********************************************/
    // Create DatabaseHelper instance
    public int getTargetData(Context context) {
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
       // param.add(new BasicNameValuePair("PERNR", "00003894"));

        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.TARGET_PAGE, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("target");
            dataHelper.deleteTarget();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                // login = jo.getString("LOGIN");
                dataHelper.insertTarget(jo.getString("begda"),
                        jo.getString("endda"),
                        jo.getString("fr_pernr"),
                        jo.getString("fr_ename"),
                        jo.getString("fr_department"),
                        jo.getString("fr_target"),
                        jo.getString("fr_net_sale"),
                        jo.getString("fr_position"));

            }

//           ***********   get activity target ***********
            JSONArray activity_ja = jsonObj.getJSONArray("activity_target");
            Log.d("activity_ja",""+activity_ja) ;
            dataHelper.deleteActivityTarget();
            for (int i = 0; i < activity_ja.length(); i++) {
                JSONObject activity_jo = activity_ja.getJSONObject(i);
                dataHelper.insertActivityTarget(activity_jo.getString("begda"),
                        activity_jo.getString("endda"),
                        activity_jo.getString("pernr"),
                        activity_jo.getString("ename"),
                        activity_jo.getString("activity"),
                        activity_jo.getString("vtext"),
                        activity_jo.getString("target_i"),
                        activity_jo.getString("ach_i"),
                        activity_jo.getString("target_h"),
                        activity_jo.getString("ach_h"));
            }
            progressBarStatus = 60;
        } catch (Exception E) {
            progressBarStatus = 60;
        }
        return progressBarStatus;
    }

    /**************************** create visit history ***********************************************/

    // Create DatabaseHelper instance
    public int getVisitHistory(Context context) {
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.VISIT_HISTORY_PAGE, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("visit_history");
            dataHelper.deleteVisitHistory();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                dataHelper.insertvisitHistory(jo.getString("date"),
                        jo.getString("time"),
                        jo.getString("comment"),
                        jo.getString("person"),
                        jo.getString("phone_number"),
                        jo.getString("partner_name"),
                        jo.getString("visit"),
                        jo.getString("audio_record"));
            }
            progressBarStatus = 60;
        } catch (Exception E) {
            progressBarStatus = 60;
        }
        return progressBarStatus;
    }

    /**************************** create search help table ***********************************************/

    // Create DatabaseHelper instance
    public int getSearchHelp(Context context) {
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));

        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.SEARCH_HELP_PAGE, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("app_search_help");
            dataHelper.deleteDSREntryHelp();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                dataHelper.insertDsrEntryHelp(
                        jo.getString("help_code"),
                        jo.getString("help_name"));
            }

            // *********************************** parsing video gallery json ************************/
            JSONArray video_gallery = jsonObj.getJSONArray("video_gallery");
            dataHelper.deleteVideoGallery();
            for (int i = 0; i < video_gallery.length(); i++) {
                JSONObject jo = video_gallery.getJSONObject(i);
                dataHelper.insertVideoGallery(
                        jo.getString("type"),
                        jo.getString("link_name"),
                        jo.getString("video_id"));
            }

            // *********************************** parsing chat app api ************************/
            JSONArray chat_app = jsonObj.getJSONArray("chat_app_api");
            pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            editor = pref.edit();
            for (int i = 0; i < chat_app.length(); i++) {
                JSONObject jo = chat_app.getJSONObject(i);
                editor.putString("chat_app_username", jo.getString("username"));
                editor.putString("chat_app_password", jo.getString("password"));
                editor.putString("chat_app_api", jo.getString("api"));
                editor.putString("chat_app_group_id", jo.getString("group_id"));
                editor.commit(); //
            }

//           *********************** multiple group api **** ******************************
            JSONArray chat_group = jsonObj.getJSONArray("chat_app_group");
            dataHelper.deleteChatAppGroup();
            for (int i = 0; i < chat_group.length(); i++) {
                JSONObject jo = chat_group.getJSONObject(i);
                dataHelper.insertChatAppGroup(
                        userid,
                        jo.getString("username"),
                        jo.getString("password"),
                        jo.getString("api"),
                        jo.getString("group_id")
                );
            }
            progressBarStatus = 80;
        } catch (Exception E) {
            progressBarStatus = 80;
        }
        return progressBarStatus;
    }


// get material detail

    public int getMaterialDetail(Context context) {

        //  Log.d("point","check point");
        int progressBarStatus;
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        //dataHelper.deleteAdhocOrder();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("pernr", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.ORDER_MATERIAL_DATA, param);
            JSONArray ja_matnr = new JSONArray(obj);
            // Log.d("json55",""+obj);
            for (int i = 0; i < ja_matnr.length(); i++) {
                JSONObject jo_matnr = ja_matnr.getJSONObject(i);
                matnr = jo_matnr.optString("matnr");
                kunnr = jo_matnr.optString("kunnr");
                vkorg = jo_matnr.optString("vkorg");
                vtweg = jo_matnr.optString("vtweg");
                extwg = jo_matnr.optString("extwg");
                maktx = jo_matnr.optString("maktx");
                kbetr = jo_matnr.optString("kbetr");
                konwa = jo_matnr.optString("konwa");
                mtart = jo_matnr.optString("mtart");

                BeanProduct beanProduct = new BeanProduct(matnr, kunnr, vkorg, vtweg, extwg, maktx, kbetr, konwa, mtart);
                dataHelper.createProduct(beanProduct);
                // take order
                // new Capture_employee_gps_location(context,"7");
            }
            progressBarStatus = 40;
        } catch (Exception e) {
            progressBarStatus = 40;
            Log.d("msg", "" + e);
        }
        return progressBarStatus;
    }

    /**************************** get material analysis ***********************************************/

    // Create DatabaseHelper instance
    public void getMaterialAnalysis(Context context) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.MATERIAL_ANALYSIS, param);

            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("material_analysis");

            //   Log.d("mat_ana",""+ja) ;


            dataHelper.deleteMaterialAnalysis();

            for (int i = 0; i < ja.length(); i++) {


                JSONObject jo = ja.getJSONObject(i);
                // login = jo.getString("LOGIN");

                dataHelper.insertMaterialAnalysis
                        (jo.getString("matnr"),
                                jo.getString("maktx"),
                                jo.getString("model"),
                                jo.getString("plant"),
                                jo.getString("indicator"),
                                jo.getString("delivery_time"),
                                jo.getString("kbetr"),
                                jo.getString("konwa"));
            }
        } catch (Exception E) {
            Log.e("","" + E);
        }
    }

    /**************************** get custome complaint ***********************************************/

    public void getCustomerComplaint(Context context) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
       //param.add(new BasicNameValuePair("PERNR", "00001432"));
        param.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(context,"objs")));

        System.out.println("URL comes in jsonparser class is (SAPWebServices) :  " + param);
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CUSTOMER_COMPLAINT, param);

            Log.e("complaint","data"+ obj.length());

            if (!obj.equalsIgnoreCase("")) {

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("complain_header");
                Log.e("complaint jsonObj ","data  "+ jsonObj.length());
                dataHelper.deleteCustomerComplaintHeader();

                System.out.println("jsonparser class size:  " + ja.length());

                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    Date date1;
                    Date date2;

                    SimpleDateFormat curdt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    String CurrentDate= curdt.format(new Date());
                    String FinalDate1=formateDate(jo.getString("cmpdt"));
                    String dayDifference = null;

                    try {
                        date1 = curdt.parse(CurrentDate);
                        date2 = curdt.parse(FinalDate1);
                        long difference = 0;
                        if (date1 != null) {
                            if (date2 != null) {
                                difference = Math.abs(date1.getTime() - date2.getTime());
                            }
                        }
                        long differenceDates = difference / (24 * 60 * 60 * 1000);
                        dayDifference = Long.toString(differenceDates);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    dataHelper.insertZCMPLNHDR(jo.getString("cmpno"),
                                    jo.getString("cmpdt"),
                                    jo.getString("caddress"),
                                    jo.getString("mblno"),
                                    jo.getString("mblno1"),
                                    jo.getString("cstname"),
                                    jo.getString("kunnr"),
                                    jo.getString("name1"),
                                    jo.getString("pernr"),
                                    jo.getString("ename"),
                                    jo.getString("catgry"),
                                    jo.getString("status"),
                                    jo.getString("edit"),
                                    jo.getString("epc"),
                                    dayDifference,
                                    jo.getString("cmp_pen_re"),
                                    jo.getString("fdate"),
                                    jo.getString("await_apr_pernr"),
                                    jo.getString("await_apr_pernr_nm"),
                                    jo.getString("pend_apr_pernr"),
                                    jo.getString("pend_apr_pernr_nm"),
                                    jo.getString("await_approval"),
                                    jo.getString("pend_approval"),
                                    jo.getString("await_apr_remark"),
                                    jo.getString("pend_apr_remark")

                            );
                }
//  *******************   complaint detail ********************************************
                JSONArray ja_dtl = jsonObj.getJSONArray("complain_detail");

                dataHelper.deleteCustomerComplaintDetail();

                for (int i = 0; i < ja_dtl.length(); i++) {
                    JSONObject jo = ja_dtl.getJSONObject(i);

                    dataHelper.insertZCMPLNDTL
                            (jo.getString("cmpno"),
                                    jo.getString("posnr"),
                                    jo.getString("matnr"),
                                    jo.getString("maktx"),
                                    jo.getString("reason"),
                                    jo.getString("warrantee"),
                                    jo.getString("sernr"),
                                    jo.getString("closer_reason"),
                                    jo.getString("defect"),
                                    jo.getString("extwg"),
                                    jo.getString("payment_by"),
                                    jo.getString("cusamt"),
                                    jo.getString("delamt"),
                                    jo.getString("comamt"),
                                    jo.getString("re_comp"),
                                    jo.getString("re_del"),
                                    jo.getString("pay_to_freelancer"),
                                    jo.getString("history"),
                                    jo.getString("vbeln"),
                                    jo.getString("fkdat"),
                                    jo.getString("insurance_txt"),
                                    jo.getString("warranty_condition"),
                                    jo.getString("cmpln_related_to"),
                                    jo.getString("war_date")

                            );
                }

// **********************  complaint category *******************************************

                JSONArray ja_category = jsonObj.getJSONArray("complain_category");

                dataHelper.deleteCategory();

                for (int i = 0; i < ja_category.length(); i++) {
                    JSONObject jo = ja_category.getJSONObject(i);

                    dataHelper.insertZCMPLN_CATEGORY
                            (jo.getString("category"));
                }


                // ***************************  complaint defect type ********************************

                JSONArray ja_defect = jsonObj.getJSONArray("complain_defect");

                dataHelper.deleteDefect();

                for (int i = 0; i < ja_defect.length(); i++) {
                    JSONObject jo = ja_defect.getJSONObject(i);

                    dataHelper.insertZCMPLN_DEFECT_TYPE
                            (jo.getString("defect"));
                }

                // ***************************  complaint related to ********************************

                JSONArray ja_cmp_relt_to = jsonObj.getJSONArray("complain_related_to");

                dataHelper.deleteReltto();

                for (int i = 0; i < ja_cmp_relt_to.length(); i++) {
                    JSONObject jo = ja_cmp_relt_to.getJSONObject(i);

                    dataHelper.insertZCMPLN_RELT_TO(jo.getString("cmpln_related_to"));
                }

                // ***************************  complaint closer reason ********************************

                JSONArray ja_closer = jsonObj.getJSONArray("complain_closer");

                //Log.d("closer",""+ ja_closer );

                dataHelper.deleteCloser();

                for (int i = 0; i < ja_closer.length(); i++) {
                    JSONObject jo = ja_closer.getJSONObject(i);

                    dataHelper.insertZCMPLN_CLOSER
                            (jo.getString("extwg"),
                                    jo.getString("reason")
                            );
                }


// **********************  complaint action  *******************************************

                JSONArray ja_action = jsonObj.getJSONArray("complain_action");

                dataHelper.deleteComplaint_action();

                for (int i = 0; i < ja_action.length(); i++) {
                    JSONObject jo_action = ja_action.getJSONObject(i);

                    dataHelper.insertComplaintAction
                            (jo_action.getString("pernr"),
                                    jo_action.getString("ename"),
                                    jo_action.getString("cmpno"),
                                    jo_action.getString("fdate"),
                                    jo_action.getString("action"),
                                    jo_action.getString("aedtm"),
                                    jo_action.getString("chtm"),
                                    jo_action.getString("status")
                            );
                }


// **********************  complaint image  *******************************************

                JSONArray ja_image = jsonObj.getJSONArray("complain_image");

                Log.d("ja_image", "" + ja_image);

                dataHelper.deleteComplaint_image();

                for (int i = 0; i < ja_image.length(); i++) {
                    JSONObject jo_image = ja_image.getJSONObject(i);

                    dataHelper.insertComplaintImageName
                            (jo_image.getString("catgry"),
                                    jo_image.getString("item"),
                                    jo_image.getString("name")
                            );
                }

                // **********************  complaint pending reason  *******************************************

                JSONArray ja_pend_reason = jsonObj.getJSONArray("pending_reason");
                dataHelper.deletePending_reason();
                for (int i = 0; i < ja_pend_reason.length(); i++) {
                    JSONObject jo_pdf = ja_pend_reason.getJSONObject(i);
                    dataHelper.insertpendingreason
                            (jo_pdf.getString("cmp_pen_re"),jo_pdf.getString("name"));
                }
//           *********************** multiple chat app group api **** ******************************
                JSONArray chat_group = jsonObj.getJSONArray("chat_app_group");
                dataHelper.deleteChatAppGroup();
                for (int i = 0; i < chat_group.length(); i++) {
                    JSONObject jo = chat_group.getJSONObject(i);
                    dataHelper.insertChatAppGroup(
                            userid,
                            jo.getString("username"),
                            jo.getString("password"),
                            jo.getString("api"),
                            jo.getString("group_id")
                    );
                }
            }
        } catch (Exception E) {
          E.printStackTrace();
        }
    }

    public void getCustomerComplaintVKFinal(Context context ) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", WebURL.MAIN_USER_ID));
        param.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(context,"objs")));
        System.out.println("param==>>"+param);
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CUSTOMER_COMPLAINT_FINAL1, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("complain_header");
            dataHelper.deleteCustomerComplaintHeaderVK();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Date date1;
                Date date2;
                SimpleDateFormat curdt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                String CurrentDate= curdt.format(new Date());
                String FinalDate1=formateDate(jo.getString("cmpdt"));
                String dayDifference = null;
                try {
                    date1 = curdt.parse(CurrentDate);
                    date2 = curdt.parse(FinalDate1);
                    long difference = 0;
                    if (date1 != null) {
                        if (date2 != null) {
                            difference = Math.abs(date1.getTime() - date2.getTime());
                        }
                    }
                    long differenceDates = difference / (24 * 60 * 60 * 1000);
                    dayDifference = Long.toString(differenceDates);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataHelper.insertZCMPLNHDR_VK(jo.getString("cmpno"),
                                jo.getString("cmpdt"),
                                jo.getString("caddress"),
                                jo.getString("mblno"),
                                jo.getString("mblno1"),
                                jo.getString("cstname"),
                                jo.getString("kunnr"),
                                jo.getString("name1"),
                                jo.getString("pernr"),
                                jo.getString("ename"),
                                jo.getString("catgry"),
                                jo.getString("status"),
                                jo.getString("edit"),
                                jo.getString("epc"),
                                dayDifference,
                                jo.getString("cmp_pen_re"),
                                jo.getString("fdate"),
                                jo.getString("await_apr_pernr"),
                                jo.getString("await_apr_pernr_nm"),
                                jo.getString("pend_apr_pernr"),
                                jo.getString("pend_apr_pernr_nm"),
                                jo.getString("await_approval"),
                                jo.getString("pend_approval"),
                                jo.getString("await_apr_remark"),
                                jo.getString("pend_apr_remark")

                        );
            }
//  *******************   complaint detail ********************************************
            JSONArray ja_dtl = jsonObj.getJSONArray("complain_detail");
            dataHelper.deleteCustomerComplaintDetailVK();
            for (int i = 0; i < ja_dtl.length(); i++) {
                JSONObject jo = ja_dtl.getJSONObject(i);
                dataHelper.insertZCMPLNDTL_VK
                        (jo.getString("cmpno"),
                                jo.getString("posnr"),
                                jo.getString("matnr"),
                                jo.getString("maktx"),
                                jo.getString("reason"),
                                jo.getString("warrantee"),
                                jo.getString("sernr"),
                                jo.getString("closer_reason"),
                                jo.getString("defect"),
                                jo.getString("extwg"),
                                jo.getString("payment_by"),
                                jo.getString("cusamt"),
                                jo.getString("delamt"),
                                jo.getString("comamt"),
                                jo.getString("re_comp"),
                                jo.getString("re_del"),
                                jo.getString("pay_to_freelancer"),
                                jo.getString("history"),
                                jo.getString("vbeln"),
                                jo.getString("fkdat"),
                                jo.getString("insurance_txt"),
                                jo.getString("warranty_condition"),
                                jo.getString("cmpln_related_to"),
                                jo.getString("war_date")

                        );
            }

// **********************  complaint category *******************************************
            JSONArray ja_category = jsonObj.getJSONArray("complain_category");
            dataHelper.deleteCategoryVK();
            for (int i = 0; i < ja_category.length(); i++) {
                JSONObject jo = ja_category.getJSONObject(i);
                dataHelper.insertZCMPLN_CATEGORYVK
                        (jo.getString("category"));
            }

            // ***************************  complaint defect type ********************************
            JSONArray ja_defect = jsonObj.getJSONArray("complain_defect");
            dataHelper.deleteDefectVK();
            for (int i = 0; i < ja_defect.length(); i++) {
                JSONObject jo = ja_defect.getJSONObject(i);
                dataHelper.insertZCMPLN_DEFECT_TYPEVK
                        (jo.getString("defect"));
            }
            // ***************************  complaint related to ********************************
            JSONArray ja_cmp_relt_to = jsonObj.getJSONArray("complain_related_to");
            dataHelper.deleteRelttoVK();
            for (int i = 0; i < ja_cmp_relt_to.length(); i++) {
                JSONObject jo = ja_cmp_relt_to.getJSONObject(i);
                dataHelper.insertZCMPLN_RELT_TOVK(jo.getString("cmpln_related_to"));
            }
            // ***************************  complaint closer reason ********************************
            JSONArray ja_closer = jsonObj.getJSONArray("complain_closer");
            dataHelper.deleteCloserVK();
            for (int i = 0; i < ja_closer.length(); i++) {
                JSONObject jo = ja_closer.getJSONObject(i);

                dataHelper.insertZCMPLN_CLOSERVK
                        (jo.getString("extwg"),
                                jo.getString("reason")
                        );
            }

// **********************  complaint action  *******************************************
            JSONArray ja_action = jsonObj.getJSONArray("complain_action");
            dataHelper.deleteComplaint_actionVK();
            for (int i = 0; i < ja_action.length(); i++) {
                JSONObject jo_action = ja_action.getJSONObject(i);
                dataHelper.insertComplaintActionVK
                        (jo_action.getString("pernr"),
                                jo_action.getString("ename"),
                                jo_action.getString("cmpno"),
                                jo_action.getString("fdate"),
                                jo_action.getString("action"),
                                jo_action.getString("aedtm"),
                                jo_action.getString("chtm"),
                                jo_action.getString("status")
                        );
            }

// **********************  complaint image  *******************************************
            JSONArray ja_image = jsonObj.getJSONArray("complain_image");
            Log.d("ja_image", "" + ja_image);
            dataHelper.deleteComplaint_imageVK();
            for (int i = 0; i < ja_image.length(); i++) {
                JSONObject jo_image = ja_image.getJSONObject(i);

                dataHelper.insertComplaintImageNameVK
                        (jo_image.getString("catgry"),
                                jo_image.getString("item"),
                                jo_image.getString("name")
                        );
            }

            // **********************  complaint pending reason  *******************************************
            JSONArray ja_pend_reason = jsonObj.getJSONArray("pending_reason");
            Log.d("ja_pend_reason", "" + ja_pend_reason);
            Log.e("ja_pend_reason", "" + ja_pend_reason);
            dataHelper.deletePending_reasonVK();
            for (int i = 0; i < ja_pend_reason.length(); i++) {
                JSONObject jo_pdf = ja_pend_reason.getJSONObject(i);
                dataHelper.insertpendingreasonVK
                        (jo_pdf.getString("cmp_pen_re"),jo_pdf.getString("name"));
            }

//           *********************** multiple chat app group api **** ******************************
            JSONArray chat_group = jsonObj.getJSONArray("chat_app_group");
            dataHelper.deleteChatAppGroupVK();
            for (int i = 0; i < chat_group.length(); i++) {
                JSONObject jo = chat_group.getJSONObject(i);
                dataHelper.insertChatAppGroupVK(
                        userid,
                        jo.getString("username"),
                        jo.getString("password"),
                        jo.getString("api"),
                        jo.getString("group_id")
                );
            }
        } catch (Exception E) {
          E.printStackTrace();
        }
    }
    // get serial no history
    public String getComplaintSerailNoHistory(Context context, String cmp_no, String cmp_sernr, String cmp_matnr) {
        String lv_cmpno = "null";
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        param.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(context,"objs")));
        param.add(new BasicNameValuePair("CMPNO", cmp_no));
        param.add(new BasicNameValuePair("SERNR", cmp_sernr));
        param.add(new BasicNameValuePair("MATNR", cmp_matnr));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CUSTOMER_COMPLAINT_SERIAL_NUMBER, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("serial_no_complain_header");
            dataHelper.deleteSerialNumberComplaintHeader();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                lv_cmpno = jo.getString("cmpno");
                dataHelper.insertSerial_Number_ZCMPLNHDR
                        (jo.getString("cmpno"),
                                jo.getString("cmpdt"),
                                jo.getString("caddress"),
                                jo.getString("mblno"),
                                jo.getString("cstname"),
                                jo.getString("kunnr"),
                                jo.getString("name1"),
                                jo.getString("pernr"),
                                jo.getString("ename"),
                                jo.getString("catgry"),
                                jo.getString("status"),
                                jo.getString("edit")
                        );
            }
//  *******************   complaint detail ********************************************
            JSONArray ja_dtl = jsonObj.getJSONArray("serial_no_complain_detail");
            dataHelper.deleteSerialNumberCustomerComplaintDetail();
            for (int i = 0; i < ja_dtl.length(); i++) {
                JSONObject jo = ja_dtl.getJSONObject(i);
                dataHelper.insertSerial_Number_ZCMPLNDTL
                        (jo.getString("cmpno"),
                                jo.getString("posnr"),
                                jo.getString("matnr"),
                                jo.getString("maktx"),
                                jo.getString("reason"),
                                jo.getString("warrantee"),
                                jo.getString("sernr"),
                                jo.getString("closer_reason"),
                                jo.getString("defect"),
                                jo.getString("extwg"),
                                jo.getString("payment_by"),
                                jo.getString("cusamt"),
                                jo.getString("delamt"),
                                jo.getString("comamt"),
                                jo.getString("re_comp"),
                                jo.getString("re_del")
                        );
            }
        } catch (Exception E) {
            Log.e("",""+E);

        }
        return lv_cmpno;
    }

    /**************************** get service center list ***********************************************/
    public void getServiceCenterList(Context context) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.SERVICE_CENTER_LIST, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja = jsonObj.getJSONArray("service_center");
            dataHelper.deleteServiceCenter();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                dataHelper.insertServiceCenter
                        (jo.getString("pernr"),
                                jo.getString("ename"),
                                jo.getString("kunnr"),
                                jo.getString("name1"),
                                jo.getString("land"),
                                jo.getString("land_txt"),
                                jo.getString("regio"),
                                jo.getString("state_txt"),
                                jo.getString("city"),
                                jo.getString("district_txt"),
                                jo.getString("taluka"),
                                jo.getString("taluka_txt"),
                                jo.getString("mobno"),
                                jo.getString("telf2"),
                                jo.getString("contact_person"),
                                jo.getString("address"),
                                jo.getString("pincode"),
                                jo.getString("email"),
                                jo.getString("lat_long")
                        );
            }
        } catch (Exception E) {
            Log.e("",""+E);
        }
    }

    /************************************ country ***********************************************/
    public void getLocationData(Context context) {
        DatabaseHelper dataHelper = new DatabaseHelper(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", userid));
        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.LOCATION_HELP, param);
            JSONObject jsonObj = new JSONObject(obj);
            JSONArray ja_country = jsonObj.getJSONArray("help_country");
            dataHelper.deleteCountry();
            for (int i = 0; i < ja_country.length(); i++) {
                JSONObject jo_country = ja_country.getJSONObject(i);
                dataHelper.insertCountry
                        (
                                jo_country.getString("land1"),
                                jo_country.getString("landx")
                        );
            }
            JSONArray ja_state = jsonObj.getJSONArray("help_state");
            dataHelper.deleteState();
            for (int i = 0; i < ja_state.length(); i++) {
                JSONObject jo_state = ja_state.getJSONObject(i);
                dataHelper.insertState
                        (
                                jo_state.getString("land1"),
                                jo_state.getString("bland"),
                                jo_state.getString("bezei")
                        );
            }
            JSONArray ja_city = jsonObj.getJSONArray("help_city");
            dataHelper.deleteCity();
            for (int i = 0; i < ja_city.length(); i++) {
                JSONObject jo_city = ja_city.getJSONObject(i);
                dataHelper.insertCity
                        (
                                jo_city.getString("land1"),
                                jo_city.getString("regio"),
                                jo_city.getString("cityc"),
                                jo_city.getString("bezei")
                        );
            }
            JSONArray ja_tehsil = jsonObj.getJSONArray("help_tehsil");
            dataHelper.deleteTehsil();
            for (int i = 0; i < ja_tehsil.length(); i++) {
                JSONObject jo_tehsil = ja_tehsil.getJSONObject(i);
                dataHelper.insertTehsil
                        (
                                jo_tehsil.getString("land1"),
                                jo_tehsil.getString("regio"),
                                jo_tehsil.getString("district"),
                                jo_tehsil.getString("tehsil"),
                                jo_tehsil.getString("tehsil_text")
                        );
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("dd.MM.yyyy");
            Date mDate = formate.parse(date);
            SimpleDateFormat appFormate = new SimpleDateFormat("MM/dd/yyyy");
            assert mDate != null;
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    /**************************** create attendance table ***********************************************/
    // Create DatabaseHelper instance


}



