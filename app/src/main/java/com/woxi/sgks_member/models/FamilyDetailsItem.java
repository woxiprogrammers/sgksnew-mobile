package com.woxi.sgks_member.models;

import java.io.Serializable;

/**
 * This class is used as a Model class
 * Created by Rohit.
 */
public class FamilyDetailsItem implements Serializable {
    private String family_id, sgks_family_id, surname, native_place, sgks_city;

    public FamilyDetailsItem() {
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getSgks_family_id() {
        return sgks_family_id;
    }

    public void setSgks_family_id(String sgks_family_id) {
        this.sgks_family_id = sgks_family_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNative_place() {
        return native_place;
    }

    public void setNative_place(String native_place) {
        this.native_place = native_place;
    }

    public String getSgks_city() {
        return sgks_city;
    }

    public void setSgks_city(String sgks_city) {
        this.sgks_city = sgks_city;
    }
}
