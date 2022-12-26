package com.ominfo.staff_original.ui.login;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.ui.login.model.LoginRequest;
import com.ominfo.staff_original.ui.login.model.LoginResponse;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.login.model.LoginViewModel;
import com.ominfo.staff_original.ui.login.model.UpdateKeyRequest;
import com.ominfo.staff_original.ui.login.model.UpdateKeyResponse;
import com.ominfo.staff_original.ui.login.model.UpdateKeyViewModel;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.input_password)
    TextInputLayout inputPassword;

    @BindView(R.id.input_email)
    TextInputLayout inputEmail;

    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    private static final String CHANNEL_ID = "101";
    private static final String TAG = "PushNotification";

    Context mContext;
    @Inject
    ViewModelFactory mViewModelFactory;
    private LoginViewModel mLoginViewModel;
    private UpdateKeyViewModel updateKeyViewModel;

    String recentToken = "";
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getDeps().inject(this);
        ButterKnife.bind(this);
        mContext = this;
        injectAPI();
        init();
        createNotificationChannel();
        getToken();
    }

    private void injectAPI() {
         mLoginViewModel = ViewModelProviders.of(LoginActivity.this, mViewModelFactory).get(LoginViewModel.class);
         mLoginViewModel.getResponse().observe(this, apiResponse ->consumeResponse(apiResponse, "Login"));

        updateKeyViewModel = ViewModelProviders.of(LoginActivity.this, mViewModelFactory).get(UpdateKeyViewModel.class);
        updateKeyViewModel.getResponse().observe(this, apiResponse ->consumeResponse(apiResponse, DynamicAPIPath.POST_UPDATE_KEY));
    }
    /* Call Api For Login user and get user details */
    private void callUpdateKeyApi(String mobileKey,String userId,String userKey) {
        if (NetworkCheck.isInternetAvailable(LoginActivity.this)) {
            UpdateKeyRequest mLoginRequest = new UpdateKeyRequest();
            mLoginRequest.setMobileKey(mobileKey); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
            mLoginRequest.setUserID(userId);
            mLoginRequest.setUserkey(userKey);
            Gson gson = new Gson();
            String bodyInStringFormat = gson.toJson(mLoginRequest);
            LogUtil.printLog("mobile_token",bodyInStringFormat);
            updateKeyViewModel.hitUpdateKeyApi(bodyInStringFormat);
        } else {
            LogUtil.printToastMSG(LoginActivity.this, getString(R.string.err_msg_connection_was_refused));
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Receve Firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void getToken() {
        //FirebaseMessaging.getInstance().deleteToken();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                //If task is failed then
                if (!task.isSuccessful()) {
                    LogUtil.printLog(TAG, "onComplete: Failed to get the Token");
                }

                //Token
                String token = task.getResult();
                recentToken = token;
                LogUtil.printLog(TAG, "onComplete: " + token);
            }
        });
    }

    private void init(){
        mDb = BaseApplication.getInstance(mContext).getAppDatabase();
        setErrorMSG();
        //set login cred
        //editTextEmail.setText("BO0005");
        //editTextPassword.setText("123"); //testecargo
        try{mDb.getDbDAO().deleteLogin();}
        catch (Exception e){e.printStackTrace();}
    }

    // set error if input field is blank
    private void setErrorMSG() {
        setErrorMessage(inputEmail, editTextEmail, getString(R.string.val_msg_please_enter_email));
        setErrorMessage(inputPassword, editTextPassword, getString(R.string.val_msg_please_enter_password));
    }

    //perform click actions
    @OnClick({R.id.loginButton})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.loginButton:
                if(isDetailsValid()) {
                    callLoginUserApi();
                }
                break;

        }
    }

    /* Call Api For Login user and get user details */
    private void callLoginUserApi() {
        if (NetworkCheck.isInternetAvailable(LoginActivity.this)) {
            LoginRequest mLoginRequest = new LoginRequest();
            mLoginRequest.setUsername(editTextEmail.getEditableText().toString().trim()); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
            mLoginRequest.setPassword(editTextPassword.getEditableText().toString().trim());
            Gson gson = new Gson();
            String bodyInStringFormat = gson.toJson(mLoginRequest);
            mLoginViewModel.hitLoginApi(bodyInStringFormat);
        } else {
            LogUtil.printToastMSG(LoginActivity.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /*check validations on field*/
    private boolean isDetailsValid() {
        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())) {
            setError(inputEmail, getString(R.string.val_msg_please_enter_email));
            return false;
        }
        else if (!getValidUser()) {
            setError(inputEmail, getString(R.string.val_msg_please_enter_email));
            return false;
        }
        else if (TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
            setError(inputPassword, getString(R.string.val_msg_please_enter_password));
            return false;
        }/* else if (editTextPassword.getEditableText().toString().trim().length() < 6) { //6
            setError(inputPassword, getString(R.string.val_msg_minimum_password));
            return false;
        }*/
        return true;
    }

    private boolean getValidUser(){
        String currentString = editTextEmail.getText().toString().trim();
        if(currentString.substring(0, 2).toLowerCase().equals("bo")){
            return true;
        }
        else {
            return false;
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
                    if (tag.equalsIgnoreCase("Login")) {
                        LoginResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), LoginResponse.class);
                        if (responseModel != null && responseModel.getStatus().equals("1")) {
                            try{
                                callUpdateKeyApi(recentToken/*responseModel.getResult().getMobileKey()*/,
                                        responseModel.getResult().getUserID(),responseModel.getResult().getUserKey());

                              /*  LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
                                if(loginResultTable!=null){
                                    if(!loginResultTable.get().equals(responseModel.getResult().getMobileKey())){
                                       }
                                }
                                else {
                                    callUpdateKeyApi(recentToken*//*responseModel.getResult().getMobileKey()*//*,
                                            responseModel.getResult().getUserID(), responseModel.getResult().getUserKey());
                                }*/
                            }catch (Exception e){
                                e.printStackTrace();
                                callUpdateKeyApi(recentToken/*responseModel.getResult().getMobileKey()*/,
                                        responseModel.getResult().getUserID(), responseModel.getResult().getUserKey());
                            }

                            finish();
                            launchScreen(mContext, DashbooardActivity.class);
                            LogUtil.printToastMSG(LoginActivity.this, responseModel.getMessage());
                            SharedPref.getInstance(this).write(SharedPrefKey.IS_LOGGED_IN, true);
                            try{
                               // responseModel.getResult().getCompany_Id().set
                                mDb.getDbDAO().insertLoginData(responseModel.getResult());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            //mDb.getDbDAO().insertLogin(responseModel);
                        } else {
                            LogUtil.printToastMSG(LoginActivity.this, responseModel.getMessage());
                        }
                    }
                    if (tag.equalsIgnoreCase(DynamicAPIPath.POST_UPDATE_KEY)) {
                        UpdateKeyResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), UpdateKeyResponse.class);
                        if (responseModel != null && responseModel.getStatus().equals("1")) {
                            //LogUtil.printToastMSG(LoginActivity.this, responseModel.getMessage());
                        } else {
                            //LogUtil.printToastMSG(LoginActivity.this, responseModel.getMessage());
                        }
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(LoginActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }


}