package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <b>public class EventDataItem implements Serializable</b>
 * <p>This class is used for getter setter method of each item in event module </p>
 * Created by - Abhay.
 * Modified by - Rohit
 */

public class EventDataItem implements Serializable {
    private String eventName, eventYear;
    private String eventDescription;
    private String eventDate;
    private String city,citryId;
    private String venue;
    private ArrayList<String> arrEventImageURLs;
    private String strEventId;
    private String strId;
    private String strlanguageId;
    private String strIsActive;

    public String getCitryId() {
        return citryId;
    }

    public void setCitryId(String citryId) {
        this.citryId = citryId;
    }

    public String getStrIsActive() {
        return strIsActive;
    }

    public void setStrIsActive(String strIsActive) {
        this.strIsActive = strIsActive;
    }

    public String getStrlanguageId() {
        return strlanguageId;
    }

    public void setStrlanguageId(String strlanguageId) {
        this.strlanguageId = strlanguageId;
    }

    public String getStrEventId() {
        return strEventId;
    }

    public void setStrEventId(String strEventId) {
        this.strEventId = strEventId;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public ArrayList<String> getArrEventImageURLs() {
        return arrEventImageURLs;
    }

    public void setArrEventImageURLs(ArrayList<String> arrEventImageURLs) {
        this.arrEventImageURLs = arrEventImageURLs;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventYear() {
        return eventYear;
    }

    public void setEventYear(String eventYear) {
        this.eventYear = eventYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
