package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class MemberDetailsItem implements Serializable</b>
 * <p>This class is used as a Model class </p>
 * Created by Rohit.
 */
public class MemberDetailsItem implements Serializable {
   private String strId;
    private String strFirstName;
    private String strMiddleName;
    private String strLastName;
    private String strCity;
    private String strCityId;
    private String strEmail;
    private String strMemberId;
    private String strGender;
    private String strMobileNumber;
    private String strDateOfBirth;
    private String strLatitude;
    private String strLongitude;
    private String strMemberImageUrl;
    private String strBloodGroup;
    private String strBloodGroupId;
    private String strAddress;
    private String strCreatedAt;
    private String strUpdatedAt;
    private String strLanguageId;
    private String bolIsActive;
    private ArrayList<MemberDetailsItem> arrMemberList;
    private ArrayList<MemberDetailsItem> arrMemberListGujarati;

    public String getBolIsActive() {
        return bolIsActive;
    }

    public void setBolIsActive(String bolIsActive) {
        this.bolIsActive = bolIsActive;
    }

    public String getStrLanguageId() {
        return strLanguageId;
    }

    public void setStrLanguageId(String strLanguageId) {
        this.strLanguageId = strLanguageId;
    }

    public ArrayList<MemberDetailsItem> getArrMemberListGujarati() {
        return arrMemberListGujarati;
    }

    public void setArrMemberListGujarati(ArrayList<MemberDetailsItem> arrMemberListGujarati) {
        this.arrMemberListGujarati = arrMemberListGujarati;
    }

    public ArrayList<MemberDetailsItem> getArrMemberList() {
        return arrMemberList;
    }

    public void setArrMemberList(ArrayList<MemberDetailsItem> arrMemberList) {
        this.arrMemberList = arrMemberList;
    }

    public String getStrBloodGroupId() {
        return strBloodGroupId;
    }

    public void setStrBloodGroupId(String strBloodGroupId) {
        this.strBloodGroupId = strBloodGroupId;
    }


    public String getStrCityId() {
        return strCityId;
    }

    public void setStrCityId(String strCityId) {
        this.strCityId = strCityId;
    }

    public String getStrCreatedAt() {
        return strCreatedAt;
    }

    public void setStrCreatedAt(String strCreatedAt) {
        this.strCreatedAt = strCreatedAt;
    }

    public String getStrUpdatedAt() {
        return strUpdatedAt;
    }

    public void setStrUpdatedAt(String strUpdatedAt) {
        this.strUpdatedAt = strUpdatedAt;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrMiddleName() {
        return strMiddleName;
    }

    public void setStrMiddleName(String strMiddleName) {
        this.strMiddleName = strMiddleName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getStrMemberId() {
        return strMemberId;
    }

    public void setStrMemberId(String strMemberId) {
        this.strMemberId = strMemberId;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    public String getStrMobileNumber() {
        return strMobileNumber;
    }

    public void setStrMobileNumber(String strMobileNumber) {
        this.strMobileNumber = strMobileNumber;
    }

    public String getStrDateOfBirth() {
        return strDateOfBirth;
    }

    public void setStrDateOfBirth(String strDateOfBirth) {
        this.strDateOfBirth = strDateOfBirth;
    }

    public String getStrLatitude() {
        return strLatitude;
    }

    public void setStrLatitude(String strLatitude) {
        this.strLatitude = strLatitude;
    }

    public String getStrLongitude() {
        return strLongitude;
    }

    public void setStrLongitude(String strLongitude) {
        this.strLongitude = strLongitude;
    }

    public String getStrMemberImageUrl() {
        return strMemberImageUrl;
    }

    public void setStrMemberImageUrl(String strMemberImageUrl) {
        this.strMemberImageUrl = strMemberImageUrl;
    }

    public String getStrBloodGroup() {
        return strBloodGroup;
    }

    public void setStrBloodGroup(String strBloodGroup) {
        this.strBloodGroup = strBloodGroup;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

}
