package com.humming.pjmember.activity.affair;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.humming.pjmember.R;
import com.humming.pjmember.activity.affair.more.DispatchActivity;
import com.humming.pjmember.activity.affair.more.EquipmentActivity;
import com.humming.pjmember.activity.affair.more.PriceActivity;
import com.humming.pjmember.activity.affair.more.ScientificResearchActivity;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2018/2/6.
 */

public class MoreAffairActivity extends BaseActivity {

    //收发文
    private LinearLayout dispatchLayout;
    //费用
    private LinearLayout priceLayout;
    //科研
    private LinearLayout scientificResearchLayout;
    //设备
    private LinearLayout equipmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affair_more);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = findViewById(R.id.base_toolbar__title);
        title.setText("更多事务管理");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        dispatchLayout = findViewById(R.id.activity_affair_more__dispatch);
        priceLayout = findViewById(R.id.activity_affair_more__price);
        scientificResearchLayout = findViewById(R.id.activity_affair_more__scientific_research);
        equipmentLayout = findViewById(R.id.activity_affair_more__equipment);

        leftArrow.setOnClickListener(this);
        dispatchLayout.setOnClickListener(this);
        priceLayout.setOnClickListener(this);
        scientificResearchLayout.setOnClickListener(this);
        equipmentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                MoreAffairActivity.this.finish();
                break;
            case R.id.activity_affair_more__dispatch:
                startActivity(DispatchActivity.class);
                break;
            case R.id.activity_affair_more__price:
                startActivity(PriceActivity.class);
                break;
            case R.id.activity_affair_more__scientific_research:
                startActivity(ScientificResearchActivity.class);
                break;
            case R.id.activity_affair_more__equipment:
                startActivity(EquipmentActivity.class);
                break;
        }
    }
}
