package com.woxi.sgkks_member.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.EventHomeFragment;
import com.woxi.sgkks_member.models.EventDataItem;

import java.util.ArrayList;

/**
 * <b>public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventItemHolder></b>
 * <p>This class is used as an Adapter for Event Listing</p>
 * Created by Rohit.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventItemHolder> {
    private ArrayList<EventDataItem> mArrEventData;

    public EventsListAdapter(ArrayList<EventDataItem> arrEventData) {
        mArrEventData = arrEventData;
    }

    @Override
    public EventItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_list, parent, false);
        itemViewMain.setOnClickListener(EventHomeFragment.onEventClickListener);
        return new EventsListAdapter.EventItemHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(EventItemHolder holder, int position) {
        if (mArrEventData.get(position).getEventName() != null) {
            holder.mTvEventsName.setText(mArrEventData.get(position).getEventName());
        }
        if (mArrEventData.get(position).getEventDate() != null) {
            holder.mTvEventsDate.setText(mArrEventData.get(position).getEventDate());
        }
        if(mArrEventData.get(position).getCity() != null){
            holder.mTvEventsCity.setText("City: "+mArrEventData.get(position).getCity());
        }
    }

    @Override
    public int getItemCount() {
        return mArrEventData.size();
    }

    class EventItemHolder extends RecyclerView.ViewHolder {
        TextView mTvEventsName, mTvEventsDate, mTvEventsCity;

        EventItemHolder(View itemView) {
            super(itemView);
            mTvEventsName =  itemView.findViewById(R.id.tvEventsName);
            mTvEventsDate =  itemView.findViewById(R.id.tvEventsDate);
            mTvEventsCity =  itemView.findViewById(R.id.tvEventsCity);
        }
    }
}
