package com.humming.pjmember.content.work;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.WorkPlanActivity;
import com.humming.pjmember.activity.work.WorkSafetyDisclosureActivity;
import com.humming.pjmember.adapter.WorkAdapter;
import com.humming.pjmember.base.BaseLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/1.
 * 全部作业
 */

public class WholeContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener {

    private WorkAdapter adapter;
    //筛选
    private LinearLayout screenLayout;

    public WholeContent(Context context) {
        this(context, null);
    }

    public WholeContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_work_, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        screenLayout = findViewById(R.id.content_work__screen);
        screenLayout.setVisibility(VISIBLE);

        listView = findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
        }

        adapter = new WorkAdapter(list);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_work__parent:
                startActivity(new Intent(getContext(), WorkSafetyDisclosureActivity.class)
                        .putExtra("isLook", true));
                break;
        }
    }
}
