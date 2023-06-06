
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UploadPodResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private UploadGdsResponseResult mUploadPodResponseResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public UploadGdsResponseResult getResult() {
        return mUploadPodResponseResult;
    }

    public void setResult(UploadGdsResponseResult uploadPodResponseResult) {
        mUploadPodResponseResult = uploadPodResponseResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
