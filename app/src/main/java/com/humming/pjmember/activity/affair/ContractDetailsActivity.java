package com.humming.pjmember.activity.affair;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/7.
 * 合同详情
 */

public class ContractDetailsActivity extends BaseActivity {

    //展开合同详情
    private LinearLayout openDetailsLayout;
    //合同详情条目
    private LinearLayout detailsParentLayout;
    //展开合同详情 文字
    private TextView openDetailsText;
    //展开合同详情 图片
    private ImageView openDetailsImage;

    //展开相关审批意见
    private LinearLayout openMiddleLayout;
    //相关审批意见条目
    private LinearLayout middleParentLayout;
    //展开相关审批意见 文字
    private TextView openMiddleText;
    //展开相关审批意见 图片
    private ImageView openMiddleImage;

    //我的意见
    private EditText mySuggestion;

    //同意 按钮
    private TextView agreeBtn;
    //驳回 按钮
    private TextView rejectBtn;

    private LinearLayout visibleLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("合同详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = (LinearLayout) findViewById(R.id.activity_contract_details__top_open_layout);
        detailsParentLayout = (LinearLayout) findViewById(R.id.activity_contract_details__top_parent);
        openDetailsText = (TextView) findViewById(R.id.activity_contract_details__top_open_text);
        openDetailsImage = (ImageView) findViewById(R.id.activity_contract_details__top_open_image);

        openMiddleLayout = (LinearLayout) findViewById(R.id.activity_contract_details__middle_open_layout);
        middleParentLayout = (LinearLayout) findViewById(R.id.activity_contract_details__middle_parent);
        openMiddleText = (TextView) findViewById(R.id.activity_contract_details__middle_open_text);
        openMiddleImage = (ImageView) findViewById(R.id.activity_contract_details__middle_open_image);

        mySuggestion = (EditText) findViewById(R.id.activity_contract_details__suggestion);

        agreeBtn = (TextView) findViewById(R.id.activity_contract_details__agree);
        rejectBtn = (TextView) findViewById(R.id.activity_contract_details__reject);

        visibleLayout = (LinearLayout) findViewById(R.id.item_contract__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        middleParentLayout.setVisibility(View.GONE);
        middleParentLayout.addView(getItemMiddle(false));
        middleParentLayout.addView(getItemMiddle(true));

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);
    }

    //合同详情条目
    private View getItemDetails() {
        View view = View.inflate(this, R.layout.item_contract_top, null);

        return view;

    }

    //相关审批意见 条目
    private View getItemMiddle(boolean isLast) {
        View view = View.inflate(this, R.layout.item_contract_middle, null);
        View line = view.findViewById(R.id.item_contract__middle_line);
        line.setVisibility(View.VISIBLE);
        if (isLast) {
            line.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                ContractDetailsActivity.this.finish();
                break;
            case R.id.activity_contract_details__top_open_layout://合同详情  展开 及  关闭
                if (visibleLayout.getVisibility() == View.GONE) {
                    openDetailsText.setText("收起");
                    openDetailsImage.setImageResource(R.mipmap.top_arrow);
                    visibleLayout.setVisibility(View.VISIBLE);
                } else {
                    openDetailsText.setText("展开");
                    openDetailsImage.setImageResource(R.mipmap.bottom_arrow);
                    visibleLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_contract_details__middle_open_layout://相关审批意见  展开 及  关闭
                if (middleParentLayout.getVisibility() == View.GONE) {
                    openMiddleText.setText("收起");
                    openMiddleImage.setImageResource(R.mipmap.top_arrow);
                    middleParentLayout.setVisibility(View.VISIBLE);
                } else {
                    openMiddleText.setText("展开");
                    openMiddleImage.setImageResource(R.mipmap.bottom_arrow);
                    middleParentLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_contract_details__agree://同意
                ContractDetailsActivity.this.finish();
                break;
            case R.id.activity_contract_details__reject://驳回
                ContractDetailsActivity.this.finish();
                break;
        }
    }
}
