
package com.ominfo.staff_original.ui.upload_pod.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetPdsGcListForPodRequest {

    @SerializedName("PDS_ID")
    private String mPDSID;
    @SerializedName("userkey")
    private String mUserkey;

    public String getPDSID() {
        return mPDSID;
    }

    public void setPDSID(String pDSID) {
        mPDSID = pDSID;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
