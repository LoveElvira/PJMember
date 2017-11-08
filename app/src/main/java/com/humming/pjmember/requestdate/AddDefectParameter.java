package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/11/1.
 */

public class AddDefectParameter implements IRequestMainData {

    /**
     * "workName": 1,//作业名称
     * "facilityId": 1,//设施id
     * "workContent": 1,//作业内容
     * "location":,//位置
     * "remark": 1//备注
     */

    private String workName;
    private long facilityId;
    private String location;
    private String remark;
    private List<String> pictureUrls;

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }
}
