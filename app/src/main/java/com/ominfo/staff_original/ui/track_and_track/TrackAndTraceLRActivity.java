package com.ominfo.staff_original.ui.track_and_track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.common.TouchImageView;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkAPIServices;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.NetworkURLs;
import com.ominfo.staff_original.network.RetroNetworkModule;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.dashboard.model.CrossingDetail;
import com.ominfo.staff_original.ui.dashboard.model.DeliveryDetail;
import com.ominfo.staff_original.ui.dashboard.model.TrackAndTraceLRResult;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.track_and_track.adapters.CrossingAdapter;
import com.ominfo.staff_original.ui.track_and_track.adapters.DeliveryAdapter;
import com.ominfo.staff_original.ui.upload_pod.model.GetPdsGcListForPodResult;
import com.ominfo.staff_original.ui.upload_pod.model.PodSaveOfLRViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodResponse;
import com.ominfo.staff_original.ui.upload_pod.model.VehicleDetailsLrImage;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.RealPathUtils;
import com.ominfo.staff_original.util.Util;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TrackAndTraceLRActivity extends BaseActivity {

    Context mContext;

    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.genDetailsConLay)
    LinearLayoutCompat genDetailsConLay;

    @BindView(R.id.crossingDetailsConLay)
    LinearLayoutCompat crossingDetailsConLay;

    @BindView(R.id.consignerDetailsConLay)
    LinearLayoutCompat consignerDetailsConLay;

    @BindView(R.id.consigneeDetailsConLay)
    LinearLayoutCompat consigneeDetailsConLay;

    @BindView(R.id.deliveryDetailsConLay)
    LinearLayoutCompat deliveryDetailsConLay;

    @BindView(R.id.podMainLay)
    LinearLayoutCompat podMainLay;


    @BindView(R.id.deliveryStatus)
    AppCompatTextView deliveryStatus;

    @BindView(R.id.lrNoValue)
    AppCompatTextView lrNoValue;

    @BindView(R.id.topLrNoValue)
    AppCompatTextView topLrNoValue;



    @BindView(R.id.lrDateValue)
    AppCompatTextView lrDateValue;

    @BindView(R.id.qtyValue)
    AppCompatTextView qtyValue;

    @BindView(R.id.bookingBranchValue)
    AppCompatTextView bookingBranchValue;

    @BindView(R.id.deliveryBranchValue)
    AppCompatTextView deliveryBranchValue;

    @BindView(R.id.deliveryLocationValue)
    AppCompatTextView deliveryLocationValue;

    @BindView(R.id.deliveryAreaValue)
    AppCompatTextView deliveryAreaValue;

    @BindView(R.id.deliveryTypeValue)
    AppCompatTextView deliveryTypeValue;

    @BindView(R.id.invoidValueValue)
    AppCompatTextView invoidValueValue;

    @BindView(R.id.eWayBillNoValue)
    AppCompatTextView eWayBillNoValue;

    @BindView(R.id.paymentTypeValue)
    AppCompatTextView paymentTypeValue;

    @BindView(R.id.totalAmountValue)
    AppCompatTextView totalAmountValue;

    @BindView(R.id.consignorNameValue)
    AppCompatTextView consignorNameValue;

    @BindView(R.id.consignorMobNoValue)
    AppCompatTextView consignorMobNoValue;


    @BindView(R.id.consigneeNameValue)
    AppCompatTextView consigneeNameValue;

    @BindView(R.id.consigneeMobNoValue)
    AppCompatTextView consigneeMobNoValue;

    @BindView(R.id.consigneeAddressValue)
    AppCompatTextView consigneeAddressValue;

    @BindView(R.id.crossingRecyclerView)
    RecyclerView crossingRecyclerView;

    @BindView(R.id.deliveryRecyclerView)
    RecyclerView deliveryRecyclerView;


    @BindView(R.id.imgCamera1)
    AppCompatImageView imgCamera1;
    @BindView(R.id.imgCamera2)
    AppCompatImageView imgCamera2;

    @BindView(R.id.changeBTN1)
    AppCompatImageView changeBTN1;
    @BindView(R.id.changeBTN2)
    AppCompatImageView changeBTN2;

    @BindView(R.id.uploadBTN)
    AppCompatButton uploadBTN;

    @Inject
    ViewModelFactory mViewModelFactory;

    private PodSaveOfLRViewModel podSaveOfLRViewModel;

    Bitmap bitmap1;
    Bitmap bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lr_details);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        getDeps().inject(this);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        setToolbar();
        getIntentDate();


    }

    private void setToolbar(){
        toolbarTitle.setText(R.string.scr_lbl_lr_details);
    }
    private void getIntentDate(){
        Intent intent = getIntent();
        if (intent != null) {
            String searchText = intent.getStringExtra("searchText");
            TrackAndTraceLRResult mTrackAndTraceResult = (TrackAndTraceLRResult) intent.getSerializableExtra("result");
            if( mTrackAndTraceResult != null ){
                setLRDetails( searchText, mTrackAndTraceResult );
            }
        }

    }


    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    ProcessCameraProvider cameraProvider;

    CameraSelector cameraSelector;

    Preview preview;

    public void enableCamera(PreviewView previewView) {

        cameraProviderFuture = ProcessCameraProvider.getInstance( mContext );

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



            } catch ( ExecutionException | InterruptedException e ) {
                System.out.println("exc : "+e);
            }
        }, ContextCompat.getMainExecutor( mContext ));

    }


    public Dialog showCameraDialog( boolean flag ) {

        final Bitmap[] bitmap = {null};

        final boolean[] isCaptured = {false};

        Dialog mDialog = new Dialog(mContext, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.activity_my_camera);
        mDialog.setCancelable(false);
        //mDialog.setCanceledOnTouchOutside(true);

        AppCompatImageView podImage1 = mDialog.findViewById(R.id.podImage1);
        FrameLayout frameLayout1 = mDialog.findViewById(R.id.container1);
        PreviewView previewView1 = mDialog.findViewById(R.id.previewView1);
        AppCompatImageView capturePhoto1 = mDialog.findViewById(R.id.capturePhoto1);
        AppCompatImageView retakePhoto1 = mDialog.findViewById(R.id.retakePhoto1);

        enableCamera( previewView1 );


        podImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullImageDialog( bitmap[0] );
            }
        });

        capturePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if( isCaptured[0] ){

                        mDialog.dismiss();
                        cameraProvider.unbindAll();

                        boolean con1 = uploadPodRequest.getPodPhoto1() != null && !uploadPodRequest.getPodPhoto1().trim().equals("");
                        boolean con2 = uploadPodRequest.getPodPhoto2() != null && !uploadPodRequest.getPodPhoto2().trim().equals("");

                        if(  con1 && con2 ){
                            uploadBTN.setVisibility( View.VISIBLE );
                        }

                    }else{

                        bitmap[0] = previewView1.getBitmap();

                        if( flag ){
                            imgCamera1.setImageBitmap( bitmap[0] );
                            uploadPodRequest.setPodPhoto1( "data:image/png;base64,"+AppUtils.convertBitmapToBas64(bitmap[0]) );
                        }else{
                            imgCamera2.setImageBitmap( bitmap[0] );
                            uploadPodRequest.setPodPhoto2( "data:image/png;base64,"+AppUtils.convertBitmapToBas64(bitmap[0]) );
                        }

                        podImage1.setImageBitmap(bitmap[0]);
                        retakePhoto1.setVisibility( View.VISIBLE );

                        isCaptured[0] = true;

                        capturePhoto1.setImageDrawable( getResources().getDrawable( R.drawable.correct ) );

                        podImage1.setVisibility(View.VISIBLE);
                        frameLayout1.setVisibility(View.GONE);

                    }

                }catch (Exception e){}
            }
        });

        retakePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if( isCaptured[0] ){

                        capturePhoto1.setImageDrawable( getResources().getDrawable( R.drawable.ic_camera_with_card ) );
                        retakePhoto1.setVisibility(View.GONE);
                        isCaptured[0] = false;
                        podImage1.setVisibility(View.GONE);
                        frameLayout1.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){}
            }
        });

        mDialog.show();

        return  mDialog;
    }


    /*
     * permission result
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    //Toast.makeText(mContext, getString(R.string.somthing_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }



    @Override
    public void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }

    UploadPodRequest uploadPodRequest = new UploadPodRequest();

    private void setLRDetails( String searchText, TrackAndTraceLRResult mTrackAndTraceResult ){

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getCurrentStatus() != null ||  !mTrackAndTraceResult.getGeneralDetails().get(0).getCurrentStatus().trim().equals("")) {

            uploadPodRequest.setId( Integer.parseInt( searchText) );

            if ( mTrackAndTraceResult.getGeneralDetails().get(0).getCurrentStatus().toUpperCase().equals("DELIVERED") ) {
                deliveryStatus.setTextColor( getResources().getColor(R.color.green_dark));
            } else{
                deliveryStatus.setTextColor(Color.BLACK);
            }

            deliveryStatus.setText(mTrackAndTraceResult.getGeneralDetails().get(0).getCurrentStatus().trim());
        }

        if( searchText != null || !searchText.equals("") ) {
            lrNoValue.setText(searchText);
            topLrNoValue.setText(searchText);
        }

        if( mTrackAndTraceResult.getGeneralDetails().get(0) != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getGCDate().trim().equals("") )
            lrDateValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getGCDate() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getParcls() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getParcls().trim().equals("") )
            qtyValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getParcls().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getBkgBranch() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getBkgBranch().trim().equals("") )
            bookingBranchValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getBkgBranch().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getDlyBranch() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getDlyBranch().trim().equals("") )
            deliveryBranchValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getDlyBranch().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getToLocation() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getToLocation().trim().equals("") )
            deliveryLocationValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getToLocation().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getDeliveryArea() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getDeliveryArea().trim().equals("") )
            deliveryAreaValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getDeliveryArea().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getDlyType() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getDlyType().trim().equals("") )
            deliveryTypeValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getDlyType().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceValue() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceValue().trim().equals("") )
            invoidValueValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceValue().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceNo() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceNo().trim().equals("") )
            eWayBillNoValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getInvoiceNo().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getPaymentType() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getPaymentType().trim().equals("") )
            paymentTypeValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getPaymentType().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getTotalGCAmount() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getTotalGCAmount().trim().equals("") )
            totalAmountValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getTotalGCAmount().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorName() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorName().trim().equals("") )
            consignorNameValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorName().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorMobileNo() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorMobileNo().trim().equals("") )
            consignorMobNoValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getConsignorMobileNo().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeName() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeName().trim().equals("") )
            consigneeNameValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeName().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeMobileNo() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeMobileNo().trim().equals("") )
            consigneeMobNoValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeMobileNo().trim() );

        if( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeAddress() != null || !mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeAddress().trim().equals("") )
            consigneeAddressValue.setText( mTrackAndTraceResult.getGeneralDetails().get(0).getConsigneeAddress().trim() );

        setAdapterForCrossingDetailsList( mTrackAndTraceResult.getCrossingDetails() );

        setAdapterForDeliveryDetailsList( mTrackAndTraceResult.getDeliveryDetails() );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String image1Url = mTrackAndTraceResult.getGeneralDetails().get(0).getPODPhoto1();
        String image2Url = mTrackAndTraceResult.getGeneralDetails().get(0).getPODPhoto2();

        new Thread(new Runnable() {
            @Override
            public void run() {

                if( image1Url != null && !image1Url.trim().equals("") ){

                    bitmap1 = getBitmapFromURL( image1Url );
                    if( bitmap1 != null){
                        imgCamera1.setImageBitmap( bitmap1 );

                        changeBTN1.setVisibility(View.GONE);
                        changeBTN2.setVisibility(View.GONE);
                        uploadBTN.setVisibility( View.GONE );

                        uploadPodRequest.setPodPhoto1( "data:image/png;base64,"+AppUtils.convertBitmapToBas64( bitmap1) );
                    }else{
                        imgCamera1.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_camera_with_card));
                    }
                }else{

                    changeBTN1.setVisibility(View.GONE);

                }

                if( image2Url != null && !image2Url.trim().equals("") ){

                    bitmap2 = getBitmapFromURL( image2Url );
                    if( bitmap2 != null){

                        imgCamera2.setImageBitmap( bitmap2 );

                        changeBTN1.setVisibility(View.GONE);
                        changeBTN2.setVisibility(View.GONE);
                        uploadBTN.setVisibility( View.GONE );

                        uploadPodRequest.setPodPhoto2( "data:image/png;base64,"+AppUtils.convertBitmapToBas64( bitmap2) );
                    }else{
                        imgCamera2.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_camera_with_card));
                    }
                }else{
                    changeBTN1.setVisibility(View.GONE);
                }

            }
        }).start();



        uploadPodRequest.setGcId( mTrackAndTraceResult.getGeneralDetails().get(0).getGCID() );
    }


//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            Log.e("src", src);
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap", "returned");
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("Exception", e.getMessage());
//            return null;
//        }
//    }

    private void setAdapterForCrossingDetailsList( List<CrossingDetail> list ){
        if (list !=null && list.size() > 0) {
            CrossingAdapter crossingAdapter = new CrossingAdapter(mContext, list );
            crossingRecyclerView.setHasFixedSize(true);
            crossingRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            crossingRecyclerView.setAdapter(crossingAdapter);
        }
    }

    private void setAdapterForDeliveryDetailsList( List<DeliveryDetail> list ){
        if ( list !=null && list.size() > 0) {
            DeliveryAdapter deliveryResult = new DeliveryAdapter(mContext, list );
            deliveryRecyclerView.setHasFixedSize(true);
            deliveryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            deliveryRecyclerView.setAdapter(deliveryResult);
        }
    }




    boolean
            genDetailsFlag=true,
            crossingDetailsFlag=false,
            consignerDetailsFlag=false,
            consigneeDetailsFlag=false,
            deliveryDetailsFlag=false,
            podFlag=false;

    //perform click actions
    @OnClick(
            {
                    R.id.imgBack,
                    R.id.genDetailsDrop,
                    R.id.crossingDetailsDrop,
                    R.id.consignerDetailsDrop,
                    R.id.consigneeDetailsDrop,
                    R.id.deliveryDetailsDrop,

                    R.id.podDrop,

                    R.id.imgCamera1,
                    R.id.imgCamera2,

                    R.id.changeBTN1,
                    R.id.changeBTN2,

                    R.id.uploadBTN

            }
    )

    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.imgBack:{
                onBackPressed();
            }

            case R.id.genDetailsDrop:{

                if( genDetailsFlag ){
                    genDetailsConLay.setVisibility( View.GONE );
                    genDetailsFlag = false;
                    setDropIcon( genDetailsFlag, id );
                }else{
                    genDetailsConLay.setVisibility( View.VISIBLE );
                    genDetailsFlag = true;
                    setDropIcon( genDetailsFlag, id );
                }
                break;
            }

            case R.id.crossingDetailsDrop: {

                if( crossingDetailsFlag ){
                    crossingDetailsConLay.setVisibility( View.GONE );
                    crossingDetailsFlag = false;
                    setDropIcon( crossingDetailsFlag, id );
                }else{
                    crossingDetailsConLay.setVisibility( View.VISIBLE );
                    crossingDetailsFlag = true;
                    setDropIcon( crossingDetailsFlag, id );
                }
                break;
            }

            case R.id.consignerDetailsDrop: {

                if( consignerDetailsFlag ){
                    consignerDetailsConLay.setVisibility( View.GONE );
                    consignerDetailsFlag = false;
                    setDropIcon( consignerDetailsFlag, id );
                }else{
                    consignerDetailsConLay.setVisibility( View.VISIBLE );
                    consignerDetailsFlag = true;
                    setDropIcon( consignerDetailsFlag, id );
                }
                break;

            }


            case R.id.consigneeDetailsDrop: {

                if( consigneeDetailsFlag ){
                    consigneeDetailsConLay.setVisibility( View.GONE );
                    consigneeDetailsFlag = false;
                    setDropIcon( consigneeDetailsFlag, id );
                }else{
                    consigneeDetailsConLay.setVisibility( View.VISIBLE );
                    consigneeDetailsFlag = true;
                    setDropIcon( consigneeDetailsFlag, id );
                }
                break;

            }

            case R.id.deliveryDetailsDrop: {

                if( deliveryDetailsFlag ){
                    deliveryDetailsConLay.setVisibility( View.GONE );
                    deliveryDetailsFlag = false;
                    setDropIcon( deliveryDetailsFlag, id );
                }else{
                    deliveryDetailsConLay.setVisibility( View.VISIBLE );
                    deliveryDetailsFlag = true;
                    setDropIcon( deliveryDetailsFlag, id );
                }
                break;

            }

            case R.id.podDrop: {

                if( podFlag ){
                    podMainLay.setVisibility( View.GONE );
                    podFlag = false;
                    setDropIcon( podFlag, id );
                }else{
                    podMainLay.setVisibility( View.VISIBLE );
                    podFlag = true;
                    setDropIcon( podFlag, id );
                }
                break;

            }


            case R.id.imgCamera1: {
                if( uploadPodRequest.getPodPhoto1() == null  ) {
                    requestPermission( true );
                }else
                    showFullImageDialog( bitmap1 );
            }break;

            case R.id.imgCamera2: {
                if( uploadPodRequest.getPodPhoto2() ==null   ) {
                    requestPermission( false );
                }else
                    showFullImageDialog( bitmap2 );
            }break;


            case R.id.changeBTN1: {
                requestPermission ( true );
            }break;

            case R.id.changeBTN2: {
                requestPermission( false );
            }break;


            case R.id.uploadBTN: {

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

                    showSuccessDialog("Upload Successfully.");

                }
            }break;

        }
    }


    public void showSuccessDialog(String msg) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_weight_submitted1);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatTextView mTextViewTitle = mDialog.findViewById(R.id.tv_dialogTitle);
        mTextViewTitle.setText(msg);
        mDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
            }
        },2000);
    }


    //show truck details popup
    public void showFullImageDialog(Bitmap imgUrl) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_doc_full_view);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        TouchImageView imgShow = mDialog.findViewById(R.id.imgShowPhoto);
        AppCompatImageView imgShare = mDialog.findViewById(R.id.imgShare);
        imgShare.setVisibility(View.GONE);
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
    }


    //request camera and storage permission
    private void requestPermission( boolean flag ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                },
                        1000);

            } else {
                showCameraDialog( flag );
            }
        } else {
            showCameraDialog( flag );
        }
    }



    private boolean isValidDetails() {

        if( uploadPodRequest.getPodPhoto1() == null || uploadPodRequest.getPodPhoto1().trim().equals("") ){
            LogUtil.printToastMSG( mContext,"Please Capture Image-1");
            return false;
        }

        if( uploadPodRequest.getPodPhoto2() == null || uploadPodRequest.getPodPhoto2().trim().equals("") ){
            LogUtil.printToastMSG( mContext,"Please Capture Image-2");
            return false;
        }

        return true;

    }

    public void setDropIcon( boolean flag, int id ){

        AppCompatImageView view = (AppCompatImageView) findViewById( id );
        if( flag ){
            view.setImageDrawable( getDrawable(R.drawable.ic_up) );
        }else{
            view.setImageDrawable( getDrawable(R.drawable.ic_down) );
        }

    }

}