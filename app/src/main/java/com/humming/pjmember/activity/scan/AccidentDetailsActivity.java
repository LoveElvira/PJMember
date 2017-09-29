package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentAcctidentBean;
import com.pjqs.dto.equipment.EquipmentMaintainBean;

import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/21.
 * 事故记录详情
 */

public class AccidentDetailsActivity extends BaseActivity {

    //保养时间标题
    private TextView timeTitle;
    //保养时间
    private TextView time;
    //设备名称标题
    private TextView nameTitle;
    //设备名称
    private TextView name;
    //设备编号标题
    private TextView numTitle;
    //设备编号
    private TextView num;
    //保养单位标题
    private TextView companyTitle;
    //保养单位
    private LinearLayout companyLayout;
    private TextView company;
    //保养内容 包含标题和内容
    private TextView content;
    //保养金额标题
    private TextView priceTitle;
    //保养金额
    private TextView price;
    //发票标题
    private TextView imageTitle;
    //发票图片
    private LinearLayout imageLayout;
    private ImageView image;

    private ArrayList<String> path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("事故记录详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        timeTitle = (TextView) findViewById(R.id.activity_log_details__time_title);
        time = (TextView) findViewById(R.id.activity_log_details__time);
        nameTitle = (TextView) findViewById(R.id.activity_log_details__facility_name_title);
        name = (TextView) findViewById(R.id.activity_log_details__facility_name);
        numTitle = (TextView) findViewById(R.id.activity_log_details__num_title);
        num = (TextView) findViewById(R.id.activity_log_details__num);
        companyLayout = (LinearLayout) findViewById(R.id.activity_log_details__company_layout);
        companyTitle = (TextView) findViewById(R.id.activity_log_details__company_title);
        company = (TextView) findViewById(R.id.activity_log_details__company);
        content = (TextView) findViewById(R.id.activity_log_details__content);
        priceTitle = (TextView) findViewById(R.id.activity_log_details__price_title);
        price = (TextView) findViewById(R.id.activity_log_details__price);
        imageLayout = (LinearLayout) findViewById(R.id.activity_log_details__listview_layout);
        imageTitle = (TextView) findViewById(R.id.activity_log_details__listview_title);
        image = (ImageView) findViewById(R.id.activity_log_details__listview);

        imageLayout.setVisibility(View.GONE);
        timeTitle.setText("事故发生时间：");
        nameTitle.setText("设备名称：");
        numTitle.setText("设备编号：");
//        companyTitle.setText("保养单位");
        priceTitle.setText("损失金额：");
//        imageTitle.setText("发票");

        leftArrow.setOnClickListener(this);
        image.setOnClickListener(this);
        getMaintainDetails();
    }

    //获取设备保养信息详情
    private void getMaintainDetails() {
        progressHUD = ProgressHUD.show(AccidentDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setAccidentId(id);
        OkHttpClientManager.postAsyn(Config.GET_ACCIDENT_DETAILS, new OkHttpClientManager.ResultCallback<EquipmentAcctidentBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentAcctidentBean response) {
                progressHUD.dismiss();
                if (response != null) {
                    name.setText(response.getEquipmentName());
                    num.setText(response.getEquipmentNo());
//                    company.setText(response.getMaintainDepartment());
                    if (response.getLossFee() != null && !"".equals(response.getLossFee())) {
                        price.setText("¥ " + String.format("%.2f", Double.parseDouble(response.getLossFee())));
                    } else {
                        price.setText("¥ 0.00");
                    }
                    time.setText(response.getAccidentTiem());
                    content.setText(initHtml("事故原因", response.getRemark()));
//                    if (response.getMaintainImg() != null && !"".equals(response.getMaintainImg())) {
//                        Glide.with(getBaseContext())
//                                .load(response.getMaintainImg())
//                                .into(image);
//                        path.add(response.getMaintainImg());
//                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentAcctidentBean.class, RepairDetailsActivity.class);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AccidentDetailsActivity.this.finish();
                break;
            case R.id.activity_log_details__listview://查看发票大图
                if (path.size() > 0) {
                    Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("imageUrl", path);
                    intent.putExtra("isShowDelete", "false");
                    startActivity(intent);
                }
                break;
        }
    }
}


