package com.humming.pjmember.activity.meeting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.conference.ConferenceBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/11/22.
 */

public class MeetingDetailsActivity extends BaseActivity {

    //会议名称
    private TextView meetingName;
    //公司名称
    private TextView companyName;
    //执行部门
    private TextView department;
    //主持人名称
    private TextView director;
    //开始时间
    private TextView startTime;
    //结束时间
    private TextView endTime;
    //会议地点
    private TextView address;
    //会议人员
    private TextView person;
    //会议内容
    private TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("会议详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        meetingName = findViewById(R.id.activity_meeting_details__name);
        companyName = findViewById(R.id.activity_meeting_details__company_name);
        department = findViewById(R.id.activity_meeting_details__department);
        director = findViewById(R.id.activity_meeting_details__director);
        startTime = findViewById(R.id.activity_meeting_details__start_time);
        endTime = findViewById(R.id.activity_meeting_details__end_time);
        address = findViewById(R.id.activity_meeting_details__address);
        person = findViewById(R.id.activity_meeting_details__person);
        content = findViewById(R.id.activity_meeting_details__content);

        leftArrow.setOnClickListener(this);
        getMeetingDetails();
    }

    private void getMeetingDetails() {

        progressHUD = ProgressHUD.show(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setConferenceId(id);

        OkHttpClientManager.postAsyn(Config.GET_MEETING_DETAILS, new OkHttpClientManager.ResultCallback<ConferenceBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(ConferenceBean response) {
                progressHUD.dismiss();
                if (response != null) {
                    meetingName.setText(response.getConferenceName());
                    companyName.setText(response.getCompanyName());
                    department.setText(response.getExecuteOrgName());
                    director.setText(response.getEmceeOpaUserName());
                    startTime.setText(response.getStartTime());
                    endTime.setText(response.getEndTime());
                    address.setText(response.getSite());
                    person.setText(initHtml("会议人员", response.getUsers()));
                    content.setText(initHtml("会议内容", response.getConferenceContent()));

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, ConferenceBean.class, MeetingDetailsActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                MeetingDetailsActivity.this.finish();
                break;
        }
    }
}
