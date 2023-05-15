
package com.ominfo.staff_original.ui.dashboard.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class TrackAndTraceLrResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private TrackAndTraceLRResult mTrackAndTraceLRResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public TrackAndTraceLRResult getResult() {
        return mTrackAndTraceLRResult;
    }

    public void setResult(TrackAndTraceLRResult trackAndTraceLRResult) {
        mTrackAndTraceLRResult = trackAndTraceLRResult;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
