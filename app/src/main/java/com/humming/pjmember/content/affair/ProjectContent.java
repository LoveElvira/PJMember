package com.humming.pjmember.content.affair;

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
import com.humming.pjmember.activity.affair.ProjectDetailsActivity;
import com.humming.pjmember.adapter.ProjectAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.project.ProjectInfoBean;
import com.pjqs.dto.project.ProjectInfoRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/7.
 * 项目
 */

public class ProjectContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ProjectAdapter adapter;

    private List<ProjectInfoBean> projectList;
    private List<ProjectInfoBean> projectLists;

    public ProjectContent(Context context) {
        this(context, null);
    }

    public ProjectContent(Context context, AttributeSet attrs) {
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

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }

        projectLists = new ArrayList<>();
        adapter = new ProjectAdapter(projectLists);
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
            getProject(pageable);
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
        getProject(pageable);
    }


    private void getProject(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT, new OkHttpClientManager.ResultCallback<ProjectInfoRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(ProjectInfoRes response) {
                closeProgress();
                if (response != null) {
                    projectLists.clear();
                    projectList = response.getProjects();
                    if (projectList != null && projectList.size() > 0) {
                        if ("".equals(pageable)) {
                            projectLists.addAll(projectList);
                            adapter.setNewData(projectList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            ProjectContent.this.pageable = response.getPagable();
                        } else {
                            projectLists.addAll(projectList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                ProjectContent.this.pageable = response.getPagable();
                                adapter.addData(projectList);
                            } else {
                                adapter.addData(projectList);
                                hasMore = false;
                                ProjectContent.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setNewData(projectLists);
                    }
                }


            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, ProjectInfoRes.class, Application.getInstance().getCurrentActivity().getClass());

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
                    getProject(pageable);
                }
                refresh.setEnabled(true);
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_affair__parent:
                Intent intent = new Intent(getContext(), ProjectDetailsActivity.class);
                intent.putExtra("id", projectLists.get(position).getProId());
                intent.putExtra("position", position);
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, Constant.CODE_REQUEST_THREE);
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

}
