
package com.ominfo.staff_original.ui.dashboard.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UserRightsRequest {

    @SerializedName("User_Id")
    private String mUserId;
    @SerializedName("userkey")
    private String mUserkey;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
