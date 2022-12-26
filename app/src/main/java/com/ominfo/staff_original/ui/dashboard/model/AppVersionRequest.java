
package com.ominfo.staff_original.ui.dashboard.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AppVersionRequest {

    @SerializedName("input_id")
    private String mInputId;
    @SerializedName("userkey")
    private String mUserkey;
    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInputId() {
        return mInputId;
    }

    public void setInputId(String inputId) {
        mInputId = inputId;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
