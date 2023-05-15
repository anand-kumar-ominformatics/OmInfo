package com.ominfo.staff_original.ui.purana_hisab.activity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;
import com.ominfo.staff_original.ui.purana_hisab.adapter.ComplaintsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintsActivity extends BaseActivity {
    Context mContext;
    @BindView(R.id.rvPuranaHisab)
    RecyclerView rvPuranaHisab;
    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;
    ComplaintsAdapter mComplaintsAdapter;
    List<DriverHisabModel> driverHisabModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        //set toolbar
        setToolbar();
        setAdapterForPuranaHisabList();
    }

    private void setToolbar(){
        //set toolbar title
        toolbarTitle.setText(R.string.scr_title_complaints);
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,0,R.id.imgCall);
    }

    private void setAdapterForPuranaHisabList() {
        driverHisabModelList.add(new DriverHisabModel("भराई","1"));
        driverHisabModelList.add(new DriverHisabModel("वराई","0"));
        driverHisabModelList.add(new DriverHisabModel("चाय पानी","0"));
        driverHisabModelList.add(new DriverHisabModel("टोल खर्चा","1"));
        driverHisabModelList.add(new DriverHisabModel("रस्सी","1"));
        driverHisabModelList.add(new DriverHisabModel("कांटा","1"));

        if (driverHisabModelList.size() > 0) {
            mComplaintsAdapter = new ComplaintsAdapter(mContext, driverHisabModelList, new ComplaintsAdapter.ListItemSelectListener() {
                @Override
                public void onItemClick(DriverHisabModel mDataTicket) {
                    Intent intent = new Intent(mContext,HisabDetailsActivity.class);
                    intent.putExtra(Constants.FROM_SCREEN, Constants.OKAY);
                    startActivity(intent);
                }
            });
            rvPuranaHisab.setHasFixedSize(true);
            rvPuranaHisab.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            rvPuranaHisab.setAdapter(mComplaintsAdapter);
            rvPuranaHisab.setVisibility(View.VISIBLE);
        } else {
            rvPuranaHisab.setVisibility(View.GONE);
        }
    }

    //perform click actions
    @OnClick({/*R.id.imgBack,R.id.imgNotify*/})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
           /* case R.id.imgBack:
                finish();
                break;
            case R.id.imgNotify:
                launchScreen(mContext, NotificationsActivity.class);
                break;*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }
}