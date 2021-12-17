package com.ominfo.staff_original.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ominfo.staff_original.ui.contacts.model.GetContactsViewModel;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleViewModel;
import com.ominfo.staff_original.ui.login.model.LoginViewModel;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Service service;

    @Inject
    public ViewModelFactory(Service service) {
        this.service = service;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
         if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(service);
          }
         else  if (modelClass.isAssignableFrom(FetchKataChitthiViewModel.class)) {
             return (T) new FetchKataChitthiViewModel(service);
         }
         else  if (modelClass.isAssignableFrom(SaveKataChitthiViewModel.class)) {
             return (T) new SaveKataChitthiViewModel(service);
         }
         else  if (modelClass.isAssignableFrom(GetContactsViewModel.class)) {
             return (T) new GetContactsViewModel(service);
         }
         else  if (modelClass.isAssignableFrom(VehicleViewModel.class)) {
             return (T) new VehicleViewModel(service);
         }
         else  if (modelClass.isAssignableFrom(AdvToDriverViewModel.class)) {
             return (T) new AdvToDriverViewModel(service);
         }


        throw new IllegalArgumentException("Unknown class name");

    }
}
