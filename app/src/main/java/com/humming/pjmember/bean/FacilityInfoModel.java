package com.humming.pjmember.bean;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/9/29.
 */

public class FacilityInfoModel implements Serializable {
    private String facilityId;
    private String facilityName;
    private boolean selected;

    public FacilityInfoModel() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
}
