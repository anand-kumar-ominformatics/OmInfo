package com.ominfo.staff_original.network;

import com.ominfo.staff_original.util.LogUtil;

public class DynamicAPIPath {

    public static final String BASE = "index_with_DRapp_live.php";
    public static final String POST_LOGIN = BASE+"?action=login";
    public static final String POST_FETCH_KATA_CHITTI = BASE+"?action=getKantaChitthi";
    public static final String POST_SAVE_KATA_CHITTI = BASE;
    public static final String POST_ADV_TO_DRIVER = BASE+"?action=getAdvanceToDriverInfo";
    public static final String POST_CONTACTS = BASE+"?action=getcontacts";
    public static final String POST_VEHICLE = BASE+"?action=getAllVehicles";
    public static final String GET_DASHBOARD = "dashboard_screen";
    public static final String RESEND_otp = "resend-otp";
    public static final String profile_update = "profile-update";
    public static final String user_info = "user-info";
    public static final String document_upload = "document-upload";
    public static final String USER_CHARGE = "user-charges";
    public static final String DOCUMENT_UPDATE = "document-update";
    public static final String GET_USER_LIST = "user";


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
