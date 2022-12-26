
package com.ominfo.staff_original.ui.attendance.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class HighlightResult {

    @SerializedName("Late_count")
    private String mLateCount;
    @SerializedName("Present_count")
    private String mPresentCount;
    @SerializedName("total_days_till_today")
    private Long mTotalDaysTillToday;
    @SerializedName("doj")
    private String doj;

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }
    public String getLateCount() {
        return mLateCount;
    }

    public void setLateCount(String lateCount) {
        mLateCount = lateCount;
    }

    public String getPresentCount() {
        return mPresentCount;
    }

    public void setPresentCount(String presentCount) {
        mPresentCount = presentCount;
    }

    public Long getTotalDaysTillToday() {
        return mTotalDaysTillToday;
    }

    public void setTotalDaysTillToday(Long totalDaysTillToday) {
        mTotalDaysTillToday = totalDaysTillToday;
    }

}
