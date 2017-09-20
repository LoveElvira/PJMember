package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.activity.takephoto.AddDefectActivity;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentRepairBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 维修记录详情
 */

public class RepairDetailsActivity extends BaseActivity {

    //报修时间
    private TextView time;
    //设备名称
    private TextView name;
    //设备编号
    private TextView num;
    //维修内容
    private TextView content;
    //维修金额
    private TextView price;
    //发票图片
    private ImageView image;

    private ArrayList<String> path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("维修记录详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        time = (TextView) findViewById(R.id.activity_repair_details__time);
        name = (TextView) findViewById(R.id.activity_repair_details__facility_name);
        num = (TextView) findViewById(R.id.activity_repair_details__num);
        content = (TextView) findViewById(R.id.activity_repair_details__content);
        price = (TextView) findViewById(R.id.activity_repair_details__price);
        image = (ImageView) findViewById(R.id.activity_repair_details__image);

        leftArrow.setOnClickListener(this);
        image.setOnClickListener(this);
        getRepairDetails();
    }

    //获取设备维修信息详情
    private void getRepairDetails() {
        progressHUD = ProgressHUD.show(RepairDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setRepairId(id);
        OkHttpClientManager.postAsyn(Config.GET_REPAIR_DETAILS, new OkHttpClientManager.ResultCallback<EquipmentRepairBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentRepairBean response) {
                progressHUD.dismiss();
                if (response != null) {

                    name.setText(response.getEquipmentName());
                    num.setText(response.getEquipmentNo());
                    Log.i("ee", response.getRepairFee() + "");
                    price.setText("¥ " + response.getRepairFee().setScale(2));
                    time.setText(response.getRepairTime());
//                    String str = "<font color='#ADADAD'>" + "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。" + "</font>";
                    content.setText(initHtml("缺陷描述", response.getReason()));
                    if (response.getInvoiceUrl() != null && !"".equals(response.getInvoiceUrl())) {
                        Glide.with(getBaseContext())
                                .load(response.getInvoiceUrl())
                                .into(image);
                        path.add(response.getInvoiceUrl());
                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentRepairBean.class, RepairDetailsActivity.class);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                RepairDetailsActivity.this.finish();
                break;
            case R.id.activity_repair_details__image://查看发票大图
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
