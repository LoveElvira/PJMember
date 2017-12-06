package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.QueryEquipmentRes;

import okhttp3.Request;

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
    //所属设施
    private TextView facility;
    //负责人
    private TextView userName;
    //负责人电话
    private TextView phone;
    private LinearLayout phoneLayout;

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
    //保险记录
    private LinearLayout insuranceLogLayout;
    //出车记录
    private LinearLayout runningLogLayout;
    //添加出车记录
    private ImageView addRunning;

    private QueryEquipmentRes equipmentRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        onLinePosition = SharePrefUtil.getString(Constant.FILE_NAME, Constant.POSITION, "0", Application.getInstance().getCurrentActivity());

        id = getIntent().getStringExtra("id");
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

        facility = (TextView) findViewById(R.id.activity_device_manage__facility);
        userName = (TextView) findViewById(R.id.activity_device_manage__user_name);
        phoneLayout = (LinearLayout) findViewById(R.id.activity_device_manage__phone_layout);
        phone = (TextView) findViewById(R.id.activity_device_manage__phone);


        repairLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__repair_log_layout);
        addRepair = (ImageView) findViewById(R.id.activity_device_manage__add_repair);
        maintainLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__maintain_log_layout);
        addMaintain = (ImageView) findViewById(R.id.activity_device_manage__add_maintain);
        useOilLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__use_oil_log_layout);
        addUseOil = (ImageView) findViewById(R.id.activity_device_manage__add_use_oil);
        accidentLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__accident_log_layout);
        addAccident = (ImageView) findViewById(R.id.activity_device_manage__add_accident);
        insuranceLogLayout = (LinearLayout) findViewById(R.id.activity_device_manage__insurance_log_layout);
        runningLogLayout = findViewById(R.id.activity_device_manage__running_log_layout);
        addRunning = findViewById(R.id.activity_device_manage__add_running);

        addRepair.setVisibility(View.VISIBLE);
        addMaintain.setVisibility(View.VISIBLE);
        addUseOil.setVisibility(View.VISIBLE);
        addAccident.setVisibility(View.VISIBLE);
        addRunning.setVisibility(View.VISIBLE);

        if ("2".equals(onLinePosition)) {
            addRepair.setVisibility(View.GONE);
            addMaintain.setVisibility(View.GONE);
            addUseOil.setVisibility(View.GONE);
            addAccident.setVisibility(View.GONE);
            addRunning.setVisibility(View.GONE);
        }

        leftArrow.setOnClickListener(this);
        repairLogLayout.setOnClickListener(this);
        addRepair.setOnClickListener(this);
        maintainLogLayout.setOnClickListener(this);
        addMaintain.setOnClickListener(this);
        useOilLogLayout.setOnClickListener(this);
        addUseOil.setOnClickListener(this);
        accidentLogLayout.setOnClickListener(this);
        addAccident.setOnClickListener(this);
        insuranceLogLayout.setOnClickListener(this);
        runningLogLayout.setOnClickListener(this);
        addRunning.setOnClickListener(this);

        getDeviceDate();
    }

    //获取设备信息
    private void getDeviceDate() {
        progressHUD = ProgressHUD.show(DeviceManageActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);

        OkHttpClientManager.postAsyn(Config.GET_EQUIPMENT_DETAILS, new OkHttpClientManager.ResultCallback<QueryEquipmentRes>() {
            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo().toString());
                Log.e("onError", info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(QueryEquipmentRes response) {
                progressHUD.dismiss();
                equipmentRes = response;
                if (response != null) {
                    name.setText(response.getEquipmentName());
                    num.setText(response.getEquipmentNo());
                    bigType.setText(response.getDeviceBroadType());
                    smallType.setText(response.getDeviceSubType());
                    model.setText(response.getBrand());
                    userName.setText(response.getFormanUserName());
                    facility.setText(response.getUseFacilityName());
                    if (response.getLicenseTag() != null && !"".equals(response.getLicenseTag())) {
                        plateNum.setText(response.getLicenseTag());
                    } else {
                        plateNum.setText("暂无数据");
                    }

                    if (response.getFormanPhone() != null && !"".equals(response.getFormanPhone())) {
                        phoneLayout.setVisibility(View.VISIBLE);
                        phone.setText(response.getFormanPhone());
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, QueryEquipmentRes.class, DeviceManageActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                DeviceManageActivity.this.finish();
                break;
            case R.id.activity_device_manage__repair_log_layout://维修记录
                intent.setClass(DeviceManageActivity.this, RepairLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__maintain_log_layout://保养记录
                intent.setClass(DeviceManageActivity.this, MaintainLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__use_oil_log_layout://用油记录
                intent.setClass(DeviceManageActivity.this, UseOilLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__accident_log_layout://事故记录
                intent.setClass(DeviceManageActivity.this, AccidentLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__insurance_log_layout://保险记录
                intent.setClass(DeviceManageActivity.this, InsuranceLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__running_log_layout://出车记录
                intent.setClass(DeviceManageActivity.this, RunningLogActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__add_repair://添加维修记录
                intent.setClass(DeviceManageActivity.this, AddRepairActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__add_maintain://添加保养记录
                intent.setClass(DeviceManageActivity.this, AddMaintainActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__add_use_oil://添加用油记录
                intent.setClass(DeviceManageActivity.this, AddUseOilActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__add_accident://添加事故记录
                intent.setClass(DeviceManageActivity.this, AddAccidentActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
            case R.id.activity_device_manage__add_running://添加出车记录
                intent.setClass(DeviceManageActivity.this, AddRunningActivity.class)
                        .putExtra("id", equipmentRes.getEquipmentId() + "");
                startActivity(intent);
                break;
        }
    }
}
