package com.humming.pjmember.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.MainActivity;
import com.humming.pjmember.R;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.service.ResponseData;
import com.humming.pjmember.utils.SharePrefUtil;
import com.pjqs.dto.login.LoginResponse;

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
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.USERNAME, name, this);
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, pwd, this);
                    if (name.equals("a") && pwd.equals("a")) {
                        startActivity(MainActivity.class);
                    } else {
                        startActivity(SelectPositonActivity.class);
                    }
                    LoginActivity.this.finish();
                }
                break;
            case R.id.activity_login__wechat_login:
                break;
        }
    }


    //登录接口
    private void goLogin(String name, String pwd) {

        RequestParameter parameter = new RequestParameter();
        parameter.setPhone(name);
        parameter.setPwd(pwd);

        OkHttpClientManager.postAsyn(Config.USER_LOGIN, new OkHttpClientManager.ResultCallback<LoginResponse>() {
            @Override
            public void onError(Request request, Error info) {

            }

            @Override
            public void onResponse(LoginResponse response) {
                if (response != null) {
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.TOKEN, response.getToken(), LoginActivity.this);
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {

            }
        }, parameter, LoginResponse.class, LoginActivity.class);

    }

}
