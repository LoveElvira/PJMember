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
import com.humming.pjmember.adapter.AccidentAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.EquipmentAcctidentInfoBean;
import com.pjqs.dto.equipment.EquipmentAcctidentRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/3.
 * 事故记录
 */

public class AccidentLogActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private AccidentAdapter adapter;

    private List<EquipmentAcctidentInfoBean> accidentList;
    private List<EquipmentAcctidentInfoBean> accidentLists;

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
        title.setText("事故记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        adapter = new RepairAdapter(list,3);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);

        accidentLists = new ArrayList<>();

        leftArrow.setOnClickListener(this);

        isShowProgress = true;
        progressHUD = ProgressHUD.show(AccidentLogActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getAccidentLog(pageable);
    }

    //获取事故记录
    private void getAccidentLog(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setEquipmentId(id);
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_ACCIDENT_LOG, new OkHttpClientManager.ResultCallback<EquipmentAcctidentRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(EquipmentAcctidentRes response) {
                closeProgress();
                if (response != null) {
                    accidentList = response.getAccident();
                    if (accidentList != null && accidentList.size() > 0) {
                        if ("".equals(pageable)) {
                            accidentLists.clear();
                            accidentLists.addAll(accidentList);
                            adapter = new AccidentAdapter(accidentList);
                            listView.setAdapter(adapter);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            AccidentLogActivity.this.pageable = response.getPageable();
                        } else {
                            accidentLists.addAll(accidentList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                AccidentLogActivity.this.pageable = response.getPageable();
                                adapter.addData(accidentList);
                            } else {
                                adapter.addData(accidentList);
                                hasMore = false;
                                AccidentLogActivity.this.pageable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(AccidentLogActivity.this, listView);
                        adapter.setOnItemChildClickListener(AccidentLogActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, EquipmentAcctidentRes.class, AccidentLogActivity.class);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getAccidentLog(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AccidentLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_log__parent:
                startActivity(new Intent(AccidentLogActivity.this, AccidentDetailsActivity.class)
                        .putExtra("id", accidentLists.get(position).getAccidentId()));
                break;
        }
    }
}
