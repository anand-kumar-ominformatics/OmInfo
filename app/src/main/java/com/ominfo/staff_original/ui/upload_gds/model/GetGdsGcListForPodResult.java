
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetGdsGcListForPodResult {

    @SerializedName("Consignee")
    private String mConsignee;
    @SerializedName("GC_ID")
    private String mGCID;
    @SerializedName("GC_No")
    private String mGCNo;
    @SerializedName("POD_Photo1")
    private Object mPODPhoto1;
    @SerializedName("POD_Photo2")
    private Object mPODPhoto2;

    private boolean inPending=false;

    public void setInPending(boolean inPending) {
        this.inPending = inPending;
    }

    public boolean getInPending() {
        return this.inPending;
    }

    public String getConsignee() {
        return mConsignee;
    }

    public void setConsignee(String consignee) {
        mConsignee = consignee;
    }

    public String getGCID() {
        return mGCID;
    }

    public void setGCID(String gCID) {
        mGCID = gCID;
    }

    public String getGCNo() {
        return mGCNo;
    }

    public void setGCNo(String gCNo) {
        mGCNo = gCNo;
    }

    public Object getPODPhoto1() {
        return mPODPhoto1;
    }

    public void setPODPhoto1(Object pODPhoto1) {
        mPODPhoto1 = pODPhoto1;
    }

    public Object getPODPhoto2() {
        return mPODPhoto2;
    }

    public void setPODPhoto2(Object pODPhoto2) {
        mPODPhoto2 = pODPhoto2;
    }
}

