package com.humming.pjmember.activity.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/4.
 * 计划作业
 */

public class WorkPlanActivity extends BaseActivity {

    //作业状态图片
    private ImageView statusImage;
    //作业状态 文字
    private TextView status;

    //可以隐藏的东西
//    private LinearLayout middleVisibilityLayout;
//    private TextView middleLookMore;

//    private TextView bottomVisibility;
//    private TextView bottomLookMore;

//    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("计划作业");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("交底内容");

//        middleVisibilityLayout = (LinearLayout) findViewById(R.id.item_plan_middle__visibility_layout);
//        middleLookMore = (TextView) findViewById(R.id.item_plan_middle__look_more);
//
//        bottomVisibility = (TextView) findViewById(R.id.item_plan_bottom__address_);
//        bottomLookMore = (TextView) findViewById(R.id.item_plan_bottom__look_more);

        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);
//        middleLookMore.setOnClickListener(this);
//        bottomLookMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                WorkPlanActivity.this.finish();
                break;
            case R.id.item_plan_middle__look_more:
//                if (isFirst) {
//                    showShortToast("请先查看安全交底内容");
//                } else {
//                    if (middleVisibilityLayout.getVisibility() == View.GONE) {
//                        middleVisibilityLayout.setVisibility(View.VISIBLE);
//                        middleLookMore.setText("隐藏更多");
//                    } else {
//                        middleVisibilityLayout.setVisibility(View.GONE);
//                        middleLookMore.setText("查看更多");
//                    }
//                }
                break;
            case R.id.item_plan_bottom__look_more:
//                isFirst = false;
//                if (bottomVisibility.getVisibility() == View.GONE) {
//                    bottomVisibility.setVisibility(View.VISIBLE);
//                    bottomLookMore.setText("隐藏更多");
//                } else {
//                    bottomVisibility.setVisibility(View.GONE);
//                    bottomLookMore.setText("查看更多");
//                }
                break;
            case R.id.base_toolbar__right_text://安全交底内容
                startActivity(new Intent(WorkPlanActivity.this, WorkSafetyDisclosureActivity.class)
                        .putExtra("isLook", false));
                break;
        }
    }
}
