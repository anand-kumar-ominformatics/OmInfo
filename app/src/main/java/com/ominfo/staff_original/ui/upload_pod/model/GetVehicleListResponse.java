
package com.ominfo.staff_original.ui.upload_pod.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetVehicleListResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<GetVehicleListResult> mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<GetVehicleListResult> getResult() {
        return mResult;
    }

    public void setResult(List<GetVehicleListResult> result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
