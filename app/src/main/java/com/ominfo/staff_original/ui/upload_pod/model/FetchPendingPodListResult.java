
package com.ominfo.staff_original.ui.upload_pod.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchPendingPodListResult implements Serializable {

    @SerializedName("PDS_Date")
    private String mPDSDate;
    @SerializedName("PDS_ID")
    private String mPDSID;
    @SerializedName("PDS_No")
    private String mPDSNo;
    @SerializedName("POD_Scanned_GC")
    private String mPODScannedGC;
    @SerializedName("Tempo_Driver")
    private String mTempoDriver;
    @SerializedName("Total_GC")
    private String mTotalGC;
    @SerializedName("Vehicle_No")
    private String mVehicleNo;

    public String getPDSDate() {
        return mPDSDate;
    }

    public void setPDSDate(String pDSDate) {
        mPDSDate = pDSDate;
    }

    public String getPDSID() {
        return mPDSID;
    }

    public void setPDSID(String pDSID) {
        mPDSID = pDSID;
    }

    public String getPDSNo() {
        return mPDSNo;
    }

    public void setPDSNo(String pDSNo) {
        mPDSNo = pDSNo;
    }

    public String getPODScannedGC() {
        return mPODScannedGC;
    }

    public void setPODScannedGC(String pODScannedGC) {
        mPODScannedGC = pODScannedGC;
    }

    public String getTempoDriver() {
        return mTempoDriver;
    }

    public void setTempoDriver(String tempoDriver) {
        mTempoDriver = tempoDriver;
    }

    public String getTotalGC() {
        return mTotalGC;
    }

    public void setTotalGC(String totalGC) {
        mTotalGC = totalGC;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        mVehicleNo = vehicleNo;
    }

}
