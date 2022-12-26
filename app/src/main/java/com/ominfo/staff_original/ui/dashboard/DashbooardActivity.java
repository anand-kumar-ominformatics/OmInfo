package com.ominfo.staff_original.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.ominfo.staff_original.ui.attendance.StartAttendanceActivity;
import com.ominfo.staff_original.ui.attendance.CalenderActivity;
import com.ominfo.staff_original.ui.attendance.model.AttendanceList;
import com.ominfo.staff_original.ui.attendance.ripple_effect.RippleBackground;
import com.ominfo.staff_original.ui.dashboard.adapter.AdvanceToDriverAdapter;
import com.ominfo.staff_original.ui.dashboard.adapter.TripAwadhiAdapter;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverRequest;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverResponse;
import com.ominfo.staff_original.ui.dashboard.model.AdvToDriverViewModel;
import com.ominfo.staff_original.ui.dashboard.model.Alldetail;
import com.ominfo.staff_original.ui.dashboard.model.AttendanceDaysTable;
import com.ominfo.staff_original.ui.dashboard.model.DayData;
import com.ominfo.staff_original.ui.dashboard.model.GetSingleEmployeeResponse;
import com.ominfo.staff_original.ui.dashboard.model.SingleEmployeeListViewModel;
import com.ominfo.staff_original.ui.dashboard.model.SingleEmployeeRequest;
import com.ominfo.staff_original.ui.dashboard.model.SingleEmployeeResult;
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
import com.ominfo.staff_original.ui.loading_list.LoadingListActivity;
import com.ominfo.staff_original.ui.pay_advance.PayAdvanceActivity;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.purana_hisab.PuranaHisabActivity;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashbooardActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private SingleEmployeeListViewModel employeeListViewModel;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LinearLayoutCompat cardThree;
    @BindView(R.id.rippleEffect)
    RippleBackground rippleEffect;
    @BindView(R.id.add_attendance)
    FloatingActionButton add_attendance;

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

    private void init() {
        mDb = BaseApplication.getInstance(mContext).getAppDatabase();
        setToolbar();
        if (!isGPSEnabled(mContext)) {
            requestPermission();
        } else {
            getLocation();
        }
        rippleEffect.stopRippleAnimation();
        add_attendance.setVisibility(View.VISIBLE);
        rippleEffect.setVisibility(View.VISIBLE);
        callEmployeeListApi();
        LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
        if (loginResultTable != null) {
            try {
                String currentString = loginResultTable.getFirstName();
                String[] separated = currentString.split(" ");
                tvDriverName.setText(separated[0]);
                tvUser.setText(loginResultTable.getUserName());
                tvBranch.setText(loginResultTable.getMainName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        driverHisabModelList.add(new DriverHisabModel("Surat", ""));
        driverHisabModelList.add(new DriverHisabModel("", "Enter Rout"));

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                DashbooardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                DashbooardActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            final Handler handler = new Handler();
            Location finalBestLocation = bestLocation;
            handler.postDelayed(new Runnable() {
                public void run() {
                    Location locationGPS = finalBestLocation;
                    if (locationGPS != null) {
                        /*lat = locationGPS.getLatitude();
                        lng = locationGPS.getLongitude();
                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lng);*/
                        //setCurrent();
                        //startLocationService();
                        //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                    } else {
                        //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1000);

        }
    }

    //request camera and storage permission
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,

                                },
                        1000);

            } else {
                OnGPS();
            }
        } else {
            OnGPS();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                if (!isGPSEnabled(mContext)) {
                    settingsRequest();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //startLocationService();
                        }
                    }, 5000);
                }

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //check if gps is enabled
    public boolean isGPSEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gps_enabled;
    }

    //sending GPS request
    public void settingsRequest() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
        }
        long INTERVAL = Constants.INTERVAL; //5 min
        long FASTEST_INTERVAL = Constants.FASTEST_INTERVAL; //2 min
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /*locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(INTERVAL_M); //higher priority*/
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("TAG", "setResultCallback: " + LocationSettingsStatusCodes.SUCCESS);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("TAG", "setResultCallback: " + LocationSettingsStatusCodes.RESOLUTION_REQUIRED);

                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(DashbooardActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("TAG", "setResultCallback: " + LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE);

                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                ) {
                    OnGPS();
                } else {
                    Toast.makeText(this, "somthing_went_wrong", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void deleteImagesFolder() {
        try {
            //private void deleteImagesFolder(){
            File myDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (myDir.exists()) {
                myDir.delete();
            }
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), Constants.FILE_NAME);
            //File oldFile = new File(myDir);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectAPI() {
        advToDriverViewModel = ViewModelProviders.of(DashbooardActivity.this, mViewModelFactory).get(AdvToDriverViewModel.class);
        advToDriverViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_ADV_TO_DRIVER));

        employeeListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SingleEmployeeListViewModel.class);
        employeeListViewModel.getResponse().observe(this, apiResponse ->consumeResponse(apiResponse, DynamicAPIPath.POST_SINLE_EMPLOYEES_LIST));
    }

    private void setToolbar() {
        initToolbar(0, mContext, R.id.imgBack, R.id.imgReport, R.id.imgNotify, R.id.imgLogout, R.id.imgCall);
    }

    /* Call Api For employee list */
    private void callEmployeeListApi() {
        try {
            if (NetworkCheck.isInternetAvailable(mContext)) {
                LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
                if (loginTable != null && loginTable.getCompany_Id() != null && loginTable.getCompany_Id().getEmp_id() != null) {
                    SingleEmployeeRequest request = new SingleEmployeeRequest();
                    request.setCompanyId(loginTable.getCompany_Id() == null ? "0" : loginTable.getCompany_Id().getCompany_ID());
                    request.setEmpId(loginTable.getCompany_Id() == null ? "0" : loginTable.getCompany_Id().getEmp_id());
                    request.setUserkey(loginTable.getUserKey());
                    Gson gson = new Gson();
                    String bodyInStringFormat = gson.toJson(request);
                    employeeListViewModel.hitSingleEmployeeList(bodyInStringFormat);
                } else {
                    LogUtil.printToastMSG(mContext, "Something is wrong.");
                }
            } else {
                LogUtil.printToastMSG(mContext, getString(R.string.err_msg_connection_was_refused));
            }
        }catch (Exception e){}
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
        if (total == null) {
            total = "0";
        }
        mTotal.setText("₹ " + total);
        setAdapterForInfoList(rvTripAwadhi, mDialog, layTotal, mainLayout, tvNoData);
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
                                       RelativeLayout layTotal, LinearLayout mainLayout, AppCompatTextView tvNoData) {
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
    @OnClick({R.id.layKataChithi, R.id.layAdvanceToDriver, R.id.layLoading,
            R.id.rippleEffect, R.id.layAttList})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rippleEffect:
                launchScreen(mContext, StartAttendanceActivity.class);
                break;
            case R.id.layAttList:
                launchScreen(mContext, CalenderActivity.class);
                break;
            case R.id.layKataChithi:
                launchScreen(mContext, KataChithiActivity.class);
                break;
            case R.id.layLoading:
                launchScreen(mContext, LoadingListActivity.class);
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
    private void openDataPicker(AppCompatTextView datePickerField, int mFrom) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "";
                if (mFrom == 0) {
                    myFormat = "dd MMM yyyy"; //In which you need put here
                } else {
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
                openContactSupportEmail(mContext, "Share " + tvTitle.getText().toString(), "", tvTitle.getText().toString() + "\n" + tvTitleValue.getText().toString());
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
                    try {
                        if (tag.equalsIgnoreCase(DynamicAPIPath.POST_SINLE_EMPLOYEES_LIST)) {
                            GetSingleEmployeeResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), GetSingleEmployeeResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                mDb.getDbDAO().deleteAttendanceData();
                                SingleEmployeeResult employeeListResData = responseModel.getResult().get(0);
                                AttendanceDaysTable daysTable = new AttendanceDaysTable();
                                DayData dayData = new DayData();
                                dayData.setMonDay("Monday");dayData.setMonWorking(employeeListResData.getMonWorking()==null?"No":employeeListResData.getMonWorking());
                                dayData.setMonStartTime(employeeListResData.getMonStartTime()==null?"10:00:00":employeeListResData.getMonStartTime());
                                dayData.setMonEndTime(employeeListResData.getMonEndTime()==null?"19:00:00":employeeListResData.getMonEndTime());
                                dayData.setTueDay("Tuesday");dayData.setTueWorking(employeeListResData.getTueWorking()==null?"No":employeeListResData.getTueWorking());
                                dayData.setTueStartTime(employeeListResData.getTueStartTime()==null?"10:00:00":employeeListResData.getTueStartTime());
                                dayData.setTueEndTime(employeeListResData.getTueEndTime()==null?"19:00:00":employeeListResData.getTueEndTime());
                                dayData.setWedDay("Wednesday");dayData.setWedWorking(employeeListResData.getWedWorking()==null?"No":employeeListResData.getWedWorking());
                                dayData.setWedStartTime(employeeListResData.getWedStartTime()==null?"10:00:00":employeeListResData.getWedStartTime());
                                dayData.setWedEndTime(employeeListResData.getWedEndTime()==null?"19:00:00":employeeListResData.getWedEndTime());
                                dayData.setThrusDay("Thursday");dayData.setThrusWorking(employeeListResData.getThrusWorking()==null?"No":employeeListResData.getThrusWorking());
                                dayData.setThrusStartTime(employeeListResData.getThrusStartTime()==null?"10:00:00":employeeListResData.getThrusStartTime());
                                dayData.setThrusEndTime(employeeListResData.getThrusEndTime()==null?"19:00:00":employeeListResData.getThrusEndTime());
                                dayData.setFriDay("Friday");dayData.setFriWorking(employeeListResData.getFriWorking()==null?"No":employeeListResData.getFriWorking());
                                dayData.setFriStartTime(employeeListResData.getFriStartTime()==null?"10:00:00":employeeListResData.getFriStartTime());
                                dayData.setFriEndTime(employeeListResData.getFriEndTime()==null?"19:00:00":employeeListResData.getFriStartTime());
                                dayData.setSatDay("Saturday");dayData.setSatWorking(employeeListResData.getSatWorking()==null?"No":employeeListResData.getSatWorking());
                                dayData.setSatStartTime(employeeListResData.getSatStartTime()==null?"10:00:00":employeeListResData.getSatStartTime());
                                dayData.setSatEndTime(employeeListResData.getSatEndTime()==null?"19:00:00":employeeListResData.getSatEndTime());
                                dayData.setSunDay("Sunday");dayData.setSunWorking(employeeListResData.getSunWorking()==null?"No":employeeListResData.getSunWorking());
                                dayData.setSunStartTime(employeeListResData.getSunStartTime()==null?"10:00:00":employeeListResData.getSunStartTime());
                                dayData.setSunEndTime(employeeListResData.getSunEndTime()==null?"19:00:00":employeeListResData.getSunEndTime());
                                daysTable.setLoginDays(dayData);
                                mDb.getDbDAO().insertAttendanceData(daysTable);
                                //AttendanceDaysTable loginAttendance = mDb.getDbDAO().getTestAttendanceData(); //check
                                if (dayData != null) {
                                    setAttendanceFloatingButtons(dayData);
                                }
                            }
                        }
                    }catch (Exception e){
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
    private void setAttendanceFloatingButtons(DayData loginDays) {
        rippleEffect.stopRippleAnimation();
        add_attendance.setVisibility(View.VISIBLE);
        rippleEffect.setVisibility(View.VISIBLE);
        Boolean iSTimer = SharedPref.getInstance(mContext).read(SharedPrefKey.CHECK_IN_BUTTON, false);
        //if(!iSTimer){
        List<AttendanceList> attendanceListList = new ArrayList<>();
        attendanceListList.add(new AttendanceList(loginDays.getMonDay(), loginDays.getMonWorking() == null ? "No" : loginDays.getMonWorking(), loginDays.getMonStartTime(), loginDays.getMonEndTime()));
        attendanceListList.add(new AttendanceList(loginDays.getTueDay(), loginDays.getTueWorking() == null ? "No" : loginDays.getTueWorking(), loginDays.getTueStartTime(), loginDays.getTueEndTime()));
        attendanceListList.add(new AttendanceList(loginDays.getWedDay(), loginDays.getWedWorking() == null ? "No" : loginDays.getWedWorking(), loginDays.getWedStartTime(), loginDays.getWedEndTime()));
        attendanceListList.add(new AttendanceList(loginDays.getThrusDay(), loginDays.getThrusWorking() == null ? "No" : loginDays.getThrusWorking(), loginDays.getThrusStartTime(), loginDays.getThrusEndTime()));
        attendanceListList.add(new AttendanceList(loginDays.getFriDay(), loginDays.getFriWorking() == null ? "No" : loginDays.getFriWorking(), loginDays.getFriStartTime(), loginDays.getFriEndTime()));
        attendanceListList.add(new AttendanceList(loginDays.getSatDay(), loginDays.getSatWorking() == null ? "No" : loginDays.getSatWorking(), loginDays.getSatStartTime(), loginDays.getSatStartTime()));
        attendanceListList.add(new AttendanceList(loginDays.getSunDay(), loginDays.getSunWorking() == null ? "No" : loginDays.getSunWorking(), loginDays.getSunStartTime(), loginDays.getSunEndTime()));
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        SharedPref.getInstance(mContext).write(SharedPrefKey.ATTENDANCE_START_TIME, "00:00:00");
        //LogUtil.printToastMSG(mContext,"att_before_Test-"+weekDay);
        for (int i = 0; i < attendanceListList.size(); i++) {
            if (weekDay.equals(attendanceListList.get(i).getMonDay()) && (attendanceListList.get(i).getMonWorking().equals("Yes")||attendanceListList.get(i).getMonWorking().equals("yes"))) {
                //LogUtil.printToastMSG(mContext,"att_Test"+weekDay);
                SharedPref.getInstance(mContext).write(SharedPrefKey.ATTENDANCE_START_TIME, attendanceListList.get(i).getMonStartTime());
                String startDate = AppUtils.getCurrentDateTime_() + " " + AppUtils.getCurrentTimeIn24hr(),
                        endDate = (attendanceListList.get(i).getMonStartTime() == null ||
                                attendanceListList.get(i).getMonStartTime().equals("00:00:00") )? AppUtils.getCurrentDateTime_() + " " + "10:00:00" : AppUtils.getCurrentDateTime_() + " " + attendanceListList.get(i).getMonStartTime(),
                        attEndDate = (attendanceListList.get(i).getMonEndTime() == null ||
                                attendanceListList.get(i).getMonEndTime().equals("00:00:00") )? AppUtils.getCurrentDateTime_() + " " + "19:00:00" : AppUtils.getCurrentDateTime_() + " " + attendanceListList.get(i).getMonEndTime();
                String Min30LaterTime = "", Min60LaterTime = "";
                try {
                    SimpleDateFormat sdf30 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String currentDateandTime = sdf30.format(new Date());
                    Date date = sdf30.parse(endDate);
                    Calendar calendar30 = Calendar.getInstance();
                    calendar30.setTime(date);
                    calendar30.add(Calendar.MINUTE, 30);
                    Min30LaterTime = sdf30.format(calendar30.getTime());
                    //LogUtil.printToastMSG(mContext,Min30LaterTime);
                    SimpleDateFormat sdf60 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    //String currentDateandTime = sdf30.format(new Date());
                    Date date60 = sdf60.parse(endDate);
                    Calendar calendar60 = Calendar.getInstance();
                    calendar60.setTime(date60);
                    calendar60.add(Calendar.HOUR, 1);
                    Min60LaterTime = sdf60.format(calendar60.getTime());
                    //LogUtil.printToastMSG(mContext,Min60LaterTime);
                } catch (Exception e) {
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                add_attendance.setVisibility(View.VISIBLE);
                rippleEffect.setVisibility(View.VISIBLE);
                try {
                    Date date1 = sdf.parse(startDate);
                    LogUtil.printLog("att_start ", startDate);
                    Date date2 = sdf.parse(endDate);
                    LogUtil.printLog("att_end ", endDate);
                    Date date3 = sdf.parse(Min30LaterTime);
                    LogUtil.printLog("att_30 ", Min30LaterTime);
                    Date date4 = sdf.parse(Min60LaterTime);
                    LogUtil.printLog("att_60 ", Min60LaterTime);
                    Date date5 = sdf.parse(attEndDate);
                    LogUtil.printLog("att_realend ", attEndDate);
                    Boolean iSActiveChill = SharedPref.getInstance(mContext).read(SharedPrefKey.CHECK_IN_BUTTON, false);
                    String iSCheckInDoneChill = SharedPref.getInstance(mContext).read(SharedPrefKey.CHECK_OUT_TIME, AppUtils.getCurrentDateTime_() + " " + "00:00:00");
                    Date dateEnd = sdf.parse(iSCheckInDoneChill);

                    //String[] valll = iSCheckInDoneChill.split(" ");
                    //if (iSActiveChill) {
                    // LogUtil.printToastMSG(mContext,iSActiveChill+"-"+iSCheckInDoneChill);
                        /*if (date1.compareTo(date2) == -1) {
                            // Outputs -1 as date1 is before date2
                            rippleEffect.stopRippleAnimation();
                            add_attendance.setVisibility(View.VISIBLE);
                            rippleEffect.setVisibility(View.VISIBLE);
                        } else {
                            // LogUtil.printToastMSG(mContext,iSActiveChill+"-"+iSCheckInDoneChill);
                            rippleEffect.startRippleAnimation(2, mContext);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_blue));
                            }
                        }*/
                    // }
                    // if (!iSActiveChill) {
                    if ((date1.compareTo(date2) == -1) || (dateEnd.compareTo(date2) == 1) || (date1.compareTo(date5) == 1) || iSActiveChill){
                        // Outputs -1 as date1 is before date2
                        rippleEffect.stopRippleAnimation();
                        add_attendance.setVisibility(View.VISIBLE);
                        rippleEffect.setVisibility(View.VISIBLE);
                        //rippleEffect.startRippleAnimation(2, mContext);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_blue));
                        }
                    } else if (date1.compareTo(date2) == 1 && date1.compareTo(date3) == -1) {
                        // Outputs -1 as date3 is before date2
                        // Outputs 0 as the dates are now equal
                        rippleEffect.startRippleAnimation(2, mContext);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_blue));
                        }
                    } else if (date1.compareTo(date3) == 1 && date1.compareTo(date4) == -1) {
                        // Outputs 1 as date3 is after date1
                        rippleEffect.stopRippleAnimation();
                        rippleEffect.startRippleAnimation(1, mContext);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_yellow));
                        }
                    } else if (date1.compareTo(date4) == 1 && date1.compareTo(date5) == -1) {
                        // Outputs 1 as date4 is after date3
                        rippleEffect.stopRippleAnimation();
                        rippleEffect.startRippleAnimation(3, mContext);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_red));
                        }
                    } else if (date1.compareTo(date5) == 1) {
                        SharedPref.getInstance(mContext).write(SharedPrefKey.CHECK_IN_BUTTON, false);

                        // Outputs 1 as date4 is after date3
                            /*rippleEffect.stopRippleAnimation();
                            add_attendance.setVisibility(View.VISIBLE);
                            rippleEffect.setVisibility(View.VISIBLE);
                            try {
                                //((BaseActivity) mContext).stopService(new Intent(mContext, BackgroundAttentionService.class));
                            } catch (Exception e) {
                            }*/
                    }
                    //  }


                } catch (ParseException e) {
                    e.printStackTrace();
                    // Exception handling goes here
                    LogUtil.printLog("att_iiss", e.getMessage());
                }
                   /* boolean b = false;

                    try {
                        if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                            b = true;  // If start date is before end date.
                        } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                            b = true;  // If two dates are equal.

                        } else {
                            b = false; // If start date is after the end date.
                            rippleEffect.stopRippleAnimation();
                            rippleEffect.startRippleAnimation(1,mContext);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                add_attendance.setForeground(mContext.getDrawable(R.drawable.attention_gradient_yellow));
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/

                //return b;
                // }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callEmployeeListApi();
    }
}