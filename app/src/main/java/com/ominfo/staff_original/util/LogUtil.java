package com.ominfo.staff_original.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogUtil {

    public static boolean isEnableLogs = true;

    public static void printLog(String tag, Object object) {
        if (isEnableLogs && object!=null) {
//            String json = new Gson().toJson(object);
            Log.d(tag, "" + object);
        }

    }

    public static void printLog(String tag, String object) {
        if (isEnableLogs && object!=null) {
            Log.d(tag, "" + object);
        }
    }

    public static void printLog(String tag, String object, Throwable tr) {
        if (isEnableLogs && object!=null) {
            LogUtil.printLog(tag, "" + object);
        }
    }

    public static void printToastMSG(Context mContext, String object) {
        if (isEnableLogs && object!=null) {
            Toast.makeText(mContext,object, Toast.LENGTH_SHORT).show();

        }
    }
}
