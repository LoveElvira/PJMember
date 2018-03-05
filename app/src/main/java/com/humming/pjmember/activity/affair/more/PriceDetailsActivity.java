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
import com.pjqs.dto.flow.CostDetailBean;
import com.pjqs.dto.flow.EquipmentApplyBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/23.
 */

public class PriceDetailsActivity extends BaseActivity {

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

    //费用编号
    private TextView num;
    //申报单位
    private TextView department;
    //年份
    private TextView year;
    //月份
    private TextView month;
    //费用类型
    private TextView type;
    //收款单位
    private TextView receiptDepartment;
    //合同名称
    private TextView conName;
    //项目名称
    private TextView projectName;
    //成本类型名称
    private TextView compositionName;
    //合同完成比例
    private TextView finishRatio;
    //上期支付
    private TextView lastPayPrice;
    //上期付款时间
    private TextView lastPayPriceTime;
    //累计支付
    private TextView totalPayPrice;
    //累计支付次数
    private TextView totalPayPriceNum;
    //上期计划
    private TextView lastPlan;
    //上月实际支付
    private TextView lastMonthPayPrice;
    //计划完成比例
    private TextView planFinishRatio;
    //本次申请费用金额
    private TextView applyPrice;
    //备注
    private TextView remark;

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
    private CostDetailBean projectDetailBean;

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
        title.setText("费用详情");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = findViewById(R.id.activity_project_details__top_open_layout);
        detailsParentLayout = findViewById(R.id.activity_project_details__top_parent);
        openDetailsText = findViewById(R.id.activity_project_details__top_open_text);
        openDetailsImage = findViewById(R.id.activity_project_details__top_open_image);

        topLayout = findViewById(R.id.item_price_top__parent);
        topLayout.setVisibility(View.VISIBLE);

        num = findViewById(R.id.item_price_top__num);
        department = findViewById(R.id.item_price_top__department);
        year = findViewById(R.id.item_price_top__year);
        month = findViewById(R.id.item_price_top__month);
        type = findViewById(R.id.item_price_top__type);
        receiptDepartment = findViewById(R.id.item_price_top__receipt_department);
        conName = findViewById(R.id.item_price_top__con_name);
        projectName = findViewById(R.id.item_price_top__project_name);
        compositionName = findViewById(R.id.item_price_top__composition_name);
        finishRatio = findViewById(R.id.item_price_top__finish_ratio);
        lastPayPrice = findViewById(R.id.item_price_top__last_pay);
        lastPayPriceTime = findViewById(R.id.item_price_top__last_pay_time);
        totalPayPrice = findViewById(R.id.item_price_top__total_pay);
        totalPayPriceNum = findViewById(R.id.item_price_top__total_pay_num);
        lastPlan = findViewById(R.id.item_price_top__last_plan);
        lastMonthPayPrice = findViewById(R.id.item_price_top__last_month_pay);
        planFinishRatio = findViewById(R.id.item_price_top__plan_finish_ratio);
        applyPrice = findViewById(R.id.item_price_top__apply_price);
        remark = findViewById(R.id.item_price_top__remark);

        openMiddleLayout = findViewById(R.id.activity_project_details__middle_open_layout);
        middleParentLayout = findViewById(R.id.activity_project_details__middle_parent);
        openMiddleText = findViewById(R.id.activity_project_details__middle_open_text);
        openMiddleImage = findViewById(R.id.activity_project_details__middle_open_image);
        ideaNum = findViewById(R.id.activity_project_details__middle_num);

        mySuggestion = findViewById(R.id.activity_project_details__suggestion);

        agreeBtn = findViewById(R.id.activity_project_details__agree);
        rejectBtn = findViewById(R.id.activity_project_details__reject);

        visibleLayout = findViewById(R.id.item_price__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        middleParentLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        getPriceDetail();
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

    private void getPriceDetail() {

        progressHUD = ProgressHUD.show(PriceDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setCostDetailId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_PRICE_DETAILS, new OkHttpClientManager.ResultCallback<CostDetailBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(CostDetailBean response) {
                progressHUD.dismiss();
                projectDetailBean = response;
                if (response != null) {

                    num.setText(projectDetailBean.getCostNo());
                    department.setText(projectDetailBean.getDecDepartment());
                    year.setText(projectDetailBean.getYear());
                    month.setText(projectDetailBean.getMonth());
                    type.setText(projectDetailBean.getCostType());
                    receiptDepartment.setText(projectDetailBean.getReceiptDepartment());
                    conName.setText(projectDetailBean.getConName());
                    projectName.setText(projectDetailBean.getProjectName());
                    compositionName.setText(projectDetailBean.getCompositionName());
                    finishRatio.setText(projectDetailBean.getCosFinishRatio());
                    lastPayPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getLastMonthPay()));
                    lastPayPriceTime.setText(projectDetailBean.getLastMonthPayTime());
                    totalPayPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getTotalPay()));
                    totalPayPriceNum.setText(projectDetailBean.getTotalPayNumber());
                    lastPlan.setText(projectDetailBean.getPriorPeriodPlay());
                    lastMonthPayPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getLastMonthActualPay()));
                    planFinishRatio.setText(projectDetailBean.getFinishRatioPlay());
                    applyPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getApplyCost()));
                    remark.setText(projectDetailBean.getRemark());

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
        }, parameter, CostDetailBean.class, PriceDetailsActivity.class);

    }


    //审核 status 审核类型 1：同意 2：退回
    private void checkContract(String status, String opinion) {

        progressHUD = ProgressHUD.show(PriceDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setId(id);
        parameter.setStatus(status);
        parameter.setNature("5");
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
                        PriceDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, PriceDetailsActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                PriceDetailsActivity.this.finish();
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
