
package com.ominfo.staff_original.ui.upload_pod.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pds_details")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class UploadPodRequest {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    @Expose
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = "gc_id")
    @Expose
    @SerializedName("gc_id")
    private String mGcId;

    @ColumnInfo(name = "user_id")
    @Expose
    @SerializedName("user_id")
    private Long mUserId;

    @ColumnInfo(name = "userkey")
    @Expose
    @SerializedName("userkey")
    private String mUserkey;

    @ColumnInfo(name = "pod_photo1")
    @Expose
    @SerializedName("pod_photo1")
    private String mPodPhoto1;

    @ColumnInfo(name = "pod_photo2")
    @Expose
    @SerializedName("pod_photo2")
    private String mPodPhoto2;

    @ColumnInfo(name = "uploaded_date")
    @Expose
    private String uploadDate;

    @ColumnInfo(name = "gc_no")
    @Expose
    private String gcNo;

    public String getGcId() {
        return mGcId;
    }

    public void setGcId(String gcId) {
        mGcId = gcId;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

    public String getUserkey() {
        return mUserkey;
    }

    public void setUserkey(String userkey) {
        mUserkey = userkey;
    }

    public String getPodPhoto1() {
        return mPodPhoto1;
    }

    public void setPodPhoto1(String podPhoto1) {
        mPodPhoto1 = podPhoto1;
    }

    public String getPodPhoto2() {
        return mPodPhoto2;
    }

    public void setPodPhoto2(String podPhoto2) {
        mPodPhoto2 = podPhoto2;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadDate() {
        return uploadDate;
    }


    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setGcNo(String gcNo) {
        this.gcNo = gcNo;
    }

    public String getGcNo() {
        return gcNo;
    }
}











