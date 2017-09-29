package com.humming.pjmember.bean;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/9/29.
 */

public class AccidentTypeModel implements Serializable {
    private Long typeId;
    private String typeContent;
    private boolean selected;

    public AccidentTypeModel() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeContent() {
        return this.typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }
}
