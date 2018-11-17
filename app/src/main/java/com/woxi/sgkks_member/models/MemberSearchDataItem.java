package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */

public class MemberSearchDataItem implements Serializable {
    private String strNextPageUrl;
    private ArrayList<MemberDetailsItem> arrMemberList;

    public MemberSearchDataItem() {
    }

    public String getStrNextPageUrl() {
        return strNextPageUrl;
    }

    public void setStrNextPageUrl(String strNextPageUrl) {
        this.strNextPageUrl = strNextPageUrl;
    }

    public ArrayList<MemberDetailsItem> getArrMemberList() {
        return arrMemberList;
    }

    public void setArrMemberList(ArrayList<MemberDetailsItem> arrMemberList) {
        this.arrMemberList = arrMemberList;
    }
}
