
package com.ominfo.staff_original.ui.contacts.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class CallResult {

    @SerializedName("Emp_Name")
    private String mEmpName;
    @SerializedName("MobileNo1")
    private String mMobileNo1;
    @SerializedName("MobileNo2")
    private String mMobileNo2;

    public String getEmpName() {
        return mEmpName;
    }

    public void setEmpName(String empName) {
        mEmpName = empName;
    }

    public String getMobileNo1() {
        return mMobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        mMobileNo1 = mobileNo1;
    }

    public String getMobileNo2() {
        return mMobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        mMobileNo2 = mobileNo2;
    }

}
