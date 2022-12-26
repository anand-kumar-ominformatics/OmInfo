
package com.ominfo.staff_original.ui.attendance.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MarkAttendanceResponse {

    @SerializedName("result")
    private MarkAttendanceResult mResult;

    public MarkAttendanceResult getResult() {
        return mResult;
    }

    public void setResult(MarkAttendanceResult result) {
        mResult = result;
    }

}
