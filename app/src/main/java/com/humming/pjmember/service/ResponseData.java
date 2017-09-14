package com.humming.pjmember.service;

/**
 * Created by Elvira on 2017/6/1.
 */

public class ResponseData {
    private int statusCode;

    public ResponseData() {
    }

    public ResponseData(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
