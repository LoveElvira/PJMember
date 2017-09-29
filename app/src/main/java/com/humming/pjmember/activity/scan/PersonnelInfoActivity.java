package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.work.WorkSafetyDisclosureActivity;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.roundview.RoundedImageView;
import com.pjqs.dto.equipment.Response;
import com.pjqs.dto.user.UserBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/27.
 * 人员信息
 */

public class PersonnelInfoActivity extends BaseActivity {

    //人员照片
    private RoundedImageView headImage;
    //人员姓名
    private TextView name;
    //人员性别
    private ImageView sex;
    //人员编号
    private TextView num;
    //联系方式
    private TextView phone;
    //人员工种
    private TextView jobType;
    //人员资历
    private TextView a;
    //人员专业
    private TextView major;
    //所属公司
    private TextView company;
    //所属部门
    private TextView org;
    //人员保险
    private TextView insurance;
    //人员保险
    private TextView safety;
    //添加人员
    private TextView addPerson;

    private boolean isAddPerson = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_info);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        isAddPerson = getIntent().getBooleanExtra("addPerson", false);
        userId = getIntent().getStringExtra("userId");
        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("人员信息");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        headImage = (RoundedImageView) findViewById(R.id.item_personnel_info__head_image);
        name = (TextView) findViewById(R.id.item_personnel_info__name);
        sex = (ImageView) findViewById(R.id.item_personnel_info__sex);
        num = (TextView) findViewById(R.id.item_personnel_info__num);
        phone = (TextView) findViewById(R.id.item_personnel_info__phone);
        jobType = (TextView) findViewById(R.id.item_personnel_info__job_type);
        major = (TextView) findViewById(R.id.item_personnel_info__major);
        company = (TextView) findViewById(R.id.item_personnel_info__company);
        org = (TextView) findViewById(R.id.item_personnel_info__org);
        insurance = (TextView) findViewById(R.id.item_personnel_info__insurance);
        safety = (TextView) findViewById(R.id.item_personnel_info__safety);
        addPerson = (TextView) findViewById(R.id.item_personnel_info__add);

        if (isAddPerson) {
            addPerson.setVisibility(View.VISIBLE);
        } else {
            addPerson.setVisibility(View.GONE);
        }

        leftArrow.setOnClickListener(this);
        addPerson.setOnClickListener(this);

        getPersonnelInfo();
    }

    //获取人员详细信息
    private void getPersonnelInfo() {

        progressHUD = ProgressHUD.show(PersonnelInfoActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setUserId(userId);
        OkHttpClientManager.postAsyn(Config.GET_WORK_PERSON_INFO, new OkHttpClientManager.ResultCallback<UserBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(UserBean response) {
                progressHUD.dismiss();
                if (response != null) {

                    Glide.with(getBaseContext())
                            .load(response.getPhoto())
                            .error(R.mipmap.bg)
                            .into(headImage);
                    name.setText(response.getName());

                    if ("1".equals(response.getSex())) {//性别(1:男；2：女)
                        sex.setImageResource(R.mipmap.sex_boy_small);
                    } else {
                        sex.setImageResource(R.mipmap.sex_girl_small);
                    }

                    num.setText(response.getUserCode());
                    phone.setText(response.getPhone());
                    jobType.setText(response.getJobType());
                    major.setText(response.getMajor());
                    company.setText(response.getCompanyName());
                    org.setText(response.getOrgName());
                    if (response.getIsInsurance() == 1) {//是否购买人生保险：1是，0否
                        insurance.setText("已购买");
                    } else {
                        insurance.setText("未购买");
                    }
                    if (response.getIsPassThreegradesafetyEdu() == 1) {//是否通过三级安全教育：1是，0否
                        safety.setText("通过三级安全教育");
                    } else {
                        safety.setText("未通过三级安全教育");
                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, UserBean.class, PersonnelInfoActivity.class);
    }


    //添加人员
    private void addPerson() {

        progressHUD = ProgressHUD.show(PersonnelInfoActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        parameter.setUserId(userId);
        OkHttpClientManager.postAsyn(Config.ADD_WORK_PERSON, new OkHttpClientManager.ResultCallback<Response>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(Response response) {
                progressHUD.dismiss();
                if (response != null) {
                    if ("1".equals(response.getCode())) {
                        PersonnelInfoActivity.this.finish();
                    } else {
                        showShortToast(response.getMsg());
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, Response.class, PersonnelInfoActivity.class);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                PersonnelInfoActivity.this.finish();
                break;
            case R.id.item_personnel_info__add:
                addPerson();
                break;
        }
    }
}
