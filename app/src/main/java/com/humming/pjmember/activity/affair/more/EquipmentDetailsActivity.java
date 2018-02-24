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
import com.pjqs.dto.flow.EquipmentApplyBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2018/2/23.
 * 设备详情
 */

public class EquipmentDetailsActivity extends BaseActivity {

    //展开项目详情
    private LinearLayout openDetailsLayout;
    //项目详情条目
    private LinearLayout detailsParentLayout;
    //展开项目详情 文字
    private TextView openDetailsText;
    //展开项目详情 图片
    private ImageView openDetailsImage;

    //申请编号
    private TextView applyNo;
    //申请人
    private TextView applyUser;
    //申请部门
    private TextView applyDepartment;
    //计划编号
    private TextView planNo;
    //申报部门
    private TextView department;
    //设备名称
    private TextView equipmentName;
    //规格型号
    private TextView equipmentSpecial;
    //单价
    private TextView price;
    //总数量
    private TextView count;
    //合计
    private TextView totalPrice;
    //购置类别
    private TextView type;
    //购置原因
    private TextView reason;
    //采购方式
    private TextView way;


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
    private EquipmentApplyBean projectDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");
        position = getIntent().getIntExtra("position", -1);

        title = findViewById(R.id.base_toolbar__title);
        title.setText("设备详情");
        leftArrow = findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        openDetailsLayout = findViewById(R.id.activity_project_details__top_open_layout);
        detailsParentLayout = findViewById(R.id.activity_project_details__top_parent);
        openDetailsText = findViewById(R.id.activity_project_details__top_open_text);
        openDetailsImage = findViewById(R.id.activity_project_details__top_open_image);

        applyNo = findViewById(R.id.item_equipment_top__apply_num);
        applyUser = findViewById(R.id.item_equipment_top__apply_user);
        applyDepartment = findViewById(R.id.item_equipment_top__apply_department);
        planNo = findViewById(R.id.item_equipment_top__plan_num);
        department = findViewById(R.id.item_equipment_top__department);
        equipmentName = findViewById(R.id.item_equipment_top__name);
        equipmentSpecial = findViewById(R.id.item_equipment_top__special);
        price = findViewById(R.id.item_equipment_top__price);
        count = findViewById(R.id.item_equipment_top__count);
        totalPrice = findViewById(R.id.item_equipment_top__total_price);
        type = findViewById(R.id.item_equipment_top__type);
        reason = findViewById(R.id.item_equipment_top__reason);
        way = findViewById(R.id.item_equipment_top__way);

        openMiddleLayout = findViewById(R.id.activity_project_details__middle_open_layout);
        middleParentLayout = findViewById(R.id.activity_project_details__middle_parent);
        openMiddleText = findViewById(R.id.activity_project_details__middle_open_text);
        openMiddleImage = findViewById(R.id.activity_project_details__middle_open_image);
        ideaNum = findViewById(R.id.activity_project_details__middle_num);

        mySuggestion = findViewById(R.id.activity_project_details__suggestion);

        agreeBtn = findViewById(R.id.activity_project_details__agree);
        rejectBtn = findViewById(R.id.activity_project_details__reject);

        visibleLayout = findViewById(R.id.item_equipment__top_visible_layout);
        visibleLayout.setVisibility(View.GONE);

        middleParentLayout.setVisibility(View.GONE);

        leftArrow.setOnClickListener(this);
        openDetailsLayout.setOnClickListener(this);
        openMiddleLayout.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);

        getEquipmentDetail();
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

    private void getEquipmentDetail() {

        progressHUD = ProgressHUD.show(EquipmentDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setProjectId(id);

        OkHttpClientManager.postAsyn(Config.GET_PROJECT_EQUIPMENT_DETAILS, new OkHttpClientManager.ResultCallback<EquipmentApplyBean>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(EquipmentApplyBean response) {
                progressHUD.dismiss();
                projectDetailBean = response;
                if (response != null) {

                    applyNo.setText(projectDetailBean.getApplyNo());
                    applyUser.setText(projectDetailBean.getApplyUser());
                    applyDepartment.setText(projectDetailBean.getApplyDepartment());
                    planNo.setText(projectDetailBean.getPlanNo());
                    department.setText(projectDetailBean.getReportDepartment());
                    equipmentName.setText(projectDetailBean.getEquipmentName());
                    equipmentSpecial.setText(StringUtils.saveTwoDecimal(projectDetailBean.getSpecification()));
                    price.setText(StringUtils.saveTwoDecimal(projectDetailBean.getPrice()));
                    count.setText(projectDetailBean.getCount().toString());
                    totalPrice.setText(StringUtils.saveTwoDecimal(projectDetailBean.getTotal()));
                    type.setText(projectDetailBean.getSourcingType());
                    reason.setText(initHtml("购置原因", projectDetailBean.getSourcingReason()));
                    way.setText(projectDetailBean.getSourcingWay());

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
        }, parameter, EquipmentApplyBean.class, EquipmentDetailsActivity.class);

    }


    //审核 status 审核类型 1：同意 2：退回
    private void checkContract(String status, String opinion) {

        progressHUD = ProgressHUD.show(EquipmentDetailsActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setId(id);
        parameter.setStatus(status);
        parameter.setNature("10");
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
                        EquipmentDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, EquipmentDetailsActivity.class);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                EquipmentDetailsActivity.this.finish();
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
