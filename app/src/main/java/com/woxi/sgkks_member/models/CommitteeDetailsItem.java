package com.woxi.sgkks_member.models;

import java.io.Serializable;

/**
 * <b><b> public class CommitteeDetailsItem implements Serializable</b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class CommitteeDetailsItem implements Serializable {
    private String committeeName, committeeDescription, committeeCity, committeeID, commAllMembers, commIsActive;

    public CommitteeDetailsItem() {
    }

    public String getCommitteeCity() {
        return committeeCity;
    }

    public void setCommitteeCity(String committeeCity) {
        this.committeeCity = committeeCity;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    public String getCommitteeDescription() {
        return committeeDescription;
    }

    public void setCommitteeDescription(String committeeDescription) {
        this.committeeDescription = committeeDescription;
    }

    public String getCommitteeID() {
        return committeeID;
    }

    public void setCommitteeID(String committeeID) {
        this.committeeID = committeeID;
    }

    public String getCommAllMembers() {
        return commAllMembers;
    }

    public void setCommAllMembers(String commAllMembers) {
        this.commAllMembers = commAllMembers;
    }

    public String getCommIsActive() {
        return commIsActive;
    }

    public void setCommIsActive(String commIsActive) {
        this.commIsActive = commIsActive;
    }
}
