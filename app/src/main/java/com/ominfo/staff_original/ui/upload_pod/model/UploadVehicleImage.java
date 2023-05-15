
package com.ominfo.staff_original.ui.upload_pod.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UploadVehicleImage {

    @SerializedName("Url")
    private String mImage;
    @SerializedName("Lr_No")
    private String mLr;

    @SerializedName("plant_id")
    private String plantID;




    public UploadVehicleImage(String plantID,String mLr,String mImage) {
        this.mImage = mImage;
        this.mLr = mLr;
        this.plantID = plantID;
    }

    public void setPlantID(String plantID) {
        this.plantID = plantID;
    }

    public String getPlantID() {
        return plantID;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getLr() {
        return mLr;
    }

    public void setLr(String lr) {
        mLr = lr;
    }

}
