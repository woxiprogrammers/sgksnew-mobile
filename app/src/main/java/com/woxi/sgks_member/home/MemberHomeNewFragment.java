package com.woxi.sgks_member.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woxi.sgks_member.R;
import com.woxi.sgks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgks_member.models.MemberDetailsItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberHomeNewFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_members_home, container, false);
        return mParentView;

    }
}
