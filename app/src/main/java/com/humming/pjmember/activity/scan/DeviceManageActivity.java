package com.humming.pjmember.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;

/**
 * Created by Elvira on 2017/9/3.
 * 设备管理
 */

public class DeviceManageActivity extends BaseActivity {

    //设备名称
    private TextView name;
    //设备编号
    private TextView num;
    //设备大类
    private TextView bigType;
    //设备小类
    private TextView smallType;
    //品牌型号
    private TextView model;
    //车辆牌照
    private TextView plateNum;
    //维修记录
    private LinearLayout repairLogLayout;
    //添加维修记录
    private ImageView addRepair;
    //保养记录
    private LinearLayout maintainLogLayout;
    //添加保养记录
    private ImageView addMaintain;
    //用油记录
    private LinearLayout useOilLogLayout;
    //添加用油记录
    private ImageView addUseOil;
    //事故记录
    private LinearLayout accidentLogLayout;
    //添加事故记录
    private ImageView addAccident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("设备管理");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        name = (TextView) findViewById(R.id.activity_device_manage__facility_name);
        num = (TextView) findViewById(R.id.activity_device_manage__num);
        bigType = (TextView) findViewById(R.id.activity_device_manage__content_big);
        smallType = (TextView) findViewById(R.id.activity_device_manage__content_small);
        model = (TextView) findViewById(R.id.activity_device_manage__model);
        plateNum = (TextView) findViewById(R.id.activity_device_manage__plate_num);

        repairLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__repair_log_layout);
        addRepair = (ImageView) findViewById(R.id.activity_device_manage__add_repair);
        maintainLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__maintain_log_layout);
        addMaintain = (ImageView) findViewById(R.id.activity_device_manage__add_maintain);
        useOilLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__use_oil_log_layout);
        addUseOil = (ImageView) findViewById(R.id.activity_device_manage__add_use_oil);
        accidentLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__accident_log_layout);
        addAccident = (ImageView) findViewById(R.id.activity_device_manage__add_accident);


        leftArrow.setOnClickListener(this);
        repairLogLayout.setOnClickListener(this);
        addRepair.setOnClickListener(this);
        maintainLogLayout.setOnClickListener(this);
        addMaintain.setOnClickListener(this);
        useOilLogLayout.setOnClickListener(this);
        addUseOil.setOnClickListener(this);
        accidentLogLayout.setOnClickListener(this);
        addAccident.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                DeviceManageActivity.this.finish();
                break;
            case R.id.activity_device_manage__repair_log_layout://维修记录
                startActivity(RepairLogActivity.class);
                break;
            case R.id.activity_device_manage__maintain_log_layout://保养记录
                startActivity(MaintainLogActivity.class);
                break;
            case R.id.activity_device_manage__use_oil_log_layout://用油记录
                startActivity(UseOilLogActivity.class);
                break;
            case R.id.activity_device_manage__accident_log_layout://事故记录
                startActivity(AccidentLogActivity.class);
                break;
            case R.id.activity_device_manage__add_repair://添加维修记录
                startActivity(AddRepairActivity.class);
                break;
            case R.id.activity_device_manage__add_maintain://添加保养记录
                startActivity(AddMaintainActivity.class);
                break;
            case R.id.activity_device_manage__add_use_oil://添加用油记录
                startActivity(AddUseOilActivity.class);
                break;
            case R.id.activity_device_manage__add_accident://添加事故记录
                startActivity(AddAccidentActivity.class);
                break;
        }
    }
}
