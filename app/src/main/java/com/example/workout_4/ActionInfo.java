package com.example.workout_4;

import java.io.Serializable;

public class ActionInfo implements Serializable{
    boolean isSelected;
    String userName;
    int flag;
    String finish;
    String spRecord;
    int totalWork;


    public ActionInfo(boolean isSelected, String userName, int flag, String finish, String spRecord, int totalWork) {
        this.isSelected = isSelected;
        this.userName = userName;
        this.flag = flag;
        this.finish = finish;
        this.spRecord = spRecord;
        this.totalWork = totalWork;
    }


    public String getSpRecord() {
        return spRecord;
    }

    public void setSpRecord(String spRecord) {
        this.spRecord = spRecord;
    }

    public int getTotalWork() {
        return totalWork;
    }

    public void setTotalWork(int totalWork) {
        this.totalWork = totalWork;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
