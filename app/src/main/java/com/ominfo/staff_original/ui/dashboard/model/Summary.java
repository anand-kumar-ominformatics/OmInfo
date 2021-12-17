
package com.ominfo.staff_original.ui.dashboard.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Summary {

    @SerializedName("NoOfTrips")
    private String mNoOfTrips;
    @SerializedName("TotalAdvance")
    private String mTotalAdvance;

    public String getNoOfTrips() {
        return mNoOfTrips;
    }

    public void setNoOfTrips(String noOfTrips) {
        mNoOfTrips = noOfTrips;
    }

    public String getTotalAdvance() {
        return mTotalAdvance;
    }

    public void setTotalAdvance(String totalAdvance) {
        mTotalAdvance = totalAdvance;
    }

}
