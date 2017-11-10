package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.adapter.ImageLookAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.pjqs.dto.equipment.EquipmentRepairBean;

import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 维修记录详情
 */

public class RepairDetailsActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //报修时间标题
    private TextView timeTitle;
    //报修时间
    private TextView time;
    //设备名称标题
    private TextView nameTitle;
    //设备名称
    private TextView name;
    //设备编号标题
    private TextView numTitle;
    //设备编号
    private TextView num;
    //维修内容 包含标题和内容
    private TextView content;
    //维修金额标题
    private TextView priceTitle;
    //维修金额
    private TextView price;
    //发票标题
    private TextView imageTitle;
    //发票图片
//    private ImageView image;
    private ImageLookAdapter adapter;

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
        title.setText("维修记录详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        timeTitle = (TextView) findViewById(R.id.activity_log_details__time_title);
        time = (TextView) findViewById(R.id.activity_log_details__time);
        nameTitle = (TextView) findViewById(R.id.activity_log_details__facility_name_title);
        name = (TextView) findViewById(R.id.activity_log_details__facility_name);
        numTitle = (TextView) findViewById(R.id.activity_log_details__num_title);
        num = (TextView) findViewById(R.id.activity_log_details__num);
        content = (TextView) findViewById(R.id.activity_log_details__content);
        priceTitle = (TextView) findViewById(R.id.activity_log_details__price_title);
        price = (TextView) findViewById(R.id.activity_log_details__price);
        imageTitle = (TextView) findViewById(R.id.activity_log_details__listview_title);
//        image = (ImageView) findViewById(R.id.activity_log_details__listview);

        listView = (RecyclerView) findViewById(R.id.activity_log_details__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);
        listView.addItemDecoration(new SpacesItemDecoration(10));


        timeTitle.setText("维修时间：");
        nameTitle.setText("设备名称：");
        numTitle.setText("设备编号：");
        priceTitle.setText("维修金额：");
        imageTitle.setText("发票：");

        leftArrow.setOnClickListener(this);
//        image.setOnClickListener(this);
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
                    if (response.getRepairFee() != null && !"".equals(response.getRepairFee())) {
                        price.setText("¥ " + String.format("%.2f", Double.parseDouble(response.getRepairFee())));
                    } else {
                        price.setText("¥ 0.00");
                    }
                    time.setText(response.getRepairTime());
                    content.setText(initHtml("维修内容", response.getReason()));
                    if (response.getInvoiceUrl() != null && response.getInvoiceUrl().size()>0) {
                        path.clear();
                        path.addAll(response.getInvoiceUrl());
                        adapter = new ImageLookAdapter(path, getBaseContext());
                        listView.setAdapter(adapter);
                        adapter.setOnItemChildClickListener(RepairDetailsActivity.this);
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_image__image_bg:
            case R.id.item_image__image:
                Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageUrl", path);
                startActivity(intent);
                break;
        }
    }
}
