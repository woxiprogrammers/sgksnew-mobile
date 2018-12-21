package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class MessageDetailsItem implements Serializable {
    private String messageID;
    private String messageTitle;
    private String messageDescription;
    private String messageImage;
    private String messageCreateDate;
    private String messageType;
    private String messageCity;
    private String messageIsActive;
    private String id;
    private String langugeId;
    private String messageCityId;
    private String isActive;
    private String messageDate;
    private String messageTypeId;

    public String getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(String messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLangugeId() {
        return langugeId;
    }

    public void setLangugeId(String langugeId) {
        this.langugeId = langugeId;
    }

    public String getMessageCityId() {
        return messageCityId;
    }

    public void setMessageCityId(String messageCityId) {
        this.messageCityId = messageCityId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }


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
