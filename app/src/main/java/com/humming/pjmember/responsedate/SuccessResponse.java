package com.humming.pjmember.responsedate;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/9/21.
 */

public class SuccessResponse implements Serializable{
    /**
     * "code": 1, //是否成功，0:失败1:成功
     * "msg": "上传成功" //信息提示
     */
    private int code;
    private String msg;

    public SuccessResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
