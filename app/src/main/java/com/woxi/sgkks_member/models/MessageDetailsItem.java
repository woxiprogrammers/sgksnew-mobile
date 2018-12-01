package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class MessageDetailsItem implements Serializable {
    private String messageID, messageTitle, messageDescription, messageImage, messageCreateDate, messageType, messageCity, messageIsActive;
    private ArrayList<MessageDetailsItem> arrMessageList;

    public ArrayList<MessageDetailsItem> getArrMessageList() {
        return arrMessageList;
    }

    public void setArrMessageList(ArrayList<MessageDetailsItem> arrMessageList) {
        this.arrMessageList = arrMessageList;
    }

    public MessageDetailsItem() {
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public String getMessageCreateDate() {
        return messageCreateDate;
    }

    public void setMessageCreateDate(String messageCreateDate) {
        this.messageCreateDate = messageCreateDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageCity() {
        return messageCity;
    }

    public void setMessageCity(String messageCity) {
        this.messageCity = messageCity;
    }

    public String getMessageIsActive() {
        return messageIsActive;
    }

    public void setMessageIsActive(String messageIsActive) {
        this.messageIsActive = messageIsActive;
    }
}
