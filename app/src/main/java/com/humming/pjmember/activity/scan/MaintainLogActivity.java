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
import com.humming.pjmember.adapter.MaintainAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentMaintainInfoBean;
import com.pjqs.dto.equipment.EquipmentMaintainRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/3.
 * 保养记录
 */

public class MaintainLogActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private MaintainAdapter adapter;

    private List<EquipmentMaintainInfoBean> maintainList;
    private List<EquipmentMaintainInfoBean> maintainLists;

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
        title.setText("保养记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        maintainLists = new ArrayList<>();

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        adapter = new RepairAdapter(list, 2);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);

        isShowProgress = true;
        progressHUD = ProgressHUD.show(MaintainLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getMaintainLog(pageable);
    }

    //获取保养记录
    private void getMaintainLog(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_MAINTAIN_LOG, new OkHttpClientManager.ResultCallback<EquipmentMaintainRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(EquipmentMaintainRes response) {
                closeProgress();
                if (response != null) {
                    maintainList = response.getMaintain();
                    if (maintainList != null && maintainList.size() > 0) {
                        if ("".equals(pageable)) {
                            maintainLists.clear();
                            maintainLists.addAll(maintainList);
                            adapter = new MaintainAdapter(maintainList);
                            listView.setAdapter(adapter);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            MaintainLogActivity.this.pageable = response.getPageable();
                        } else {
                            maintainLists.addAll(maintainList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                MaintainLogActivity.this.pageable = response.getPageable();
                                adapter.addData(maintainList);
                            } else {
                                adapter.addData(maintainList);
                                hasMore = false;
                                MaintainLogActivity.this.pageable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(MaintainLogActivity.this, listView);
                        adapter.setOnItemChildClickListener(MaintainLogActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, EquipmentMaintainRes.class, MaintainLogActivity.class);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                MaintainLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getMaintainLog(pageable);
                }
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_log__parent:
                startActivity(new Intent(MaintainLogActivity.this, RepairDetailsActivity.class)
                        .putExtra("id", maintainLists.get(position).getMaintainId()));
                break;
        }
    }
}
