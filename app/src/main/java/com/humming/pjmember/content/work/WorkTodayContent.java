package com.humming.pjmember.content.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.WorkPlanActivity;
import com.humming.pjmember.activity.work.WorkSafetyDisclosureActivity;
import com.humming.pjmember.adapter.WorkAdapter;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.viewutils.CircleTextProgressbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/1.
 * 今日工作
 */

public class WorkTodayContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener {

    private WorkAdapter adapter;
    private CircleTextProgressbar circleText;
    //今日工作数量
    private TextView workNum;
    //已完成数量
    private TextView completeNum;
    //未已完成数量
    private TextView unCompleteNum;

    public WorkTodayContent(Context context) {
        this(context, null);
    }

    public WorkTodayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_work_today, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

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
        adapter.addHeaderView(getheadView());
        listView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(this);

    }

    private View getheadView() {
        View headView = inflate(getContext(), R.layout.item_work_top, null);
        workNum = headView.findViewById(R.id.item_work_top__work_num);
        completeNum = headView.findViewById(R.id.item_work_top__complete_num);
        unCompleteNum = headView.findViewById(R.id.item_work_top__uncomplete_num);
        circleText = headView.findViewById(R.id.item_work_top__progress);
        circleText.setOutLineColor(getResources().getColor(R.color.white));
        circleText.setInCircleColor(Color.TRANSPARENT);
        circleText.setProgressColor(getResources().getColor(R.color.blue_365DFE));
        circleText.setProgressLineWidth(10);
        circleText.setOutLineWidth(10);
        circleText.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
//        circleText.setCountdownProgressListener(2, progressListener);
//        circleText.setTimeMillis(8 * 1000);
        circleText.start(25);
//        circleText.setProgress(25);
        return headView;
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
