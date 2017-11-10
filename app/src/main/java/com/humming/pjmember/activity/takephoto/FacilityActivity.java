package com.humming.pjmember.activity.takephoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.PersonActivity;
import com.humming.pjmember.adapter.FacilityAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.bean.AccidentTypeModel;
import com.humming.pjmember.bean.FacilityInfoModel;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.pjqs.dto.equipment.EquipmentTypeRes;
import com.pjqs.dto.facility.FacilityInfo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/25.
 * 选择设施
 */

public class FacilityActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {


    private FacilityAdapter adapter;
    private List<FacilityInfoModel> facilityList;

    private FacilityInfoModel facilityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("设施列表");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("确定");

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);

        facilityList = new ArrayList<>();
        adapter = new FacilityAdapter(facilityList);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        getFacility();
    }


    //获取设施列表
    private void getFacility() {
        RequestParameter parameter = new RequestParameter();
        OkHttpClientManager.postAsyn(Config.GET_FACILITY, new OkHttpClientManager.ResultCallback<List<FacilityInfo>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(List<FacilityInfo> response) {
                if (response != null) {
                    facilityList.clear();
                    for (int i = 0; i < response.size(); i++) {
                        FacilityInfoModel model = new FacilityInfoModel();
                        model.setFacilityName(response.get(i).getFacilityName());
                        model.setFacilityId(response.get(i).getFacilityId());
                        model.setSelected(false);
                        facilityList.add(model);
                    }
                    adapter.setNewData(facilityList);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
            }
        }, parameter, new TypeReference<List<FacilityInfo>>() {
        }, PersonActivity.class);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_accident_type__parent:
                if (!facilityList.get(position).isSelected()) {
                    facilityModel = facilityList.get(position);
                    for (int i = 0; i < facilityList.size(); i++) {
                        facilityList.get(i).setSelected(false);
                    }
                    facilityList.get(position).setSelected(true);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                FacilityActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("facilityModel", facilityModel));
                FacilityActivity.this.finish();
                break;
        }
    }
}
