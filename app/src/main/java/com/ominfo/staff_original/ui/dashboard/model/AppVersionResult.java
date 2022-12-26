
package com.ominfo.staff_original.ui.dashboard.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AppVersionResult {

    @SerializedName("id")
    private String mId;
    @SerializedName("input_id")
    private String mInputId;
    @SerializedName("version")
    private String mVersion;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInputId() {
        return mInputId;
    }

    public void setInputId(String inputId) {
        mInputId = inputId;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

}
