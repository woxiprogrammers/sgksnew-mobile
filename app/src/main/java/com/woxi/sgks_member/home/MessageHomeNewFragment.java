package com.woxi.sgks_member.home;

import android.content.Context;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.MessageListAdapter;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;

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
        Log.i("@@","Frag");
        mContext = getActivity();
        mRvMessageList = mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad = mParentView.findViewById(R.id.rlLazyLoad);

        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            requestMessageList();
        }else {

        }
    }
    private void setUpRecyclerView(ArrayList<MessageDetailsItem> messageDetailsItems){
        mRvMessageList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMessageList.setLayoutManager(linearLayoutManager);
        mRvAdapter = new MessageListAdapter(messageDetailsItems);
        mRvMessageList.setAdapter(mRvAdapter);
    }

    private void requestMessageList()
    {
        String url="http://www.mocky.io/v2/5b866cad3400005e068b5590";
        final JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Object resp= AppParser.parseMessageNewResponse(response.toString());
                            if(resp instanceof Boolean){
                                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();

                            }else if(resp instanceof ArrayList){
                                ArrayList<MessageDetailsItem> messageDetailsItems= (ArrayList<MessageDetailsItem>) resp;
                                setUpRecyclerView(messageDetailsItems);

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
