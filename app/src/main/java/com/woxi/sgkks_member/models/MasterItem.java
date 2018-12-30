package com.woxi.sgkks_member.models;

public class MasterItem {
    String strBuzzImageUrl;
    int intBuzzId, intClassifiedCount, intMessagesCount;

    public String getStrBuzzImageUrl() {
        return strBuzzImageUrl;
    }

    public void setStrBuzzImageUrl(String strBuzzImageUrl) {
        this.strBuzzImageUrl = strBuzzImageUrl;
    }

    public int getIntBuzzId() {
        return intBuzzId;
    }

    public void setIntBuzzId(int intBuzzId) {
        this.intBuzzId = intBuzzId;
    }

    public int getIntClassifiedCount() {
        return intClassifiedCount;
    }

    public void setIntClassifiedCount(int intClassifiedCount) {
        this.intClassifiedCount = intClassifiedCount;
    }

    public int getIntMessagesCount() {
        return intMessagesCount;
    }

    public void setIntMessagesCount(int intMessagesCount) {
        this.intMessagesCount = intMessagesCount;
    }
}
