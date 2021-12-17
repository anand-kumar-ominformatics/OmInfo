
package com.ominfo.staff_original.ui.purana_hisab.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PuranaHisabModel {

    @SerializedName("purana_hisab_title")
    private String puranaHisabTitle;

    @SerializedName("purana_hisab_value")
    private String puranaHisabValue;

    @SerializedName("from")
    private int from;

    public PuranaHisabModel(String puranaHisabTitle, String puranaHisabValue, int from) {
        this.puranaHisabTitle = puranaHisabTitle;
        this.puranaHisabValue = puranaHisabValue;
        this.from = from;
    }

    public String getPuranaHisabTitle() {
        return puranaHisabTitle;
    }

    public void setPuranaHisabTitle(String puranaHisabTitle) {
        this.puranaHisabTitle = puranaHisabTitle;
    }

    public String getPuranaHisabValue() {
        return puranaHisabValue;
    }

    public void setPuranaHisabValue(String puranaHisabValue) {
        this.puranaHisabValue = puranaHisabValue;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "PuranaHisabModel{" +
                "puranaHisabTitle='" + puranaHisabTitle + '\'' +
                ", puranaHisabValue='" + puranaHisabValue + '\'' +
                ", from=" + from +
                '}';
    }
}
