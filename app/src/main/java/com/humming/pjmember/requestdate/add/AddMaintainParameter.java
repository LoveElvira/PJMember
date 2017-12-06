package com.humming.pjmember.requestdate.add;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/9/20.
 * 新增设备保养信息
 */

public class AddMaintainParameter implements IRequestMainData {

    /**
     * 参数名称	是否必填	参数说明
     * equipmentId	是	设备ID
     * type	是	保养类型
     * maintainFee	是	保养费用
     * maintainTime	是	保养时间
     * content	是	保养内容
     * maintainDepartment	是	保养单位
     * maintainImg	否	保养图片
     */

    private String equipmentId;
    private String type;
    private String maintainFee;
    private String maintainTime;
    private String content;
    private String maintainDepartment;
    private List<String> maintainImgUrl;

    public AddMaintainParameter() {
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

    public String getMaintainFee() {
        return maintainFee;
    }

    public void setMaintainFee(String maintainFee) {
        this.maintainFee = maintainFee;
    }

    public String getMaintainTime() {
        return maintainTime;
    }

    public void setMaintainTime(String maintainTime) {
        this.maintainTime = maintainTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMaintainDepartment() {
        return maintainDepartment;
    }

    public void setMaintainDepartment(String maintainDepartment) {
        this.maintainDepartment = maintainDepartment;
    }

    public List<String> getMaintainImgUrl() {
        return maintainImgUrl;
    }

    public void setMaintainImgUrl(List<String> maintainImgUrl) {
        this.maintainImgUrl = maintainImgUrl;
    }
}
