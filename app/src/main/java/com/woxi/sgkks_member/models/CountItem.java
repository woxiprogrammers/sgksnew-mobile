package com.woxi.sgkks_member.models;

public class CountItem {
    String strCityName, strCityId;
    int intMessageCount;
    int intClassifiedCount;

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    public String getStrCityId() {
        return strCityId;
    }

    public void setStrCityId(String strCityId) {
        this.strCityId = strCityId;
    }

    public int getIntMessageCount() {
        return intMessageCount;
    }

    public void setIntMessageCount(int intMessageCount) {
        this.intMessageCount = intMessageCount;
    }

    public int getIntClassifiedCount() {
        return intClassifiedCount;
    }

    public void setIntClassifiedCount(int intClassifiedCount) {
        this.intClassifiedCount = intClassifiedCount;
    }
}
