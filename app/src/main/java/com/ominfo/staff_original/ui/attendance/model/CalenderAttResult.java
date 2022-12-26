
package com.ominfo.staff_original.ui.attendance.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class CalenderAttResult {

    @SerializedName("date")
    private String mDate;
    @SerializedName("end_time")
    private String mEndTime;
    @SerializedName("office_end_addr")
    private String mOfficeEndAddr;
    @SerializedName("office_start_addr")
    private String mOfficeStartAddr;
    @SerializedName("start_time")
    private String mStartTime;
    @SerializedName("Status")
    private String mStatus;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public String getOfficeEndAddr() {
        return mOfficeEndAddr;
    }

    public void setOfficeEndAddr(String officeEndAddr) {
        mOfficeEndAddr = officeEndAddr;
    }

    public String getOfficeStartAddr() {
        return mOfficeStartAddr;
    }

    public void setOfficeStartAddr(String officeStartAddr) {
        mOfficeStartAddr = officeStartAddr;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
