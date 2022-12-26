package com.ominfo.staff_original.interfaces;

import android.os.Build;

public interface Constants {
    /*date  format*/
    public String DATE_FORMAT_YYYY_MM_DD = "dd MMM yyyy";
    public String DATE_FORMAT_dd_MM_yyyy = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public String DATE_FORMAT_dd_mm_yyyy_ = "dd MMM yyyy";
    String SCANNER  = "/ScannerImages";
    String CAMERA  = "/CameraImages";
    String OTP = "otp";
    int VERSION = 14;
    String RESEND_OTP = "resend_otp";
    String UPDATE_PROFILE_INFO = "update_profile_info";
    String FILE_NAME = "OmTuranth_Staff";
    int SPLAHS_TIME_OUT = 1000;
    String ANDROID = "Android";
    String DEVICE_OS_VALUE = Build.VERSION.RELEASE;
    String TABLET = "Tablet";
    String PHONE = "Phone";
    //For decrypt Token
    String SECRET_KEY = "TheBestSecretKey";
    String INTERNAL_FOLDER_NAME = "send_bits";
    int DEFAULT_IMAGE_SIZE = 240;
    String FROM_TAB_CHANGE = "from_tab_change";
    String FROM_SCREEN = "FROM_SCREEN";
    String COMPLAINTS = "complaints";
    String OKAY = "OKAY";
    String ACCEPTED = "ACCEPTED";
    String TITLE = "title";
    String MESSAGE = "MESSAGE";
    int LOADER_TIMEOUT = 32000;
    String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    String CHANGE_PIN = "CHANGE_PIN";
    String KEY_OBJECT = "KEY_Object";

    /*SCREEEN NAME */
    String DASHBOARD = "DASHBOARD";
    String LOGIN = "LOGIN";
    String REGISTRATION = "REGISTRATION";
    String SETTING = "Setting";
    String MY_PROFILE = "MY_PROFILE";
    String KEY_TITLE = "title";
    String KEY_FROM_SCREEN = "from_screen";
    String CREATE_PIN = "create_pin";
    String PAYMENT_INFO = "payment_info";
    String SCANNED_RESULT = "scannedResult";

    public static final float APP_BAR_ELEVATION = 7;

    String CreatePIN = "create_pin";
    String REGISTRATION_MY_PROFILE = "register_myProfile";
    String TOPIC = "topic";
    String USER_ID = "USER_ID";
    String URL = "url";
    String FETCH_PROFILE_INFO = "fetch_profile_info";
    String FETCH_HISTORY_INFO = "fetch_history_info";
    String FETCH_HISTORY_INFO_More = "fetch_history_info_More";
    String UPLOAD_DOCUMENT = "upload_document";
    String UPDATE_DOCUMENT = "update_document";
    String FETCH_USER_CHARGE = "fetch_charge";
    String UPDATE_CARD = "update_card";
    String BANK_LIST = "bank_list";
    String HISTORY_ITEM = "history_item";
    String CALL = "call";
    String SEND_TEXT = "send_text";
    int  IS_FRONT_IMAGE = 0;
    int  IS_BACK_IMAGE = 1;
    String FORGOT_PASSWORD = "forgot_password";
    String CONFIRM_PIN = "confirm_pin";
    String SEND_MONEY = "send_money";
    long INTERVAL = 4000; //5*60*1000;
    long FASTEST_INTERVAL = 2000;//5*60*1000;
    long INTERVAL_ATTENDENCE = 4000; //2*60*1000;//60 * 1 * 1000; //60 sec
    long FASTEST_INTERVAL_ATTENDENCE = 2000;//30 * 1 * 1000; //30 sec

}
