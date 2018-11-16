package com.woxi.sgkks_member.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class MessageAndClassifiedResponseItem implements Serializable {
    private ArrayList<ClassifiedDetailsItem> arrClassifiedList;
    private ArrayList<MessageDetailsItem> arrMessageList;

    public MessageAndClassifiedResponseItem() {
    }

    public ArrayList<MessageDetailsItem> getArrMessageList() {
        return arrMessageList;
    }

    public void setArrMessageList(ArrayList<MessageDetailsItem> arrMessageList) {
        this.arrMessageList = arrMessageList;
    }

    public ArrayList<ClassifiedDetailsItem> getArrClassifiedList() {
        return arrClassifiedList;
    }

    public void setArrClassifiedList(ArrayList<ClassifiedDetailsItem> arrClassifiedList) {
        this.arrClassifiedList = arrClassifiedList;
    }
}
