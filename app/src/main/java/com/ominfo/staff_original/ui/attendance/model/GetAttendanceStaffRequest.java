
package com.ominfo.staff_original.ui.attendance.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetAttendanceStaffRequest {

    @SerializedName("date")
    private String mDate;
    @SerializedName("User_ID")
    private String mUserID;
    @SerializedName("userkey")
    private String mUserkey;
    @SerializedName("is_allowed")
    private String is_allowed;

    public String getIs_allowed() {
        return is_allowed;
    }

    public void setIs_allowed(String is_allowed) {
        this.is_allowed = is_allowed;
    }
    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

}
