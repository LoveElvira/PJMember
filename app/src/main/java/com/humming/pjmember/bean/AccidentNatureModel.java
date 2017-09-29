package com.humming.pjmember.bean;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/9/29.
 */

public class AccidentNatureModel implements Serializable {
    private Long natureId;
    private String natureContent;
    private boolean selected;

    public AccidentNatureModel() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getNatureId() {
        return this.natureId;
    }

    public void setNatureId(Long natureId) {
        this.natureId = natureId;
    }

    public String getNatureContent() {
        return this.natureContent;
    }

    public void setNatureContent(String natureContent) {
        this.natureContent = natureContent;
    }
}
