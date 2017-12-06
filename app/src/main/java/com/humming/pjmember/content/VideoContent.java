package com.humming.pjmember.content;

import android.content.Context;
import android.util.AttributeSet;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseLinearLayout;

/**
 * Created by Elvira on 2017/12/5.
 */

public class VideoContent extends BaseLinearLayout {

    public VideoContent(Context context) {
        this(context, null);
    }

    public VideoContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_video, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = findViewById(R.id.base_toolbar__title);
        title.setText("视频");
    }
}
