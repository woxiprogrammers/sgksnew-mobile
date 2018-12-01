package com.woxi.sgkks_member.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.SelectCityActivity;
import com.woxi.sgkks_member.models.CityIteam;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityListHolder>{
    private ArrayList<CityIteam> arrCityList;

    public CityAdapter(ArrayList<CityIteam> arrCityList) {
        this.arrCityList = arrCityList;
    }

    @Override
    public CityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_list, parent, false);
        itemViewMain.setOnClickListener(SelectCityActivity.onCityClickListener);
        return new CityListHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(CityListHolder holder, int position) {
        TextView tvCityName = holder.tvCity;
        TextView tvCityMemberCount = holder.tvMemberCount;
        TextView tvMemberCountInt = holder.tvMemberCountInt;
        String strCityName = "",strCityMemberCount = "";
        if(arrCityList.get(position).getStrCityName() != null ){
            strCityName = arrCityList.get(position).getStrCityName();
            tvCityName.setText(strCityName);
        }
        if(arrCityList.get(position).getStrMemberCount() != null){
            strCityMemberCount = arrCityList.get(position).getStrMemberCount();
            tvCityMemberCount.setText("Members Count : ");
            tvMemberCountInt.setText(strCityMemberCount);
        }
    }

    @Override
    public int getItemCount() {
        return arrCityList.size();
    }

    class CityListHolder extends RecyclerView.ViewHolder {
        Context mContext;
        TextView  tvCity, tvMemberCount, tvMemberCountInt;

        CityListHolder(View view) {
            super(view);
            this.mContext = view.getContext();
            this.tvCity =  view.findViewById(R.id.cardTvCity);
            this.tvMemberCount = view.findViewById(R.id.cardTvMemberCount);
            this.tvMemberCountInt = view.findViewById(R.id.cardTvMemberCountInt);

        }
    }
}
