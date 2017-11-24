package com.humming.pjmember.activity.meeting;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.TimeAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.content.meeting.MeetingContent;
import com.humming.pjmember.utils.TimeUtils;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2017/9/7.
 * 会议安排
 */

public class MeetingActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //头部时间
    private TextView time;
    private TimeAdapter timeAdapter;
    private List<Map<String, String>> timeModelList;

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
//        rightImage.setImageResource(R.mipmap.meeting_search);
        rightText = findViewById(R.id.base_toolbar__right_text);

        time = (TextView) findViewById(R.id.item_time_top__time);
        time.setVisibility(View.VISIBLE);
        listView = (RecyclerView) findViewById(R.id.item_time_top__listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        listView.setLayoutManager(gridLayoutManager);

        timeModelList = TimeUtils.getDateAfter();
        timeModelList.get(0).put("isSelect", "true");

        timeAdapter = new TimeAdapter(timeModelList);
        listView.setAdapter(timeAdapter);
        timeAdapter.setOnItemChildClickListener(this);


        viewPager = (BaseViewPager) findViewById(R.id.activity_meeting__viewpager);

        meetingContent = new MeetingContent(this);

        list = new ArrayList<>();
        list.add(meetingContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        time.setText(timeModelList.get(0).get("year") + "-" + timeModelList.get(0).get("month"));
        rightText.setText(timeModelList.get(0).get("week"));
        meetingContent.getMeetingList(getDate(0));

    }

    private String getDate(int position) {
        return timeModelList.get(position).get("year") + "-" + timeModelList.get(position).get("month") + "-" + timeModelList.get(position).get("day");
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_time_top__parent:

                if ("false".equals(timeModelList.get(position).get("isSelect"))) {
                    for (int i = 0; i < timeModelList.size(); i++) {
                        timeModelList.get(i).put("isSelect", "false");
                    }
                    time.setText(timeModelList.get(position).get("year") + "-" + timeModelList.get(position).get("month"));
                    rightText.setText(timeModelList.get(position).get("week"));
                    timeModelList.get(position).put("isSelect", "true");
                    adapter.notifyDataSetChanged();
                    meetingContent.getMeetingList(getDate(position));
                }
                break;
        }
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
