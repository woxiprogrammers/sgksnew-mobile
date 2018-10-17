package com.woxi.sgks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class ClassifiedDetailsItem implements Serializable</b>
 * <p>This class is used as Modal class </p>
 * Created by Rohit.
 */

public class ClassifiedDetailsItem implements Serializable {
    private String classifiedID, classifiedTitle, classifiedDescription, classifiedPackage, classifiedType, classifiedCreateDate, classifiedCity;
    private ArrayList<String> arrClassifiedImages;

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
