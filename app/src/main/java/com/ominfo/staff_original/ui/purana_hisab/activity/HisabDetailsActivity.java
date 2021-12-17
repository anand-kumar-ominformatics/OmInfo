package com.ominfo.staff_original.ui.purana_hisab.activity;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.interfaces.Constants;
import com.ominfo.staff_original.ui.driver_hisab.model.DriverHisabModel;
import com.ominfo.staff_original.ui.purana_hisab.adapter.HisabDetailsAdapter;
import com.ominfo.staff_original.ui.purana_hisab.model.PuranaHisabModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HisabDetailsActivity extends BaseActivity {

    @BindView(R.id.tvAurJaniye)
    AppCompatTextView mTextViewAurJaniye;
    Context mContext;
    @BindView(R.id.rvDriverHisab)
    RecyclerView rvDriverHisab;
    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;
    @BindView(R.id.imgExpandSheet)
    AppCompatImageView imgExpandSheet;
    @BindView(R.id.layTotalBhatta)
    LinearLayoutCompat layTotalBhatta;
    @BindView(R.id.layTotalHisab)
    LinearLayoutCompat layTotalHisab;
    @BindView(R.id.complaintButton)
    AppCompatButton complaintButton;
    @BindView(R.id.notOkayButton)
    AppCompatButton NotOkayButton;
    @BindView(R.id.layOkay)
    LinearLayoutCompat layOkay;

    // creating a variable for medi recorder object class.
    private MediaRecorder mRecorder;
    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;
    // string variable is created for storing a file name
    private static String mFileName = null;
    // constant for storing audio permission
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    HisabDetailsAdapter mHisabDetailsAdapter;
    List<PuranaHisabModel> puranaHisabModelList = new ArrayList<>();
    private boolean mSheetExpandStatus = false;
    private String fromScr = "";
    int mHisabIcon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisab_details);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //set toolbar title
        setToolbar();
        getIntentData();
    }

    private void setToolbar() {
        //set toolbar title
        toolbarTitle.setText(getString(R.string.scr_title_purana_hisab));
        initToolbar(1, mContext, R.id.imgBack, R.id.imgReport, R.id.imgNotify, 0, R.id.imgCall);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            fromScr = intent.getStringExtra(Constants.FROM_SCREEN);
            if (fromScr.equals(Constants.OKAY)) {
                /*complaintButton.setText(fromScr);
                NotOkayButton.setVisibility(View.VISIBLE);
                complaintButton.setVisibility(View.VISIBLE);
                NotOkayButton.setText(getString(R.string.scr_lbl_not_okay));
                complaintButton.setText(getString(R.string.scr_lbl_okay));*/
                layOkay.setVisibility(View.VISIBLE);
                complaintButton.setVisibility(View.GONE);
                mHisabIcon = 0;
                setAdapterForActivityList();
            }
            if (fromScr.equals(Constants.COMPLAINTS)) {
                /*complaintButton.setVisibility(View.VISIBLE);
                complaintButton.setText(getString(R.string.scr_lbl_complaint));
                NotOkayButton.setVisibility(View.GONE);*/
                layOkay.setVisibility(View.GONE);
                complaintButton.setVisibility(View.VISIBLE);
                mHisabIcon = 1;
                setAdapterForActivityList();
            }
            if (fromScr.equals(Constants.ACCEPTED)) {
                layOkay.setVisibility(View.GONE);
                complaintButton.setVisibility(View.GONE);
                mHisabIcon = 0;
                setAdapterForActivityList();
            }
        } else {
            /*layOkay.setVisibility(View.GONE);
            complaintButton.setVisibility(View.VISIBLE);
            mHisabIcon = 1;
            setAdapterForActivityList();*/
        }
    }

    private void showInfoDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_information);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        //appCompatButton.setVisibility(View.VISIBLE);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //launchScreen(mContext, DriverHisabActivity.class);
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

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void setAdapterForActivityList() {
        puranaHisabModelList.add(new PuranaHisabModel("भराई", "1", mHisabIcon));
        puranaHisabModelList.add(new PuranaHisabModel("वराई", "1", mHisabIcon));
        puranaHisabModelList.add(new PuranaHisabModel("चाय पानी", "1", mHisabIcon));
        puranaHisabModelList.add(new PuranaHisabModel("टोल खर्चा", "0", mHisabIcon));
        puranaHisabModelList.add(new PuranaHisabModel("रस्सी", "1", mHisabIcon));
        puranaHisabModelList.add(new PuranaHisabModel("कांटा", "0", mHisabIcon));

        if (puranaHisabModelList.size() > 0) {
            mHisabDetailsAdapter = new HisabDetailsAdapter(mContext, puranaHisabModelList, new HisabDetailsAdapter.ListItemSelectListener() {
                @Override
                public void onItemClick(DriverHisabModel mDataTicket) {

                }
            });
            rvDriverHisab.setHasFixedSize(true);
            rvDriverHisab.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            rvDriverHisab.setAdapter(mHisabDetailsAdapter);
            rvDriverHisab.setVisibility(View.VISIBLE);
        } else {
            rvDriverHisab.setVisibility(View.GONE);
        }
    }

    private void showComplaintDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_check_issue);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        //appCompatButton.setVisibility(View.VISIBLE);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showRaiseIssueDialog();
                //launchScreen(mContext, DriverHisabActivity.class);
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

    private void showIssueRecordingDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_issue_recording);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
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

    private void showRaiseIssueDialog() {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_raise_issue);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatImageView imgRecord = mDialog.findViewById(R.id.imgRecord);
        AppCompatImageView imgStop = mDialog.findViewById(R.id.imgStop);
        AppCompatTextView tvListening = mDialog.findViewById(R.id.tvListening);
        AppCompatButton okayButton = mDialog.findViewById(R.id.okayButton);
        //LinearLayoutCompat appCompatLayout = mDialog.findViewById(R.id.layPopup);
        //appCompatButton.setVisibility(View.VISIBLE);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                showSuccessDialog(getString(R.string.msg_issue_submitted));
            }
        });
        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvListening.setText("Recording...");
                imgRecord.setVisibility(View.GONE);
                imgStop.setVisibility(View.VISIBLE);
                //requestPermission();
            }
        });
        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvListening.setText(R.string.scr_lbl_stop_recording);
                imgRecord.setVisibility(View.VISIBLE);
                imgStop.setVisibility(View.GONE);
                //pauseRecording();
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

    //perform click actions
    @OnClick({R.id.complaintButton,R.id.driverNamValue, R.id.cleanerNamValue,R.id.tvHisabName, R.id.layAurJaniye, R.id.okayButton, R.id.imgInfo, R.id.notOkayButton})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.complaintButton:
                showComplaintDialog();
                break;
            case R.id.cleanerNamValue:
                showCleanerDetailsDialog(getString(R.string.scr_lbl_cleaner_details_marathi),
                        "Ravi Sharma");
                break;
            case R.id.driverNamValue:
                showCleanerDetailsDialog(getString(R.string.scr_lbl_Driver_details), "Ram Singh");
                break;
            case R.id.imgInfo:
                showInfoDialog();
                break;
            case R.id.notOkayButton:
                showComplaintDialog();
                break;
            case R.id.okayButton:
                finish();
                Intent intent = new Intent(mContext, HisabDetailsActivity.class);
                intent.putExtra(Constants.FROM_SCREEN, Constants.ACCEPTED);
                startActivity(intent);
                break;
            case R.id.tvHisabName:
                //showIssueRecordingDialog();
                break;
            case R.id.layAurJaniye:
                if (mSheetExpandStatus) {
                    //clicked
                    mSheetExpandStatus = false;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 100, getResources()
                                    .getDisplayMetrics());
                    int marginInDp40 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
                                    .getDisplayMetrics());
                    setMargins(layTotalHisab, 0, marginInDp, 0, 0);
                    setMargins(rvDriverHisab, 0, marginInDp40, 0, 0);
                    imgExpandSheet.setBackgroundDrawable(getDrawable(R.drawable.ic_down));
                    layTotalBhatta.setVisibility(View.GONE);

                } else {
                    mSheetExpandStatus = true;
                    int marginInDp230 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 190, getResources()
                                    .getDisplayMetrics());
                    int marginInDp40 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 70, getResources()
                                    .getDisplayMetrics());
                    setMargins(layTotalHisab, 0, marginInDp230, 0, 0);
                    setMargins(rvDriverHisab, 0, marginInDp40, 0, 0);
                    imgExpandSheet.setBackgroundDrawable(getDrawable(R.drawable.ic_up));
                    layTotalBhatta.setVisibility(View.VISIBLE);

                }
                break;

        }
    }

    private void RequestPermissionRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record nd store the audio.
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "OmTuranth");
        mFileName = directory.getPath();
        // we are here initializing our filename variable
        // with the path of the recorded audio file.
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";

        // below method is used to initialize
        // the media recorder clss
        mRecorder = new MediaRecorder();

        // below method is used to set the audio
        // source which we are using a mic.
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // below method is used to set
        // the output format of the audio.
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // below method is used to set the
        // audio encoder for our recorded audio.
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // below method is used to set the
        // output file location for our recorded audio
        mRecorder.setOutputFile(mFileName);
        try {
            // below mwthod will prepare
            // our audio recorder class
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
        // start method will start
        // the audio recording.
        mRecorder.start();
        //statusTV.setText("Recording Started");
    }


    //request camera and storage permission
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                },
                        1000);

            } else {
                RequestPermissionRecording();
            }
        } else {
            //createFolderForLprImages();
            RequestPermissionRecording();

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
                    RequestPermissionRecording();
                } else {
                    //Toast.makeText(mContext, getString(R.string.somthing_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }



    public void playAudio() {
       /* stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
*/
        // for playing our recorded audio
        // we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer.setDataSource(mFileName);

            // below method will prepare our media player
            mPlayer.prepare();

            // below method will start our media player.
            mPlayer.start();
            //statusTV.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
       /* stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
*/
        // below method will stop
        // the audio recording.
        mRecorder.stop();

        // below method will release
        // the media recorder class.
        mRecorder.release();
        mRecorder = null;
        //statusTV.setText("Recording Stopped");
    }

    public void pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;
        /*stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));
        statusTV.setText("Recording Play Stopped");*/
    }
}