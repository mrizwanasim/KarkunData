package com.karkun.recyclerViewAdapterAndRealmDatabase;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Windows on 31/01/2018.
 */

public class KarkunData extends RealmObject{

    @PrimaryKey
    private long id;
    private String name, age, address, department, phoneNumber, pictures;

    public KarkunData() {
    }

    public KarkunData(long id, String name, String age, String address, String department, String phoneNumber, String pictures) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.pictures = pictures;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }
}
