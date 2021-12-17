
package com.ominfo.staff_original.ui.kata_chithi.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Array2 {

    @SerializedName("App_Document_ID")
    private String mAppDocumentID;
    @SerializedName("Transaction_ID")
    private String mTransactionID;
    @SerializedName("Url")
    private String mUrl;
    @SerializedName("url_prefix")
    private String mUrlPrefix;

    public String getAppDocumentID() {
        return mAppDocumentID;
    }

    public void setAppDocumentID(String appDocumentID) {
        mAppDocumentID = appDocumentID;
    }

    public String getTransactionID() {
        return mTransactionID;
    }

    public void setTransactionID(String transactionID) {
        mTransactionID = transactionID;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUrlPrefix() {
        return mUrlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        mUrlPrefix = urlPrefix;
    }

}
