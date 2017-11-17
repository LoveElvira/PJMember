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
import com.humming.pjmember.content.affair.ContractIncomeContent;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.StringUtils;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.contract.ContractDetailBean;
import com.pjqs.dto.contract.ContractInfoRes;
import com.pjqs.dto.contract.FloNodeExeRecordBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/7.
 * 合同详情
 */

public class ContractDetailsActivity extends BaseActivity {

    //展开合同详情
    private LinearLayout openDetailsLayout;
    //合同详情条目
    private LinearLayout detailsParentLayout;
    //展开合同详情 文字
    private TextView openDetailsText;
    //展开合同详情 图片
    private ImageView openDetailsImage;

    //合同编号
    private TextView contractNum;
    //合同名称
    private TextView contractName;
    //合同类型
    private TextView contractType;
    //详情类型
    private TextView contractDetailType;
    //开工日期
    private TextView startTime;
    //预计完成时间
    private TextView planCompleteTime;
    //预算费用
    private TextView budgetPrice;
    //计划费用
    private TextView planPrice;
    //项目名称
    private TextView projectName;
    //申报部门
    private TextView department;
    //申报日期
    private TextView declareTime;
    //是否有效
    private TextView isValid;
    //供应商名字
    private TextView supplierName;
    //财务归档标志
    private TextView mark;
    //合同销项时间
    private TextView outputTime;
    //对应收入合同名称
    private TextView contractIncomeName;
    //是否预算内
    private TextView isInbudget;
    //单位负责人
    private TextView companyPerson;
    //是否是项目合同
    private TextView isContractProject;
    //合同决算价格
    private TextView finalPrice;
    //已实际支付金额
    private TextView actualAmount;
    //承办人
    private TextView undertaker;


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

    private ContractDetailBean contractDetailBean;

    private String conNature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");
        conNature = getIntent().getStringExtra("conNature");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("合同详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = (LinearLayout) findViewById(R.id.activity_contract_details__top_open_layout);
        detailsParentLayout = (LinearLayout) findViewById(R.id.activity_contract_details__top_parent);
        openDetailsText = (TextView) findViewById(R.id.activity_contract_details__top_open_text);
        openDetailsImage = (ImageView) findViewById(R.id.activity_contract_details__top_open_image);

        contractNum = (TextView) findViewById(R.id.item_contract_top__num);
        contractName = (TextView) findViewById(R.id.item_contract_top__name);
        contractType = (TextView) findViewById(R.id.item_contract_top__type);
        contractDetailType = (TextView) findViewById(R.id.item_contract_top__detail_type);
        startTime = (TextView) findViewById(R.id.item_contract_top__start_time);
        planCompleteTime = (TextView) findViewById(R.id.item_contract_top__plan_complete_time);
        budgetPrice = (TextView) findViewById(R.id.item_contract_top__budget_price);
        planPrice = (TextView) findViewById(R.id.item_contract_top__plan_price);
        projectName = (TextView) findViewById(R.id.item_contract_top__project_name);
        department = (TextView) findViewById(R.id.item_contract_top__department);
        declareTime = (TextView) findViewById(R.id.item_contract_top__declare_time);
        isValid = (TextView) findViewById(R.id.item_contract_top__is_valid);
        supplierName = (TextView) findViewById(R.id.item_contract_top__supplier_name);
        mark = (TextView) findViewById(R.id.item_contract_top__mark);
        outputTime = (TextView) findViewById(R.id.item_contract_top__output_time);
        contractIncomeName = (TextView) findViewById(R.id.item_contract_top__income_name);
        isInbudget = (TextView) findViewById(R.id.item_contract_top__is_in_budget);
        companyPerson = (TextView) findViewById(R.id.item_contract_top__company_person);
        isContractProject = (TextView) findViewById(R.id.item_contract_top__is_project);
        finalPrice = (TextView) findViewById(R.id.item_contract_top__final_price);
        actualAmount = (TextView) findViewById(R.id.item_contract_top__actual_amount);
        undertaker = (TextView) findViewById(R.id.item_contract_top__undertaker);

        openMiddleLayout = (LinearLayout) findViewById(R.id.activity_contract_details__middle_open_layout);
        middleParentLayout = (LinearLayout) findViewById(R.id.activity_contract_details__middle_parent);
        openMiddleText = (TextView) findViewById(R.id.activity_contract_details__middle_open_text);
        openMiddleImage = (ImageView) findViewById(R.id.activity_contract_details__middle_open_image);
        ideaNum = (TextView) findViewById(R.id.activity_contract_details__middle_num);

        mySuggestion = (EditText) findViewById(R.id.activity_contract_details__suggestion);

        agreeBtn = (TextView) findViewById(R.id.activity_contract_details__agree);
        rejectBtn = (TextView) findViewById(R.id.activity_contract_details__reject);

        visibleLayout = (LinearLayout) findViewById(R.id.item_contract__top_visible_layout);
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

        progressHUD = ProgressHUD.show(ContractDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setContractId(id);
        parameter.setConNature(conNature);

        OkHttpClientManager.postAsyn(Config.GET_CONTRACT_DETAILS, new OkHttpClientManager.ResultCallback<ContractDetailBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(ContractDetailBean response) {
                progressHUD.dismiss();
                contractDetailBean = response;
                if (response != null) {

                    contractNum.setText(contractDetailBean.getConNo());
                    contractName.setText(contractDetailBean.getConName());
                    contractType.setText(contractDetailBean.getConType());
                    contractDetailType.setText(contractDetailBean.getDetailType());
                    startTime.setText(contractDetailBean.getStarDate());
                    planCompleteTime.setText(contractDetailBean.getScheFinishDatel());
                    budgetPrice.setText(StringUtils.saveTwoDecimal(contractDetailBean.getBudgetCost()));
                    planPrice.setText(StringUtils.saveTwoDecimal(contractDetailBean.getPlanCost()));
                    projectName.setText(contractDetailBean.getProName());
                    department.setText(contractDetailBean.getDecDepartment());
                    declareTime.setText(contractDetailBean.getDeclareDate());
                    isValid.setText(contractDetailBean.getIsValid());
                    supplierName.setText(contractDetailBean.getSuppliersName());
                    mark.setText(contractDetailBean.getArchiveFlg());
                    outputTime.setText(contractDetailBean.getConOutputTime());
                    contractIncomeName.setText(contractDetailBean.getIncomeConName());
                    isInbudget.setText(contractDetailBean.getIsWithinBudget());
                    companyPerson.setText(contractDetailBean.getDepartmentCharge());
                    isContractProject.setText(contractDetailBean.getIsProContract());
                    finalPrice.setText(StringUtils.saveTwoDecimal(contractDetailBean.getConFinalPrice()));
                    actualAmount.setText(StringUtils.saveTwoDecimal(contractDetailBean.getPaidAmount()));
                    undertaker.setText(contractDetailBean.getAgent());

                    if (contractDetailBean.getFlos() != null && contractDetailBean.getFlos().size() > 0) {
                        middleParentLayout.removeAllViews();
                        for (int i = 0; i < contractDetailBean.getFlos().size(); i++) {

                            if (i == contractDetailBean.getFlos().size() - 1) {
                                middleParentLayout.addView(getItemMiddle(contractDetailBean.getFlos().get(i), true));
                            } else {
                                middleParentLayout.addView(getItemMiddle(contractDetailBean.getFlos().get(i), false));
                            }
                        }

                        ideaNum.setText("相关审批意见(" + contractDetailBean.getFlos().size() + ")");

                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, ContractDetailBean.class, Application.getInstance().getCurrentActivity().getClass());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                ContractDetailsActivity.this.finish();
                break;
            case R.id.activity_contract_details__top_open_layout://合同详情  展开 及  关闭
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
            case R.id.activity_contract_details__middle_open_layout://相关审批意见  展开 及  关闭
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
            case R.id.activity_contract_details__agree://同意
                ContractDetailsActivity.this.finish();
                break;
            case R.id.activity_contract_details__reject://驳回
                ContractDetailsActivity.this.finish();
                break;
        }
    }
}
