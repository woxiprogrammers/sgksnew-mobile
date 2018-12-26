package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class ClassifiedDetailsItem implements Serializable</b>
 * <p>This class is used as Modal class </p>
 * Created by Rohit.
 */

public class ClassifiedDetailsItem implements Serializable {
    private String classifiedID;
    private String classifiedTitle;
    private String classifiedDescription;
    private String classifiedPackage;
    private String classifiedType;
    private String classifiedCreateDate;
    private String classifiedCity;
    private String id;
    private String isActive;
    private String cityId;
    private ArrayList<String> arrClassifiedImages;
    private ArrayList<ClassifiedDetailsItem> arrClassifiedList;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ClassifiedDetailsItem> getArrClassifiedList() {
        return arrClassifiedList;
    }

    public void setArrClassifiedList(ArrayList<ClassifiedDetailsItem> arrClassifiedList) {
        this.arrClassifiedList = arrClassifiedList;
    }

    public String getClassifiedID() {
        return classifiedID;
    }

    public void setClassifiedID(String classifiedID) {
        this.classifiedID = classifiedID;
    }

    public String getClassifiedTitle() {
        return classifiedTitle;
    }

    public void setClassifiedTitle(String classifiedTitle) {
        this.classifiedTitle = classifiedTitle;
    }

    public String getClassifiedDescription() {
        return classifiedDescription;
    }

    public void setClassifiedDescription(String classifiedDescription) {
        this.classifiedDescription = classifiedDescription;
    }

    public String getClassifiedPackage() {
        return classifiedPackage;
    }

    public void setClassifiedPackage(String classifiedPackage) {
        this.classifiedPackage = classifiedPackage;
    }

    public String getClassifiedType() {
        return classifiedType;
    }

    public void setClassifiedType(String classifiedType) {
        this.classifiedType = classifiedType;
    }

    public String getClassifiedCreateDate() {
        return classifiedCreateDate;
    }

    public void setClassifiedCreateDate(String classifiedCreateDate) {
        this.classifiedCreateDate = classifiedCreateDate;
    }

    public String getClassifiedCity() {
        return classifiedCity;
    }

    public void setClassifiedCity(String classifiedCity) {
        this.classifiedCity = classifiedCity;
    }

    public ArrayList<String> getArrClassifiedImages() {
        return arrClassifiedImages;
    }

    public void setArrClassifiedImages(ArrayList<String> arrClassifiedImages) {
        this.arrClassifiedImages = arrClassifiedImages;
    }
}
