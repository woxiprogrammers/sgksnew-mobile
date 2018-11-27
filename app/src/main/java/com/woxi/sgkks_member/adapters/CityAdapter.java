package com.woxi.sgkks_member.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxi.sgkks_member.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityListHolder>{


    @Override
    public CityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CityListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CityListHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;
        Context mContext;
        TextView  tvCity;

        CityListHolder(View view) {
            super(view);
            this.mContext = view.getContext();
            this.tvCity =  view.findViewById(R.id.cardTvCity);

        }
    }
}
