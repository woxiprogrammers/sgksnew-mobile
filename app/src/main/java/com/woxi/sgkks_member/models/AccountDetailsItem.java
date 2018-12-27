package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class AccountDetailsItem implements Serializable</b>
 * <p>This class is used as an Model class for Account Details</p>
 * Created by Rohit.
 */
public class AccountDetailsItem implements Serializable {

    private String strAccountName, strAccountDescription, strId, strAccountId, strCity, strCityId, strIsActive, strYear;
    private ArrayList<AccountImages> imagesList;

    public String getStrYear() {
        return strYear;
    }

    public void setStrYear(String strYear) {
        this.strYear = strYear;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrAccountId() {
        return strAccountId;
    }

    public void setStrAccountId(String strAccountId) {
        this.strAccountId = strAccountId;
    }

    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getStrCityId() {
        return strCityId;
    }

    public void setStrCityId(String strCityId) {
        this.strCityId = strCityId;
    }

    public String getStrIsActive() {
        return strIsActive;
    }

    public void setStrIsActive(String strIsActive) {
        this.strIsActive = strIsActive;
    }

    public String getStrAccountName() {
        return strAccountName;
    }

    public void setStrAccountName(String strAccountName) {
        this.strAccountName = strAccountName;
    }

    public ArrayList<AccountImages> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<AccountImages> imagesList) {
        this.imagesList = imagesList;
    }

    public String getStrAccountDescription() {
        return strAccountDescription;
    }

    public void setStrAccountDescription(String strAccountDescription) {
        this.strAccountDescription = strAccountDescription;
    }

}
