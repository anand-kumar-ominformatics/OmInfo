
package com.ominfo.staff_original.ui.dashboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetUserRightsResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<GetUserRightsResult> mGetUserRightsResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<GetUserRightsResult> getResult() {
        return mGetUserRightsResult;
    }

    public void setResult(List<GetUserRightsResult> getUserRightsResult) {
        mGetUserRightsResult = getUserRightsResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
