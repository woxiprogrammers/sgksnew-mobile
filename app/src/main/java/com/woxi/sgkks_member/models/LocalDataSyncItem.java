package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */

public class LocalDataSyncItem implements Serializable {

    private ArrayList<CityIteam> arrCityItems = new ArrayList<>();
    private ArrayList<CityIteam> arrCityItemsGujarati = new ArrayList<>();

    private ArrayList<MemberDetailsItem> arrMemberDetailsItems = new ArrayList<>();
    private ArrayList<MemberDetailsItem> arrMemberDetailsGujaratiItems = new ArrayList<>();

    private ArrayList<EventDataItem> arrEventDataItem = new ArrayList<>();
    private ArrayList<EventDataItem> arrEventDataGujaratiItem = new ArrayList<>();
    private ArrayList<String> arrEventImageURLs;

    private ArrayList<AccountDetailsItem> arrAccountItem = new ArrayList<>();
    private ArrayList<AccountDetailsItem> arrAccountGujaratiItem = new ArrayList<>();

    private ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems = new ArrayList<>();
    private ArrayList<CommitteeDetailsItem> arrCommitteeDetailsGujaratiItems = new ArrayList<>();

    private ArrayList<MessageDetailsItem> arrMessageDetailsItems = new ArrayList<>();
    private ArrayList<MessageDetailsItem> arrMessageDetailsGujaratiItems = new ArrayList<>();

    private ArrayList<ClassifiedDetailsItem> arrClassifiedItems = new ArrayList<>();
    private ArrayList<ClassifiedDetailsItem> arrClassifiedGujaratiItems = new ArrayList<>();

    public ArrayList<String> getArrEventImageURLs() {
        return arrEventImageURLs;
    }

    public void setArrEventImageURLs(ArrayList<String> arrEventImageURLs) {
        this.arrEventImageURLs = arrEventImageURLs;
    }

    public LocalDataSyncItem() {
    }

    public ArrayList<CityIteam> getArrCityItems() {
        return arrCityItems;
    }

    public void setArrCityItems(ArrayList<CityIteam> arrCityItems) {
        this.arrCityItems = arrCityItems;
    }

    public ArrayList<CityIteam> getArrCityItemsGujarati() {
        return arrCityItemsGujarati;
    }

    public void setArrCityItemsGujarati(ArrayList<CityIteam> arrCityItemsGujarati) {
        this.arrCityItemsGujarati = arrCityItemsGujarati;
    }

    public ArrayList<EventDataItem> getArrEventDataItem() {
        return arrEventDataItem;
    }

    public void setArrEventDataItem(ArrayList<EventDataItem> arrEventDataItem) {
        this.arrEventDataItem = arrEventDataItem;
    }

    public ArrayList<EventDataItem> getArrEventDataGujaratiItem() {
        return arrEventDataGujaratiItem;
    }

    public void setArrEventDataGujaratiItem(ArrayList<EventDataItem> arrEventDataGujaratiItem) {
        this.arrEventDataGujaratiItem = arrEventDataGujaratiItem;
    }

    public ArrayList<AccountDetailsItem> getArrAccountItem() {
        return arrAccountItem;
    }

    public void setArrAccountItem(ArrayList<AccountDetailsItem> arrAccountItem) {
        this.arrAccountItem = arrAccountItem;
    }

    public ArrayList<AccountDetailsItem> getArrAccountGujaratiItem() {
        return arrAccountGujaratiItem;
    }

    public void setArrAccountGujaratiItem(ArrayList<AccountDetailsItem> arrAccountGujaratiItem) {
        this.arrAccountGujaratiItem = arrAccountGujaratiItem;
    }

    public ArrayList<CommitteeDetailsItem> getArrCommitteeDetailsGujaratiItems() {
        return arrCommitteeDetailsGujaratiItems;
    }

    public void setArrCommitteeDetailsGujaratiItems(ArrayList<CommitteeDetailsItem> arrCommitteeDetailsGujaratiItems) {
        this.arrCommitteeDetailsGujaratiItems = arrCommitteeDetailsGujaratiItems;
    }

    public ArrayList<MessageDetailsItem> getArrMessageDetailsGujaratiItems() {
        return arrMessageDetailsGujaratiItems;
    }

    public void setArrMessageDetailsGujaratiItems(ArrayList<MessageDetailsItem> arrMessageDetailsGujaratiItems) {
        this.arrMessageDetailsGujaratiItems = arrMessageDetailsGujaratiItems;
    }

    public ArrayList<ClassifiedDetailsItem> getArrClassifiedItems() {
        return arrClassifiedItems;
    }

    public void setArrClassifiedItems(ArrayList<ClassifiedDetailsItem> arrClassifiedItems) {
        this.arrClassifiedItems = arrClassifiedItems;
    }

    public ArrayList<ClassifiedDetailsItem> getArrClassifiedGujaratiItems() {
        return arrClassifiedGujaratiItems;
    }

    public void setArrClassifiedGujaratiItems(ArrayList<ClassifiedDetailsItem> arrClassifiedGujaratiItems) {
        this.arrClassifiedGujaratiItems = arrClassifiedGujaratiItems;
    }

    public ArrayList<MemberDetailsItem> getArrMemberDetailsGujaratiItems() {
        return arrMemberDetailsGujaratiItems;
    }

    public void setArrMemberDetailsGujaratiItems(ArrayList<MemberDetailsItem> arrMemberDetailsGujaratiItems) {
        this.arrMemberDetailsGujaratiItems = arrMemberDetailsGujaratiItems;
    }

    public ArrayList<MemberDetailsItem> getArrMemberDetailsItems() {
        return arrMemberDetailsItems;
    }

    public void setArrMemberDetailsItems(ArrayList<MemberDetailsItem> arrMemberDetailsItems) {
        this.arrMemberDetailsItems = arrMemberDetailsItems;
    }

    public ArrayList<CommitteeDetailsItem> getArrCommitteeDetailsItems() {
        return arrCommitteeDetailsItems;
    }

    public void setArrCommitteeDetailsItems(ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems) {
        this.arrCommitteeDetailsItems = arrCommitteeDetailsItems;
    }

    public ArrayList<MessageDetailsItem> getArrMessageDetailsItems() {
        return arrMessageDetailsItems;
    }

    public void setArrMessageDetailsItems(ArrayList<MessageDetailsItem> arrMessageDetailsItems) {
        this.arrMessageDetailsItems = arrMessageDetailsItems;
    }
}
