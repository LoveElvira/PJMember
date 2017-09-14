package com.humming.pjmember.activity.meeting;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.content.meeting.MeetingContent;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/7.
 * 会议安排
 */

public class MeetingActivity extends BaseActivity {

    //管理切换的界面
    private BaseViewPager viewPager;
    private ContentAdapter adapter;
    private List<View> list;//确定有几个页面
    private final String[] titles = {"a"};

    private MeetingContent meetingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("会议安排");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightImage = (ImageView) findViewById(R.id.base_toolbar__right_image);
        rightImage.setImageResource(R.mipmap.meeting_search);

        viewPager = (BaseViewPager) findViewById(R.id.activity_meeting__viewpager);

        meetingContent = new MeetingContent(this);

        list = new ArrayList<>();
        list.add(meetingContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        leftArrow.setOnClickListener(this);
        rightImage.setOnClickListener(this);
        viewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                MeetingActivity.this.finish();
                break;
            case R.id.base_toolbar__right_image:
                startActivity(SearchMeetingActivity.class);
                break;
        }
    }
}
