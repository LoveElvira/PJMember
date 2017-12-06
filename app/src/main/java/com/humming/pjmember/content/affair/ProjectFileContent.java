package com.humming.pjmember.content.affair;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseLinearLayout;

/**
 * Created by Elvira on 2017/12/5.
 */

public class ProjectFileContent extends BaseLinearLayout {

    public ProjectFileContent(Context context) {
        this(context, null);
    }

    public ProjectFileContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.common_listview, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

    }

}
