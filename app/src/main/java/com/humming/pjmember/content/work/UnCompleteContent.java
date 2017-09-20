package com.humming.pjmember.content.work;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.WorkSafetyDisclosureActivity;
import com.humming.pjmember.adapter.WorkAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.work.QueryWorkRes;
import com.pjqs.dto.work.WorkInfoBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/1.
 * 未核销作业
 */

public class UnCompleteContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private WorkAdapter adapter;

    private List<WorkInfoBean> workList;//每一页
    private List<WorkInfoBean> workLists;//总的


    public UnCompleteContent(Context context) {
        this(context, null);
    }

    public UnCompleteContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_work_, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        pageable = "";
        listView = findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
        }

//        adapter = new WorkAdapter(list);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);
        workLists = new ArrayList<>();
//        getWorkDate(pageable);
        isOne = false;
        isShowProgress = true;
    }

    public void isInitFirst() {
        if (inspectNet()) {
            if (!isOne) {
                progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressHUD.dismiss();
                    }
                });
                getWorkDate(pageable);
                isOne = true;
            }
//            listView.setVisibility(VISIBLE);
//            noWifiLayout.setVisibility(GONE);
        } else {
//            listView.setVisibility(GONE);
//            noWifiLayout.setVisibility(VISIBLE);
        }
    }

    private void getWorkDate(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setType("2");
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_WORK, new OkHttpClientManager.ResultCallback<QueryWorkRes>() {
            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo().toString());
                Log.e("onError", info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(QueryWorkRes response) {
                closeProgress();
                if (response != null) {
                    workList = response.getWorks();
                    if (workList != null && workList.size() > 0) {
                        if ("".equals(pageable)) {
                            workLists.clear();
                            workLists.addAll(workList);
                            adapter = new WorkAdapter(workList);
                            listView.setAdapter(adapter);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            UnCompleteContent.this.pageable = response.getPagable();
                        } else {
                            workLists.addAll(workList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                UnCompleteContent.this.pageable = response.getPagable();
                                adapter.addData(workList);
                            } else {
                                adapter.addData(workList);
                                hasMore = false;
                                UnCompleteContent.this.pageable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(UnCompleteContent.this, listView);
                        adapter.setOnItemChildClickListener(UnCompleteContent.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, QueryWorkRes.class, Application.getInstance().getCurrentActivity().getClass());

    }

    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd();
                } else {
                    getWorkDate(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_work__parent:
                startActivity(new Intent(getContext(), WorkSafetyDisclosureActivity.class)
                        .putExtra("isLook", true));
                break;
        }
    }

}
