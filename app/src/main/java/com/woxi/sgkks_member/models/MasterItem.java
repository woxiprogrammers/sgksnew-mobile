package com.woxi.sgkks_member.models;

public class MasterItem {
    String strBuzzImageUrl;
    int intBuzzId;
    boolean isAddEditEnabled;

    public boolean isAddEditEnabled() {
        return isAddEditEnabled;
    }

    public void setAddEditEnabled(boolean addEditEnabled) {
        isAddEditEnabled = addEditEnabled;
    }

    public String getStrBuzzImageUrl() {
        return strBuzzImageUrl;
    }

    public void setStrBuzzImageUrl(String strBuzzImageUrl) {
        this.strBuzzImageUrl = strBuzzImageUrl;
    }

    public int getIntBuzzId() {
        return intBuzzId;
    }

    public void setIntBuzzId(int intBuzzId) {
        this.intBuzzId = intBuzzId;
    }


}
