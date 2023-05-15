package com.ominfo.staff_original.ui.track_and_track.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.dashboard.model.CrossingDetail;

import java.util.List;

public class CrossingAdapter extends RecyclerView.Adapter<CrossingAdapter.ViewHolder> {

    private List<CrossingDetail> mListData;

    private Context mContext;

    public CrossingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public CrossingAdapter(Context context, List<CrossingDetail> listData) {
        this.mListData = listData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CrossingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_crossing_details, parent, false);

        return new CrossingAdapter.ViewHolder(listItem);
    }

    public void updateList(List<CrossingDetail> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CrossingAdapter.ViewHolder holder, int position) {

        if ( mListData != null) {

            if( ! mListData.get(position).getManifestNo().trim().equals("") ) {
               holder.manifestNo.setText ( mListData.get(position).getManifestNo().trim() );
            }

            if( ! mListData.get(position).getMemoDate().trim().equals("") ) {
                holder.manifestDate.setText ( mListData.get(position).getMemoDate().trim() );
            }

            if( ! mListData.get(position).getMemoType().trim().equals("") ) {
                holder.manifestType.setText ( mListData.get(position).getMemoType().trim() );
            }

            if( ! mListData.get(position).getVehicleNo().trim().equals("") ) {
                holder.vehicleNo.setText ( mListData.get(position).getVehicleNo().trim() );
            }

            if( ! mListData.get(position).getManifestFrom().trim().equals("") ) {
                holder.manifestFrom.setText ( mListData.get(position).getManifestFrom().trim() );
            }

            if( ! mListData.get(position).getManifestTo().trim().equals("") ) {
                holder.manifestTo.setText ( mListData.get(position).getManifestTo().trim() );
            }

            if( mListData.size()-1 == position ) {
                holder.seprator.setVisibility(View.GONE);
            }

        }

    }

    public void removeItem(int position) {
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    public List<CrossingDetail> getData() {
        return mListData;
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView manifestNo;
        AppCompatTextView manifestDate;
        AppCompatTextView manifestType;
        AppCompatTextView vehicleNo;
        AppCompatTextView manifestFrom;
        AppCompatTextView manifestTo;

        View seprator;

        ViewHolder(View itemView) {
            super(itemView);
            manifestNo = itemView.findViewById(R.id.manifestNoValue);
            manifestDate = itemView.findViewById(R.id.manifestDateValue);
            manifestType = itemView.findViewById(R.id.manifestTypeValue);
            vehicleNo = itemView.findViewById(R.id.vehicleNoValue);
            manifestFrom = itemView.findViewById(R.id.manifestFromValue);
            manifestTo = itemView.findViewById(R.id.manifestToValue);

            seprator = itemView.findViewById(R.id.seperatorLine);

        }

    }

}
