package com.woxi.sgks_member.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.EventAndClassifiedDetailActivity;
import com.woxi.sgks_member.miscellaneous.AccountsActivity;
import com.woxi.sgks_member.models.AccountDetailsItem;

import java.util.ArrayList;

/**
 * <b>public class AccountAndEventDetailsAdapter extends RecyclerView.Adapter<AccountAndEventDetailsAdapter.DetailsHolder></b>
 * <p>This class is used as an Adapter of Images in Grid for Account and Event Screen</p>
 * Created by Rohit.
 */
public class AccountAndEventDetailsAdapter extends RecyclerView.Adapter<AccountAndEventDetailsAdapter.DetailsHolder> {
    private ArrayList<AccountDetailsItem> mArrAccountDetails;
    private ArrayList<String> mArrEventImageUrls;
    private boolean isFromEvent; //'true' if from event, else 'false'

    /**
     * Constructor to be used in Account Details Activity
     *
     * @param arrAccountDetails arrAccountDetails
     *                          Created By - Rohit
     */
    public AccountAndEventDetailsAdapter(ArrayList<AccountDetailsItem> arrAccountDetails) {
        mArrAccountDetails = arrAccountDetails;
        isFromEvent = false;
    }

    /**
     * Constructor to be used in Event Details Activity
     *
     * @param isFromEvent       true if from event, else false
     * @param arrEventImageUrls arrEventImageUrls
     */
    public AccountAndEventDetailsAdapter(boolean isFromEvent, ArrayList<String> arrEventImageUrls) {
        this.isFromEvent = isFromEvent;
        this.mArrEventImageUrls = arrEventImageUrls;
    }

    @Override
    public DetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the row layout and initialize the View Holder.
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_event_classified_images, parent, false);
        if (isFromEvent) {
            itemViewMain.setOnClickListener(EventAndClassifiedDetailActivity.onGridImageClickListener);
        } else {
            itemViewMain.setOnClickListener(AccountsActivity.onGridImageClickListener);
        }
        return new DetailsHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(DetailsHolder holder, int position) {
        String strImageUrl = "";
        if (isFromEvent) {
            strImageUrl = mArrEventImageUrls.get(position);
            holder.mRlImageName.setVisibility(View.GONE);
        } else {
            holder.mRlImageName.setVisibility(View.VISIBLE);
            if (mArrAccountDetails.get(position).getStrAccountImageUrl() != null) {
                strImageUrl = mArrAccountDetails.get(position).getStrAccountImageUrl();
            }
            if (mArrAccountDetails.get(position).getStrAccountName() != null) {
                holder.mTvImageName.setText(mArrAccountDetails.get(position).getStrAccountName());
            }
        }

        //Loading image from url.
        Glide.with(holder.mContext)
                .load(strImageUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(holder.accountImage);
    }

    @Override
    public int getItemCount() {
        if (isFromEvent) {
            return mArrEventImageUrls.size();
        } else {
            return mArrAccountDetails.size();
        }
    }

    class DetailsHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView accountImage;
        TextView mTvImageName;
        RelativeLayout mRlImageName;

        DetailsHolder(View itemView) {
            super(itemView);
            this.mContext = itemView.getContext();
            this.accountImage = (ImageView) itemView.findViewById(R.id.ivAccountImage);
            this.mTvImageName = (TextView) itemView.findViewById(R.id.tvAccountTitle);
            this.mRlImageName = (RelativeLayout) itemView.findViewById(R.id.rlImageName);
        }
    }
}
