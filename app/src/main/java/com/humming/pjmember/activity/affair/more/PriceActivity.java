package com.humming.pjmember.activity.affair.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.AffairPriceAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.flow.CostDetailInfoBean;
import com.pjqs.dto.flow.CostDetailResp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/9.
 * 费用审批列表
 */

public class PriceActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private AffairPriceAdapter adapter;

    List<CostDetailInfoBean> projectList;
    List<CostDetailInfoBean> projectLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = findViewById(R.id.base_toolbar__title);
        title.setText("费用");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        refresh = findViewById(R.id.common_refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        refresh.setOnRefreshListener(this);

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        projectLists = new ArrayList<>();
        adapter = new AffairPriceAdapter(projectLists);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnLoadMoreListener(this, listView);

        leftArrow.setOnClickListener(this);

        pageable = "";
        getProject(pageable);
    }

    private void getProject(final String pageable) {
        progressHUD = ProgressHUD.show(PriceActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_PRICE, new OkHttpClientManager.ResultCallback<CostDetailResp>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(CostDetailResp response) {
                progressHUD.dismiss();
                if (response != null) {
                    projectLists.clear();
                    projectList = response.getCostDetailInfoBeen();
                    if (projectList != null && projectList.size() > 0) {
                        if ("".equals(pageable)) {
                            projectLists.addAll(projectList);
                            adapter.setNewData(projectList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            PriceActivity.this.pageable = response.getPagable();
                        } else {
                            projectLists.addAll(projectList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                PriceActivity.this.pageable = response.getPagable();
                                adapter.addData(projectList);
                            } else {
                                adapter.addData(projectList);
                                hasMore = false;
                                PriceActivity.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setNewData(projectLists);
                    }
                } else {
                    projectLists.clear();
                    adapter.setNewData(projectLists);
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, CostDetailResp.class, PriceActivity.class);

    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd(false);
                } else {
                    getProject(pageable);
                }
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_affair_price__parent:
                startActivityForResult(new Intent(PriceActivity.this, PriceDetailsActivity.class)
                        .putExtra("id", projectLists.get(position).getCostDetailId())
                        .putExtra("position", position), Constant.CODE_REQUEST_ONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Constant.CODE_RESULT)
            return;
        int position = data.getIntExtra("position", -1);
        switch (requestCode) {
            case Constant.CODE_REQUEST_ONE:
                pageable = "";
                getProject(pageable);
                break;
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        listView.post(new Runnable() {
            @Override
            public void run() {
                pageable = "";
                getProject(pageable);
                refresh.setRefreshing(false);
                adapter.loadMoreEnd(true);
                adapter.setEnableLoadMore(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                PriceActivity.this.finish();
                break;
        }
    }

}
