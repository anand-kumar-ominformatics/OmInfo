
package com.ominfo.staff_original.ui.dashboard.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "test_attendance_data")
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AttendanceDaysTable {
    @ColumnInfo(name = "LoginDays")
    @Expose
    @SerializedName("LoginDays")
    private DayData loginDays;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    @Expose
    @SerializedName("ID")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayData getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(DayData loginDays) {
        this.loginDays = loginDays;
    }
}
