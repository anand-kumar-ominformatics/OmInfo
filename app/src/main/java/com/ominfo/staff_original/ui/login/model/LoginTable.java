
package com.ominfo.staff_original.ui.login.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "login_data")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoginTable {

    @ColumnInfo(name = "base_url")
    @Expose
    @SerializedName("base_url")
    private String mBaseUrl;

    @ColumnInfo(name = "token")
    @Expose
    @SerializedName("token")
    private String token;

    @ColumnInfo(name = "isadmin")
    @Expose
    @SerializedName("isadmin")
    private String mIsadmin;

    @ColumnInfo(name = "User_Key")
    @Expose
    @SerializedName("User_Key")
    private String userKey;

    @ColumnInfo(name = "User_ID")
    @Expose
    @SerializedName("User_ID")
    private String userID;

    @ColumnInfo(name = "name")
    @Expose
    @SerializedName("name")
    private String mName;

    @ColumnInfo(name = "designation")
    @Expose
    @SerializedName("designation")
    private String designation;


    @ColumnInfo(name = "profile_picture")
    @Expose
    @SerializedName("profile_picture")
    private String mProfilePicture;

    @ColumnInfo(name = "employee_id")
    @Expose
    @SerializedName("employee_id")
    private String employeeId;

    @ColumnInfo(name = "company_id")
    @Expose
    @SerializedName("company_id")
    private String companyId;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    @Expose
    @SerializedName("ID")
    private int id;

    @ColumnInfo(name = "manager_id")
    @Expose
    @SerializedName("manager_id")
    private String managerId;

    @ColumnInfo(name = "branch_latitude")
    @Expose
    @SerializedName("branch_latitude")
    private String branchLatitude;

    @ColumnInfo(name = "branch_longitute")
    @Expose
    @SerializedName("branch_longitute")
    private String branchLongitute;

    @ColumnInfo(name = "first_login")
    @Expose
    @SerializedName("first_login")
    private String firstLogin;


    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getBranchLatitude() {
        return branchLatitude;
    }

    public void setBranchLatitude(String branchLatitude) {
        this.branchLatitude = branchLatitude;
    }

    public String getBranchLongitute() {
        return branchLongitute;
    }

    public void setBranchLongitute(String branchLongitute) {
        this.branchLongitute = branchLongitute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public String getIsadmin() {
        return mIsadmin;
    }

    public void setIsadmin(String isadmin) {
        mIsadmin = isadmin;
    }


    public String getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        mProfilePicture = profilePicture;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


}
