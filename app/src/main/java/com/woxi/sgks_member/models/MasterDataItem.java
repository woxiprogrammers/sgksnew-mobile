package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */

public class MasterDataItem implements Serializable {
    private String strSgksBuzz_Id;
    private String strSgksBuzz_ImgUrl;
    private String strSgksArea;
    private String strSuggestionCategory;
    private String strMessageIds;
    private String strClassifiedIds;
    private int intTotalMemberCount;

    public MasterDataItem() {
    }

    public String getStrSgksBuzz_Id() {
        return strSgksBuzz_Id;
    }

    public void setStrSgksBuzz_Id(String strSgksBuzz_Id) {
        this.strSgksBuzz_Id = strSgksBuzz_Id;
    }

    public String getStrSgksBuzz_ImgUrl() {
        return strSgksBuzz_ImgUrl;
    }

    public void setStrSgksBuzz_ImgUrl(String strSgksBuzz_ImgUrl) {
        this.strSgksBuzz_ImgUrl = strSgksBuzz_ImgUrl;
    }

    public String getStrSgksArea() {
        return strSgksArea;
    }

    public void setStrSgksArea(String strSgksArea) {
        this.strSgksArea = strSgksArea;
    }

    public String getStrSuggestionCategory() {
        return strSuggestionCategory;
    }

    public void setStrSuggestionCategory(String strSuggestionCategory) {
        this.strSuggestionCategory = strSuggestionCategory;
    }

    public String getStrMessageIds() {
        return strMessageIds;
    }

    public void setStrMessageIds(String strMessageIds) {
        this.strMessageIds = strMessageIds;
    }

    public int getIntTotalMemberCount() {
        return intTotalMemberCount;
    }

    public void setIntTotalMemberCount(int intTotalMemberCount) {
        this.intTotalMemberCount = intTotalMemberCount;
    }

    public String getStrClassifiedIds() {
        return strClassifiedIds;
    }

    public void setStrClassifiedIds(String strClassifiedIds) {
        this.strClassifiedIds = strClassifiedIds;
    }
}
