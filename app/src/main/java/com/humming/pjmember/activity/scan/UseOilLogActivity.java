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
import com.humming.pjmember.adapter.RepairAdapter;
import com.humming.pjmember.adapter.UseOilAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentOilRecInfo;
import com.pjqs.dto.equipment.EquipmentOilRecInfoRes;
import com.pjqs.dto.equipment.EquipmentRepairRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/3.
 * 用油记录
 */

public class UseOilLogActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private UseOilAdapter adapter;

    private List<EquipmentOilRecInfo> oilList;
    private List<EquipmentOilRecInfo> oilLists;

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
        title.setText("用油记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);


        oilLists = new ArrayList<>();
        adapter = new UseOilAdapter(oilLists);
        listView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, listView);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
        getUseOilLog(pageable);
    }

    //获取用油记录
    private void getUseOilLog(final String pageable) {
        progressHUD = ProgressHUD.show(UseOilLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_OIL_LOG, new OkHttpClientManager.ResultCallback<EquipmentOilRecInfoRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentOilRecInfoRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    oilList = response.getOilRecInfoList();
                    if (oilList != null && oilList.size() > 0) {
                        if ("".equals(pageable)) {
                            oilLists.clear();
                            oilLists.addAll(oilList);
                            adapter.setNewData(oilList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            UseOilLogActivity.this.pageable = response.getPagable();
                        } else {
                            oilLists.addAll(oilList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                UseOilLogActivity.this.pageable = response.getPagable();
                                adapter.addData(oilList);
                            } else {
                                adapter.addData(oilList);
                                hasMore = false;
                                UseOilLogActivity.this.pageable = "";
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
        }, parameter, EquipmentOilRecInfoRes.class, UseOilLogActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getUseOilLog(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                UseOilLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_use_oil__parent:
                startActivity(new Intent(UseOilLogActivity.this, UseOilDetailsActivity.class)
                        .putExtra("id", oilLists.get(position).getOilId()));
                break;
        }
    }
}
