package com.ominfo.staff_original.ui.kata_chithi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ominfo.staff_original.BuildConfig;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.common.TouchImageView;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.kata_chithi.adapter.AutoSuggestAdapter;
import com.ominfo.staff_original.ui.kata_chithi.adapter.ImagesAdapter;
import com.ominfo.staff_original.ui.kata_chithi.model.Array6;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiRequest;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.FetchKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.KataChitthiImageModel;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiRequest;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.SaveKataChitthiViewModel;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleRequest;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleResult;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleViewModel;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.RealPathUtils;
import com.ominfo.staff_original.util.SharedPref;
import com.ominfo.staff_original.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class KataChithiActivityBkup extends BaseActivity {

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

    @BindView(R.id.tvDateValueTo)
    AppCompatTextView tvDateValue;

    @BindView(R.id.getDetailsButton)
    AppCompatButton getDetailsButton;

    @BindView(R.id.AutoComTextViewVehNo)
    AppCompatAutoCompleteTextView AutoComTextViewVehNo;

    @BindView(R.id.etWeight)
    AppCompatEditText etWeight;

    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.tvBranchName)
    AppCompatTextView tvBranchName;

    @BindView(R.id.rvImages)
    RecyclerView recyclerViewImages;

    @BindView(R.id.imgNoImage)
    CardView imageViewNoImage;
    private int downloaded = 0, downloadedCount = 0;
    List<VehicleResult> vehicleNoDropdown = new ArrayList<>();
    VehicleResult mSelectedVehicle = new VehicleResult();
    @Inject
    ViewModelFactory mViewModelFactory;
    private FetchKataChitthiViewModel mFetchKataChitthiViewModel;
    private SaveKataChitthiViewModel mSaveKataChitthiViewModel;
    private VehicleViewModel vehicleViewModel;
    int cam = 0;
    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kata_chithi);
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
        etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    if (b) {
                        if (etWeight.getText().toString().trim().equals(".00")) {
                            etWeight.setText("");
                        }
                    } else {
                        if (etWeight.getText().toString().trim().equals(".00")) {
                            etWeight.setText(".00");
                        }
                        String currentString = etWeight.getText().toString().trim();
                        String[] separated = currentString.split("\\.");
                        if (separated.length == 1) {
                            etWeight.append(".00");
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        etWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // Your action on done
                    try{
                    AppUtils.hideKeyBoard(KataChithiActivityBkup.this);
                    String currentString = etWeight.getText().toString().trim();
                    String[] separated = currentString. split("\\.");
                    if(separated.length==1){
                        etWeight.append(".00");
                    }
                    if(separated.length>1){
                        if(separated[1]!=null){
                            etWeight.setText(currentString);
                        }
                    }
                    }catch (Exception e){e.printStackTrace();}
                    return true;
                }
                return false;
            }
        });
        //tvDateValue.setEnabled(false);
        etWeight.setEnabled(false);
        etWeight.setText(".00");
        String mDate = AppUtils.getCurrentDateTime_();//SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.KATA_CHITTI_DATE, AppUtils.getCurrentDateTime_());
        tvDateValue.setText(AppUtils.getconvertedKataData(mDate));
        cam=2;
        requestPermission();
        //mSelectedVehicle = null;
        callVehicleApi();
        getDetailsButton.setBackground(mContext.getResources().getDrawable(
                R.drawable.bg_button_round_corner_grey
        ));
        getDetailsButton.setTextColor(
                mContext.getResources().getColor(R.color.white)
        );

    }

    private void setToolbar() {
        //set toolbar title
        toolbarTitle.setText("Kanta Chitthi");
        initToolbar(1, mContext, R.id.imgBack, R.id.imgReport, R.id.imgNotify, R.id.imgLogout, R.id.imgCall);
    }

    private void reqPermissionCode(){
        if(cam==0) {
            cameraIntent();
        }else if(cam==1) {
            galleryIntent();
        }else if(cam==2) {
            //deleteImagesFolder();
        }
    }

    /*private void deleteDir(){
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.FILE_NAME);
        //File oldFile = new File(myDir);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }*/

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    //set value to vehicle colour dropdown
    private void setDropdownVehNo() {
        try {
            List<String> list = new ArrayList<>();
            if (vehicleNoDropdown != null && vehicleNoDropdown.size() > 0) {
                String[] mDropdownList = new String[vehicleNoDropdown.size()];
                for (int i = 0; i < vehicleNoDropdown.size(); i++) {
                    list.add(String.valueOf(vehicleNoDropdown.get(i).getVehicleNo()));
                    mDropdownList[i] = String.valueOf(vehicleNoDropdown.get(i).getVehicleNo());
                   /* if (!VehiIdDropdown.equals("")) {
                        if (vehicleNoDropdown.get(i).getId().equals(VehiIdDropdown)) {
                            pos = i;
                        }
                    }*/
                }
                //AutoComTextViewVehNo.setText(mDropdownList[pos]);
                AutoSuggestAdapter adapter = new AutoSuggestAdapter(this,
                        android.R.layout.simple_list_item_1, list);
/*
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        R.layout.row_dropdown_item,
                        mDropdownList);*/
                //AutoComTextViewVehNo.setThreshold(10);
                AutoComTextViewVehNo.setAdapter(adapter);
                AutoComTextViewVehNo.setThreshold(1);
                //AutoComTextViewVehNo.showDropDown();
                //mSelectedColor = mDropdownList[pos];
                AutoComTextViewVehNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int pos = 0;
                        for (int i = 0; i < vehicleNoDropdown.size(); i++) {
                           if (vehicleNoDropdown.get(i).getVehicleNo().equals(AutoComTextViewVehNo.getText().toString())) {
                               pos = i;
                         }
                        }
                        Handler handler = new Handler();
                        Runnable runnable = null;
                        int delay = 100;
                        Runnable finalRunnable = runnable;
                        int finalPos = pos;
                        handler.postDelayed(runnable = new Runnable() {
                            public void run() {
                                mSelectedVehicle = vehicleNoDropdown.get(finalPos);
                                //LogUtil.printToastMSG(mContext,mSelectedVehicle.getDriverName());
                                AppUtils.hideKeyBoard(KataChithiActivityBkup.this);
                                kataChitthiImageList.removeAll(kataChitthiImageList);
                                tvBranchName.setText("");
                                //tvDateValue.setText("");
                                etWeight.setText(".00");
                                tvBranchName.setText(mSelectedVehicle.getDriverName());
                                getDetailsButton.setBackground(null);
                                if (mSelectedVehicle.getDriverID().equals("0")
                                        || mSelectedVehicle.getDriverName().equals("")
                                        ||mSelectedVehicle.getDriverID()==null
                                        ||mSelectedVehicle.getDriverName()==null)
                                {
                                    getDetailsButton.setBackground(mContext.getResources().getDrawable(
                                            R.drawable.bg_button_round_corner_grey
                                    ));
                                    getDetailsButton.setTextColor(
                                            mContext.getResources().getColor(R.color.white)
                                    );
                                    //tvDateValue.setEnabled(false);
                                    tvBranchName.setText(R.string.scr_lbl_no_driver);
                                }
                                else {
                                    getDetailsButton.setBackground(mContext.getResources().getDrawable(
                                            R.drawable.bg_button_round_corner_blue
                                    ));
                                    getDetailsButton.setTextColor(
                                            mContext.getResources().getColor(R.color.white)
                                    );
                                    //tvDateValue.setEnabled(true);
                                    kataChitthiImageList.removeAll(kataChitthiImageList);
                                    setAdapterForPuranaHisabList();
                                }
                            }
                        }, delay);

                    }

                });

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectAPI() {
        mFetchKataChitthiViewModel = ViewModelProviders.of(KataChithiActivityBkup.this, mViewModelFactory).get(FetchKataChitthiViewModel.class);
        mFetchKataChitthiViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, "fetch"));

        mSaveKataChitthiViewModel = ViewModelProviders.of(KataChithiActivityBkup.this, mViewModelFactory).get(SaveKataChitthiViewModel.class);
        mSaveKataChitthiViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, "save"));

        vehicleViewModel = ViewModelProviders.of(KataChithiActivityBkup.this, mViewModelFactory).get(VehicleViewModel.class);
        vehicleViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_VEHICLE));
    }

    //perform click actions
    @OnClick({R.id.submitButton, R.id.imgCapture, R.id.tvDateValueTo
            ,R.id.getDetailsButton})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getDetailsButton:
                //on button
                try {
                    if (mSelectedVehicle != null) {
                        if (mSelectedVehicle.getDriverID() == null
                                || mSelectedVehicle.getDriverName() == null
                        ||mSelectedVehicle.getDriverID().equals("0")
                                || mSelectedVehicle.getDriverName().equals("")
                                ) {
                        } else {
                            //tvDateValue.setEnabled(true);
                            callFetchKataChitthiApi();
                            setAdapterForPuranaHisabList();
                            etWeight.setEnabled(true);
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
                break;
            case R.id.submitButton:
                try{
                    if (mSelectedVehicle != null) {
                        if (mSelectedVehicle.getDriverID() == null
                                || mSelectedVehicle.getDriverName() == null
                                || mSelectedVehicle.getDriverID().equals("0")
                                || mSelectedVehicle.getDriverName().equals("")
                                ) {
                            LogUtil.printToastMSG(mContext, "Sorry, No driver available!");
                        } else {
                            callSaveKataChitthiApi();
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
                break;
            case R.id.imgCapture:
                showChooseCameraDialog();
                break;
            case R.id.tvDateValueTo:
                openDataPicker(tvDateValue);
                break;
        }
    }

    //show truck details popup
    public void showFullImageDialog(KataChitthiImageModel model, Bitmap imgUrl) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_doc_full_view);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        TouchImageView imgShow = mDialog.findViewById(R.id.imgShowPhoto);
        AppCompatImageView imgShare = mDialog.findViewById(R.id.imgShare);
        //imgShow.setImageBitmap(img);
        // if (model.getImageType()==1) {
        try {
            File imgFile = new File(model.getImagePath());
            try{imgShow.setImageBitmap(imgUrl);}catch (Exception e){e.printStackTrace();}
            if (imgFile != null && !imgFile.equals("") && !imgFile.equals("null")) {
                imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //sendPhotoToOtherApps(Uri.fromFile(imgFile));
                            shareToInstant("Shared kata chitthi photo", imgFile, view);
                        } catch (Exception e) {
                        }
                    }
                });
            } else {
                LogUtil.printToastMSG(mContext, "Sorry, Image not available!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.printToastMSG(mContext, "Sorry, Image not available!");
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
                setAdapterForPuranaHisabList();
                SharedPref.getInstance(mContext).write(SharedPrefKey.KATA_CHITTI_DATE, getDate(sdf.format(myCalendar.getTime())));
                //callFetchKataChitthiApi();
            }

        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        String dateRestrict = AppUtils.getCurrentDateTime();
        dpDialog.getDatePicker().setMaxDate(AppUtils.getChangeDate(dateRestrict));

        dpDialog.show();

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
                reqPermissionCode();
            }
        } else {
            reqPermissionCode();
        }
    }

    private void showChooseCameraDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_select_image);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatTextView tvChooseFromCamera = mDialog.findViewById(R.id.tvChooseFromCamera);
        AppCompatTextView tvCameraImage = mDialog.findViewById(R.id.tvCameraImage);
        tvChooseFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cam = 1;
                requestPermission();
            }
        });
        tvCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cam = 0;
                requestPermission();
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

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = getOutputPhotoFile();//Uri.fromFile(getOutputPhotoFile());
        //tempUri=picUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, picUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        //intent.putExtra("URI", picUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Uri getOutputPhotoFile() {
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    /* Call Api to fetch kata chitthi */
    private void callFetchKataChitthiApi() {
        kataChitthiImageList.removeAll(kataChitthiImageList);
        if (NetworkCheck.isInternetAvailable(KataChithiActivityBkup.this)) {
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            //DashboardResult dashboardResult = mDb.getDbDAO().getVehicleDetails();
            if (loginResultTable != null) {
                //String mDate = AppUtils.getCurrentDateTime_();//SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.KATA_CHITTI_DATE, AppUtils.getCurrentDateTime_());
                FetchKataChitthiRequest mLoginRequest = new FetchKataChitthiRequest();
                mLoginRequest.setDriverID(mSelectedVehicle.getDriverID());//loginResultTable.getDriverId()); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
                mLoginRequest.setUserkey(loginResultTable.getUserKey());
                mLoginRequest.setVehicleID(mSelectedVehicle.getVehicleID());//dashboardResult.getArray5().get(0).getVehicleID());
                mLoginRequest.setTransactionDate(getDate(tvDateValue.getText().toString()));
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(mLoginRequest);
                LogUtil.printLog("request fetch", bodyInStringFormat);
                mFetchKataChitthiViewModel.hitGetKataChitthiApi(bodyInStringFormat);
            }
        } else {
            LogUtil.printToastMSG(KataChithiActivityBkup.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /* Call Api to fetch kata chitthi */
    private void callVehicleApi() {
        if (NetworkCheck.isInternetAvailable(KataChithiActivityBkup.this)) {
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            //DashboardResult dashboardResult = mDb.getDbDAO().getVehicleDetails();
            if (loginResultTable != null) {
                VehicleRequest mLoginRequest = new VehicleRequest();
                mLoginRequest.setUserkey(loginResultTable.getUserKey());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(mLoginRequest);
                LogUtil.printLog("request fetch", bodyInStringFormat);
                vehicleViewModel.hitVehicleApi(bodyInStringFormat);
            }
        } else {
            LogUtil.printToastMSG(KataChithiActivityBkup.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /* Call Api to save kata chitthi */
    private void callSaveKataChitthiApi() {
        if (NetworkCheck.isInternetAvailable(KataChithiActivityBkup.this)) {
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            List<String> mImageList = new ArrayList<>();
            for (int i = 0; i < kataChitthiImageList.size(); i++) {
                if (kataChitthiImageList.get(i).getImageType() == 1) {
                    String mBase64 = AppUtils.getBase64images(kataChitthiImageList.get(i).getImagePath());
                    mImageList.add("data:image/png;base64," + mBase64);
                }
            }
            if (mImageList.size() > 0) {
                if (loginResultTable != null) {
                    //DashboardResult dashboardResult = mDb.getDbDAO().getVehicleDetails();
                    SaveKataChitthiRequest mLoginRequest = new SaveKataChitthiRequest();
                    mLoginRequest.setDriverID(Long.parseLong(mSelectedVehicle.getDriverID())); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
                    mLoginRequest.setUserkey(loginResultTable.getUserKey());
                    mLoginRequest.setVehicleID(Long.parseLong(mSelectedVehicle.getVehicleID()));//dashboardResult.getArray5().get(0).getVehicleID()));
                    mLoginRequest.setTransactionDate(getDate(tvDateValue.getText().toString()));
                    mLoginRequest.setUserID(Long.parseLong(loginResultTable.getUserID()));
                    mLoginRequest.setTransactionID(Long.parseLong("0"));
                    mLoginRequest.setWeight(etWeight.getText().toString().trim());
                    mLoginRequest.setPhotoXml(mImageList);
                    Gson gson = new Gson();
                    String bodyInStringFormat = gson.toJson(mLoginRequest);
                    LogUtil.printLog("save_kata",bodyInStringFormat);
                    RequestBody mRequestBodyType = RequestBody.create(MediaType.parse("text/plain"), "saveKantaChitthi");
                    RequestBody mRequestBodyTypeImage = RequestBody.create(MediaType.parse("text/plain"), bodyInStringFormat);
                    LogUtil.printLog("request save", bodyInStringFormat);
                    mSaveKataChitthiViewModel.hitSaveKataChitthi(mRequestBodyType, mRequestBodyTypeImage);
                }
            } else {
                LogUtil.printToastMSG(mContext, "Please Add Photo.");
            }
        } else {
            LogUtil.printToastMSG(KataChithiActivityBkup.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    private String getDate(String strDate) {
        try {
            String[] separated = strDate.split("/");
            String dd = separated[0];
            String mm = separated[1];
            String yyyy = separated[2];
            return yyyy + "-" + mm + "-" + dd;
        } catch (Exception e) {
            return AppUtils.getCurrentDateTime_();
        }
    }

    private void setAdapterForPuranaHisabList() {
        if (kataChitthiImageList.size() > 0) {
            mImagesAdapter = new ImagesAdapter(mContext, kataChitthiImageList, new ImagesAdapter.ListItemSelectListener() {
                @Override
                public void onItemClick(KataChitthiImageModel mDataTicket, Bitmap bitmap) {
                    try {
                        showFullImageDialog(mDataTicket, bitmap);
                    } catch (Exception e) {
                        LogUtil.printToastMSG(mContext, "Fail to load Image.");
                    }
                }
            });

            recyclerViewImages.setHasFixedSize(true);
            recyclerViewImages.setLayoutManager(new GridLayoutManager(mContext, 4));
            recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewImages.setAdapter(mImagesAdapter);
            recyclerViewImages.setVisibility(View.VISIBLE);
            imageViewNoImage.setVisibility(View.GONE);
            mImagesAdapter.notifyDataSetChanged();
        } else {
            recyclerViewImages.setVisibility(View.GONE);
            imageViewNoImage.setVisibility(View.VISIBLE);
        }
    }

    private void onSelectFromGalleryResult(Intent data,String pathFile,int cameraOrGallery) {
        try {
            String actualPath = RealPathUtils.getActualPath(mContext, data.getData());
            kataChitthiImageList.add(new KataChitthiImageModel(actualPath, 1, null));
            setAdapterForPuranaHisabList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                onSelectFromGalleryResult(data,"",0);
            }
        }
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            //if (data != null) {
                try {
                    File file = new File(tempUri + "/IMG_temp.jpg");

                    Bitmap mImgaeBitmap = null;
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                        // Calculate inSampleSize
                        options.inSampleSize = Util.calculateInSampleSize(options, 600, 600);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
                        Bitmap scaledBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                        //check the rotation of the image and display it properly
                        ExifInterface exif;
                        exif = new ExifInterface(file.getAbsolutePath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        mImgaeBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        SaveImageMM(mImgaeBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    CropImage.activity(picUri).start(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        }
    }

    //TODO will add comments later
    private void SaveImageMM(Bitmap finalBitmap) {
        File myDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
        String fname = "Image_" + timeStamp + "_capture.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            //new ImageCompression(this,file.getAbsolutePath()).execute(finalBitmap);
            FileOutputStream out = new FileOutputStream(file);
            //finalBitmap = Bitmap.createScaledBitmap(finalBitmap,(int)1080/2,(int)1920/2, true);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out); //less than 300 kb
            out.flush();
            out.close();
            String oldFname = "IMG_temp.jpg";
            File oldFile = new File(myDir, oldFname);
            if (oldFile.exists()) oldFile.delete();
            //save image to db
            int id = 0;
            String pathDb = file.getPath();
            //set image list adapter
            kataChitthiImageList.add(new KataChitthiImageModel(pathDb, 1, null));
            setAdapterForPuranaHisabList();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                   reqPermissionCode();
                } else {
                    //Toast.makeText(mContext, getString(R.string.somthing_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

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
                        try {
                            if (tag.equalsIgnoreCase("fetch")) {
                                FetchKataChitthiResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), FetchKataChitthiResponse.class);
                                if (responseModel != null && responseModel.getStatus().equals("1")) {
                                    if (responseModel.getResult() != null) {
                                        etWeight.setText(responseModel.getResult().getArray1().get(0).getWeight());
                                        if (responseModel.getResult().getArray1().get(0).getWeight() == null || responseModel.getResult().getArray1().get(0).getWeight().equals(".00")
                                                || responseModel.getResult().getArray1().get(0).getWeight().equals("")) {
                                            etWeight.setEnabled(true);
                                        }
                                        else {
                                            String mDate = AppUtils.changeDatePuranaHisabDetails(responseModel.getResult().getArray1().get(0).getTransactionDate());
                                            if(mDate.equals(AppUtils.getCurrentDateTime())) {
                                                etWeight.setEnabled(true);
                                                //"Transaction_Date":"2021-11-18 00:00:00.000"
                                            }
                                            else {
                                                etWeight.setEnabled(false);
                                            }
                                        }

                                        File myDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
                                        myDir.mkdirs();
                                        if (responseModel.getResult().getArray2() != null) {
                                            downloaded = responseModel.getResult().getArray2().size();
                                            for (int i = 0; i < responseModel.getResult().getArray2().size(); i++) {
                                                Random rnd = new Random();
                                                int n = 100000 + rnd.nextInt(900000);
                                                String fname = "Image_" + n + "_capture.jpg";
                                                File file = new File(myDir, fname);
                                                downloadImageFromUrl(responseModel.getResult().getArray2().get(i).getUrlPrefix() + responseModel.getResult().getArray2().get(i).getUrl(), "", file);
                                            }
                                        } else {
                                            etWeight.setText(".00");
                                            etWeight.setEnabled(true);
                                            kataChitthiImageList.removeAll(kataChitthiImageList);
                                            setAdapterForPuranaHisabList();
                                        }
                                    } else {
                                        etWeight.setText(".00");
                                        etWeight.setEnabled(true);
                                        kataChitthiImageList.removeAll(kataChitthiImageList);
                                        setAdapterForPuranaHisabList();
                                    }

                                } else {
                                    LogUtil.printToastMSG(KataChithiActivityBkup.this, responseModel.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            etWeight.setText(".00");
                            etWeight.setEnabled(true);
                            kataChitthiImageList.removeAll(kataChitthiImageList);
                            setAdapterForPuranaHisabList();
                        }
                        if (tag.equalsIgnoreCase("save")) {
                            SaveKataChitthiResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), SaveKataChitthiResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                //LogUtil.printToastMSG(KataChithiActivity.this, responseModel.getMessage());
                                showSuccessDialog(getString(R.string.msg_weight_submitted));
                                //deleteImagesFolder();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 900);
                            } else {
                                LogUtil.printToastMSG(KataChithiActivityBkup.this, responseModel.getMessage());
                            }
                        }
                        if (tag.equalsIgnoreCase(DynamicAPIPath.POST_VEHICLE)) {
                            VehicleResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), VehicleResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                               vehicleNoDropdown = responseModel.getResult();
                               setDropdownVehNo();
                            } else {
                                LogUtil.printToastMSG(KataChithiActivityBkup.this, responseModel.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(KataChithiActivityBkup.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }

    /*private void deleteImagesFolder() {
        try {
            //private void deleteImagesFolder(){
            File myDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
            if (myDir.exists()) {
                myDir.delete();
            }
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
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
    }*/

    private void downloadImageFromUrl(String url, String lr, File file) {
        @SuppressLint("StaticFieldLeak")
        class DownloadFileFromURL extends AsyncTask<String, String, String> {

            private static final String TAG = "yyyy";
            private String Path = file.getAbsolutePath();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //mDialogloader.show();
                showProgressLoader(getString(R.string.scr_message_please_wait));
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    Array6 repeatativeList = new Array6();
                    if (!url.equals("")) {
                        int count;
                        try {
                            String baseUrl = url;
                            URL url = new URL(baseUrl/* + params[0]*/);
                            URLConnection connection = url.openConnection();
                            connection.connect();

                            // this will be useful so that you can show a tipical 0-100%
                            // progress bar
                            int lengthOfFile = connection.getContentLength();

                            // download the file
                            InputStream input = new BufferedInputStream(url.openStream(), 8192);

                            // Output stream
                            OutputStream output = new FileOutputStream(file);

                            byte[] data = new byte[1024];

                            long total = 0;

                            while ((count = input.read(data)) != -1) {
                                total += count;
                                // publishing the progress....
                                // After this onProgressUpdate will be called
                                publishProgress("" + (int) ((total * 100) / lengthOfFile));

                                // writing data to file
                                output.write(data, 0, count);
                            }

                            // flushing output
                            output.flush();

                            // closing streams
                            output.close();
                            input.close();
                            Path = file.getAbsolutePath();
                            //new VehicleDetailsLrImage(lr, "", null,file.getAbsolutePath()));

                        } catch (Exception e) {
                            LogUtil.printLog(TAG, "Error: " + Objects.requireNonNull(e.getMessage()));
                        }
                    }
                    //LrNo = mImageList.get(i).getLr();
                    // }
                    return Path;
                } catch (Exception e) {
                    e.printStackTrace();
                    return Path;
                }
            }

            @Override
            protected void onPostExecute(String list) {
                kataChitthiImageList.add(new KataChitthiImageModel(list, 0, null));
                downloadedCount++;
                if (downloaded == downloadedCount) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                   /* handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                    dismissLoader();
                    downloadedCount = 0;
                    try {
                        setAdapterForPuranaHisabList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                        /*}
                    }, 100);*/
                }
            }
        }
        new DownloadFileFromURL().execute("url", "path", "lr");
    }


}