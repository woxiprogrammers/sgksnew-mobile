package com.woxi.sgkks_member.models;

import java.util.ArrayList;

public class MemberOfflineItem {
    int pageNumber;
    ArrayList <MemberDetailsItem> arrMemberDetails;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ArrayList<MemberDetailsItem> getArrMemberDetails() {
        return arrMemberDetails;
    }

    public void setArrMemberDetails(ArrayList<MemberDetailsItem> arrMemberDetails) {
        this.arrMemberDetails = arrMemberDetails;
    }
}
