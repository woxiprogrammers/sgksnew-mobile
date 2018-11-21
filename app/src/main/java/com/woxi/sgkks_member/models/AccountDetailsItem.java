package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class AccountDetailsItem implements Serializable</b>
 * <p>This class is used as an Model class for Account Details</p>
 * Created by Rohit.
 */
public class AccountDetailsItem implements Serializable {

    private String strAccountName, strAccountDescription;

    private ArrayList<AccountImages> imagesList;

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
