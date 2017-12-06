package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.add.AddRunningParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 添加出车记录
 */

public class AddRunningActivity extends BaseActivity {

    //开始时间标题 开始时间
    private TextView startTimeTitle;
    private TextView startTime;
    //结束时间标题 结束时间
    private LinearLayout endTimeLayout;
    private TextView endTimeTitle;
    private TextView endTime;
    //公里数标题 公里数
    private TextView runKmTitle;
    private TextView runKm;
    //备注标题 备注
    private TextView remarkTitle;
    private TextView remark;

    //提交
    private TextView submit;

    private LinearLayout imageLayout;

    private boolean isStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");
        popupParent = View.inflate(getBaseContext(), R.layout.activity_add_log, null);

        title = findViewById(R.id.base_toolbar__title);
        title.setText("添加出车记录");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        startTimeTitle = findViewById(R.id.activity_add_log__time_title);
        startTime = findViewById(R.id.activity_add_log__time);
        endTimeLayout = findViewById(R.id.activity_add_log__company_layout);
        endTimeTitle = findViewById(R.id.activity_add_log__company_title);
        endTime = findViewById(R.id.activity_add_log__company);
        runKmTitle = findViewById(R.id.activity_add_log__price_title);
        runKm = findViewById(R.id.activity_add_log__price);
        remarkTitle = findViewById(R.id.activity_add_log__content_title);
        remark = findViewById(R.id.activity_add_log__content);
        imageLayout = findViewById(R.id.activity_add_log__listview_layout);
        submit = findViewById(R.id.activity_add_log__submit);


        startTimeTitle.setText("开始时间");
        startTime.setHint("请选择开始时间");
        endTimeLayout.setVisibility(View.VISIBLE);
        endTimeTitle.setText("结束时间");
        endTime.setHint("请选择结束时间");
        endTime.setFocusable(false);
        runKmTitle.setText("公里数(KM)");
        runKm.setHint("请输入公里数");
        remarkTitle.setText("备注(选填)");
        remark.setHint("请输入备注");
        imageLayout.setVisibility(View.GONE);


        leftArrow.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private boolean isNull(String startTime, String endTime, String runKm) {
        if (TextUtils.isEmpty(startTime)) {
            showShortToast("开始时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(endTime)) {
            showShortToast("结束时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(runKm)) {
            showShortToast("公里数不能为空");
            return false;
        }
        return true;
    }

    //新增设备出车信息
    private void addRunningLog(String startTime, String endTime, String runKm, String remark) {
        progressHUD = ProgressHUD.show(AddRunningActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        AddRunningParameter parameter = new AddRunningParameter();
        parameter.setEquipmentId(id);
        parameter.setStartTime(startTime);
        parameter.setEndTime(endTime);
        parameter.setRunKm(runKm);
        if (!StringUtils.isEmpty(remark)) {
            parameter.setRemark(remark);
        }

        OkHttpClientManager.postAsyn(Config.ADD_RUNNING_LOG, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo().toString());
                Log.e("onError", info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(SuccessResponse response) {
                progressHUD.dismiss();
                if (response != null) {
                    showShortToast(response.getMsg());
                    if (response.getCode() == 1) {
                        AddRunningActivity.this.finish();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, AddRunningActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddRunningActivity.this.finish();
                break;
            case R.id.activity_add_log__time:
                isStartTime = true;
                showPopWindowDatePicker(popupParent);
                break;
            case R.id.activity_add_log__company:
                isStartTime = false;
                showPopWindowDatePicker(popupParent);
                break;
            case R.id.date_submit://获取时间
                if (isStartTime) {
                    startTime.setText(getDate());
                } else {
                    endTime.setText(getDate());
                }
                break;
            case R.id.activity_add_log__submit:
                String startTimeStr = startTime.getText().toString().trim();
                String endTimeStr = endTime.getText().toString().trim();
                String runKmStr = runKm.getText().toString().trim();
                String remarkStr = remark.getText().toString().trim();
                if (isNull(startTimeStr, endTimeStr, runKmStr)) {
                    addRunningLog(startTimeStr, endTimeStr, runKmStr, remarkStr);
                }
                break;
        }
    }
}
