package com.ominfo.staff_original.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.assigned_advance.AssignedAdvanceActivity;
import com.ominfo.staff_original.ui.dashboard.adapter.AdvanceToDriverAdapter;
import com.ominfo.staff_original.ui.dashboard.adapter.TripAwadhiAdapter;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverRequest;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverResponse;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverViewModel;
import com.ominfo.staff_original.ui.dashboard.model.Alldetail;
import com.ominfo.staff_original.ui.driver_hisab.DriverHisabActivity;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;
import com.ominfo.staff_original.ui.kata_chithi.KataChithiActivity;
import com.ominfo.staff_original.ui.kata_chithi.model.Array6;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleRequest;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleViewModel;
import com.ominfo.staff_original.ui.pay_advance.PayAdvanceActivity;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.purana_hisab.PuranaHisabActivity;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashbooardActivity extends BaseActivity {

    Context mContext;
    final Calendar myCalendar = Calendar.getInstance();
    private AppDatabase mDb;
    TripAwadhiAdapter mTripAwadhiAdapter;
    List<DriverHisabModel> driverHisabModelList = new ArrayList<>();
    @BindView(R.id.tvDriverName)
    AppCompatTextView tvDriverName;
    @BindView(R.id.tvUser)
    AppCompatTextView tvUser;
    @BindView(R.id.tvBranch)
    AppCompatTextView tvBranch;
    AdvanceToDriverAdapter advanceToDriverAdapter;
    @Inject
    ViewModelFactory mViewModelFactory;
    AdvToDriverViewModel advToDriverViewModel;
    List<Alldetail> mAdvToDriverList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbooard);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        getDeps().inject(this);
        ButterKnife.bind(this);
        injectAPI();
        init();
    }

    private void init(){
        mDb =BaseApplication.getInstance(mContext).getAppDatabase();
        //requestPermission();
        setToolbar();
        LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
        if(loginResultTable!=null){
            try {
                String currentString = loginResultTable.getFirstName();
                String[] separated = currentString.split(" ");
                tvDriverName.setText(separated[0]);
                tvUser.setText(loginResultTable.getUserName());
                tvBranch.setText(loginResultTable.getMainName());
            }catch (Exception e){e.printStackTrace();}
        }

        driverHisabModelList.add(new DriverHisabModel("Surat",""));
        driverHisabModelList.add(new DriverHisabModel("","Enter Rout"));

    }

    private void injectAPI() {
        advToDriverViewModel = ViewModelProviders.of(DashbooardActivity.this, mViewModelFactory).get(AdvToDriverViewModel.class);
        advToDriverViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_ADV_TO_DRIVER));
    }

    private void setToolbar(){
        initToolbar(0,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,R.id.imgLogout,R.id.imgCall);
    }

    //show Advance To Driver popup
    public void showAdvanceToDriverDialog(String total) {
        //DashboardResult mData = mNewDocList;
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_advance_to_driver);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatTextView mTotal = mDialog.findViewById(R.id.tvTotalAmount);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        RecyclerView rvTripAwadhi = mDialog.findViewById(R.id.rvTripAwadhi);
        RelativeLayout layTotal = mDialog.findViewById(R.id.layTotal);
        LinearLayout mainLayout = mDialog.findViewById(R.id.mainLayout);
        AppCompatTextView tvNoData = mDialog.findViewById(R.id.tvNoData);
        if(total==null){total="0";}
        mTotal.setText("₹ " +total);
        setAdapterForInfoList(rvTripAwadhi,mDialog, layTotal,  mainLayout, tvNoData);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
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

    private void setAdapterForInfoList(RecyclerView rvTripAwadhi, Dialog mDialog,
                                       RelativeLayout layTotal, LinearLayout mainLayout,AppCompatTextView tvNoData) {
        //mAdvToDriverList.add(new Alldetail("200","12 Dec 2021","MOHIT (ABDUL MAJID)","MH01CV9965"));
        //mAdvToDriverList.add(new Alldetail("2500","12 Jan 2021","NAUSHAD","MH01CV9665"));
        //mAdvToDriverList.add(new Alldetail("2900","13 Apr 2021","MOHIT (ABDUL MAJID)","MH01CV1965"));

        if (mAdvToDriverList.size() > 0) {
            rvTripAwadhi.setVisibility(View.VISIBLE);
            layTotal.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rvTripAwadhi.setVisibility(View.GONE);
            layTotal.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
        advanceToDriverAdapter = new AdvanceToDriverAdapter(mContext, mAdvToDriverList, new AdvanceToDriverAdapter.ListItemSelectListener() {
            @Override
            public void onItemClick(Array6 mData) {
                //mDialog.dismiss();
                //showFullImageDialog(mData);
            }
        });
        rvTripAwadhi.setHasFixedSize(true);
        rvTripAwadhi.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvTripAwadhi.setAdapter(advanceToDriverAdapter);
    }

    //perform click actions
    @OnClick({R.id.layKataChithi,R.id.layAdvanceToDriver})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.layKataChithi:
                launchScreen(mContext, KataChithiActivity.class);
                break;
            case R.id.layAdvanceToDriver:
                callAdvToDriverApi();
                break;
        }
    }

    /* Call Api to Advance to driver */
    private void callAdvToDriverApi() {
        if (NetworkCheck.isInternetAvailable(DashbooardActivity.this)) {
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            //DashboardResult dashboardResult = mDb.getDbDAO().getVehicleDetails();
            if (loginResultTable != null) {
                AdvToDriverRequest mLoginRequest = new AdvToDriverRequest();
                mLoginRequest.setUserkey(loginResultTable.getUserKey());
                mLoginRequest.setBranchID(loginResultTable.getMainId());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(mLoginRequest);
                LogUtil.printLog("request fetch", bodyInStringFormat);
                advToDriverViewModel.hitAdvanceToDriverApi(bodyInStringFormat);
            }
        } else {
            LogUtil.printToastMSG(DashbooardActivity.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    //set date picker view
    private void openDataPicker(AppCompatTextView datePickerField,int mFrom) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat="";
                if(mFrom==0) {
                     myFormat = "dd MMM yyyy"; //In which you need put here
                }
                else{
                     myFormat = "dd/MM/yyyy"; //In which you need put here
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                datePickerField.setText(sdf.format(myCalendar.getTime()));
            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void showEwayBillDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_eway_bill);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        AppCompatImageView imgShare = mDialog.findViewById(R.id.imgShare);
        AppCompatTextView tvTitle = mDialog.findViewById(R.id.tvNumberTitle);
        AppCompatTextView tvTitleValue = mDialog.findViewById(R.id.tvNumberTitleValue);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                openContactSupportEmail(mContext,"Share "+tvTitle.getText().toString(),"",tvTitle.getText().toString()+"\n"+tvTitleValue.getText().toString());
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

    //show truck details popup
    public void showTruckDetailsDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_truck_details);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        //AppCompatButton cancelButton = mDialog.findViewById(R.id.cancelButton);
        RelativeLayout relRC = mDialog.findViewById(R.id.relRC);
        RelativeLayout relPUC = mDialog.findViewById(R.id.relPUC);
        RelativeLayout relIss = mDialog.findViewById(R.id.relIss);

        relRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showFullImageDialog();
            }
        });
        relPUC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showFullImageDialog();
            }
        });
        relIss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showFullImageDialog();
            }
        });
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
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

    //show truck details popup
    public void showFullImageDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_doc_full_view);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
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


    //request camera and storage permission
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,

                                },
                        1000);

            } else {
                //createFolder();
            }
        } else {
            //createFolder();
        }
    }


    /*
     * ACCESS_FINE_LOCATION permission result
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    //BaseApplication.getInstance().mService.requestLocationUpdates();
                } else {
                    //Toast.makeText(mContext, getString(R.string.somthing_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    /*Api response */
    private void consumeResponse(ApiResponse apiResponse, String tag) {
        switch (apiResponse.status) {

            case LOADING:
                showProgressLoader(getString(R.string.scr_message_please_wait));
                break;

            case SUCCESS:
                dismissLoader();
                if (!apiResponse.data.isJsonNull()) {
                    LogUtil.printLog(tag, apiResponse.data.toString());
                    try {
                        if (tag.equalsIgnoreCase(DynamicAPIPath.POST_ADV_TO_DRIVER)) {
                            AdvToDriverResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), AdvToDriverResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                mAdvToDriverList.removeAll(mAdvToDriverList);
                                mAdvToDriverList = responseModel.getResult().getAlldetails();
                                showAdvanceToDriverDialog(responseModel.getResult().getSummary().get(0).getTotalAdvance());
                            } else {
                                LogUtil.printToastMSG(DashbooardActivity.this, responseModel.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(DashbooardActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }


}