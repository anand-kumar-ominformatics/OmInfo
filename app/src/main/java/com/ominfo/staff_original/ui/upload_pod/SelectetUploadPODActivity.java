package com.ominfo.staff_original.ui.upload_pod;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.ui.upload_pod.fragment.PendingForUpload;
import com.ominfo.staff_original.ui.upload_pod.fragment.PendingFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectetUploadPODActivity extends BaseActivity {

    Context mContext;

    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.imgCall)
    AppCompatImageView imgCall;



    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    private AppDatabase mDb;

    public static String packageName;

    public static File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lr_number);
        //for full screen toolbar
        mDb = BaseApplication.getInstance().getAppDatabase();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        ButterKnife.bind(this);
        init();

        packageName = getPackageName();
        directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    }

    private void init(){
        //set toolbar
        setToolbar();
        setLrNumberTab();
    }
    private void setToolbar(){
        toolbarTitle.setText("PDS pending POD");
        imgCall.setVisibility(View.GONE);
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,R.id.imgLogout,R.id.imgCall);
    }

    /*public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }*/


    private void setLrNumberTab(){
        // get the reference of FrameLayout and TabLayout
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        // Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Pending PDS"); // set the Text for the first Tab
        //firstTab.setIcon(R.drawable.ic_enforcement); // set an icon for the
        // first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        // Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Pending for Upload"); // set the Text for the second Tab
        //secondTab.setIcon(R.drawable.ic_record_ticket); // set an icon for the second tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
        Fragment fragment = new PendingFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        // Create a new Tab named "Third"
       /* TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Third"); // set the Text for the first Tab
        thirdTab.setIcon(R.drawable.ic_launcher_background); // set an icon for the first tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout
       */

        // perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PendingFragment();
                        break;
                    case 1:
                        fragment = new PendingForUpload();
                        break;
                    default:
                        fragment = new PendingFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //perform click actions
    @OnClick({/*R.id.imgBack,R.id.imgNotify*/})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
          /*  case R.id.imgBack:
                finish();
                break;
            case R.id.imgNotify:
                //launchScreen(mContext, NotificationsActivity.class);
                break;*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }
}