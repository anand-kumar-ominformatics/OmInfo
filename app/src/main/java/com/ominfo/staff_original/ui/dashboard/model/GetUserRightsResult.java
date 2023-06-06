
package com.ominfo.staff_original.ui.dashboard.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "user_rights")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetUserRightsResult {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    @Expose
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = "App_Button_ID")
    @Expose
    @SerializedName("App_Button_ID")
    private String mAppButtonID;

    @ColumnInfo(name = "App_Button_Name")
    @Expose
    @SerializedName("App_Button_Name")
    private String mAppButtonName;

    @ColumnInfo(name = "Can_Add")
    @Expose
    @SerializedName("Can_Add")
    private String mCanAdd;

    @ColumnInfo(name = "Can_Delete")
    @Expose
    @SerializedName("Can_Delete")
    private String mCanDelete;

    @ColumnInfo(name = "Can_Edit")
    @Expose
    @SerializedName("Can_Edit")
    private String mCanEdit;

    @ColumnInfo(name = "Can_Read")
    @Expose
    @SerializedName("Can_Read")
    private String mCanRead;

    @ColumnInfo(name = "eCargo_MenuItem_ID")
    @Expose
    @SerializedName("eCargo_MenuItem_ID")
    private String mECargoMenuItemID;

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getAppButtonID() {
        return mAppButtonID;
    }

    public void setAppButtonID(String appButtonID) {
        mAppButtonID = appButtonID;
    }

    public String getAppButtonName() {
        return mAppButtonName;
    }

    public void setAppButtonName(String appButtonName) {
        mAppButtonName = appButtonName;
    }

    public String getCanAdd() {
        return mCanAdd;
    }

    public void setCanAdd(String canAdd) {
        mCanAdd = canAdd;
    }

    public String getCanDelete() {
        return mCanDelete;
    }

    public void setCanDelete(String canDelete) {
        mCanDelete = canDelete;
    }

    public String getCanEdit() {
        return mCanEdit;
    }

    public void setCanEdit(String canEdit) {
        mCanEdit = canEdit;
    }

    public String getCanRead() {
        return mCanRead;
    }

    public void setCanRead(String canRead) {
        mCanRead = canRead;
    }

    public String getECargoMenuItemID() {
        return mECargoMenuItemID;
    }

    public void setECargoMenuItemID(String eCargoMenuItemID) {
        mECargoMenuItemID = eCargoMenuItemID;
    }

}
