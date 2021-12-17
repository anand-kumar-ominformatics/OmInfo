
package com.ominfo.staff_original.ui.kata_chithi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FetchKataChitthiResult {

    @SerializedName("array1")
    private List<Array1> mArray1;
    @SerializedName("array2")
    private List<Array2> mArray2;

    public List<Array1> getArray1() {
        return mArray1;
    }

    public void setArray1(List<Array1> array1) {
        mArray1 = array1;
    }

    public List<Array2> getArray2() {
        return mArray2;
    }

    public void setArray2(List<Array2> array2) {
        mArray2 = array2;
    }

}
