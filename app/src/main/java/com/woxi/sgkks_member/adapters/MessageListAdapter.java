package com.woxi.sgkks_member.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.MessageHomeFragment;
import com.woxi.sgkks_member.home.MessageHomeNewFragment;
import com.woxi.sgkks_member.models.MessageDetailsItem;

import java.util.ArrayList;

/**
 * <b><b>public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.NewsDetailsHolder> </b></b>
 * <p>This class is used as an Adapter for News Listing</p>
 * Created by Rohit.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.NewsDetailsHolder> {
    private ArrayList<MessageDetailsItem> mArrNewsDetails;

    public MessageListAdapter(ArrayList<MessageDetailsItem> mArrNewsDetails) {
        this.mArrNewsDetails = mArrNewsDetails;
    }

    @Override
    public MessageListAdapter.NewsDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_listing, parent, false);
        itemViewMain.setOnClickListener(MessageHomeNewFragment.onRvItemClickListener);
        return new MessageListAdapter.NewsDetailsHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(MessageListAdapter.NewsDetailsHolder holder, int position) {
        final Context mContext = holder.mContext;
        ImageView ivNewsImg = holder.ivNewsImg;
        final ImageView ivNewsListImg = holder.ivNewsListImg;
        TextView tvNewsListTitle = holder.tvNewsListTitle;
        TextView tvNewsListDescription = holder.tvNewsListDescription;
        TextView tvNewsCreatedAt = holder.tvNewsCreatedAt;

        if (mArrNewsDetails.get(position).getMessageTitle() != null) {
            tvNewsListTitle.setText(mArrNewsDetails.get(position).getMessageTitle());
        }

        //Loading image from url.
        String strUrl = mArrNewsDetails.get(position).getMessageImage();
        Glide.with(mContext)
                .load(strUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(ivNewsListImg);

        if (mArrNewsDetails.get(position).getMessageDescription() != null) {
            tvNewsListDescription.setText(mArrNewsDetails.get(position).getMessageDescription());
        }
        if (mArrNewsDetails.get(position).getMessageDate() != null) {
            tvNewsCreatedAt.setText("Date : " + mArrNewsDetails.get(position).getMessageDate());
        }

        //News Types- birthday, nidhan, achievement , buzz.
        //birthday-red, nidhan-black, achvement-golden, buzz-blue
        if (mArrNewsDetails.get(position).getMessageType() != null) {
            if (mArrNewsDetails.get(position).getMessageType().equalsIgnoreCase(mContext.getString(R.string.news_type_birthday))) {
                ivNewsImg.setColorFilter(ContextCompat.getColor(mContext, R.color.colorRed));
            } else if (mArrNewsDetails.get(position).getMessageType().equalsIgnoreCase(mContext.getString(R.string.news_type_nidhan))) {
                ivNewsImg.setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlack));
            } else if (mArrNewsDetails.get(position).getMessageType().equalsIgnoreCase(mContext.getString(R.string.news_type_achievement))) {
                ivNewsImg.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGolden));
            } else if (mArrNewsDetails.get(position).getMessageType().equalsIgnoreCase(mContext.getString(R.string.news_type_general))) {
                ivNewsImg.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGreen));
            } else {
                //else buzz
                ivNewsImg.setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlueMedium));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mArrNewsDetails.size();
    }

    class NewsDetailsHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView ivNewsListImg;
        TextView tvNewsListTitle, tvNewsListDescription, tvNewsCreatedAt;
        ImageView ivNewsImg;

        NewsDetailsHolder(View itemView) {
            super(itemView);
            this.mContext = itemView.getContext();
            this.ivNewsListImg =  itemView.findViewById(R.id.ivNewsListImg);
            this.tvNewsListTitle = itemView.findViewById(R.id.tvNewsListTitle);
            this.tvNewsListDescription =  itemView.findViewById(R.id.tvNewsListDescription);
            this.tvNewsCreatedAt =  itemView.findViewById(R.id.tvNewsCreatedAt);
            this.ivNewsImg =  itemView.findViewById(R.id.ivNewsImg);
        }
    }
}
