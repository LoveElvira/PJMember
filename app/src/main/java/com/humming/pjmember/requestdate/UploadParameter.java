package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

/**
 * Created by Elvira on 2017/9/26.
 */

public class UploadParameter implements IRequestMainData {
    private String type;

    public UploadParameter() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
