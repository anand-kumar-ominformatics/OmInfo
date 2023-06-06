
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPendingGdsListResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<FetchPendingGdsListResult> mFetchPendingGdsListResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<FetchPendingGdsListResult> getResult() {
        return mFetchPendingGdsListResult;
    }

    public void setResult(List<FetchPendingGdsListResult> fetchPendingGdsListResult) {
        mFetchPendingGdsListResult = fetchPendingGdsListResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
