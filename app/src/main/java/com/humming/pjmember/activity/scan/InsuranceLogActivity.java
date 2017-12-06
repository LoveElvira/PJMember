package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.InsuranceAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentInsuranceInfo;
import com.pjqs.dto.equipment.EquipmentInsuranceInfoRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 获取保险记录
 */

public class InsuranceLogActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {
    private InsuranceAdapter adapter;

    private List<EquipmentInsuranceInfo> insuranceList;
    private List<EquipmentInsuranceInfo> insuranceLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        pageable = "";
        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("保险记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        insuranceLists = new ArrayList<>();
        adapter = new InsuranceAdapter(insuranceLists);
        listView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, listView);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);

        getInsuranceLog(pageable);
    }

    //获取保险记录
    private void getInsuranceLog(final String pageable) {
        progressHUD = ProgressHUD.show(InsuranceLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_INSURANCE_LOG, new OkHttpClientManager.ResultCallback<EquipmentInsuranceInfoRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentInsuranceInfoRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    insuranceList = response.getInsuranceInfos();
                    if (insuranceList != null && insuranceList.size() > 0) {
                        if ("".equals(pageable)) {
                            insuranceLists.clear();
                            insuranceLists.addAll(insuranceList);
                            adapter.setNewData(insuranceList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            InsuranceLogActivity.this.pageable = response.getPagable();
                        } else {
                            insuranceLists.addAll(insuranceList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                InsuranceLogActivity.this.pageable = response.getPagable();
                                adapter.addData(insuranceList);
                            } else {
                                adapter.addData(insuranceList);
                                hasMore = false;
                                InsuranceLogActivity.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EquipmentInsuranceInfoRes.class, InsuranceLogActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getInsuranceLog(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                InsuranceLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_log__parent:
                startActivity(new Intent(InsuranceLogActivity.this, InsuranceDetailsActivity.class)
                        .putExtra("id", insuranceLists.get(position).getId().toString()));
                break;
        }
    }
}
