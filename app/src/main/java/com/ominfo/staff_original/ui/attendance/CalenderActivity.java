package com.ominfo.staff_original.ui.attendance;
import static com.ominfo.staff_original.util.AppUtils.convertMonthHighlight;
import static com.ominfo.staff_original.util.AppUtils.getCurrentYear;

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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
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
import com.ominfo.staff_original.ui.attendance.model.CalenderAllListViewModel;
import com.ominfo.staff_original.ui.attendance.model.CalenderAllRequest;
import com.ominfo.staff_original.ui.attendance.model.CalenderAllResponse;
import com.ominfo.staff_original.ui.attendance.model.CalenderAllResult;
import com.ominfo.staff_original.ui.attendance.model.CalenderAttRequest;
import com.ominfo.staff_original.ui.attendance.model.CalenderAttResponse;
import com.ominfo.staff_original.ui.attendance.model.CalenderAttResult;
import com.ominfo.staff_original.ui.attendance.model.CalenderHolidayLeave;
import com.ominfo.staff_original.ui.attendance.model.CalenderHolidaysListViewModel;
import com.ominfo.staff_original.ui.attendance.model.HighlightResponse;
import com.ominfo.staff_original.ui.attendance.model.HighlightsViewModel;
import com.ominfo.staff_original.ui.kata_chithi.adapter.ImagesAdapter;
import com.ominfo.staff_original.ui.kata_chithi.model.Array6;
import com.ominfo.staff_original.ui.kata_chithi.model.KataChitthiImageModel;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleRequest;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleResponse;
import com.ominfo.staff_original.ui.kata_chithi.model.VehicleResult;
import com.ominfo.staff_original.ui.loading_list.model.LoadingListRequest;
import com.ominfo.staff_original.ui.loading_list.model.LoadingListResponse;
import com.ominfo.staff_original.ui.loading_list.model.SaveLoadingList;
import com.ominfo.staff_original.ui.loading_list.model.SaveLoadingListResponse;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.util.AppUtils;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.RealPathUtils;
import com.ominfo.staff_original.util.SharedPref;
import com.ominfo.staff_original.util.Util;
import com.ominfo.staff_original.zcustomcalendar.CustomCalendar;
import com.ominfo.staff_original.zcustomcalendar.OnCalenderHolidaySelectedListener;
import com.ominfo.staff_original.zcustomcalendar.OnDateSelectedListener;
import com.ominfo.staff_original.zcustomcalendar.Property;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

public class CalenderActivity extends BaseActivity {

    Context mContext;
    private static final int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri picUri;
    private String tempUri;
    final Calendar myCalendar = Calendar.getInstance();
    Bitmap mImgaeBitmap;
    private AppDatabase mDb;
    String mFromDate = "",mToDate = "";
    ImagesAdapter mImagesAdapter;
    List<KataChitthiImageModel> kataChitthiImageList = new ArrayList<>();
    Dialog mDialogLogout,dialogAddHoliday,mDialogEditHoliday;
    Boolean enabledPopup = false;

    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;
    @BindView(R.id.tvMonthList)
    AppCompatAutoCompleteTextView tvMonthList;
    @BindView(R.id.tvAttendance)
    AppCompatTextView tvAttendanceTitle;
    @BindView(R.id.tvAttendanceValue)
    AppCompatTextView tvAttendanceValue;
    @BindView(R.id.tvLeaveValue)
    AppCompatTextView tvLeaveValue;
    private CalenderHolidaysListViewModel calenderHolidaysListViewModel;
    private CalenderAllListViewModel calenderAllListViewModel;
    private int downloaded = 0, downloadedCount = 0;
    List<VehicleResult> vehicleNoDropdown = new ArrayList<>();
    VehicleResult mSelectedVehicle = new VehicleResult();
    @Inject
    ViewModelFactory mViewModelFactory;
    private HighlightsViewModel highlightsViewModel;
    /*
    private SaveLoadingListViewModel mSaveLoadingListViewModel;
    private VehicleViewModel vehicleViewModel;*/
    int cam = 0;
    private int SELECT_FILE = 1;
    HashMap<Object, Property> descHashMap = new HashMap<>();
    HashMap<Integer, Object> dateHashmap = new HashMap<>();
    Calendar calendar = Calendar.getInstance();
    @BindView(R.id.custom_calendar)
    CustomCalendar custom_calendar;
    @BindView(R.id.tvHighlightsSection)
    AppCompatTextView tvHighlightsSection;
    public static List<CalenderAllResult> calenderHolidayLeave = new ArrayList<>();
    String stringAttendance = "";
    String currentMonth = "0",todayMonth = "0", joiningMonth = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
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
        mFromDate = AppUtils.startMonth();
        mToDate = AppUtils.endMonth();
        setDropdownMonthList();
        setCalenderData();
        callCalenderColoursApi();
        callHighlightsApi();
    }

    private void setToolbar() {
        //set toolbar title
        toolbarTitle.setText(R.string.scr_lbl_attendance_details);
        initToolbar(1, mContext, R.id.imgBack, R.id.imgReport, R.id.imgNotify, R.id.imgLogout, R.id.imgCall);
    }

    private void setCalenderData() {
        tvHighlightsSection.setText("Highlights ");
        tvMonthList.setText(AppUtils.getCurrentMonth()+" "+getCurrentYear());
        setDropdownMonthList();
        // Initialize description hashmap
        LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
        // Initialize default property
        Property defaultProperty = new Property();

        // Initialize default resource
        defaultProperty.layoutResource = R.layout.default_view;
        // Initialize and assign variable
        defaultProperty.dateTextViewResource = R.id.text_view;

        // Put object and property
        descHashMap.put("default", defaultProperty);

        // for current date
        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("current", currentProperty);

        // for present date
        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("present", presentProperty);

        // For absent
        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("absent", absentProperty);

        // set desc hashmap on custom calendar
        custom_calendar.invalidate();
        custom_calendar.setMapDescToProp(descHashMap);

        // Initialize date hashmap

        // initialize calendar

        // Put values
        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
        try {
            if (calenderHolidayLeave != null && calenderHolidayLeave.size() > 0) {
                for (int i = 0; i < calenderHolidayLeave.size(); i++) {
                    String[] str = calenderHolidayLeave.get(i).getDate().split("-");
                    dateHashmap.put(Integer.valueOf(str[2]), "absent");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       /* dateHashmap.put(1,"present");
        dateHashmap.put(2,"absent");
        dateHashmap.put(3,"present");
        dateHashmap.put(4,"absent");
        dateHashmap.put(20,"present");
        dateHashmap.put(30,"absent");*/

        // set date
        // custom_calendar.setNextButtonColor(R.color.colorAccent);
        custom_calendar.setDate(calendar, dateHashmap);
        custom_calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                // get string date
                try {
                    String mon = String.valueOf(selectedDate.get(Calendar.MONTH) + 1).length() == 1 ?
                            "0" + String.valueOf(selectedDate.get(Calendar.MONTH) + 1) :
                            String.valueOf(selectedDate.get(Calendar.MONTH) + 1);
                    String day = String.valueOf(selectedDate.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                            "0" + String.valueOf(selectedDate.get(Calendar.DAY_OF_MONTH)) :
                            String.valueOf(selectedDate.get(Calendar.DAY_OF_MONTH));
                    String sDate = selectedDate.get(Calendar.YEAR)
                            + "-" + mon
                            + "-" + day;

                    // display date in toast
                    Boolean event = false;
                    for (int i = 0; i < calenderHolidayLeave.size(); i++) {
                        if (sDate.equals(calenderHolidayLeave.get(i).getDate())) {
                            enabledPopup = true;
                            stringAttendance = AppUtils.dateConvertYYYYToDD(sDate);
                            currentMonth = AppUtils.convertMonthToIntMMM(tvMonthList.getText().toString().trim());
                            todayMonth = AppUtils.convertMonthToIntMMM(AppUtils.getCurrentMonth());
                            if((Integer.parseInt(currentMonth)<=Integer.parseInt(todayMonth))
                                    && (Integer.parseInt(joiningMonth)<=Integer.parseInt(currentMonth))) {
                                callCalenderHolidaysApi(sDate);
                            }
                            /*showEditHolidayDialog(calenderHolidayLeave.get(i).getRecordId(),
                                    calenderHolidayLeave.get(i).getDate(), calenderHolidayLeave.get(i).getName() + "("
                                            + calenderHolidayLeave.get(i).getDescription() + ")");
                            //LogUtil.printToastMSG(mContext, calenderHolidayLeave.get(i).getName());*/
                            event = true;
                            break;
                        }
                    }
                    if (!event) {
                        stringAttendance = AppUtils.dateConvertYYYYToDD(sDate);
                        currentMonth = AppUtils.convertMonthToIntMMM(tvMonthList.getText().toString().trim());
                        todayMonth = AppUtils.convertMonthToIntMMM(AppUtils.getCurrentMonth());
                        if((Integer.parseInt(currentMonth)<=Integer.parseInt(todayMonth))
                                && (Integer.parseInt(joiningMonth)<=Integer.parseInt(currentMonth))) {
                            callCalenderHolidaysApi(sDate);
                        }
                        //enabledPopup = false;
                        //                            LogUtil.printSnackBar(mContext, Color.YELLOW, ((CalenderActivity) mContext).findViewById(android.R.id.content), "No events for!");
                    }
                }catch (Exception e){
                    LogUtil.printToastMSG(mContext, "Something went wrong!");
                }
            }
        });
        custom_calendar.setOnCalenderListener(new OnCalenderHolidaySelectedListener() {
            @Override
            public void onDateSelected(Calendar selectedDate) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String test=  sdf.format(selectedDate.getTime());
                mFromDate = AppUtils.startHolidayMonth(selectedDate);
                mToDate = AppUtils.endHolidayMonth(selectedDate);
                calendar = selectedDate;
                //LogUtil.printToastMSG(mContext,test+"k "+mFromDate+"jn"+mToDate);
                callCalenderColoursApi();
                //callDashboardApi();
            }
        });
    }

    //set value to Search dropdown
    private void setDropdownMonthList() {
        List<String> leaveModelList = new ArrayList<>();
        leaveModelList.add("January");
        leaveModelList.add("February");
        leaveModelList.add("March");
        leaveModelList.add("April");
        leaveModelList.add("May");
        leaveModelList.add("June");
        leaveModelList.add("July");
        leaveModelList.add("August");
        leaveModelList.add("September");
        leaveModelList.add("October");
        leaveModelList.add("November");
        leaveModelList.add("December");
        try {
            int pos = 0;
            if (leaveModelList != null && leaveModelList.size() > 0) {
                String[] mDropdownList = new String[leaveModelList.size()];
                for (int i = 0; i < leaveModelList.size(); i++) {
                    mDropdownList[i] = leaveModelList.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        mContext,
                        R.layout.row_dropdown_item_high,
                        mDropdownList);
                //tvHighlight.setThreshold(1);
                tvMonthList.setAdapter(adapter);
                //mListDropdownView.setHint(mDropdownList[pos]);
                tvMonthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AppUtils.hideKeyBoard(CalenderActivity.this);
                        tvMonthList.setText(convertMonthHighlight(tvMonthList.getText().toString().trim())+" "+getCurrentYear());
                        setDropdownMonthList();
                        String[] curr = AppUtils.getCurrentDateTime_().split("-");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        Date date = null;
                        String[] splitDate = tvMonthList.getText().toString().trim().split(" ");
                        int val = Integer.valueOf(AppUtils.convertMonthToIntMMM(splitDate[0]));
                        try {
                            date = parseFormat.parse(curr[0] + "/" + val + "/" + curr[2]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendarSel = Calendar.getInstance();
                        calendarSel.setTime(date);
                        mFromDate = AppUtils.startHolidayMonth(calendarSel);
                        mToDate = AppUtils.endHolidayMonth(calendarSel);
                        calendar = calendarSel;
                        currentMonth = AppUtils.convertMonthToIntMMM(tvMonthList.getText().toString().trim());
                        todayMonth = AppUtils.convertMonthToIntMMM(AppUtils.getCurrentMonth());
                        if(joiningMonth.equals("0")){
                            joiningMonth = todayMonth;
                        }
                        if((Integer.parseInt(currentMonth)<=Integer.parseInt(todayMonth))
                                && (Integer.parseInt(joiningMonth)<=Integer.parseInt(currentMonth))) {
                            callCalenderColoursApi();
                            callHighlightsApi();
                        }else {  setDefaultCalender();
                            // LogUtil.printToastMSG(mContext,"No data available");
                        }
                    }
                });

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* Call Api For calender holidays */
    private void callCalenderHolidaysApi(String mDate) {
        if (NetworkCheck.isInternetAvailable(mContext)) {
            LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
            if (loginTable != null && loginTable.getCompany_Id()!=null) {
                CalenderAttRequest calenderAttRequest = new CalenderAttRequest();
                calenderAttRequest.setDate(mDate);
                calenderAttRequest.setUserID(loginTable.getCompany_Id().getEmp_id());
                calenderAttRequest.setUserkey(loginTable.getUserKey());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(calenderAttRequest);

                calenderHolidaysListViewModel.hitCalenderHolidaysListApi(bodyInStringFormat);
            } else {
                LogUtil.printToastMSG(mContext, getString(R.string.plese_contact_ur_admin));
            }
        } else {
            LogUtil.printToastMSG(mContext, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /* Call Api For calender colour */
    private void callCalenderColoursApi() {
        if (NetworkCheck.isInternetAvailable(mContext)) {
            LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
            if (loginTable != null && loginTable.getCompany_Id()!=null) {
                CalenderAllRequest calenderAttRequest = new CalenderAllRequest();
                calenderAttRequest.setFromDate(mFromDate);
                calenderAttRequest.setEndDate(mToDate);
                calenderAttRequest.setUserID(loginTable.getCompany_Id().getEmp_id());
                calenderAttRequest.setUserkey(loginTable.getUserKey());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(calenderAttRequest);

                calenderAllListViewModel.hitCalenderHolidaysListApi(bodyInStringFormat);
            } else {
                LogUtil.printToastMSG(mContext, getString(R.string.plese_contact_ur_admin));
            }
        } else {
            LogUtil.printToastMSG(mContext, getString(R.string.err_msg_connection_was_refused));
        }
    }

    /* Call Api For calender colour */
    private void callHighlightsApi() {
        if (NetworkCheck.isInternetAvailable(mContext)) {
            LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
            if (loginTable != null && loginTable.getCompany_Id()!=null) {
                CalenderAllRequest calenderAttRequest = new CalenderAllRequest();
                calenderAttRequest.setFromDate(mFromDate);
                calenderAttRequest.setEndDate(mToDate);
                calenderAttRequest.setUserID(loginTable.getCompany_Id().getEmp_id());
                calenderAttRequest.setUserkey(loginTable.getUserKey());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(calenderAttRequest);

                highlightsViewModel.hitCalenderHolidaysListApi(bodyInStringFormat);
            } else {
                LogUtil.printToastMSG(mContext, getString(R.string.plese_contact_ur_admin));
            }
        } else {
            LogUtil.printToastMSG(mContext, getString(R.string.err_msg_connection_was_refused));
        }
    }

    public void showEditHolidayDialog(String id,String date,String name) {
        mDialogEditHoliday = new Dialog(mContext, R.style.ThemeDialogCustom);
        mDialogEditHoliday.setContentView(R.layout.dialog_edit_holiday);
        mDialogEditHoliday.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialogEditHoliday.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialogEditHoliday.findViewById(R.id.uploadButton);
        AppCompatButton cancelButton = mDialogEditHoliday.findViewById(R.id.cancelButton);
        AppCompatTextView tvStart = mDialogEditHoliday.findViewById(R.id.tvStart);
        AppCompatTextView tvDate = mDialogEditHoliday.findViewById(R.id.tvDate);
        AppCompatTextView tvName = mDialogEditHoliday.findViewById(R.id.tvName);
        tvDate.setText("Date : "+AppUtils.convertDobDate(date));
        tvName.setText("Title : "+name);
        tvStart.setText("You want to cancel this holiday ?");
        LoginResultTable loginTable = mDb.getDbDAO().getLoginData();
        if(loginTable!=null){
            //if(loginTable.getIsadmin().equals("0")){
                tvStart.setText("Holiday Details");
                okayButton.setVisibility(View.GONE);
                cancelButton.setText("Okay");
           // }
        }
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabledPopup = false;
                //callEditHolidayApi(id);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogEditHoliday.dismiss();
                enabledPopup = false;
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogEditHoliday.dismiss();
                enabledPopup = false;
            }
        });

        mDialogEditHoliday.show();

    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void injectAPI() {
        calenderHolidaysListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CalenderHolidaysListViewModel.class);
        calenderHolidaysListViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_CALENDER_HOLIDAY));

        calenderAllListViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CalenderAllListViewModel.class);
        calenderAllListViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_CALENDER_ALL));

        highlightsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HighlightsViewModel.class);
        highlightsViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POST_HIGHLIGHTS));
    }

    //perform click actions
    @OnClick({})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

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
                            shareToInstant("Shared loading list photo", imgFile, view);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.printToastMSG(mContext,"Something went wrong.");
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
                //setAdapterForPuranaHisabList();
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


    private void onSelectFromGalleryResult(Intent data,String pathFile,int cameraOrGallery) {
        try {
            String actualPath = RealPathUtils.getActualPath(mContext, data.getData());
            kataChitthiImageList.add(new KataChitthiImageModel(actualPath, 1, null));
            //setAdapterForPuranaHisabList();
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
            //setAdapterForPuranaHisabList();
        } catch (Exception e) {
            e.printStackTrace();
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
                            if (tag.equalsIgnoreCase(DynamicAPIPath.POST_CALENDER_ALL)) {
                                CalenderAllResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), CalenderAllResponse.class);
                                if (responseModel != null && responseModel.getStatus().equals("1")) {
                                    try{calenderHolidayLeave=new ArrayList<>();
                                        descHashMap = new HashMap<>();dateHashmap = new HashMap<>();}catch (Exception e){}
                                    if (responseModel.getResult()!=null/* &&
                                            responseModel.getResult().size()>0*/) {
                                        String[] dateList = responseModel.getResult().get(0).split("~");
                                        if(dateList.length>0) {
                                            for (String s : dateList) {
                                                calenderHolidayLeave.add(new CalenderAllResult(s));
                                            }
                                        }
                                        try{
                                            String[] strTest = calenderHolidayLeave.get(0).getDate().split("-");
                                            String[] strDate = AppUtils.getCurrentDateInyyyymmdd().split("-");
                                            if(Integer.parseInt(strTest[1])==Integer.parseInt(strDate[1])){
                                                dateHashmap.put(Integer.parseInt(strDate[2]), "current");
                                            }}catch(Exception e){}
                                        for (int i = 0; i < calenderHolidayLeave.size(); i++) {
                                            String[] str = calenderHolidayLeave.get(i).getDate().split("-");
                                            dateHashmap.put(Integer.valueOf(str[2]), "absent");
                                        }
                                        // set date
                                        // custom_calendar.setNextButtonColor(R.color.colorAccent);
                                        custom_calendar.setDate(calendar, dateHashmap);
                                    }
                                    else{
                                        setDefaultCalender();}
                                    // LogUtil.printToastMSG(mContext, responseModel.getResult().getMessage());
                                } else {
                                    setDefaultCalender();
                                    LogUtil.printToastMSG(mContext, responseModel.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            setDefaultCalender();
                            e.printStackTrace();
                        }

                        try{
                            if (tag.equalsIgnoreCase(DynamicAPIPath.POST_CALENDER_HOLIDAY)) {
                                CalenderAttResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), CalenderAttResponse.class);
                                if (responseModel != null && responseModel.getStatus().equals("1")) {
                                    showAttendanceDetailsDialog(responseModel.getResult());
                                }
                                else{
                                    LogUtil.printToastMSG(mContext,responseModel.getMessage());
                                }
                            }
                        }catch (Exception e){e.printStackTrace();}
                        try{
                            if (tag.equalsIgnoreCase(DynamicAPIPath.POST_HIGHLIGHTS)) {
                                HighlightResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), HighlightResponse.class);
                                if (responseModel != null && responseModel.getStatus().equals("1")) {
                                    try {
                                        joiningMonth = responseModel.getResult().get(0).getDoj().split("-")[1];
                                    }catch (Exception e){}
                                    long total = responseModel.getResult().get(0).getTotalDaysTillToday();
                                    tvAttendanceValue.setText(""+responseModel.getResult().get(0).getPresentCount()+" / "+total);
                                    tvLeaveValue.setText(""+responseModel.getResult().get(0).getLateCount()+" / "+total);
                                }
                                else{
                                    LogUtil.printToastMSG(mContext,responseModel.getMessage());
                                }
                            }
                        }catch (Exception e){e.printStackTrace();}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                dismissLoader();
                LogUtil.printToastMSG(CalenderActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }

    public void showAttendanceDetailsDialog(CalenderAttResult data) {
        Dialog dialogAttendance = new Dialog(mContext, R.style.ThemeDialogCustom);
        dialogAttendance.setContentView(R.layout.dialog_attendance_details);
        dialogAttendance.setCanceledOnTouchOutside(true);
        RelativeLayout mClose = dialogAttendance.findViewById(R.id.layCancel);
        AppCompatTextView tvAttDate  = dialogAttendance.findViewById(R.id.tvDateCal);
        AppCompatTextView tvStatusCal = dialogAttendance.findViewById(R.id.tvStatusCal);
        AppCompatTextView tvInTime = dialogAttendance.findViewById(R.id.tvInTimeData);
        AppCompatTextView tvOutTime = dialogAttendance.findViewById(R.id.tvOutTimeData);
        AppCompatTextView tvInLocation = dialogAttendance.findViewById(R.id.tvInLocation);
        AppCompatTextView tvOutLocation = dialogAttendance.findViewById(R.id.tvOutLocation);
        AppCompatTextView titleInLocation = dialogAttendance.findViewById(R.id.titleInLocation);
        RelativeLayout tvTimings = dialogAttendance.findViewById(R.id.tvTimings);
        RelativeLayout layOutLoc = dialogAttendance.findViewById(R.id.layOutLoc);
        RelativeLayout layInLoc = dialogAttendance.findViewById(R.id.layInLoc);
        tvInTime.setText(" "+AppUtils.convert24to12Attendance(data.getStartTime()));
        tvOutTime.setText(" "+AppUtils.convert24to12Attendance(data.getEndTime()));
        tvInLocation.setText(data.getOfficeStartAddr());
        tvOutLocation.setText(data.getOfficeEndAddr());
        tvAttDate.setText(" "+stringAttendance);
        tvStatusCal.setText(data.getStatus());
        if(data.getStatus().equals("Present")){
            tvTimings.setVisibility(View.VISIBLE);
            layInLoc.setVisibility(View.VISIBLE);
            layOutLoc.setVisibility(View.VISIBLE);
            titleInLocation.setVisibility(View.VISIBLE);
            tvStatusCal.setTextColor(getResources().getColor(R.color.green));
        }else{
            tvTimings.setVisibility(View.INVISIBLE);
            layOutLoc.setVisibility(View.GONE);
            titleInLocation.setVisibility(View.GONE);
            layInLoc.setVisibility(View.GONE);
            tvStatusCal.setText(R.string.scr_lbl_you_absent);
            tvStatusCal.setTextColor(getResources().getColor(R.color.deep_red));
        }
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAttendance.dismiss();
            }
        });
        dialogAttendance.show();
    }


    private void setDefaultCalender(){
        try{calenderHolidayLeave=new ArrayList<>();
            descHashMap = new HashMap<>();dateHashmap = new HashMap<>();}catch (Exception e){}
        dateHashmap.put(1, "default");
        custom_calendar.setDate(calendar, dateHashmap);
        TextView current_date = custom_calendar.getMonthYearTextView();
        String calMon = AppUtils.convertMonthToIntMMM(current_date.getText().toString());
        String[] strDate = AppUtils.getCurrentDateInyyyymmdd().split("-");
        if (Integer.parseInt(calMon)==Integer.parseInt(strDate[1])) {
            String day = AppUtils.getDayToday(AppUtils.getCurrentDateTime_());
            dateHashmap.put(Integer.parseInt(day.equals("") && day == null ? "0" : day), "current");
            custom_calendar.setDate(calendar, dateHashmap);
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
                        //setAdapterForPuranaHisabList();
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