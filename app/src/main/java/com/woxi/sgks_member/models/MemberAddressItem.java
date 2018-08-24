package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class MemberAddressItem implements Serializable {
    private String addFamilyId, addAddressId, addAddressLine, addArea, addLandMark, addCity, addState, addCountry, addPincode;

    public MemberAddressItem() {
    }

    public String getAddFamilyId() {
        return addFamilyId;
    }

    public void setAddFamilyId(String addFamilyId) {
        this.addFamilyId = addFamilyId;
    }

    public String getAddAddressId() {
        return addAddressId;
    }

    public void setAddAddressId(String addAddressId) {
        this.addAddressId = addAddressId;
    }

    public String getAddAddressLine() {
        return addAddressLine;
    }

    public void setAddAddressLine(String addAddressLine) {
        this.addAddressLine = addAddressLine;
    }

    public String getAddArea() {
        return addArea;
    }

    public void setAddArea(String addArea) {
        this.addArea = addArea;
    }

    public String getAddLandMark() {
        return addLandMark;
    }

    public void setAddLandMark(String addLandMark) {
        this.addLandMark = addLandMark;
    }

    public String getAddCity() {
        return addCity;
    }

    public void setAddCity(String addCity) {
        this.addCity = addCity;
    }

    public String getAddState() {
        return addState;
    }

    public void setAddState(String addState) {
        this.addState = addState;
    }

    public String getAddCountry() {
        return addCountry;
    }

    public void setAddCountry(String addCountry) {
        this.addCountry = addCountry;
    }

    public String getAddPincode() {
        return addPincode;
    }

    public void setAddPincode(String addPincode) {
        this.addPincode = addPincode;
    }
}
