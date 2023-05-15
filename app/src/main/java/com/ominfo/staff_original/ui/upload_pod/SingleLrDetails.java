package com.ominfo.staff_original.ui.upload_pod;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.ominfo.staff_original.BuildConfig;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.common.TouchImageView;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.upload_pod.adapter.SingleLrAdapter;
import com.ominfo.staff_original.ui.upload_pod.model.FetchPendingPodListResult;
import com.ominfo.staff_original.ui.upload_pod.model.GetPdsGcListForPodRequest;
import com.ominfo.staff_original.ui.upload_pod.model.GetPdsGcListForPodResponse;
import com.ominfo.staff_original.ui.upload_pod.model.GetPdsGcListForPodResult;
import com.ominfo.staff_original.ui.upload_pod.model.PdsGcListForPodViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.RealPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleLrDetails extends BaseActivity implements Filterable {

    Context mContext;
    private static final int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri picUri;
    private String tempUri;
    final Calendar myCalendar = Calendar.getInstance();
    Bitmap mImgaeBitmap;
    private AppDatabase mDb;


    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;


    @BindView(R.id.pdsNoValue)
    AppCompatTextView pdsNoValue;


    @BindView(R.id.pdsDateValue)
    AppCompatTextView pdsDateValue;


    @BindView(R.id.vehicleNoValue)
    AppCompatTextView vehicleNoValue;

    @BindView(R.id.driverNameValue)
    AppCompatTextView driverNameValue;

    @BindView(  R.id.imgCall)
    AppCompatImageView imgCall;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_emptyLayTitle)
    AppCompatTextView tv_emptyLayTitle;

    @BindView(R.id.empty_layoutActivity)
    LinearLayoutCompat emptylayoutActivity;

    @BindView(R.id.tvSearchView)
    AppCompatEditText tvSearchView;

    @BindView(R.id.clearSearchBTN)
    AppCompatImageView clearSearchBTN;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    ViewModelFactory mViewModelFactory;

    private PdsGcListForPodViewModel pdsGcListForPodViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_lr_details);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        getDeps().inject(this);
        ButterKnife.bind(this);

        init();

        injectAPI();

        getIntentData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getIntentData();
            }
        });


        tvSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println( s );
                try {

                    if( s.length() == 0 )
                        exampleFilter.filter( null );
                    else
                        exampleFilter.filter( s );

                    if( s.toString().trim().length() != 0 ){
                        clearSearchBTN.setVisibility(View.VISIBLE);
                    }
                    else
                        clearSearchBTN.setVisibility(View.GONE);

                }catch ( Exception e){ System.out.println("aa" +e);}


            }
        });

        clearSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    tvSearchView.setText("");
                    clearSearchBTN.setVisibility(View.GONE);
                    exampleFilter.filter( null );
                }catch ( Exception e){ System.out.println("aa" +e);}
            }
        });

    }

    SingleLrAdapter singleLrAdapter;
    private List<GetPdsGcListForPodResult> mListData;
    private List<GetPdsGcListForPodResult> exampleListFull;

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            System.out.println("aa "+constraint);

            List<GetPdsGcListForPodResult> filteredList = new ArrayList<>();

            boolean driverFlag = constraint == null || constraint.length() == 0;

            if ( driverFlag ) {
                filteredList.addAll(exampleListFull);
                System.out.println("alll");
            } else {
                System.out.println("aasingle");
                String lrNo = constraint.toString().toLowerCase().trim();

                for (GetPdsGcListForPodResult item : exampleListFull) {
                    System.out.println("aa item ");
                    if ( item.getGCNo().toLowerCase().contains(lrNo) ) {
                        filteredList.add(item);
                        LogUtil.printLog("test","pds match" );
                    }

                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }  @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {

                mListData.clear();

                mListData.addAll( (List) results.values);

                singleLrAdapter.notifyDataSetChanged();

            }catch (Exception e){
                System.out.println("peaa "+e);
            }
        }
    };



    private void getIntentData(){

        Intent intent = getIntent();
        FetchPendingPodListResult data = (FetchPendingPodListResult) intent.getSerializableExtra("data");

        if( data.getPDSNo() != null || data.getPDSNo().length() != 0 )
            pdsNoValue.setText( data.getPDSNo() );

        if( data.getPDSDate() != null || data.getPDSDate().length() != 0 )
            pdsDateValue.setText( data.getPDSDate() );

        if( data.getVehicleNo() != null || data.getVehicleNo().length() != 0 )
            vehicleNoValue.setText( data.getVehicleNo() );

        if( data.getTempoDriver() != null || data.getTempoDriver().length() != 0 )
            driverNameValue.setText( data.getTempoDriver() );

        callSinglePodAPI( data );

    }

    private void callSinglePodAPI(FetchPendingPodListResult data) {

        if (NetworkCheck.isInternetAvailable( getApplicationContext() )) {

            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();

            if (loginResultTable != null) {

                GetPdsGcListForPodRequest getPdsGcListForPodRequest = new GetPdsGcListForPodRequest();

                getPdsGcListForPodRequest.setUserkey(loginResultTable.getUserKey());
                getPdsGcListForPodRequest.setPDSID(data.getPDSID() );

                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(getPdsGcListForPodRequest);

                pdsGcListForPodViewModel.hitPdsListForPodApi(bodyInStringFormat);

            }

        } else {
            LogUtil.printToastMSG(getApplicationContext() , getString(R.string.err_msg_connection_was_refused));
        }

    }

    private void init() {

        mDb = BaseApplication.getInstance(mContext).getAppDatabase();
        setToolbar();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void setToolbar(){
        //set toolbar title
        imgCall.setVisibility(View.INVISIBLE);
        toolbarTitle.setText("LR of PDS for POD");
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,0,R.id.imgCall);
    }

    private void injectAPI() {

        pdsGcListForPodViewModel = ViewModelProviders.of(SingleLrDetails.this, mViewModelFactory).get(PdsGcListForPodViewModel.class);
        pdsGcListForPodViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.GET_PDS_GC_LIST_FOR_POD ) );

    }



    //show truck details popup
    public void showFullImageDialog(Bitmap imgUrl , String gnNo) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_doc_full_view);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        TouchImageView imgShow = mDialog.findViewById(R.id.imgShowPhoto);
        AppCompatImageView imgShare = mDialog.findViewById(R.id.imgShare);
        imgShare.setVisibility(View.VISIBLE);
        imgShow.setImageBitmap(imgUrl);

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

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imgUrl.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), imgUrl, "Title", null);
                    String actualPath = RealPathUtils.getActualPath(mContext, Uri.parse(path));
                    File imgFile = new File(actualPath);
                    shareToInstant("Shared Lr No "+gnNo+" photo", imgFile, view);
                } catch (Exception e) {
                    System.out.println("share ex "+ e);
                }
            }
        });

        mDialog.show();
    }



    private static void shareToInstant(String content, File imageFile, View view) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        sharingIntent.setType("text/plain");
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content);

        try {
            view.getContext().startActivity(Intent.createChooser(sharingIntent, "Share it Via"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(view.getContext(), R.string.err_msg_connection_was_refused, Toast.LENGTH_SHORT).show();
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
                    try {

                        if (tag.equalsIgnoreCase(DynamicAPIPath.GET_PDS_GC_LIST_FOR_POD)) {

                            GetPdsGcListForPodResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), GetPdsGcListForPodResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1") ) {

                                if (responseModel.getResult() != null) {

                                    List<GetPdsGcListForPodResult> getPdsGcListForPodResults = responseModel.getResult();

                                    List<UploadPodRequest> uploadPodRequests = mDb.getDbDAO().getPdsList();


                                    int outer=0;
                                    int inner=0;
                                    for( GetPdsGcListForPodResult getPdsGcListForPodResult1 : getPdsGcListForPodResults ){

                                        System.out.println("outer "+ outer++);
                                        System.out.println("outer "+ outer + " inner "+inner);

                                        for( UploadPodRequest uploadPodRequest : uploadPodRequests ){
                                            inner++;
                                            if( getPdsGcListForPodResult1.getGCNo().equalsIgnoreCase( uploadPodRequest.getGcNo() ) ){

                                                getPdsGcListForPodResult1.setInPending( true );

//                                                getPdsGcListForPodResult1.setPODPhoto1( uploadPodRequest.getPodPhoto1() );
//                                                getPdsGcListForPodResult1.setPODPhoto2( uploadPodRequest.getPodPhoto2() );

                                                System.out.println("inner break");
                                                break;
                                            }
                                        }

                                    }

                                    exampleListFull = new ArrayList<>(getPdsGcListForPodResults);
                                    this.mListData = getPdsGcListForPodResults;

                                }

                                setAdapterForLrImageList();

                            } else {
                                LogUtil.printToastMSG(SingleLrDetails.this, responseModel.getMessage());
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(SingleLrDetails.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }

    private void setAdapterForLrImageList() {

        if(mListData.size()==0) {
            tv_emptyLayTitle.setText( getString(R.string.scr_lbl_no_data_available ) );
            setVisibleLayout( false );
            return;
        }else
            setVisibleLayout( true );

        final Dialog[] mDialog = new Dialog[1];

        singleLrAdapter= new SingleLrAdapter( mContext , mListData,
                new SingleLrAdapter.ItemClickListener() {
                    @Override
                    public void onClick(GetPdsGcListForPodResult data) {

                        if( mDialog[0] != null ){
                            mDialog[0].dismiss();
                        }

                        if( cameraProvider != null )
                            cameraProvider.unbindAll();

                        LogUtil.printToastMSG(getApplicationContext(),"Please wait");
                        mDialog[0] = showCameraDialog ( data );
                    }
                });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(singleLrAdapter);
    }



    //request camera and storage permission
    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (
                    mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                },
                        1000);

            }
        }
    }






    UploadPodRequest uploadPodRequest = new UploadPodRequest();
    public Dialog showCameraDialog(GetPdsGcListForPodResult data) {

        final boolean[] isOnGoing1Camera = {false};
        final boolean[] isOnGoing2Camera = {true};

        final Bitmap[] bitmap1 = new Bitmap[1];
        final Bitmap[] bitmap2 = new Bitmap[1];

        uploadPodRequest.setPodPhoto1( null );
        uploadPodRequest.setPodPhoto2( null );
        uploadPodRequest.setGcId( data.getGCID() );


        String[] permitionName = {Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERACT_ACROSS_PROFILES};


        ActivityCompat.requestPermissions(this, permitionName, 1);
        requestPermission();

        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_for_camera_layout);
        mDialog.setCancelable(false);
        //mDialog.setCanceledOnTouchOutside(true);

        AppCompatTextView lrNoValue = mDialog.findViewById(R.id.lrNoValue);
        if( data.getGCNo() !=null && !data.getGCNo().trim().equals("") ) {
            lrNoValue.setText(data.getGCNo());
            uploadPodRequest.setGcNo( data.getGCNo());
            uploadPodRequest.setId( Integer.parseInt( data.getGCID() ));
        }


        AppCompatImageView podImage1 = mDialog.findViewById(R.id.podImage1);
        FrameLayout frameLayout1 = mDialog.findViewById(R.id.container1);
        PreviewView previewView1 = mDialog.findViewById(R.id.previewView1);
        ProgressBar progress1 = mDialog.findViewById(R.id.progress1);
        AppCompatImageView capturePhoto1 = mDialog.findViewById(R.id.capturePhoto1);
        AppCompatImageView retakePhoto1 = mDialog.findViewById(R.id.retakePhoto1);

        AppCompatImageView podImage2 = mDialog.findViewById(R.id.podImage2);
        FrameLayout frameLayout2 = mDialog.findViewById(R.id.container2);
        PreviewView previewView2 = mDialog.findViewById(R.id.previewView2);
        ProgressBar progress2 = mDialog.findViewById(R.id.progress2);
        AppCompatImageView capturePhoto2 = mDialog.findViewById(R.id.capturePhoto2);
        AppCompatImageView retakePhoto2 = mDialog.findViewById(R.id.retakePhoto2);

        enableCamera( previewView1 );

        try{
            String image1Url = data.getPODPhoto1() == null ? "no" : data.getPODPhoto1().toString();
            if( !image1Url.equals("no") ){

                bitmap1[0] = getBitmapFromURL( image1Url );

                if( bitmap1[0] != null){

                    podImage1.setImageBitmap(bitmap1[0]);
                    setVisiblityLayout( true, frameLayout1,podImage1 );
                    retakePhoto1.setVisibility(View.VISIBLE);
                    uploadPodRequest.setPodPhoto1( "data:image/png;base64,"+ AppUtils.convertBitmapToBas64(bitmap1[0]) );

                }else{
                    setVisiblityLayout( false, frameLayout1,podImage1 );
                    capturePhoto1.setVisibility(View.VISIBLE);
                    retakePhoto1.setVisibility(View.VISIBLE);

                    isOnGoing1Camera[0] = true;
                }
            }else{
                retakePhoto1.setVisibility(View.GONE);
                capturePhoto1.setVisibility(View.VISIBLE);
                setVisiblityLayout( false, frameLayout1,podImage1 );
            }
        }catch (Exception e){}

        try{
            String image2Url = data.getPODPhoto2() == null ? "no" : data.getPODPhoto2().toString();
            if( !image2Url.equals("no") ){
                bitmap2[0] = getBitmapFromURL( image2Url );

                if( bitmap2[0] != null){
                    isOnGoing2Camera[0] = false;
                    podImage2.setImageBitmap(bitmap2[0]);
                    setVisiblityLayout( true, frameLayout2,podImage2 );
                    retakePhoto2.setVisibility(View.VISIBLE);
                    uploadPodRequest.setPodPhoto2( "data:image/png;base64,"+ AppUtils.convertBitmapToBas64(bitmap2[0]) );
                }else{
                    capturePhoto2.setVisibility(View.VISIBLE);
                    retakePhoto2.setVisibility(View.VISIBLE);
                }

            }else{
                retakePhoto2.setVisibility(View.GONE);
                capturePhoto2.setVisibility(View.VISIBLE);
                setVisiblityLayout( false, frameLayout2,podImage2 );

                if( !isOnGoing1Camera[0]){
                    isOnGoing1Camera[0] = true;
                }

            }
        }catch (Exception e){}

        podImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullImageDialog(bitmap1[0],data.getGCNo() );
            }
        });

        capturePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(isOnGoing1Camera[0]){

                        bitmap1[0] = previewView1.getBitmap();
                        uploadPodRequest.setPodPhoto1( "data:image/png;base64,"+AppUtils.convertBitmapToBas64(bitmap1[0]) );
                        podImage1.setImageBitmap(bitmap1[0]);
                        retakePhoto1.setVisibility( View.VISIBLE );

                        setVisiblityLayout( true, frameLayout1,podImage1);
                        cameraProvider.unbindAll();

                        if(isOnGoing2Camera[0]){
                            isOnGoing2Camera[0] = true;
                            preview = new Preview.Builder().build();
                            preview.setSurfaceProvider( previewView2.getSurfaceProvider() );
                            preview.setTargetRotation(Surface.ROTATION_0);
                            cameraProvider.bindToLifecycle((LifecycleOwner)mContext, cameraSelector, preview);

                        }

                        isOnGoing1Camera[0] = false;

                    }else{
                        LogUtil.printToastMSG(getApplicationContext(),"Please Re-take Picture");
                    }
                }catch (Exception e){}




            }
        });

        retakePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    retakePhoto2.setVisibility( View.VISIBLE );
                    isOnGoing2Camera[0] = false;
                    cameraProvider.unbindAll();

                    isOnGoing1Camera[0] = true;
                    try {

                        preview = new Preview.Builder().build();
                        preview.setSurfaceProvider( previewView1.getSurfaceProvider() );
                        preview.setTargetRotation(Surface.ROTATION_0);
                        cameraProvider.bindToLifecycle((LifecycleOwner)mContext, cameraSelector, preview);

                    }catch (Exception e){
                        LogUtil.printLog("test",e);
                    }

                    retakePhoto1.setVisibility( View.GONE );
                    setVisiblityLayout( false, frameLayout1,podImage1);

                }catch (Exception e){}


            }
        });




        podImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullImageDialog(bitmap2[0], data.getGCNo() );
            }
        });

        capturePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           try {
               if(isOnGoing2Camera[0]){

                   bitmap2[0] = previewView2.getBitmap();
                   uploadPodRequest.setPodPhoto2( "data:image/png;base64,"+AppUtils.convertBitmapToBas64(bitmap2[0]) );
                   podImage2.setImageBitmap(bitmap2[0]);
                   retakePhoto2.setVisibility( View.VISIBLE );

                   setVisiblityLayout( true, frameLayout2,podImage2);

                   isOnGoing2Camera[0] = false;
                   cameraProvider.unbindAll();

               }else{
                   LogUtil.printToastMSG(getApplicationContext(),"Please Re-take Picture");
               }
           }catch (Exception e){}

            }
        });

        retakePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    retakePhoto1.setVisibility( View.VISIBLE);
                    isOnGoing1Camera[0] = false;
                    cameraProvider.unbindAll();

                    isOnGoing2Camera[0] = true;

                    try {

                        preview = new Preview.Builder().build();
                        preview.setSurfaceProvider( previewView2.getSurfaceProvider() );
                        preview.setTargetRotation(Surface.ROTATION_0);
                        cameraProvider.bindToLifecycle((LifecycleOwner)mContext, cameraSelector, preview);

                    }catch (Exception e){
                        LogUtil.printLog("test",e);
                    }

                    retakePhoto2.setVisibility( View.GONE );
                    setVisiblityLayout( false, frameLayout2,podImage2);

                }catch (Exception e){}
            }
        });


        AppCompatButton submitBTN = mDialog.findViewById(R.id.submitButton);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( isValidDetails () ){


                    AppDatabase mDb = BaseApplication.getInstance().getAppDatabase();

                    LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();

                    if (loginResultTable != null) {

                        uploadPodRequest.setUserkey(loginResultTable.getUserKey());
                        uploadPodRequest.setUserId( Long.parseLong( loginResultTable.getUserID()) );

                        String date = AppUtils.getCurrDate();
                        String time = AppUtils.getCurrentTime1();

                        uploadPodRequest.setUploadDate( date +" | "+time);

                        mDb.getDbDAO().insertOrUpdate( uploadPodRequest );

                    }
                    data.setInPending( true );
                    showSuccessDialog("Upload Successfully.",mDialog);
                    singleLrAdapter.notifyDataSetChanged();

                }

            }
        });

        mDialog.show();

        AppCompatImageView imgCancel = mDialog.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraProvider.unbindAll();
                mDialog.dismiss();
            }
        });

        return  mDialog;
    }


    private boolean isValidDetails() {

        boolean condition1 = uploadPodRequest.getPodPhoto1() == null || uploadPodRequest.getPodPhoto1().trim().equals("");
        boolean condition2 = uploadPodRequest.getPodPhoto2() == null || uploadPodRequest.getPodPhoto2().trim().equals("");


        if(  condition1 && condition2 ){
            LogUtil.printToastMSG( getApplicationContext(),"Single picture is mandatory");
            return false;
        }

        if( condition1 ){
            uploadPodRequest.setPodPhoto1("data:image/png;base64,");
        }

        if( condition2){
            uploadPodRequest.setPodPhoto2("data:image/png;base64,");
        }

        return true;

    }


    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    ProcessCameraProvider cameraProvider;

    CameraSelector cameraSelector;

    Preview preview;

    public void enableCamera(PreviewView previewView) {

        cameraProviderFuture = ProcessCameraProvider.getInstance( getApplicationContext() );

        cameraProviderFuture.addListener(() -> {
            try {

                cameraProvider = cameraProviderFuture.get();

                cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview = new Preview.Builder().build();
                preview.setSurfaceProvider( previewView.getSurfaceProvider() );
                preview.setTargetRotation(Surface.ROTATION_0);

                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)mContext, cameraSelector, preview);

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1000, 500)).build();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor( getApplicationContext()), new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        System.out.println("camera .........." + image );
                    }
                });

            } catch ( ExecutionException | InterruptedException e ) {
                System.out.println("exc : "+e);
            }
        }, ContextCompat.getMainExecutor( getApplicationContext() ));

    }





    public void setVisiblityLayout( boolean flag, FrameLayout frameLayout, AppCompatImageView appCompatImageView){

        if( flag ){
            frameLayout.setVisibility(View.GONE);
            appCompatImageView.setVisibility(View.VISIBLE);
        }else{
            frameLayout.setVisibility(View.VISIBLE);
            appCompatImageView.setVisibility(View.GONE);
        }
    }




    public void setVisibleLayout( boolean flag ){

        if( flag ){
            emptylayoutActivity.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            emptylayoutActivity.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


    public void showSuccessDialog(String msg, Dialog d) {

        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_weight_submitted);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        AppCompatTextView mTextViewTitle = mDialog.findViewById(R.id.tv_dialogTitle);
        AppCompatButton button = mDialog.findViewById(R.id.okayButton);
        AppCompatImageView errorImg = mDialog.findViewById(R.id.errorImg);
        mTextViewTitle.setText(msg);
        button.setVisibility(View.GONE);
        errorImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_check));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                cameraProvider.unbindAll();
                d.dismiss();
                mDialog.dismiss();

            }
        },2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }

    private void deleteImagesFolder(){
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.FILE_NAME);
        if (myDir.exists()){ myDir.delete();}
    }


}