
package com.ominfo.staff_original.ui.upload_pod.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.upload_pod.model.GetPdsGcListForPodResult;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;
import com.ominfo.staff_original.util.LogUtil;

import java.util.List;

public class PendingForUploadAdapter extends RecyclerView.Adapter<PendingForUploadAdapter.ViewHolder> {

    private List<UploadPodRequest> mListData;
    private Context mContext;


    public PendingForUploadAdapter(Context context, List<UploadPodRequest> listData) {

        this.mListData = listData;
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_pending_pds_upload, parent, false);
        return new ViewHolder(listItem);
    }

    public void updateList(List<UploadPodRequest> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try{
            if (mListData != null) {

                String lrNo = mListData.get(position).getGcNo();
                String date = mListData.get(position).getUploadDate();

                if( lrNo != null && lrNo.length() != 0 )
                    holder.pdsNo.setText( lrNo );
                else
                    holder.pdsNo.setText( "No available" );

                if( date != null && date.length() != 0 )
                    holder.uploadedOnValue.setText( date );
                else
                    holder.uploadedOnValue.setText( "No available" );
            }


            if( mListData.size()-1 == position ){
                holder.sep.setVisibility(View.GONE);
            }
        }catch (Exception e){}


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView pdsNo;
        AppCompatTextView uploadedOnValue;
        View sep;

        ViewHolder(View itemView) {
            super(itemView);

            pdsNo = itemView.findViewById(R.id.lrNoValue);
            uploadedOnValue = itemView.findViewById(R.id.uploadedOnValue);

            sep = itemView.findViewById(R.id.sep);

        }
    }


}
