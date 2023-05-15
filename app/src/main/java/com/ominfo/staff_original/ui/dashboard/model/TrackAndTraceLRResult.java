
package com.ominfo.staff_original.ui.dashboard.model;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class TrackAndTraceLRResult implements Serializable {

    @SerializedName("crossing_details")
    private List<CrossingDetail> mCrossingDetails;
    @SerializedName("delivery_details")
    private List<DeliveryDetail> mDeliveryDetails;
    @SerializedName("general_details")
    private List<GeneralDetail> mGeneralDetails;

    public List<CrossingDetail> getCrossingDetails() {
        return mCrossingDetails;
    }

    public void setCrossingDetails(List<CrossingDetail> crossingDetails) {
        mCrossingDetails = crossingDetails;
    }

    public List<DeliveryDetail> getDeliveryDetails() {
        return mDeliveryDetails;
    }

    public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
        mDeliveryDetails = deliveryDetails;
    }

    public List<GeneralDetail> getGeneralDetails() {
        return mGeneralDetails;
    }

    public void setGeneralDetails(List<GeneralDetail> generalDetails) {
        mGeneralDetails = generalDetails;
    }

}
