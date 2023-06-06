package com.ominfo.staff_original.ui.upload_pod.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseApplication;
import com.ominfo.staff_original.basecontrol.BaseFragment;
import com.ominfo.staff_original.database.AppDatabase;
import com.ominfo.staff_original.network.ViewModelFactory;
import com.ominfo.staff_original.ui.upload_pod.SelectetUploadPODActivity;
import com.ominfo.staff_original.ui.upload_pod.adapter.PendingForUploadAdapter;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingPDSForUpload#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingPDSForUpload extends BaseFragment  {

    private Context mContext;

    @BindView(R.id.empty_layoutActivity)
    LinearLayoutCompat emptylayoutActivity;

    @BindView(R.id.rvLrNumber)
    RecyclerView rvLrNumber;

    @BindView(R.id.tv_emptyLayTitle)
    AppCompatTextView tv_emptyLayTitle;

    @Inject
    ViewModelFactory mViewModelFactory;

    private AppDatabase mDb;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public PendingPDSForUpload() {
        // Required empty public constructor
    }

    public static PendingPDSForUpload newInstance(String param1, String param2) {
        PendingPDSForUpload fragment = new PendingPDSForUpload();
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
        View view = inflater.inflate(R.layout.fragment_pending_for_upload, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SelectetUploadPODActivity) mContext).getDeps().inject(this);
        mDb = BaseApplication.getInstance().getAppDatabase();

        fetchDBData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchDBData();
                callPodSaveOfLRApi();
            }
        });


    }

    private void fetchDBData(  ) {
        List<UploadPodRequest> uploadPodRequests = mDb.getDbDAO().getPdsList();
        setAdapterForLrImageList( uploadPodRequests );
    }

    private void setAdapterForLrImageList(List<UploadPodRequest> uploadPodRequests) {

        if(uploadPodRequests.size()==0) {
            tv_emptyLayTitle.setText( getContext().getString(R.string.scr_lbl_no_data_available ) );
            setVisibleLayout( false );
            return;
        }else
            setVisibleLayout( true );

        PendingForUploadAdapter pendingForUploadAdapter = new PendingForUploadAdapter( mContext , uploadPodRequests);

        rvLrNumber.setHasFixedSize(true);
        rvLrNumber.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvLrNumber.setAdapter(pendingForUploadAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }



}
