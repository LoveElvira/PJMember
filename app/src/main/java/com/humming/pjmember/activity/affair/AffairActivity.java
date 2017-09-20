package com.humming.pjmember.activity.affair;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.content.affair.ContractContent;
import com.humming.pjmember.content.affair.DispatchContent;
import com.humming.pjmember.content.affair.ProjectContent;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/7.
 * 事务管理
 */

public class AffairActivity extends BaseActivity {

    //管理切换的界面
    private BaseViewPager viewPager;
    private ContentAdapter adapter;
    private List<View> list;//确定有几个页面
    private final String[] titles = {"a", "b", "c"};

    //头部按钮 点击事件的布局
    private RelativeLayout contractLayout;
    private RelativeLayout projectLayout;
    private RelativeLayout dispatchLayout;

    //合同
    private ContractContent contractContent;
    //项目
    private ProjectContent projectContent;
    //收发文
    private DispatchContent dispatchContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affair);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("事务管理");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        viewPager = (BaseViewPager) findViewById(R.id.activity_affair__viewpager);

        contractLayout = (RelativeLayout) findViewById(R.id.top_button__contract_layout);
        projectLayout = (RelativeLayout) findViewById(R.id.top_button__project_layout);
        dispatchLayout = (RelativeLayout) findViewById(R.id.top_button__dispatch_layout);

        contractContent = new ContractContent(this);
        projectContent = new ProjectContent(this);
        dispatchContent = new DispatchContent(this);

        list = new ArrayList<>();
        list.add(contractContent);
        list.add(projectContent);
        list.add(dispatchContent);
        adapter = new ContentAdapter(list, titles);
//        viewPager.setEnabled(true);
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
        contractLayout.setOnClickListener(this);
        projectLayout.setOnClickListener(this);
        dispatchLayout.setOnClickListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AffairActivity.this.finish();
                break;
            case R.id.top_button__contract_layout:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.top_button__project_layout:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.top_button__dispatch_layout:
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(2);
                }
                break;
        }
    }
}
