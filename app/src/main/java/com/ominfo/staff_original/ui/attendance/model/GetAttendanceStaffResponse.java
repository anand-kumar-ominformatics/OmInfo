
package com.ominfo.staff_original.ui.attendance.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetAttendanceStaffResponse {

    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private GetAttendanceStaffResult mResult;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public GetAttendanceStaffResult getResult() {
        return mResult;
    }

    public void setResult(GetAttendanceStaffResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
