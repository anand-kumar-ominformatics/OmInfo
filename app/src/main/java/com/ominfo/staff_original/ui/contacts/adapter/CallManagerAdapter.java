package com.ominfo.staff_original.ui.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.contacts.model.CallResult;

import java.util.List;

public class CallManagerAdapter extends RecyclerView.Adapter<CallManagerAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<CallResult> mListData;
    private Context mContext;
    private String mDate;

    public CallManagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public CallManagerAdapter(Context context, List<CallResult> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_call_manager, parent, false);

        return new ViewHolder(listItem);
    }

    public void updateList(List<CallResult> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mListData != null) {
            if(mListData.get(position).getEmpName().equals(" ")){
                holder.tvCallName.setVisibility(View.GONE);
                holder.layCaller1.setVisibility(View.GONE);
                holder.layCaller2.setVisibility(View.GONE);
                holder.viewBorder.setVisibility(View.GONE);
            }
            else {
                holder.viewBorder.setVisibility(View.VISIBLE);
                holder.tvCallName.setText(mListData.get(position).getEmpName());
                if(mListData.get(position).getMobileNo1()==null || mListData.get(position).getMobileNo1().equals(" ")){
                    holder.layCaller1.setVisibility(View.GONE);
                }
                else {
                    holder.layCaller1.setVisibility(View.VISIBLE);
                    holder.tvCaller1.setText(mListData.get(position).getMobileNo1());
                }
                if(mListData.get(position).getMobileNo2()==null || mListData.get(position).getMobileNo2().equals(" ")){
                    holder.viewBorder.setVisibility(View.GONE);
                    holder.layCaller2.setVisibility(View.GONE);
                }
                else {
                    holder.viewBorder.setVisibility(View.VISIBLE);
                    holder.layCaller2.setVisibility(View.VISIBLE);
                    holder.tvCaller2.setText(mListData.get(position).getMobileNo2());
                }
            }

            holder.tvCallName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_grey_left_right_border_dialog));
            holder.layCard.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_white_left_right_border_dialog));

            if(position==0){
                holder.tvCallName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_no_bottom_dialog));
                holder.layCard.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_white_left_right_border_dialog));
            }
             if(position==mListData.size()-1){
                holder.tvCallName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_grey_left_right_border_dialog));
                holder.layCard.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_white_no_top_dialog));
            }
        }

        holder.layCaller1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listItemSelectListener.onItemClick(holder.tvCaller1.getText().toString().trim());
                }catch (Exception e){}
            }
        });

        holder.layCaller2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listItemSelectListener.onItemClick(holder.tvCaller2.getText().toString().trim());
                }catch (Exception e){}
            }
        });


    }

    public void removeItem(int position) {
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    /*public void restoreItem(String item, int position) {
        mListData.add(position, item);
        notifyItemInserted(position);
    }*/

    public List<CallResult> getData() {
        return mListData;
    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvCallName,tvCaller1,tvCaller2;
        RelativeLayout layCaller1,layCaller2;
        LinearLayoutCompat layCard;
        View viewBorder;

        ViewHolder(View itemView) {
            super(itemView);
            tvCallName = itemView.findViewById(R.id.tvCallName);
            layCaller1 = itemView.findViewById(R.id.layCaller1);
            layCaller2 = itemView.findViewById(R.id.layCaller2);
            tvCaller1 = itemView.findViewById(R.id.tvCaller1);
            tvCaller2 = itemView.findViewById(R.id.tvCaller2);
            layCard = itemView.findViewById(R.id.layCard);
            viewBorder = itemView.findViewById(R.id.viewBorder);

        }
    }

    public interface ListItemSelectListener {
        void onItemClick(String mData);
    }
}
