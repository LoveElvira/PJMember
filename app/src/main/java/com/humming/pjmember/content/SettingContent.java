package com.humming.pjmember.content;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.LoginActivity;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseLinearLayout;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.utils.SharePrefUtil;

/**
 * Created by Elvira on 2017/8/31.
 * 设置
 */

public class SettingContent extends BaseLinearLayout {

    //头像
    private ImageView headImage;
    //用户名
    private TextView userName;
    //所属公司
    private TextView company;
    //修改密码
    private LinearLayout updatePwdLayout;
    //退出登录
    private TextView exitLogin;

    public SettingContent(Context context) {
        this(context, null);
    }

    public SettingContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_setting, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = findViewById(R.id.base_toolbar__title);
        title.setText("设置");

        headImage = findViewById(R.id.fragment_setting__head_image);
        userName = findViewById(R.id.fragment_setting__name);
        company = findViewById(R.id.fragment_setting__company);
        updatePwdLayout = findViewById(R.id.fragment_setting__update_pwd);
        exitLogin = findViewById(R.id.fragment_setting__exit);

        String nickName = SharePrefUtil.getString(Constant.FILE_NAME, Constant.NICKNAME, "", Application.getInstance().getCurrentActivity());
        String url = SharePrefUtil.getString(Constant.FILE_NAME, Constant.HEADURL, "", Application.getInstance().getCurrentActivity());

        userName.setText(nickName);
        Glide.with(getContext())
                .load(url)
                .into(headImage);
        updatePwdLayout.setOnClickListener(this);
        exitLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_setting__update_pwd://修改密码
                break;
            case R.id.fragment_setting__exit://退出登录

                startActivity(LoginActivity.class);

                break;
        }
    }
}
