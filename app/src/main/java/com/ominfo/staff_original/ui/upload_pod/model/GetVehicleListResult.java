
package com.ominfo.staff_original.ui.upload_pod.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "vehicle_List")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetVehicleListResult {

    @ColumnInfo(name = "No_Of_LR")
    @Expose
    @SerializedName("No_Of_LR")
    private String mNoOfLR;

    @ColumnInfo(name = "Lr_no")
    @Expose
    @SerializedName("Lr_no")
    private String Lr_no;

    @ColumnInfo(name = "Plant")
    @Expose
    @SerializedName("Plant")
    private String mPlant;

    @ColumnInfo(name = "Transaction_Date")
    @Expose
    @SerializedName("Transaction_Date")
    private String mTransactionDate;

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "Transaction_ID")
    @Expose
    @SerializedName("Transaction_ID")
    private String mTransactionID;

    @ColumnInfo(name = "Vehicle_No")
    @Expose
    @SerializedName("Vehicle_No")
    private String mVehicleNo;

    public GetVehicleListResult(String mNoOfLR, String mPlant, String mTransactionDate, String mTransactionID, String mVehicleNo) {
        this.mNoOfLR = mNoOfLR;
        this.mPlant = mPlant;
        this.mTransactionDate = mTransactionDate;
        this.mTransactionID = mTransactionID;
        this.mVehicleNo = mVehicleNo;
    }

    public String getLr_no() {
        return Lr_no;
    }

    public void setLr_no(String lr_no) {
        Lr_no = lr_no;
    }

    public GetVehicleListResult() {
    }

    public String getNoOfLR() {
        return mNoOfLR;
    }

    public void setNoOfLR(String noOfLR) {
        mNoOfLR = noOfLR;
    }

    public String getPlant() {
        return mPlant;
    }

    public void setPlant(String plant) {
        mPlant = plant;
    }

    public String getTransactionDate() {
        return mTransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        mTransactionDate = transactionDate;
    }

    public String getTransactionID() {
        return mTransactionID;
    }

    public void setTransactionID(String transactionID) {
        mTransactionID = transactionID;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        mVehicleNo = vehicleNo;
    }

}
