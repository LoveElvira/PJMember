package com.humming.pjmember.content.work;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.WorkPlanActivity;
import com.humming.pjmember.activity.work.WorkSafetyDisclosureActivity;
import com.humming.pjmember.adapter.WorkAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.CircleTextProgressbar;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.work.QueryWorkRes;
import com.pjqs.dto.work.WorkInfoBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/1.
 * 今日工作
 */

public class WorkTodayContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private WorkAdapter adapter;
    private CircleTextProgressbar circleText;
    //今日工作数量
    private TextView workNum;
    //已完成数量
    private TextView completeNum;
    //未已完成数量
    private TextView unCompleteNum;

    private List<WorkInfoBean> workList;//每一页
    private List<WorkInfoBean> workLists;//总的

    public WorkTodayContent(Context context) {
        this(context, null);
    }

    public WorkTodayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_work_today, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        pageable = "";

        refresh = findViewById(R.id.common_refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        refresh.setOnRefreshListener(this);

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);


        workLists = new ArrayList<>();

        adapter = new WorkAdapter(workLists);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnLoadMoreListener(this, listView);
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
                getWorkData(pageable);
                isOne = true;
            }
//            listView.setVisibility(VISIBLE);
//            noWifiLayout.setVisibility(GONE);
        } else {
//            listView.setVisibility(GONE);
//            noWifiLayout.setVisibility(VISIBLE);
        }
    }

    public void updateView(int roadWorkState, String roadWork, int isSafety, int position) {
        if (workLists.get(position).getIsSafety() != isSafety) {
            workLists.get(position).setIsSafety(isSafety);
        }
        if (workLists.get(position).getRoadWorkState() != roadWorkState) {
            workLists.get(position).setRoadWorkState(roadWorkState);
        }
        if (!roadWork.equals(workLists.get(position).getRoadWork())) {
            workLists.get(position).setRoadWork(roadWork);
        }
        adapter.notifyDataSetChanged();
    }

    private View getHeaderView(int finishWork, int sumWork) {
        View headView = inflate(getContext(), R.layout.item_work_top, null);
        workNum = headView.findViewById(R.id.item_work_top__work_num);
        completeNum = headView.findViewById(R.id.item_work_top__complete_num);
        unCompleteNum = headView.findViewById(R.id.item_work_top__uncomplete_num);
        circleText = headView.findViewById(R.id.item_work_top__progress);
        circleText.setOutLineColor(ContextCompat.getColor(getContext(), R.color.white));
        circleText.setInCircleColor(Color.TRANSPARENT);
        circleText.setProgressColor(ContextCompat.getColor(getContext(), R.color.blue_365DFE));
        circleText.setProgressLineWidth(10);
        circleText.setOutLineWidth(10);
        circleText.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
        circleText.start((int)(((float)finishWork / (float) sumWork) * 100));
        workNum.setText(sumWork + "");
        completeNum.setText(finishWork + "");
        unCompleteNum.setText((sumWork - finishWork) + "");
        return headView;
    }

    private void getWorkData(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setType("1");
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_WORK, new OkHttpClientManager.ResultCallback<QueryWorkRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
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
                            adapter.setNewData(workList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            WorkTodayContent.this.pageable = response.getPagable();
                            adapter.removeAllHeaderView();
                            adapter.addHeaderView(getHeaderView(response.getFinishWork(), response.getSumWork()));
                        } else {
                            workLists.addAll(workList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                WorkTodayContent.this.pageable = response.getPagable();
                                adapter.addData(workList);
                            } else {
                                adapter.addData(workList);
                                hasMore = false;
                                WorkTodayContent.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
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
        refresh.setEnabled(false);
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd(false);
                } else {
                    getWorkData(pageable);
                }
                refresh.setEnabled(true);
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_work__parent:
                Intent intent = new Intent();
                if (workLists.get(position).getIsSafety() == 1) {//已经读过安全交底
                    intent.setClass(getContext(), WorkPlanActivity.class);
                } else {
                    intent.setClass(getContext(), WorkSafetyDisclosureActivity.class);
                    intent.putExtra("isLook", true);
                }
                intent.putExtra("id", workLists.get(position).getWorkId() + "");
                intent.putExtra("position", position);
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, Constant.CODE_REQUEST_ONE);
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
                getWorkData(pageable);
                refresh.setRefreshing(false);
                adapter.loadMoreEnd(true);
                adapter.setEnableLoadMore(true);
            }
        });
    }
}
