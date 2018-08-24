package com.woxi.sgks_member.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.CommitteeHomeFragment;
import com.woxi.sgks_member.models.CommitteeDetailsItem;

import java.util.ArrayList;

/**
 * <b>public class CommitteeListsAdapter extends RecyclerView.Adapter<CommitteeListsAdapter.ItemHolder></b>
 * <p>This class is used as an Adapter for Committee Listing</p>
 * Created by Rohit.
 */
public class CommitteeListsAdapter extends RecyclerView.Adapter<CommitteeListsAdapter.ItemHolder> {
    private ArrayList<CommitteeDetailsItem> mArrMainCommList;

    public CommitteeListsAdapter(ArrayList<CommitteeDetailsItem> arrMainCommList) {
        mArrMainCommList = arrMainCommList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commitee_list, parent, false);
        itemViewMain.setOnClickListener(CommitteeHomeFragment.onCommitteeClickListener);
        return new CommitteeListsAdapter.ItemHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.committeeName.setText(mArrMainCommList.get(position).getCommitteeName().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return mArrMainCommList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView committeeName;

        ItemHolder(View itemView) {
            super(itemView);
            committeeName = (TextView) itemView.findViewById(R.id.tvCommitteeName);
        }
    }
}
