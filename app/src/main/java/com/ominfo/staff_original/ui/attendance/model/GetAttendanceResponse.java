
package com.ominfo.staff_original.ui.attendance.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetAttendanceResponse {

    @SerializedName("result")
    private GetAttendanceResult mResult;

    public GetAttendanceResult getResult() {
        return mResult;
    }

    public void setResult(GetAttendanceResult result) {
        mResult = result;
    }

}
