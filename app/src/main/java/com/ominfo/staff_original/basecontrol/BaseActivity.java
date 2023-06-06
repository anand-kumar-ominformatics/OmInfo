package com.ominfo.staff_original.basecontrol;

import static com.ominfo.staff_original.ui.attendance.StartAttendanceActivity.result;
import static com.ominfo.staff_original.ui.attendance.StartAttendanceActivity.tvCurrLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.deps.DaggerDeps;
import com.ominfo.staff_original.common.BackgroundAttentionService;
import com.ominfo.staff_original.database.AppDatabase;

import com.ominfo.staff_original.basecontrol.deps.Deps;
import com.ominfo.staff_original.dialog.ViewDialog;
import com.ominfo.staff_original.interfaces.ServiceCallBackInterface;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.NetworkModule;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.contacts.AllContactsActivity;
import com.ominfo.staff_original.ui.contacts.adapter.CallManagerAdapter;
import com.ominfo.staff_original.ui.contacts.model.CallRequest;
import com.ominfo.staff_original.ui.contacts.model.CallResponse;
import com.ominfo.staff_original.ui.contacts.model.CallResult;
import com.ominfo.staff_original.ui.contacts.model.GetContactsViewModel;
import com.ominfo.staff_original.ui.login.LoginActivity;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.notifications.NotificationsActivity;
import com.ominfo.staff_original.ui.purana_hisab.activity.ComplaintsActivity;
import com.ominfo.staff_original.ui.upload_pod.model.PodSaveOfLRViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodResponse;
import com.ominfo.staff_original.util.CustomAnimationUtil;
import com.ominfo.staff_original.util.LogUtil;
import com.ominfo.staff_original.util.SharedPref;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/* base activity for all activity
* please use this naming convention
*
 public static final int SOME_CONSTANT = 42;
    public int publicField;
    private static MyClass sSingleton;
    int mPackagePrivate;
    private int mPrivate;
    protected int mProtected;
    boolean isBoolean;
    boolean hasBoolean;
    View mMyView;
*
*
* */

public class BaseActivity extends AppCompatActivity implements ServiceCallBackInterface, LifecycleObserver {
    private Deps mDeps;
    private Window mWindow;
    private CustomAnimationUtil customAnimationUtil;
    private ViewDialog viewDialog;
    private boolean mBound = false;
    @Inject
    ViewModelFactory mViewModelFactory;
    Location location;
    private PodSaveOfLRViewModel podSaveOfLRViewModel;

    CallManagerAdapter mCallManagerAdapter;
    private GetContactsViewModel getContactsViewModel;
    List<CallResult> callResultList =new ArrayList<>();
    private AppDatabase mDb;
    public Context context;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeps = DaggerDeps.builder().networkModule(new NetworkModule()).build();
        mDeps.inject(this);
        mDb =BaseApplication.getInstance(this).getAppDatabase();
        injectAPI();
        myReceiver = new MyReceiver();
    }

    private void injectAPI() {

        getContactsViewModel = ViewModelProviders.of(BaseActivity.this, mViewModelFactory).get(GetContactsViewModel.class);
        getContactsViewModel.getResponse().observe(this, apiResponse ->consumeResponse(apiResponse, DynamicAPIPath.POST_CONTACTS));

        podSaveOfLRViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PodSaveOfLRViewModel.class);
        podSaveOfLRViewModel.getResponse().observe(this, apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POD_SAVE_FOR_LR1));

    }


    public void callPodSaveOfLRApi() {

        if (NetworkCheck.isInternetAvailable( getApplicationContext() )) {

            try{
                AppDatabase mDb = BaseApplication.getInstance().getAppDatabase();

                List<UploadPodRequest> loginResultTable = mDb.getDbDAO().getPdsList();

                LogUtil.printLog("pending",loginResultTable.size() );

                for( UploadPodRequest item1 : loginResultTable ){

                    LogUtil.printLog("may","api calling");
                    try {

                        item1.setId(null);
                        item1.setGcNo(null);
                        item1.setUploadDate(null);

                        Gson gson = new Gson();
                        String bodyInStringFormat = gson.toJson(item1);

                        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), DynamicAPIPath.POD_SAVE_FOR_LR1);
                        RequestBody json = RequestBody.create(MediaType.parse("text/plain"), bodyInStringFormat);
                        podSaveOfLRViewModel.hitPodSaveOfLr(action,json);
                        LogUtil.printLog("may","api called");
                    }catch (Exception e){
                        LogUtil.printLog("may",e);
                    }
                }

            }catch (Exception e){ LogUtil.printLog("may", " fully exe"+e );}
            

        } else {
            LogUtil.printToastMSG(getApplicationContext(), getString(R.string.err_msg_connection_was_refused));
        }
    }

    public void setToolbar(AppCompatActivity appCompatActivity, Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public static class DarkStatusBar {

        public static void setLightStatusBar(View view, Activity activity) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int flags = view.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.WHITE);
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link }.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(BackgroundAttentionService.EXTRA_LOCATION);
            String address = "";
            try {
                tvCurrLocation.setText("Current Location : Fetching...");
            } catch (Exception e) {
            }
            if (location != null) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LAT, String.valueOf(location.getLatitude()));
                    SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LONG, String.valueOf(location.getLongitude()));
                    SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_TITLE, "Fetching...");
                    ///addresses = null;
                    if (addresses != null && addresses.size() > 0) {
                        //LogUtil.printToastMSG(getApplicationContext(),"addresss_attendnce"+address);
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        //address = null;
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        //tvCurrLocation.setText("Current Location : " + address);
                        SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LAT, String.valueOf(location.getLatitude()));
                        SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LONG, String.valueOf(location.getLongitude()));
                        SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_TITLE, address);
                        Boolean iSTimer = SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.CHECK_OUT_ENABLED, false);
                        tvCurrLocation.setText("Current Location : " + address);
                        tvCurrLocation.setEnabled(false);
                        tvCurrLocation.setPaintFlags(0);
                        //LogUtil.printToastMSG(getApplicationContext(),"addresss_attendnce"+address);
                        //Handler handler = new Handler(Looper.getMainLooper());

                        if(address ==null || address.equals("")){
                            String locationRes = SharedPref.getInstance(getBaseContext()).read(SharedPrefKey.LOCATION_ENTERED_TXT, "Fetching...");
                            SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.NOT_FETCHED, true);
                            tvCurrLocation.setEnabled(true);
                            tvCurrLocation.setPaintFlags(tvCurrLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            tvCurrLocation.setText("Tap to add your current location");
                            if (result != null && !result.equals("temp") && !result.equals("") && !result.equals("Fetching...")) {
                                if(locationRes!=null && !locationRes.equals("") && !locationRes.equals("Fetching..."))
                                {
                                    result=locationRes;
                                    address = locationRes;
                                    //SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_TITLE, locationRes);
                                }
                                tvCurrLocation.setText("Current Location : " + result);
                            }
                            if (tvCurrLocation.getText().toString() == null || tvCurrLocation.getText().toString().equals("Tap to add your current location") || tvCurrLocation.getText().toString().equals("Current Location : Fetching...")) {
                                LogUtil.printSnackBar(getBaseContext(), R.color.white, findViewById(android.R.id.content),
                                        "Sorry, We are unable to fetch your location! \nPlease select it manually.");
                            }
                        }

                    } else {
                        //SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LAT, "0.0");
                        //SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_LONG, "0.0");
                        //SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_TITLE, "");
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // update the ui from here
                                String locationLat = SharedPref.getInstance(getBaseContext()).read(SharedPrefKey.ENTERED_VISIT_LAT, "0.0");
                                String locationLng = SharedPref.getInstance(getBaseContext()).read(SharedPrefKey.ENTERED_VISIT_LNG, "0.0");
                                String locationRes = SharedPref.getInstance(getBaseContext()).read(SharedPrefKey.LOCATION_ENTERED_TXT, "Fetching...");
                                //data.getStringExtra("result");
                                SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.NOT_FETCHED, true);
                                tvCurrLocation.setEnabled(true);
                                tvCurrLocation.setPaintFlags(tvCurrLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvCurrLocation.setText("Tap to add your current location");

                                if (result != null && !result.equals("temp") && !result.equals("") && !result.equals("Fetching...")) {
                                    if(locationRes!=null && !locationRes.equals("") && !locationRes.equals("Fetching..."))
                                    {
                                        result=locationRes;
                                        //SharedPref.getInstance(getBaseContext()).write(SharedPrefKey.ATTENTION_LOC_TITLE, locationRes);
                                    }
                                    tvCurrLocation.setText("Current Location : " + result);
                                }
                                if (tvCurrLocation.getText().toString() == null || tvCurrLocation.getText().toString().equals("Tap to add your current location") || tvCurrLocation.getText().toString().equals("Current Location : Fetching...")) {
                                    LogUtil.printSnackBar(getBaseContext(), R.color.white, findViewById(android.R.id.content),
                                            "Sorry, We are unable to fetch your location! \nPlease select it manually.");}

                            }
                        },100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    public void showSmallProgressBar(FrameLayout mProgressBarHolder) {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        mProgressBarHolder.setAnimation(inAnimation);
        mProgressBarHolder.setVisibility(View.VISIBLE);
    }
    public void startLocationService() {
        startService(new Intent(this, BackgroundAttentionService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(BackgroundAttentionService.ACTION_BROADCAST));
    }

    public void dismissSmallProgressBar(FrameLayout mProgressBarHolder) {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        mProgressBarHolder.setAnimation(outAnimation);
        mProgressBarHolder.setVisibility(View.GONE);
    }

    public void setFontInCollapseToolbar(CollapsingToolbarLayout mCollapsingToolbarLayout) {
        /*Typeface typeface = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold);
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        mCollapsingToolbarLayout.setExpandedTitleTypeface(typeface);*/
    }

    public Deps getDeps() {
        return mDeps;
    }

    /**
     * show error message according to error type
     */
    /**
     * @param view     view that will be animated
     * @param duration for how long in ms will it shake
     * @param offset   start offset of the animation
     * @return returns the same view with animation properties
     */
    public static View makeMeShake(View view, int duration, int offset) {
        Animation anim = new TranslateAnimation(-offset, offset, 0, 0);
        anim.setDuration(duration);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        view.startAnimation(anim);
        return view;
    }

    /*Api response */
    private void consumeResponse(ApiResponse apiResponse, String tag) {

        LogUtil.printLog("may",tag +" base act consume call" + apiResponse.data + " "+ apiResponse.status);

        switch (apiResponse.status) {

            case LOADING:
                if( !tag.equalsIgnoreCase(DynamicAPIPath.POD_SAVE_FOR_LR1) )
                    showProgressLoader(getString(R.string.scr_message_please_wait));

                break;

            case SUCCESS:
                dismissLoader();
                if (!apiResponse.data.isJsonNull()) {
                    LogUtil.printLog(tag, apiResponse.data.toString());

                    try{
                        if (tag.equalsIgnoreCase(DynamicAPIPath.POST_CONTACTS)) {
                            CallResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), CallResponse.class);
                            if (responseModel != null && responseModel.getStatus().equals("1")) {
                                callResultList = responseModel.getResult();
                                try{
                                    showCallManagerDialog(this,1);
                                }catch (Exception e){}
                            } else {
                                //LogUtil.printToastMSG(DashbooardActivity.this, responseModel.getMessage());
                            }
                        }
                    }catch (Exception e){e.printStackTrace();}

                    try {

                        if (tag.equalsIgnoreCase(DynamicAPIPath.POD_SAVE_FOR_LR1)) {

                            UploadPodResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), UploadPodResponse.class);

                            if (responseModel != null && responseModel.getStatus().equals("1")) {

                                String message = responseModel.getMessage();
                                int arrOfStr = message.indexOf(".");
                                mDb.getDbDAO().deletePdsById( Integer.parseInt( message.substring(arrOfStr+1, message.length() )) );

                                LogUtil.printLog("may", "success fully " +responseModel.getMessage());
                            }else
                                LogUtil.printLog("may", "fiallled " +responseModel.getMessage());

                        }
                    } catch (Exception e) {
                        LogUtil.printLog("may", "fiallled ex " +e);
                    }
                }
                break;
            case ERROR:
                LogUtil.printLog("may",tag +" base act err consume errow");
                dismissLoader();
                //LogUtil.printToastMSG(DashbooardActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }

    /*  openContactSupportEmail(this, getString(R.string.app_name),
                       "sendbits@gmail.com", "");*/
    /*send email*/
    public static void openContactSupportEmail(Context context, String subject, String email, String body) {
        /*Create an ACTION_SEND Intent*/
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        String shareBody = "Here is the share content body";
        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        /*Fire!*/
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    /**
     * show error when edit text is empty
     *
     * @param textInputLayout
     * @param error
     */
    public void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.requestFocus();
        makeMeShake(textInputLayout, 20, 5);
    }

    // Monitors the state of the connection to the service.
    public ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            isServiceActive(true, service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            isServiceActive(false, null);

        }
    };


    public void isServiceActive(boolean is, IBinder service) {
    }

    @Override
    public void isServiceActiveCall(boolean is, IBinder service) {
//        if (BaseApplication.getInstance().isApplicationBackgruond || mBound) {
//            unbindService(mServiceConnection);
//            mBound = false;
//        }
    }


    public void setErrorMessage(TextInputLayout inputLayouts, AppCompatEditText editText, String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (editText.getEditableText().toString().trim().isEmpty()) {
                    inputLayouts.setError(errorMsg);
                    editText.requestFocus();
                    makeMeShake(editText, 20, 5);
                } else {
                    inputLayouts.setErrorEnabled(false);
                }
            }
        });
    }

    //show cleaner details popup
    public void showCleanerDetailsDialog(String title , String name) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_cleaner_details);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        RelativeLayout layCaller1 = mDialog.findViewById(R.id.layCaller1);
        RelativeLayout layCaller2 = mDialog.findViewById(R.id.layCaller2);
        AppCompatTextView tvNum1 = mDialog.findViewById(R.id.tvNum1);
        AppCompatTextView tvNum2 = mDialog.findViewById(R.id.tvNum2);
        AppCompatTextView tvTitle = mDialog.findViewById(R.id.tvTitle);
        AppCompatTextView tvName = mDialog.findViewById(R.id.tvName);
        tvTitle.setText(title);tvName.setText(name);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        layCaller1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                setCallDialor(tvNum1.getText().toString().trim());
            }
        });
        layCaller2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                setCallDialor(tvNum2.getText().toString().trim());
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

    /*
     * call this method to display full screen progress loader
     * */
    public void showProgressLoader(String message) {
        try {
            if (viewDialog == null) {
                viewDialog = new ViewDialog(this);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                viewDialog.showDialog(message);
            }

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    /*
     * call this method to dismiss progress loader
     * */
    public void dismissLoader() {
        try {
            if (viewDialog != null) {
                viewDialog.hideDialog();
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        /*try {
            if (!this.isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*launch screen*/
    public void launchScreenClear(Context mContext, Class mActivity){
        Intent intent=new Intent(mContext,mActivity);
        startActivity(intent);
        finish();
    }

    /*launch screen*/
    public void launchScreen(Context mContext, Class mActivity){
        Intent intent=new Intent(mContext,mActivity);
        startActivity(intent);
    }


    /*set error in input field if invalid*/
    public void setError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.requestFocus();
        if (customAnimationUtil == null) {
            customAnimationUtil = new CustomAnimationUtil(this);
        }
        //customAnimationUtil.showErrorEditTextAnimation(textInputLayout, R.anim.shake);
    }

    public Window getmWindow() {
        return mWindow;
    }


    public void setErrorMessageWithNoInputLayout(final AppCompatEditText editText, final String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateFieldForNoInputLayout(editText, errorMsg);
//                if (editText.getText().toString().trim().isEmpty()) {
//                    inputLayouts.setError(errorMsg);
//                    requestFocus(editText);
//                } else {
//                    inputLayouts.setErrorEnabled(false);
//                }
            }
        });
    }
    public boolean isValidateFieldForNoInputLayout(AppCompatEditText editText, String errorMsg) {
        return true;
    }

    public void transperentStatusBar(Context context) {
        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // edited here
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
//        Toast.makeText(this,"In Foreground",Toast.LENGTH_LONG).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void appInPauseState() {
//        Toast.makeText(this,"In Background",Toast.LENGTH_LONG).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appInStopState() {
        try {
            if (BaseApplication.getInstance().isApplicationBackgruond || mBound) {
                unbindService(mServiceConnection);
                mBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * foreground service code
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    public void showSuccessDialog(String msg) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_weight_submitted1);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatTextView mTextViewTitle = mDialog.findViewById(R.id.tv_dialogTitle);
        mTextViewTitle.setText(msg);
        //AppCompatButton appCompatButton = mDialog.findViewById(R.id.btn_done);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        /*appCompatButton.setVisibility(View.VISIBLE);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mEditTextNote.getText().toString()))
                {
                    LogUtil.printToastMSG(mContext,getString(R.string.val_msg_please_enter_note));
                }
                else {
                    callUpdateMarkApi(mEditTextNote.getText().toString());
                    mDialog.dismiss();
                }
            }
        });*/
        mDialog.show();
    }

    public void showUploadDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_upload_total_hisab);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.uploadButton);
        AppCompatButton cancelButton = mDialog.findViewById(R.id.cancelButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showSuccessDialog(getString(R.string.msg_kharcha_uploaded));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 700);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
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

    public void showLogoutDialog(Context mContext) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_logout);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.uploadButton);
        AppCompatButton cancelButton = mDialog.findViewById(R.id.cancelButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finishAffinity();
                launchScreen(mContext, LoginActivity.class);
                SharedPref.getInstance(mContext).write(SharedPrefKey.IS_LOGGED_IN, false);
                try{mDb.getDbDAO().deleteLogin();}catch (Exception e){e.printStackTrace();}
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
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

    public void initToolbar(int val,Context mContext,int backId,int complaintId,int NotifyId,int logoutId,int callId) {
        AppCompatImageView imgBack = findViewById(backId);
        AppCompatImageView imgComplaint = findViewById(complaintId);
        AppCompatImageView imgNotify = findViewById(NotifyId);
        AppCompatImageView imgLogout = findViewById(logoutId);
        AppCompatImageView imgCall = findViewById(callId);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (val == 1) {
                    finish();
                }
            }
        });
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        imgComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (val == 1) {
                    finish();
                    launchScreen(mContext, ComplaintsActivity.class);
                } else {
                    launchScreen(mContext, ComplaintsActivity.class);
                }
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (val == 1) {
                    finish();
                    launchScreen(mContext, NotificationsActivity.class);
                } else {
                    launchScreen(mContext, NotificationsActivity.class);
                }
            }
        });
        try {
            imgLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLogoutDialog(mContext);
                }
            });
        }catch (Exception e){}

    }

    /* Call Api For Login user and get user details */
    private void callApi() {
        if (NetworkCheck.isInternetAvailable(BaseActivity.this)) {
            LoginResultTable loginResultTable = mDb.getDbDAO().getLoginData();
            if(loginResultTable!=null) {
                CallRequest mLoginRequest = new CallRequest();
                mLoginRequest.setUserID(loginResultTable.getUserID()); //6b07b768-926c-49b6-ac1c-89a9d03d4c3b
                mLoginRequest.setUserkey(loginResultTable.getUserKey());
                Gson gson = new Gson();
                String bodyInStringFormat = gson.toJson(mLoginRequest);
                LogUtil.printLog("request call:",bodyInStringFormat);
                getContactsViewModel.hitGetContactsApi(bodyInStringFormat);
            }
            else {
                //LogUtil.printToastMSG(mContext,"Logger - No data");
            }
        } else {
            //LogUtil.printToastMSG(BaseActivity.this, getString(R.string.err_msg_connection_was_refused));
        }
    }

    //show call manager popup
    public void showCallManagerDialog(Context mContext,int val) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_call_manager);
        mDialog.setCanceledOnTouchOutside(true);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.detailsButton);
        AppCompatTextView tvNoContacts = mDialog.findViewById(R.id.tvNoContacts);
        RecyclerView recyclerView = mDialog.findViewById(R.id.rvCallManager);

        setAdapterForPuranaHisabList(recyclerView,mContext,mDialog,tvNoContacts);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                launchScreen(mContext, AllContactsActivity.class);
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        //if(val==0) {
        mDialog.show();
        //}
    }

    public void setCallDialor(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private void setAdapterForPuranaHisabList(RecyclerView recyclerView,Context mContext,Dialog mDialog,AppCompatTextView tvNoContacts ) {

        if (callResultList.size() > 0) {
            mCallManagerAdapter = new CallManagerAdapter(mContext, callResultList, new CallManagerAdapter.ListItemSelectListener() {
                @Override
                public void onItemClick(String mDataTicket) {
                    mDialog.dismiss();
                    setCallDialor(mDataTicket);
                }
            });
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(mCallManagerAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            tvNoContacts.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvNoContacts.setVisibility(View.VISIBLE);
        }
    }

}
