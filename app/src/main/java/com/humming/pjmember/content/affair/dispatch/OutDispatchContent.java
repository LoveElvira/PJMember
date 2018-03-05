package com.humming.pjmember.content.affair.dispatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.affair.ProjectFileDetailsActivity;
import com.humming.pjmember.activity.affair.more.OutDispatchDetailsActivity;
import com.humming.pjmember.adapter.OutDispatchAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.flow.DisFileInfoBean;
import com.pjqs.dto.flow.DisFileResp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/26.
 */

public class OutDispatchContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private OutDispatchAdapter adapter;

    private List<DisFileInfoBean> projectList;
    private List<DisFileInfoBean> projectLists;


    public OutDispatchContent(Context context) {
        this(context, null);
    }

    public OutDispatchContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_work_, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        refresh = findViewById(R.id.common_refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        refresh.setOnRefreshListener(this);

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        projectLists = new ArrayList<>();
        adapter = new OutDispatchAdapter(projectLists);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnLoadMoreListener(this, listView);
        isOne = false;
        isShowProgress = true;

    }

    public void isInitFirst() {
//        if (inspectNet()) {
        if (!isOne) {
            progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressHUD.dismiss();
                }
            });
            getOutDispatch(pageable);
            isOne = true;
        }
//            listView.setVisibility(VISIBLE);
//            noWifiLayout.setVisibility(GONE);
//        } else {
//            listView.setVisibility(GONE);
//            noWifiLayout.setVisibility(VISIBLE);
//        }
    }


    public void updateData() {
//        isShowProgress = true;
//        progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                progressHUD.dismiss();
//            }
//        });
        pageable = "";
        getOutDispatch(pageable);
    }

    private void getOutDispatch(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_OUT_DISPATCH, new OkHttpClientManager.ResultCallback<DisFileResp>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(DisFileResp response) {
                closeProgress();
                if (response != null) {
                    projectLists.clear();
                    projectList = response.getDisFileInfoBeen();
                    if (projectList != null && projectList.size() > 0) {
                        if ("".equals(pageable)) {
                            projectLists.addAll(projectList);
                            adapter.setNewData(projectList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            OutDispatchContent.this.pageable = response.getPagable();
                        } else {
                            projectLists.addAll(projectList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                OutDispatchContent.this.pageable = response.getPagable();
                                adapter.addData(projectList);
                            } else {
                                adapter.addData(projectList);
                                hasMore = false;
                                OutDispatchContent.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setNewData(projectLists);
                    }
                }else{
                    projectLists.clear();
                    adapter.setNewData(projectLists);
                }


            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, DisFileResp.class, Application.getInstance().getCurrentActivity().getClass());

    }

    @Override
    public void onLoadMoreRequested() {
        refresh.setEnabled(false);
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd(false);
                } else {
                    isShowProgress = true;
                    progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                        }
                    });
                    getOutDispatch(pageable);
                }
                refresh.setEnabled(true);
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_dispatch__parent:
                Intent intent = new Intent(getContext(), OutDispatchDetailsActivity.class);
                intent.putExtra("id", projectLists.get(position).getId().toString());
                intent.putExtra("position", position);
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, Constant.CODE_REQUEST_TWO);
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
                getOutDispatch(pageable);
                refresh.setRefreshing(false);
                adapter.loadMoreEnd(true);
                adapter.setEnableLoadMore(true);
            }
        });
    }
}

