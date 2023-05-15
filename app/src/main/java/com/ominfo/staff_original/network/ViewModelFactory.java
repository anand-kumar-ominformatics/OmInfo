package com.ominfo.staff_original.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ominfo.staff_original.ui.attendance.model.CalenderAllListViewModel;
import com.ominfo.staff_original.ui.attendance.model.CalenderHolidaysListViewModel;
import com.ominfo.staff_original.ui.attendance.model.GetAttendanceViewModel;
import com.ominfo.staff_original.ui.attendance.model.HighlightsViewModel;
import com.ominfo.staff_original.ui.attendance.model.UpdateAttendanceViewModel;
import com.ominfo.staff_original.ui.contacts.model.GetContactsViewModel;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverViewModel;
import com.ominfo.staff_original.ui.dashboard.model.AppVersionViewModel;
import com.ominfo.staff_original.ui.dashboard.model.SingleEmployeeListViewModel;
import com.ominfo.staff_original.ui.dashboard.model.TrackAndTraceLRViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleViewModel;
import com.ominfo.staff_original.ui.loading_list.model.FetchLoadingListViewModel;
import com.ominfo.staff_original.ui.loading_list.model.SaveLoadingListViewModel;
import com.ominfo.staff_original.ui.login.model.LoginViewModel;
import com.ominfo.staff_original.ui.login.model.UpdateKeyViewModel;

import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingListViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.GetVehicleViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.PdsGcListForPodViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.PodSaveOfLRViewModel;


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
         else  if (modelClass.isAssignableFrom(FetchLoadingListViewModel.class)) {
             return (T) new FetchLoadingListViewModel(service);
         }else  if (modelClass.isAssignableFrom(SaveLoadingListViewModel.class)) {
             return (T) new SaveLoadingListViewModel(service);
         } else  if (modelClass.isAssignableFrom(GetAttendanceViewModel.class)) {
             return (T) new GetAttendanceViewModel(service);
         }else  if (modelClass.isAssignableFrom(UpdateAttendanceViewModel.class)) {
             return (T) new UpdateAttendanceViewModel(service);
         }else  if (modelClass.isAssignableFrom(CalenderHolidaysListViewModel.class)) {
             return (T) new CalenderHolidaysListViewModel(service);
         }else  if (modelClass.isAssignableFrom(CalenderAllListViewModel.class)) {
             return (T) new CalenderAllListViewModel(service);
         }else  if (modelClass.isAssignableFrom(HighlightsViewModel.class)) {
             return (T) new HighlightsViewModel(service);
         }else  if (modelClass.isAssignableFrom(SingleEmployeeListViewModel.class)) {
             return (T) new SingleEmployeeListViewModel(service);
         }else  if (modelClass.isAssignableFrom(AppVersionViewModel.class)) {
             return (T) new AppVersionViewModel(service);
         }else  if (modelClass.isAssignableFrom(UpdateKeyViewModel.class)) {
             return (T) new UpdateKeyViewModel(service);
         }else  if (modelClass.isAssignableFrom(TrackAndTraceLRViewModel.class)) {
             return (T) new TrackAndTraceLRViewModel(service);
         }else  if (modelClass.isAssignableFrom(GetVehicleViewModel.class)) {
             return (T) new GetVehicleViewModel(service);
         } else  if (modelClass.isAssignableFrom(PodSaveOfLRViewModel.class)) {
             return (T) new PodSaveOfLRViewModel(service);
         } else  if (modelClass.isAssignableFrom(FetchPendingListViewModel.class)) {
             return (T) new FetchPendingListViewModel(service);
         }else  if (modelClass.isAssignableFrom(PdsGcListForPodViewModel.class)) {
             return (T) new PdsGcListForPodViewModel(service);
         }

         throw new IllegalArgumentException("Unknown class name");
    }
}
