package com.humming.pjmember.content.affair;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.AffairAdapter;
import com.humming.pjmember.base.BaseLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/7.
 * 项目
 */

public class ProjectContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener {

    private AffairAdapter adapter;

    public ProjectContent(Context context) {
        this(context, null);
    }

    public ProjectContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.comment_listview, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        listView = findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }

        adapter = new AffairAdapter(list, 2);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_affair__parent:
//                startActivity(WorkPlanActivity.class);
                break;
        }
    }
}
