
package com.ominfo.staff_original.ui.upload_pod.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPendingPodListResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<FetchPendingPodListResult> mFetchPendingPodListResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<FetchPendingPodListResult> getResult() {
        return mFetchPendingPodListResult;
    }

    public void setResult(List<FetchPendingPodListResult> fetchPendingPodListResult) {
        mFetchPendingPodListResult = fetchPendingPodListResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
