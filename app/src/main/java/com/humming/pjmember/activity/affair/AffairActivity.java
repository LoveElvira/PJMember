package com.humming.pjmember.activity.affair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.content.affair.ContractExpenditureContent;
import com.humming.pjmember.content.affair.ContractIncomeContent;
import com.humming.pjmember.content.affair.DispatchContent;
import com.humming.pjmember.content.affair.ProjectContent;
import com.humming.pjmember.content.affair.ProjectFileContent;
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
    private final String[] titles = {"a", "b", "c", "d", "e"};

    //头部按钮 点击事件的布局
    private RelativeLayout contractLayout;
    private RelativeLayout contractExpenditureLayout;
    private RelativeLayout projectLayout;
    private RelativeLayout projectFileLayout;
    private RelativeLayout moreLayout;

    //选中的线
    private View contractLine;
    private View contractExpenditureLine;
    private View projectLine;
    private View projectFileLine;

    //收入合同
    private ContractIncomeContent contractIncomeContent;
    //收入合同
    private ContractExpenditureContent contractExpenditureContent;
    //支出项目
    private ProjectContent projectContent;
    //项目文件
    private ProjectFileContent projectFileContent;
    //收发文
//    private DispatchContent dispatchContent;


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
        contractExpenditureLayout = (RelativeLayout) findViewById(R.id.top_button__contract_expenditure_layout);
        projectLayout = (RelativeLayout) findViewById(R.id.top_button__project_layout);
        projectFileLayout = (RelativeLayout) findViewById(R.id.top_button__project_file_layout);
        moreLayout = (RelativeLayout) findViewById(R.id.top_button__more_layout);

        contractLine = findViewById(R.id.bottom_button__contract_line);
        contractExpenditureLine = findViewById(R.id.bottom_button__contract_expenditure_line);
        projectLine = findViewById(R.id.bottom_button__project_line);
        projectFileLine = findViewById(R.id.bottom_button__project_file_line);

        contractIncomeContent = new ContractIncomeContent(this);
        contractExpenditureContent = new ContractExpenditureContent(this);
        projectContent = new ProjectContent(this);
        projectFileContent = new ProjectFileContent(this);
//        dispatchContent = new DispatchContent(this);

        list = new ArrayList<>();
        list.add(contractIncomeContent);
        list.add(contractExpenditureContent);
        list.add(projectContent);
        list.add(projectFileContent);
//        list.add(dispatchContent);
        adapter = new ContentAdapter(list, titles);
//        viewPager.setEnabled(true);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 4) {
                    initLine(position);
                }
                switch (position) {
                    case 0:
                        contractIncomeContent.isInitFirst();
                        break;
                    case 1:
                        contractExpenditureContent.isInitFirst();
                        break;
                    case 2:
                        projectContent.isInitFirst();
                        break;
                    case 3:
                        projectFileContent.isInitFirst();
                        break;
//                    case 4:
//                        wholeContent.isInitFirst();
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        leftArrow.setOnClickListener(this);
        contractLayout.setOnClickListener(this);
        contractExpenditureLayout.setOnClickListener(this);
        projectLayout.setOnClickListener(this);
        projectFileLayout.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        viewPager.setCurrentItem(0);
        contractIncomeContent.isInitFirst();
    }

    private void initLine(int position) {
        contractLine.setVisibility(View.GONE);
        contractExpenditureLine.setVisibility(View.GONE);
        projectLine.setVisibility(View.GONE);
        projectFileLine.setVisibility(View.GONE);
        switch (position) {
            case 0:
                contractLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                contractExpenditureLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                projectLine.setVisibility(View.VISIBLE);
                break;
            case 3:
                projectFileLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Constant.CODE_RESULT)
            return;
        int position = data.getIntExtra("position", -1);
        switch (requestCode) {
            case Constant.CODE_REQUEST_ONE:
                contractIncomeContent.updateData();
                break;
            case Constant.CODE_REQUEST_TWO:
                contractExpenditureContent.updateData();
                break;
            case Constant.CODE_REQUEST_THREE:
                projectContent.updateData();
                break;
        }
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
            case R.id.top_button__contract_expenditure_layout:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.top_button__project_layout:
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(2);
                }
                break;
            case R.id.top_button__project_file_layout:
                if (viewPager.getCurrentItem() != 3) {
                    viewPager.setCurrentItem(3);
                }
                break;
            case R.id.top_button__more_layout:
//                if (viewPager.getCurrentItem() != 4) {
//                    viewPager.setCurrentItem(4);
//                }
                startActivity(MoreAffairActivity.class);
                break;
        }
    }
}
