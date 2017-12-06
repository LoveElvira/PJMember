package com.humming.pjmember.requestdate.add;

import com.humming.pjmember.service.IRequestMainData;

/**
 * Created by Elvira on 2017/12/6.
 */

public class AddRunningParameter implements IRequestMainData {
    /**
     * equipmentId	是	设备ID
     * startTime	是	开始时间
     * endTime	是	结束时间
     * runKm	是	公里数
     * remark	否	备注
     */

    private String equipmentId;
    private String startTime;
    private String endTime;
    private String runKm;
    private String remark;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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

    public String getRunKm() {
        return runKm;
    }

    public void setRunKm(String runKm) {
        this.runKm = runKm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
