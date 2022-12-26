
package com.ominfo.staff_original.ui.attendance.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UpdateAttendanceResult {

    @SerializedName("messagel")
    private String mMessagel;
    @SerializedName("status")
    private String mStatus;

    public String getMessagel() {
        return mMessagel;
    }

    public void setMessagel(String messagel) {
        mMessagel = messagel;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
