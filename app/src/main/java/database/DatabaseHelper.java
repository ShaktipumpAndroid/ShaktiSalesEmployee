package database;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import activity.CustomUtility;
import bean.AttendanceBean;
import bean.BeanProduct;
import bean.BeanProductFinal;
import bean.CallLog;
import bean.CheckInOutBean;
import bean.ClouserComplaint;
import bean.CmpReviewImageBean;
import bean.ComplaintAudio;
import bean.ComplaintImage;
import bean.ComplaintStart;
import bean.DsrEntryBean;
import bean.EmployeeGPSActivityBean;
import bean.ForwardForAppEntryBean;
import bean.InprocessComplaint;
import bean.LocalConvenienceBean;
import bean.LoginBean;
import bean.NewAddedCustomerBean;
import bean.NoOrderBean;
import bean.SurveyBean;

/**
 * Created by shakti on 10/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "db_sales_employee";
    // Database Version
    public static final int DATABASE_VERSION = 30;

    public static DatabaseHelper sInstance;
    // Table Names
    public static final String TABLE_LOGIN = "tbl_login";
    public static final String TABLE_ATTENDANCE = "tbl_attendance_report";
    public static final String TABLE_TARGET = "tbl_target";
    public static final String TABLE_ACTIVITY_TARGET = "tbl_activity_target";
    public static final String TABLE_SEARCH_HELP = "tbl_search_help";
    public static final String TABLE_DSR_ENTRY = "tbl_dsr_entry";
    public static final String TABLE_FRWD_APP_CMP = "tbl_frwd_app_cmp";
    public static final String TABLE_PEND_APP_CMP = "tbl_pend_app_cmp";
    public static final String TABLE_ROUTE_DETAIL = "tbl_route_detail";
    public static final String TABLE_NEW_ADDED_CUSTOMER = "tbl_new_added_customer";
    public static final String TABLE_CHECK_IN_OUT = "tbl_check_in_out";
    public static final String TABLE_CMP_ATTACHMENT = "tbl_cmpln_attachment";
    public static final String TABLE_PARTNER_TYPE_CLASS_HELP = "tbl_partner_type_class";

    public static final String TABLE_AREA_DISTRIBUTOR = "tbl_area_distributor";
    public static final String TABLE_MARK_ATTENDANCE = "tbl_mark_attendance";
    public static final String TABLE_NO_ORDER = "tbl_no_order";
    public static final String TABLE_ADHOC_ORDER_CUSTOMER = "tbl_adhoc_order_customer";
    public static final String TABLE_ADHOC_FINAL = "tbl_adhocorder_final";
    public static final String TABLE_EMPLOYEE_GPS_ACTIVITY = "tbl_employee_activity";
    public static final String TABLE_VISIT_HISTORY = "tbl_visit_history";
    public static final String TABLE_LOCAL_CONVENIENCE = "tbl_local_convenience";
    public static final String TABLE_SURVEY = "tbl_survey";
    public static final String TABLE_VIEW_SURVEY = "tbl_view_survey";
    public static final String TABLE_MATERIAL_ANALYSIS = "tbl_material_analysis";
    public static final String TABLE_VIDEO_GALLERY = "tbl_video_gallery";
    public static final String TABLE_ZCMPLNHDR = "tbl_zcmplnhdr";
    public static final String TABLE_ZCMPLNHDR_VK = "tbl_zcmplnhdr_vk";
    public static final String TABLE_ZCMPLNHDTL = "tbl_zcmplndtl";
    public static final String TABLE_ZCMPLNHDTL_VK = "tbl_zcmplndtl_vk";
    public static final String TABLE_SERAIL_NUMBER_ZCMPLNHDR = "tbl_serail_number_zcmplnhdr";
    public static final String TABLE_SERAIL_NUMBER_ZCMPLNHDTL = "tbl_serail_number_zcmplndtl";
    public static final String TABLE_COMPLAINT_AUDIO = "tbl_complaint_audio";
    public static final String TABLE_ZCMPLN_CATEGORY = "tbl_zcmpln_category";
    public static final String TABLE_ZCMPLN_CATEGORY_VK = "tbl_zcmpln_category_vk";
    public static final String TABLE_ZCMPLN_DEFECT = "tbl_zcmpln_defect";
    public static final String TABLE_ZCMPLN_DEFECT_VK = "tbl_zcmpln_defect_vk";
    public static final String TABLE_ZCMPLN_RELT_TO = "tbl_zcmpln_relt_to";
    public static final String TABLE_ZCMPLN_RELT_TO_VK = "tbl_zcmpln_relt_to_vk";
    public static final String TABLE_ZCMPLN_CLOSER = "tbl_zcmpln_closer";
    public static final String TABLE_ZCMPLN_CLOSER_VK = "tbl_zcmpln_closer_vk";
    public static final String TABLE_ZINPROCESS_COMPLAINT = "tbl_zinprocess_complaint";
    public static final String TABLE_COMPLAINT_ACTION = "tbl_complaint_action";
    public static final String TABLE_COMPLAINT_ACTION_VK = "tbl_complaint_action_vk";
    public static final String TABLE_PENDING_REASON = "tbl_complaint_pending_reason";
    public static final String TABLE_PENDING_REASON_VK = "tbl_complaint_pending_reason_vk";
    public static final String TABLE_COMPLAINT_IMAGE_NAME = "tbl_complaint_image_name";
    public static final String TABLE_COMPLAINT_IMAGE_NAME_VK = "tbl_complaint_image_name_vk";
    //public static final String TABLE_COMPLAINT_PDF_NAME = "tbl_complaint_pdf_name";
    public static final String TABLE_CLOSE_COMPLAINT = "tbl_close_complaint";
    public static final String TABLE_SERVICE_CENTER = "tbl_service_center";
    public static final String TABLE_COUNTRY = "tbl_country";
    public static final String TABLE_STATE = "tbl_sate";
    public static final String TABLE_CITY = "tbl_city";
    public static final String TABLE_TEHSIL = "tbl_tehsil";
    public static final String TABLE_ZCMPLN_IMAGE = "tbl_zcmpln_image";
    public static final String TABLE_CALL_LOG = "tbl_call_log";
    public static final String TABLE_CHAT_APP = "tbl_chat_app";
    public static final String TABLE_CHAT_APP_VK = "tbl_chat_app_vk";
    public static final String TABLE_DATA_SYNC_CHAT_APP = "tbl_data_sync_chat_app";
    public static final String TABLE_COMPLAINT_DISTANCE = "tbl_complaint_distance";
    public static final String TABLE_REVIEW_COMPLAINT_IMAGES = "tbl_review_complaint_images";
    // Common column names
    public static final String KEY_ID = "id";
    // attendance Table - column name
    public static final String KEY_PERNR = "pernr";
    public static final String KEY_ENAME = "ename";
    public static final String KEY_FOLLOW_UP_DATE = "follow_up_date";
    public static final String KEY_CONVERSION_STATUS = "conversion_status";
    public static final String KEY_SAVE_BY = "save_by";
    public static final String KEY_FR_PERNR = "fr_pernr";
    public static final String KEY_TO_PERNR = "to_pernr";
    public static final String KEY_HELP_NAME = "help_name";
    public static final String KEY_BUDAT = "budat";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_AUDIO_RECORD = "audio_record";
    public static final String KEY_IMAGE = "cmp_image";
    public static final String KEY_PDF1 = "cmp_pdf1";
    public static final String KEY_PDF2 = "cmp_pdf2";
    public static final String KEY_ROUTE_NAME = "route_name";
    public static final String KEY_PARTNER_NAME = "partner_name";
    public static final String KEY_LAND1 = "land1";
    public static final String KEY_LAND_TXT = "land_txt";
    public static final String KEY_STATE_CODE = "state_code";
    public static final String KEY_STATE_TXT = "state_txt";
    public static final String KEY_DISTRICT_CODE = "district_code";
    public static final String KEY_DISTRICT_TXT = "district_txt";
    public static final String KEY_TALUKA_CODE = "taluka_code";
    public static final String KEY_TALUKA_TXT = "taluka_txt";
    public static final String KEY_DISTRIBUTOR_NAME = "distributor_name";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_CUSTOMER_CATAGORY = "customer_category"; // new = add by mobile app
    public static final String KEY_AADHAR_CARD = "aadhar_card";
    public static final String KEY_PAN_CARD = "pan_card";
    public static final String KEY_TIN_NO = "tin_no";
    public static final String KEY_MARKET_PLACE = "market_place";
    public static final String KEY_DOB = "dob";
    public static final String KEY_INTRESTED = "intrested";
    public static final String KEY_ADDED_AT_LATLONG = "added_at_latlong";
    public static final String KEY_VISIT = "visit";
    public static final String KEY_KTOKD = "ktokd";
    public static final String KEY_LAT_LONG = "lat_long";
    // check in check out table
    public static final String KEY_DATE_IN = "date_in";
    public static final String KEY_TIME_IN = "time_in";
    public static final String KEY_DATE_OUT = "date_out";
    public static final String KEY_TIME_OUT = "time_out";
    public static final String KEY_CHECK_OUT_LATITUDE = "check_out_latitude";
    public static final String KEY_CHECK_OUT_LONGITUDE = "check_out_longitude";
    public static final String KEY_PARTNER_TEXT = "partner_text";
    public static final String TYPE = "TYPE";


    public static final String KEY_BILL_NO = "vbeln";
    public static final String KEY_BILL_DATE = "fkdat";
    public static final String KEY_INSU_TXT = "insurance_txt";
    public static final String KEY_WARR_COND = "warranty_condition";
    public static final String KEY_WAR_DAT = "war_dat";


    // route plan table
    public static final String PERNR = "PERNR";
    public static final String BEGDA = "BEGDA";
    public static final String SERVER_DATE_IN = "SERVER_DATE_IN";
    public static final String SERVER_TIME_IN = "SERVER_TIME_IN";
    public static final String SERVER_DATE_OUT = "SERVER_DATE_OUT";
    public static final String SERVER_TIME_OUT = "SERVER_TIME_OUT";
    public static final String IN_ADDRESS = "IN_ADDRESS";
    public static final String OUT_ADDRESS = "OUT_ADDRESS";
    public static final String IN_TIME = "IN_TIME";
    public static final String OUT_TIME = "OUT_TIME";
    public static final String WORKING_HOURS = "WORKING_HOURS";
    public static final String IMAGE_DATA = "IMAGE_DATA";
    public static final String CURRENT_MILLIS = "CURRENT_MILLIS";
    public static final String IN_LAT_LONG = "IN_LAT_LONG";
    public static final String OUT_LAT_LONG = "OUT_LAT_LONG";
    public static final String IN_FILE_NAME = "IN_FILE_NAME";
    public static final String IN_FILE_LENGTH = "IN_FILE_LENGTH";
    public static final String IN_FILE_VALUE = "IN_FILE_VALUE";
    public static final String OUT_FILE_NAME = "OUT_FILE_NAME";
    public static final String OUT_FILE_LENGTH = "OUT_FILE_LENGTH";
    public static final String OUT_FILE_VALUE = "OUT_FILE_VALUE";
    public static final String IN_STATUS = "IN_STATUS";
    public static final String OUT_STATUS = "OUT_STATUS";
    public static final String IN_IMAGE = "IN_IMAGE";
    public static final String OUT_IMAGE = "OUT_IMAGE";
    public static final String KEY_EXTWG = "extwg";
    public static final String KEY_PERSON = "person";
    public static final String KEY_CR_TIME = "cr_time";
    public static final String KEY_OUTER_VIEW = "outer_view";
    public static final String KEY_INNER_VIEW = "inner_view";
    public static final String KEY_OTHER_VIEW = "other_view";
    public static final String KEY_OWNER_VIEW = "owner_view";
    public static final String KEY_CARD_VIEW = "card_view";
    public static final String KEY_PLANT = "plant";
    public static final String KEY_INDICATOR = "indicator";
    public static final String KEY_DELIVERY_TIME = "delivery_time";
    public static final String KEY_EXTRA1 = "extra1";
    public static final String KEY_EXTRA2 = "extra2";
    public static final String KEY_EXTRA3 = "extra3";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_CHAT_APP = "chat_app";
    // employee activity field
    public static final String KEY_EVENT = "event";
    public static final String KEY_VIDEO_TYPE = "video_type";
    public static final String KEY_VIDEO_NAME = "video_name";
    public static final String KEY_VIDEO_LINK = "video_link";


    // attendance table field
    public static final String KEY_CMPNO = "cmpno";
    public static final String KEY_STATUS = "status";
    public static final String KEY_REMARK = "remark";
    public static final String KEY_CMPDT = "cmpdt";
    public static final String KEY_POSNR = "posnr";
    public static final String KEY_REASON = "reason";
    public static final String KEY_PEND_NO = "cmp_pen_re";
    public static final String KEY_NAME = "name";
    public static final String KEY_CLOSER_RESON = "closer_reason";
    public static final String KEY_DEFECT = "defect";
    public static final String KEY_RELT_TO = "relt_to";
    public static final String KEY_WARRANTY = "warranty";
    public static final String KEY_SERNR = "sernr";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_ITEM = "image_item";
    public static final String KEY_PAY_FREELANCER = "pay_freelancer";
    public static final String KEY_PAY_COM = "re_company";
    public static final String KEY_PAY_DEA = "re_dealer";
    public static final String KEY_FOC_AMT = "focamt";
    public static final String KEY_PEN_DAY = "penday";
    public static final String KEY_PEN_RES = "pen_res";
    public static final String KEY_FLW_DT = "fdate";
    public static final String KEY_AWT_PERNR = "awtpernr";
    public static final String KEY_AWT_PERNR_NM = "awtpernrnm";
    public static final String KEY_PEND_PERNR = "pendpernr";
    public static final String KEY_PEND_PERNR_NM = "pendpernrnm";
    public static final String KEY_AWT_APR = "awtapr";
    public static final String KEY_PEND_APR = "pendapr";
    public static final String KEY_AWT_APR_RMK = "awtrmrk";
    public static final String KEY_PEND_APR_RMK = "pendrmrk";
    public static final String KEY_IMAGE1 = "image1";
    public static final String KEY_IMAGE2 = "image2";
    public static final String KEY_IMAGE3 = "image3";
    public static final String KEY_IMAGE4 = "image4";
    public static final String KEY_IMAGE5 = "image5";
    public static final String KEY_IMAGE6 = "image6";
    public static final String KEY_IMAGE7 = "image7";
    public static final String KEY_IMAGE8 = "image8";
    public static final String KEY_IMAGE9 = "image9";
    public static final String KEY_IMAGE10 = "image10";
    public static final String KEY_IMAGE11 = "image11";
    public static final String KEY_IMAGE12 = "image12";
    public static final String KEY_IMAGE13 = "image13";
    public static final String KEY_IMAGE14 = "image14";
    public static final String KEY_IMAGE15 = "image15";
    public static final String KEY_PAYMENT_BY = "payment_by";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_DEALER = "dealer";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_FEEDBACK_CUSTOMER = "customer_feedback";
    public static final String KEY_FEEDBACK_STATUS = "customer_status";
    public static final String KEY_CMPLN_STATUS = "cmpln_status";
    public static final String KEY_EDIT = "edit";
    public static final String KEY_EPC = "epc";
    public static final String KEY_DOWNLAOD_FROM = "download_from";
    public static final String KEY_CALL_TYPE = "call_type";
    public static final String KEY_CALL_DURATION = "call_duration";
    public static final String KEY_HISTORY = "sernr_history";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_API = "api";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_TABLE_NAME = "table_name";
    public static final String KEY_SYNC_ID = "sync_id";
    public static final String KEY_CELL_ID = "cell_id";
    public static final String KEY_LOCATION_CODE = "location_code";
    public static final String KEY_MOBILE_COUNTRY_CODE = "mobile_country_code";
    public static final String KEY_MOBILE_NETWORK_CODE = "mobile_network_code";

    public static final String CREATE_TABLE_MARK_ATTENDANCE = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_MARK_ATTENDANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + BEGDA + " TEXT,"
            + SERVER_DATE_IN + " TEXT,"
            + SERVER_TIME_IN + " TEXT,"
            + SERVER_DATE_OUT + " TEXT,"
            + SERVER_TIME_OUT + " TEXT,"
            + IN_ADDRESS + " TEXT,"
            + OUT_ADDRESS + " TEXT,"
            + IN_TIME + " TEXT,"
            + OUT_TIME + " TEXT,"
            + WORKING_HOURS + " TEXT,"
            + IMAGE_DATA + " TEXT,"
            + CURRENT_MILLIS + " TEXT,"
            + IN_LAT_LONG + " TEXT,"
            + OUT_LAT_LONG + " TEXT,"
            + IN_FILE_NAME + " TEXT,"
            + IN_FILE_LENGTH + " TEXT,"
            + IN_FILE_VALUE + " TEXT,"
            + OUT_FILE_NAME + " TEXT,"
            + IN_STATUS + " TEXT,"
            + OUT_STATUS + " TEXT,"
            + IN_IMAGE + " BLOB,"
            + OUT_IMAGE + " BLOB,"
            + OUT_FILE_LENGTH + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + OUT_FILE_VALUE + " TEXT" + ")";

    private static final String TABLE_ADHOC = "tbl_adhocorder";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_BEGDAT = "begdat";
    private static final String KEY_INDZ = "indz";
    private static final String KEY_IODZ = "iodz";
    private static final String KEY_TOTDZ = "totdz";
    private static final String KEY_ATN_STATUS = "atn_status";
    private static final String KEY_LEAVE_TYP = "leave_typ";
    private static final String KEY_SRV_CNT_BORD_IMG = "srv_cnt_bord_img";
    private static final String KEY_SRV_CNT_TRN_LTR_IMG = "srv_cnt_img";
    private static final String KEY_CERTIFICATE_IMG = "certificate_img";
    private static final String KEY_SLFY_SERV_PER = "slfy_serv_per";
    private static final String KEY_SPR_PRT_STK_IMG = "spr_prt_stk_img";
    private static final String KEY_PRD_TRN_IMG = "prd_trn_img";
    private static final String KEY_OTHR_IMG = "othr_img";
    // Target Table - column name
    private static final String KEY_BEGDA = "begda";
    private static final String KEY_ENDDA = "endda";
    private static final String KEY_FR_ENAME = "fr_ename";
    private static final String KEY_FR_DEPARTMENT = "fr_department";
    private static final String KEY_FR_TARGET = "fr_target";
    private static final String KEY_FR_NET_SALE = "fr_net_sale";
    private static final String KEY_FR_POSITION = "fr_position";
    private static final String KEY_INDV_ACT_TARGET = "indv_act_target";
    private static final String KEY_INDV_ACT_ACHIEVEMENT = "indv_act_achievement";
    private static final String KEY_HRCY_ACT_TARGET = "hrcy_act_target";
    private static final String KEY_HRCY_ACT_ACHIEVEMENT = "hrcy_act_achievement";
    private static final String KEY_ACTIVITY_CODE = "activity_code";
    private static final String KEY_ACTIVITY_NAME = "activity_name";
    // dsr entry Table - column name
    private static final String KEY_BTRTL = "btrtl";
    private static final String KEY_HELP_CODE = "help_code";

    // review complaint images
    private static final String KEY_DATABASE_STATUS = "db_status";
    private static final String KEY_ROUTE_CODE = "route_code";
    private static final String KEY_KUNNR = "kunnr";
    private static final String KEY_PARTNER = "partner";
    private static final String KEY_PARTNER_CLASS = "partner_class";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOB_NO = "mob_no";
    private static final String KEY_ALT_MOB_NO = "alt_mob_no";
    private static final String KEY_TEL_NUMBER = "tel_number";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CONTACT_PERSON = "contact_person";
    private static final String KEY_CONTACT_PERSON_PHONE = "contact_person_phone";
    private static final String KEY_DISTRIBUTOR_CODE = "distributor_code";
    private static final String KEY_CHECK_IN_LATITUDE = "check_in_latitude";
    private static final String KEY_CHECK_IN_LONGITUDE = "check_in_longitude";
    private static final String KEY_PARTNER_CLASS_TEXT = "partner_class_text";
    // Adhoc order table Common column names
    private static final String KEY_MATNR = "matnr";
    private static final String KEY_VKORG = "vkorg";
    private static final String KEY_VTWEG = "vtweg";
    private static final String KEY_MAKTX = "maktx";
    private static final String KEY_KBETR = "kbetr";
    private static final String KEY_MTART = "mtart";
    private static final String KEY_KONWA = "konwa";
    private static final String KEY_MENGE = "menge";
    private static final String KEY_TOT_KBETR = "tot_kbetr";
    private static final String KEY_CUSTOMER_NAME = "customer_name";
    private static final String KEY_CR_DATE = "cr_date";
    private static final String IMAGE = "IMAGE";
    private static final String IMAGE2 = "IMAGE1";
    private static final String KEY_AGENDA = "agenda";
    private static final String KEY_OUTCOMES = "outcomes";


    public static final String KEY_FROM_TIME = "start_time";
    public static final String KEY_TO_TIME = "end_time";
    private static final String KEY_FROM_LAT = "start_lat";
    private static final String KEY_TO_LAT = "end_lat";
    private static final String KEY_FROM_LNG = "start_long";
    private static final String KEY_TO_LNG = "end_long";
    private static final String KEY_START_LOC = "start_location";
    private static final String KEY_END_LOC = "end_location";
    private static final String KEY_DISTANCE = "distance";
    public static final String KEY_PHOTO1 = "photo1";
    public static final String KEY_PHOTO2 = "photo2";
    private static final String KEY_TASK_DATE_TO = "date_to";

    //  partner type & class search help  table create statement
    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN + "(" + KEY_PERNR + " PRIMARY KEY ,"
            + KEY_ENAME + " TEXT)";
    // Attendance table create statement
    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ATTENDANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PERNR
            + " TEXT," + KEY_BEGDAT + " TEXT," + KEY_INDZ + " TEXT," + KEY_IODZ + " TEXT," + KEY_TOTDZ
            + " TEXT," + KEY_ATN_STATUS + " TEXT," + KEY_LEAVE_TYP + " TEXT)";
    // sales Target table create statement
    private static final String CREATE_TABLE_TARGET = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TARGET + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_TO_PERNR + " TEXT,"
            + KEY_FR_PERNR + " TEXT,"
            + KEY_FR_ENAME + " TEXT,"
            + KEY_FR_DEPARTMENT + " TEXT,"
            + KEY_FR_TARGET + " TEXT,"
            + KEY_FR_NET_SALE + " TEXT,"
            + KEY_FR_POSITION + " TEXT)";
    // activity Target table create statement
    private static final String CREATE_TABLE_ACTIVITY_TARGET = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACTIVITY_TARGET + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_ACTIVITY_CODE + " TEXT,"
            + KEY_ACTIVITY_NAME + " TEXT,"
            + KEY_INDV_ACT_TARGET + " TEXT,"
            + KEY_INDV_ACT_ACHIEVEMENT + " TEXT,"
            + KEY_HRCY_ACT_TARGET + " TEXT,"
            + KEY_HRCY_ACT_ACHIEVEMENT + " TEXT,"
            + KEY_FR_DEPARTMENT + " TEXT,"
            + KEY_FR_POSITION + " TEXT)";
    // Review Images table create statement
    private static final String CREATE_TABLE_REVIEW_CMP_IMAGES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_REVIEW_COMPLAINT_IMAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_IMAGE1 + " TEXT,"
            + KEY_IMAGE2 + " TEXT,"
            + KEY_IMAGE3 + " TEXT,"
            + KEY_IMAGE4 + " TEXT,"
            + KEY_IMAGE5 + " TEXT,"
            + KEY_IMAGE6 + " TEXT,"
            + KEY_IMAGE7 + " TEXT,"
            + KEY_IMAGE8 + " TEXT,"
            + KEY_IMAGE9 + " TEXT,"
            + KEY_IMAGE10 + " TEXT,"
            + KEY_IMAGE11 + " TEXT,"
            + KEY_IMAGE12 + " TEXT,"
            + KEY_IMAGE13 + " TEXT,"
            + KEY_IMAGE14 + " TEXT,"
            + KEY_IMAGE15 + " TEXT)";

    // dsr search help  table create statement
    private static final String CREATE_TABLE_SEARCH_HELP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SEARCH_HELP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HELP_CODE + " TEXT,"
            + KEY_HELP_NAME + " TEXT)";


    // Table Create Statements
    //  route plan save  table create statement
    private static final String CREATE_TABLE_ROUTE_DETAIL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ROUTE_DETAIL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_VKORG + " TEXT,"
            + KEY_VTWEG + " TEXT,"
            + KEY_ROUTE_CODE + " TEXT,"
            + KEY_ROUTE_NAME + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_PARTNER + " TEXT,"
            + KEY_PARTNER_CLASS + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_LAND_TXT + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_STATE_TXT + " TEXT,"
            + KEY_DISTRICT_CODE + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_TALUKA_CODE + " TEXT,"
            + KEY_TALUKA_TXT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_TEL_NUMBER + " TEXT,"
            + KEY_PINCODE + " TEXT,"
            + KEY_CONTACT_PERSON + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_CONTACT_PERSON_PHONE + " TEXT,"
            + KEY_AADHAR_CARD + " TEXT,"
            + KEY_PAN_CARD + " TEXT,"
            + KEY_TIN_NO + " TEXT,"
            + KEY_MARKET_PLACE + " TEXT,"
            + KEY_DOB + " TEXT,"
            + KEY_INTRESTED + " TEXT,"
            + KEY_KTOKD + " TEXT,"
            + KEY_ADDED_AT_LATLONG + " TEXT,"
            + KEY_CUSTOMER_CATAGORY + " TEXT)";

    //   new added customer
    private static final String CREATE_TABLE_NEW_ADDED_CUSTOMER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NEW_ADDED_CUSTOMER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_VKORG + " TEXT,"
            + KEY_VTWEG + " TEXT,"
            + KEY_ROUTE_CODE + " TEXT,"
            + KEY_ROUTE_NAME + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_PARTNER + " TEXT,"
            + KEY_PARTNER_CLASS + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_LAND_TXT + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_STATE_TXT + " TEXT,"
            + KEY_DISTRICT_CODE + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_TALUKA_CODE + " TEXT,"
            + KEY_TALUKA_TXT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_TEL_NUMBER + " TEXT,"
            + KEY_PINCODE + " TEXT,"
            + KEY_CONTACT_PERSON + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_CONTACT_PERSON_PHONE + " TEXT,"
            + KEY_AADHAR_CARD + " TEXT,"
            + KEY_PAN_CARD + " TEXT,"
            + KEY_TIN_NO + " TEXT,"
            + KEY_MARKET_PLACE + " TEXT,"
            + KEY_DOB + " TEXT,"
            + KEY_INTRESTED + " TEXT,"
            + KEY_ADDED_AT_LATLONG + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_KTOKD + " TEXT,"
            + KEY_CUSTOMER_CATAGORY + " TEXT)";

    private static final String CREATE_TABLE_LOCAL_CONVENIENCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOCAL_CONVENIENCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_FROM_TIME + " TEXT,"
            + KEY_TO_TIME + " TEXT,"
            + KEY_FROM_LAT + " TEXT,"
            + KEY_FROM_LNG + " TEXT,"
            + KEY_TO_LAT + " TEXT,"
            + KEY_TO_LNG + " TEXT,"
            + KEY_START_LOC + " TEXT,"
            + KEY_END_LOC + " TEXT,"
            + KEY_DISTANCE + " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_TASK_DATE_TO + " TEXT)";


    private static final String CREATE_TABLE_CHECK_IN_OUT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CHECK_IN_OUT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_DATE_IN + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_DATE_OUT + " TEXT,"
            + KEY_TIME_OUT + " TEXT,"
            + KEY_CHECK_IN_LATITUDE + " TEXT,"
            + KEY_CHECK_OUT_LATITUDE + " TEXT,"
            + KEY_CHECK_IN_LONGITUDE + " TEXT,"
            + KEY_CHECK_OUT_LONGITUDE + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_HELP_NAME + " TEXT,"
            + KEY_ROUTE_CODE + " TEXT,"
            + KEY_AUDIO_RECORD + " BLOB NOT NULL,"
            + KEY_SYNC + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_CHAT_APP + " TEXT,"
            + KEY_FOLLOW_UP_DATE + " TEXT,"
            + KEY_CONVERSION_STATUS + " TEXT,"
            + KEY_SRV_CNT_BORD_IMG + " BLOB NOT NULL,"
            + KEY_SRV_CNT_TRN_LTR_IMG + " BLOB NOT NULL,"
            + KEY_CERTIFICATE_IMG + " BLOB NOT NULL,"
            + KEY_SLFY_SERV_PER + " BLOB NOT NULL,"
            + KEY_SPR_PRT_STK_IMG + " BLOB NOT NULL,"
            + KEY_PRD_TRN_IMG + " BLOB NOT NULL,"
            + KEY_OTHR_IMG + " BLOB NOT NULL,"
            + KEY_PHONE_NUMBER + " TEXT" + ")";


    //  partner type & class search help  table create statement
    private static final String CREATE_TABLE_PARTNER_TYPE_CLASS_HELP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PARTNER_TYPE_CLASS_HELP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PARTNER + " TEXT,"
            + KEY_PARTNER_TEXT + " TEXT,"
            + KEY_PARTNER_CLASS + " TEXT,"
            + KEY_PARTNER_CLASS_TEXT + " TEXT)";


    //  partner type & class search help  table create statement
    private static final String CREATE_TABLE_AREA_DISTRIBUTOR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AREA_DISTRIBUTOR + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

            + KEY_LAND_TXT + " TEXT,"
            + KEY_STATE_TXT + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_TALUKA_TXT + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT)";

    // dsr entry save  table create statement
    private static final String CREATE_TABLE_DSR_ENTRY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DSR_ENTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_HELP_NAME + " TEXT,"
            + KEY_AGENDA + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_LONGITUDE + " TEXT)";

    // Froward Approval Complaint save  table create statement
    private static final String CREATE_TABLE_FRWD_APP_CMP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FRWD_APP_CMP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_POSNR + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_CUSTOMER + " TEXT,"
            + KEY_DEALER + " TEXT,"
            + KEY_COMPANY + " TEXT,"
            + KEY_DEFECT + " TEXT,"
            + KEY_RELT_TO + " TEXT,"
            + KEY_FOC_AMT + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_PAY_FREELANCER + " TEXT,"
            + KEY_PAY_DEA + " TEXT,"
            + KEY_PAY_COM + " TEXT,"
            + KEY_AWT_PERNR + " TEXT,"
            + KEY_PEND_PERNR + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_AWT_APR_RMK + " TEXT)";

    // Pendiing Approval Complaint save  table create statement
    private static final String CREATE_TABLE_PEND_APP_CMP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PEND_APP_CMP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_REMARK + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_PEND_APR_RMK + " TEXT)";

    // visit history
    private static final String CREATE_TABLE_VISIT_HISTORY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_VISIT_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ENAME + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_AUDIO_RECORD + " string,"
            + KEY_VISIT + " TEXT)";

    // no order  table create statement
    private static final String CREATE_TABLE_NO_ORDER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NO_ORDER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_HELP_NAME + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_ROUTE_CODE + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + IN_IMAGE + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_LONGITUDE + " TEXT)";

    // Table adhoc order
    private static final String CREATE_TABLE_TBL_ADHOC = "CREATE TABLE "
            + TABLE_ADHOC + "("
            + KEY_MATNR + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_VKORG + " TEXT,"
            + KEY_VTWEG + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_KBETR + " TEXT,"
            + KEY_KONWA + " TEXT,"
            + KEY_MTART + " TEXT)";

    private static final String CREATE_TABLE_TBL_ADHOC_FINAL = "CREATE TABLE "
            + TABLE_ADHOC_FINAL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // add in 1.6
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_MATNR + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_KBETR + " TEXT,"
            + KEY_MENGE + " TEXT,"
            + KEY_TOT_KBETR + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_PERSON + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_ROUTE_CODE + " TEXT,"
            + KEY_PARTNER + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_SYNC + " TEXT)";

    //  employee activity ( gps tracking)
    private static final String CREATE_TABLE_EMPLOYEE_GPS_ACTIVITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE_GPS_ACTIVITY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_EVENT + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_CELL_ID + " TEXT,"
            + KEY_LOCATION_CODE + " TEXT,"
            + KEY_MOBILE_COUNTRY_CODE + " TEXT,"
            + KEY_MOBILE_NETWORK_CODE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT)";

    //  adhoc order customer
    private static final String CREATE_TABLE_ADHOC_ORDER_CUSTOMER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ADHOC_ORDER_CUSTOMER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_PARTNER + " TEXT,"
            + KEY_PARTNER_CLASS + " TEXT,"
            + KEY_LAND_TXT + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT)";

    // survey table
    private static final String CREATE_TABLE_SURVEY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SURVEY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_OUTER_VIEW + " TEXT,"
            + KEY_INNER_VIEW + " TEXT,"
            + KEY_OTHER_VIEW + " TEXT,"
            + KEY_OWNER_VIEW + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_CARD_VIEW + " TEXT)";
    private static final String CREATE_TABLE_VIEW_SURVEY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_VIEW_SURVEY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_DOWNLAOD_FROM + " TEXT,"
            + KEY_OUTER_VIEW + " TEXT,"
            + KEY_INNER_VIEW + " TEXT,"
            + KEY_OTHER_VIEW + " TEXT,"
            + KEY_OWNER_VIEW + " TEXT,"
            + KEY_CARD_VIEW + " TEXT)";


    // attendance table
    //  material analysis
    private static final String CREATE_TABLE_MATERIAL_ANALYSIS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MATERIAL_ANALYSIS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATNR + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_PLANT + " TEXT,"
            + KEY_INDICATOR + " TEXT,"
            + KEY_DELIVERY_TIME + " TEXT,"
            + KEY_KBETR + " TEXT,"
            + KEY_KONWA + " TEXT)";

    //  Video gallery
    private static final String CREATE_TABLE_VIDEO_GALLERY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_VIDEO_GALLERY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_VIDEO_TYPE + " TEXT,"
            + KEY_VIDEO_NAME + " TEXT,"
            + KEY_VIDEO_LINK + " TEXT)";

    //  Complaint Header
    private static final String CREATE_TABLE_ZCMPLNHDR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLNHDR + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_CMPDT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_ALT_MOB_NO + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_SAVE_BY + " TEXT,"
            + KEY_EDIT + " TEXT,"
            + KEY_EPC + " TEXT,"
            + KEY_PEN_DAY + " TEXT,"
            + KEY_PEN_RES + " TEXT,"
            + KEY_FLW_DT + " TEXT,"
            + KEY_AWT_PERNR + " TEXT,"
            + KEY_AWT_PERNR_NM + " TEXT,"
            + KEY_PEND_PERNR + " TEXT,"
            + KEY_PEND_PERNR_NM + " TEXT,"
            + KEY_AWT_APR + " TEXT,"
            + KEY_PEND_APR + " TEXT,"
            + KEY_AWT_APR_RMK + " TEXT,"
            + KEY_PEND_APR_RMK + " TEXT,"
            + KEY_ENAME + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLNHDR_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLNHDR_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_CMPDT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_ALT_MOB_NO + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_SAVE_BY + " TEXT,"
            + KEY_EDIT + " TEXT,"
            + KEY_EPC + " TEXT,"
            + KEY_PEN_DAY + " TEXT,"
            + KEY_PEN_RES + " TEXT,"
            + KEY_FLW_DT + " TEXT,"
            + KEY_AWT_PERNR + " TEXT,"
            + KEY_AWT_PERNR_NM + " TEXT,"
            + KEY_PEND_PERNR + " TEXT,"
            + KEY_PEND_PERNR_NM + " TEXT,"
            + KEY_AWT_APR + " TEXT,"
            + KEY_PEND_APR + " TEXT,"
            + KEY_AWT_APR_RMK + " TEXT,"
            + KEY_PEND_APR_RMK + " TEXT,"
            + KEY_ENAME + " TEXT)";

    //  Complaint Detail
    private static final String CREATE_TABLE_ZCMPLNDTL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLNHDTL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_POSNR + " TEXT,"
            + KEY_MATNR + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_WARRANTY + " TEXT,"
            + KEY_WAR_DAT + " TEXT,"
            + KEY_SERNR + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_PAYMENT_BY + " TEXT,"
            + KEY_CUSTOMER + " TEXT,"
            + KEY_DEALER + " TEXT,"
            + KEY_COMPANY + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT,"
            + KEY_HISTORY + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_INSU_TXT + " TEXT,"
            + KEY_WARR_COND + " TEXT,"
            + KEY_PAY_FREELANCER + " TEXT,"
            + KEY_PAY_COM + " TEXT,"
            + KEY_PAY_DEA + " TEXT,"
            + KEY_RELT_TO + " TEXT,"
            + KEY_DEFECT + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLNDTL_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLNHDTL_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_POSNR + " TEXT,"
            + KEY_MATNR + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_WARRANTY + " TEXT,"
            + KEY_WAR_DAT + " TEXT,"
            + KEY_SERNR + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_PAYMENT_BY + " TEXT,"
            + KEY_CUSTOMER + " TEXT,"
            + KEY_DEALER + " TEXT,"
            + KEY_COMPANY + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT,"
            + KEY_HISTORY + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_INSU_TXT + " TEXT,"
            + KEY_WARR_COND + " TEXT,"
            + KEY_PAY_FREELANCER + " TEXT,"
            + KEY_PAY_COM + " TEXT,"
            + KEY_PAY_DEA + " TEXT,"
            + KEY_RELT_TO + " TEXT,"
            + KEY_DEFECT + " TEXT)";


    private static final String CREATE_TABLE_ZCMPLN_IMAGE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_IMAGE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_POSNR + " TEXT,"
            + KEY_IMAGE + " BLOB,"
//            + KEY_PDF1 + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_CMP_ATTACHMENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CMP_ATTACHMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_IMAGE1 + " TEXT,"
            + KEY_IMAGE2 + " BLOB,"
            + KEY_IMAGE3 + " BLOB,"
            + KEY_IMAGE4 + " BLOB,"
            + KEY_IMAGE5 + " BLOB,"
            + KEY_IMAGE6 + " BLOB,"
            + KEY_IMAGE7 + " BLOB,"
            + KEY_IMAGE8 + " BLOB,"
            + KEY_IMAGE9 + " BLOB,"
            + KEY_IMAGE10 + " BLOB,"
            + KEY_IMAGE11 + " BLOB,"
            + KEY_IMAGE12 + " BLOB,"
            + KEY_IMAGE13 + " BLOB,"
            + KEY_IMAGE14 + " BLOB,"
            + KEY_IMAGE15 + " BLOB,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_DISTANCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_DISTANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_LAT_LONG + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_EXTRA1 + " TEXT,"
            + KEY_EXTRA2 + " TEXT,"
            + KEY_SYNC + " TEXT)";

    //  complaint closed by person
    private static final String CREATE_TABLE_CLOSE_COMPLAINT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CLOSE_COMPLAINT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_CMPNO + " TEXT,"

            + KEY_POSNR + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            /*+ KEY_PAYMENT_BY + " TEXT,"*/
            + KEY_CUSTOMER + " TEXT,"
            + KEY_DEALER + " TEXT,"
            + KEY_COMPANY + " TEXT,"
            + KEY_FEEDBACK_CUSTOMER + " TEXT,"
            + KEY_FEEDBACK_STATUS + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT,"
            + KEY_DEFECT + " TEXT,"
            + KEY_RELT_TO + " TEXT,"
            + KEY_FOC_AMT + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_PAY_FREELANCER + " TEXT,"
            + KEY_PAY_DEA + " TEXT,"
            + KEY_PAY_COM + " TEXT,"

            + KEY_SYNC + " TEXT)";
    //  complaint service center
    private static final String CREATE_TABLE_SERVICE_CENTER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SERVICE_CENTER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_KUNNR + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_LAND_TXT + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_STATE_TXT + " TEXT,"
            + KEY_DISTRICT_CODE + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_TALUKA_CODE + " TEXT,"
            + KEY_TALUKA_TXT + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_TEL_NUMBER + " TEXT,"
            + KEY_CONTACT_PERSON + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_PINCODE + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_LAT_LONG + " TEXT,"
            + KEY_SYNC + " TEXT)";

    //  Serail Number Complaint Header
    private static final String CREATE_TABLE_SERAIL_NUMBER_ZCMPLNHDR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SERAIL_NUMBER_ZCMPLNHDR + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_CMPDT + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_DISTRIBUTOR_CODE + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_SAVE_BY + " TEXT,"
            + KEY_EDIT + " TEXT,"
            + KEY_ENAME + " TEXT)";

    //  Complaint Detail
    private static final String CREATE_TABLE_SERAIL_NUMBER_ZCMPLNDTL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SERAIL_NUMBER_ZCMPLNHDTL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_POSNR + " TEXT,"
            + KEY_MATNR + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_WARRANTY + " TEXT,"
            + KEY_SERNR + " TEXT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_PAYMENT_BY + " TEXT,"
            + KEY_CUSTOMER + " TEXT,"
            + KEY_DEALER + " TEXT,"
            + KEY_COMPANY + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT,"
            + KEY_RELT_TO + " TEXT,"
            + KEY_DEFECT + " TEXT)";

    //  country
    private static final String CREATE_TABLE_COUNTRY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COUNTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_LAND_TXT + " TEXT)";

    //  state
    private static final String CREATE_TABLE_STATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STATE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_STATE_TXT + " TEXT)";

    //  city
    private static final String CREATE_TABLE_CITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CITY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_DISTRICT_CODE + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT)";

    //  tehsil
    private static final String CREATE_TABLE_TEHSIL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TEHSIL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_STATE_CODE + " TEXT,"
            + KEY_DISTRICT_CODE + " TEXT,"
            + KEY_TALUKA_CODE + " TEXT,"
            + KEY_TALUKA_TXT + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_CATEGORY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_CATEGORY_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_CATEGORY_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_DEFECT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_DEFECT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DEFECT + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_DEFECT_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_DEFECT_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DEFECT + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_RELT_TO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_RELT_TO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RELT_TO + " TEXT)";

    private static final String CREATE_TABLE_ZCMPLN_RELT_TO_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_RELT_TO_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RELT_TO + " TEXT)";


    private static final String CREATE_TABLE_ZCMPLN_CLOSER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_CLOSER + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT)";
    private static final String CREATE_TABLE_ZCMPLN_CLOSER_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZCMPLN_CLOSER_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EXTWG + " TEXT,"
            + KEY_CLOSER_RESON + " TEXT)";

    private static final String CREATE_TABLE_INPROCESS_COMPLAINT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZINPROCESS_COMPLAINT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_FOLLOW_UP_DATE + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_ACTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_ACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_FOLLOW_UP_DATE + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_ACTION_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_ACTION_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_FOLLOW_UP_DATE + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_CMPLN_STATUS + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_PENDING_REASON = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PENDING_REASON + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PEND_NO + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_PENDING_REASON_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PENDING_REASON_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PEND_NO + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_IMAGE_NAME = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_IMAGE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_ITEM + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_IMAGE_NAME_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_IMAGE_NAME_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_ITEM + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_SYNC + " TEXT)";

    //  customer audio file
    private static final String CREATE_TABLE_COMPLAINT_AUDIO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COMPLAINT_AUDIO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_AUDIO_RECORD + " string,"
            + KEY_CLOSER_RESON + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT,"
            + KEY_DISTRIBUTOR_NAME + " TEXT,"
            + KEY_DISTRICT_TXT + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_CHAT_APP + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT)";

    private static final String CREATE_TABLE_CALL_LOG = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CALL_LOG + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_CR_DATE + " TEXT,"
            + KEY_CR_TIME + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_CALL_TYPE + " TEXT,"
            + KEY_CALL_DURATION + " TEXT,"
            + KEY_LAT_LONG + " TEXT,"
            + KEY_SYNC + " TEXT)";

    private static final String CREATE_TABLE_CHAT_APP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CHAT_APP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_API + " TEXT,"
            + KEY_GROUP_ID + " TEXT)";

    private static final String CREATE_TABLE_CHAT_APP_VK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CHAT_APP_VK + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_API + " TEXT,"
            + KEY_GROUP_ID + " TEXT)";

    private static final String CREATE_TABLE_DATA_SYNC_CHAT_APP = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DATA_SYNC_CHAT_APP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TABLE_NAME + " TEXT,"
            + KEY_SYNC_ID + " TEXT)";

    int mcc;
    int mnc;
    int cid;
    int lac;
    TelephonyManager telephonyManager;
    GsmCellLocation cellLocation;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /* @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }*/
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_TARGET);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_SEARCH_HELP);
        db.execSQL(CREATE_TABLE_DSR_ENTRY);
        db.execSQL(CREATE_TABLE_FRWD_APP_CMP);
        db.execSQL(CREATE_TABLE_PEND_APP_CMP);
        db.execSQL(CREATE_TABLE_ROUTE_DETAIL);
        db.execSQL(CREATE_TABLE_CHECK_IN_OUT);
        db.execSQL(CREATE_TABLE_PARTNER_TYPE_CLASS_HELP);

        db.execSQL(CREATE_TABLE_AREA_DISTRIBUTOR);
        db.execSQL(CREATE_TABLE_MARK_ATTENDANCE);
        db.execSQL(CREATE_TABLE_TBL_ADHOC);
        db.execSQL(CREATE_TABLE_TBL_ADHOC_FINAL);
        db.execSQL(CREATE_TABLE_NO_ORDER);
        db.execSQL(CREATE_TABLE_EMPLOYEE_GPS_ACTIVITY);
        db.execSQL(CREATE_TABLE_ADHOC_ORDER_CUSTOMER);
        db.execSQL(CREATE_TABLE_VISIT_HISTORY);
        db.execSQL(CREATE_TABLE_NEW_ADDED_CUSTOMER);
        db.execSQL(CREATE_TABLE_SURVEY);
        db.execSQL(CREATE_TABLE_MATERIAL_ANALYSIS);
        db.execSQL(CREATE_TABLE_VIDEO_GALLERY);
        db.execSQL(CREATE_TABLE_ACTIVITY_TARGET);

        db.execSQL(CREATE_TABLE_ZCMPLNDTL);
        db.execSQL(CREATE_TABLE_ZCMPLNDTL_VK);
        db.execSQL(CREATE_TABLE_ZCMPLNHDR);
        db.execSQL(CREATE_TABLE_ZCMPLNHDR_VK);
        db.execSQL(CREATE_TABLE_ZCMPLN_CATEGORY);
        db.execSQL(CREATE_TABLE_ZCMPLN_CATEGORY_VK);
        db.execSQL(CREATE_TABLE_ZCMPLN_DEFECT);
        db.execSQL(CREATE_TABLE_ZCMPLN_DEFECT_VK);
        db.execSQL(CREATE_TABLE_ZCMPLN_RELT_TO);
        db.execSQL(CREATE_TABLE_ZCMPLN_RELT_TO_VK);
        db.execSQL(CREATE_TABLE_ZCMPLN_CLOSER);
        db.execSQL(CREATE_TABLE_ZCMPLN_CLOSER_VK);
        db.execSQL(CREATE_TABLE_INPROCESS_COMPLAINT);
        db.execSQL(CREATE_TABLE_CLOSE_COMPLAINT);
        db.execSQL(CREATE_TABLE_SERVICE_CENTER);

        db.execSQL(CREATE_TABLE_COUNTRY);
        db.execSQL(CREATE_TABLE_STATE);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_TEHSIL);
        db.execSQL(CREATE_TABLE_COMPLAINT_ACTION);
        db.execSQL(CREATE_TABLE_COMPLAINT_ACTION_VK);
        db.execSQL(CREATE_TABLE_ZCMPLN_IMAGE);
        db.execSQL(CREATE_TABLE_CMP_ATTACHMENT);
        db.execSQL(CREATE_TABLE_VIEW_SURVEY);
        db.execSQL(CREATE_TABLE_CALL_LOG);

        db.execSQL(CREATE_TABLE_SERAIL_NUMBER_ZCMPLNHDR);
        db.execSQL(CREATE_TABLE_SERAIL_NUMBER_ZCMPLNDTL);
        db.execSQL(CREATE_TABLE_COMPLAINT_AUDIO);
        db.execSQL(CREATE_TABLE_CHAT_APP);
        db.execSQL(CREATE_TABLE_CHAT_APP_VK);
        db.execSQL(CREATE_TABLE_DATA_SYNC_CHAT_APP);
        db.execSQL(CREATE_TABLE_COMPLAINT_DISTANCE);
        db.execSQL(CREATE_TABLE_COMPLAINT_IMAGE_NAME);
        db.execSQL(CREATE_TABLE_COMPLAINT_IMAGE_NAME_VK);
        db.execSQL(CREATE_TABLE_COMPLAINT_PENDING_REASON);
        db.execSQL(CREATE_TABLE_COMPLAINT_PENDING_REASON_VK);
        db.execSQL(CREATE_TABLE_REVIEW_CMP_IMAGES);
        db.execSQL(CREATE_TABLE_LOCAL_CONVENIENCE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_TARGET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HELP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DSR_ENTRY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRWD_APP_CMP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEND_APP_CMP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_ADDED_CUSTOMER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_IN_OUT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER_TYPE_CLASS_HELP);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_DISTRIBUTOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARK_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NO_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADHOC_ORDER_CUSTOMER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADHOC_FINAL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADHOC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_GPS_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_HISTORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW_SURVEY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL_ANALYSIS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_GALLERY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLNHDR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLNHDR_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLNHDTL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLNHDTL_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERAIL_NUMBER_ZCMPLNHDR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERAIL_NUMBER_ZCMPLNHDTL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_AUDIO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_CATEGORY_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_DEFECT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_DEFECT_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_CLOSER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_CLOSER_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZINPROCESS_COMPLAINT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_ACTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_ACTION_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_IMAGE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_IMAGE_NAME_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_REASON);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_REASON_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOSE_COMPLAINT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_CENTER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEHSIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_LOG);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_APP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_APP_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_SYNC_CHAT_APP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_DISTANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW_COMPLAINT_IMAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CONVENIENCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_RELT_TO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZCMPLN_RELT_TO_VK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CMP_ATTACHMENT);

            Log.d("newDatabaseVersion123", "" + newVersion);
            onCreate(db);



    }

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public boolean doesTableExist(String userid) {
           boolean result = false;
        try {
       SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select DISTINCT pernr from tbl_check_in_out where pernr = '" + userid + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                result = true;
            }else {
                onCreate(database);
            }
            cursor.close();
        }
        } catch (Exception e) {
            Log.e(TAG, "[tableIsExist method]error, e: ", e);
        }
        return result;
    }
    public void deleteDataSyncToChatApp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATA_SYNC_CHAT_APP, null, null);


    }

    public void deletePendcmpdata(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEND_APP_CMP, KEY_CMPNO + " = '" + cmpno + "'", null);


    }

    public void deletecmpattachment(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CMP_ATTACHMENT, KEY_CMPNO + " = '" + cmpno + "'", null);


    }


    public void deleteReviewCmpImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW_COMPLAINT_IMAGES, null, null);


    }

    public void deleteReviewCmpImages(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW_COMPLAINT_IMAGES, KEY_CMPNO + " = '" + cmpno + "'", null);


    }

  /*  public void deleteCmpPdf(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_PDF_NAME, KEY_CMPNO + " = '" + cmpno + "'", null);

    }*/


    public void deleteSurveyView() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIEW_SURVEY, null, null);


    }


    public void deleteComplaint_action() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_ACTION, null, null);


    }

    public void deleteComplaint_actionVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_ACTION_VK, null, null);


    }

    public void deletePending_reason() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PENDING_REASON, null, null);


    }

    public void deletePending_reasonVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PENDING_REASON_VK, null, null);


    }

    public void deleteComplaint_image() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_IMAGE_NAME, null, null);


    }

    public void deleteComplaint_imageVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_IMAGE_NAME_VK, null, null);


    }

    public void deleteLocalconvenienceDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCAL_CONVENIENCE, null, null);

    }

    public void deleteLocalconvenienceDetail(String strdt, String strtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_BEGDA + "='" + strdt + "'" + " AND " + KEY_FROM_TIME + " = '" + strtm + "'");

    }

   /* public void deleteComplaint_pdf() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPLAINT_PDF_NAME, null, null);

    }*/


    public void deleteCountry() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY, null, null);


    }


    public void deleteState() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATE, null, null);


    }

    public void deleteCity() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITY, null, null);


    }

    public void deleteTehsil() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEHSIL, null, null);


    }


    public void deleteServiceCenter() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVICE_CENTER, null, null);


    }


    public void deleteMaterialAnalysis() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MATERIAL_ANALYSIS, null, null);


    }

    public void deleteCustomerComplaintHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDR, null, null);


    }

    public void deleteCustomerComplaintHeaderVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDR_VK, null, null);


    }

    public void deleteCustomerComplaintDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDTL, null, null);

    }

    public void deleteCustomerComplaintDetailVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDTL_VK, null, null);

    }

    public void deleteCustomerComplaintHeader(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDR, KEY_CMPNO + " = '" + cmpno + "'", null);


    }

    public void deleteCustomerComplaintHeaderVK(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDR_VK, KEY_CMPNO + " = '" + cmpno + "'", null);


    }

    public void deleteCustomerComplaintDetail(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDTL, KEY_CMPNO + " = '" + cmpno + "'", null);

    }

    public void deleteCustomerComplaintDetailVK(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLNHDTL_VK, KEY_CMPNO + " = '" + cmpno + "'", null);

    }


    public void deleteSerialNumberComplaintHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERAIL_NUMBER_ZCMPLNHDR, null, null);


    }

    public void deleteSerialNumberCustomerComplaintDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERAIL_NUMBER_ZCMPLNHDTL, null, null);

    }


    public void deleteVideoGallery() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEO_GALLERY, null, null);

    }


    public void deleteChatAppGroup() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_APP, null, null);

    }

    public void deleteChatAppGroupVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_APP_VK, null, null);

    }


    public void deleteCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_CATEGORY, null, null);

    }

    public void deleteCategoryVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_CATEGORY_VK, null, null);

    }


    public void deleteDefect() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_DEFECT, null, null);

    }

    public void deleteDefectVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_DEFECT_VK, null, null);

    }

    public void deleteReltto() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_RELT_TO, null, null);

    }

    public void deleteRelttoVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_RELT_TO_VK, null, null);

    }


    public void deleteCloser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_CLOSER, null, null);

    }

    public void deleteCloserVK() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_CLOSER_VK, null, null);

    }

    public void deleteImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteImage2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, null, null);
    }

    public void deleteImage1(String cmp_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, KEY_CMPNO + " ='" + cmp_no + "'" + " AND " + KEY_CATEGORY + " != 'others'", null);
    }

    public void deleteSurvey() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_SURVEY, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteNewAddedCustomer() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NEW_ADDED_CUSTOMER, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteAlreadyExistNewAddedCustomer(String keyMobNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NEW_ADDED_CUSTOMER, KEY_MOB_NO + " = '" + keyMobNo + "'", null);
        Log.i("DeletedRow", "" + i);
    }

    public void deleteMarkAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_MARK_ATTENDANCE, KEY_SYNC + " = 'YES'" + " AND " + KEY_BEGDA + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteComplaintInprocessAction() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_ZINPROCESS_COMPLAINT, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteClouserComplaint() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_CLOSE_COMPLAINT, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteComplaintImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_ZCMPLN_IMAGE, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteComplaintAudio() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_COMPLAINT_AUDIO, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteComplaintStart() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_COMPLAINT_DISTANCE, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);
    }

    public void deleteCallLog() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_CALL_LOG, KEY_SYNC + " = 'YES'", null);
    }

    public void deleteAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE, null, null);
    }

    public void deleteTarget() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TARGET, null, null);

    }

    public void deleteActivityTarget() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITY_TARGET, null, null);

    }


    public void deleteDSREntryHelp() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEARCH_HELP, null, null);

    }

    public void deletecmpattach() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CMP_ATTACHMENT, null, null);

    }


    public void deleteAdhocOrderFinal() {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_ADHOC_FINAL, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);

    }

    public void deleteAdhocOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADHOC, null, null);

    }


    public void deleteDSREntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_SURVEY, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);


    }

    public void deleteRouteDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROUTE_DETAIL, null, null);

    }

    public void deleteCheckInOut() {
        SQLiteDatabase db = this.getWritableDatabase();
       db.delete(TABLE_CHECK_IN_OUT, KEY_SYNC + " = 'YES'" + " AND " + KEY_DATE_IN + " != '" + new CustomUtility().getCurrentDate() + "'", null);



    }

    public void deletePartnerTypeClassHelp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNER_TYPE_CLASS_HELP, null, null);

    }


    public void deleteAreaDistributor() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AREA_DISTRIBUTOR, null, null);

    }

    public void deleteLogin() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null);

    }


    public void deleteNoOrder() {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_NO_ORDER, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);


    }

    public void deleteEmployeeGPSActivity() {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_EMPLOYEE_GPS_ACTIVITY, KEY_SYNC + " = 'YES'" + " AND " + KEY_BUDAT + " != '" + new CustomUtility().getCurrentDate() + "'", null);


    }


    public void deleteAdhocOrderCustomer() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADHOC_ORDER_CUSTOMER, null, null);

    }

    public void deleteVisitHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VISIT_HISTORY, null, null);

    }


    /**********************  insert Employee GPS Activity ************************************/
    public void insertEmployeeGPSActivity(
            String pernr,
            String budat,
            String time,
            String event,
            String latitude,
            String longitude,
            Context context,
            String phone_number


    ) {


//*************  get mobile tower location *******************************

        String cell_id = "0",
                location_code = "0",
                mobile_country_code = "0",
                mobile_network_code = "0";


        try {

            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

            if (cellLocation != null) {
                cid = cellLocation.getCid();
                lac = cellLocation.getLac();
            }

            String networkOperator = telephonyManager.getNetworkOperator();

            if (!TextUtils.isEmpty(networkOperator)) {
                mcc = Integer.parseInt(networkOperator.substring(0, 3));
                mnc = Integer.parseInt(networkOperator.substring(3));
            }


//        String loc = "cell id: " + String.valueOf(cid) + "location area code:" + String.valueOf(lac) +
//                "mcc: " + String.valueOf(mcc) +    "mnc: " + String.valueOf(mnc);


//            String loc = "ci=: " + String.valueOf(cid) + "lac=:" + String.valueOf(lac) +
//                    "mcc=: " + String.valueOf(mcc) +    "mnc=: " + String.valueOf(mnc);


            cell_id = String.valueOf(cid);
            location_code = String.valueOf(lac);

            mobile_country_code = String.valueOf(mcc);
            mobile_network_code = String.valueOf(mnc);


        } catch (SQLiteException e) {
            e.printStackTrace();
        }

//*************  end mobile tower location *******************************


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_EVENT, event);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");

            values.put(KEY_CELL_ID, cell_id);
            values.put(KEY_LOCATION_CODE, location_code);
            values.put(KEY_MOBILE_COUNTRY_CODE, mobile_country_code);
            values.put(KEY_MOBILE_NETWORK_CODE, mobile_network_code);


            // Insert Row
            long i = db.insert(TABLE_EMPLOYEE_GPS_ACTIVITY, null, values);


            //Toast.makeText(context,String.valueOf( "mayank"+ cell_id +"--"+latitude) , Toast.LENGTH_SHORT).show();


            //Log.d("sync", cell_id +"--"+latitude);


            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert survey ************************************/
    public void insertSurvey(


            String pernr,
            String budat,
            String time,
            String comment,
            String latitude,
            String longitude,
            String outer_view,
            String inner_view,
            String other_view,
            String owner_view,
            String card_view,
            String phone_number
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        //db.beginTransactionNonExclusive();();
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_OUTER_VIEW, outer_view);
            values.put(KEY_INNER_VIEW, inner_view);
            values.put(KEY_OTHER_VIEW, other_view);
            values.put(KEY_OWNER_VIEW, owner_view);
            values.put(KEY_CARD_VIEW, card_view);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_SURVEY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert survey ************************************/
    public void insertReviewCmpImage(String cmpno,
                                     String image1,
                                     String image2,
                                     String image3,
                                     String image4,
                                     String image5,
                                     String image6,
                                     String image7,
                                     String image8,
                                     String image9,
                                     String image10,
                                     String image11,
                                     String image12,
                                     String image13,
                                     String image14,
                                     String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            // Insert Row
            long i = db.insert(TABLE_REVIEW_COMPLAINT_IMAGES, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    public void updateReviewCmpImage(String cmpno,
                                     String image1,
                                     String image2,
                                     String image3,
                                     String image4,
                                     String image5,
                                     String image6,
                                     String image7,
                                     String image8,
                                     String image9,
                                     String image10,
                                     String image11,
                                     String image12,
                                     String image13,
                                     String image14,
                                     String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;
        String where = "";

        try {

            values = new ContentValues();
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            where = KEY_CMPNO + "='" + cmpno + "'";

            // Insert Row
            long i = db.update(TABLE_REVIEW_COMPLAINT_IMAGES, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /**********************  insert view survey ************************************/
    public void insertViewSurvey(

            String ename,
            String budat,
            String comment,
            String outer_view,
            String inner_view,
            String other_view,
            String owner_view,
            String card_view,
            String phone_number,
            String download_from

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();

            values.put(KEY_BUDAT, budat);
            values.put(KEY_ENAME, ename);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_OUTER_VIEW, outer_view);
            values.put(KEY_INNER_VIEW, inner_view);
            values.put(KEY_OTHER_VIEW, other_view);
            values.put(KEY_OWNER_VIEW, owner_view);
            values.put(KEY_CARD_VIEW, card_view);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_DOWNLAOD_FROM, download_from);


            // Insert Row
            long i = db.insert(TABLE_VIEW_SURVEY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert login detail ************************************/
    public void insertLoginData(
            String lv_pernr,
            String lv_ename
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, lv_pernr);
            values.put(KEY_ENAME, lv_ename);

            // Insert Row
            long i = db.insert(TABLE_LOGIN, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert area distributor ************************************/
    public void insertAreaDistributor(
            String distributor_code,
            String distributor_name,
            String land_txt,
            String state_txt,
            String district_txt,
            String taluka_txt) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_TXT, taluka_txt);


            // Insert Row
            long i = db.insert(TABLE_AREA_DISTRIBUTOR, null, values);

            //  Log.d("route",""+ i);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert customer for adhoc order ************************************/
    public void insertAdhocOrderCustomer(
            String pernr,
            String phone_number,
            String partner_name,
            String partner_class,
            String partner_type,
            String country,
            String district) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_PARTNER, partner_type);
            values.put(KEY_LAND_TXT, country);
            values.put(KEY_DISTRICT_TXT, district);


            // Insert Row
            long i = db.insert(TABLE_ADHOC_ORDER_CUSTOMER, null, values);

            // Log.d("route",""+ i);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert visit history ************************************/
    public void insertvisitHistory(

            String budat,
            String time,
            String comment,
            String ename,
            String phone_number,
            String partner_name,
            String visit,
            String audio_record) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_ENAME, ename);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_VISIT, visit);
            values.put(KEY_AUDIO_RECORD, audio_record);


            // Insert Row
            long i = db.insert(TABLE_VISIT_HISTORY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    /**********************  insert visit history ************************************/
    public void insertComplaintAudio(

            String pernr,
            String cmpno,
            String budat,
            String time,
            String audio_record,

            String chat_dealer,
            String chat_customer,
            String chat_address,
            String action
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_CR_TIME, time);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_AUDIO_RECORD, audio_record);
            values.put(KEY_CLOSER_RESON, action);
            values.put(KEY_PARTNER_NAME, chat_customer);
            values.put(KEY_DISTRIBUTOR_NAME, chat_dealer);
            values.put(KEY_DISTRICT_TXT, chat_address);

            values.put(KEY_SYNC, "NOT");
            values.put(KEY_CHAT_APP, "NOT");

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_AUDIO, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public void insertLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());
            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());

            // Insert Row
            long i = db.insert(TABLE_LOCAL_CONVENIENCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void updateLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());

            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());

            where = KEY_PERNR + "='" + localconvenienceBean.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + localconvenienceBean.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + localconvenienceBean.getFrom_time() + "'" + " AND " +
                    KEY_FROM_LAT + "='" + localconvenienceBean.getFrom_lat() + "'" + " AND " +
                    KEY_FROM_LNG + "='" + localconvenienceBean.getFrom_lng() + "'";

            // update Row
            long i = db.update(TABLE_LOCAL_CONVENIENCE, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /**********************  insert Partner Type Class help ************************************/
    public void insertPartnerTypeClassHelp(
            String partner,
            String partner_text,
            String partner_class,
            String partner_class_text
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_TEXT, partner_text);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_PARTNER_CLASS_TEXT, partner_class_text);
            // Insert Row
            long i = db.insert(TABLE_PARTNER_TYPE_CLASS_HELP, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    @SuppressLint("Range")
    public AttendanceBean getMarkAttendanceByDate111(String date, String id) {
        AttendanceBean array_list = new AttendanceBean();
        Cursor res = null;
        SQLiteDatabase db = null;
        try {


            db = this.getReadableDatabase();

            // String query = "select * from " + TABLE_MARK_ATTENDANCE+" where  BEGDA = "+"'"+date+"'";
            String query = "select * from " + TABLE_MARK_ATTENDANCE + " where  BEGDA = " + "'" + date + "'" +
                    " AND " + PERNR + " = '" + id + "'";

//        +  " WHERE " + KEY_PERNR +  " = '" +userid.trim()+"'"
//                + " AND " + KEY_CUSTOMER_CATAGORY  + " = '" + "NEW" + "'" ;
//
            res = db.rawQuery(query, null);

            res.moveToFirst();
            while (res.isAfterLast() == false) {
                AttendanceBean bean = new AttendanceBean();
                // bean.ID = (res.getString(res.getColumnIndex(AttendanceDBFields.ID)));
                bean.TYPE = (res.getString(res.getColumnIndex(TYPE)));
                bean.PERNR = (res.getString(res.getColumnIndex(PERNR)));
                bean.BEGDA = (res.getString(res.getColumnIndex(BEGDA)));
                bean.SERVER_DATE_IN = (res.getString(res.getColumnIndex(SERVER_DATE_IN)));
                bean.SERVER_TIME_IN = (res.getString(res.getColumnIndex(SERVER_TIME_IN)));
                bean.SERVER_DATE_OUT = (res.getString(res.getColumnIndex(SERVER_DATE_OUT)));
                bean.SERVER_TIME_OUT = (res.getString(res.getColumnIndex(SERVER_TIME_OUT)));
                bean.IN_ADDRESS = (res.getString(res.getColumnIndex(IN_ADDRESS)));
                bean.OUT_ADDRESS = (res.getString(res.getColumnIndex(OUT_ADDRESS)));
                bean.IN_TIME = (res.getString(res.getColumnIndex(IN_TIME)));
                bean.OUT_TIME = (res.getString(res.getColumnIndex(OUT_TIME)));
                bean.WORKING_HOURS = (res.getString(res.getColumnIndex(WORKING_HOURS)));
                bean.IMAGE_DATA = (res.getString(res.getColumnIndex(IMAGE_DATA)));

                try {
                    bean.CURRENT_MILLIS = (Long.parseLong(res.getString(res.getColumnIndex(CURRENT_MILLIS))));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }
                bean.IN_LAT_LONG = (res.getString(res.getColumnIndex(IN_LAT_LONG)));
                bean.OUT_LAT_LONG = (res.getString(res.getColumnIndex(OUT_LAT_LONG)));
                bean.IN_FILE_NAME = (res.getString(res.getColumnIndex(IN_FILE_NAME)));
                bean.IN_FILE_LENGTH = (res.getString(res.getColumnIndex(IN_FILE_LENGTH)));
                bean.IN_FILE_VALUE = (res.getString(res.getColumnIndex(IN_FILE_VALUE)));
                bean.OUT_FILE_NAME = (res.getString(res.getColumnIndex(OUT_FILE_NAME)));
                bean.OUT_FILE_LENGTH = (res.getString(res.getColumnIndex(OUT_FILE_LENGTH)));
                bean.OUT_FILE_VALUE = (res.getString(res.getColumnIndex(OUT_FILE_VALUE)));

                bean.IN_STATUS = (res.getString(res.getColumnIndex(IN_STATUS)));
                bean.OUT_STATUS = (res.getString(res.getColumnIndex(OUT_STATUS)));


                //array_list.add(bean);
                array_list = bean;
                res.moveToNext();
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        } finally {

            res.close();
            db.close();
        }


        return array_list;
    }

    /********************************  insert  route detail   *************************************/
    public void insertRouteDetail(
            String pernr,
            String budat,
            String route_code,
            String route_name,
            String kunnr,
            String partner,
            String partner_class,
            String latitude,
            String longitude,
            String partner_name,
            String land1,
            String land_txt,
            String state_code,
            String state_txt,
            String district_code,
            String district_txt,
            String taluka_code,
            String taluka_txt,
            String address,
            String email,
            String mob_no,
            String tel_number,
            String pincode,
            String contact_person,
            String distributor_code,
            String distributor_name,
            String phone_number,
            String vkorg,
            String vtweg,
            String interested,
            String ktokd
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_ROUTE_NAME, route_name);
            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, state_code);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_CODE, district_code);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_CODE, taluka_code);
            values.put(KEY_TALUKA_TXT, taluka_txt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_EMAIL, email);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_TEL_NUMBER, tel_number);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_VKORG, vkorg);
            values.put(KEY_VTWEG, vtweg);
            values.put(KEY_INTRESTED, interested);
            values.put(KEY_KTOKD, ktokd);


            // Insert Row
            long i = db.insert(TABLE_ROUTE_DETAIL, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /********************************  insert  new added customer   *************************************/
    public void insertNewAddedCustomer(
            String pernr,
            String route_code,
            String route_name,
            String partner,
            String partner_class,
            String partner_name,
            String land1,
            String land_txt,
            String state_code,
            String state_txt,
            String district_code,
            String district_txt,
            String taluka_code,
            String taluka_txt,
            String address,
            String email,
            String mob_no,
            String tel_number,
            String aadhar_card,
            String pan_card,
            String tin_no,
            String market_place,
            String dob,
            String pincode,
            String contact_person,
            String contact_person_phone,
            String distributor_code,
            String distributor_name,
            String intrested,
            String budat,
            String time,
            String added_at_latlong,
            String vkorg,
            String vtweg

    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_ROUTE_NAME, route_name);
            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, state_code);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_CODE, district_code);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_CODE, taluka_code);
            values.put(KEY_TALUKA_TXT, taluka_txt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_EMAIL, email);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_TEL_NUMBER, tel_number);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_CONTACT_PERSON_PHONE, contact_person_phone);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PHONE_NUMBER, mob_no);
            values.put(KEY_AADHAR_CARD, aadhar_card);
            values.put(KEY_PAN_CARD, pan_card);
            values.put(KEY_TIN_NO, tin_no);
            values.put(KEY_MARKET_PLACE, market_place);
            values.put(KEY_DOB, dob);
            values.put(KEY_CUSTOMER_CATAGORY, "NEW");  // new customer addred by mobile app
            values.put(KEY_INTRESTED, intrested);
            values.put(KEY_ADDED_AT_LATLONG, added_at_latlong);
            values.put(KEY_VKORG, vkorg);
            values.put(KEY_VTWEG, vtweg);
            values.put(KEY_LATITUDE, "0.0");
            values.put(KEY_LONGITUDE, "0.0");
            values.put(KEY_KTOKD, "9999");
            values.put(KEY_SYNC, "NOT");
            long i = db.insert(TABLE_NEW_ADDED_CUSTOMER, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /********************************  insert  attendance ************************************************/
    public void insertAttendance(String pernr,
                                 String begdat,
                                 String indz,
                                 String iodz,
                                 String totdz,
                                 String atn_status,
                                 String leave_typ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pernr);
            values.put(KEY_BEGDAT, begdat);
            values.put(KEY_INDZ, indz);
            values.put(KEY_IODZ, iodz);
            values.put(KEY_TOTDZ, totdz);
            values.put(KEY_ATN_STATUS, atn_status);
            values.put(KEY_LEAVE_TYP, leave_typ);


            // db.delete(TABLE_ATTENDANCE,null,null);

            // Insert Row
            long i = db.insert(TABLE_ATTENDANCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /********************************  insert  attendance ************************************************/
    public void insertCmpattach(String cmpno,
                                String image1,
                                String image2,
                                String image3,
                                String image4,
                                String image5,
                                String image6,
                                String image7,
                                String image8,
                                String image9,
                                String image10,
                                String image11,
                                String image12,
                                String image13,
                                String image14,
                                String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            // Insert Row
            long i = db.insert(TABLE_CMP_ATTACHMENT, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /******************************* Insert Target **********************************************/
    public void insertTarget(String begda,
                             String endda,
                             String fr_pernr,
                             String fr_ename,
                             String fr_department,
                             String fr_target,
                             String fr_net_sale,
                             String fr_position) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_BEGDA, begda);
            values.put(KEY_ENDDA, endda);
            values.put(KEY_FR_PERNR, fr_pernr);
            values.put(KEY_FR_ENAME, fr_ename);
            values.put(KEY_FR_DEPARTMENT, fr_department);
            values.put(KEY_FR_TARGET, fr_target);
            values.put(KEY_FR_NET_SALE, fr_net_sale);
            values.put(KEY_FR_POSITION, fr_position);
            values.put(KEY_TO_PERNR, LoginBean.getUseid());

            // Insert Row
            long i = db.insert(TABLE_TARGET, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /******************************* insert activity target **********************************************/
    public void insertActivityTarget(
            String begda,
            String endda,
            String pernr,
            String ename,
            String activity_code,
            String activity_name,
            String indv_target,
            String indv_achv,
            String hrcy_target,
            String hrcy_achv
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_BEGDA, begda);
            values.put(KEY_ENDDA, endda);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_ACTIVITY_CODE, activity_code);
            values.put(KEY_ACTIVITY_NAME, activity_name);
            values.put(KEY_INDV_ACT_TARGET, indv_target);
            values.put(KEY_INDV_ACT_ACHIEVEMENT, indv_achv);
            values.put(KEY_HRCY_ACT_TARGET, hrcy_target);
            values.put(KEY_HRCY_ACT_ACHIEVEMENT, hrcy_achv);

            // Insert Row
            long i = db.insert(TABLE_ACTIVITY_TARGET, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert DSR_entry_help data **********************************************/

    public void insertDsrEntryHelp(String help_code, String help_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_HELP_CODE, help_code);
            values.put(KEY_HELP_NAME, help_name);

            // Insert Row
            long i = db.insert(TABLE_SEARCH_HELP, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert Complaint inprocess data for offline record **********************************************/

    public void insertInprocessComplaint(String pernr,
                                         String ename,
                                         String cmpno,
                                         String follow_up_date,
                                         String reason,
                                         String reasonid,
                                         String cr_date,
                                         String cr_time,
                                         String latitude,
                                         String longitude,
                                         String status
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);

            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_REASON, reason);
            values.put(KEY_NAME, reasonid);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_CMPLN_STATUS, status);


            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_ZINPROCESS_COMPLAINT, null, values);

            //Log.d("audio_action111",""+i+"--"+pernr+"---"+cmpno+"--"+audio_record);


            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert closure request for offline record **********************************************/

    public void insertClosureRequestComplaint(
            String pernr,
            String cmpno,
            String posnr,

            String category,
            /* String  payment_by ,*/
            String dealer,
            String customer,
            String company,
            String customer_feedback,
            String customer_status,
            String freelance,
            String dea,
            String com,
            String closer_reson,
            String defect,
            String relt_to,
            String focamt,

            String cr_date,
            String cr_time,
            String latitude,
            String longitude

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_CATEGORY, category);
            /* values.put( KEY_PAYMENT_BY ,payment_by);*/
            values.put(KEY_DEALER, dealer);
            values.put(KEY_CUSTOMER, customer);
            values.put(KEY_COMPANY, company);
            values.put(KEY_FEEDBACK_CUSTOMER, customer_feedback);
            values.put(KEY_FEEDBACK_STATUS, customer_status);
            values.put(KEY_CLOSER_RESON, closer_reson);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_RELT_TO, relt_to);
            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PAY_FREELANCER, freelance);
            values.put(KEY_PAY_COM, com);
            values.put(KEY_PAY_DEA, dea);
            values.put(KEY_FOC_AMT, focamt);

            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_CLOSE_COMPLAINT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /****************************** insert complaint image  **********************************************/

    public void insertComplaintImage(
            String cmpno,
            String posnr,
            String category,
            String image
//            , String pdfData
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_IMAGE, image);
//            values.put(KEY_PDF1, pdfData);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CR_DATE, new CustomUtility().getCurrentDate());
            values.put(KEY_SYNC, "NOT");
            long i = db.insert(TABLE_ZCMPLN_IMAGE, null, values);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /****************************** insert complaint distance **********************************************/

    public void insertComplaintDistance(
            String cmpno,
            String pernr,
            String lat_long
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_LAT_LONG, lat_long);
            values.put(KEY_CR_DATE, new CustomUtility().getCurrentDate());
            values.put(KEY_CR_TIME, new CustomUtility().getCurrentTime());
            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_DISTANCE, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert frwd app data for offline record **********************************************/

    public void insertfrwdappcmp(
            String awaitpernr,
            String cmpno,
            String posnr,
            String category,
            String dealer,
            String customer,
            String company,
            String freelance,
            String dea,
            String com,
            String defect,
            String relt_to,
            String focamt,
            String cr_date,
            String cr_time,
            String latitude,
            String longitude,
            String pendpernr,
            String awaitrmrk) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_AWT_PERNR, awaitpernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_DEALER, dealer);
            values.put(KEY_CUSTOMER, customer);
            values.put(KEY_COMPANY, company);
            values.put(KEY_PAY_FREELANCER, freelance);
            values.put(KEY_PAY_DEA, dea);
            values.put(KEY_PAY_COM, com);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_RELT_TO, relt_to);
            values.put(KEY_FOC_AMT, focamt);
            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PEND_PERNR, pendpernr);
            values.put(KEY_AWT_APR_RMK, awaitrmrk);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_FRWD_APP_CMP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert pend app data for offline record **********************************************/

    public void insertpendappcmp(String cmpno, String status, String remark,
                                 String pendpernr) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pendpernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_STATUS, status);
            values.put(KEY_REMARK, remark);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_PEND_APP_CMP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert DSR entry data for offline record **********************************************/

    public void insertDsrEntry(String pernr,
                               String budat,
                               String time,
                               String help_name,
                               String agenda,
                               String outcomes,
                               String latitude,
                               String longitude) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);

            values.put(KEY_TIME_IN, time);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_AGENDA, agenda);
            values.put(KEY_COMMENT, outcomes);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_DSR_ENTRY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert no order data for offline record **********************************************/

    public void insertNoOrderData(String pernr,
                                  String budat,
                                  String time,
                                  String help_name,
                                  String comment,
                                  String latitude,
                                  String longitude,
                                  String phone_number,
                                  String route_code,
                                  String no_order_image) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);

            values.put(KEY_TIME_IN, time);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_COMMENT, comment);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(IN_IMAGE, no_order_image);

            values.put(KEY_SYNC, "NOT");
            // Insert Row
            long i = db.insert(TABLE_NO_ORDER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert check in chek out data for offline record **********************************************/

    public void insertCheckInOut(String pernr, String date_in, String time_in, String timeout,
                                 String checkInLat, String checkOutLat, String checkInLong, String checkOutLong, String comment, String helpName,
                                 String route_code, String audioRecord, String keySync, String patnerName, String districtName, String chatApp,
                                 String followUpDate, String conversationStatus, String srvCntBordImg, String srvCntTrnLtrImg, String certificateImg,
                                 String slfyServPer, String sprPrtStkImg, String keyPrdTrnImg, String keyOtherImg, String phoneNumber) {


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PERNR, pernr);
        contentValues.put(KEY_DATE_IN, date_in);
        contentValues.put(KEY_TIME_IN, time_in);
        contentValues.put(KEY_TIME_OUT, timeout);
        contentValues.put(KEY_CHECK_IN_LATITUDE, checkInLat);
        contentValues.put(KEY_CHECK_OUT_LATITUDE, checkOutLat);
        contentValues.put(KEY_CHECK_IN_LONGITUDE, checkInLong);
        contentValues.put(KEY_CHECK_OUT_LONGITUDE, checkOutLong);
        contentValues.put(KEY_COMMENT, comment);
        contentValues.put(KEY_HELP_NAME, helpName);
        contentValues.put(KEY_ROUTE_CODE, route_code);
        contentValues.put(KEY_AUDIO_RECORD, audioRecord);
        contentValues.put(KEY_SYNC, keySync);
        contentValues.put(KEY_PARTNER_NAME, patnerName);
        contentValues.put(KEY_DISTRICT_TXT, districtName);
        contentValues.put(KEY_CHAT_APP, chatApp);
        contentValues.put(KEY_FOLLOW_UP_DATE, followUpDate);
        contentValues.put(KEY_CONVERSION_STATUS, conversationStatus);
        contentValues.put(KEY_SRV_CNT_BORD_IMG, srvCntBordImg);
        contentValues.put(KEY_SRV_CNT_TRN_LTR_IMG, srvCntTrnLtrImg);
        contentValues.put(KEY_CERTIFICATE_IMG, certificateImg);
        contentValues.put(KEY_SLFY_SERV_PER, slfyServPer);
        contentValues.put(KEY_SPR_PRT_STK_IMG, sprPrtStkImg);
        contentValues.put(KEY_PRD_TRN_IMG, keyPrdTrnImg);
        contentValues.put(KEY_OTHR_IMG, keyOtherImg);
        contentValues.put(KEY_PHONE_NUMBER, phoneNumber);


        database.insert(TABLE_CHECK_IN_OUT, null, contentValues);
        database.close();

      /*  // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pernr);
            values.put(KEY_DATE_IN, date_in);
            values.put(KEY_TIME_IN, time_in);
            values.put(KEY_CHECK_IN_LATITUDE, check_in_latitude);
            values.put(KEY_CHECK_IN_LONGITUDE, check_in_longitude);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            Log.e("values", String.valueOf(values));
          db.insert(TABLE_CHECK_IN_OUT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }*/
    }


    public void updateServiceCenterSurvey(
            String kunnr,
            String lat_long
    ) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_LAT_LONG, lat_long);


            String updateQuery = "UPDATE "
                    + TABLE_SERVICE_CENTER +
                    " SET lat_long = '" + lat_long +

                    "' WHERE kunnr = '" + kunnr + "'";

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
            db.endTransaction();
            if (c != null) {
                c.close();
            }
            db.close();


        }
    }


    /****************************** update check out chek out data for offline record **********************************************/

    public void updateCheckInOut(String pernr, String date_in, String time_in, String timeout,
                                 String checkInLat, String checkOutLat, String checkInLong, String checkOutLong, String comment, String helpName,
                                 String route_code, String audioRecord, String keySync, String patnerName, String districtName, String chatApp,
                                 String followUpDate, String conversationStatus, String srvCntBordImg, String srvCntTrnLtrImg, String certificateImg,
                                 String slfyServPer, String sprPrtStkImg, String keyPrdTrnImg, String keyOtherImg, String phoneNumber
    ) {

     /*   // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            String where = " ";

            values.put(KEY_PERNR, pernr);
            values.put(KEY_DATE_OUT, date_out);
            values.put(KEY_TIME_OUT, time_out);
            values.put(KEY_CHECK_OUT_LATITUDE, check_out_latitude);
            values.put(KEY_CHECK_OUT_LONGITUDE, check_out_longitude);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_AUDIO_RECORD, audio_record);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_PARTNER_NAME, customer_name);
            values.put(KEY_DISTRICT_TXT, city_name);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);
            values.put(KEY_CONVERSION_STATUS, conversion_status);
            values.put(KEY_SRV_CNT_BORD_IMG, srv_cnt_brd_img);
            values.put(KEY_SRV_CNT_TRN_LTR_IMG, srv_cnt_trn_ltr_img);
            values.put(KEY_CERTIFICATE_IMG, certificate_img);
            values.put(KEY_SLFY_SERV_PER, slfy_serv_per);
            values.put(KEY_SPR_PRT_STK_IMG, spr_prt_stk_img);
            values.put(KEY_PRD_TRN_IMG, prd_trn_img);
            values.put(KEY_OTHR_IMG, other_img);
            values.put(KEY_SYNC, "NOT");
            values.put(KEY_CHAT_APP, "NOT");

            where = KEY_PERNR + "='" + pernr + "'" + " AND " +
                    KEY_DATE_IN + "='" + date_out + "'" + " AND " +
                    KEY_PHONE_NUMBER + "='" + phone_number + "'";

            Log.e("UpdateCheckInValue====>", String.valueOf(values));
            db.update(TABLE_CHECK_IN_OUT, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();


        }*/


      SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE TABLE_CHECK_IN_OUT SET pernr = ?, date_in = ?, time_in = ?, timeout = ?,checkInLat = ?,checkOutLat = ?, " +
                "checkInLong = ?, checkOutLong = ?, comment = ?,helpName = ?, route_code = ?, audioRecord = ?, keySync = ?,patnerName = ?," +
                " districtName = ?, chatApp = ?, followUpDate = ?,conversationStatus = ?,srvCntBordImg = ?," +
                "srvCntTrnLtrImg = ?, certificateImg = ?, slfyServPer = ?, sprPrtStkImg = ?,keyPrdTrnImg = ?,keyOtherImg = ?,phoneNumber = ? WHERE ID = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, pernr);
        statement.bindString(2, date_in);
        statement.bindString(3, time_in);
        statement.bindString(4, checkInLat);
        statement.bindString(5, checkOutLat);
        statement.bindString(6, checkInLong);
        statement.bindString(7, checkOutLong);
        statement.bindString(8, comment);
        statement.bindString(9, helpName);
        statement.bindString(10, route_code);
        statement.bindString(11, audioRecord);
        statement.bindString(12, keySync);
        statement.bindString(13, patnerName);
        statement.bindString(14, districtName);
        statement.bindString(15, chatApp);
        statement.bindString(16, followUpDate);
        statement.bindString(17, conversationStatus);
        statement.bindString(18, srvCntBordImg);
        statement.bindString(19, srvCntTrnLtrImg);
        statement.bindString(20, certificateImg);
        statement.bindString(21, slfyServPer);
        statement.bindString(22, sprPrtStkImg);
        statement.bindString(23, keyPrdTrnImg);
        statement.bindString(24, keyOtherImg);
        statement.bindString(25, phoneNumber);

        statement.execute();
        database.close();
    }


    /***********************************  insert mark attendance ******************************/
    public long insertMarkAttendance(AttendanceBean attendanceBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        long t = 0;

        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(TYPE, attendanceBean.TYPE);
            contentValues.put(PERNR, attendanceBean.PERNR);
            contentValues.put(BEGDA, attendanceBean.BEGDA);
            contentValues.put(SERVER_DATE_IN, attendanceBean.SERVER_DATE_IN);
            contentValues.put(SERVER_TIME_IN, attendanceBean.SERVER_TIME_IN);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            contentValues.put(IN_ADDRESS, attendanceBean.IN_ADDRESS);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            contentValues.put(IN_TIME, attendanceBean.IN_TIME);
            contentValues.put(OUT_TIME, attendanceBean.OUT_TIME);
            contentValues.put(WORKING_HOURS, attendanceBean.WORKING_HOURS);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            contentValues.put(IN_LAT_LONG, attendanceBean.IN_LAT_LONG);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            contentValues.put(IN_FILE_NAME, attendanceBean.IN_FILE_NAME);
            contentValues.put(IN_FILE_LENGTH, attendanceBean.IN_FILE_LENGTH);
            contentValues.put(IN_FILE_VALUE, attendanceBean.IN_FILE_VALUE);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);
            contentValues.put(IN_STATUS, attendanceBean.IN_STATUS);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);

            contentValues.put(IN_IMAGE, attendanceBean.IN_IMAGE);

            contentValues.put(KEY_SYNC, "NOT");

            t = db.insert(TABLE_MARK_ATTENDANCE, null, contentValues);

            //System.out.println(t);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            // db.endTransaction();

            db.close();
        }

        return t;
    }


    /***************** update unsync data *********************













     /***************** end update unsync data ********************/


    public void insertUnsyncDataChatApp(String table_name, String key_id) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;


        try {
            values = new ContentValues();


            values.put(KEY_TABLE_NAME, table_name);
            values.put(KEY_SYNC_ID, key_id);
            long i = db.insert(TABLE_DATA_SYNC_CHAT_APP, null, values);


            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }

    }


    public void updateAudioChatApp(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateDataSendToChatApp(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            Log.d("sync_id", "" + key_id + "" + table_name);


            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateComplaintAudio(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateUnsyncData(String table_name, String key_id) {
        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        try {
            db = this.getWritableDatabase();
            contentValues.put(KEY_SYNC, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    /**************************update mark atendnace ********************************************/

    public boolean updateMarkAttendance(AttendanceBean attendanceBean) {
        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        try {
            db = this.getWritableDatabase();
            contentValues.put(PERNR, attendanceBean.PERNR);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            contentValues.put(OUT_TIME, attendanceBean.SERVER_TIME_OUT);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);
            contentValues.put(OUT_IMAGE, attendanceBean.OUT_IMAGE);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);
            contentValues.put(KEY_SYNC, "NOT");
            long t = db.update(TABLE_MARK_ATTENDANCE, contentValues, BEGDA + "='" + attendanceBean.SERVER_DATE_OUT + "'" + " AND " +
                    PERNR + "='" + LoginBean.getUseid() + "'", null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return true;
    }

    /************ select mark attendance****************************************************/
    @SuppressLint("Range")
    public AttendanceBean getMarkAttendanceByDate(String date, String id) {
        AttendanceBean array_list = new AttendanceBean();
        Cursor res = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "select * from " + TABLE_MARK_ATTENDANCE + " where  BEGDA = " + "'" + date + "'" +
                    " AND " + PERNR + " = '" + id + "'";
            res = db.rawQuery(query, null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                AttendanceBean bean = new AttendanceBean();
                bean.TYPE = (res.getString(res.getColumnIndex(TYPE)));
                bean.PERNR = (res.getString(res.getColumnIndex(PERNR)));
                bean.BEGDA = (res.getString(res.getColumnIndex(BEGDA)));
                bean.SERVER_DATE_IN = (res.getString(res.getColumnIndex(SERVER_DATE_IN)));
                bean.SERVER_TIME_IN = (res.getString(res.getColumnIndex(SERVER_TIME_IN)));
                bean.SERVER_DATE_OUT = (res.getString(res.getColumnIndex(SERVER_DATE_OUT)));
                bean.SERVER_TIME_OUT = (res.getString(res.getColumnIndex(SERVER_TIME_OUT)));
                bean.IN_ADDRESS = (res.getString(res.getColumnIndex(IN_ADDRESS)));
                bean.OUT_ADDRESS = (res.getString(res.getColumnIndex(OUT_ADDRESS)));
                bean.IN_TIME = (res.getString(res.getColumnIndex(IN_TIME)));
                bean.OUT_TIME = (res.getString(res.getColumnIndex(OUT_TIME)));
                bean.WORKING_HOURS = (res.getString(res.getColumnIndex(WORKING_HOURS)));
                bean.IMAGE_DATA = (res.getString(res.getColumnIndex(IMAGE_DATA)));

                try {
                    bean.CURRENT_MILLIS = (Long.parseLong(res.getString(res.getColumnIndex(CURRENT_MILLIS))));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }
                bean.IN_LAT_LONG = (res.getString(res.getColumnIndex(IN_LAT_LONG)));
                bean.OUT_LAT_LONG = (res.getString(res.getColumnIndex(OUT_LAT_LONG)));
                bean.IN_FILE_NAME = (res.getString(res.getColumnIndex(IN_FILE_NAME)));
                bean.IN_FILE_LENGTH = (res.getString(res.getColumnIndex(IN_FILE_LENGTH)));
                bean.IN_FILE_VALUE = (res.getString(res.getColumnIndex(IN_FILE_VALUE)));
                bean.OUT_FILE_NAME = (res.getString(res.getColumnIndex(OUT_FILE_NAME)));
                bean.OUT_FILE_LENGTH = (res.getString(res.getColumnIndex(OUT_FILE_LENGTH)));
                bean.OUT_FILE_VALUE = (res.getString(res.getColumnIndex(OUT_FILE_VALUE)));
                bean.IN_STATUS = (res.getString(res.getColumnIndex(IN_STATUS)));
                bean.OUT_STATUS = (res.getString(res.getColumnIndex(OUT_STATUS)));
                array_list = bean;
                res.moveToNext();
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        } finally {
            res.close();
            db.close();
        }
        return array_list;
    }

    /*   get all attendance */
    @SuppressLint("Range")
    public ArrayList<AttendanceBean> getAllAttendance() {
        ArrayList<AttendanceBean> array_list = new ArrayList<AttendanceBean>();
        SQLiteDatabase db = null;
        String selectQuery = null;
        String userid = LoginBean.getUseid();
        Cursor res = null;
        try {
            db = this.getReadableDatabase();
            // res = db.rawQuery("select * from " + TABLE_MARK_ATTENDANCE, null);
            selectQuery = "SELECT * FROM " + TABLE_MARK_ATTENDANCE
                    + " WHERE " + PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";
            res = db.rawQuery(selectQuery, null);
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                AttendanceBean bean = new AttendanceBean();
                //   bean.ID = (res.getString(res.getColumnIndex(AttendanceDBFields.ID)));
                bean.KEY_ID = (res.getString(res.getColumnIndex(KEY_ID)));
                bean.TYPE = (res.getString(res.getColumnIndex(TYPE)));
                bean.PERNR = (res.getString(res.getColumnIndex(PERNR)));
                bean.BEGDA = (res.getString(res.getColumnIndex(BEGDA)));
                bean.SERVER_DATE_IN = (res.getString(res.getColumnIndex(SERVER_DATE_IN)));
                bean.SERVER_TIME_IN = (res.getString(res.getColumnIndex(SERVER_TIME_IN)));
                bean.SERVER_DATE_OUT = (res.getString(res.getColumnIndex(SERVER_DATE_OUT)));
                bean.SERVER_TIME_OUT = (res.getString(res.getColumnIndex(SERVER_TIME_OUT)));
                bean.IN_ADDRESS = (res.getString(res.getColumnIndex(IN_ADDRESS)));
                bean.OUT_ADDRESS = (res.getString(res.getColumnIndex(OUT_ADDRESS)));
                bean.IN_TIME = (res.getString(res.getColumnIndex(IN_TIME)));
                bean.OUT_TIME = (res.getString(res.getColumnIndex(OUT_TIME)));
                bean.WORKING_HOURS = (res.getString(res.getColumnIndex(WORKING_HOURS)));
                bean.IMAGE_DATA = (res.getString(res.getColumnIndex(IMAGE_DATA)));
                bean.IN_IMAGE = (res.getString(res.getColumnIndex(IN_IMAGE)));
                bean.OUT_IMAGE = (res.getString(res.getColumnIndex(OUT_IMAGE)));
                try {
                    bean.CURRENT_MILLIS = (Long.parseLong(res.getString(res.getColumnIndex(CURRENT_MILLIS))));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }
                bean.IN_LAT_LONG = (res.getString(res.getColumnIndex(IN_LAT_LONG)));
                bean.OUT_LAT_LONG = (res.getString(res.getColumnIndex(OUT_LAT_LONG)));
                bean.IN_FILE_NAME = (res.getString(res.getColumnIndex(IN_FILE_NAME)));
                bean.IN_FILE_LENGTH = (res.getString(res.getColumnIndex(IN_FILE_LENGTH)));
                bean.IN_FILE_VALUE = (res.getString(res.getColumnIndex(IN_FILE_VALUE)));
                bean.OUT_FILE_NAME = (res.getString(res.getColumnIndex(OUT_FILE_NAME)));
                bean.OUT_FILE_LENGTH = (res.getString(res.getColumnIndex(OUT_FILE_LENGTH)));
                bean.OUT_FILE_VALUE = (res.getString(res.getColumnIndex(OUT_FILE_VALUE)));
                bean.IN_STATUS = (res.getString(res.getColumnIndex(IN_STATUS)));
                bean.OUT_STATUS = (res.getString(res.getColumnIndex(OUT_STATUS)));
                array_list.add(bean);
                res.moveToNext();
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        } finally {
            res.close();
            db.close();
        }
        return array_list;
    }

    /*********************************** select check in check out data ****************************/
    ArrayList<CheckInOutBean> list_checkInOut = new ArrayList<>();

    @SuppressLint("Range")
    public ArrayList<CheckInOutBean> getCheckInOut() {

        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_CHECK_IN_OUT, null);

        // moving our cursor to first position.
        if (cursor!=null && cursor.moveToFirst()) {

            do {
                CheckInOutBean checkInOut = new CheckInOutBean();
                checkInOut.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                checkInOut.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                checkInOut.setDate_in(cursor.getString(cursor.getColumnIndex(KEY_DATE_IN)));
                checkInOut.setTime_in(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                checkInOut.setDate_out(cursor.getString(cursor.getColumnIndex(KEY_DATE_OUT)));
                checkInOut.setTime_out(cursor.getString(cursor.getColumnIndex(KEY_TIME_OUT)));
                checkInOut.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_NAME)));
                checkInOut.setCheck_in_latitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_IN_LATITUDE)));
                checkInOut.setCheck_out_latitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_OUT_LATITUDE)));
                checkInOut.setCheck_in_longitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_IN_LONGITUDE)));
                checkInOut.setCheck_out_longitude(cursor.getString(cursor.getColumnIndex(KEY_CHECK_OUT_LONGITUDE)));
                checkInOut.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
                checkInOut.setComment(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
                checkInOut.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                checkInOut.setHelp_name(cursor.getString(cursor.getColumnIndex(KEY_HELP_NAME)));
                checkInOut.setAudio_record(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_RECORD)));
                checkInOut.setFollow_up_date(cursor.getString(cursor.getColumnIndex(KEY_FOLLOW_UP_DATE)));
                checkInOut.setConversion_status(cursor.getString(cursor.getColumnIndex(KEY_CONVERSION_STATUS)));
                checkInOut.setPhoto1_text(cursor.getString(cursor.getColumnIndex(KEY_SRV_CNT_BORD_IMG)));
                checkInOut.setPhoto2_text(cursor.getString(cursor.getColumnIndex(KEY_SRV_CNT_TRN_LTR_IMG)));
                checkInOut.setPhoto3_text(cursor.getString(cursor.getColumnIndex(KEY_CERTIFICATE_IMG)));
                checkInOut.setPhoto4_text(cursor.getString(cursor.getColumnIndex(KEY_SLFY_SERV_PER)));
                checkInOut.setPhoto5_text(cursor.getString(cursor.getColumnIndex(KEY_SPR_PRT_STK_IMG)));
                checkInOut.setPhoto6_text(cursor.getString(cursor.getColumnIndex(KEY_PRD_TRN_IMG)));
                checkInOut.setPhoto7_text(cursor.getString(cursor.getColumnIndex(KEY_OTHR_IMG)));

                list_checkInOut.add(checkInOut);

            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return list_checkInOut;


        /*SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(" SELECT * FROM " + TABLE_CHECK_IN_OUT, null);

        list_checkInOut.clear();
        CheckInOutBean checkInOut;

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                 checkInOut = new CheckInOutBean();
                checkInOut.setKey_id(cursor.getString(0));
                checkInOut.setPernr(cursor.getString(1));
                checkInOut.setDate_in(cursor.getString(2));
                checkInOut.setTime_in(cursor.getString(3));
                checkInOut.setDate_out(cursor.getString(4));
                checkInOut.setTime_out(cursor.getString(5));
                checkInOut.setCheck_in_latitude(cursor.getString(6));
                checkInOut.setCheck_out_latitude(cursor.getString(7));
                checkInOut.setCheck_in_longitude(cursor.getString(8));
                checkInOut.setCheck_out_longitude(cursor.getString(9));
                checkInOut.setComment(cursor.getString(10));
                checkInOut.setHelp_name(cursor.getString(11));
                checkInOut.setRoute_code(cursor.getString(12));
                checkInOut.setAudio_record(cursor.getString(13));
                checkInOut.setKeySynk(cursor.getString(14));
                checkInOut.setCustomer_name(cursor.getString(15));
                checkInOut.setDistrict(cursor.getString(16));
                checkInOut.setChatApp(cursor.getString(17));
                checkInOut.setFollow_up_date(cursor.getString(18));
                checkInOut.setConversion_status(cursor.getString(19));
                checkInOut.setPhoto1_text(cursor.getString(20));
                checkInOut.setPhoto2_text(cursor.getString(21));
                checkInOut.setPhoto3_text(cursor.getString(22));
                checkInOut.setPhoto4_text(cursor.getString(23));
                checkInOut.setPhoto5_text(cursor.getString(24));
                checkInOut.setPhoto6_text(cursor.getString(25));
                checkInOut.setPhoto7_text(cursor.getString(26));
                checkInOut.setPhone_number(cursor.getString(28));



                checkInOut.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                list_checkInOut.add(checkInOut);
            }
        }
        cursor.close();
        database.close();

        return list_checkInOut;*/
    }

    public int getLatestNewAddedCustomerKeyID() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        int maxValue = 0;
        try {
            selectQuery = "SELECT MAX (" + KEY_ID + ") FROM " + TABLE_NEW_ADDED_CUSTOMER;
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                maxValue = cursor.getInt(0);
////                deletedRow = db.delete(TABLE_NEW_ADDED_CUSTOMER, KEY_ID + " = '" + id + "'", null);
//                selectQuery = "SELECT * FROM " + TABLE_NEW_ADDED_CUSTOMER + " WHERE " + KEY_ID + " = '" + id + "'";
//                cursor = db.rawQuery(selectQuery, null);
//                if (cursor.getCount() > 0) {
//                        newAddedCustoemr.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
//                        newAddedCustoemr.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
//                        newAddedCustoemr.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
//                        newAddedCustoemr.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
//                        newAddedCustoemr.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
//                        newAddedCustoemr.setRoute_name(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME)));
//                        newAddedCustoemr.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_KUNNR)));
//                        newAddedCustoemr.setPartner(cursor.getString(cursor.getColumnIndex(KEY_PARTNER)));
//                        newAddedCustoemr.setPartner_class(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_CLASS)));
//                        newAddedCustoemr.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
//                        newAddedCustoemr.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
//                        newAddedCustoemr.setPartner_name(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_NAME)));
//                        newAddedCustoemr.setLand1(cursor.getString(cursor.getColumnIndex(KEY_LAND1)));
//                        newAddedCustoemr.setLand_txt(cursor.getString(cursor.getColumnIndex(KEY_LAND_TXT)));
//                        newAddedCustoemr.setState_code(cursor.getString(cursor.getColumnIndex(KEY_STATE_CODE)));
//                        newAddedCustoemr.setState_txt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TXT)));
//                        newAddedCustoemr.setDistrict_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_CODE)));
//                        newAddedCustoemr.setDistrict_txt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TXT)));
//                        newAddedCustoemr.setTaluka_code(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_CODE)));
//                        newAddedCustoemr.setTaluka_txt(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_TXT)));
//                        newAddedCustoemr.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
//                        newAddedCustoemr.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
//                        newAddedCustoemr.setMob_no(cursor.getString(cursor.getColumnIndex(KEY_MOB_NO)));
//                        newAddedCustoemr.setTel_number(cursor.getString(cursor.getColumnIndex(KEY_TEL_NUMBER)));
//                        newAddedCustoemr.setPincode(cursor.getString(cursor.getColumnIndex(KEY_PINCODE)));
//                        newAddedCustoemr.setContact_person(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_PERSON)));
//                        newAddedCustoemr.setDistributor_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_CODE)));
//                        newAddedCustoemr.setDistributor_name(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_NAME)));
//                        newAddedCustoemr.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
//                        newAddedCustoemr.setAadhar_card(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_CARD)));
//                        newAddedCustoemr.setPan_card(cursor.getString(cursor.getColumnIndex(KEY_PAN_CARD)));
//                        newAddedCustoemr.setTin_no(cursor.getString(cursor.getColumnIndex(KEY_TIN_NO)));
//                        newAddedCustoemr.setMarket_place(cursor.getString(cursor.getColumnIndex(KEY_MARKET_PLACE)));
//                        newAddedCustoemr.setDob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
//                        newAddedCustoemr.setIntrested(cursor.getString(cursor.getColumnIndex(KEY_INTRESTED)));
//                        newAddedCustoemr.setAdded_at_latlong(cursor.getString(cursor.getColumnIndex(KEY_ADDED_AT_LATLONG)));
//                }
//                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return maxValue;
    }

    /*********************************** select new added customer data****************************/
    @SuppressLint("Range")
    public NewAddedCustomerBean getLatestNewAddedCustomer(int keyId) {
        NewAddedCustomerBean newAddedCustomer = new NewAddedCustomerBean();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {
            selectQuery = "SELECT * FROM " + TABLE_NEW_ADDED_CUSTOMER + " WHERE " + KEY_ID + " = '" + keyId + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    newAddedCustomer.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    newAddedCustomer.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    newAddedCustomer.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    newAddedCustomer.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    newAddedCustomer.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
                    newAddedCustomer.setRoute_name(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME)));
                    newAddedCustomer.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_KUNNR)));
                    newAddedCustomer.setPartner(cursor.getString(cursor.getColumnIndex(KEY_PARTNER)));
                    newAddedCustomer.setPartner_class(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_CLASS)));
                    newAddedCustomer.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    newAddedCustomer.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    newAddedCustomer.setPartner_name(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_NAME)));
                    newAddedCustomer.setLand1(cursor.getString(cursor.getColumnIndex(KEY_LAND1)));
                    newAddedCustomer.setLand_txt(cursor.getString(cursor.getColumnIndex(KEY_LAND_TXT)));
                    newAddedCustomer.setState_code(cursor.getString(cursor.getColumnIndex(KEY_STATE_CODE)));
                    newAddedCustomer.setState_txt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TXT)));
                    newAddedCustomer.setDistrict_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_CODE)));
                    newAddedCustomer.setDistrict_txt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TXT)));
                    newAddedCustomer.setTaluka_code(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_CODE)));
                    newAddedCustomer.setTaluka_txt(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_TXT)));
                    newAddedCustomer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                    newAddedCustomer.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                    newAddedCustomer.setMob_no(cursor.getString(cursor.getColumnIndex(KEY_MOB_NO)));
                    newAddedCustomer.setTel_number(cursor.getString(cursor.getColumnIndex(KEY_TEL_NUMBER)));
                    newAddedCustomer.setPincode(cursor.getString(cursor.getColumnIndex(KEY_PINCODE)));
                    newAddedCustomer.setContact_person(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_PERSON)));
                    newAddedCustomer.setDistributor_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_CODE)));
                    newAddedCustomer.setDistributor_name(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_NAME)));
                    newAddedCustomer.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    newAddedCustomer.setAadhar_card(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_CARD)));
                    newAddedCustomer.setPan_card(cursor.getString(cursor.getColumnIndex(KEY_PAN_CARD)));
                    newAddedCustomer.setTin_no(cursor.getString(cursor.getColumnIndex(KEY_TIN_NO)));
                    newAddedCustomer.setMarket_place(cursor.getString(cursor.getColumnIndex(KEY_MARKET_PLACE)));
                    newAddedCustomer.setDob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
                    newAddedCustomer.setIntrested(cursor.getString(cursor.getColumnIndex(KEY_INTRESTED)));
                    newAddedCustomer.setAdded_at_latlong(cursor.getString(cursor.getColumnIndex(KEY_ADDED_AT_LATLONG)));
                }
//                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return newAddedCustomer;
    }

    @SuppressLint("Range")
    public ArrayList<NewAddedCustomerBean> getNewAddedCustomer() {
        String userid = LoginBean.getUseid();
        ArrayList<NewAddedCustomerBean> list_newAddedCustomer = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {
            selectQuery = "SELECT * FROM " + TABLE_NEW_ADDED_CUSTOMER
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_CUSTOMER_CATAGORY + " = '" + "NEW" + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NewAddedCustomerBean newAddedCustoemr = new NewAddedCustomerBean();
                    newAddedCustoemr.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    newAddedCustoemr.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    newAddedCustoemr.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    newAddedCustoemr.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    newAddedCustoemr.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
                    newAddedCustoemr.setRoute_name(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME)));
                    newAddedCustoemr.setKunnr(cursor.getString(cursor.getColumnIndex(KEY_KUNNR)));
                    newAddedCustoemr.setPartner(cursor.getString(cursor.getColumnIndex(KEY_PARTNER)));
                    newAddedCustoemr.setPartner_class(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_CLASS)));
                    newAddedCustoemr.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    newAddedCustoemr.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    newAddedCustoemr.setPartner_name(cursor.getString(cursor.getColumnIndex(KEY_PARTNER_NAME)));
                    newAddedCustoemr.setLand1(cursor.getString(cursor.getColumnIndex(KEY_LAND1)));
                    newAddedCustoemr.setLand_txt(cursor.getString(cursor.getColumnIndex(KEY_LAND_TXT)));
                    newAddedCustoemr.setState_code(cursor.getString(cursor.getColumnIndex(KEY_STATE_CODE)));
                    newAddedCustoemr.setState_txt(cursor.getString(cursor.getColumnIndex(KEY_STATE_TXT)));
                    newAddedCustoemr.setDistrict_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_CODE)));
                    newAddedCustoemr.setDistrict_txt(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TXT)));
                    newAddedCustoemr.setTaluka_code(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_CODE)));
                    newAddedCustoemr.setTaluka_txt(cursor.getString(cursor.getColumnIndex(KEY_TALUKA_TXT)));
                    newAddedCustoemr.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                    newAddedCustoemr.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                    newAddedCustoemr.setMob_no(cursor.getString(cursor.getColumnIndex(KEY_MOB_NO)));
                    newAddedCustoemr.setTel_number(cursor.getString(cursor.getColumnIndex(KEY_TEL_NUMBER)));
                    newAddedCustoemr.setPincode(cursor.getString(cursor.getColumnIndex(KEY_PINCODE)));
                    newAddedCustoemr.setContact_person(cursor.getString(cursor.getColumnIndex(KEY_CONTACT_PERSON)));
                    newAddedCustoemr.setDistributor_code(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_CODE)));
                    newAddedCustoemr.setDistributor_name(cursor.getString(cursor.getColumnIndex(KEY_DISTRIBUTOR_NAME)));
                    newAddedCustoemr.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    newAddedCustoemr.setAadhar_card(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_CARD)));
                    newAddedCustoemr.setPan_card(cursor.getString(cursor.getColumnIndex(KEY_PAN_CARD)));
                    newAddedCustoemr.setTin_no(cursor.getString(cursor.getColumnIndex(KEY_TIN_NO)));
                    newAddedCustoemr.setMarket_place(cursor.getString(cursor.getColumnIndex(KEY_MARKET_PLACE)));
                    newAddedCustoemr.setDob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
                    newAddedCustoemr.setIntrested(cursor.getString(cursor.getColumnIndex(KEY_INTRESTED)));
                    newAddedCustoemr.setAdded_at_latlong(cursor.getString(cursor.getColumnIndex(KEY_ADDED_AT_LATLONG)));
                    list_newAddedCustomer.add(newAddedCustoemr);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return list_newAddedCustomer;
    }

    /*********************************** select dsr entry data *********************************/
    @SuppressLint("Range")
    public ArrayList<DsrEntryBean> getDsrEntry() {
        String userid = LoginBean.getUseid();
        ArrayList<DsrEntryBean> list_dsrEntry = new ArrayList<>();
        list_dsrEntry.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_DSR_ENTRY
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //  Log.d("dsr_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    DsrEntryBean dsrEntryBean = new DsrEntryBean();
                    dsrEntryBean.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    dsrEntryBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    dsrEntryBean.setDate(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    dsrEntryBean.setHelp_name(cursor.getString(cursor.getColumnIndex(KEY_HELP_NAME)));
                    dsrEntryBean.setDsr_agenda(cursor.getString(cursor.getColumnIndex(KEY_AGENDA)));
                    dsrEntryBean.setDsr_outcomes(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
                    dsrEntryBean.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    dsrEntryBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    dsrEntryBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    list_dsrEntry.add(dsrEntryBean);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_dsrEntry;
    }


    /*********************************** select cmplnt frwd App entry data *********************************/


    @SuppressLint("Range")
    public ArrayList<ForwardForAppEntryBean> getfrwdcmpapp() {
        String userid = LoginBean.getUseid();
        ArrayList<ForwardForAppEntryBean> list_dsrEntry = new ArrayList<>();
        list_dsrEntry.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_FRWD_APP_CMP
                    + " WHERE " + KEY_AWT_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //  Log.d("dsr_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    ForwardForAppEntryBean dsrEntryBean = new ForwardForAppEntryBean();

                    dsrEntryBean.setKeyid(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    dsrEntryBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    dsrEntryBean.setPosnr(cursor.getString(cursor.getColumnIndex(KEY_POSNR)));
                    dsrEntryBean.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                    dsrEntryBean.setDealer(cursor.getString(cursor.getColumnIndex(KEY_DEALER)));
                    dsrEntryBean.setCustomer(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER)));
                    dsrEntryBean.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                    dsrEntryBean.setPay_freelancer(cursor.getString(cursor.getColumnIndex(KEY_PAY_FREELANCER)));
                    dsrEntryBean.setPay_dealer(cursor.getString(cursor.getColumnIndex(KEY_PAY_DEA)));
                    dsrEntryBean.setPay_company(cursor.getString(cursor.getColumnIndex(KEY_PAY_COM)));
                    dsrEntryBean.setDefect(cursor.getString(cursor.getColumnIndex(KEY_DEFECT)));
                    dsrEntryBean.setRelat_to(cursor.getString(cursor.getColumnIndex(KEY_RELT_TO)));
                    dsrEntryBean.setFoc_amt(cursor.getString(cursor.getColumnIndex(KEY_FOC_AMT)));
                    dsrEntryBean.setCrt_dt(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    dsrEntryBean.setCrt_tm(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    dsrEntryBean.setLat(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    dsrEntryBean.setLng(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));


                    dsrEntryBean.setAwaitpernr(cursor.getString(cursor.getColumnIndex(KEY_AWT_PERNR)));
                    dsrEntryBean.setPendpernr(cursor.getString(cursor.getColumnIndex(KEY_PEND_PERNR)));
                    dsrEntryBean.setAwaitrmrk(cursor.getString(cursor.getColumnIndex(KEY_AWT_APR_RMK)));
                    list_dsrEntry.add(dsrEntryBean);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_dsrEntry;
    }


    /*********************************** select cmplnt pend App entry data *********************************/


    @SuppressLint("Range")
    public ArrayList<ForwardForAppEntryBean> getpendcmpapp() {
        String userid = LoginBean.getUseid();
        ArrayList<ForwardForAppEntryBean> list_dsrEntry = new ArrayList<>();
        list_dsrEntry.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_PEND_APP_CMP
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                //  Log.d("dsr_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    ForwardForAppEntryBean dsrEntryBean = new ForwardForAppEntryBean();

                    dsrEntryBean.setKeyid(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    dsrEntryBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    dsrEntryBean.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    dsrEntryBean.setPendrmrk(cursor.getString(cursor.getColumnIndex(KEY_REMARK)));
                    dsrEntryBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    list_dsrEntry.add(dsrEntryBean);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_dsrEntry;
    }


    /*********************************** select employee gps activity  *****************************/


    @SuppressLint("Range")
    public ArrayList<EmployeeGPSActivityBean> getEmployeeGpsActivity(Context context) {
        String userid = LoginBean.getUseid();
        ArrayList<EmployeeGPSActivityBean> list_employeeGPSActivity = new ArrayList<>();
        list_employeeGPSActivity.clear();


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        LoginBean.userid = pref.getString("key_username", "userid");


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_GPS_ACTIVITY
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("gps_person_select",""+ userid);

            if (cursor.getCount() > 0) {

                //   Log.d("gps_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    EmployeeGPSActivityBean employeeGPSActivity = new EmployeeGPSActivityBean();

//                    Log.d("gps_count1",""+ cursor.getString(cursor.getColumnIndex(KEY_PERNR)) +
//                            cursor.getString(cursor.getColumnIndex(KEY_BUDAT)) +
//                            cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
//

                    employeeGPSActivity.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));

                    employeeGPSActivity.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    employeeGPSActivity.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    employeeGPSActivity.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    employeeGPSActivity.setEvent(cursor.getString(cursor.getColumnIndex(KEY_EVENT)));
                    employeeGPSActivity.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    employeeGPSActivity.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    employeeGPSActivity.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));

                    employeeGPSActivity.setCell_id(cursor.getString(cursor.getColumnIndex(KEY_CELL_ID)));
                    employeeGPSActivity.setLocation_code(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_CODE)));
                    employeeGPSActivity.setMobile_country_code(cursor.getString(cursor.getColumnIndex(KEY_MOBILE_COUNTRY_CODE)));
                    employeeGPSActivity.setMobile_network_code(cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NETWORK_CODE)));


                    list_employeeGPSActivity.add(employeeGPSActivity);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }


            db.close();
        }
        return list_employeeGPSActivity;
    }


    /*********************************** select call log  *****************************/


    @SuppressLint("Range")
    public ArrayList<CallLog> getCallLog(Context context) {
        String userid = LoginBean.getUseid();
        ArrayList<CallLog> list_callLog = new ArrayList<>();
        list_callLog.clear();


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        LoginBean.userid = pref.getString("key_username", "userid");


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_CALL_LOG
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("gps_person_select",""+ userid);

            if (cursor.getCount() > 0) {

                //   Log.d("gps_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    CallLog callLog = new CallLog();


//

                    callLog.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));

                    callLog.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    callLog.setDate(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    callLog.setTime(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    callLog.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    callLog.setName(cursor.getString(cursor.getColumnIndex(KEY_ENAME)));
                    callLog.setCall_type(cursor.getString(cursor.getColumnIndex(KEY_CALL_TYPE)));
                    callLog.setCall_duration(cursor.getString(cursor.getColumnIndex(KEY_CALL_DURATION)));


                    list_callLog.add(callLog);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }


            db.close();
        }
        return list_callLog;
    }


    /*********************************** select no order entry data  *********************************/


    @SuppressLint("Range")
    public ArrayList<NoOrderBean> getNoOrderData() {
        String userid = LoginBean.getUseid();
        ArrayList<NoOrderBean> list_noOrder = new ArrayList<>();
        list_noOrder.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_NO_ORDER
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                //  Log.d("dsr_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    NoOrderBean noOrderBean = new NoOrderBean();

                    noOrderBean.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    noOrderBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    noOrderBean.setDate(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    noOrderBean.setHelp_name(cursor.getString(cursor.getColumnIndex(KEY_HELP_NAME)));
                    noOrderBean.setComment(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
                    noOrderBean.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    noOrderBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    noOrderBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    noOrderBean.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    noOrderBean.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
                    noOrderBean.setImage(cursor.getString(cursor.getColumnIndex(IN_IMAGE)));

                    list_noOrder.add(noOrderBean);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_noOrder;
    }

    /*********************************** select survey data  *********************************/


    @SuppressLint("Range")
    public ArrayList<SurveyBean> getSurveyData() {

        String userid = LoginBean.getUseid();

        ArrayList<SurveyBean> list_survey = new ArrayList<>();
        list_survey.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_SURVEY
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                //  Log.d("dsr_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    SurveyBean surveyBean = new SurveyBean();

                    surveyBean.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    surveyBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    surveyBean.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    surveyBean.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    surveyBean.setComment(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
                    surveyBean.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    surveyBean.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    surveyBean.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    surveyBean.setOther_view(cursor.getString(cursor.getColumnIndex(KEY_OTHER_VIEW)));
                    surveyBean.setOwner_view(cursor.getString(cursor.getColumnIndex(KEY_OWNER_VIEW)));
                    surveyBean.setCard_view(cursor.getString(cursor.getColumnIndex(KEY_CARD_VIEW)));
                    surveyBean.setInner_view(cursor.getString(cursor.getColumnIndex(KEY_INNER_VIEW)));
                    surveyBean.setOuter_view(cursor.getString(cursor.getColumnIndex(KEY_OUTER_VIEW)));

                    list_survey.add(surveyBean);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_survey;
    }


    /*********************************** select inprocess complaint  *********************************/


    @SuppressLint("Range")
    public ArrayList<InprocessComplaint> getInprocessComplaintData() {

        String userid = LoginBean.getUseid();

        ArrayList<InprocessComplaint> list_inprocess = new ArrayList<>();
        list_inprocess.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_ZINPROCESS_COMPLAINT
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);
            Log.d("inprocess", "" + cursor.getCount());
            if (cursor.getCount() > 0) {

//                Log.d("cmpln_db_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    InprocessComplaint inprocessComplaint = new InprocessComplaint();

                    inprocessComplaint.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    inprocessComplaint.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    inprocessComplaint.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    inprocessComplaint.setFollow_up_date(cursor.getString(cursor.getColumnIndex(KEY_FOLLOW_UP_DATE)));
                    inprocessComplaint.setReason(cursor.getString(cursor.getColumnIndex(KEY_REASON)));
                    inprocessComplaint.setReasonid(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    inprocessComplaint.setCr_date(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    inprocessComplaint.setCr_time(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    inprocessComplaint.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    inprocessComplaint.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    inprocessComplaint.setCmpln_status(cursor.getString(cursor.getColumnIndex(KEY_CMPLN_STATUS)));


                    list_inprocess.add(inprocessComplaint);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_inprocess;
    }


    /*********************************** select complant image  *********************************/


    @SuppressLint("Range")
    public ArrayList<ComplaintImage> getComplaintImage() {
        ArrayList<ComplaintImage> list_complaintImage = new ArrayList<>();
        list_complaintImage.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {
            selectQuery = "SELECT * FROM " + TABLE_ZCMPLN_IMAGE + " WHERE " + KEY_SYNC + " = '" + "NOT" + "'";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    ComplaintImage complaintImage = new ComplaintImage();

                    complaintImage.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    complaintImage.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    complaintImage.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                    complaintImage.setPosnr(cursor.getString(cursor.getColumnIndex(KEY_POSNR)));
                    complaintImage.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));


                    list_complaintImage.add(complaintImage);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_complaintImage;
    }


    /*********************************** select complant pdf  *********************************/


    /*public ArrayList<ComplaintPdf> getComplaintPdf() {


        ArrayList<ComplaintPdf> list_complaintPdf = new ArrayList<>();
        list_complaintPdf.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_PDF_NAME
                    + " WHERE " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    ComplaintPdf complaintPdf = new ComplaintPdf();

                    complaintPdf.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    complaintPdf.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    complaintPdf.setPernr(cursor.getString(cursor.getColumnIndex(PERNR)));

                    complaintPdf.setName1(cursor.getString(cursor.getColumnIndex(KEY_PDF1)));
                    complaintPdf.setName2(cursor.getString(cursor.getColumnIndex(KEY_PDF2)));


                    list_complaintPdf.add(complaintPdf);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_complaintPdf;
    }*/


    /*********************************** select complant audio  *********************************/


    @SuppressLint("Range")
    public ArrayList<ComplaintAudio> getComplaintAudio() {

        ArrayList<ComplaintAudio> list_complaintAudio = new ArrayList<>();
        list_complaintAudio.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_AUDIO
                    + " WHERE " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("cmp_audio",""+cursor.getCount()) ;

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    ComplaintAudio complaintAudio = new ComplaintAudio();

                    complaintAudio.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    complaintAudio.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    complaintAudio.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    complaintAudio.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    complaintAudio.setTime(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    complaintAudio.setAudio_record(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_RECORD)));


                    list_complaintAudio.add(complaintAudio);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_complaintAudio;
    }


    /*********************************** select complant start  *********************************/


    @SuppressLint("Range")
    public ArrayList<ComplaintStart> getComplaintStart() {

        ArrayList<ComplaintStart> list_complaintStart = new ArrayList<>();
        list_complaintStart.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DISTANCE
                    + " WHERE " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("cmp_audio",""+cursor.getCount()) ;

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    ComplaintStart complaintStart = new ComplaintStart();

                    complaintStart.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    complaintStart.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    complaintStart.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    complaintStart.setCr_date(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    complaintStart.setCr_time(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    complaintStart.setLat_long(cursor.getString(cursor.getColumnIndex(KEY_LAT_LONG)));


                    list_complaintStart.add(complaintStart);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_complaintStart;
    }


    /*********************************** select clouser complaint  *********************************/


    @SuppressLint("Range")
    public ArrayList<ClouserComplaint> getClouserComplaintData() {

        String userid = LoginBean.getUseid();

        ArrayList<ClouserComplaint> list_clouserComplaint = new ArrayList<>();
        list_clouserComplaint.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_CLOSE_COMPLAINT
                    + " WHERE " + KEY_PERNR + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSOR", "*****" + cursor.getCount() + " " + selectQuery);
            if (cursor.getCount() > 0) {

                //  Log.d("clouser_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    ClouserComplaint clouserComplaint = new ClouserComplaint();

                    clouserComplaint.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    clouserComplaint.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    clouserComplaint.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    clouserComplaint.setPosnr(cursor.getString(cursor.getColumnIndex(KEY_POSNR)));
                    clouserComplaint.setReason(cursor.getString(cursor.getColumnIndex(KEY_REASON)));
                    clouserComplaint.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                    /*clouserComplaint.setPayment_by(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_BY)));*/
                    clouserComplaint.setCustomer(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER)));
                    clouserComplaint.setDealer(cursor.getString(cursor.getColumnIndex(KEY_DEALER)));
                    clouserComplaint.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));

                    clouserComplaint.setCustomerFeedback(cursor.getString(cursor.getColumnIndex(KEY_FEEDBACK_CUSTOMER)));
                    clouserComplaint.setCustomerFeedbackStatus(cursor.getString(cursor.getColumnIndex(KEY_FEEDBACK_STATUS)));


                    clouserComplaint.setCloser_reason(cursor.getString(cursor.getColumnIndex(KEY_CLOSER_RESON)));
                    clouserComplaint.setDefect(cursor.getString(cursor.getColumnIndex(KEY_DEFECT)));
                    clouserComplaint.setCmpln_relt_to(cursor.getString(cursor.getColumnIndex(KEY_RELT_TO)));
                    clouserComplaint.setDate(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    clouserComplaint.setTime(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    clouserComplaint.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    clouserComplaint.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    clouserComplaint.setPay_freelancer(cursor.getString(cursor.getColumnIndex(KEY_PAY_FREELANCER)));
                    clouserComplaint.setDea(cursor.getString(cursor.getColumnIndex(KEY_PAY_DEA)));
                    clouserComplaint.setCom(cursor.getString(cursor.getColumnIndex(KEY_PAY_COM)));
                    clouserComplaint.setFocamt(cursor.getString(cursor.getColumnIndex(KEY_FOC_AMT)));


                    list_clouserComplaint.add(clouserComplaint);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return list_clouserComplaint;
    }

    /*********************************** select login data  ***************************************/

    @SuppressLint("Range")
    public boolean getLogin() {
        long t = 0;
        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_LOGIN;


            c = db.rawQuery(selectQuery, null);
            LoginBean lb = new LoginBean();
            // Log.d("login",""+c.getCount() ) ;

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    lb.setLogin(c.getString(c.getColumnIndex(KEY_PERNR)), c.getString(c.getColumnIndex(KEY_ENAME)));

                }
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }


        return false;
    }

//    adhoc order methods


    @SuppressLint("Range")
    public ArrayList<BeanProductFinal> getOrderList() {

        String userid = LoginBean.getUseid();

        ArrayList<BeanProductFinal> orderlist = new ArrayList<BeanProductFinal>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            //selectQuery = "SELECT * FROM " + TABLE_ADHOC_FINAL;

            selectQuery = "SELECT * FROM " + TABLE_ADHOC_FINAL

                    + " WHERE " + KEY_PERSON + " = '" + userid + "'"
                    + " AND " + KEY_SYNC + " = '" + "NOT" + "'";


            cursor = db.rawQuery(selectQuery, null);

            //  Log.d("take_order",""+cursor.getCount());

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    BeanProductFinal beanproductfinal = new BeanProductFinal();

                    beanproductfinal.setKey_id(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    beanproductfinal.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));
                    beanproductfinal.setMatnr(cursor.getString(cursor.getColumnIndex(KEY_MATNR)));
                    beanproductfinal.setExtwg(cursor.getString(cursor.getColumnIndex(KEY_EXTWG)));
                    beanproductfinal.setMaktx(cursor.getString(cursor.getColumnIndex(KEY_MAKTX)));
                    beanproductfinal.setKbetr(cursor.getString(cursor.getColumnIndex(KEY_KBETR)));
                    beanproductfinal.setMenge(cursor.getString(cursor.getColumnIndex(KEY_MENGE)));
                    beanproductfinal.setTot_kbetr(cursor.getString(cursor.getColumnIndex(KEY_TOT_KBETR)));
                    beanproductfinal.setCustomer_name(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                    beanproductfinal.setPerson(cursor.getString(cursor.getColumnIndex(KEY_PERSON)));
                    beanproductfinal.setCr_date(cursor.getString(cursor.getColumnIndex(KEY_CR_DATE)));
                    beanproductfinal.setCr_time(cursor.getString(cursor.getColumnIndex(KEY_CR_TIME)));
                    beanproductfinal.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    beanproductfinal.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));

                    beanproductfinal.setRoute_code(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_CODE)));
                    beanproductfinal.setPartner_type(cursor.getString(cursor.getColumnIndex(KEY_PARTNER)));


                    orderlist.add(beanproductfinal);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return orderlist;
    }


    @SuppressLint("Range")
    public CmpReviewImageBean getReviewCmpImage(String cmpno) {

        CmpReviewImageBean cmpReviewImageBean = new CmpReviewImageBean();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            //selectQuery = "SELECT * FROM " + TABLE_ADHOC_FINAL;

            selectQuery = "SELECT * FROM " + TABLE_REVIEW_COMPLAINT_IMAGES + " WHERE " + KEY_CMPNO + " = '" + cmpno + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    cmpReviewImageBean = new CmpReviewImageBean();

                    cmpReviewImageBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    cmpReviewImageBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_IMAGE1)));
                    cmpReviewImageBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_IMAGE2)));
                    cmpReviewImageBean.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_IMAGE3)));
                    cmpReviewImageBean.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_IMAGE4)));
                    cmpReviewImageBean.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_IMAGE5)));
                    cmpReviewImageBean.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_IMAGE6)));
                    cmpReviewImageBean.setPhoto7(cursor.getString(cursor.getColumnIndex(KEY_IMAGE7)));
                    cmpReviewImageBean.setPhoto8(cursor.getString(cursor.getColumnIndex(KEY_IMAGE8)));
                    cmpReviewImageBean.setPhoto9(cursor.getString(cursor.getColumnIndex(KEY_IMAGE9)));
                    cmpReviewImageBean.setPhoto10(cursor.getString(cursor.getColumnIndex(KEY_IMAGE10)));
                    cmpReviewImageBean.setPhoto11(cursor.getString(cursor.getColumnIndex(KEY_IMAGE11)));
                    cmpReviewImageBean.setPhoto12(cursor.getString(cursor.getColumnIndex(KEY_IMAGE12)));
                    cmpReviewImageBean.setPhoto13(cursor.getString(cursor.getColumnIndex(KEY_IMAGE13)));

                    cmpReviewImageBean.setPhoto14(cursor.getString(cursor.getColumnIndex(KEY_IMAGE14)));
                    cmpReviewImageBean.setPhoto15(cursor.getString(cursor.getColumnIndex(KEY_IMAGE15)));

                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return cmpReviewImageBean;
    }

    public void createProduct(BeanProduct beanproduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MATNR, beanproduct.getMatnr());
        values.put(KEY_KUNNR, beanproduct.getKunnr());
        values.put(KEY_VKORG, beanproduct.getVkorg());
        values.put(KEY_VTWEG, beanproduct.getVtweg());
        values.put(KEY_EXTWG, beanproduct.getExtwg());
        values.put(KEY_MAKTX, beanproduct.getMaktx());
        values.put(KEY_KBETR, beanproduct.getKbetr());
        values.put(KEY_KONWA, beanproduct.getKonwa());
        values.put(KEY_MTART, beanproduct.getMtart());
        // insert row
        Long result = db.insert(TABLE_ADHOC, null, values);
    }

    public void createProductFinal(BeanProductFinal beanproductfinal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        Log.d("phone", "--"+beanproductfinal.getPhone_number()+"--"+ beanproductfinal.getCustomer_name() +"--"
//                + beanproductfinal.getPerson()+"--"+beanproductfinal.getCr_date()+"--"+beanproductfinal.getCr_time());

        values.put(KEY_PHONE_NUMBER, beanproductfinal.getPhone_number());
        values.put(KEY_MATNR, beanproductfinal.getMatnr());
        values.put(KEY_EXTWG, beanproductfinal.getExtwg());
        values.put(KEY_MAKTX, beanproductfinal.getMaktx());
        values.put(KEY_KBETR, beanproductfinal.getKbetr());
        values.put(KEY_MENGE, beanproductfinal.getMenge());
        values.put(KEY_TOT_KBETR, beanproductfinal.getTot_kbetr());
        values.put(KEY_CUSTOMER_NAME, beanproductfinal.getCustomer_name());
        values.put(KEY_PERSON, beanproductfinal.getPerson());
        values.put(KEY_CR_DATE, beanproductfinal.getCr_date());
        values.put(KEY_CR_TIME, beanproductfinal.getCr_time());
        values.put(KEY_LATITUDE, beanproductfinal.getLatitude());
        values.put(KEY_LONGITUDE, beanproductfinal.getLongitude());

        values.put(KEY_ROUTE_CODE, beanproductfinal.getRoute_code());
        values.put(KEY_PARTNER, beanproductfinal.getPartner_type());

        values.put(KEY_SYNC, "NOT");

        // insert row
        db.insert(TABLE_ADHOC_FINAL, null, values);

    }


    @SuppressLint("Range")
    public BeanProduct getBeanProduct(String kunnr, String maktx) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;

//        Customer wise price
        selectQuery = "SELECT  * FROM " + TABLE_ADHOC + " WHERE " + KEY_MAKTX + " = '" + maktx + "'"
                + " AND " + KEY_KUNNR + " = '" + kunnr + "'";

        c = db.rawQuery(selectQuery, null);


        if (c.getCount() <= 0) {
//        Material wise price
            kunnr = "9999999999";
            selectQuery = "SELECT  * FROM " + TABLE_ADHOC + " WHERE " + KEY_MAKTX + " = '" + maktx + "'"
                    + " AND " + KEY_KUNNR + " = '" + kunnr + "'";

            c = db.rawQuery(selectQuery, null);
        }

        BeanProduct beanproduct = new BeanProduct();
        if (c.getCount() > 0) {

            if (c.moveToFirst()) {
                beanproduct.setMatnr(c.getString(c.getColumnIndex(KEY_MATNR)));
                beanproduct.setKunnr(c.getString(c.getColumnIndex(KEY_KUNNR)));
                beanproduct.setVkorg(c.getString(c.getColumnIndex(KEY_VKORG)));
                beanproduct.setVtweg(c.getString(c.getColumnIndex(KEY_VTWEG)));
                beanproduct.setExtwg(c.getString(c.getColumnIndex(KEY_EXTWG)));
                beanproduct.setMaktx(c.getString(c.getColumnIndex(KEY_MAKTX)));
                beanproduct.setKbetr(c.getString(c.getColumnIndex(KEY_KBETR)));
                beanproduct.setKonwa(c.getString(c.getColumnIndex(KEY_KONWA)));
                beanproduct.setMtart(c.getString(c.getColumnIndex(KEY_MTART)));

            }
        }
        return beanproduct;
    }


    @SuppressLint("Range")
    public ArrayList<String> getModel() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  DISTINCT " + KEY_EXTWG + " FROM " + TABLE_ADHOC;

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<String> model = new ArrayList<String>();

        model.add("Select Model");

        if (c.moveToFirst()) {


            while (!c.isAfterLast()) {


                // Log.d("order",""+c.getColumnIndex(KEY_EXTWG));

                model.add(c.getString(c.getColumnIndex(KEY_EXTWG)));
                c.moveToNext();
            }
        }

        return model;
    }


    @SuppressLint("Range")
    public ArrayList<String> getDiscription(String model) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  DISTINCT " + KEY_MAKTX + " FROM " + TABLE_ADHOC + " WHERE " + KEY_EXTWG + " = '" + model + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<String> discription = new ArrayList<String>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                //  Log.d("maktx",c.getString(c.getColumnIndex(KEY_MAKTX)));
                discription.add(c.getString(c.getColumnIndex(KEY_MAKTX)));
                c.moveToNext();
            }
        }
        return discription;
    }


    /******************************* Insert Material Analysis **********************************************/
    public void insertMaterialAnalysis(
            String matnr,
            String maktx,
            String extwg,
            String plant,
            String indicator,
            String delivery_time,
            String kbetr,
            String konwa) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_EXTWG, extwg);
            values.put(KEY_PLANT, plant);
            values.put(KEY_INDICATOR, indicator);
            values.put(KEY_DELIVERY_TIME, delivery_time);
            values.put(KEY_KBETR, kbetr);
            values.put(KEY_KONWA, konwa);


            // Insert Row
            long i = db.insert(TABLE_MATERIAL_ANALYSIS, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert Video gallery  ******************************/

    public void insertVideoGallery(
            String type,
            String name,
            String link
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_VIDEO_TYPE, type);
            values.put(KEY_VIDEO_NAME, name);
            values.put(KEY_VIDEO_LINK, link);


            // Insert Row
            long i = db.insert(TABLE_VIDEO_GALLERY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert chat app group  ******************************/

    public void insertChatAppGroup(
            String pernr,
            String username,
            String password,
            String api,
            String group_id

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;


        //   Log.d("chat_app_inst2",   username +"--" + group_id +"--"+ api)    ;
        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);
            values.put(KEY_API, api);
            values.put(KEY_GROUP_ID, group_id);

            // Log.d("chat_app_inst3",  username +"--" + group_id +"--"+ api)    ;


            // Insert Row
            long i = db.insert(TABLE_CHAT_APP, null, values);


            // Log.d("chat_app_inst",""+i +"" + username +"--" + group_id +"--"+ api)    ;

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertChatAppGroupVK(
            String pernr,
            String username,
            String password,
            String api,
            String group_id

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;


        //   Log.d("chat_app_inst2",   username +"--" + group_id +"--"+ api)    ;
        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);
            values.put(KEY_API, api);
            values.put(KEY_GROUP_ID, group_id);

            // Log.d("chat_app_inst3",  username +"--" + group_id +"--"+ api)    ;


            // Insert Row
            long i = db.insert(TABLE_CHAT_APP_VK, null, values);


            // Log.d("chat_app_inst",""+i +"" + username +"--" + group_id +"--"+ api)    ;

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert customer complaint  ******************************/

    public void insertZCMPLNHDR(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String mob_no1,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit,
            String epc,
            String penday,
            String pen_res,
            String fdate,
            String awaitaprpernr,
            String awaitaprpernrnm,
            String pendaprpernr,
            String pendaprpernrnm,
            String awaitapproval,
            String pendapproval,
            String awaitaprremark,
            String pendaprremark


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_ALT_MOB_NO, mob_no1);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);
            values.put(KEY_EPC, epc);
            values.put(KEY_PEN_DAY, penday);
            values.put(KEY_PEN_RES, pen_res);
            values.put(KEY_FLW_DT, fdate);
            values.put(KEY_AWT_PERNR, awaitaprpernr);
            values.put(KEY_AWT_PERNR_NM, awaitaprpernrnm);
            values.put(KEY_PEND_PERNR, pendaprpernr);
            values.put(KEY_PEND_PERNR_NM, pendaprpernrnm);
            values.put(KEY_AWT_APR, awaitapproval);
            values.put(KEY_PEND_APR, pendapproval);
            values.put(KEY_AWT_APR_RMK, awaitaprremark);
            values.put(KEY_PEND_APR_RMK, pendaprremark);


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDR, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertZCMPLNHDR_VK(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String mob_no1,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit,
            String epc,
            String penday,
            String pen_res,
            String fdate,
            String awaitaprpernr,
            String awaitaprpernrnm,
            String pendaprpernr,
            String pendaprpernrnm,
            String awaitapproval,
            String pendapproval,
            String awaitaprremark,
            String pendaprremark


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_ALT_MOB_NO, mob_no1);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);
            values.put(KEY_EPC, epc);
            values.put(KEY_PEN_DAY, penday);
            values.put(KEY_PEN_RES, pen_res);
            values.put(KEY_FLW_DT, fdate);
            values.put(KEY_AWT_PERNR, awaitaprpernr);
            values.put(KEY_AWT_PERNR_NM, awaitaprpernrnm);
            values.put(KEY_PEND_PERNR, pendaprpernr);
            values.put(KEY_PEND_PERNR_NM, pendaprpernrnm);
            values.put(KEY_AWT_APR, awaitapproval);
            values.put(KEY_PEND_APR, pendapproval);
            values.put(KEY_AWT_APR_RMK, awaitaprremark);
            values.put(KEY_PEND_APR_RMK, pendaprremark);


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDR_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    //////////////////////new by vikas


    public void insertZCMPLNHDRVKFinal(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String mob_no1,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit,
            String epc,
            String penday,
            String pen_res,
            String fdate,
            String awaitaprpernr,
            String awaitaprpernrnm,
            String pendaprpernr,
            String pendaprpernrnm,
            String awaitapproval,
            String pendapproval,
            String awaitaprremark,
            String pendaprremark


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_ALT_MOB_NO, mob_no1);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);
            values.put(KEY_EPC, epc);
            values.put(KEY_PEN_DAY, penday);
            values.put(KEY_PEN_RES, pen_res);
            values.put(KEY_FLW_DT, fdate);
            values.put(KEY_AWT_PERNR, awaitaprpernr);
            values.put(KEY_AWT_PERNR_NM, awaitaprpernrnm);
            values.put(KEY_PEND_PERNR, pendaprpernr);
            values.put(KEY_PEND_PERNR_NM, pendaprpernrnm);
            values.put(KEY_AWT_APR, awaitapproval);
            values.put(KEY_PEND_APR, pendapproval);
            values.put(KEY_AWT_APR_RMK, awaitaprremark);
            values.put(KEY_PEND_APR_RMK, pendaprremark);


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDR_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertSerial_Number_ZCMPLNHDR(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);


            // Insert Row
            long i = db.insert(TABLE_SERAIL_NUMBER_ZCMPLNHDR, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert customer complaint detail  ******************************/

    public void insertZCMPLNDTL(
            String cmpno,
            String posnr,
            String matnr,
            String maktx,
            String reason,
            String warranty,
            String sernr,
            String closer_reason,
            String defect,
            String extwg,
            String payment_by,
            String customer,
            String dealer,
            String company,
            String re_comp,
            String re_del,
            String pay_freelancer,
            String history,
            String billno,
            String billdt,
            String insutxt,
            String warrncond,
            String cmpln_relt_to,
            String wardat


    ) {


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_REASON, reason);
            values.put(KEY_WARRANTY, warranty);
            values.put(KEY_SERNR, sernr);
            values.put(KEY_CLOSER_RESON, closer_reason);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_EXTWG, extwg);
            values.put(KEY_HISTORY, history);
            values.put(KEY_PAY_FREELANCER, pay_freelancer);
            values.put(KEY_BILL_DATE, billdt);
            values.put(KEY_BILL_NO, billno);
            values.put(KEY_INSU_TXT, insutxt);
            values.put(KEY_WARR_COND, warrncond);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_RELT_TO, cmpln_relt_to);


            if (payment_by.equalsIgnoreCase("payment by")) {

                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);

            }

            if (payment_by.equalsIgnoreCase("Return By")) {
                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);
            }


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDTL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    public void insertZCMPLNDTL_VK(
            String cmpno,
            String posnr,
            String matnr,
            String maktx,
            String reason,
            String warranty,
            String sernr,
            String closer_reason,
            String defect,
            String extwg,
            String payment_by,
            String customer,
            String dealer,
            String company,
            String re_comp,
            String re_del,
            String pay_freelancer,
            String history,
            String billno,
            String billdt,
            String insutxt,
            String warrncond,
            String cmpln_relt_to,
            String wardat


    ) {


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_REASON, reason);
            values.put(KEY_WARRANTY, warranty);
            values.put(KEY_SERNR, sernr);
            values.put(KEY_CLOSER_RESON, closer_reason);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_EXTWG, extwg);
            values.put(KEY_HISTORY, history);
            values.put(KEY_PAY_FREELANCER, pay_freelancer);
            values.put(KEY_BILL_DATE, billdt);
            values.put(KEY_BILL_NO, billno);
            values.put(KEY_INSU_TXT, insutxt);
            values.put(KEY_WARR_COND, warrncond);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_RELT_TO, cmpln_relt_to);


            if (payment_by.equalsIgnoreCase("payment by")) {

                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);

            }

            if (payment_by.equalsIgnoreCase("Return By")) {
                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);
            }


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDTL_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertSerial_Number_ZCMPLNDTL(
            String cmpno,
            String posnr,
            String matnr,
            String maktx,
            String reason,
            String warranty,
            String sernr,
            String closer_reason,
            String defect,
            String extwg,
            String payment_by,
            String customer,
            String dealer,
            String company,
            String re_comp,
            String re_del


    ) {


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_REASON, reason);
            values.put(KEY_WARRANTY, warranty);
            values.put(KEY_SERNR, sernr);
            values.put(KEY_CLOSER_RESON, closer_reason);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_EXTWG, extwg);


            if (payment_by.equalsIgnoreCase("payment by")) {

                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
            }

            if (payment_by.equalsIgnoreCase("Return By")) {
                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
            }


            // Insert Row
            long i = db.insert(TABLE_SERAIL_NUMBER_ZCMPLNHDTL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert customer complaint category  ******************************/

    public void insertZCMPLN_CATEGORY(
            String category
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, category);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CATEGORY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }

    public void insertZCMPLN_CATEGORYVK(
            String category
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, category);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CATEGORY_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert customer complaint defect type  ******************************/

    public void insertZCMPLN_DEFECT_TYPE(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_DEFECT, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_DEFECT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    public void insertZCMPLN_DEFECT_TYPEVK(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_DEFECT, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_DEFECT_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }

    /****************************** insert customer complaint related to  ******************************/

    public void insertZCMPLN_RELT_TO(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_RELT_TO, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_RELT_TO, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }

    public void insertZCMPLN_RELT_TOVK(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_RELT_TO, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_RELT_TO_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert customer complaint defect type  ******************************/

    public void insertZCMPLN_CLOSER(
            String extwg,
            String reason

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_EXTWG, extwg);
            values.put(KEY_CLOSER_RESON, reason);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CLOSER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }

    public void insertZCMPLN_CLOSERVK(
            String extwg,
            String reason

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_EXTWG, extwg);
            values.put(KEY_CLOSER_RESON, reason);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CLOSER_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert service center  *****************************************/

    public void insertServiceCenter(
            String pernr,
            String ename,
            String kunnr,
            String customer_name,
            String land,
            String land_txt,
            String regio,
            String state_txt,
            String city,
            String district_txt,
            String taluka,
            String taluka_txt,
            String mobno,
            String telf2,
            String contact_person,
            String address,
            String pincode,
            String email,
            String lat_long


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_LAND1, land);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, regio);
            values.put(KEY_STATE_TXT, state_txt);

            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_DISTRICT_TXT, district_txt);

            values.put(KEY_TALUKA_CODE, taluka);
            values.put(KEY_TALUKA_TXT, taluka_txt);

            values.put(KEY_MOB_NO, mobno);
            values.put(KEY_TEL_NUMBER, telf2);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_EMAIL, email);
            values.put(KEY_LAT_LONG, lat_long);


            // Insert Row
            long i = db.insert(TABLE_SERVICE_CENTER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert country  *****************************************/

    public void insertCountry(
            String land1,
            String land_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            // Insert Row
            long i = db.insert(TABLE_COUNTRY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /****************************** insert state  *****************************************/

    public void insertState(
            String land1,
            String state,
            String state_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_STATE_TXT, state_txt);
            // Insert Row
            long i = db.insert(TABLE_STATE, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert city   *****************************************/

    public void insertCity(
            String land1,
            String state,
            String city,
            String city_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_DISTRICT_TXT, city_txt);
            // Insert Row
            long i = db.insert(TABLE_CITY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert tehsil   *****************************************/

    public void insertTehsil(
            String land1,
            String state,
            String city,
            String tehsil,
            String tehsil_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_TALUKA_CODE, tehsil);
            values.put(KEY_TALUKA_TXT, tehsil_txt);
            // Insert Row
            long i = db.insert(TABLE_TEHSIL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert Complaint inprocess data for offline record **********************************************/

    public void insertComplaintAction(String pernr,
                                      String ename,
                                      String cmpno,
                                      String follow_up_date,
                                      String reason,
                                      String cr_date,
                                      String cr_time,
                                      String status


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);

            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_REASON, reason);
            values.put(KEY_CMPLN_STATUS, status);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_ACTION, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    public void insertComplaintActionVK(String pernr,
                                        String ename,
                                        String cmpno,
                                        String follow_up_date,
                                        String reason,
                                        String cr_date,
                                        String cr_time,
                                        String status


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);

            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_REASON, reason);
            values.put(KEY_CMPLN_STATUS, status);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_ACTION_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert Pending Reason **********************************************/

    public void insertpendingreason(String pen_rea_no,
                                    String name


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PEND_NO, pen_rea_no);
            values.put(KEY_NAME, name);

            // Insert Row
            long i = db.insert(TABLE_PENDING_REASON, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    public void insertpendingreasonVK(String pen_rea_no,
                                      String name


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PEND_NO, pen_rea_no);
            values.put(KEY_NAME, name);

            // Insert Row
            long i = db.insert(TABLE_PENDING_REASON_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert Complaint Image name **********************************************/

    public void insertComplaintImageName(String catgry,
                                         String item,
                                         String name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, catgry);
            values.put(KEY_ITEM, item);
            values.put(KEY_IMAGE, name);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_IMAGE_NAME, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    public void insertComplaintImageNameVK(String catgry,
                                           String item,
                                           String name


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, catgry);
            values.put(KEY_ITEM, item);
            values.put(KEY_IMAGE, name);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_IMAGE_NAME_VK, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData(String endat, String endtm) {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + " = '" + endat + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'";

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }


    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE;

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }


    @SuppressLint("Range")
    public ArrayList<LocalConvenienceBean> getLocalConveyance() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        ArrayList<LocalConvenienceBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {

            String selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + " != '" + "" + "'" + " AND " + KEY_TO_TIME + " != '" + "" + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursor.getString(cursor.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursor.getString(cursor.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursor.getString(cursor.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursor.getString(cursor.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursor.getString(cursor.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursor.getString(cursor.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursor.getString(cursor.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursor.getString(cursor.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursor.getString(cursor.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursor.getString(cursor.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));


                        list_document.add(localConvenienceBean);


                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }


    //********************************* get complaint distaince
    public boolean getTableComplaintDistance(String cmp_no) {

        String selectQuery = null;
        boolean complaint_no = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cmp_distance_cursor = null;


        try {


            selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DISTANCE

                    + " WHERE " + KEY_CMPNO + " = '" + cmp_no + "'";
//


            cmp_distance_cursor = db.rawQuery(selectQuery, null);

//            Log.d("cmp_distance",""+cmp_distance_cursor.getCount());

            if (cmp_distance_cursor.getCount() > 0) {

                while (cmp_distance_cursor.moveToNext()) {

                    complaint_no = true;


                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cmp_distance_cursor != null) {
                cmp_distance_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }

        return complaint_no;
    }


    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;

        String Query = "SELECT * FROM " + tablename + " WHERE " + field + " = '" + fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    cursor.moveToNext();
                }
            }
        }

        cursor.close();
        return true;
    }


    public boolean isEmpty(String TableName) {

        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database, TableName);

        if (NoOfRows == 0) {
            return true;
        } else {
            return false;
        }
    }

}
