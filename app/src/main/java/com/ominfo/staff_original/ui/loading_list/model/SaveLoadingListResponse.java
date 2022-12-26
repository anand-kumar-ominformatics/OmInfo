
package com.ominfo.staff_original.ui.loading_list.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SaveLoadingListResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private SaveLoadingListResult mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public SaveLoadingListResult getResult() {
        return mResult;
    }

    public void setResult(SaveLoadingListResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
