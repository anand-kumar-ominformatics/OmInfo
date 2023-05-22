package com.ominfo.staff_original.basecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.ominfo.staff_original.R;

import com.ominfo.staff_original.deps.DaggerDeps;
import com.ominfo.staff_original.deps.Deps;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.interfaces.CustomDialogHelper;
import com.ominfo.staff_original.network.ApiResponse;
import com.ominfo.staff_original.network.DynamicAPIPath;
import com.ominfo.staff_original.network.NetworkCheck;
import com.ominfo.staff_original.network.NetworkModule;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.upload_pod.model.PodSaveOfLRViewModel;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodResponse;
import com.ominfo.staff_original.util.CustomAnimationUtil;
import com.ominfo.staff_original.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/* base fragment for all fragments
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
public class BaseFragment extends Fragment {

    private AlertDialog alertDialog;
    private CustomAnimationUtil customAnimationUtil;

    private PodSaveOfLRViewModel podSaveOfLRViewModel;

    private AppDatabase mDb;
    private Deps mDeps;

    @Inject
    ViewModelFactory mViewModelFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDeps = DaggerDeps.builder().networkModule(new NetworkModule()).build();
        mDeps.inject(this);
        mDb =BaseApplication.getInstance(getActivity()).getAppDatabase();
        injectAPI();
    }

    private void injectAPI() {

        podSaveOfLRViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PodSaveOfLRViewModel.class);
        podSaveOfLRViewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> consumeResponse(apiResponse, DynamicAPIPath.POD_SAVE_FOR_LR1));

    }



    public void callPodSaveOfLRApi() {

        if (NetworkCheck.isInternetAvailable( getContext() )) {

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
            LogUtil.printToastMSG(getContext(), getString(R.string.err_msg_connection_was_refused));
        }
    }




    /*Api response */
    private void consumeResponse(ApiResponse apiResponse, String tag) {

        LogUtil.printLog("may",tag +" base frg consume call" + apiResponse.data + " "+ apiResponse.status);


        switch (apiResponse.status) {

            case LOADING:
                break;

            case SUCCESS:
                if (!apiResponse.data.isJsonNull()) {
                    LogUtil.printLog(tag, apiResponse.data.toString());


                    try {

                        if (tag.equalsIgnoreCase(DynamicAPIPath.POD_SAVE_FOR_LR1)) {


                            UploadPodResponse responseModel = new Gson().fromJson(apiResponse.data.toString(), UploadPodResponse.class);

                            if (responseModel != null && responseModel.getStatus().equals("1")) {

                                String message = responseModel.getMessage();
                                int arrOfStr = message.indexOf(".");
                                mDb.getDbDAO().deletePdsById( Integer.parseInt( message.substring(arrOfStr+1, message.length() )) );

                            }else
                                LogUtil.printLog("may", "fiallled " +responseModel.getMessage());

                        }
                    } catch (Exception e) {
                        LogUtil.printLog("may", "fiallled ex " +e);
                    }

                }
                break;
            case ERROR:
                LogUtil.printLog("may",tag +" frg base  err consume call");
                //LogUtil.printToastMSG(DashbooardActivity.this, getString(R.string.err_msg_connection_was_refused));
                break;
        }
    }



    public void showCustomAlertDialog(Context mContext, String title, String msg, String yesBTNTxt
            , String noBTNTxt, final CustomDialogHelper customDialogHelper) {

        new AlertDialog.Builder(mContext)
                .setTitle("")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(yesBTNTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        customDialogHelper.onYesButtonClick();
                    }
                })
                .setNegativeButton(noBTNTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void setDialogVisible(Context mContext) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(false);
            ViewSwitcher viewSwitcher = new ViewSwitcher(mContext);
//            viewSwitcher.addView(ViewSwitcher.inflate(mContext, R.layout.progressbar, null));
//            TextView txtView = (TextView) viewSwitcher.findViewById(R.id.textView);
//            ProgressBar progressBar = (ProgressBar) viewSwitcher.findViewById(R.id.progressBar_cyclic);
            builder.setView(viewSwitcher);
            alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogCancel(Context mContext) {
        try {
            if (alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void showSnakeBar(String msg) {
        /*Snackbar snackbar;
        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "" + msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color._FF5C47));
        TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();*/

    }

    public void TextWatchNormalForCardNumber(AppCompatTextView appCompatEditText) {
        appCompatEditText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 52; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 60; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }

            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }

    public static void setErrorMessage(Activity activity, final TextInputLayout inputLayouts, final AppCompatEditText editText, final String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateField(activity,inputLayouts, editText, errorMsg);
            }
        });
    }

    /**
     * override this method for validations
     */
    public static boolean isValidateField(Activity activity, TextInputLayout inputLayouts, AppCompatEditText editText, String errorMsg) {
        if (editText.getText().toString().trim().isEmpty()) {
            inputLayouts.setError(errorMsg);
            requestFocus(activity,editText);
            makeMeShake(editText, 20, 5);
            return false;
        } else {
            inputLayouts.setErrorEnabled(false);
            return true;
        }

    }

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

    public static void requestFocus(Activity activity, View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /*remve amount format*/
    public String orignalAmount(String amount) {
        String sAmount = amount;
        try {
            sAmount = amount.replace(",", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sAmount;
    }

    /*set error in input field if invalid*/
    public void setError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.requestFocus();
        if (customAnimationUtil == null) {
            customAnimationUtil = new CustomAnimationUtil(getActivity());
        }
        //customAnimationUtil.showErrorEditTextAnimation(textInputLayout, R.anim.shake);
    }
}
