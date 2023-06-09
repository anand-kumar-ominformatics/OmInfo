
package com.ominfo.staff_original.ui.login.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_login")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoginResultTable {

    @ColumnInfo(name = "AllowBooking")
    @Expose
    @SerializedName("AllowBooking")
    private String mAllowBooking;

    @ColumnInfo(name = "Bkg_End_Time")
    @Expose
    @SerializedName("Bkg_End_Time")
    private String mBkgEndTime;

    @ColumnInfo(name = "Bkg_Start_Time")
    @Expose
    @SerializedName("Bkg_Start_Time")
    private String mBkgStartTime;

    @ColumnInfo(name = "Company_Name")
    @Expose
    @SerializedName("Company_Name")
    private String mCompanyName;

    @ColumnInfo(name = "Division_Id")
    @Expose
    @SerializedName("Division_Id")
    private String mDivisionId;

    @ColumnInfo(name = "Division_Name")
    @Expose
    @SerializedName("Division_Name")
    private String mDivisionName;

    @ColumnInfo(name = "Driver_Id")
    @Expose
    @SerializedName("Driver_Id")
    private String mDriverId;

    @ColumnInfo(name = "End_Date")
    @Expose
    @SerializedName("End_Date")
    private String mEndDate;

    @ColumnInfo(name = "First_Name")
    @Expose
    @SerializedName("First_Name")
    private String mFirstName;

    @ColumnInfo(name = "GC_Caption")
    @Expose
    @SerializedName("GC_Caption")
    private String mGCCaption;

    @ColumnInfo(name = "Hierarchy_Code")
    @Expose
    @SerializedName("Hierarchy_Code")
    private String mHierarchyCode;

    @ColumnInfo(name = "Is_Account_Transfer_Required")
    @Expose
    @SerializedName("Is_Account_Transfer_Required")
    private String mIsAccountTransferRequired;

    @ColumnInfo(name = "Is_Activate_Divisions")
    @Expose
    @SerializedName("Is_Activate_Divisions")
    private String mIsActivateDivisions;

    @ColumnInfo(name = "Is_CSA")
    @Expose
    @SerializedName("Is_CSA")
    private String mIsCSA;

    @ColumnInfo(name = "Is_Co_Loader_Business")
    @Expose
    @SerializedName("Is_Co_Loader_Business")
    private String mIsCoLoaderBusiness;

    @ColumnInfo(name = "IsEmpDivValid")
    @Expose
    @SerializedName("IsEmpDivValid")
    private String mIsEmpDivValid;

    @ColumnInfo(name = "Is_LHPO_Series_Required")
    @Expose
    @SerializedName("Is_LHPO_Series_Required")
    private String mIsLHPOSeriesRequired;

    @ColumnInfo(name = "Is_Mac_Id_Found")
    @Expose
    @SerializedName("Is_Mac_Id_Found")
    private String mIsMacIdFound;

    @ColumnInfo(name = "Is_Memo_Series_Required")
    @Expose
    @SerializedName("Is_Memo_Series_Required")
    private String mIsMemoSeriesRequired;

    @ColumnInfo(name = "Is_User_Active")
    @Expose
    @SerializedName("Is_User_Active")
    private String mIsUserActive;

    @ColumnInfo(name = "Is_Valid_User")
    @Expose
    @SerializedName("Is_Valid_User")
    private String mIsValidUser;

    @ColumnInfo(name = "LHPO_Caption")
    @Expose
    @SerializedName("LHPO_Caption")
    private String mLHPOCaption;

    @ColumnInfo(name = "Last_Name")
    @Expose
    @SerializedName("Last_Name")
    private String mLastName;

    @ColumnInfo(name = "LoginTime")
    @Expose
    @SerializedName("LoginTime")
    private String mLoginTime;

    @ColumnInfo(name = "Main_Id")
    @Expose
    @SerializedName("Main_Id")
    private String mMainId;

    @ColumnInfo(name = "Main_Name")
    @Expose
    @SerializedName("Main_Name")
    private String mMainName;

    @ColumnInfo(name = "Profile_Id")
    @Expose
    @SerializedName("Profile_Id")
    private String mProfileId;

    @ColumnInfo(name = "Standard_Basic_Freight_Unit_ID")
    @Expose
    @SerializedName("Standard_Basic_Freight_Unit_ID")
    private String mStandardBasicFreightUnitID;

    @ColumnInfo(name = "Standard_Freight_Rate_Per")
    @Expose
    @SerializedName("Standard_Freight_Rate_Per")
    private String mStandardFreightRatePer;

    @ColumnInfo(name = "Start_Date")
    @Expose
    @SerializedName("Start_Date")
    private String mStartDate;

    @ColumnInfo(name = "TodaysDate")
    @Expose
    @SerializedName("TodaysDate")
    private String mTodaysDate;

    @PrimaryKey
    @ColumnInfo(name = "User_ID")
    @Expose
    @NonNull
    @SerializedName("User_ID")
    private String mUserID;

    @ColumnInfo(name = "User_Key")
    @Expose
    @SerializedName("User_Key")
    private String mUserKey;

    @ColumnInfo(name = "User_Name")
    @Expose
    @SerializedName("User_Name")
    private String mUserName;

    @ColumnInfo(name = "Year_Code")
    @Expose
    @SerializedName("Year_Code")
    private String mYearCode;

    @ColumnInfo(name = "0")
    @Expose
    @SerializedName("0")
    private CompanyIdData Company_Id;

    public CompanyIdData getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(CompanyIdData company_Id) {
        Company_Id = company_Id;
    }

    public String getAllowBooking() {
        return mAllowBooking;
    }

    public void setAllowBooking(String allowBooking) {
        mAllowBooking = allowBooking;
    }

    public String getBkgEndTime() {
        return mBkgEndTime;
    }

    public void setBkgEndTime(String bkgEndTime) {
        mBkgEndTime = bkgEndTime;
    }

    public String getBkgStartTime() {
        return mBkgStartTime;
    }

    public void setBkgStartTime(String bkgStartTime) {
        mBkgStartTime = bkgStartTime;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public String getDivisionId() {
        return mDivisionId;
    }

    public void setDivisionId(String divisionId) {
        mDivisionId = divisionId;
    }

    public String getDivisionName() {
        return mDivisionName;
    }

    public void setDivisionName(String divisionName) {
        mDivisionName = divisionName;
    }

    public String getDriverId() {
        return mDriverId;
    }

    public void setDriverId(String driverId) {
        mDriverId = driverId;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getGCCaption() {
        return mGCCaption;
    }

    public void setGCCaption(String gCCaption) {
        mGCCaption = gCCaption;
    }

    public String getHierarchyCode() {
        return mHierarchyCode;
    }

    public void setHierarchyCode(String hierarchyCode) {
        mHierarchyCode = hierarchyCode;
    }

    public String getIsAccountTransferRequired() {
        return mIsAccountTransferRequired;
    }

    public void setIsAccountTransferRequired(String isAccountTransferRequired) {
        mIsAccountTransferRequired = isAccountTransferRequired;
    }

    public String getIsActivateDivisions() {
        return mIsActivateDivisions;
    }

    public void setIsActivateDivisions(String isActivateDivisions) {
        mIsActivateDivisions = isActivateDivisions;
    }

    public String getIsCSA() {
        return mIsCSA;
    }

    public void setIsCSA(String isCSA) {
        mIsCSA = isCSA;
    }

    public String getIsCoLoaderBusiness() {
        return mIsCoLoaderBusiness;
    }

    public void setIsCoLoaderBusiness(String isCoLoaderBusiness) {
        mIsCoLoaderBusiness = isCoLoaderBusiness;
    }

    public String getIsEmpDivValid() {
        return mIsEmpDivValid;
    }

    public void setIsEmpDivValid(String isEmpDivValid) {
        mIsEmpDivValid = isEmpDivValid;
    }

    public String getIsLHPOSeriesRequired() {
        return mIsLHPOSeriesRequired;
    }

    public void setIsLHPOSeriesRequired(String isLHPOSeriesRequired) {
        mIsLHPOSeriesRequired = isLHPOSeriesRequired;
    }

    public String getIsMacIdFound() {
        return mIsMacIdFound;
    }

    public void setIsMacIdFound(String isMacIdFound) {
        mIsMacIdFound = isMacIdFound;
    }

    public String getIsMemoSeriesRequired() {
        return mIsMemoSeriesRequired;
    }

    public void setIsMemoSeriesRequired(String isMemoSeriesRequired) {
        mIsMemoSeriesRequired = isMemoSeriesRequired;
    }

    public String getIsUserActive() {
        return mIsUserActive;
    }

    public void setIsUserActive(String isUserActive) {
        mIsUserActive = isUserActive;
    }

    public String getIsValidUser() {
        return mIsValidUser;
    }

    public void setIsValidUser(String isValidUser) {
        mIsValidUser = isValidUser;
    }

    public String getLHPOCaption() {
        return mLHPOCaption;
    }

    public void setLHPOCaption(String lHPOCaption) {
        mLHPOCaption = lHPOCaption;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getLoginTime() {
        return mLoginTime;
    }

    public void setLoginTime(String loginTime) {
        mLoginTime = loginTime;
    }

    public String getMainId() {
        return mMainId;
    }

    public void setMainId(String mainId) {
        mMainId = mainId;
    }

    public String getMainName() {
        return mMainName;
    }

    public void setMainName(String mainName) {
        mMainName = mainName;
    }

    public String getProfileId() {
        return mProfileId;
    }

    public void setProfileId(String profileId) {
        mProfileId = profileId;
    }

    public String getStandardBasicFreightUnitID() {
        return mStandardBasicFreightUnitID;
    }

    public void setStandardBasicFreightUnitID(String standardBasicFreightUnitID) {
        mStandardBasicFreightUnitID = standardBasicFreightUnitID;
    }

    public String getStandardFreightRatePer() {
        return mStandardFreightRatePer;
    }

    public void setStandardFreightRatePer(String standardFreightRatePer) {
        mStandardFreightRatePer = standardFreightRatePer;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getTodaysDate() {
        return mTodaysDate;
    }

    public void setTodaysDate(String todaysDate) {
        mTodaysDate = todaysDate;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getUserKey() {
        return mUserKey;
    }

    public void setUserKey(String userKey) {
        mUserKey = userKey;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getYearCode() {
        return mYearCode;
    }

    public void setYearCode(String yearCode) {
        mYearCode = yearCode;
    }

}
