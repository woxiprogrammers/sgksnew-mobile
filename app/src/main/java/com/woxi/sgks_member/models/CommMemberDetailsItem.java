package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class CommMemberDetailsItem implements Serializable {
    private String committeeMemName, committeeMemDesignation, committeeMemImageUrl, committeeMemContact, committeeMemEmail, committeeMemAddress;

    public String getCommitteeMemName() {
        return committeeMemName;
    }

    public void setCommitteeMemName(String committeeMemName) {
        this.committeeMemName = committeeMemName;
    }

    public String getCommitteeMemImageUrl() {
        return committeeMemImageUrl;
    }

    public void setCommitteeMemImageUrl(String committeeMemImageUrl) {
        this.committeeMemImageUrl = committeeMemImageUrl;
    }

    public String getCommitteeMemDesignation() {
        return committeeMemDesignation;
    }

    public void setCommitteeMemDesignation(String committeeMemDesignation) {
        this.committeeMemDesignation = committeeMemDesignation;
    }

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
