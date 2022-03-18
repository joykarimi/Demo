package com.example.project;

import java.io.Serializable;

public class Workers implements Serializable {

    private String name, type, uid;
    boolean isAcquired;

    public Workers(){}
    public Workers(String name, String type){
        this.name = name;
        this.type = type;
        isAcquired = false;
        uid = "none";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAcquired(boolean acquired) {
        isAcquired = acquired;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public boolean getAcquired(){
        return isAcquired;
    }

    public String getUid() {
        return uid;
    }
}
