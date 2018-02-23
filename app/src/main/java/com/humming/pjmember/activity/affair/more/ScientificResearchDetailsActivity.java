package com.humming.pjmember.activity.affair.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.activity.affair.ProjectDetailsActivity;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.project.OutProjectDetailScienceGradeBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/6.
 * 项目评分
 */

public class ScientificResearchDetailsActivity extends BaseActivity {

    //展开项目详情
    private LinearLayout openDetailsLayout;
    //项目详情条目
    private LinearLayout detailsParentLayout;
    //展开项目详情 文字
    private TextView openDetailsText;
    //展开项目详情 图片
    private ImageView openDetailsImage;

    private LinearLayout visibleLayout;

    //项目编号
    private TextView projectNum;
    //项目名称
    private TextView projectName;
    //项目单位
    private TextView projectDepartment;
    //开工日期
    private TextView startTime;
    //预计完成时间
    private TextView planCompleteTime;
    //经费概预算
    private TextView probablyBudget;
    //计划费用
    private TextView planPrice;
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
    //是否是历史项目
    private TextView isHistory;
    //立项的必要性质
    private TextView necessity;
    //备注
    private TextView remark;

    //选题
    private SeekBar selectSeekBar;
    private TextView selectScore;
    //原因分析
    private SeekBar reasonSeekBar;
    private TextView reasonScore;
    //对策与措施
    private SeekBar measureSeekBar;
    private TextView measureScore;
    //效果
    private SeekBar effectSeekBar;
    private TextView effectScore;
    //特点
    private SeekBar characteristicSeekBar;
    private TextView characteristicScore;

    //提交
    private TextView submit;
    private OutProjectDetailScienceGradeBean projectDetailBean;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientific_research_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");
        position = getIntent().getIntExtra("position", -1);

        title = findViewById(R.id.base_toolbar__title);
        title.setText("项目评分");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = findViewById(R.id.activity_project_details__top_open_layout);
        detailsParentLayout = findViewById(R.id.activity_project_details__top_parent);
        openDetailsText = findViewById(R.id.activity_project_details__top_open_text);
        openDetailsImage = findViewById(R.id.activity_project_details__top_open_image);
        visibleLayout = findViewById(R.id.item_project__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        projectNum = findViewById(R.id.item_project_top__num);
        projectName = findViewById(R.id.item_project_top__name);
        projectDepartment = findViewById(R.id.item_project_top__department);
        startTime = findViewById(R.id.item_project_top__start_time);
        planCompleteTime = findViewById(R.id.item_project_top__plan_complete_time);
        probablyBudget = findViewById(R.id.item_project_top__probably_budget);
        planPrice = findViewById(R.id.item_project_top__plan_price);
        workDepartment = findViewById(R.id.item_project_top__work_department);
        facilityName = findViewById(R.id.item_project_top__facility_name);
        viability = findViewById(R.id.item_project_top__viability);
        personNum = findViewById(R.id.item_project_top__person_num);
        content = findViewById(R.id.item_project_top__content);
        material = findViewById(R.id.item_project_top__material);
        isHistory = findViewById(R.id.item_project_top__is_history);
        remark = findViewById(R.id.item_project_top__remark);
        necessity = findViewById(R.id.item_project_top__necessity);

        selectSeekBar = findViewById(R.id.item_comment__select_progress);
        selectScore = findViewById(R.id.item_comment__select_score);
        reasonSeekBar = findViewById(R.id.item_comment__reason_progress);
        reasonScore = findViewById(R.id.item_comment__reason_score);
        measureSeekBar = findViewById(R.id.item_comment__measure_progress);
        measureScore = findViewById(R.id.item_comment__measure_score);
        effectSeekBar = findViewById(R.id.item_comment__effect_progress);
        effectScore = findViewById(R.id.item_comment__effect_score);
        characteristicSeekBar = findViewById(R.id.item_comment__characteristic_progress);
        characteristicScore = findViewById(R.id.item_comment__characteristic_score);

        submit = findViewById(R.id.activity_project_details__submit);

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        submit.setOnClickListener(this);
        selectSeekBar.setOnSeekBarChangeListener(changeListener);
        reasonSeekBar.setOnSeekBarChangeListener(changeListener);
        measureSeekBar.setOnSeekBarChangeListener(changeListener);
        effectSeekBar.setOnSeekBarChangeListener(changeListener);
        characteristicSeekBar.setOnSeekBarChangeListener(changeListener);

        getScientificDetails();
    }

    private void getScientificDetails() {

        progressHUD = ProgressHUD.show(ScientificResearchDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setProjectId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_SCIENCE_DETAILS, new OkHttpClientManager.ResultCallback<OutProjectDetailScienceGradeBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(OutProjectDetailScienceGradeBean response) {
                progressHUD.dismiss();
                projectDetailBean = response;
                if (response != null) {
                    projectNum.setText(projectDetailBean.getProjectCode());
                    projectName.setText(projectDetailBean.getProjectName());
                    projectDepartment.setText(projectDetailBean.getProjectDepartment());
                    startTime.setText(projectDetailBean.getPlanStartTime());
                    planCompleteTime.setText(projectDetailBean.getPlanEndTime());
                    planPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getPlanAmount()));
                    workDepartment.setText(projectDetailBean.getExecDepartment());
                    facilityName.setText(projectDetailBean.getFacilityName());
                    viability.setText(projectDetailBean.getViability());
                    personNum.setText(projectDetailBean.getPlanNumber().toString());
                    content.setText(initHtml("工作内容", projectDetailBean.getContent()));
                    material.setText(projectDetailBean.getMaterial());
                    probablyBudget.setText(StringUtils.saveTwoDecimal(projectDetailBean.getBudget()));
                    isHistory.setText(projectDetailBean.getIsOldProject());
                    remark.setText(initHtml("备注", projectDetailBean.getRemark()));
                    necessity.setText(projectDetailBean.getNecessityCondition());
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, OutProjectDetailScienceGradeBean.class, ScientificResearchDetailsActivity.class);

    }

    //科研打分
    private void submitScientificGrade(String selectTopic, String reasonAnalyze, String countermeasure, String effect, String characteristic) {

        progressHUD = ProgressHUD.show(ScientificResearchDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setScienceGradeId(id);
        parameter.setSelectTopic(selectTopic);
        parameter.setReasonAnalyze(reasonAnalyze);
        parameter.setCountermeasure(countermeasure);
        parameter.setEffect(effect);
        parameter.setCharacteristic(characteristic);
        OkHttpClientManager.postAsyn(Config.SET_PROJECT_SCIENCE_GRADE, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        ScientificResearchDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, ProjectDetailsActivity.class);

    }

    SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            switch (seekBar.getId()) {
                case R.id.item_comment__select_progress:
                    setScore(selectScore, i);
                    break;
                case R.id.item_comment__reason_progress:
                    setScore(reasonScore, i);
                    break;
                case R.id.item_comment__measure_progress:
                    setScore(measureScore, i);
                    break;
                case R.id.item_comment__effect_progress:
                    setScore(effectScore, i);
                    break;
                case R.id.item_comment__characteristic_progress:
                    setScore(characteristicScore, i);
                    break;
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    //更改评分进度
    private void setScore(TextView score, int i) {
        if (i == 0) {
            score.setText("0");
        } else {
            score.setText((i + 11) + "");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                ScientificResearchDetailsActivity.this.finish();
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
            case R.id.activity_project_details__submit://提交评分
                String selectTopic = selectScore.getText().toString();
                String reasonAnalyze = reasonScore.getText().toString();
                String countermeasure = measureScore.getText().toString();
                String effect = effectScore.getText().toString();
                String characteristic = characteristicScore.getText().toString();
                if ("0".equals(selectTopic)) {
                    showShortToast("请对 选题 进行评分");
                    return;
                } else if ("0".equals(reasonAnalyze)) {
                    showShortToast("请对 原因分析 进行评分");
                    return;
                } else if ("0".equals(countermeasure)) {
                    showShortToast("请对 对策与措施 进行评分");
                    return;
                } else if ("0".equals(effect)) {
                    showShortToast("请对 效果 进行评分");
                    return;
                } else if ("0".equals(characteristic)) {
                    showShortToast("请对 特点 进行评分");
                    return;
                }

                //提交
                submitScientificGrade(selectTopic, reasonAnalyze, countermeasure, effect, characteristic);
                break;
        }
    }
}
