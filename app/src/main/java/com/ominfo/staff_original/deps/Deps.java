package com.ominfo.staff_original.deps;


import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.network.NetworkModule;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.ui.kata_chithi.KataChithiActivity;
import com.ominfo.staff_original.ui.pay_advance.PayAdvanceActivity;
import com.ominfo.staff_original.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NetworkModule.class})
@Singleton
public interface Deps
{
    void inject(BaseActivity baseActivity);

    void inject(LoginActivity loginActivity);

    void inject(KataChithiActivity kataChithiActivity);

    void inject(DashbooardActivity dashbooardActivity);

}