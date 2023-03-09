package webservice;

import bean.LoginBean;

/**
 * Created by shakti on 10/6/2016.
 */
public class WebURL {

    public static  String Serial_no ="";

    //public static final String SEARCH_PAGE = "https://spdevsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zjson/search.htm";
    public static  int IC_PHOTO_CHECK = 0;

// ************ Developments server *************
   
    /************* production server **************/




    public static  String MAIN_USER_ID= "";

    public static final String BASE_URL_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/";
    public static final String BASE_URL_VK_SALES= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/";


   // public static final String LOGIN_VK_PAGE = BASE_URL_VK +"login.htm";
    public static final String PENDING_COMPLAIN_ALL_LIST_VK_PAGE = BASE_URL_VK +"get_pending_approval_header.htm";
    public static final String IBASE_INFO = BASE_URL_VK_SALES +"ibas_serial_no.htm";
    // public static final String PENDING_COMPLAIN_ALL_LIST_VK_PAGE = BASE_URL_VK +"get_header.htm";
    // public static final String PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE = BASE_URL_VK +"get_item.htm";
    public static final String PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE = BASE_URL_VK +"get_pending_approval_item.htm";
    public static final String PENDING_COMPLAIN_ALL_REMARK_VK_PAGE = BASE_URL_VK +"get_reply.htm";
    public static final String PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE = BASE_URL_VK +"save_action.htm";
    public static final String PENDING_COMPLAIN_SAVE_ENG_ACTION_VK_PAGE = BASE_URL_VK +"save_engineer_action.htm";



    public static final String LOGIN_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/login.htm";
 //   public static final String APP_VERSION = "https://spprdsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zmapp_sales_emp/app_version.htm";
    public static final String ATTENDANCE_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/attendance.htm";
    public static final String TARGET_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/sales_target.htm";
    public static final String VISIT_HISTORY_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/visit_history.htm";

    public static final String SEARCH_HELP_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/search_help.htm";
    public static final String ROUTE_DETAIL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_route_plan.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/sync_offline_data_new.htm";

    public static final String ORDER_MATERIAL_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_data.htm";
    public static final String RESEND_PASSWORD_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/Reset_Password.htm";
    public static final String IMAGE_DIRECTORY_NAME = "Shakti App Image";

    public static final String MATERIAL_ANALYSIS = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_analysis.htm";
    public static final String MATERIAL_STOCK = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_stock.htm";
    public static final String CUSTOMER_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_customer_complaint.htm";
   // public static final String CUSTOMER_COMPLAINT_FINAL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_pending_for_engineer.htm";
    public static final String CUSTOMER_COMPLAINT_FINAL1 = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_pending_for_engineer.htm";
    public static final String CUSTOMER_COMPLAINT_SERIAL_NUMBER = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_serial_number_complaint.htm";
    public static final String CABLE_SELECTION = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zcable1/cable_selection.htm";
    public static final String SOLAR_CABLE_SELECTION = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zcable/cable_selection.htm";

    public static final String PIPE_SELECTION = "https://www.shaktipumps.com/pipes_selector.php";
    public static final String SERVICE_CENTER_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_service_center.htm";
    public static final String LOCATION_HELP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/location_data.htm";

    public static final String MOU_BILL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/mou_tgt_ach.htm";
    public static final String FORWARD_TO = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/forward_to.htm";
    public static final String COMPLAINT_ATTACHMENT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/attach_cmpln_image.htm";

    public static final String REVIEW_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_pending_cmp_review.htm";
    public static final String SAVE_REVIEW_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/save_cmp_review.htm";
    public static final String IMAGES_REVIEW_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_cmp_attachment.htm";
    public static final String NORMS_MATERIAL_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_norms_stock.htm";
    public static final String NORM_DATA_ENTRY = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/norms_data_entry.htm";

    static String str = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/file_folder.htm" + "?id=" + LoginBean.userid;
    public static final String FILES_AND_FOLDER = str;
    static String dashboard_url = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zapp_page/registration.htm" + "?=" + LoginBean.userid;

    public static final String DASHBOARD = dashboard_url;
    public static final String LOCAL_CONVENIENVCE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zhr_emp_app_1/start_end_location.htm";
    public static final String DEVICE_DETAILS = "https://solar10.shaktisolarrms.com/Home/SAPOnlineDeviceDetails" ;
   // public static final String DISTANCE_MAP_CAL_API = "https://apis.mapmyindia.com/advancedmaps/v1/your_rest_key_here/route_adv/driving/";
    public static final String DISTANCE_MAP_CAL_API = "https://apis.mapmyindia.com/advancedmaps/v1/f7f6d1d01b27bba264f18d7dd63a980a/route_adv/driving/";

     public static final String GOODS_RECP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/service_grn.htm";
    public static final String STOCK_ISSUE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/service_stock_issue.htm";
    public static final String STOCK_RECEIVER = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/service_stock_receiver.htm";
    public static final String TRANS_ENTRY = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/service_transport_entry.htm";
    public static final String CMPLN_ATTACHMENT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_cmp_attachment.htm";
}
