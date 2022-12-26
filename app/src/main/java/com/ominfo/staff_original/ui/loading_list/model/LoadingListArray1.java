
package com.ominfo.staff_original.ui.loading_list.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoadingListArray1 {

    @SerializedName("Branch_ID")
    private String mBranchID;
    @SerializedName("Branch_Name")
    private String mBranchName;
    @SerializedName("Listing_Date")
    private String mListingDate;
    @SerializedName("Listing_ID")
    private String mListingID;
    @SerializedName("Vehicle_ID")
    private String mVehicleID;
    @SerializedName("Vehicle_No")
    private String mVehicleNo;

    public String getBranchID() {
        return mBranchID;
    }

    public void setBranchID(String branchID) {
        mBranchID = branchID;
    }

    public String getBranchName() {
        return mBranchName;
    }

    public void setBranchName(String branchName) {
        mBranchName = branchName;
    }

    public String getListingDate() {
        return mListingDate;
    }

    public void setListingDate(String listingDate) {
        mListingDate = listingDate;
    }

    public String getListingID() {
        return mListingID;
    }

    public void setListingID(String listingID) {
        mListingID = listingID;
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
