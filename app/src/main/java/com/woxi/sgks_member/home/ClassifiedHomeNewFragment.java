package com.woxi.sgks_member.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.ClassifiedListAdapter;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.models.ClassifiedDetailsItem;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Created By Sharvari
 */
public class ClassifiedHomeNewFragment extends Fragment implements FragmentInterface {
    private View mParentView;
    private Context mContext;
    private boolean isClassifiedApiRequested = false;
    private boolean isClassifiedApiInProgress = false;
    private RecyclerView mRvClassifiedList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    public static View.OnClickListener onRvItemClickListener;
    private ArrayList<ClassifiedDetailsItem> mArrClassifiedDetails;
    private String TAG = "ClassifiedHomeFragment";
    private String messageNextPageUrl = "";
    private int intClassifiedArraySize = 0;
    public ClassifiedHomeNewFragment() {
        // Required empty public constructor
    }

    public static ClassifiedHomeNewFragment newInstance() {
        return new ClassifiedHomeNewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_messages_classified_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        mRvClassifiedList =  mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad =  mParentView.findViewById(R.id.rlLazyLoad);
        mPbLazyLoad.setVisibility(View.GONE);
        mRvClassifiedList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvClassifiedList.setLayoutManager(linearLayoutManager);
        requestToGetClassifiedList();

    }
    private void setUpRecyclerView() {
        mRvAdapter = new ClassifiedListAdapter(mArrClassifiedDetails);
        mRvClassifiedList.setAdapter(mRvAdapter);
        onRvItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassifiedDetailsItem classifiedDetailsItem = mArrClassifiedDetails.get(mRvClassifiedList.getChildAdapterPosition(v));
                Intent intentDetails = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                intentDetails.putExtra("currentClassifiedDetail", classifiedDetailsItem);
                startActivity(intentDetails);
            }
        };
    }
    private void requestToGetClassifiedList(){
        //ToDO PageID
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,AppURLs.API_CLASSIFIED_LISTING, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Object resp= AppParser.parseClassifiedResponse(response.toString());
                            if(resp instanceof Boolean){
                                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                            }else if(resp instanceof ArrayList){
                                mArrClassifiedDetails= (ArrayList<ClassifiedDetailsItem>) resp;
                                setUpRecyclerView();
//                                setUpRecyclerView(messageDetailsItems);
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

    @Override
    public void fragmentBecameVisible() {
    }
}
