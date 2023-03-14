package webservice;

import bean.LoginBean;

/**
 * Created by shakti on 10/6/2016.
 */
public class WebURL {

    public static String Serial_no = "";

    //public static final String SEARCH_PAGE = "https://spdevsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zjson/search.htm";
    public static int IC_PHOTO_CHECK = 0;

// ************ Developments server *************

    /************* production server **************/


    public static String MAIN_USER_ID = "";

    public static final String BASE_URL_VK = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/";



    // public static final String LOGIN_VK_PAGE = BASE_URL_VK +"login.htm";
    public static final String PENDING_COMPLAIN_ALL_LIST_VK_PAGE = BASE_URL_VK + "get_pending_approval_header.htm";
    public static final String IBASE_INFO = BASE_URL_VK + "ibas_serial_no.htm";
    // public static final String PENDING_COMPLAIN_ALL_LIST_VK_PAGE = BASE_URL_VK +"get_header.htm";
    // public static final String PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE = BASE_URL_VK +"get_item.htm";
    public static final String PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE = BASE_URL_VK + "get_pending_approval_item.htm";
    public static final String PENDING_COMPLAIN_ALL_REMARK_VK_PAGE = BASE_URL_VK + "get_reply.htm";
    public static final String PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE = BASE_URL_VK + "save_action.htm";
    public static final String PENDING_COMPLAIN_SAVE_ENG_ACTION_VK_PAGE = BASE_URL_VK + "save_engineer_action.htm";

    public static final String LOGIN_PAGE = BASE_URL_VK + "login.htm";
    //   public static final String APP_VERSION = "https://spprdsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zmapp_sales_emp/app_version.htm";
    public static final String ATTENDANCE_PAGE = BASE_URL_VK + "attendance.htm";
    public static final String TARGET_PAGE = BASE_URL_VK + "sales_target.htm";
    public static final String VISIT_HISTORY_PAGE = BASE_URL_VK + "visit_history.htm";

    public static final String SEARCH_HELP_PAGE = BASE_URL_VK + "search_help.htm";
    public static final String ROUTE_DETAIL = BASE_URL_VK + "get_route_plan.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = BASE_URL_VK + "sync_offline_data_new.htm";

    public static final String ORDER_MATERIAL_DATA = BASE_URL_VK + "material_data.htm";
    public static final String RESEND_PASSWORD_PAGE = BASE_URL_VK + "Reset_Password.htm";
    public static final String IMAGE_DIRECTORY_NAME = "Shakti App Image";

    public static final String MATERIAL_ANALYSIS = BASE_URL_VK + "material_analysis.htm";
    public static final String MATERIAL_STOCK = BASE_URL_VK + "material_stock.htm";
    public static final String CUSTOMER_COMPLAINT = BASE_URL_VK + "get_customer_complaint.htm";
    // public static final String CUSTOMER_COMPLAINT_FINAL = BASE_URL_VK +"get_pending_for_engineer.htm";
    public static final String CUSTOMER_COMPLAINT_FINAL1 = BASE_URL_VK + "get_pending_for_engineer.htm";
    public static final String CUSTOMER_COMPLAINT_SERIAL_NUMBER = BASE_URL_VK + "get_serial_number_complaint.htm";
    public static final String CABLE_SELECTION = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zcable1/cable_selection.htm";
    public static final String SOLAR_CABLE_SELECTION = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zcable/cable_selection.htm";

    public static final String PIPE_SELECTION = "https://www.shaktipumps.com/pipes_selector.php";
    public static final String SERVICE_CENTER_LIST = BASE_URL_VK + "get_service_center.htm";
    public static final String LOCATION_HELP = BASE_URL_VK + "location_data.htm";

    public static final String MOU_BILL = BASE_URL_VK + "mou_tgt_ach.htm";
    public static final String FORWARD_TO = BASE_URL_VK + "forward_to.htm";
    public static final String COMPLAINT_ATTACHMENT = BASE_URL_VK + "attach_cmpln_image.htm";

    public static final String REVIEW_COMPLAINT = BASE_URL_VK + "get_pending_cmp_review.htm";
    public static final String SAVE_REVIEW_COMPLAINT = BASE_URL_VK + "save_cmp_review.htm";
    public static final String IMAGES_REVIEW_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_complain_attachment.htm";
    public static final String NORMS_MATERIAL_LIST = BASE_URL_VK + "material_norms_stock.htm";
    public static final String NORM_DATA_ENTRY = BASE_URL_VK + "norms_data_entry.htm";

    static String str = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/file_folder.htm" + "?id=" + LoginBean.userid;
    public static final String FILES_AND_FOLDER = str;
    static String dashboard_url = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zapp_page/registration.htm" + "?=" + LoginBean.userid;

    public static final String DASHBOARD = dashboard_url;
    public static final String LOCAL_CONVENIENVCE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zhr_emp_app_1/start_end_location.htm";
    public static final String DEVICE_DETAILS = "https://solar10.shaktisolarrms.com/Home/SAPOnlineDeviceDetails";
    // public static final String DISTANCE_MAP_CAL_API = "https://apis.mapmyindia.com/advancedmaps/v1/your_rest_key_here/route_adv/driving/";
    public static final String DISTANCE_MAP_CAL_API = "https://apis.mapmyindia.com/advancedmaps/v1/f7f6d1d01b27bba264f18d7dd63a980a/route_adv/driving/";

    public static final String GOODS_RECP = BASE_URL_VK + "service_grn.htm";
    public static final String STOCK_ISSUE = BASE_URL_VK + "service_stock_issue.htm";
    public static final String STOCK_RECEIVER = BASE_URL_VK + "service_stock_receiver.htm";
    public static final String TRANS_ENTRY = BASE_URL_VK + "service_transport_entry.htm";

}
