package com.ominfo.staff_original.ui.dashboard.adapter;

import static com.ominfo.staff_original.util.AppUtils.monthToSlash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.dashboard.model.Alldetail;
import com.ominfo.staff_original.ui.kata_chithi.model.Array6;

import java.util.List;

public class AdvanceToDriverAdapter extends RecyclerView.Adapter<AdvanceToDriverAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<Alldetail> mListData;
    private Context mContext;
    private String mDate;

    public AdvanceToDriverAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AdvanceToDriverAdapter(Context context, List<Alldetail> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_advance_to_driver, parent, false);

        return new ViewHolder(listItem);
    }

    public void updateList(List<Alldetail> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mListData != null) {
            try {
                //String currentString = ;
                //String[] separated = currentString.split(" ");
                holder.tvDriverName.setText(mListData.get(position).getDriverName());
            }catch (Exception e){e.printStackTrace();}
            try {
            holder.tvDate.setText(monthToSlash(mListData.get(position).getDate()));
            String vehNo = getLast4Characters(mListData.get(position).getVehicleNo());
            holder.tvVehNo.setText(vehNo);
            holder.tvAmt.setText("₹ " +mListData.get(position).getAdvance());
            }catch (Exception e){e.printStackTrace();}

            if(mListData.size()>0){
                if(position%2!=0){
                    holder.relRC.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_grey_left_right_no_border_dialog));
                }
                else {
                    holder.relRC.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_white_left_right_no_border_dialog));
                }
            }
            if(position==mListData.size()-1){
                holder.relRC.setBackground(mContext.getResources().getDrawable(R.drawable.shape_round_no_top_dialog));
            }

        }

       /* holder.layCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemSelectListener.onItemClick(mListData.get(position));
            }
        });*/


    }

    public void removeItem(int position) {
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    private String getLast4Characters(String input){
        String lastFourDigits = "";     //substring containing last 4 characters

        if (input.length() > 4)
        {
            lastFourDigits = input.substring(input.length() - 4);
        }
        else
        {
            lastFourDigits = input;
        }

        return  lastFourDigits;
    }

    /*public void restoreItem(String item, int position) {
        mListData.add(position, item);
        notifyItemInserted(position);
    }*/

    public List<Alldetail> getData() {
        return mListData;
    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvDriverName,tvDate,tvVehNo,tvAmt;
        //LinearLayoutCompat layCard;
        LinearLayoutCompat relRC;

        ViewHolder(View itemView) {
            super(itemView);
            tvDriverName = itemView.findViewById(R.id.tvDriver);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvVehNo = itemView.findViewById(R.id.tvVehicleNo);
            tvAmt = itemView.findViewById(R.id.tvAmt);
            relRC = itemView.findViewById(R.id.layHeaderToolbar);
        }
    }

    public interface ListItemSelectListener {
        void onItemClick(Array6 mData);
    }
}
