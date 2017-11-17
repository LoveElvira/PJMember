package com.humming.pjmember.activity.affair;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.contract.FloNodeExeRecordBean;
import com.pjqs.dto.project.OutProjectDetailBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/11/17.
 * 项目详情
 */

public class ProjectDetailsActivity extends BaseActivity {

    //展开项目详情
    private LinearLayout openDetailsLayout;
    //项目详情条目
    private LinearLayout detailsParentLayout;
    //展开项目详情 文字
    private TextView openDetailsText;
    //展开项目详情 图片
    private ImageView openDetailsImage;

    //项目编号
    private TextView projectNum;
    //项目名称
    private TextView projectName;
    //工程性质
    private TextView projectType;
    //项目单位
    private TextView projectDepartment;
    //开工日期
    private TextView startTime;
    //预计完成时间
    private TextView planCompleteTime;
    //预算费用
    private TextView budgetPrice;
    //计划费用
    private TextView planPrice;
    //统筹名称
    private TextView overallPlanningName;
    //是否统筹
    private TextView isOverallPlanning;
    //施工单位
    private TextView workDepartment;
    //设施名称
    private TextView facilityName;
    //项目的可行性
    private TextView viability;
    //计划人工
    private TextView personNum;
    //工作内容
    private TextView content;
    //主要用材
    private TextView material;
    //经费概预算
    private TextView probablyBudget;
    //是否是历史项目
    private TextView isHistory;
    //状态
    private TextView status;
    //立项的必要性质
    private TextView necessity;

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

    private OutProjectDetailBean projectDetailBean;

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

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("项目详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = (LinearLayout) findViewById(R.id.activity_project_details__top_open_layout);
        detailsParentLayout = (LinearLayout) findViewById(R.id.activity_project_details__top_parent);
        openDetailsText = (TextView) findViewById(R.id.activity_project_details__top_open_text);
        openDetailsImage = (ImageView) findViewById(R.id.activity_project_details__top_open_image);

        projectNum = (TextView) findViewById(R.id.item_project_top__num);
        projectName = (TextView) findViewById(R.id.item_project_top__name);
        projectType = (TextView) findViewById(R.id.item_project_top__type);
        projectDepartment = (TextView) findViewById(R.id.item_project_top__department);
        startTime = (TextView) findViewById(R.id.item_project_top__start_time);
        planCompleteTime = (TextView) findViewById(R.id.item_project_top__plan_complete_time);
        budgetPrice = (TextView) findViewById(R.id.item_project_top__budget_price);
        planPrice = (TextView) findViewById(R.id.item_project_top__plan_price);
        overallPlanningName = (TextView) findViewById(R.id.item_project_top__overall_planning_name);
        isOverallPlanning = (TextView) findViewById(R.id.item_project_top__is_overall_planning);
        workDepartment = (TextView) findViewById(R.id.item_project_top__work_department);
        facilityName = (TextView) findViewById(R.id.item_project_top__facility_name);
        viability = (TextView) findViewById(R.id.item_project_top__viability);
        personNum = (TextView) findViewById(R.id.item_project_top__person_num);
        content = (TextView) findViewById(R.id.item_project_top__content);
        material = (TextView) findViewById(R.id.item_project_top__material);
        probablyBudget = (TextView) findViewById(R.id.item_project_top__probably_budget);
        isHistory = (TextView) findViewById(R.id.item_project_top__is_history);
        status = (TextView) findViewById(R.id.item_project_top__status);
        necessity = (TextView) findViewById(R.id.item_project_top__necessity);

        openMiddleLayout = (LinearLayout) findViewById(R.id.activity_project_details__middle_open_layout);
        middleParentLayout = (LinearLayout) findViewById(R.id.activity_project_details__middle_parent);
        openMiddleText = (TextView) findViewById(R.id.activity_project_details__middle_open_text);
        openMiddleImage = (ImageView) findViewById(R.id.activity_project_details__middle_open_image);
        ideaNum = (TextView) findViewById(R.id.activity_project_details__middle_num);

        mySuggestion = (EditText) findViewById(R.id.activity_project_details__suggestion);

        agreeBtn = (TextView) findViewById(R.id.activity_project_details__agree);
        rejectBtn = (TextView) findViewById(R.id.activity_project_details__reject);

        visibleLayout = (LinearLayout) findViewById(R.id.item_project__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        middleParentLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        getContractDetail();

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


    private void getContractDetail() {

        progressHUD = ProgressHUD.show(ProjectDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setProjectId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_DETAILS, new OkHttpClientManager.ResultCallback<OutProjectDetailBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(OutProjectDetailBean response) {
                progressHUD.dismiss();
                projectDetailBean = response;
                if (response != null) {

                    projectNum.setText(projectDetailBean.getSubproNo());
                    projectName.setText(projectDetailBean.getSubproName());
                    projectType.setText(projectDetailBean.getProType());
                    projectDepartment.setText(projectDetailBean.getSubproDepartment());
                    startTime.setText(projectDetailBean.getPlanStartTime());
                    planCompleteTime.setText(projectDetailBean.getPlanEndTime());
                    budgetPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getCostAmountTotal()));
                    planPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getPlanAmount()));
                    overallPlanningName.setText(projectDetailBean.getArrangerName());
                    isOverallPlanning.setText(projectDetailBean.getIsArranger());
                    workDepartment.setText(projectDetailBean.getExecDepartment());
                    facilityName.setText(projectDetailBean.getFacilityName());
                    viability.setText(projectDetailBean.getViability());
                    personNum.setText(projectDetailBean.getPlanNumber().toString());
                    content.setText(initHtml("工作内容", projectDetailBean.getContent()));
                    material.setText(projectDetailBean.getMaterial());
                    probablyBudget.setText(StringUtils.saveTwoDecimal(projectDetailBean.getBudget()));
                    isHistory.setText(projectDetailBean.getIsOldProject());
                    status.setText(projectDetailBean.getStatus());
                    necessity.setText(projectDetailBean.getNecessityCondition());

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
        }, parameter, OutProjectDetailBean.class, Application.getInstance().getCurrentActivity().getClass());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                ProjectDetailsActivity.this.finish();
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
                ProjectDetailsActivity.this.finish();
                break;
            case R.id.activity_project_details__reject://驳回
                ProjectDetailsActivity.this.finish();
                break;
        }
    }
}
