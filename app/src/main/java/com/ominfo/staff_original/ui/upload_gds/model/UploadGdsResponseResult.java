
package com.ominfo.staff_original.ui.upload_gds.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UploadGdsResponseResult {

    @SerializedName("Error_Code")
    private String mErrorCode;
    @SerializedName("Error_Desc")
    private String mErrorDesc;

    public String getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(String errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorDesc() {
        return mErrorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        mErrorDesc = errorDesc;
    }

}
