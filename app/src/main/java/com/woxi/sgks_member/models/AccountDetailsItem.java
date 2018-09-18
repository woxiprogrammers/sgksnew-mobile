package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * <b>public class AccountDetailsItem implements Serializable</b>
 * <p>This class is used as an Model class for Account Details</p>
 * Created by Rohit.
 */
public class AccountDetailsItem implements Serializable {

    private String strAccountImageUrl, strAccountName;
    public String getStrAccountImageUrl() {
        return strAccountImageUrl;
    }
    public void setStrAccountImageUrl(String strAccountImageUrl) {
        this.strAccountImageUrl = strAccountImageUrl;
    }
    public String getStrAccountName() {
        return strAccountName;
    }
    public void setStrAccountName(String strAccountName) {
        this.strAccountName = strAccountName;
    }
}
