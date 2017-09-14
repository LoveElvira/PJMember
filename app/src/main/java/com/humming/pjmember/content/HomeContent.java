package com.humming.pjmember.content;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.activity.affair.AffairActivity;
import com.humming.pjmember.activity.meeting.MeetingActivity;
import com.humming.pjmember.activity.notify.NotifyActivity;
import com.humming.pjmember.activity.scan.DeviceManageActivity;
import com.humming.pjmember.activity.statistics.StatisticsActivity;
import com.humming.pjmember.activity.takephoto.DefectActivity;
import com.humming.pjmember.activity.work.WorkActivity;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.google.zxing.activity.CaptureActivity;
import com.humming.pjmember.utils.SharePrefUtil;

/**
 * Created by Elvira on 2017/8/31.
 * 首页
 */

public class HomeContent extends BaseLinearLayout {

    //天气情况图标
    private ImageView weatherImage;
    //天气  温度
    private TextView weatherTemp;
    //时间 + 气候
    private TextView weatherTime;

    //动态创建条目 的 父类
    private LinearLayout itemLayoutParent;

    //用户名 和 密码
    private String userName;
    private String password;

    //职位  1 ：一线  2 ： 业务  3 ： 管理
    private String positon = "1";

    public HomeContent(Context context) {
        this(context, null);
    }

    public HomeContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_home, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        userName = SharePrefUtil.getString(Constant.FILE_NAME, Constant.USERNAME, "", Application.getInstance().getCurrentActivity());
        password = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PASSWORD, "", Application.getInstance().getCurrentActivity());
        positon = SharePrefUtil.getString(Constant.FILE_NAME, Constant.POSITION, "1", Application.getInstance().getCurrentActivity());


        titleLayout = findViewById(R.id.base_toolbar__title_layout);
        title = findViewById(R.id.base_toolbar__title);
        titleImage = findViewById(R.id.base_toolbar__title_image);
        titleImage.setVisibility(VISIBLE);
        title.setText("普陀区");
        titleImage.setImageResource(R.mipmap.address);

        weatherImage = findViewById(R.id.fragment_home__wether_image);
        weatherTemp = findViewById(R.id.fragment_home__wether_temp);
        weatherTime = findViewById(R.id.fragment_home__wether_time);

        itemLayoutParent = findViewById(R.id.fragment_home__item_parent);

        itemLayoutParent.removeAllViews();

        //领导用户
        if (userName.equals("a") && password.equals("a") || positon.equals("3")) {
            itemLayoutParent.addView(initItem(false, 0));
            itemLayoutParent.addView(initItem(false, 2));
            itemLayoutParent.addView(initItem(true, 4));
        } else {
            itemLayoutParent.addView(initItem(false, 0));
            itemLayoutParent.addView(initItem(true, 2));
        }

    }

    //设置条目基础信息
    private View initItem(boolean isLast, int position) {
        View item = inflate(getContext(), R.layout.item_home, null);
        LinearLayout leftLayout = item.findViewById(R.id.item_home__left_layout);
        ImageView leftImage = item.findViewById(R.id.item_home__left_image);
        final TextView leftText = item.findViewById(R.id.item_home__left_text);
        ImageView leftTip = item.findViewById(R.id.item_home__left_tip);

        LinearLayout rightLayout = item.findViewById(R.id.item_home__right_layout);
        ImageView rightImage = item.findViewById(R.id.item_home__right_image);
        final TextView rightText = item.findViewById(R.id.item_home__right_text);
        ImageView rightTip = item.findViewById(R.id.item_home__right_tip);

        leftTip.setVisibility(GONE);
        rightTip.setVisibility(GONE);
        View bottomLine = item.findViewById(R.id.item_home__bottom_line);
        bottomLine.setVisibility(VISIBLE);
        if (isLast) {
            bottomLine.setVisibility(GONE);
        }
        //领导用户
        if (userName.equals("a") && password.equals("a")) {
            switch (position) {
                case 0:
                    leftImage.setImageResource(R.mipmap.affair);
                    leftText.setText("事务管理");
                    rightImage.setImageResource(R.mipmap.equipment);
                    rightText.setText("设备管理");
                    break;
                case 2:
                    leftImage.setImageResource(R.mipmap.meeting);
                    leftText.setText("会议安排");
                    rightImage.setImageResource(R.mipmap.statistics);
                    rightText.setText("统计中心");
                    break;
                case 4:
                    leftImage.setImageResource(R.mipmap.notify);
                    leftText.setText("通知");
                    rightImage.setImageResource(R.mipmap.video);
                    rightText.setText("视频管理");
                    leftTip.setVisibility(VISIBLE);
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    leftImage.setImageResource(R.mipmap.work);
                    leftText.setText("作业管理");
                    rightImage.setImageResource(R.mipmap.notify);
                    rightText.setText("通知");
                    rightTip.setVisibility(VISIBLE);
                    break;
                case 2:
                    leftImage.setImageResource(R.mipmap.take_photo);
                    leftText.setText("拍一拍");
                    rightImage.setImageResource(R.mipmap.scan);
                    rightText.setText("扫一扫");
                    break;
            }
        }

        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startContent(leftText.getText().toString().trim());
            }
        });
        rightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startContent(rightText.getText().toString().trim());
            }
        });

        return item;
    }

    private void startContent(String name) {
        if ("作业管理".equals(name)) {
            startActivity(WorkActivity.class);
        } else if ("拍一拍".equals(name)) {
            startActivity(DefectActivity.class);
        } else if ("通知".equals(name)) {
            startActivity(NotifyActivity.class);
        } else if ("扫一扫".equals(name)) {
            startActivity(CaptureActivity.class);
        } else if ("事务管理".equals(name)) {
            startActivity(AffairActivity.class);
        } else if ("设备管理".equals(name)) {
//            startActivity(CaptureActivity.class);
        } else if ("会议安排".equals(name)) {
            startActivity(MeetingActivity.class);
        } else if ("统计中心".equals(name)) {
            startActivity(StatisticsActivity.class);
        } else if ("视频管理".equals(name)) {
//            startActivity(CaptureActivity.class);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__title_layout:
                break;
        }
    }
}
