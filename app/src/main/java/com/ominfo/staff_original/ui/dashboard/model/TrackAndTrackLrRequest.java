
package com.ominfo.staff_original.ui.dashboard.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class TrackAndTrackLrRequest {

    @SerializedName("gc_no")
    private String mGcNo;
    @SerializedName("userkey")
    private String mUserkey;

    public String getGcNo() {
        return mGcNo;
    }

    public void setGcNo(String gcNo) {
        mGcNo = gcNo;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
