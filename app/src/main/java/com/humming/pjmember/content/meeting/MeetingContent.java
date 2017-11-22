package com.humming.pjmember.content.meeting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.affair.ContractDetailsActivity;
import com.humming.pjmember.activity.meeting.MeetingDetailsActivity;
import com.humming.pjmember.adapter.MeetingAdapter;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.conference.ConferenceInfoBean;
import com.pjqs.dto.conference.ConferenceInfoResp;
import com.pjqs.dto.contract.ContractDetailBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/7.
 */

public class MeetingContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener {

    private MeetingAdapter adapter;
    private List<ConferenceInfoBean> meetingLists;

    private String date;

    public MeetingContent(Context context) {
        this(context, null);
    }

    public MeetingContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.common_listview, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        meetingLists = new ArrayList<>();
        adapter = new MeetingAdapter(meetingLists);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

    }

    public void getMeetingList(String date) {

        progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        this.date = date;
        RequestParameter parameter = new RequestParameter();
        parameter.setDate(date);

        OkHttpClientManager.postAsyn(Config.GET_MEETING, new OkHttpClientManager.ResultCallback<ConferenceInfoResp>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(ConferenceInfoResp response) {
                progressHUD.dismiss();
                if (response != null) {
                    if (response.getConferences() != null && response.getConferences().size() > 0) {
                        meetingLists.clear();
                        meetingLists.addAll(response.getConferences());
                        adapter.setNewData(meetingLists);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, ConferenceInfoResp.class, Application.getInstance().getCurrentActivity().getClass());

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_meeting__parent:
                startActivity(new Intent(getContext(), MeetingDetailsActivity.class)
                        .putExtra("id", meetingLists.get(position).getId()));
                break;
        }
    }
}
