package com.humming.pjmember.requestdate.add;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/12/6.
 * 新增设备用油信息
 */

public class AddUserOilParameter implements IRequestMainData {
    /**
     * equipmentId	是	设备ID
     * makeupOilQuantity	是	加油量
     * makeupOilCard	是	加油卡号
     * makeupOilTime	是	加油时间
     * makeupOilAddr	是	加油地点
     * money	是	金额
     * oilImgs	是	图片列表
     */
    private String equipmentId;
    private String makeupOilQuantity;
    private String makeupOilCard;
    private String makeupOilTime;
    private String makeupOilAddr;
    private String money;
    private List<String> oilImgs;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getMakeupOilQuantity() {
        return makeupOilQuantity;
    }

    public void setMakeupOilQuantity(String makeupOilQuantity) {
        this.makeupOilQuantity = makeupOilQuantity;
    }

    public String getMakeupOilCard() {
        return makeupOilCard;
    }

    public void setMakeupOilCard(String makeupOilCard) {
        this.makeupOilCard = makeupOilCard;
    }

    public String getMakeupOilTime() {
        return makeupOilTime;
    }

    public void setMakeupOilTime(String makeupOilTime) {
        this.makeupOilTime = makeupOilTime;
    }

    public String getMakeupOilAddr() {
        return makeupOilAddr;
    }

    public void setMakeupOilAddr(String makeupOilAddr) {
        this.makeupOilAddr = makeupOilAddr;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<String> getOilImgs() {
        return oilImgs;
    }

    public void setOilImgs(List<String> oilImgs) {
        this.oilImgs = oilImgs;
    }
}
