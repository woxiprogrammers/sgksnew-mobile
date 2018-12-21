package com.woxi.sgkks_member.models;

public class CityIteam {
    int intCityId;
    String strCityName;
    String strMemberCount;
    String strStateId;
    String is_active;
    String id, languageId;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getStrStateId() {
        return strStateId;
    }

    public void setStrStateId(String strStateId) {
        this.strStateId = strStateId;
    }

    public String getStrMemberCount() {
        return strMemberCount;
    }

    public void setStrMemberCount(String strMemberCount) {
        this.strMemberCount = strMemberCount;
    }

    public int getIntCityId() {
        return intCityId;
    }

    public void setIntCityId(int intCityId) {
        this.intCityId = intCityId;
    }

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }
}
