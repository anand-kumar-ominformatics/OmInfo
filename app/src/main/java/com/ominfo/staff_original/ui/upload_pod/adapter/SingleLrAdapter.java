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
import com.ominfo.staff_original.util.LogUtil;

import java.util.List;

public class SingleLrAdapter extends RecyclerView.Adapter<SingleLrAdapter.ViewHolder> {
    ItemClickListener itemClickListener;
    private List<GetPdsGcListForPodResult> mListData;
    private Context mContext;


    public SingleLrAdapter(Context context, List<GetPdsGcListForPodResult> listData, ItemClickListener itemClickListener) {

        this.mListData = listData;
        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_pending_list_for_lr, parent, false);
        return new ViewHolder(listItem);
    }

    public void updateList(List<GetPdsGcListForPodResult> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (mListData != null) {
            try {

                String lrNo = mListData.get(position).getGCNo();
                String consignee = mListData.get(position).getConsignee();

                Object photo1 = mListData.get(position).getPODPhoto1();
                Object photo2 = mListData.get(position).getPODPhoto2();

                if( lrNo != null || lrNo.length() != 0 )
                    holder.pdsNo.setText( lrNo );
                else
                    holder.pdsNo.setText( "No available" );

                if( consignee != null || consignee.length() != 0 )
                    holder.vehNo.setText( consignee );
                else
                    holder.vehNo.setText( "No available" );

                holder.image.setVisibility(View.VISIBLE);

                if( photo1 == null && photo2 == null ){

                    if( mListData.get(position).getInPending() ){
                        holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pending_for_upload));
                    }else{
                        holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pending_for_capture));
                    }


                }else{

                    boolean flag1 = photo1 != null || photo1.toString().length() != 0;
                    boolean flag2 = photo2 != null || photo2.toString().length() != 0;

                    if( flag1 && flag2 ) {
                        holder.image.setBackgroundDrawable( mContext.getDrawable( R.drawable.ic_baseline_check_circle_outline_24 ) );
                    }else{
                        holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.alert));
                    }

                }

            }catch (Exception e){
                LogUtil.printLog("test",e);
            }
        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick( mListData.get(position) );
            }
        });

        if( mListData.size()-1 == position ){
            holder.sep.setVisibility(View.GONE);
        }else
            holder.sep.setVisibility(View.VISIBLE);


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView pdsNo;
        AppCompatTextView vehNo;
        AppCompatImageView image;
        LinearLayoutCompat layoutItem;
        View sep;

        ViewHolder(View itemView) {
            super(itemView);

            pdsNo = itemView.findViewById(R.id.pdsNoValue);
            vehNo = itemView.findViewById(R.id.vehNo);
            image = itemView.findViewById(R.id.image);

            layoutItem = itemView.findViewById(R.id.layoutItem);

            sep = itemView.findViewById(R.id.sep);

        }
    }

    public interface ItemClickListener {
        void onClick(GetPdsGcListForPodResult data);
    }
}
