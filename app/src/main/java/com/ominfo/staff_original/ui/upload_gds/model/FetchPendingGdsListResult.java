
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPendingGdsListResult implements Serializable {
    @SerializedName("DDC_ID")
    private String mDDCID;
    @SerializedName("DDC_No")
    private String mDDCNo;
    @SerializedName("POD_Scanned_GC")
    private String mPODScannedGC;
    @SerializedName("Total_GC")
    private String mTotalGC;



    public String getDDCID() {
        return mDDCID;
    }

    public void setDDCID(String dDCID) {
        mDDCID = dDCID;
    }

    public String getDDCNo() {
        return mDDCNo;
    }

    public void setDDCNo(String dDCNo) {
        mDDCNo = dDCNo;
    }

    public String getPODScannedGC() {
        return mPODScannedGC;
    }

    public void setPODScannedGC(String pODScannedGC) {
        mPODScannedGC = pODScannedGC;
    }

    public String getTotalGC() {
        return mTotalGC;
    }

    public void setTotalGC(String totalGC) {
        mTotalGC = totalGC;
    }


}
