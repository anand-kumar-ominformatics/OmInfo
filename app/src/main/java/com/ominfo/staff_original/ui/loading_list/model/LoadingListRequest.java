
package com.ominfo.staff_original.ui.loading_list.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoadingListRequest {

    @SerializedName("Branch_ID")
    private String mBranchID;
    @SerializedName("Listing_Date")
    private String mListingDate;
    @SerializedName("userkey")
    private String mUserkey;
    @SerializedName("Vehicle_ID")
    private String mVehicleID;

    public String getBranchID() {
        return mBranchID;
    }

    public void setBranchID(String branchID) {
        mBranchID = branchID;
    }

    public String getListingDate() {
        return mListingDate;
    }

    public void setListingDate(String listingDate) {
        mListingDate = listingDate;
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
