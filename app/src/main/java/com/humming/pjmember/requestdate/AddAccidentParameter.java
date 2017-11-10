package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/9/20.
 * 新增设备事故信息
 */

public class AddAccidentParameter implements IRequestMainData {

    /**
     * 参数名称	是否必填	参数说明
     * equipmentId	是	设备ID
     * type	是	事故类型
     * lossFee	是	损失金额
     * accidentTime	是	事故日期
     * nature	是	事故性质
     * remark	是	备注
     */

    private String equipmentId;
    private String type;
    private String lossFee;
    private String accidentTime;
    private String nature;
    private String remark;
    private List<String> accidentUrl;

    public AddAccidentParameter() {
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

    public String getLossFee() {
        return lossFee;
    }

    public void setLossFee(String lossFee) {
        this.lossFee = lossFee;
    }

    public String getAccidentTime() {
        return accidentTime;
    }

    public void setAccidentTime(String accidentTime) {
        this.accidentTime = accidentTime;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getAccidentUrl() {
        return accidentUrl;
    }

    public void setAccidentUrl(List<String> accidentUrl) {
        this.accidentUrl = accidentUrl;
    }
}
