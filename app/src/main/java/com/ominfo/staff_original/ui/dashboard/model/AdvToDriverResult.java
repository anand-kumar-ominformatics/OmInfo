
package com.ominfo.staff_original.ui.dashboard.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AdvToDriverResult {

    @SerializedName("alldetails")
    private List<Alldetail> mAlldetails;
    @SerializedName("summary")
    private List<Summary> mSummary;

    public List<Alldetail> getAlldetails() {
        return mAlldetails;
    }

    public void setAlldetails(List<Alldetail> alldetails) {
        mAlldetails = alldetails;
    }

    public List<Summary> getSummary() {
        return mSummary;
    }

    public void setSummary(List<Summary> summary) {
        mSummary = summary;
    }

}
