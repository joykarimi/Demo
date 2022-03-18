package com.example.project;

import java.io.Serializable;

public class ScheduleTaskModel implements Serializable {

    int year, month, dayOfMonth, hourOfDay, minute;
    String workerUid, userUid, address, workerName, jobType, taskUid;
    boolean taskDone;
    public ScheduleTaskModel() {}

    public ScheduleTaskModel(int year, int month, int dayOfMonth, int hourOfDay, int minute, String workerUid, String userUid, String address, String workerName, String jobType) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.workerUid = workerUid;
        this.userUid = userUid;
        this.address = address;
        this.workerName = workerName;
        this.jobType = jobType;
        taskDone = false;
    }

    public void setTaskUid(String taskUid) {
        this.taskUid = taskUid;
    }

    public String getTaskUid() {
        return taskUid;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    public boolean getTaskDone(){
        return taskDone;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerName() {
        return workerName;
    }
    public int getYear() {
        return year;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public String getWorkerUid() {
        return workerUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setWorkerUid(String workerUid) {
        this.workerUid = workerUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
