package com.ominfo.staff_original.ui.contacts;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.ominfo.staff_original.R;
import com.ominfo.staff_original.basecontrol.BaseActivity;
import com.ominfo.staff_original.ui.contacts.model.CallExpandableListAdapter;
import com.ominfo.staff_original.ui.contacts.model.ExpandableListDataItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllContactsActivity extends BaseActivity {

    Context mContext;
    @BindView(R.id.toolbarTitle)
    AppCompatTextView toolbarTitle;
    @BindView(R.id.expandableListViewSample)
    ExpandableListView expandableListViewExample;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.tvSearchView)
    AppCompatTextView textViewSearch;

    //Expandable list
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    LinkedHashMap<String, List<String>> expandableDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        //for full screen toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = this;
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        setToolbar();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    textViewSearch.setVisibility(View.VISIBLE);
                }
                else {
                    textViewSearch.setVisibility(View.GONE);
                }
            }
        });
        setExpandableList();
    }

    private void setToolbar(){
        toolbarTitle.setText(R.string.scr_lbl_branch_contacts);
        initToolbar(1,mContext,R.id.imgBack,R.id.imgReport,R.id.imgNotify,0,R.id.imgCall);
    }

    private void setExpandableList(){
        expandableDetailList = ExpandableListDataItems.getData();
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CallExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        expandableListViewExample.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the child in any group is clicked
        // via a toast method, it is shown to display the selected child item as a sample
        // we may need to add further steps according to the requirements
        expandableListViewExample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                        + " -> "
                        + expandableDetailList.get(
                        expandableTitleList.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT
                ).show();*/
                showCallDetailsDialog(expandableDetailList.get(
                        expandableTitleList.get(groupPosition)).get(
                        childPosition));
                return false;
            }
        });

    }

    //show call manager popup
    public void showCallDetailsDialog(String callName) {
        Dialog mDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mDialog.setContentView(R.layout.dialog_contact_details);
        AppCompatImageView mClose = mDialog.findViewById(R.id.imgCancel);
        AppCompatButton mapsButton = mDialog.findViewById(R.id.MapsButton);
        AppCompatTextView tvCallName = mDialog.findViewById(R.id.tvCallName);
        AppCompatTextView tvCleanerName = mDialog.findViewById(R.id.tvCleanerName);
        RelativeLayout layCall1 = mDialog.findViewById(R.id.layCall1);
        RelativeLayout layCall2 = mDialog.findViewById(R.id.layCall2);
        AppCompatTextView tvCaller1 = mDialog.findViewById(R.id.tvCaller1);
        AppCompatTextView tvCaller2 = mDialog.findViewById(R.id.tvCaller2);
        try {
            String[] separated = callName.split("\\(");
            String[] separated1 = separated[1].split("\\)");
            tvCleanerName.setText(separated1[0]);
            tvCallName.setText(separated[0]);
        }catch (Exception e){
            tvCallName.setText(callName);
        }
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                String url = "http://bit.ly/2VEM3sC";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        layCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                setCallDialor(tvCaller1.getText().toString().trim());
            }
        });
        layCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                setCallDialor(tvCaller2.getText().toString().trim());
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



    @Override
    public void onResume() {
        super.onResume();
        callPodSaveOfLRApi();
    }


    //perform click actions
    @OnClick({/*R.id.imgBack*/})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
           /* case R.id.imgBack:
                 finish();
                break;
*/
        }
    }

    }
