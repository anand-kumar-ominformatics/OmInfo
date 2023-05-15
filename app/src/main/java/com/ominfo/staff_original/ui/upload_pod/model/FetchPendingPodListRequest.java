
package com.ominfo.staff_original.ui.upload_pod.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPendingPodListRequest {

    @SerializedName("Branch_ID")
    private String mBranchID;
    @SerializedName("Date")
    private String mDate;
    @SerializedName("userkey")
    private String mUserkey;

    public String getBranchID() {
        return mBranchID;
    }

    public void setBranchID(String branchID) {
        mBranchID = branchID;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
