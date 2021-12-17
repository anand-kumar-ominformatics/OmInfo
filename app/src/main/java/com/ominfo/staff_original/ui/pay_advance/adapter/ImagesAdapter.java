package com.ominfo.staff_original.ui.pay_advance.adapter;

import static com.ominfo.staff_original.util.AppUtils.getBitmapFromView;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.pay_advance.model.KataChitthiImageModel;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;

import java.io.File;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    ListItemSelectListener listItemSelectListener;
    private List<KataChitthiImageModel> mListData;
    private Context mContext;
    private String mDate;

    public ImagesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ImagesAdapter(Context context, List<KataChitthiImageModel> listData, ListItemSelectListener itemClickListener) {
        this.mListData = listData;
        this.mContext = context;
        this.listItemSelectListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_images_kata_chithi, parent, false);

        return new ViewHolder(listItem);
    }

    public void updateList(List<KataChitthiImageModel> list){
        mListData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mListData != null) {
            File imgFile = new File(mListData.get(position).getImagePath());
            if(mListData.get(position).getImageType()==1) {
                holder.mProgressBar.setVisibility(View.GONE);
                holder.imgShow.setImageURI(Uri.fromFile(imgFile));
            }else {
                AppUtils.loadImage(mContext, mListData.get(position).getImagePath(), holder.imgShow, holder.mProgressBar);
            }
        }

        holder.layClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = getBitmapFromView(holder.imgShow);
                    listItemSelectListener.onItemClick(mListData.get(position), bitmap);
                }catch (Exception e){
                    LogUtil.printToastMSG(mContext,"Fail to load Image");
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imgShow;
        RelativeLayout layClick;
        ProgressBar mProgressBar;

        ViewHolder(View itemView) {
            super(itemView);
            imgShow = itemView.findViewById(R.id.imgShow);
            layClick = itemView.findViewById(R.id.layClick);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public interface ListItemSelectListener {
        void onItemClick(KataChitthiImageModel mData,Bitmap bitmap);
    }
}
