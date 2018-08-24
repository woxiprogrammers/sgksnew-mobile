package com.woxi.sgks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class MemberDetailsItem implements Serializable</b>
 * <p>This class is used as a Model class </p>
 * Created by Rohit.
 */
public class MemberDetailsItem implements Serializable {
    private String memID, memFamilyId, memSgksFamilyId, memSgksMemberId, address_id;
    private String memFirstName, memMidName, memSurname, memNativePlace, memSgksMainCity, memMosalVillage, memberImageURL;
    private String memGender, memMobile, memEmail, memBloodGroup, memMaritalStatus, memBirthDate, memMarriageDate, memOccupation;
    private String memSgksArea, memLatitude, memLongitude, memIsActive;
    private ArrayList<String> arrMemHobbies, arrMemEducation;
    private MemberAddressItem memAddress;

    public MemberDetailsItem() {
    }

    public String getMemFirstName() {
        return memFirstName;
    }

    public void setMemFirstName(String memFirstName) {
        this.memFirstName = memFirstName;
    }

    public String getMemberImageURL() {
        return memberImageURL;
    }

    public void setMemberImageURL(String memberImageURL) {
        this.memberImageURL = memberImageURL;
    }

    public String getMemEmail() {
        return memEmail;
    }

    public void setMemEmail(String memEmail) {
        this.memEmail = memEmail;
    }

    public String getMemBirthDate() {
        return memBirthDate;
    }

    public void setMemBirthDate(String memBirthDate) {
        this.memBirthDate = memBirthDate;
    }

    public String getMemBloodGroup() {
        return memBloodGroup;
    }

    public void setMemBloodGroup(String memBloodGroup) {
        this.memBloodGroup = memBloodGroup;
    }

    public String getMemMaritalStatus() {
        return memMaritalStatus;
    }

    public void setMemMaritalStatus(String memMaritalStatus) {
        this.memMaritalStatus = memMaritalStatus;
    }

    public String getMemMarriageDate() {
        return memMarriageDate;
    }

    public void setMemMarriageDate(String memMarriageDate) {
        this.memMarriageDate = memMarriageDate;
    }

    public String getMemSurname() {
        return memSurname;
    }

    public void setMemSurname(String memSurname) {
        this.memSurname = memSurname;
    }

    public String getMemNativePlace() {
        return memNativePlace;
    }

    public void setMemNativePlace(String memNativePlace) {
        this.memNativePlace = memNativePlace;
    }

    public String getMemSgksMainCity() {
        return memSgksMainCity;
    }

    public void setMemSgksMainCity(String memSgksMainCity) {
        this.memSgksMainCity = memSgksMainCity;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getMemMidName() {
        return memMidName;
    }

    public void setMemMidName(String memMidName) {
        this.memMidName = memMidName;
    }

    public String getMemGender() {
        return memGender;
    }

    public void setMemGender(String memGender) {
        this.memGender = memGender;
    }

    public String getMemMobile() {
        return memMobile;
    }

    public void setMemMobile(String memMobile) {
        this.memMobile = memMobile;
    }

    public String getMemSgksArea() {
        return memSgksArea;
    }

    public void setMemSgksArea(String memSgksArea) {
        this.memSgksArea = memSgksArea;
    }

    public String getMemLatitude() {
        return memLatitude;
    }

    public void setMemLatitude(String memLatitude) {
        this.memLatitude = memLatitude;
    }

    public String getMemLongitude() {
        return memLongitude;
    }

    public void setMemLongitude(String memLongitude) {
        this.memLongitude = memLongitude;
    }

    public String getMemMosalVillage() {
        return memMosalVillage;
    }

    public void setMemMosalVillage(String memMosalVillage) {
        this.memMosalVillage = memMosalVillage;
    }

    public String getMemOccupation() {
        return memOccupation;
    }

    public void setMemOccupation(String memOccupation) {
        this.memOccupation = memOccupation;
    }

    public MemberAddressItem getMemAddress() {
        return memAddress;
    }

    public void setMemAddress(MemberAddressItem memAddress) {
        this.memAddress = memAddress;
    }

    public ArrayList<String> getArrMemHobbies() {
        return arrMemHobbies;
    }

    public void setArrMemHobbies(ArrayList<String> arrMemHobbies) {
        this.arrMemHobbies = arrMemHobbies;
    }

    public ArrayList<String> getArrMemEducation() {
        return arrMemEducation;
    }

    public void setArrMemEducation(ArrayList<String> arrMemEducation) {
        this.arrMemEducation = arrMemEducation;
    }

    public String getMemFamilyId() {
        return memFamilyId;
    }

    public void setMemFamilyId(String memFamilyId) {
        this.memFamilyId = memFamilyId;
    }

    public String getMemSgksFamilyId() {
        return memSgksFamilyId;
    }

    public void setMemSgksFamilyId(String memSgksFamilyId) {
        this.memSgksFamilyId = memSgksFamilyId;
    }

    public String getMemSgksMemberId() {
        return memSgksMemberId;
    }

    public void setMemSgksMemberId(String memSgksMemberId) {
        this.memSgksMemberId = memSgksMemberId;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getMemIsActive() {
        return memIsActive;
    }

    public void setMemIsActive(String memIsActive) {
        this.memIsActive = memIsActive;
    }
}
