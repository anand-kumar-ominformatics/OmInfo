package com.ominfo.staff_original.network;
import com.google.gson.JsonElement;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Service {

    private NetworkAPIServices networkAPIServices;

    public Service(NetworkAPIServices networkAPIServices) {
        this.networkAPIServices = networkAPIServices;
    }
    public Observable<JsonElement> executeGetAppVersionAPI(String request) {
        return networkAPIServices.getAppVersion(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_GET_VERSION),request);
    }
    public Observable<JsonElement> executeLoginAPI(String request) {
        return networkAPIServices.login(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_LOGIN),request);
    }
    public Observable<JsonElement> executeUpdateKeyAPI(String request) {
        return networkAPIServices.updateKey(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_UPDATE_KEY),request);
    }
    public Observable<JsonElement> executeSingleUserAPI(String request) {
        return networkAPIServices.SingleUser(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_SINGLE_USER),request);
    }
    public Observable<JsonElement> executeContactsAPI(String request) {
        return networkAPIServices.contacts(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_CONTACTS),request);
    }
    public Observable<JsonElement> executeGetAttendanceAPI(String markAttendanceRequest) {
        return networkAPIServices.getAttendanceStaff(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL,
                DynamicAPIPath.POST_GET_ATTENDANCE),
                markAttendanceRequest);
    }
    public Observable<JsonElement> executeUpdateAttendanceAPI(RequestBody mRequestBodyType, RequestBody mRequestBodyTypeImage) {
        return networkAPIServices.updateAttendance(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_UPDATE_ATTENDANCE),
                mRequestBodyType,mRequestBodyTypeImage
        );
    }

    public Observable<JsonElement> executeCalenderHolidaysListAPI(RequestBody action,RequestBody cId,RequestBody from
            ,RequestBody to) {
        return networkAPIServices.calenderHolidays(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL,
                DynamicAPIPath.POST_CALENDER_HOLIDAY),
                action,cId,from,to
        );
    }

    public Observable<JsonElement> executeCalenderDetailsAPI(String calender) {
        return networkAPIServices.calenderDetails(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL,
                DynamicAPIPath.POST_CALENDER_HOLIDAY),
                calender
        );
    }

    public Observable<JsonElement> executeCalenderAllAPI(String calender) {
        return networkAPIServices.calenderAll(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL,
                DynamicAPIPath.POST_CALENDER_ALL),
                calender
        );
    }
    public Observable<JsonElement> executeHighlightsAPI(String calender) {
        return networkAPIServices.getHighlights(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL,
                DynamicAPIPath.POST_HIGHLIGHTS),
                calender
        );
    }

    public Observable<JsonElement> executeFetchKataChitthiAPI(String request) {
        return networkAPIServices.fetchKataChitthi(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_FETCH_KATA_CHITTI),request);
    }

    public Observable<JsonElement> executeFetchLoadingListAPI(String request) {
        return networkAPIServices.fetchLoadingList(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_FETCH_LOADING_LIST),request);
    }

    public Observable<JsonElement> executeAdvanceToDriverAPI(String request) {
        return networkAPIServices.advanceToDriver(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_ADV_TO_DRIVER),request);
    }

    public Observable<JsonElement> executeVehicleAPI(String request) {
        return networkAPIServices.getVehicle(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_VEHICLE),request);
    }

    public Observable<JsonElement> executeSaveKataChitthiAPI( RequestBody mRequestBodyType, RequestBody mRequestBodyTypeImage) {
        return networkAPIServices.saveKataChitthi(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_SAVE_KATA_CHITTI),mRequestBodyType,mRequestBodyTypeImage);
    }

    public Observable<JsonElement> executeSaveLoadingListAPI( RequestBody mRequestBodyType, RequestBody mRequestBodyTypeImage) {
        return networkAPIServices.saveLoadingList(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.POST_SAVE_LOADING_LIST),mRequestBodyType,mRequestBodyTypeImage);
    }

    public Observable<JsonElement> executeUserListApi(String mLimit) {
        return networkAPIServices.getBookWithTopic(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.GET_USER_LIST+mLimit));
    }

    public Observable<JsonElement> executeDashboardAPI() {
        return networkAPIServices.dashboard(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.GET_DASHBOARD));
    }

    //dummy apis
    public Observable<JsonElement> executeResendOTPAPI() {
        return networkAPIServices.resendOTP(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.RESEND_otp));
    }

    public Observable<JsonElement> executeFetchProfileInfoAPI() {
        return networkAPIServices.FetchProfileInfo(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.user_info));
    }

    public Observable<JsonElement> executeUpdateProfileInfoAPI(RequestBody mRequestBodyFirstName, RequestBody mRequestBodyDOB, RequestBody mRequestBodyEmail,
                                                               RequestBody mRequestBodyPhone, RequestBody mRequestBodyAddress, RequestBody mRequestBodyCity, RequestBody mRequestBodyState,
                                                               RequestBody mRequestBodyZipCode
            , MultipartBody.Part image) {
        return networkAPIServices.updateProfileInfo(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL
                , DynamicAPIPath.profile_update), mRequestBodyFirstName, mRequestBodyDOB, mRequestBodyEmail, mRequestBodyPhone,
                mRequestBodyAddress, mRequestBodyCity, mRequestBodyState, mRequestBodyZipCode, image);
    }


    public Observable<JsonElement> executeUploadDocToVerification(RequestBody doc_type,MultipartBody.Part front_image,MultipartBody.Part back_image) {
        return networkAPIServices.UploadDocToVerification(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL
                , DynamicAPIPath.document_upload), doc_type,front_image,back_image);
    }

    public Observable<JsonElement> executeUserChargeAPI() {
        return networkAPIServices.FetchUserCharge(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL, DynamicAPIPath.USER_CHARGE));
    }

    public Observable<JsonElement> executeUpdateDocToVerification(RequestBody doc_type,MultipartBody.Part front_image,MultipartBody.Part back_image) {
        return networkAPIServices.UpdateDocToVerification(DynamicAPIPath.makeDynamicEndpointAPIGateWay(NetworkURLs.BASE_URL
                , DynamicAPIPath.DOCUMENT_UPDATE), doc_type,front_image,back_image);
    }



}
