package com.humming.pjmember.activity.affair.more;

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
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.contract.FloNodeExeRecordBean;
import com.pjqs.dto.flow.DisFileBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/26.
 */

public class OutDispatchDetailsActivity extends BaseActivity {

    //展开项目详情
    private LinearLayout openDetailsLayout;
    //项目详情条目
    private LinearLayout detailsParentLayout;
    //展开项目详情 文字
    private TextView openDetailsText;
    //展开项目详情 图片
    private ImageView openDetailsImage;

    //头部展示内容
    private LinearLayout topLayout;

    //发文编号
    private TextView code;
    //发文文号
    private TextView num;
    //发文主题
    private TextView outTitle;
    //签发
    private TextView signer;
    //密级
    private TextView confidential;
    //核稿
    private TextView nuclearDraft;
    //主送
    private TextView mainSending;
    //抄送
    private TextView copySending;
    //内发
    private TextView innerDeliver;
    //会签
    private TextView countersignOrg;
    //主题词
    private TextView subjectTerm;
    //拟办部门
    private TextView imitateOrgName;
    //拟办人
    private TextView imitateUserName;
    //负责人
    private TextView principalUserName;
    //打字
    private TextView typeWriting;
    //校对
    private TextView proofread;
    //监印
    private TextView inspectSeal;
    //封发日期
    private TextView time;
    //内容
    private TextView content;

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

    private LinearLayout visibleLayout;
    private int position;
    private DisFileBean projectDetailBean;

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
        title.setText("发文详情");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = findViewById(R.id.activity_project_details__top_open_layout);
        detailsParentLayout = findViewById(R.id.activity_project_details__top_parent);
        openDetailsText = findViewById(R.id.activity_project_details__top_open_text);
        openDetailsImage = findViewById(R.id.activity_project_details__top_open_image);

        topLayout = findViewById(R.id.item_dispatch_out_top__parent);
        topLayout.setVisibility(View.VISIBLE);

        code = findViewById(R.id.item_dispatch_out_top__code);
        num = findViewById(R.id.item_dispatch_out_top__num);
        outTitle = findViewById(R.id.item_dispatch_out_top__title);
        signer = findViewById(R.id.item_dispatch_out_top__signer);
        confidential = findViewById(R.id.item_dispatch_out_top__confidential);
        nuclearDraft = findViewById(R.id.item_dispatch_out_top__nuclear_draft);
        mainSending = findViewById(R.id.item_dispatch_out_top__main_send);
        copySending = findViewById(R.id.item_dispatch_out_top__copy_send);
        innerDeliver = findViewById(R.id.item_dispatch_out_top__inner_deliver);
        countersignOrg = findViewById(R.id.item_dispatch_out_top__counter_sign);
        subjectTerm = findViewById(R.id.item_dispatch_out_top__subject_term);
        imitateOrgName = findViewById(R.id.item_dispatch_out_top__imitate_org_name);
        imitateUserName = findViewById(R.id.item_dispatch_out_top__imitate_user_name);
        principalUserName = findViewById(R.id.item_dispatch_out_top__principal_name);
        typeWriting = findViewById(R.id.item_dispatch_out_top__typewriting);
        proofread = findViewById(R.id.item_dispatch_out_top__proofread);
        inspectSeal = findViewById(R.id.item_dispatch_out_top__inspect_seal);
        time = findViewById(R.id.item_dispatch_out_top__time);
        content = findViewById(R.id.item_dispatch_out_top__content);

        openMiddleLayout = findViewById(R.id.activity_project_details__middle_open_layout);
        middleParentLayout = findViewById(R.id.activity_project_details__middle_parent);
        openMiddleText = findViewById(R.id.activity_project_details__middle_open_text);
        openMiddleImage = findViewById(R.id.activity_project_details__middle_open_image);
        ideaNum = findViewById(R.id.activity_project_details__middle_num);

        mySuggestion = findViewById(R.id.activity_project_details__suggestion);

        agreeBtn = findViewById(R.id.activity_project_details__agree);
        rejectBtn = findViewById(R.id.activity_project_details__reject);

        visibleLayout = findViewById(R.id.item_dispatch_out__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        middleParentLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        getOutDispatchDetails();
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

    private void getOutDispatchDetails() {

        progressHUD = ProgressHUD.show(OutDispatchDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setDisFileId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_OUT_DISPATCH_DETAILS, new OkHttpClientManager.ResultCallback<DisFileBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(DisFileBean response) {
                progressHUD.dismiss();
                projectDetailBean = response;
                if (response != null) {

                    code.setText(projectDetailBean.getNumberCode().toString());
                    num.setText(projectDetailBean.getPrefix() + projectDetailBean.getTypenumber());
                    outTitle.setText(projectDetailBean.getTitle());
                    signer.setText(projectDetailBean.getSigner());
                    confidential.setText(projectDetailBean.getConfidential());
                    nuclearDraft.setText(projectDetailBean.getNuclearDraft());
                    mainSending.setText(projectDetailBean.getMainSending());
                    copySending.setText(projectDetailBean.getCopySending());
                    innerDeliver.setText(projectDetailBean.getInnerDeliver());
                    countersignOrg.setText(projectDetailBean.getCountersignOrgId());
                    subjectTerm.setText(projectDetailBean.getSubjectTerm());
                    imitateOrgName.setText(projectDetailBean.getImitateOrgName());
                    imitateUserName.setText(projectDetailBean.getImitateUserName());
                    principalUserName.setText(projectDetailBean.getPrincipalUserName());
                    typeWriting.setText(projectDetailBean.getTypewriting());
                    proofread.setText(projectDetailBean.getProofread());
                    inspectSeal.setText(projectDetailBean.getInspectSeal());
                    time.setText(projectDetailBean.getDispatchDate());
                    content.setText(initHtml("发文内容", projectDetailBean.getConfidential()));

                    if (projectDetailBean.getFlos() != null && projectDetailBean.getFlos().size() > 0) {
                        middleParentLayout.removeAllViews();
                        for (int i = 0; i < projectDetailBean.getFlos().size(); i++) {

                            if (i == projectDetailBean.getFlos().size() - 1) {
                                middleParentLayout.addView(getItemMiddle(projectDetailBean.getFlos().get(i), true));
                            } else {
                                middleParentLayout.addView(getItemMiddle(projectDetailBean.getFlos().get(i), false));
                            }
                        }
                        ideaNum.setText("相关审批意见(" + projectDetailBean.getFlos().size() + ")");
                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, DisFileBean.class, OutDispatchDetailsActivity.class);

    }


    //审核 status 审核类型 1：同意 2：退回
    private void checkContract(String status, String opinion) {

        progressHUD = ProgressHUD.show(OutDispatchDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setId(id);
        parameter.setStatus(status);
        parameter.setNature("14");
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
                        OutDispatchDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, OutDispatchDetailsActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                OutDispatchDetailsActivity.this.finish();
                break;
            case R.id.activity_project_details__top_open_layout://合同详情  展开 及  关闭
                if (visibleLayout.getVisibility() == View.GONE) {
                    openDetailsText.setText("收起");
                    openDetailsImage.setImageResource(R.mipmap.top_arrow);
                    visibleLayout.setVisibility(View.VISIBLE);
                } else {
                    openDetailsText.setText("展开");
                    openDetailsImage.setImageResource(R.mipmap.bottom_arrow);
                    visibleLayout.setVisibility(View.GONE);
                }
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
