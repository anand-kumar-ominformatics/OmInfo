package com.ominfo.staff_original.ui.dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.ui.dashboard.model.GetUserRightsResult;

import java.util.List;


public class DashboardBtnAdapter extends ArrayAdapter<GetUserRightsResult> {

    Context context;

    ItemClickListener itemClickListener;


    public DashboardBtnAdapter(Context mContext, List<GetUserRightsResult> getUserRightsResultList , ItemClickListener itemClickListener) {
        super(mContext, 0, getUserRightsResultList);
        this.itemClickListener=itemClickListener;
        this.context = mContext;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        System.out.println(position +"position");

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_for_dashboard_icon, parent, false);
        }


        try{

            GetUserRightsResult courseModel = getItem(position);
            ImageView image = listitemView.<ImageView>findViewById(R.id.icon);
            TextView text = listitemView.<TextView>findViewById(R.id.text);
            CardView layout = listitemView.<CardView>findViewById(R.id.layout);

//        image.setText(courseModel.getCourse_name());

            int imageId=0;

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClick( courseModel.getAppButtonName() );
                }
            });



            switch ( courseModel.getAppButtonName() ){
                case "Kanta Chithi":{
                    imageId = R.drawable.ic_kata_chitti;
                    break;
                }
                case "Advance to Driver":{
                    imageId = R.drawable.ic_payment_to_drive;
                    break;
                }
                case "Loading List":{
                    imageId = R.drawable.ic_loading_list;
                    break;
                }
                case "Attendance List":{
                    imageId = R.drawable.ic_attendance_list;
                    break;
                }
                case "PDS Pending POD":{
                    imageId = R.drawable.upload_pod;
                    break;
                }case "GDS Pending POD":{
                    imageId = R.drawable.upload_gds;
                    break;
                } default:{
                    imageId = R.drawable.ic_kata_chitti;
                }
            }

            image.setImageResource( imageId );
            text.setText( courseModel.getAppButtonName() );


        }catch (Exception e){
            System.out.println(e);
        }

        return listitemView;
    }



    public interface ItemClickListener{
        public void itemClick( String btnName);
    }

}
