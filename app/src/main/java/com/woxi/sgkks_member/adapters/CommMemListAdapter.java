package com.woxi.sgkks_member.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.models.CommMemSecondDetailItem;
import com.woxi.sgkks_member.models.CommMemberDetailsItem;

import java.util.ArrayList;

/**
 * <b>public class CommMemListAdapter extends BaseExpandableListAdapter</b>
 * <p>This class is used as an adapter for Committee-details - member's listing</p>
 * Created by Rohit.
 */
public class CommMemListAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<CommMemberDetailsItem> arrCommMemberDetails;
    private ArrayList<CommMemSecondDetailItem> arrMemSecondDetail;
    private CommMemberDetailsItem commMemberDetailItem;
    private CommMemSecondDetailItem commMemSecondItem;

    public CommMemListAdapter(Context mContext, ArrayList<CommMemberDetailsItem> arrCommMemberDetails) {
//        this.mContext = mContext;
        this.arrCommMemberDetails = arrCommMemberDetails;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        commMemberDetailItem = new CommMemberDetailsItem();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrMemSecondDetail.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrMemSecondDetail.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        commMemSecondItem = (CommMemSecondDetailItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_member_child, parent, false);
        }
        TextView tv_email =  convertView.findViewById(R.id.tvMemberEmail);
        TextView tv_address = convertView.findViewById(R.id.tvMemberAddress);
        if (commMemSecondItem.getCommitteeMemEmail() != null) {
            tv_email.setText(commMemSecondItem.getCommitteeMemEmail());
        }
        if (commMemSecondItem.getCommitteeMemAddress() != null) {
            tv_address.setText(commMemSecondItem.getCommitteeMemAddress());
        }
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrCommMemberDetails.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (arrCommMemberDetails == null) {
            arrCommMemberDetails = new ArrayList<>();
        }
        return arrCommMemberDetails.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        commMemberDetailItem = (CommMemberDetailsItem) getGroup(groupPosition);
        arrMemSecondDetail = new ArrayList<>();
        commMemSecondItem = new CommMemSecondDetailItem();
        commMemSecondItem.setCommitteeMemContact(commMemberDetailItem.getCommitteeMemContact() + "");
        commMemSecondItem.setCommitteeMemEmail(commMemberDetailItem.getCommitteeMemEmail() + "");
        commMemSecondItem.setCommitteeMemAddress(commMemberDetailItem.getCommitteeMemAddress() + "");
        arrMemSecondDetail.add(commMemSecondItem);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_member_group, parent, false);
        }
        TextView tv_name =  convertView.findViewById(R.id.tvCommMemberName);
        TextView tv_designation =  convertView.findViewById(R.id.tvMemberDesignation);
        tv_name.setText(commMemberDetailItem.getCommitteeMemName());
        tv_designation.setText(commMemberDetailItem.getCommitteeMemDesignation());

        ImageView tvCommMemCall =  convertView.findViewById(R.id.ivCommMemCall);
        if (arrCommMemberDetails.get(groupPosition).getCommitteeMemContact() != null && !arrCommMemberDetails.get(groupPosition).getCommitteeMemContact().equalsIgnoreCase("")) {
            tvCommMemCall.setVisibility(View.VISIBLE);
            tvCommMemCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMobNo = "tel:" + arrCommMemberDetails.get(groupPosition).getCommitteeMemContact();
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse(strMobNo));
                    v.getContext().startActivity(intentCall);
                }
            });
        } else {
            tvCommMemCall.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

