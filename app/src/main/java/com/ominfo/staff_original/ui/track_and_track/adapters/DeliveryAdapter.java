
package com.ominfo.staff_original.ui.track_and_track.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.dashboard.model.DeliveryDetail;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {

    private List<DeliveryDetail> mListData;

    private Context mContext;

    public DeliveryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public DeliveryAdapter(Context context, List<DeliveryDetail> listData) {
        this.mListData = listData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_delivery_details, parent, false);

        return new DeliveryAdapter.ViewHolder(listItem);
    }

    public void updateList(List<DeliveryDetail> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAdapter.ViewHolder holder, int position) {

        if (mListData != null) {


            if(!mListData.get(position).getDDCNo().trim().trim().equals("")) {
                holder.deliveryNo.setText ( mListData.get(position).getDDCNo().trim() );
            }

            if(!mListData.get(position).getDDCType().trim().equals("")) {
                holder.deliveryType.setText ( mListData.get(position).getDDCType().trim() );
            }

            if(!mListData.get(position).getDDCDate().trim().equals("")) {
                holder.deliveryDate.setText ( mListData.get(position).getDDCDate().trim() );
            }

            if(!mListData.get(position).getDDCTime().trim().equals("")) {
                holder.deliveryTime.setText ( mListData.get(position).getDDCTime().trim() );
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

    public List<DeliveryDetail> getData() {
        return mListData;
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView deliveryNo;
        AppCompatTextView deliveryType;
        AppCompatTextView deliveryDate;
        AppCompatTextView deliveryTime;

        View seprator;
        
        ViewHolder(View itemView) {
            super(itemView);
            deliveryNo = itemView.findViewById(R.id.deliveryNoValue);
            deliveryType = itemView.findViewById(R.id.deliveryTypeValue);
            deliveryDate = itemView.findViewById(R.id.deliveryDateValue);
            deliveryTime = itemView.findViewById(R.id.deliveryTimeValue);
            seprator = itemView.findViewById(R.id.seperatorLine);
        }

    }

}
