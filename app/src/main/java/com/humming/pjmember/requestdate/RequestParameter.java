package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

/**
 * Created by Elvira on 2017/9/8.
 */

public class RequestParameter implements IRequestMainData {

    //登录
    private String phone;
    private String pwd;

    //获取设备信息 ID  获取事故、维修 记录的ID
    private String equipmentId;

    //获取作 type: 1--今日作业  2--未完成作业  3--全部作业
    private String type;
    //第几页 获取第几页数据，首页不用传
    private String pagable;

    //获取维修记录详情 id
    private String repairId;

    public RequestParameter() {
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
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
