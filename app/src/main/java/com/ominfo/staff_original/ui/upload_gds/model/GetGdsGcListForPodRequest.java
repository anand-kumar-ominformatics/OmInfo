
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetGdsGcListForPodRequest {
    @SerializedName("DDC_ID")
    private String ddCID;
    @SerializedName("userkey")
    private String mUserkey;

    public String getDdCID() {
        return ddCID;
    }

    public void setDdCID(String ddCID) {
        this.ddCID = ddCID;
    }

    public String getmUserkey() {
        return mUserkey;
    }

    public void setmUserkey(String mUserkey) {
        this.mUserkey = mUserkey;
    }

}
