package com.woxi.sgks_member.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.MemberHomeFragment;
import com.woxi.sgks_member.models.MemberDetailsItem;

import java.util.ArrayList;

/**
 * <b><b>public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemDetailsHolder> </b></b>
 * <p>This class is used as an adapter for App-home Member-listing </p>
 * Created by Rohit.
 *
 * Modified By Sharvari
 */
public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemDetailsHolder> {
    private ArrayList<MemberDetailsItem> mArrMemDetails;

    public MemberListAdapter(ArrayList<MemberDetailsItem> mArrMemDetails) {
        this.mArrMemDetails = mArrMemDetails;
        Log.i("@@1", String.valueOf(mArrMemDetails.size()));
    }

    @Override
    public MemDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_listing, parent, false);
        itemViewMain.setOnClickListener(MemberHomeFragment.onMemberClickListener);
        return new MemDetailsHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(final MemDetailsHolder holder, int position) {
        Log.i("@@","OnBindViewHolder");
        final ImageView memberImage = holder.memberImage;
        final Context mContext = holder.mContext;
        TextView memberName = holder.memberName;
        TextView memberAddress = holder.memberAddress;
        TextView memberID = holder.memberID;
        TextView memberLocation = holder.memberLocation;

        final TextView memberCallNo = holder.memberCallNo;
        String strFName = "", strMName = "", strSurname = "";
        if (mArrMemDetails.get(position).getMemFirstName() != null) {
            strFName = mArrMemDetails.get(position).getMemFirstName();
        }
        if (mArrMemDetails.get(position).getMemFirstName() != null) {
            strMName = mArrMemDetails.get(position).getMemMidName();
        }
        if (mArrMemDetails.get(position).getMemFirstName() != null) {
            strSurname = mArrMemDetails.get(position).getMemSurname();
        }
        final String strFullName = strFName + " " + strMName + " " + strSurname;
        memberName.setText(strFullName);

        if (mArrMemDetails.get(position).getMemSgksArea() != null) {
            memberAddress.setText(mArrMemDetails.get(position).getMemSgksArea() + ", " + mArrMemDetails.get(position).getMemSgksMainCity());
        }

        if (mArrMemDetails.get(position).getMemSgksMainCity() != null) {
            memberID.setText("SGKS ID : " + "SGKS-" + mArrMemDetails.get(position).getMemSgksMainCity() + "-" + mArrMemDetails.get(position).getMemID());
        }

        if (mArrMemDetails.get(position).getMemLatitude() != null && !mArrMemDetails.get(position).getMemLatitude().equalsIgnoreCase("null")) {
            final String strLatitude = mArrMemDetails.get(position).getMemLatitude();
            final String strLongitude = mArrMemDetails.get(position).getMemLongitude();
            memberLocation.setVisibility(View.VISIBLE);
            memberLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!strLatitude.equalsIgnoreCase("") && !strLongitude.equalsIgnoreCase("")) {
                        String uriBegin = "geo:" + strLongitude + "," + strLatitude;
                        String query = strLongitude + "," + strLatitude + "(" + strFullName + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uriLocation = Uri.parse(uriString);
                        final Intent intent = new Intent(Intent.ACTION_VIEW, uriLocation);
                        mContext.startActivity(intent);
                    }
                }
            });
        } else {
            memberLocation.setVisibility(View.GONE);
        }
        if (mArrMemDetails.get(position).getMemMobile() != null && !mArrMemDetails.get(position).getMemMobile().equalsIgnoreCase("null")) {
            memberCallNo.setVisibility(View.VISIBLE);
            memberCallNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMobNo = "tel:" + mArrMemDetails.get(holder.getAdapterPosition()).getMemMobile();
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse(strMobNo));
                    v.getContext().startActivity(intentCall);
                }
            });
        } else {
            memberCallNo.setVisibility(View.GONE);
        }
        //Loading member image from url.
        memberImage.setImageDrawable(null);
        String strUrl = mArrMemDetails.get(position).getMemberImageURL();
        if (strUrl != null && !strUrl.equalsIgnoreCase("")) {
            Glide.with(mContext)
                    .load(strUrl)
                    .asBitmap()
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_profile)
                    .into(new BitmapImageViewTarget(memberImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            memberImage.setImageDrawable(circularBitmapDrawable);
                            memberImage.setBackground(null);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        Log.i("@@2", String.valueOf(mArrMemDetails.size()));
        return mArrMemDetails.size();
    }

    class MemDetailsHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        Context mContext;
        TextView memberName, memberAddress, memberCallNo, memberID, memberLocation;

        MemDetailsHolder(View view) {
            super(view);
            this.mContext = view.getContext();
            this.memberImage =  view.findViewById(R.id.ivMemberImage);
            this.memberName =  view.findViewById(R.id.tvMemberName);
            this.memberAddress =  view.findViewById(R.id.tvMemberAddress);
            this.memberCallNo =  view.findViewById(R.id.tvMemberCallNo);
            this.memberLocation =  view.findViewById(R.id.tvMemberLocation);
            this.memberID = view.findViewById(R.id.tvMemberID);
        }
    }
}