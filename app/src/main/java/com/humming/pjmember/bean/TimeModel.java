package com.humming.pjmember.bean;

/**
 * Created by Elvira on 2017/9/20.
 */

public class TimeModel {
    private boolean isSelect;
    private String time;

    public TimeModel() {
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
