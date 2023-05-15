
package com.ominfo.staff_original.ui.upload_pod.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UploadPodResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private UploadPodResponseResult mUploadPodResponseResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public UploadPodResponseResult getResult() {
        return mUploadPodResponseResult;
    }

    public void setResult(UploadPodResponseResult uploadPodResponseResult) {
        mUploadPodResponseResult = uploadPodResponseResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
