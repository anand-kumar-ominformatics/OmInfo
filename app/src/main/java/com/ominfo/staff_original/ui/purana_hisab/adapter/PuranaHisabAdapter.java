package com.ominfo.staff_original.ui.purana_hisab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;

import java.util.List;

public class PuranaHisabAdapter extends RecyclerView.Adapter<PuranaHisabAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<DriverHisabModel> mListData;
    private Context mContext;
    private String mDate;

    public PuranaHisabAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public PuranaHisabAdapter(Context context, List<DriverHisabModel> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_asssign_advance, parent, false);

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
                //holder.tvHideStatus.setVisibility(View.GONE);
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.tvPrice.setText("₹1500");
                holder.viewBorder.setBackground(mContext.getResources().getDrawable(R.drawable.layout_round_shape_corners_8_green_view));
            }
            if (mListData.get(position).getDriverHisabValue().equals("0")) {
                //holder.tvHideStatus.setVisibility(View.GONE);
                holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.deep_red));
                holder.tvPrice.setText("₹1500");
                holder.viewBorder.setBackground(mContext.getResources().getDrawable(R.drawable.layout_round_shape_corners_8_red_view));

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
        AppCompatTextView tvPrice;
        LinearLayoutCompat layClick;
        View viewBorder;


        ViewHolder(View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            viewBorder = itemView.findViewById(R.id.viewBorder);
            layClick = itemView.findViewById(R.id.layClick);
        }
    }

    public interface ListItemSelectListener {
        void onItemClick(DriverHisabModel mData);
    }
}
