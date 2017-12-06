package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.adapter.ImageLookAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.pjqs.dto.equipment.EquipmentOilRecBean;
import com.pjqs.dto.equipment.EquipmentOilRecInfoRes;
import com.pjqs.dto.equipment.EquipmentRepairBean;

import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 用油详情记录
 */

public class UseOilDetailsActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //加油时间标题
    private TextView timeTitle;
    //加油时间
    private TextView time;
    //设备名称标题
    private TextView nameTitle;
    //设备名称
    private TextView name;
    //设备编号标题
    private TextView numTitle;
    //设备编号
    private TextView num;
    //创建人标题
    private LinearLayout userNameLayout;
    private TextView userNameTitle;
    //创建人
    private TextView userName;
    //地址标题
    private LinearLayout addressLayout;
    private TextView addressTitle;
    //地址
    private TextView address;
    //加油卡号标题
    private LinearLayout cardLayout;
    private TextView cardTitle;
    //加油卡号
    private TextView card;
    //加油量 包含标题和内容
    private TextView content;
    //维修金额标题
    private TextView priceTitle;
    //维修金额
    private TextView price;
    //发票标题
    private TextView imageTitle;
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
        title.setText("用油记录详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        timeTitle = (TextView) findViewById(R.id.activity_log_details__time_title);
        time = (TextView) findViewById(R.id.activity_log_details__time);
        nameTitle = (TextView) findViewById(R.id.activity_log_details__facility_name_title);
        name = (TextView) findViewById(R.id.activity_log_details__facility_name);
        numTitle = (TextView) findViewById(R.id.activity_log_details__num_title);
        num = (TextView) findViewById(R.id.activity_log_details__num);
        userNameLayout = (LinearLayout) findViewById(R.id.activity_log_details__company_layout);
        userNameTitle = (TextView) findViewById(R.id.activity_log_details__company_title);
        userName = (TextView) findViewById(R.id.activity_log_details__company);
        cardLayout = (LinearLayout) findViewById(R.id.activity_log_details__type_layout);
        cardTitle = (TextView) findViewById(R.id.activity_log_details__type_title);
        card = (TextView) findViewById(R.id.activity_log_details__type);
        addressLayout = (LinearLayout) findViewById(R.id.activity_log_details__address_layout);
        addressTitle = (TextView) findViewById(R.id.activity_log_details__address_title);
        address = (TextView) findViewById(R.id.activity_log_details__address);
        content = (TextView) findViewById(R.id.activity_log_details__content);
        priceTitle = (TextView) findViewById(R.id.activity_log_details__price_title);
        price = (TextView) findViewById(R.id.activity_log_details__price);
        imageTitle = (TextView) findViewById(R.id.activity_log_details__listview_title);

        listView = (RecyclerView) findViewById(R.id.activity_log_details__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);
        listView.addItemDecoration(new SpacesItemDecoration(10));

        timeTitle.setText("加油时间：");
        nameTitle.setText("设备名称：");
        numTitle.setText("设备编号：");
        userNameLayout.setVisibility(View.VISIBLE);
        userNameTitle.setText("创建人：");
        cardLayout.setVisibility(View.VISIBLE);
        cardTitle.setText("加油卡号：");
        addressLayout.setVisibility(View.VISIBLE);
        addressTitle.setText("加油地址：");
        priceTitle.setText("加油金额：");
        imageTitle.setText("发票：");

        leftArrow.setOnClickListener(this);
        getUseOilDetails();
    }

    //获取设备用油信息详情
    private void getUseOilDetails() {
        progressHUD = ProgressHUD.show(UseOilDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setOilId(id);
        OkHttpClientManager.postAsyn(Config.GET_OIL_DETAILS, new OkHttpClientManager.ResultCallback<EquipmentOilRecBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentOilRecBean response) {
                progressHUD.dismiss();
                if (response != null) {
                    name.setText(response.getEquipmentName());
                    num.setText(response.getEquipmentNo());
                    price.setText(StringUtils.saveTwoDecimal(response.getMoney()));
                    time.setText(response.getMakeupOilTime());
                    userName.setText(response.getCrtUserName());
                    card.setText(response.getMakeupOilCard());
                    address.setText(response.getMakeupOilAddr());
                    content.setText(initHtml("加油量(L)", response.getMakeupOilQuantity()));
                    if (response.getOilImgs() != null && response.getOilImgs().size() > 0) {
                        path.clear();
                        path.addAll(response.getOilImgs());
                        adapter = new ImageLookAdapter(path, getBaseContext());
                        listView.setAdapter(adapter);
                        adapter.setOnItemChildClickListener(UseOilDetailsActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentOilRecBean.class, UseOilDetailsActivity.class);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                UseOilDetailsActivity.this.finish();
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
