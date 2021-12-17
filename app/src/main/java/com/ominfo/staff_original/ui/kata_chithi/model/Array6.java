
package com.ominfo.staff_original.ui.kata_chithi.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Array6 {

    @SerializedName("Attachment_Path")
    private String mAttachmentPath;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("Attachment_prefix")
    private String attachmentPrefix;

    public String getAttachmentPrefix() {
        return attachmentPrefix;
    }

    public void setAttachmentPrefix(String attachmentPrefix) {
        this.attachmentPrefix = attachmentPrefix;
    }

    public String getAttachmentPath() {
        return mAttachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        mAttachmentPath = attachmentPath;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return "Array6{" +
                "mAttachmentPath='" + mAttachmentPath + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", attachmentPrefix='" + attachmentPrefix + '\'' +
                '}';
    }
}
