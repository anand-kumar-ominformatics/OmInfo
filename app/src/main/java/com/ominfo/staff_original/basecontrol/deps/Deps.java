package com.ominfo.staff_original.basecontrol.deps;


import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseFragment;
import com.ominfo.staff_original.network.NetworkModule;
import com.ominfo.staff_original.ui.SplashActivity;
import com.ominfo.staff_original.ui.attendance.CalenderActivity;
import com.ominfo.staff_original.ui.attendance.StartAttendanceActivity;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.ui.kata_chithi.KataChithiActivity;
import com.ominfo.staff_original.ui.loading_list.LoadingListActivity;
import com.ominfo.staff_original.ui.login.LoginActivity;
import com.ominfo.staff_original.ui.track_and_track.TrackAndTraceLRActivity;

import com.ominfo.staff_original.ui.upload_gds.SelectetUploadGDSActivity;
import com.ominfo.staff_original.ui.upload_gds.SingleGDSDetails;
import com.ominfo.staff_original.ui.upload_gds.fragment.PendingGDSForUpload;
import com.ominfo.staff_original.ui.upload_gds.fragment.PendingGDSFragment;
import com.ominfo.staff_original.ui.upload_pod.SelectetUploadPODActivity;
import com.ominfo.staff_original.ui.upload_pod.SingleLrDetails;
import com.ominfo.staff_original.ui.upload_pod.fragment.PendingPDSForUpload;
import com.ominfo.staff_original.ui.upload_pod.fragment.PendingPDSFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NetworkModule.class})
@Singleton
public interface Deps
{
    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(LoginActivity loginActivity);

    void inject(KataChithiActivity kataChithiActivity);

    void inject(DashbooardActivity dashbooardActivity);

    void inject(LoadingListActivity loadingListActivity);

    void inject(StartAttendanceActivity startAttendanceActivity);

    void inject(CalenderActivity calenderActivity);

    void inject(SplashActivity splashActivity);

    void inject(PendingPDSFragment pendingPDSFragment);

    void inject(SelectetUploadPODActivity selectetUploadPODActivity);

    void inject(TrackAndTraceLRActivity trackAndTraceLRActivity);

    void inject(SingleLrDetails trackAndTraceLRActivity);

    void inject(PendingPDSForUpload pendingPDSForUpload);

    void inject(PendingGDSFragment pendingGDSFragment);

    void inject(SingleGDSDetails singleGDSDetails);


}
