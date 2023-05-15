package com.ominfo.staff_original.ui.upload_pod.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.basecontrol.BaseFragment;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.upload_pod.SelectetUploadPODActivity;
import com.ominfo.staff_original.ui.upload_pod.SingleLrDetails;
import com.ominfo.staff_original.ui.upload_pod.adapter.PendingListAdapter;
import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingListViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingPodListRequest;
import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingPodListResponse;
import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingPodListResult;
import com.ominfo.staff_original.ui.upload_pod.model.PodSaveOfLRViewModel;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingFragment extends BaseFragment implements Filterable {

    private Context mContext;

    @BindView(R.id.empty_layoutActivity)
    LinearLayoutCompat emptylayoutActivity;

    @BindView(R.id.rvLrNumber)
    RecyclerView rvLrNumber;

    @BindView(R.id.tv_emptyLayTitle)
    AppCompatTextView tv_emptyLayTitle;

    @BindView(R.id.imgFilter)
    AppCompatImageView imgFilter;



    @BindView(R.id.dateLayout)
    LinearLayoutCompat dateLayout;

    @BindView(R.id.calanderImage)
    ImageView calanderImage;

    @BindView(R.id.dateValue)
    AppCompatTextView dateValue;


    @Inject
    ViewModelFactory mViewModelFactory;

    private FetchPendingListViewModel fetchPendingListViewModel;


    private AppDatabase mDb;

    public FetchPendingPodListRequest fetchPendingPodListRequest;

    public PendingFragment() {
        // Required empty public constructor
    }

    public static PendingFragment newInstance(String param1, String param2) {
        PendingFragment fragment = new PendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_lr_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SelectetUploadPODActivity) mContext).getDeps().inject(this);
        injectAPI();
        mDb = BaseApplication.getInstance().getAppDatabase();

        callGetPdsListForPod( AppUtils.getCurrentDateInyyyymmdd() );

    }

    private void injectAPI() {

        fetchPendingListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FetchPendingListViewModel.class);
        fetchPendingListViewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.GET_PDS_LIST_FOR_POD));

    }


    /* Call Api to Advance to driver */
    private void callGetPdsListForPod( String date ) {
        if (NetworkCheck.isInternetAvailable( getContext() )) {

            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();

            if (loginResultTable != null) {

                dateValue.setText( date );

                fetchPendingPodListRequest = new FetchPendingPodListRequest();

                fetchPendingPodListRequest.setUserkey(loginResultTable.getUserKey());
                fetchPendingPodListRequest.setBranchID( loginResultTable.getMainId() );
                fetchPendingPodListRequest.setDate( date );

//                fetchPendingPodListRequest.setUserkey("2609614150");
//                fetchPendingPodListRequest.setBranchID( "72" );
//                fetchPendingPodListRequest.setDate( "2022-06-27" );

                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(fetchPendingPodListRequest);
                fetchPendingListViewModel.hitGetPdsListForPodApi(bodyInStringFormat);

            }

        } else {
            LogUtil.printToastMSG( getContext() , getString(R.string.err_msg_connection_was_refused));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }

    private void setAdapterForLrImageList() {

        if(mListData.size()==0) {
            tv_emptyLayTitle.setText( getString(R.string.scr_lbl_no_data_available ) );
            setVisibleLayout( false );
            return;
        }else
            setVisibleLayout( true );

        pendingListAdapter = new PendingListAdapter( mContext , mListData,
                new PendingListAdapter.ItemClickListener() {
                    @Override
                    public void onClick(FetchPendingPodListResult data) {

                        Intent intent = new Intent(getContext(), SingleLrDetails.class);
                        intent.putExtra("data",data);
                        startActivity( intent );

                    }
                });

        rvLrNumber.setHasFixedSize(true);
        rvLrNumber.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvLrNumber.setAdapter(pendingListAdapter);

    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<FetchPendingPodListResult> filteredList = new ArrayList<>();


            String pdsNo1 = SharedPref.getInstance(getContext()).read(SharedPrefKey.POD_FILTER_BY_PDS_NO,"");
            String vehicleNo1 = SharedPref.getInstance(getContext()).read(SharedPrefKey.POD_FILTER_BY_VEH_NO,"");
            String driverName1 = SharedPref.getInstance(getContext()).read(SharedPrefKey.POD_FILTER_BY_DRIVER_NAME,"");

            boolean pdsFlag = pdsNo1 == null || pdsNo1.length() == 0;
            boolean vehFlag = vehicleNo1 == null || vehicleNo1.length() == 0;
            boolean driverFlag = driverName1 == null || driverName1.length() == 0;

            LogUtil.printLog("test",pdsFlag +" / " +vehFlag +" / "+driverFlag );

            if ( pdsFlag && vehFlag && driverFlag ) {
                LogUtil.printLog("test","all" );

                filteredList.addAll(exampleListFull);
            } else {

                String pdsp = pdsNo1.toLowerCase().trim();
                String vehp = vehicleNo1.toLowerCase().trim();
                String driverp = driverName1.toLowerCase().trim();

                LogUtil.printLog("test","single" );
                for (FetchPendingPodListResult item : exampleListFull) {

                    if (item.getPDSNo().toLowerCase().equalsIgnoreCase(pdsp) || (item.getPDSNo() == null ? "PDS No" : item.getPDSNo()).toLowerCase().equalsIgnoreCase(pdsp)) {
                        filteredList.add(item);
                        LogUtil.printLog("test","pds match" );
                    } else if (item.getVehicleNo().toLowerCase().equalsIgnoreCase(vehp) || (item.getVehicleNo() == null ? "Vehicle No" : item.getVehicleNo()).toLowerCase().equalsIgnoreCase(vehp)) {
                        filteredList.add(item);
                        LogUtil.printLog("test","veh match" );

                    } else if (item.getTempoDriver().toLowerCase().equalsIgnoreCase(driverp) || (item.getTempoDriver() == null ? "Driver Name" : item.getTempoDriver()).toLowerCase().equalsIgnoreCase(driverp)) {
                        filteredList.add(item);
                        LogUtil.printLog("test","driver match" );
                    }

                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }  @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {

                LogUtil.printLog("test","add"+mListData.size() );

                mListData.clear();
                LogUtil.printLog("test","add"+mListData.size() );



                mListData.addAll( (List) results.values);

                LogUtil.printLog("test","add"+((List) results.values).size() );


                LogUtil.printLog("test","add" );

                pendingListAdapter.notifyDataSetChanged();

            }catch (Exception e){}
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    private List<FetchPendingPodListResult> mListData;
    private List<FetchPendingPodListResult> exampleListFull;

    //Api response
    private void consumeResponse(ApiResponse apiResponse, String tag) {
        System.out.println(tag+"base consume call");

        switch (apiResponse.status) {


            case LOADING:
                ((BaseActivity) mContext).showProgressLoader(getString(R.string.scr_message_please_wait));
                break;

            case SUCCESS:
                ((BaseActivity) mContext).dismissLoader();

                if (!apiResponse.data.isJsonNull()) {
                    LogUtil.printLog(tag, apiResponse.data.toString());

                    try {

                        if (tag.equalsIgnoreCase(DynamicAPIPath.GET_PDS_LIST_FOR_POD)) {

                            FetchPendingPodListResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), FetchPendingPodListResponse.class);

                            if (responseModel != null && responseModel.getStatus().equals("1")) {


                                this.mListData = responseModel.getResult();
                                exampleListFull = new ArrayList<>(responseModel.getResult());

                                setAdapterForLrImageList( );
                            } else {
                                setVisibleLayout( false );
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case ERROR:
                ((BaseActivity) mContext).dismissLoader();
                LogUtil.printLog(tag, apiResponse.data );
                System.out.println( apiResponse.data +"................");
                LogUtil.printToastMSG(mContext, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }


    //perform click actions
    @OnClick({R.id.imgFilter,R.id.dateLayout})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgFilter:
                filterDialog();
                break;
            case R.id.dateLayout:
                openDataPicker(dateValue,1);
                break;

        }
    }


    private void filterDialog() {

        Dialog mDialog = new Dialog(mContext, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_filter_for_pod);
        mDialog.setCanceledOnTouchOutside(true);

        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);

        AppCompatButton applyBTN = mDialog.findViewById(R.id.applyBTN);
        AppCompatButton resetBTN = mDialog.findViewById(R.id.resetBTN);

        TextInputEditText pdsNo = mDialog.findViewById(R.id.pdsNoValue);
        TextInputEditText vehicleNo = mDialog.findViewById(R.id.vehicleNoValue);
        TextInputEditText driverName = mDialog.findViewById(R.id.driverNameValue);



        applyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_PDS_NO,pdsNo.getText().toString().trim());
                    SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_VEH_NO,vehicleNo.getText().toString().trim());
                    SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_DRIVER_NAME,driverName.getText().toString().trim());

                    exampleFilter.filter( "" );

                    mDialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.printToastMSG(mContext,"Something went wrong!");
                }
            }
        });

        resetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_PDS_NO,"");
                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_VEH_NO,"");
                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_DRIVER_NAME,"");

                pdsNo.setText("");
                vehicleNo.setText("");
                driverName.setText("");

                exampleFilter.filter("");

                mDialog.dismiss();

            }
        });


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_PDS_NO,"");
                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_VEH_NO,"");
                SharedPref.getInstance(getContext()).write(SharedPrefKey.POD_FILTER_BY_DRIVER_NAME,"");
                exampleFilter.filter("");
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    PendingListAdapter pendingListAdapter;

//    private void showSelectDateDialog() {
//
//        Dialog mDialog = new Dialog(mContext, R.style.ThemeDialogCustom);
//        mDialog.setContentView(R.layout.dialog_select_date);
//        mDialog.setCanceledOnTouchOutside(true);
//        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
//        AppCompatButton okayButton = mDialog.findViewById(R.id.submitButton);
//        AppCompatButton cancelButton = mDialog.findViewById(R.id.cancelButton);
//        AppCompatTextView tvFromDate = mDialog.findViewById(R.id.tvDateValueFrom);
//        LinearLayoutCompat layFromDate = mDialog.findViewById(R.id.layFromDate);
//
//        String fromDate = SharedPref.getInstance(getContext()).read(SharedPrefKey.CURRENT_DATE, AppUtils.getCurrentDateInyyyymmdd());
//
//        tvFromDate.setText( fromDate );
//
//        okayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    mDialog.dismiss();
//
//                    String date = tvFromDate.getText().toString();
//                    dateValue.setText( date );
//                    callGetPdsListForPod(  date  );
//
//                }catch (Exception e){e.printStackTrace();
//                    LogUtil.printToastMSG(mContext,"Something went wrong!");}
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        layFromDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDataPicker(tvFromDate,1);
//            }
//        });
//        mClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//
//        mDialog.show();
//    }

    final Calendar myCalendar = Calendar.getInstance();
    //set date picker view
    public void openDataPicker(AppCompatTextView datePickerField, int mFrom) {
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
                    myFormat = "yyyy/MM/dd"; //In which you need put here
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                datePickerField.setText(sdf.format(myCalendar.getTime()));

                callGetPdsListForPod( datePickerField.getText().toString().trim() );

            }

        };

        new DatePickerDialog(mContext, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void setVisibleLayout( boolean flag ){

        if( flag ){
            emptylayoutActivity.setVisibility(View.GONE);
            rvLrNumber.setVisibility(View.VISIBLE);
        }else{
            emptylayoutActivity.setVisibility(View.VISIBLE);
            rvLrNumber.setVisibility(View.GONE);
        }

    }

}
