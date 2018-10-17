package com.woxi.sgks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */

public class AccountYearItem implements Serializable {
    private String strYear;
    private ArrayList<AccountDetailsItem> arrAccountDetails;

    public String getStrYear() {
        return strYear;
    }

    public void setStrYear(String strYear) {
        this.strYear = strYear;
    }

    public ArrayList<AccountDetailsItem> getArrAccountDetails() {
        return arrAccountDetails;
    }

    public void setArrAccountDetails(ArrayList<AccountDetailsItem> arrAccountDetails) {
        this.arrAccountDetails = arrAccountDetails;
    }
}