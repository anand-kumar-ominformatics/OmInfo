
package com.ominfo.staff_original.ui.dashboard.model;

import androidx.room.Ignore;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Alldetail {

    @SerializedName("Advance")
    private String mAdvance;
    @SerializedName("Date")
    private String mDate;
    @SerializedName("Driver_ID")
    private String mDriverID;
    @SerializedName("Driver_Name")
    private String mDriverName;
    @SerializedName("PayableBranch")
    private String mPayableBranch;
    @SerializedName("TripExpenseAprovalID")
    private String mTripExpenseAprovalID;
    @SerializedName("TripExpenseID")
    private String mTripExpenseID;
    @SerializedName("TripNo")
    private String mTripNo;
    @SerializedName("Vehicle_ID")
    private String mVehicleID;
    @SerializedName("Vehicle_No")
    private String mVehicleNo;

    @Ignore
    public Alldetail(String mAdvance, String mDate, String mDriverName, String mVehicleNo) {
        this.mAdvance = mAdvance;
        this.mDate = mDate;
        this.mDriverName = mDriverName;
        this.mVehicleNo = mVehicleNo;
    }

    public String getAdvance() {
        return mAdvance;
    }

    public void setAdvance(String advance) {
        mAdvance = advance;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDriverID() {
        return mDriverID;
    }

    public void setDriverID(String driverID) {
        mDriverID = driverID;
    }

    public String getDriverName() {
        return mDriverName;
    }

    public void setDriverName(String driverName) {
        mDriverName = driverName;
    }

    public String getPayableBranch() {
        return mPayableBranch;
    }

    public void setPayableBranch(String payableBranch) {
        mPayableBranch = payableBranch;
    }

    public String getTripExpenseAprovalID() {
        return mTripExpenseAprovalID;
    }

    public void setTripExpenseAprovalID(String tripExpenseAprovalID) {
        mTripExpenseAprovalID = tripExpenseAprovalID;
    }

    public String getTripExpenseID() {
        return mTripExpenseID;
    }

    public void setTripExpenseID(String tripExpenseID) {
        mTripExpenseID = tripExpenseID;
    }

    public String getTripNo() {
        return mTripNo;
    }

    public void setTripNo(String tripNo) {
        mTripNo = tripNo;
    }

    public String getVehicleID() {
        return mVehicleID;
    }

    public void setVehicleID(String vehicleID) {
        mVehicleID = vehicleID;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        mVehicleNo = vehicleNo;
    }

}
