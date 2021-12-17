
package com.ominfo.staff_original.ui.kata_chithi.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchKataChitthiRequest {

    @SerializedName("Driver_ID")
    private String mDriverID;
    @SerializedName("TransactionDate")
    private String mTransactionDate;
    @SerializedName("userkey")
    private String mUserkey;
    @SerializedName("VehicleID")
    private String mVehicleID;

    public String getDriverID() {
        return mDriverID;
    }

    public void setDriverID(String driverID) {
        mDriverID = driverID;
    }

    public String getTransactionDate() {
        return mTransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        mTransactionDate = transactionDate;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

    public String getVehicleID() {
        return mVehicleID;
    }

    public void setVehicleID(String vehicleID) {
        mVehicleID = vehicleID;
    }

}
