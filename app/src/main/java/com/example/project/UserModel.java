package com.example.project;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String name, pno, email, password, house, street, sector, area, city, province, imageRef;

    public UserModel() {}

    public UserModel(String name, String pno, String email, String password, String house, String street, String sector, String area, String city, String province){
        this.name = name;
        this.pno = pno;
        this.email = email;
        this.password = password;
        this.house = house;
        this.street = street;
        this.sector = sector;
        this.area = area;
        this.city = city;
        this.province = province;
        imageRef = "none";
    }

    public UserModel(String name, String pno, String email, String password){
        this.name = name;
        this.pno = pno;
        this.email = email;
        this.password = password;
        imageRef = "none";
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPassword() {
        return password;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getHouse() {
        return house;
    }

    public String getName() {
        return name;
    }

    public String getPno() {
        return pno;
    }

    public String getProvince() {
        return province;
    }

    public String getSector() {
        return sector;
    }

    public String getStreet() {
        return street;
    }
}
