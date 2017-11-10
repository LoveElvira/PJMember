package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/9/20.
 * 新增设备维修信息
 */

public class AddRepairParameter implements IRequestMainData {
    /**
     * 参数名称	是否必填	参数说明
     * equipmentId	是	设备ID
     * repairFee	是	维修费用
     * repairTime	是	维修时间
     * reason	    是  维修原因
     * repairDepartment	是	维修单位
     * repairImg	否	维修图片
     */

    private String equipmentId;
    private String repairFee;
    private String repairTime;
    private String reason;
    private String repairDepartment;
    private List<String> repairImgUrl;

    public AddRepairParameter() {
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(String repairFee) {
        this.repairFee = repairFee;
    }

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRepairDepartment() {
        return repairDepartment;
    }

    public void setRepairDepartment(String repairDepartment) {
        this.repairDepartment = repairDepartment;
    }

    public List<String> getRepairImgUrl() {
        return repairImgUrl;
    }

    public void setRepairImgUrl(List<String> repairImgUrl) {
        this.repairImgUrl = repairImgUrl;
    }
}
