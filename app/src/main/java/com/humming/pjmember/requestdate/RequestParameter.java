package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

/**
 * Created by Elvira on 2017/9/8.
 */

public class RequestParameter implements IRequestMainData {

    //登录
    private String phone;
    private String pwd;

    //获取设备信息 ID
    private String equipmentId;

    //获取作 type: 1--今日作业  2--未完成作业  3--全部作业
    private String type;

    public RequestParameter() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
