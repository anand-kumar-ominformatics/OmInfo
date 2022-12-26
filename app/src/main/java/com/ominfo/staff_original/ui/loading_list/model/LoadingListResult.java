
package com.ominfo.staff_original.ui.loading_list.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LoadingListResult {

    @SerializedName("Array1")
    private List<LoadingListArray1> mArray1;
    @SerializedName("Array2")
    private List<LoadingListArray2> mArray2;

    public List<LoadingListArray1> getArray1() {
        return mArray1;
    }

    public void setArray1(List<LoadingListArray1> array1) {
        mArray1 = array1;
    }

    public List<LoadingListArray2> getArray2() {
        return mArray2;
    }

    public void setArray2(List<LoadingListArray2> array2) {
        mArray2 = array2;
    }

}
