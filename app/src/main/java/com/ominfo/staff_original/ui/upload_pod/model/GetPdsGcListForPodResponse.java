
package com.ominfo.staff_original.ui.upload_pod.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetPdsGcListForPodResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<GetPdsGcListForPodResult> mGetPdsGcListForPodResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<GetPdsGcListForPodResult> getResult() {
        return mGetPdsGcListForPodResult;
    }

    public void setResult(List<GetPdsGcListForPodResult> getPdsGcListForPodResult) {
        mGetPdsGcListForPodResult = getPdsGcListForPodResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
