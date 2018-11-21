package com.woxi.sgkks_member.home;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.MessageListAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageHomeNewFragment extends Fragment implements AppConstants, FragmentInterface {
    private View mParentView;
    private Context mContext;
    private RecyclerView mRvMessageList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    private DatabaseQueryHandler databaseQueryHandler;
    public static View.OnClickListener onRvItemClickListener;
    private ArrayList<MessageDetailsItem> mArrMessageDetails;
    public MessageHomeNewFragment() {
        // Required empty public constructor
    }

    public static MessageHomeNewFragment newInstance() {
        return new MessageHomeNewFragment();
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
        databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
        mRvMessageList = mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad = mParentView.findViewById(R.id.rlLazyLoad);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMessageList.setLayoutManager(linearLayoutManager);
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            requestMessageList();
        }else {

        }
    }
    private void setUpRecyclerView(ArrayList<MessageDetailsItem> messageDetailsItems){
        mRvMessageList.setHasFixedSize(true);
        mRvAdapter = new MessageListAdapter(messageDetailsItems);
        mRvMessageList.setAdapter(mRvAdapter);
        onRvItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDetailsItem messageDetailsItem = mArrMessageDetails.get(mRvMessageList.getChildAdapterPosition(v));
                Intent intentDetails = new Intent(mContext, MessageDetailsActivity.class);
                intentDetails.putExtra("currentNewsDetail", messageDetailsItem);
                startActivity(intentDetails);
            }
        };
    }

    private void requestMessageList() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,AppURLs.API_MESSAGE_LISTING, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Object resp= AppParser.parseMessageNewResponse(response.toString());
                            if(resp instanceof Boolean){
                                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                            }else if(resp instanceof ArrayList){
                                mArrMessageDetails= (ArrayList<MessageDetailsItem>) resp;
                                setUpRecyclerView(mArrMessageDetails);
                               // databaseQueryHandler.insertOrUpdateAllMessages(mArrMessageDetails,false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
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
