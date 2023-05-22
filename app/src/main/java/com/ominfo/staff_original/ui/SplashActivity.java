package com.ominfo.staff_original.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.ui.dashboard.model.AppVersionRequest;
import com.ominfo.staff_original.ui.dashboard.model.AppVersionResponse;
import com.ominfo.staff_original.ui.dashboard.model.AppVersionViewModel;
import com.ominfo.staff_original.ui.login.LoginActivity;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.tv_header)
    AppCompatImageView imgheader;
    @Inject
    ViewModelFactory mViewModelFactory;
    private AppVersionViewModel appVersionViewModel;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        mDb = BaseApplication.getInstance(this).getAppDatabase();
        getDeps().inject(this);
        injectAPI();
        Glide.with(this)
                .load(R.drawable.ic_turanth_landsc_logo)
                .into(imgheader);

        callVehicleDetailsApi();
    }
    private void injectAPI() {
        appVersionViewModel = ViewModelProviders.of(SplashActivity.this, mViewModelFactory).get(AppVersionViewModel.class);
        appVersionViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_GET_VERSION));
    }

    /* Call Api For Vehicle No */
    private void callVehicleDetailsApi() {
        if (NetworkCheck.isInternetAvailable(SplashActivity.this)) {
            String userK = "617042344";
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            if (loginResultTable != null) {
                //userK = loginResultTable.getUserKey();
            }
            AppVersionRequest mRequest = new AppVersionRequest();
            mRequest.setUserkey(userK); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
            mRequest.setInputId("Staff");
            mRequest.setValue("0");
            Gson gson = new Gson();
            String bodyInStringFormat = gson.toJson(mRequest);
            appVersionViewModel.hitAppVersionApi(bodyInStringFormat);
        } else {
            setTimeStamp();
            LogUtil.printToastMSG(SplashActivity.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /*wait for few second than launch new screen*/
    private void setTimeStamp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //get login status
                    Boolean iSLoggedIn = SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.IS_LOGGED_IN, false);
                    if (iSLoggedIn){
                        launchScreen(DashbooardActivity.class);
                    }else {
                        launchScreen(LoginActivity.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, Constants.SPLAHS_TIME_OUT);
    }

    /*navigate to new screen*/
    private void launchScreen(Class activity) {
        startActivity(new Intent(SplashActivity.this, activity));
        finish();
    }

    public void showLogoutDialog(Context mContext) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_logout);
        //mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatTextView tvStart = mDialog.findViewById(R.id.tvStart);
        AppCompatButton okayButton = mDialog.findViewById(R.id.uploadButton);
        AppCompatButton cancelButton = mDialog.findViewById(R.id.cancelButton);
        okayButton.setVisibility(View.GONE);
        cancelButton.setText("Update");
        tvStart.setText("Please, Update your app to new version to continue using.");
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss(); SplashActivity.this.finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finishAffinity();
                SharedPref.getInstance(mContext).write(SharedPrefKey.IS_LOGGED_IN, false);
                launchScreen(mContext, LoginActivity.class);
                try{mDb.getDbDAO().deleteLogin();}
                catch (Exception e){e.printStackTrace();}
                try {
                    Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.ominfo.staff_original"));
                    appStoreIntent.setPackage("com.android.vending");

                    startActivity(appStoreIntent);
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.ominfo.staff_original")));
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
                    if (tag.equalsIgnoreCase(DynamicAPIPath.POST_GET_VERSION)) {
                        try {
                            AppVersionResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), AppVersionResponse.class);

                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                if (responseModel.getResult() != null && responseModel.getResult().size() > 0) {
                                    String versionYour = responseModel.getResult().get(0).getVersion() == null
                                            || responseModel.getResult().get(0).getVersion().equals("") ? "0" : responseModel.getResult().get(0).getVersion();
                                    if (Constants.VERSION < Integer.parseInt(versionYour)) {
                                        showLogoutDialog(this);
                                    } else {
                                        setTimeStamp();
                                    }
                                } else {
                                    setTimeStamp();
                                }
                            } else {
                                setTimeStamp();
                            }
                        }catch (Exception e){
                            setTimeStamp();
                        }
                    }
                    break;
                }
            case ERROR:
                dismissLoader();
                setTimeStamp();
                LogUtil.printToastMSG(SplashActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }
}