package com.humming.pjmember;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.content.HomeContent;
import com.humming.pjmember.content.SettingContent;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    //管理切换的界面
    private BaseViewPager viewPager;
    private ContentAdapter adapter;
    private List<View> list;//确定有几个页面
    private final String[] titles = {"a", "b"};

    //首页
    private HomeContent homeContent;
    //设置
    private SettingContent settingContent;

    //底部按钮 点击事件的布局
    private LinearLayout homeLayout;
    private LinearLayout settingLayout;

    //底部按钮 图片
    private ImageView homeImage;
    private ImageView settingImage;

    //底部按钮 文字
    private TextView homeText;
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

        viewPager = (BaseViewPager) findViewById(R.id.activity_main__viewpager);

        homeLayout = (LinearLayout) findViewById(R.id.bottom_button__home_layout);
        settingLayout = (LinearLayout) findViewById(R.id.bottom_button__setting_layout);

        homeImage = (ImageView) findViewById(R.id.bottom_button__home_image);
        settingImage = (ImageView) findViewById(R.id.bottom_button__setting_image);

        homeText = (TextView) findViewById(R.id.bottom_button__home_text);
        settingText = (TextView) findViewById(R.id.bottom_button__setting_text);

        homeContent = new HomeContent(this);
        settingContent = new SettingContent(this);

        list = new ArrayList<>();
        list.add(homeContent);
        list.add(settingContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initBottomView();

                if (position == 0) {
                    homeImage.setImageResource(R.mipmap.home_select);
                    homeText.setTextColor(getResources().getColor(R.color.blue));
                } else if (position == list.size() - 1) {
                    settingImage.setImageResource(R.mipmap.setting_select);
                    settingText.setTextColor(getResources().getColor(R.color.blue));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        homeLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        viewPager.setCurrentItem(0);

    }

    private void initBottomView() {
        homeImage.setImageResource(R.mipmap.home_default);
        homeText.setTextColor(getResources().getColor(R.color.white));
        settingImage.setImageResource(R.mipmap.setting_default);
        settingText.setTextColor(getResources().getColor(R.color.white));
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
