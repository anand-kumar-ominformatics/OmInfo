
package com.ominfo.staff_original.ui.attendance.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LocationPerHourResponse {

    @SerializedName("result")
    private LocationPerHourResult mResult;

    public LocationPerHourResult getResult() {
        return mResult;
    }

    public void setResult(LocationPerHourResult result) {
        mResult = result;
    }

}
