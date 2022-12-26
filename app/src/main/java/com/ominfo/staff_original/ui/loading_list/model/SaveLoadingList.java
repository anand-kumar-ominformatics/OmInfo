
package com.ominfo.staff_original.ui.loading_list.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SaveLoadingList {

    @SerializedName("Branch_ID")
    private String mBranchID;
    @SerializedName("Listing_Date")
    private String mListingDate;
    @SerializedName("Listing_ID")
    private Long mListingID;
    @SerializedName("Photo_Xml")
    private List<String> mPhotoXml;
    @SerializedName("User_ID")
    private Long mUserID;
    @SerializedName("userkey")
    private String mUserkey;
    @SerializedName("Vehicle_ID")
    private Long mVehicleID;

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

    public Long getListingID() {
        return mListingID;
    }

    public void setListingID(Long listingID) {
        mListingID = listingID;
    }

    public List<String> getPhotoXml() {
        return mPhotoXml;
    }

    public void setPhotoXml(List<String> photoXml) {
        mPhotoXml = photoXml;
    }

    public Long getUserID() {
        return mUserID;
    }

    public void setUserID(Long userID) {
        mUserID = userID;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

    public Long getVehicleID() {
        return mVehicleID;
    }

    public void setVehicleID(Long vehicleID) {
        mVehicleID = vehicleID;
    }

}
