package com.woxi.sgks_member.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.ClassifiedHomeFragment;
import com.woxi.sgks_member.home.ClassifiedHomeNewFragment;
import com.woxi.sgks_member.models.ClassifiedDetailsItem;

import java.util.ArrayList;

/**
 * <b>public class ClassifiedListAdapter extends RecyclerView.Adapter<ClassifiedListAdapter.ItemHolder></b>
 * <p>This class is used as an Adapter for Classified List</p>
 * Created by Rohit.
 */

public class ClassifiedListAdapter extends RecyclerView.Adapter<ClassifiedListAdapter.ItemHolder> {
    private ArrayList<ClassifiedDetailsItem> mArrClassifiedDetails;

    public ClassifiedListAdapter(ArrayList<ClassifiedDetailsItem> mArrClassifiedDetails) {
        this.mArrClassifiedDetails = mArrClassifiedDetails;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classified_listing, parent, false);
        itemViewMain.setOnClickListener(ClassifiedHomeNewFragment.onRvItemClickListener);
        return new ClassifiedListAdapter.ItemHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Context mContext = holder.mContext;
        ImageView ivClassifiedListImg = holder.ivClassifiedListImg;
        TextView tvTitle = holder.tvTitle;
//      TextView tvDescription = holder.tvDescription;
        if (mArrClassifiedDetails.get(position).getClassifiedTitle() != null) {
            tvTitle.setText(mArrClassifiedDetails.get(position).getClassifiedTitle());
        }
        //Loading image from url.
        String strUrl = mArrClassifiedDetails.get(position).getArrClassifiedImages().get(0);
        Glide.with(mContext)
                .load(strUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(ivClassifiedListImg);
        /*if (mArrClassifiedDetails.get(position).getClassifiedDescription() != null) {
            tvDescription.setText(mArrClassifiedDetails.get(position).getClassifiedDescription());
        }*/
    }

    @Override
    public int getItemCount() {
        return mArrClassifiedDetails.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        Context mContext;
        ImageView ivClassifiedListImg;
        TextView tvTitle;
//        TextView tvDescription;

        ItemHolder(View itemView) {
            super(itemView);
            this.mContext = itemView.getContext();
            this.ivClassifiedListImg =  itemView.findViewById(R.id.ivNewsListImg);
            this.tvTitle =  itemView.findViewById(R.id.tvNewsListTitle);
//            this.tvDescription = (TextView) itemView.findViewById(R.id.tvNewsListDescription);
        }
    }
}
