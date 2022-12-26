
package com.ominfo.staff_original.ui.attendance.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UpdateAttendanceResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private List<UpdateAttendanceResult> mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<UpdateAttendanceResult> getResult() {
        return mResult;
    }

    public void setResult(List<UpdateAttendanceResult> result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
