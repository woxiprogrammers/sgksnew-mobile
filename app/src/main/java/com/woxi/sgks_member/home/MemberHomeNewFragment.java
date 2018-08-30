package com.woxi.sgks_member.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.MemberListAdapter;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgks_member.models.MemberDetailsItem;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberHomeNewFragment extends Fragment implements FragmentInterface, AppConstants {
    public static View.OnClickListener onMemberClickListener;
    private Context mContext;
    private RecyclerView mRvMemberList;
    private RecyclerView.Adapter mRvAdapter;
    private View mParentView;
    private EditText mEtMemberSearch;
    private RelativeLayout mPbLazyLoad;
    private String TAG = "MemberHomeFragment";
    private ArrayList<MemberDetailsItem> mArrMemDetails;
    //    private boolean isApiRequested = false;
    private String strSearchTerm = "", strNextPageUrl = "";
    private int arrSize = 0;
    private LinearLayoutManager linearLayoutManager;
    private boolean isApiInProgress = false;
    private DatabaseQueryHandler databaseQueryHandler;
    private String strNoResults = "";

    public MemberHomeNewFragment() {
        // Required empty public constructor
    }

    public static MemberHomeNewFragment newInstance() {
        return new MemberHomeNewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_members_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        mRvMemberList = mParentView.findViewById(R.id.rvMemberList);
        mPbLazyLoad = mParentView.findViewById(R.id.rlLazyLoad);
        mEtMemberSearch = mParentView.findViewById(R.id.etSearchMember);
        new AppCommonMethods(mContext).hideKeyBoard(mEtMemberSearch);
        ;
        mPbLazyLoad.setVisibility(View.GONE);
        databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMemberList.setLayoutManager(linearLayoutManager);
        mEtMemberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 2) {
                    String currentSearch = charSequence.toString().toLowerCase();
                    if (strNoResults.equalsIgnoreCase("") || !strNoResults.matches("^" + currentSearch + "*")) {
                        strNoResults = "";
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        functionToGetMembersList();
//        requestToGetMembersData();
        onMemberClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberDetailsItem memberDetailsItem = mArrMemDetails.get(mRvMemberList.getChildAdapterPosition(v));
                Intent intentDetails = new Intent(mContext, MemberDetailsActivity.class);
                intentDetails.putExtra("currentMemberDetail", memberDetailsItem);
                startActivity(intentDetails);
            }
        };
    }

    private void functionToGetMembersList() {
        boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
        if (!new AppCommonMethods(mContext).isNetworkAvailable()) {
            if (isOfflineSupportEnabled) {
                String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
                mArrMemDetails = databaseQueryHandler.queryMembers(strSearchTerm, "PUNE");
                //If there are no results from local database go online else show local results.
                if (mArrMemDetails == null || mArrMemDetails.size() == 0) {
                    mRvAdapter = new MemberListAdapter(new ArrayList<MemberDetailsItem>());
                    mRvMemberList.setAdapter(mRvAdapter);
                    mRvMemberList.getAdapter().notifyDataSetChanged();
//                searchMemberOnline();
                } else {
                    new AppCommonMethods().LOG(0, TAG + " Local Search", mArrMemDetails.toString());
                    mRvAdapter = new MemberListAdapter(mArrMemDetails);
                    mRvMemberList.setAdapter(mRvAdapter);
                }
            }
        } else {
            requestToGetMembersData();
        }
    }

    @Override
    public void fragmentBecameVisible() {
        mParentView.findViewById(R.id.etSearchMember).clearFocus();
    }

    private void requestToGetMembersData() {
        String url = "http://www.mocky.io/v2/5b87c73a2e0000710c05fae1";
        final JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Object resp = AppParser.parseNewMemberList(response.toString());
                            ArrayList<MemberDetailsItem> mNextArrMemDetails = (ArrayList<MemberDetailsItem>) resp;
                            if (resp instanceof Boolean) {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            } else if (resp instanceof ArrayList) {
                                if (mNextArrMemDetails.size() > 0) {
                                    mRvMemberList.setHasFixedSize(true);
                                    mRvAdapter = new MemberListAdapter(mNextArrMemDetails);
                                    mRvMemberList.setAdapter(mRvAdapter);
                                    databaseQueryHandler.insertOrUpdateAllMembers(mNextArrMemDetails);
                                } else {
                                    Toast.makeText(mContext, "No Record Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req, "messageList");
    }
}
