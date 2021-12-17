package com.ominfo.staff_original.ui.purana_hisab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;
import com.ominfo.staff_original.ui.purana_hisab.model.PuranaHisabModel;

import java.util.List;

public class HisabDetailsAdapter extends RecyclerView.Adapter<HisabDetailsAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<PuranaHisabModel> mListData;
    private Context mContext;
    private String mDate;

    public HisabDetailsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public HisabDetailsAdapter(Context context, List<PuranaHisabModel> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_driver_hisab_details, parent, false);

        return new ViewHolder(listItem);
    }

    public void updateList(List<PuranaHisabModel> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mListData != null) {
            holder.tvHisabName.setText(mListData.get(position).getPuranaHisabTitle());
            if(mListData.get(position).getFrom()==1){
                //holder.imgFinalAmout.setVisibility(View.GONE);
            }
            else {
                //holder.imgFinalAmout.setVisibility(View.VISIBLE);
            }
            if(mListData.get(position).getPuranaHisabValue().equals("1")) {
                holder.tvTotalFineChange.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.imgFinalAmout.setVisibility(View.GONE);
            }
            else {
                holder.tvTotalFineChange.setTextColor(mContext.getResources().getColor(R.color.deep_red));
                holder.imgFinalAmout.setVisibility(View.VISIBLE);
            }
             }

        /*holder.mActivityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemSelectListener.onItemClick(mListData.get(position));
            }
        });*/


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvHisabName,tvTotalFineChange ;
        AppCompatImageView imgFinalAmout;


        ViewHolder(View itemView) {
            super(itemView);
            tvHisabName = itemView.findViewById(R.id.tvHisabName);
            tvTotalFineChange = itemView.findViewById(R.id.tvTotalFineChange);
            //imgHisabFine = itemView.findViewById(R.id.imgHisabFine);
            imgFinalAmout = itemView.findViewById(R.id.imgFinalAmout);
        }
    }

    public interface ListItemSelectListener {
        void onItemClick(DriverHisabModel mData);
    }
}
