
package com.ominfo.staff_original.ui.kata_chithi.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SaveKataChitthiResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private SaveKataChitthiResult mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public SaveKataChitthiResult getResult() {
        return mResult;
    }

    public void setResult(SaveKataChitthiResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
