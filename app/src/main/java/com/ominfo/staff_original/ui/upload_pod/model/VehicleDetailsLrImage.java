
package com.ominfo.staff_original.ui.upload_pod.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class VehicleDetailsLrImage {

    @SerializedName("image")
    private String image;
    @SerializedName("Lr_No")
    private String lr;
    @SerializedName("Url")
    private String url;
    @SerializedName("image_path")
    private String imagePath;


    public VehicleDetailsLrImage(String mImage, String imageUri, String imagePath) {
        this.image = mImage;
        this.url = imageUri;
        this.imagePath = imagePath;
    }


    public VehicleDetailsLrImage() {

    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLr(String lr) {
        this.lr = lr;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }


    public String getImage() {
        return image;
    }

    public String getLr() {
        return lr;
    }

    public String getUrl() {
        return url;
    }


}
