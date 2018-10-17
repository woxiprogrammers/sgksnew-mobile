package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class CommMemSecondDetailItem implements Serializable {
    private String committeeMemContact, committeeMemEmail, committeeMemAddress;


    public String getCommitteeMemContact() {
        return committeeMemContact;
    }

    public void setCommitteeMemContact(String committeeMemContact) {
        this.committeeMemContact = committeeMemContact;
    }

    public String getCommitteeMemEmail() {
        return committeeMemEmail;
    }

    public void setCommitteeMemEmail(String committeeMemEmail) {
        this.committeeMemEmail = committeeMemEmail;
    }

    public String getCommitteeMemAddress() {
        return committeeMemAddress;
    }

    public void setCommitteeMemAddress(String committeeMemAddress) {
        this.committeeMemAddress = committeeMemAddress;
    }
}

