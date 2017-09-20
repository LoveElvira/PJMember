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
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentRepairInfoBean;
import com.pjqs.dto.equipment.EquipmentRepairRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/3.
 * 维修记录
 */

public class RepairLogActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RepairAdapter adapter;

    private List<EquipmentRepairInfoBean> repairList;
    private List<EquipmentRepairInfoBean> repairLists;

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
        title.setText("维修记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        adapter = new RepairAdapter(list,1);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);
        repairLists = new ArrayList<>();

        leftArrow.setOnClickListener(this);

        isShowProgress = true;
        progressHUD = ProgressHUD.show(RepairLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getRepairLog(pageable);
    }

    //获取维修记录
    private void getRepairLog(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_REPAIR_LOG, new OkHttpClientManager.ResultCallback<EquipmentRepairRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(EquipmentRepairRes response) {
                closeProgress();
                if (response != null) {
                    repairList = response.getRepairs();
                    if (repairList != null && repairList.size() > 0) {
                        if ("".equals(pageable)) {
                            repairLists.clear();
                            repairLists.addAll(repairList);
                            adapter = new RepairAdapter(repairList);
                            listView.setAdapter(adapter);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            RepairLogActivity.this.pageable = response.getPageable();
                        } else {
                            repairLists.addAll(repairList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                RepairLogActivity.this.pageable = response.getPageable();
                                adapter.addData(repairList);
                            } else {
                                adapter.addData(repairList);
                                hasMore = false;
                                RepairLogActivity.this.pageable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(RepairLogActivity.this, listView);
                        adapter.setOnItemChildClickListener(RepairLogActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, EquipmentRepairRes.class, RepairLogActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getRepairLog(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                RepairLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_log__parent:
                startActivity(new Intent(RepairLogActivity.this, RepairDetailsActivity.class)
                        .putExtra("id", repairLists.get(position).getRepairId()));
                break;
        }
    }
}
