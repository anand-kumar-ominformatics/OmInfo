package com.ominfo.staff_original.ui.purana_hisab.adapter;

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
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<DriverHisabModel> mListData;
    private Context mContext;
    private String mDate;

    public ComplaintsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ComplaintsAdapter(Context context, List<DriverHisabModel> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_complaints, parent, false);

        return new ViewHolder(listItem);
    }

    public void updateList(List<DriverHisabModel> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mListData != null) {
            if (mListData.get(position).getDriverHisabValue().equals("1")) {
                holder.imgComplaintStatus.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_checked_com));
                holder.tvResolvedBy.setVisibility(View.VISIBLE);
                holder.tvComplaintStaus.setText("Manager A");
            }
            if (mListData.get(position).getDriverHisabValue().equals("0")) {
                holder.imgComplaintStatus.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_cross_com));
                holder.tvResolvedBy.setVisibility(View.GONE);
                holder.tvComplaintStaus.setText(R.string.scr_lbl_unresolved);
            }
        }

        holder.layClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemSelectListener.onItemClick(mListData.get(position));
            }
        });


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvHideStatus,tvStatus,tvComplaintStaus,tvResolvedBy;
        LinearLayoutCompat layClick;
        AppCompatImageView imgComplaintStatus;
        //AppCompatEditText etHisab;


        ViewHolder(View itemView) {
            super(itemView);
            tvHideStatus = itemView.findViewById(R.id.tvHideStatus);
            tvComplaintStaus = itemView.findViewById(R.id.tvComplaintStaus);
            tvResolvedBy = itemView.findViewById(R.id.tvResolvedBy);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layClick = itemView.findViewById(R.id.layClick);
            imgComplaintStatus = itemView.findViewById(R.id.imgComplaintStatus);
        }
    }

    public interface ListItemSelectListener {
        void onItemClick(DriverHisabModel mData);
    }
}
