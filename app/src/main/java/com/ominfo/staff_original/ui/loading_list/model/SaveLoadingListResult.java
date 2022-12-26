
package com.ominfo.staff_original.ui.loading_list.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SaveLoadingListResult {

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
