package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.google.zxing.activity.CaptureActivity;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.work.WorkBean;

/**
 * Created by Elvira on 2017/9/4.
 * 计划作业
 */

public class WorkPlanActivity extends BaseWorkActivity {

    //作业类型图标
    private ImageView typeImage;
    //时间
    private TextView time;
    //作业编号
    private TextView workNum;
    //作业名称
    private TextView workName;
    //作业类型名称
    private TextView typeName;
    //设施名称
    private TextView facilityName;
    //作业时间 开始时间 - 结束时间
    private TextView workTime;
    //工作内容
    private TextView content;
    //负责人
    private TextView userName;
    //负责人电话
    private TextView phone;

    //人员列表
    private LinearLayout personLayout;
    //设备列表
    private LinearLayout equipmentLayout;
    //材料列表
    private LinearLayout materialLayout;
    //相关图片列表
    private LinearLayout picLayout;

    //添加人员
    private ImageView addPerson;
    //添加图片
    private ImageView addPic;

    //开始工作
    private TextView startWorkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("计划作业");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("交底内容");

        typeImage = (ImageView) findViewById(R.id.item_plan_middle__tip);
        time = (TextView) findViewById(R.id.item_plan_middle__time);
        workNum = (TextView) findViewById(R.id.item_plan_middle__work_num);
        workName = (TextView) findViewById(R.id.item_plan_middle__work_name);
        typeName = (TextView) findViewById(R.id.item_plan_middle__type);
        facilityName = (TextView) findViewById(R.id.item_plan_middle__facility_name);
        workTime = (TextView) findViewById(R.id.item_plan_middle__work_time);
        content = (TextView) findViewById(R.id.item_plan_middle__content);
        userName = (TextView) findViewById(R.id.item_plan_middle__user_name);
        phone = (TextView) findViewById(R.id.item_plan_middle__phone);

        addPerson = (ImageView) findViewById(R.id.item_plan_bottom__add_person);
        addPic = (ImageView) findViewById(R.id.item_plan_bottom__add_pic);
        personLayout = (LinearLayout) findViewById(R.id.item_plan_bottom__person_layout);
        picLayout = (LinearLayout) findViewById(R.id.item_plan_bottom__pic_layout);
        equipmentLayout = (LinearLayout) findViewById(R.id.item_plan_bottom__equipment_layout);
        materialLayout = (LinearLayout) findViewById(R.id.item_plan_bottom__material_layout);

        startWorkBtn = (TextView) findViewById(R.id.activity_plan_details__btn);

        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);
        addPerson.setOnClickListener(this);
        addPic.setOnClickListener(this);
        personLayout.setOnClickListener(this);
        picLayout.setOnClickListener(this);
        equipmentLayout.setOnClickListener(this);
        materialLayout.setOnClickListener(this);
        startWorkBtn.setOnClickListener(this);

        getWorkDetails();
    }

    private void getWorkDetails() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.CODE_SUCCESS:
                        workBean = (WorkBean) msg.obj;
                        initDate();
                        break;
                }
            }
        };
        progressHUD = ProgressHUD.show(WorkPlanActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getWorkDetails(handler);
    }

    private void initDate() {

        if (workBean != null) {

//            Glide.with(getBaseContext())
//                    .load(workBean.getWorkTypeUrl())
//                    .into(typeImage);

            time.setText(workBean.getWorkStartDate());
            workNum.setText(workBean.getWorkCode());
            workName.setText(workBean.getWorkName());
            String str = "";
            if (workBean.getWorkNature() == 1) {
                str = "计划作业";
                typeImage.setImageResource(R.mipmap.work_tip_plan);
            } else if (workBean.getWorkNature() == 2) {
                str = "缺陷作业";
                typeImage.setImageResource(R.mipmap.work_tip_defect);
            } else if (workBean.getWorkNature() == 3) {
                str = "抢修作业";
                typeImage.setImageResource(R.mipmap.work_tip_repair);
            }
            typeName.setText(str);
            facilityName.setText(workBean.getFacilityName());
            workTime.setText(workBean.getWorkStartDate() + "~" + workBean.getWorkEndDate());
            content.setText(initHtml("工作内容", workBean.getWorkContent()));
            userName.setText(workBean.getFormanUserName());
            phone.setText(workBean.getFormanPhone());

            if (workBean.getIsPrincipal() == 1) {//是否是现场负责人 1:是 0:不是
                addPerson.setVisibility(View.VISIBLE);
                startWorkBtn.setVisibility(View.VISIBLE);
            }

        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                WorkPlanActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text://安全交底内容
                startActivity(new Intent(WorkPlanActivity.this, WorkSafetyDisclosureActivity.class)
                        .putExtra("isLook", false)
                        .putExtra("id", id));
                break;
            case R.id.item_plan_bottom__add_person://添加人员信息
                startActivity(new Intent(WorkPlanActivity.this, CaptureActivity.class)
                        .putExtra("addPerson", true)
                        .putExtra("workId", id));
                break;
            case R.id.item_plan_bottom__person_layout://获取人员列表
                startActivity(new Intent(WorkPlanActivity.this, PersonActivity.class)
                        .putExtra("id", id));
                break;
            case R.id.item_plan_bottom__equipment_layout://获取设备列表
                startActivity(new Intent(WorkPlanActivity.this, EquipmentActivity.class)
                        .putExtra("id", id));
                break;
            case R.id.item_plan_bottom__material_layout://获取材料列表
                startActivity(new Intent(WorkPlanActivity.this, MaterialActivity.class)
                        .putExtra("id", id));
                break;
            case R.id.item_plan_bottom__add_pic://添加图片
                break;
            case R.id.item_plan_bottom__pic_layout://查看相关图片
                break;
            case R.id.activity_plan_details__btn://开始工作
                break;
        }
    }
}
