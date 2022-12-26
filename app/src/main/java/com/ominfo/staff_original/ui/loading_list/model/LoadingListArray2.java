
package com.ominfo.staff_original.ui.loading_list.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoadingListArray2 {

    @SerializedName("App_Document_ID")
    private String mAppDocumentID;
    @SerializedName("Listing_ID")
    private String mListingID;
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

    public String getListingID() {
        return mListingID;
    }

    public void setListingID(String listingID) {
        mListingID = listingID;
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
