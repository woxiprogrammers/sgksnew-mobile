package com.woxi.sgks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */

public class LocalDataSyncItem implements Serializable {
    private ArrayList<FamilyDetailsItem> arrFamilyDetailsItems = new ArrayList<>();
    private ArrayList<MemberDetailsItem> arrMemberDetailsItems = new ArrayList<>();
    private ArrayList<MemberAddressItem> arrMemberAddressItems = new ArrayList<>();
    private ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems = new ArrayList<>();
    private ArrayList<MessageDetailsItem> arrMessageDetailsItems = new ArrayList<>();

    public LocalDataSyncItem() {
    }

    public ArrayList<FamilyDetailsItem> getArrFamilyDetailsItems() {
        return arrFamilyDetailsItems;
    }

    public void setArrFamilyDetailsItems(ArrayList<FamilyDetailsItem> arrFamilyDetailsItems) {
        this.arrFamilyDetailsItems = arrFamilyDetailsItems;
    }

    public ArrayList<MemberDetailsItem> getArrMemberDetailsItems() {
        return arrMemberDetailsItems;
    }

    public void setArrMemberDetailsItems(ArrayList<MemberDetailsItem> arrMemberDetailsItems) {
        this.arrMemberDetailsItems = arrMemberDetailsItems;
    }

    public ArrayList<MemberAddressItem> getArrMemberAddressItems() {
        return arrMemberAddressItems;
    }

    public void setArrMemberAddressItems(ArrayList<MemberAddressItem> arrMemberAddressItems) {
        this.arrMemberAddressItems = arrMemberAddressItems;
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
