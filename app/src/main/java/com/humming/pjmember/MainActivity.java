package com.humming.pjmember;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.activity.work.PersonActivity;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.content.HomeContent;
import com.humming.pjmember.content.SettingContent;
import com.humming.pjmember.content.VideoContent;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;
import com.pjqs.dto.equipment.EquipmentTypeRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends BaseActivity {

    //管理切换的界面
    private BaseViewPager viewPager;
    private ContentAdapter adapter;
    private List<View> list;//确定有几个页面
    private final String[] titles = {"a", "b"};

    //首页
    private HomeContent homeContent;
    //视频
    private VideoContent videoContent;
    //设置
    private SettingContent settingContent;

    //底部按钮 点击事件的布局
    private LinearLayout homeLayout;
    private LinearLayout videoLayout;
    private LinearLayout settingLayout;

    //底部按钮 图片
    private ImageView homeImage;
    private ImageView videoImage;
    private ImageView settingImage;

    //底部按钮 文字
    private TextView homeText;
    private TextView videoText;
    private TextView settingText;

    private long mExitTime; //退出时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        onLinePosition = SharePrefUtil.getString(Constant.FILE_NAME, Constant.POSITION, "0", Application.getInstance().getCurrentActivity());

        viewPager = (BaseViewPager) findViewById(R.id.activity_main__viewpager);

        homeLayout = (LinearLayout) findViewById(R.id.bottom_button__home_layout);
        videoLayout = (LinearLayout) findViewById(R.id.bottom_button__video_layout);
        settingLayout = (LinearLayout) findViewById(R.id.bottom_button__setting_layout);

        homeImage = (ImageView) findViewById(R.id.bottom_button__home_image);
        videoImage = (ImageView) findViewById(R.id.bottom_button__video_image);
        settingImage = (ImageView) findViewById(R.id.bottom_button__setting_image);

        homeText = (TextView) findViewById(R.id.bottom_button__home_text);
        videoText = (TextView) findViewById(R.id.bottom_button__video_text);
        settingText = (TextView) findViewById(R.id.bottom_button__setting_text);

        homeContent = new HomeContent(this);
        videoContent = new VideoContent(this);
        settingContent = new SettingContent(this);
        videoLayout.setVisibility(View.GONE);
        list = new ArrayList<>();
        list.add(homeContent);
        if (onLinePosition.equals("2")) {
            videoLayout.setVisibility(View.VISIBLE);
            list.add(videoContent);
        }
        list.add(settingContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initBottomView();
                if (position == 0) {
                    homeImage.setImageResource(R.mipmap.home_select);
                    homeText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                    return;
                } else if (position == list.size() - 1) {
                    settingImage.setImageResource(R.mipmap.setting_select);
                    settingText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                    return;
                }
                if (onLinePosition.equals("2")) {
                    if (position == 1) {
                        videoImage.setImageResource(R.mipmap.home_select);
                        videoText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        homeLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        viewPager.setCurrentItem(0);

        getAccidentType();
    }

    private void initBottomView() {
        homeImage.setImageResource(R.mipmap.home_default);
        homeText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        videoImage.setImageResource(R.mipmap.home_default);
        videoText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        settingImage.setImageResource(R.mipmap.setting_default);
        settingText.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
    }

    //获取事故类型
    private void getAccidentType() {
        RequestParameter parameter = new RequestParameter();
        OkHttpClientManager.postAsyn(Config.GET_ACCIDENT_TYPE, new OkHttpClientManager.ResultCallback<EquipmentTypeRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(EquipmentTypeRes response) {
                if (response != null) {
                    SharePrefUtil.setObject(Constant.FILE_NAME, Constant.ACCIDENT_TYPE, response, MainActivity.this);
                    SharePrefUtil.getObject(Constant.FILE_NAME, Constant.ACCIDENT_TYPE, null, MainActivity.this);

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
            }
        }, parameter, EquipmentTypeRes.class, PersonActivity.class);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bottom_button__home_layout:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.bottom_button__video_layout:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.bottom_button__setting_layout:
                if (viewPager.getCurrentItem() != list.size() - 1) {
                    viewPager.setCurrentItem(list.size() - 1);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                showShortToast("再按一次退出应用程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
