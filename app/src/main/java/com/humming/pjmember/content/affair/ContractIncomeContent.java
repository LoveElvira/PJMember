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
import com.humming.pjmember.activity.affair.ContractDetailsActivity;
import com.humming.pjmember.adapter.ContractAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.contract.ContractInfoBean;
import com.pjqs.dto.contract.ContractInfoRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/7.
 * 收入合同
 */

public class ContractIncomeContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private String conNature = "1";

    private ContractAdapter adapter;

    private List<ContractInfoBean> contractList;
    private List<ContractInfoBean> contractLists;

    public ContractIncomeContent(Context context) {
        this(context, null);
    }

    public ContractIncomeContent(Context context, AttributeSet attrs) {
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

        contractLists = new ArrayList<>();
        adapter = new ContractAdapter(contractLists);
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
            getContract(pageable);
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
        getContract(pageable);
    }


    private void getContract(final String pageable) {
        RequestParameter parameter = new RequestParameter();
        parameter.setConNature(conNature);
        parameter.setPagable(pageable);

        OkHttpClientManager.postAsyn(Config.GET_CONTRACT, new OkHttpClientManager.ResultCallback<ContractInfoRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                closeProgress();
            }

            @Override
            public void onResponse(ContractInfoRes response) {
                closeProgress();
                if (response != null) {
                    contractLists.clear();
                    contractList = response.getContracts();
                    if (contractList != null && contractList.size() > 0) {
                        if ("".equals(pageable)) {
                            contractLists.addAll(contractList);
                            adapter.setNewData(contractList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            ContractIncomeContent.this.pageable = response.getPagable();
                        } else {
                            contractLists.addAll(contractList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                ContractIncomeContent.this.pageable = response.getPagable();
                                adapter.addData(contractList);
                            } else {
                                adapter.addData(contractList);
                                hasMore = false;
                                ContractIncomeContent.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setNewData(contractLists);
                    }
                }else{
                    contractLists.clear();
                    adapter.setNewData(contractLists);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                closeProgress();
            }
        }, parameter, ContractInfoRes.class, Application.getInstance().getCurrentActivity().getClass());

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
                    getContract(pageable);
                }
                refresh.setEnabled(true);
            }
        }, delayMillis);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_affair__parent:
                Intent intent = new Intent(getContext(), ContractDetailsActivity.class);
                intent.putExtra("id", contractLists.get(position).getConId());
                intent.putExtra("conNature", conNature);
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
                getContract(pageable);
                refresh.setRefreshing(false);
                adapter.loadMoreEnd(true);
                adapter.setEnableLoadMore(true);
            }
        });
    }

}
