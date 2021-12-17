package com.ominfo.staff_original.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.common.SwipeToDeleteCallback;
import com.ominfo.staff_original.common.customui.OtpEditText;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;
import com.ominfo.staff_original.ui.notifications.adapter.NotificationsAdapter;
import com.ominfo.staff_original.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationsActivity extends BaseActivity {

    Context mContext;
    @BindView(R.id.rvPuranaHisab)
    RecyclerView rvPuranaHisab;
    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;
    @BindView(R.id.snackbar)
    ConstraintLayout snackbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    NotificationsAdapter mNotificationsAdapter;
    List<DriverHisabModel> driverHisabModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkCheck.isInternetAvailable(mContext)) {
                   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    //Do something
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

                } else {
                    LogUtil.printToastMSG(mContext,getString(R.string.err_msg_connection_was_refused));
                            mSwipeRefreshLayout.setRefreshing(false);

                }
            }
        });
        //enableSwipeToDeleteAndUndo();
    }

    private void setToolbar(){
        //set toolbar title
        toolbarTitle.setText(R.string.scr_title_notifications);
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,0,R.id.imgCall);
    }

    private void setAdapterForPuranaHisabList() {
        driverHisabModelList.add(new DriverHisabModel("भराई","1"));
        driverHisabModelList.add(new DriverHisabModel("वराई","0"));
        driverHisabModelList.add(new DriverHisabModel("टोल खर्चा","1"));
        driverHisabModelList.add(new DriverHisabModel("रस्सी","1"));
        driverHisabModelList.add(new DriverHisabModel("चाय पानी","0"));
        driverHisabModelList.add(new DriverHisabModel("कांटा","1"));

        if (driverHisabModelList.size() > 0) {
            mNotificationsAdapter = new NotificationsAdapter(mContext, driverHisabModelList, new NotificationsAdapter.ListItemSelectListener() {
                @Override
                public void onItemClick(DriverHisabModel mDataTicket) {
                    showInfoDialog();
                }
            });
            rvPuranaHisab.setHasFixedSize(true);
            rvPuranaHisab.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            rvPuranaHisab.setAdapter(mNotificationsAdapter);
            rvPuranaHisab.setVisibility(View.VISIBLE);
        } else {
            rvPuranaHisab.setVisibility(View.GONE);
        }
    }

    private void showInfoDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_get_otp);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        //appCompatButton.setVisibility(View.VISIBLE);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showOtpFromDriverDialog();
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void showOtpFromDriverDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_otp_from_driver);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        OtpEditText edtOpt = mDialog.findViewById(R.id.edtOpt);
        AppCompatImageView imgErr = mDialog.findViewById(R.id.imgErr);
        AppCompatTextView tvErrMsg = mDialog.findViewById(R.id.tvErrMsg);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgErr.setVisibility(View.GONE);
                //tvErrMsg.setText(R.string.msg_otp_has_successfully);
                if(edtOpt.getText().toString().trim().equals("1234")){
                    mDialog.dismiss();
                    showSuccessDialog(getString(R.string.msg_advance_paid_success));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 700);

                }
                else
                {
                    imgErr.setVisibility(View.VISIBLE);
                    tvErrMsg.setText(R.string.msg_otp_you_have_entered);
                }
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this,mNotificationsAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final String item = mNotificationsAdapter.getData().get(position).getDriverHisabValue();
                //if(item.equals("1")) {
                   //mNotificationsAdapter.removeItem(position);
                //}

                /*Snackbar snackbar = Snackbar
                        .make(snackbar, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mNotificationsAdapter.restoreItem(item, position);
                        rvPuranaHisab.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
*/
            }
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int swipeFlags = ItemTouchHelper.END; // or RIGHT
                final int position = viewHolder.getAdapterPosition();
                final String item = mNotificationsAdapter.getData().get(position).getDriverHisabValue();
                if(item.equals("1")) {
                    mNotificationsAdapter.removeItem(position);
                }
                if(item.equals("0")) {
                    mNotificationsAdapter.notifyDataSetChanged();
                    //mNotificationsAdapter.removeItem(position);
                }
                return makeMovementFlags(0, swipeFlags);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvPuranaHisab);
    }


    //perform click actions
    @OnClick({/*R.id.imgBack,R.id.imgNotify*/})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

        }
    }
}