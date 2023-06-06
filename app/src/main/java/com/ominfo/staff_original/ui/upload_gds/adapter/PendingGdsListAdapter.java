package com.ominfo.staff_original.ui.upload_gds.adapter;

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
import com.ominfo.staff_original.ui.upload_gds.model.FetchPendingGdsListResult;
import com.ominfo.staff_original.util.LogUtil;

import java.util.List;

public class PendingGdsListAdapter extends RecyclerView.Adapter<PendingGdsListAdapter.ViewHolder> {
    ItemClickListener itemClickListener;
    private List<FetchPendingGdsListResult> mListData;
    private Context mContext;


    public PendingGdsListAdapter(Context context, List<FetchPendingGdsListResult> listData, ItemClickListener itemClickListener) {

        this.mListData = listData;
        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_upload_psd_list, parent, false);
        return new ViewHolder(listItem);
    }

    public void updateList(List<FetchPendingGdsListResult> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (mListData != null) {
            try {

                String pdsNo = mListData.get(position).getDDCNo();
//                String vehNo = mListData.get(position).getVehicleNo();
//                String driverName = mListData.get(position).getTemGdsriver();
                String totalGC = mListData.get(position).getTotalGC();
                String scanedGC = mListData.get(position).getPODScannedGC();

                if( pdsNo != null || !pdsNo.equals("") )
                    holder.pdsNo.setText( pdsNo );
                else
                    holder.pdsNo.setText( "No available" );

//                if( vehNo != null  )
//                    holder.vehNo.setText( vehNo +"/"+driverName);
//                else
//                    holder.vehNo.setText( "No available" );



                boolean flag = totalGC.equalsIgnoreCase( scanedGC );

                if( flag ) {
                    holder.image.setVisibility(View.VISIBLE);
                    holder.pending.setVisibility(View.GONE);
                    holder.image.setImageDrawable( mContext.getDrawable( R.drawable.ic_baseline_check_circle_outline_24 ) );
                }else{

                    holder.image.setVisibility(View.GONE);
                    holder.pending.setVisibility(View.VISIBLE);
                    int pending = Integer.parseInt(mListData.get(position).getTotalGC())-Integer.parseInt(mListData.get(position).getPODScannedGC());
                    holder.pending.setText( pending +"/" +mListData.get(position).getTotalGC() );

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
        }


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView pdsNo;
        AppCompatTextView vehNo;
        AppCompatTextView pending;
        AppCompatImageView image;
        LinearLayoutCompat layoutItem;
        View sep;


        ViewHolder(View itemView) {
            super(itemView);

            pdsNo = itemView.findViewById(R.id.pdsNoValue);
            vehNo = itemView.findViewById(R.id.vehNo);
            pending = itemView.findViewById(R.id.pendingValue);
            image = itemView.findViewById(R.id.image);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            sep = itemView.findViewById(R.id.sep);

        }
    }

    public interface ItemClickListener {
        void onClick(FetchPendingGdsListResult data);
    }
}
