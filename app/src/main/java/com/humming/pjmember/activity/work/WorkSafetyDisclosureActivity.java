package com.humming.pjmember.activity.work;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/12.
 * 安全交底内容
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class WorkSafetyDisclosureActivity extends BaseActivity implements View.OnScrollChangeListener {

    //提示
    private TextView tip;
    //交底Scrollview
    private ScrollView scrollView;
    //确认按钮
    private TextView confirm;

    private boolean isScrollBottom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_safety_disclosure);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("安全交底");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        tip = (TextView) findViewById(R.id.activity_work_safety__tip);
        scrollView = (ScrollView) findViewById(R.id.activity_work_safety__scrollview);
        confirm = (TextView) findViewById(R.id.activity_work_safety__confirm);
//        confirm.setTextColor(getResources().getColor(R.color.gray_888888));
        confirm.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_rectangle_gray_radius_3));

        if (getIntent().getBooleanExtra("isLook", false)) {
            tip.setText("请认真阅读安全交底内容");
            confirm.setVisibility(View.VISIBLE);
        } else {
            tip.setText("安全交底内容：");
            confirm.setVisibility(View.GONE);
        }

        leftArrow.setOnClickListener(this);
        confirm.setOnClickListener(this);
        scrollView.setOnScrollChangeListener(this);
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        if (scrollView.getScrollY() + scrollView.getHeight() - scrollView.getPaddingTop() - scrollView.getPaddingBottom() == scrollView.getChildAt(0).getHeight()) {
            isScrollBottom = true;
            // 小心踩坑2: 这里不能是 >=
            // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
//            confirm.setTextColor(getResources().getColor(R.color.white));
            confirm.setText("确认交底");
            confirm.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_rectangle_green_radius_3));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                WorkSafetyDisclosureActivity.this.finish();
                break;
            case R.id.activity_work_safety__confirm:
                if (isScrollBottom) {
                    startActivity(WorkPlanActivity.class);
                    WorkSafetyDisclosureActivity.this.finish();
                } else {
                    showShortToast("请认真阅读安全交底内容");
                }
                break;
        }
    }

}
