
package com.ominfo.staff_original.ui.attendance.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class CalenderAllResult {

    @SerializedName("date")
    private String mDate;

    public CalenderAllResult(String mDate) {
        this.mDate = mDate;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

}
