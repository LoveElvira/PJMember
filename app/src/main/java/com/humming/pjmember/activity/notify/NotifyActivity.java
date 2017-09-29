package com.humming.pjmember.activity.notify;

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
import com.humming.pjmember.adapter.NotifyAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.emergency.EmergencyInfoBean;
import com.pjqs.dto.emergency.EmergencyRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 通知 天气预报
 */

public class NotifyActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private NotifyAdapter adapter;

    private List<EmergencyInfoBean> emergencyList;
    private List<EmergencyInfoBean> emergencyLists;

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
        title.setText("通知");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//
//        adapter = new NotifyAdapter(list);
//        listView.setAdapter(adapter);

        emergencyLists = new ArrayList<>();

        leftArrow.setOnClickListener(this);
        pageable = "";
        getNotify(pageable);
    }

    //获取事故记录
    private void getNotify(final String pageable) {
        progressHUD = ProgressHUD.show(NotifyActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_DEFECT, new OkHttpClientManager.ResultCallback<EmergencyRes>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EmergencyRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    emergencyList = response.getEmergencys();
                    if (emergencyList != null && emergencyList.size() > 0) {
                        if ("".equals(pageable)) {
                            emergencyLists.clear();
                            emergencyLists.addAll(emergencyList);
                            adapter = new NotifyAdapter(emergencyList);
                            listView.setAdapter(adapter);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            NotifyActivity.this.pageable = response.getPagable();
                        } else {
                            emergencyLists.addAll(emergencyList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                NotifyActivity.this.pageable = response.getPagable();
                                adapter.addData(emergencyList);
                            } else {
                                adapter.addData(emergencyList);
                                hasMore = false;
                                NotifyActivity.this.pageable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(NotifyActivity.this, listView);
                        adapter.setOnItemChildClickListener(NotifyActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, EmergencyRes.class, NotifyActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getNotify(pageable);
                }
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                NotifyActivity.this.finish();
                break;
        }
    }
}
