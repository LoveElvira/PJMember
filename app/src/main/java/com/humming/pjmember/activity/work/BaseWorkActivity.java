package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.work.WorkBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/21.
 * 为了 交底内容 和 工作内容共用一个接口
 */

public class BaseWorkActivity extends BaseActivity {

    protected WorkBean workBean;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
    }

    //获取作业详情
    protected void getWorkDetails(final Handler handler) {
        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        OkHttpClientManager.postAsyn(Config.GET_WORK_DETAILS, new OkHttpClientManager.ResultCallback<WorkBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(WorkBean response) {
                progressHUD.dismiss();
                if (response != null) {
                    Message msg = new Message();
                    msg.what = Constant.CODE_SUCCESS;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, WorkBean.class, BaseWorkActivity.class);
    }

}
