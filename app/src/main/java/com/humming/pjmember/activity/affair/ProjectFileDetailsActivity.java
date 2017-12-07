package com.humming.pjmember.activity.affair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.contract.FloNodeExeRecordBean;
import com.pjqs.dto.project.ProjectAccessoryCheckBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/12/6.
 * 项目文件详情
 */

public class ProjectFileDetailsActivity extends BaseActivity {


    //项目名称
    private TextView projectName;
    //文件类型
    private TextView projectType;
    //上传人
    private TextView person;
    //文件上传时间
    private TextView time;
    //文件url
    private TextView url;

    //展开相关审批意见
    private LinearLayout openMiddleLayout;
    //相关审批意见条目
    private LinearLayout middleParentLayout;
    //展开相关审批意见 文字
    private TextView openMiddleText;
    //展开相关审批意见 图片
    private ImageView openMiddleImage;
    //相关审批意见 条数
    private TextView ideaNum;


    //我的意见
    private EditText mySuggestion;

    //同意 按钮
    private TextView agreeBtn;
    //驳回 按钮
    private TextView rejectBtn;

    private ProjectAccessoryCheckBean checkBean;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        id = getIntent().getStringExtra("id");
        position = getIntent().getIntExtra("position", -1);

        title = findViewById(R.id.base_toolbar__title);
        title.setText("项目文件详情");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        projectName = findViewById(R.id.item_project_file_top__name);
        projectType = findViewById(R.id.item_project_file_top__type);
        person = findViewById(R.id.item_project_file_top__person);
        time = findViewById(R.id.item_project_file_top__time);
        url = findViewById(R.id.item_project_file_top__address);

        openMiddleLayout = findViewById(R.id.activity_project_file_details__middle_open_layout);
        middleParentLayout = findViewById(R.id.activity_project_file_details__middle_parent);
        openMiddleText = findViewById(R.id.activity_project_file_details__middle_open_text);
        openMiddleImage = findViewById(R.id.activity_project_file_details__middle_open_image);
        ideaNum = findViewById(R.id.activity_project_file_details__middle_num);

        mySuggestion = findViewById(R.id.activity_project_file_details__suggestion);

        agreeBtn = findViewById(R.id.activity_project_file_details__agree);
        rejectBtn = findViewById(R.id.activity_project_file_details__reject);

        middleParentLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        getProjectFileDetail();
    }

    //相关审批意见 条目
    private View getItemMiddle(FloNodeExeRecordBean recordBean, boolean isLast) {
        View view = View.inflate(this, R.layout.item_contract_middle, null);

        TextView type = view.findViewById(R.id.item_contract_middle__type);
        TextView name = view.findViewById(R.id.item_contract_middle__name);
        TextView time = view.findViewById(R.id.item_contract_middle__time);
        TextView idea = view.findViewById(R.id.item_contract_middle__check_idea);

        type.setText(recordBean.getType());
        name.setText(recordBean.getUserName());
        time.setText(recordBean.getTime());
        idea.setText(recordBean.getCheckIdea());

        View line = view.findViewById(R.id.item_contract__middle_line);
        line.setVisibility(View.VISIBLE);
        if (isLast) {
            line.setVisibility(View.GONE);
        }
        return view;
    }


    private void getProjectFileDetail() {

        progressHUD = ProgressHUD.show(ProjectFileDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setProjectId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_FILE_DETAILS, new OkHttpClientManager.ResultCallback<ProjectAccessoryCheckBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(ProjectAccessoryCheckBean response) {
                progressHUD.dismiss();
                checkBean = response;
                if (response != null) {

                    projectName.setText(checkBean.getProjectName());
                    projectType.setText(checkBean.getTypeMsg());
                    person.setText(checkBean.getCrtUserName());
                    time.setText(checkBean.getTime());
                    url.setText(checkBean.getFileUrl());

                    if (checkBean.getFlos() != null && checkBean.getFlos().size() > 0) {
                        middleParentLayout.removeAllViews();
                        for (int i = 0; i < checkBean.getFlos().size(); i++) {

                            if (i == checkBean.getFlos().size() - 1) {
                                middleParentLayout.addView(getItemMiddle(checkBean.getFlos().get(i), true));
                            } else {
                                middleParentLayout.addView(getItemMiddle(checkBean.getFlos().get(i), false));
                            }
                        }

                        ideaNum.setText("相关审批意见(" + checkBean.getFlos().size() + ")");

                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, ProjectAccessoryCheckBean.class, ProjectFileDetailsActivity.class);

    }

    //审核 status 审核类型 1：同意 2：退回
    private void checkContract(String status, String opinion) {

        progressHUD = ProgressHUD.show(ProjectFileDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setId(id);
        parameter.setStatus(status);
        parameter.setNature(checkBean.getType());
        if (!TextUtils.isEmpty(opinion)) {
            parameter.setOpinion(opinion);
        }

        OkHttpClientManager.postAsyn(Config.CHECK_CONTRACT, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(SuccessResponse response) {
                progressHUD.dismiss();
                if (response != null) {
                    showShortToast(response.getMsg());
                    if (response.getCode() == 1) {
                        setResult(Constant.CODE_RESULT, new Intent()
                                .putExtra("position", position));
                        ProjectFileDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, ProjectFileDetailsActivity.class);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                ProjectFileDetailsActivity.this.finish();
                break;
            case R.id.activity_project_details__middle_open_layout://相关审批意见  展开 及  关闭
                if (middleParentLayout.getVisibility() == View.GONE) {
                    openMiddleText.setText("收起");
                    openMiddleImage.setImageResource(R.mipmap.top_arrow);
                    middleParentLayout.setVisibility(View.VISIBLE);
                } else {
                    openMiddleText.setText("展开");
                    openMiddleImage.setImageResource(R.mipmap.bottom_arrow);
                    middleParentLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_project_details__agree://同意
                checkContract("1", mySuggestion.getText().toString().trim());
                break;
            case R.id.activity_project_details__reject://驳回
                checkContract("2", mySuggestion.getText().toString().trim());
                break;
        }
    }
}
