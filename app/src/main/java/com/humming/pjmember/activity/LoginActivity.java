package com.humming.pjmember.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.humming.pjmember.MainActivity;
import com.humming.pjmember.R;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.login.LevelBean;
import com.pjqs.dto.login.LoginResponse;

import java.io.Serializable;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/8/31.
 */

public class LoginActivity extends BaseActivity {

    //用户名
    private EditText userName;
    //密码
    private EditText password;
    //登录按钮
    private TextView goLogin;
    //微信登录
    private LinearLayout wechatLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        userName = (EditText) findViewById(R.id.activity_login__username);
        password = (EditText) findViewById(R.id.activity_login__password);
        goLogin = (TextView) findViewById(R.id.activity_login__login);
        wechatLogin = (LinearLayout) findViewById(R.id.activity_login__wechat_login);

        goLogin.setOnClickListener(this);
        wechatLogin.setOnClickListener(this);


        String name = SharePrefUtil.getString(Constant.FILE_NAME, Constant.USERNAME, "", this);
        String pwd = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PASSWORD, "", this);

        if (!TextUtils.isEmpty(name)) {
            userName.setText(name);
        }
        if (!TextUtils.isEmpty(pwd)) {
            password.setText(pwd);
        }

    }

    private boolean isEditNull(String name, String pwd) {
        if (TextUtils.isEmpty(name)) {
            showShortToast("用户名不能为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            showShortToast("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_login__login:
                String name = userName.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (isEditNull(name, pwd)) {
                    goLogin(name, pwd);
                }
                break;
            case R.id.activity_login__wechat_login:
                break;
        }
    }


    //登录接口
    private void goLogin(final String name, final String pwd) {

        progressHUD = ProgressHUD.show(LoginActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setPhone(name);
        parameter.setPwd(pwd);

        OkHttpClientManager.postAsyn(Config.USER_LOGIN, new OkHttpClientManager.ResultCallback<LoginResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.i("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(LoginResponse response) {
                progressHUD.dismiss();
                if (response != null) {
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.TOKEN, response.getToken(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.NICKNAME, response.getNickName(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.HEADURL, response.getHeadImgUrl(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.USER_ID, response.getUserId(), LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.USERNAME, name, LoginActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, pwd, LoginActivity.this);
                    startActivity(response.getLevelBeans());
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.i("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, LoginResponse.class, LoginActivity.class);

    }

    //推送绑定账号
    private void bindAccount() {
        String userId = SharePrefUtil.getString(Constant.FILE_NAME, "userId", "", LoginActivity.this);
        if (!"".equals(userId)) {
            final CloudPushService pushService = Application.getInstance().pushService;
            pushService.bindAccount(userId, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i("TAG", "----" + pushService.getDeviceId());
                    SharePrefUtil.putString(Constant.FILE_NAME, "deviceId", pushService.getDeviceId(), LoginActivity.this);
                }

                @Override
                public void onFailed(String s, String s1) {

                }
            });
        }
    }

    private void startActivity(List<LevelBean> levelBeanList) {
        if (levelBeanList.size() == 1) {
            if (levelBeanList.get(0).getLevelNo() == 0) {//一线人员
                onLinePosition = "0";
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "0", LoginActivity.this);
            } else if (levelBeanList.get(0).getLevelNo() == 1) {//业务人员
                onLinePosition = "1";
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "1", LoginActivity.this);
            } else if (levelBeanList.get(0).getLevelNo() == 2) {//领导人员
                onLinePosition = "2";
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "2", LoginActivity.this);
            }
            startActivity(MainActivity.class);
        } else {
            startActivity(new Intent(LoginActivity.this, SelectPositonActivity.class)
                    .putExtra("levelBeanList", (Serializable) levelBeanList));
        }
        LoginActivity.this.finish();

    }

}
