package com.humming.pjmember.activity.meeting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/7.
 * 会议查询
 */

public class SearchMeetingActivity extends BaseActivity {

    //所属部门
    private TextView department;
    //年份
    private TextView yearTv;
    //月份
    private TextView monthTv;
    //星期
    private TextView weekTv;
    //查询
    private TextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_search);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("会议查询");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        department = (TextView) findViewById(R.id.activity_metting_search__department);
        yearTv = (TextView) findViewById(R.id.activity_metting_search__year);
        monthTv = (TextView) findViewById(R.id.activity_metting_search__month);
        weekTv = (TextView) findViewById(R.id.activity_metting_search__week);
        search = (TextView) findViewById(R.id.activity_metting_search__search);

        leftArrow.setOnClickListener(this);
        department.setOnClickListener(this);
        yearTv.setOnClickListener(this);
        monthTv.setOnClickListener(this);
        weekTv.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                SearchMeetingActivity.this.finish();
                break;
            case R.id.activity_metting_search__department://所属部门
                break;
            case R.id.activity_metting_search__year://年份
                break;
            case R.id.activity_metting_search__month://月份
                break;
            case R.id.activity_metting_search__week://星期
                break;
            case R.id.activity_metting_search__search://搜索
                SearchMeetingActivity.this.finish();
                break;
        }
    }
}
