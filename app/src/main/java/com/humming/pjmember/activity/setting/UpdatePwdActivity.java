package com.humming.pjmember.activity.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.activity.LoginActivity;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.responsedate.NullResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.ProgressHUD;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/11/9.
 */

public class UpdatePwdActivity extends BaseActivity {

    //旧密码
    private EditText oldPwd;
    //新密码
    private EditText newPwd;
    //确认修改按钮
    private TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("修改密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        oldPwd = (EditText) findViewById(R.id.activity_update_pwd__old);
        newPwd = (EditText) findViewById(R.id.activity_update_pwd__new);
        confirm = (TextView) findViewById(R.id.activity_update_pwd__confirm);

        confirm.setOnClickListener(this);
        leftArrow.setOnClickListener(this);

    }

    private boolean isNull(String oldPwd, String newPwd) {
        if (TextUtils.isEmpty(oldPwd)) {
            showShortToast("旧密码不能为空");
            return false;
        } else if (TextUtils.isEmpty(newPwd)) {
            showShortToast("新密码不能为空");
            return false;
        }
        return true;
    }

    //修改密码
    private void updatePwd(final String oldPwd, final String newPwd) {

        progressHUD = ProgressHUD.show(UpdatePwdActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setOldPwd(oldPwd);
        parameter.setNewPwd(newPwd);

        OkHttpClientManager.postAsyn(Config.UPDATE_PWD, new OkHttpClientManager.ResultCallback<NullResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.i("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(NullResponse response) {
                progressHUD.dismiss();
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, newPwd, UpdatePwdActivity.this);
                UpdatePwdActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.i("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, NullResponse.class, LoginActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                UpdatePwdActivity.this.finish();
                break;
            case R.id.activity_update_pwd__confirm:
                String oldPwd = this.oldPwd.getText().toString().trim();
                String newPwd = this.newPwd.getText().toString().trim();
                if (isNull(oldPwd, newPwd)) {
                    updatePwd(oldPwd, newPwd);
                }
                break;
        }
    }
}
