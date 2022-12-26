
package com.ominfo.staff_original.ui.login.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UpdateKeyRequest {

    @SerializedName("MobileKey")
    private String mMobileKey;
    @SerializedName("User_ID")
    private String mUserID;
    @SerializedName("userkey")
    private String mUserkey;

    public String getMobileKey() {
        return mMobileKey;
    }

    public void setMobileKey(String mobileKey) {
        mMobileKey = mobileKey;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
