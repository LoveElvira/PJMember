package com.humming.pjmember.requestdate.add;

import com.humming.pjmember.service.IRequestMainData;

/**
 * Created by Elvira on 2017/12/6.
 * 新增设备保险信息
 */

public class AddInsuranceParameter implements IRequestMainData {
    /**
     * equipmentId	是	设备ID
     * insuranceName	是	保险名称
     * orderNo	是	保险单号
     * assuredMan	是	被保险人
     * applicantMan	是	投保人
     * type	是	保险种类
     * startTime	是	有效期始
     * endTime	是	有效期止
     * money	是	保险费用额
     */
    private String equipmentId;
    private String insuranceName;
    private String orderNo;
    private String assuredMan;
    private String applicantMan;
    private String type;
    private String startTime;
    private String endTime;
    private String money;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAssuredMan() {
        return assuredMan;
    }

    public void setAssuredMan(String assuredMan) {
        this.assuredMan = assuredMan;
    }

    public String getApplicantMan() {
        return applicantMan;
    }

    public void setApplicantMan(String applicantMan) {
        this.applicantMan = applicantMan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
