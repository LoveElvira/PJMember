package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.EquipmentAdapter;
import com.humming.pjmember.adapter.PersonAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentInfoBean;
import com.pjqs.dto.equipment.EquipmentRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/22.
 * 设备列表
 */

public class EquipmentActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private EquipmentAdapter adapter;
    private List<EquipmentInfoBean> equipmentList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("设备列表");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        adapter = new EquipmentAdapter(list);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
        getEquipmentList();
    }

    //获取设备列表
    private void getEquipmentList() {

        progressHUD = ProgressHUD.show(EquipmentActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        OkHttpClientManager.postAsyn(Config.GET_WORK_EQUIPMENT, new OkHttpClientManager.ResultCallback<EquipmentRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    if ("1".equals(response.getCode())) {
                        equipmentList = response.getEquipments();
                        if (equipmentList != null && equipmentList.size() > 0) {
                            adapter = new EquipmentAdapter(equipmentList);
                            listView.setAdapter(adapter);
                            adapter.setOnItemChildClickListener(EquipmentActivity.this);
                        }
                    } else {
                        showShortToast(response.getMessage());
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentRes.class, EquipmentActivity.class);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                EquipmentActivity.this.finish();
                break;
        }
    }
}
