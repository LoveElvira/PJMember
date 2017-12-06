package com.humming.pjmember.activity.scan;

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
import com.humming.pjmember.adapter.RunningAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentRunnintBean;
import com.pjqs.dto.equipment.EquipmentRunnintInfoRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 出车记录
 */

public class RunningLogActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private RunningAdapter adapter;

    private List<EquipmentRunnintBean> runningList;
    private List<EquipmentRunnintBean> runningLists;

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
        title.setText("出车记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);


        runningLists = new ArrayList<>();
        adapter = new RunningAdapter(runningLists);
        listView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, listView);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
        getRunningLog(pageable);
    }


    //获取出车记录
    private void getRunningLog(final String pageable) {
        progressHUD = ProgressHUD.show(RunningLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_RUNNING_LOG, new OkHttpClientManager.ResultCallback<EquipmentRunnintInfoRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentRunnintInfoRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    runningList = response.getRunnintBeen();
                    if (runningList != null && runningList.size() > 0) {
                        if ("".equals(pageable)) {
                            runningLists.clear();
                            runningLists.addAll(runningList);
                            adapter.setNewData(runningList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            RunningLogActivity.this.pageable = response.getPagable();
                        } else {
                            runningLists.addAll(runningList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                RunningLogActivity.this.pageable = response.getPagable();
                                adapter.addData(runningList);
                            } else {
                                adapter.addData(runningList);
                                hasMore = false;
                                RunningLogActivity.this.pageable = "";
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
        }, parameter, EquipmentRunnintInfoRes.class, RunningLogActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getRunningLog(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                RunningLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
