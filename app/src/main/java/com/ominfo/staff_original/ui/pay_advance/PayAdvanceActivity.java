package com.ominfo.staff_original.ui.pay_advance;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ominfo.staff_original.BuildConfig;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.common.TouchImageView;
import com.ominfo.staff_original.common.customui.OtpEditText;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.pay_advance.adapter.ImagesAdapter;
import com.ominfo.staff_original.ui.pay_advance.model.FetchKataChitthiRequest;
import com.ominfo.staff_original.ui.pay_advance.model.FetchKataChitthiResponse;
import com.ominfo.staff_original.ui.pay_advance.model.FetchKataChitthiViewModel;
import com.ominfo.staff_original.ui.pay_advance.model.KataChitthiImageModel;
import com.ominfo.staff_original.ui.pay_advance.model.SaveKataChitthiRequest;
import com.ominfo.staff_original.ui.pay_advance.model.SaveKataChitthiResponse;
import com.ominfo.staff_original.ui.pay_advance.model.SaveKataChitthiViewModel;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;
import com.ominfo.staff_original.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PayAdvanceActivity extends BaseActivity {

    Context mContext;
    private static final int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri picUri;
    private String tempUri;
    final Calendar myCalendar = Calendar.getInstance();
    Bitmap mImgaeBitmap;
    private AppDatabase mDb;

    ImagesAdapter mImagesAdapter;
    List<KataChitthiImageModel> kataChitthiImageList = new ArrayList<>();

    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;


    @Inject
    ViewModelFactory mViewModelFactory;
    private FetchKataChitthiViewModel mFetchKataChitthiViewModel;
    private SaveKataChitthiViewModel mSaveKataChitthiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_advance);
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
        String mDate = SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.KATA_CHITTI_DATE, AppUtils.getCurrentDateTime_());
        setToolbar();
    }

    private void setToolbar(){
        //set toolbar title
        toolbarTitle.setText(R.string.scr_lbl_pay_advance);
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,0,R.id.imgCall);
    }

    private void injectAPI() {
        mFetchKataChitthiViewModel = ViewModelProviders.of(PayAdvanceActivity.this, mViewModelFactory).get(FetchKataChitthiViewModel.class);
        mFetchKataChitthiViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, "fetch"));

        mSaveKataChitthiViewModel = ViewModelProviders.of(PayAdvanceActivity.this, mViewModelFactory).get(SaveKataChitthiViewModel.class);
        mSaveKataChitthiViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, "save"));
    }

    //perform click actions
    @OnClick({R.id.submitButton/*, R.id.tvDateValue*/
            /*,R.id.imgShow*/})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
           /* case R.id.imgShow:
                showFullImageDialog();
                break;*/
            case R.id.submitButton:
                showInfoDialog();
                break;
        }
    }

    private void showInfoDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_get_otp);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        //appCompatButton.setVisibility(View.VISIBLE);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showOtpFromDriverDialog();
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

    private void showOtpFromDriverDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_otp_from_driver);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        OtpEditText edtOpt = mDialog.findViewById(R.id.edtOpt);
        AppCompatImageView imgErr = mDialog.findViewById(R.id.imgErr);
        AppCompatTextView tvErrMsg = mDialog.findViewById(R.id.tvErrMsg);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgErr.setVisibility(View.GONE);
                //tvErrMsg.setText(R.string.msg_otp_has_successfully);
                if(edtOpt.getText().toString().trim().equals("1234")){
                    mDialog.dismiss();
                    showSuccessDialog(getString(R.string.msg_advance_paid_success));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 700);

                }
                else
                {
                    imgErr.setVisibility(View.VISIBLE);
                    tvErrMsg.setText(R.string.msg_otp_you_have_entered);
                }
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
    public void showFullImageDialog(KataChitthiImageModel model,Bitmap imgUrl) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_doc_full_view);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        TouchImageView imgShow = mDialog.findViewById(R.id.imgShowPhoto);
        AppCompatImageView imgShare = mDialog.findViewById(R.id.imgShare);

        //imgShow.setImageBitmap(img);
        if (model.getImageType()==1) {
            File imgFile = new File(model.getImagePath());
            imgShow.setImageURI(Uri.fromFile(imgFile));
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //sendPhotoToOtherApps(Uri.fromFile(imgFile));
                        shareToInstant("Shared Kata chitthi photo",imgFile,view);
                    }catch (Exception e){}
                }
            });
        } else {
            imgShow.setImageBitmap(imgUrl);
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        openContactSupportEmail(mContext,"Share Image","",model.getImagePath());
                    }catch (Exception e){}
                }
            });
            //AppUtils.loadImage(mContext, model.getDriverHisabTitle(), imgShow, null);
        }

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

    //set date picker view
    private void openDataPicker(AppCompatTextView datePickerField) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                datePickerField.setText(sdf.format(myCalendar.getTime()));
                kataChitthiImageList.removeAll(kataChitthiImageList);
                SharedPref.getInstance(mContext).write(SharedPrefKey.KATA_CHITTI_DATE, getDate(sdf.format(myCalendar.getTime())));
            }

        };

        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    //request camera and storage permission
    private void requestPermission() {
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
                cameraIntent();
            }
        } else {
            //createFolderForLprImages();
            cameraIntent();

        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = getOutputPhotoFile();//Uri.fromFile(getOutputPhotoFile());
        //tempUri=picUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        //intent.putExtra("URI", picUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Uri getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.FILE_NAME);
        tempUri = directory.getPath();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e("getOutputPhotoFile", "Failed to create storage directory.");
                return null;
            }
        }

        Uri path;
        if (Build.VERSION.SDK_INT > 23) {
            File oldPath = new File(directory.getPath() + File.separator + "IMG_temp.jpg");
            String fileUrl = oldPath.getPath();
            if (fileUrl.substring(0, 7).matches("file://")) {
                fileUrl = fileUrl.substring(7);
            }
            File file = new File(fileUrl);

            path = FileProvider.getUriForFile(mContext, this.getPackageName() + ".provider", file);
        } else {
            path = Uri.fromFile(new File(directory.getPath() + File.separator + "IMG_temp.jpg"));
        }
        return path;
    }


    private String getDate(String strDate){
        try {
            String[] separated = strDate.split("/");
            String dd = separated[0];
            String mm = separated[1];
            String yyyy = separated[2];
            return yyyy + "-" + mm + "-" + dd;
        }catch (Exception e){
            return AppUtils.getCurrentDateTime_();
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
                        if (tag.equalsIgnoreCase("fetch")) {
                            FetchKataChitthiResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), FetchKataChitthiResponse.class);
                            if (responseModel != null /*&& responseModel.getStatus().equals("1")*/) {
                                //LogUtil.printToastMSG(KataChithiActivity.this, responseModel.getMessage());
                                if (responseModel.getResult() != null) {
                                    for (int i = 0; i < responseModel.getResult().size(); i++) {
                                        kataChitthiImageList.add(new KataChitthiImageModel(responseModel.getResult().get(i).getUrlPrefix() + responseModel.getResult().get(i).getUrls(), 0, null));
                                    }
                                }
                                //setAdapterForPuranaHisabList();
                            } else {
                                LogUtil.printToastMSG(PayAdvanceActivity.this, responseModel.getMessage());
                            }
                        }
                        if (tag.equalsIgnoreCase("save")) {
                            SaveKataChitthiResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), SaveKataChitthiResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                //LogUtil.printToastMSG(KataChithiActivity.this, responseModel.getMessage());
                                showSuccessDialog(getString(R.string.msg_weight_submitted));
                                deleteImagesFolder();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 900);
                            } else {
                                LogUtil.printToastMSG(PayAdvanceActivity.this, responseModel.getMessage());
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(PayAdvanceActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }

    private void deleteImagesFolder(){
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.FILE_NAME);
        if (myDir.exists()){ myDir.delete();}
    }


}