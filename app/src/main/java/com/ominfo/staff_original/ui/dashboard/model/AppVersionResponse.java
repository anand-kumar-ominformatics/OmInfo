
package com.ominfo.staff_original.ui.dashboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AppVersionResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<AppVersionResult> mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<AppVersionResult> getResult() {
        return mResult;
    }

    public void setResult(List<AppVersionResult> result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
