package com.ominfo.staff_original.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.ui.login.LoginActivity;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.tv_header)
    AppCompatImageView imgheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        setTimeStamp();
        Glide.with(this)
                .load(R.drawable.ic_turanth_landsc_logo)
                .into(imgheader);

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
}