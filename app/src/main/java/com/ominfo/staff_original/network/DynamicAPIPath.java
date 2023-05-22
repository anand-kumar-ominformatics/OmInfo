package com.ominfo.staff_original.network;

import com.ominfo.staff_original.util.LogUtil;

public class DynamicAPIPath {

    public static final String BASE = "index_with_DRapp_live.php";//"index_with_DRapp_test.php"; //"index_with_DRapp_live.php";

    public static final String POST_LOGIN = BASE+"?action=login";

    public static final String TRACK_AND_TRACE_LR= BASE+"?action=TrackandtraceLR";
    public static final String POD_SAVE_FOR_LR= BASE;//+"?action=podSaveforLR";
    public static final String POD_SAVE_FOR_LR1 = "podSaveforLR";

    public static final String GET_PDS_GC_LIST_FOR_POD= BASE+"?action=getPDSGCListforPOD";

    public static final String GET_PDS_LIST_FOR_POD= BASE+"?action=getPDSListforPOD";

    public static final String POST_UPDATE_KEY = BASE+"?action=updatemobilekey";
    public static final String POST_FETCH_KATA_CHITTI = BASE+"?action=getKantaChitthi";
    public static final String POST_FETCH_LOADING_LIST = BASE+"?action=getLoadingList";
    public static final String POST_SINGLE_USER = BASE+"?action=get_single_employee_list";

    public static final String POST_GET_VERSION= "index_omtrucking.php"+"?action=getAppversion";

    public static final String POST_SAVE_KATA_CHITTI = BASE;
    public static final String POST_CALENDER_HOLIDAY=BASE+"?action=getall_attendance_report";
    public static final String POST_CALENDER_ALL=BASE+"?action=getall_attendance_calendar";
    public static final String POST_HIGHLIGHTS=BASE+"?action=gethighlights_details";
    public static final String POST_SAVE_LOADING_LIST = BASE;
    public static final String POST_GET_VISIT_NO= BASE+"generatevisit_id";
    public static final String POST_ADV_TO_DRIVER = BASE+"?action=getAdvanceToDriverInfo";
    public static final String POST_CONTACTS = BASE+"?action=getcontacts";
    public static final String POST_VEHICLE = BASE+"?action=getAllVehicles";
    public static final String POST_SINLE_EMPLOYEES_LIST = BASE+"?action=get_single_employee_list";
    public static final String GET_DASHBOARD = "dashboard_screen";
    public static final String RESEND_otp = "resend-otp";

    public static final String POST_VEHICLE_NO= BASE+"?action=getVehicles";

    public static final String POST_VEHICLE_DETAILS= BASE+"?action=getVehWiseLRTransaction";

    public static final String profile_update = "profile-update";
    public static final String user_info = "user-info";
    public static final String document_upload = "document-upload";
    public static final String USER_CHARGE = "user-charges";
    public static final String DOCUMENT_UPDATE = "document-update";
    public static final String GET_USER_LIST = "user";
    public static final String action_get_attendance = "get_today_attendance";
    public static final String POST_UPDATE_ATTENDANCE= BASE+"?action=mark_attendance_new";
    public static final String POST_GET_ATTENDANCE = BASE+"?action=getattendance_individual";
    public static final String action_update_attendance = "mark_attendance_new";

    public static final String POST_GET_VEHICLE= BASE+"?action=getVehWiseLRs";


    /**
     * Make dynamic url method
     * there are some of the api is no need to dynamic url but rest of the api are dynamic url
     * This method add country code between the base url and rest path
     **/
    public static String makeDynamicEndpointAPIGateWay(String baseUrl, String path) {
        String finalEndPoint = "";
        String tempBaseURL = "";
        try {
            finalEndPoint =  baseUrl.concat(path);
            LogUtil.printLog("Base URL : ", tempBaseURL);
            LogUtil.printLog("path : ", path);
            LogUtil.printLog(" final end point : ", finalEndPoint);

        } catch (Exception e) {
            e.printStackTrace();
            return finalEndPoint;
        }
        return finalEndPoint;
    }
}
