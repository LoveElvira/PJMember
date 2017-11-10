package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.google.zxing.activity.CaptureActivity;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.PicassoLoader;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.selectpic.ImageConfig;
import com.humming.pjmember.viewutils.selectpic.ImageSelector;
import com.humming.pjmember.viewutils.selectpic.ImageSelectorActivity;
import com.pjqs.dto.work.WorkBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 计划作业
 */

public class WorkPlanActivity extends BaseWorkActivity {

    //作业类型图标
    private ImageView typeImage;
    //时间
    private TextView time;
    //作业状态
    private TextView workStatus;
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
    private LinearLayout picParentLayout;
    //相关图片列表
    private LinearLayout picLayout;

    //添加人员
    private ImageView addPerson;
    //添加图片
    private ImageView addPic;

    //开始工作
    private TextView startWorkBtn;

    private ArrayList<String> path = new ArrayList<>();
    private List<Map<String, String>> list = new ArrayList<>();

    //作业状态
    private int status = 0;
    //是否点击了添加图片
    private boolean isAddPic = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        position = getIntent().getIntExtra("position", 0);

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("计划作业");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("交底内容");

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        workStatus = (TextView) findViewById(R.id.item_plan_middle__status);
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
        picParentLayout = (LinearLayout) findViewById(R.id.item_plan_bottom__pic_parent_layout);
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
        initHandler();
        if (workBean != null) {
            initData();
        } else {
            getWorkDetails();
        }
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.CODE_SUCCESS:
                        workBean = (WorkBean) msg.obj;
                        initData();
                        break;
                }
            }
        };
    }

    private void getWorkDetails() {
        progressHUD = ProgressHUD.show(WorkPlanActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getWorkDetails(handler);
    }

    private void initData() {

        if (workBean != null) {
            Glide.with(getBaseContext())
                    .load(workBean.getWorkTypeUrl())
                    .into(typeImage);
            time.setText(workBean.getWorkStartDate());
            workNum.setText(workBean.getWorkCode());
            workName.setText(workBean.getWorkName());
            String str = "";
            if (workBean.getWorkNature() == 1) {
                str = "计划作业";
//                typeImage.setImageResource(R.mipmap.work_tip_plan);
            } else if (workBean.getWorkNature() == 2) {
                str = "缺陷作业";
//                typeImage.setImageResource(R.mipmap.work_tip_defect);
            } else if (workBean.getWorkNature() == 3) {
                str = "抢修作业";
//                typeImage.setImageResource(R.mipmap.work_tip_repair);
            } else if (workBean.getWorkNature() == 4) {
                str = "随修作业";
            }
            typeName.setText(str);
            facilityName.setText(workBean.getFacilityName());
            workTime.setText(workBean.getWorkStartDate() + "~" + workBean.getWorkEndDate());
            content.setText(initHtml("工作内容", workBean.getWorkContent()));
            userName.setText(workBean.getFormanUserName());
            phone.setText(workBean.getFormanPhone());

            startWorkBtn.setVisibility(View.GONE);
            addPerson.setVisibility(View.GONE);
            status = workBean.getRoadWorkState();
            workStatus.setText(workBean.getRoadWork());
            if (workBean.getIsPrincipal() == 1) {//是否是现场负责人 1:是 0:不是
                startWorkBtn.setVisibility(View.VISIBLE);
                if (status == 0) {
                    startWorkBtn.setText("开始工作");
                } else if (status == 2) {
                    addPerson.setVisibility(View.VISIBLE);
                    startWorkBtn.setText("结束工作");
                } else {
                    startWorkBtn.setVisibility(View.GONE);
                }
            }
            picParentLayout.setVisibility(View.GONE);
            addPic.setVisibility(View.GONE);
            if (status == 2) {
                addPic.setVisibility(View.VISIBLE);
                picParentLayout.setVisibility(View.VISIBLE);
            } else if (status != 0) {
                picParentLayout.setVisibility(View.VISIBLE);
            }

        }

    }

    private void endWork() {
        progressHUD = ProgressHUD.show(WorkPlanActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        OkHttpClientManager.postAsyn(Config.WORK_END, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        getWorkDetails();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, BaseWorkActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.CODE_RESULT) {

            if (requestCode == Constant.CODE_REQUEST_ONE) {
                isAddPic = false;
                path.clear();
                list.clear();
                if (data.getBooleanExtra("startOrEndWorkSuccess", false)) {
                    getWorkDetails();
                }
                return;
            }

            List<String> pathList = null;
            if (requestCode == Constant.CODE_REQUEST_THREE) {
                pathList = (List<String>) data.getSerializableExtra("imagePath");
            } else {
                pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            }
            Log.i("ee", "---------" + pathList.size());

            if (pathList == null || pathList.size() == 0) {
                isAddPic = false;
            }

            path.clear();
            path.addAll(pathList);

            list.clear();
            //list.remove(list.size() - 1);//移除掉最后一个
            for (int i = 0; i < pathList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map.put("imagePath", pathList.get(i));
                map.put("isAdd", "0");
                list.add(map);
            }
            if (list.size() < 6) {
                addData();//添加最后一个 add
            }
//            adapter.notifyDataSetChanged();

            Intent intent = new Intent(WorkPlanActivity.this, StartOrEndWorkActivity.class);
            intent.putExtra("photoLists", (Serializable) list);
            intent.putExtra("path", path);
            intent.putExtra("id", id);
            if (status == 0) {
                intent.putExtra("flag", "start");
            } else if (status == 2 && isAddPic) {
                intent.putExtra("flag", "bind");
                isAddPic = false;
            } else if (status == 2 && !isAddPic) {
                intent.putExtra("flag", "end");
            }
            startActivityForResult(intent, Constant.CODE_REQUEST_ONE);

        }

        if (resultCode != RESULT_OK) {
            return;
        }

        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case Constant.CODE_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        path.clear();
                        list.clear();
                        path.add(0, mPublishPhotoPath);
//                        list.remove(list.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        list.add(0, map);
                        if (list.size() < 6) {
                            addData();//添加最后一个 add
                        }
//                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                        isAddPic = false;
                    }
                    break;
            }
        } else {//高于4.4
            switch (requestCode) {
                case Constant.CODE_REQUEST_ONE:
                    if (resultCode == RESULT_OK) {
                        path.clear();
                        list.clear();
                        path.add(0, mPublishPhotoPath);
//                        list.remove(list.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        list.add(0, map);
                        if (list.size() < 6) {
                            addData();//添加最后一个 add
                        }
//                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                        isAddPic = false;
                    }
                    break;
            }
        }

        Intent intent = new Intent(WorkPlanActivity.this, StartOrEndWorkActivity.class);
        intent.putExtra("photoLists", (Serializable) list);
        intent.putExtra("path", path);
        intent.putExtra("id", id);

        if (status == 0) {
            intent.putExtra("flag", "start");
        } else if (status == 2 && isAddPic) {
            intent.putExtra("flag", "bind");
            isAddPic = false;
        } else if (status == 2 && !isAddPic) {
            intent.putExtra("flag", "end");
        }

        startActivityForResult(intent, Constant.CODE_REQUEST_ONE);

    }

    private void addData() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("roadWorkState", workBean.getRoadWorkState())
                        .putExtra("roadWork", workBean.getRoadWork())
                        .putExtra("isSafety", workBean.getIsSafety())
                        .putExtra("position", position));
                WorkPlanActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text://安全交底内容
                startActivity(new Intent(WorkPlanActivity.this, WorkSafetyDisclosureActivity.class)
                        .putExtra("isLook", false)
                        .putExtra("id", id)
                        .putExtra("position", position));
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
                isAddPic = true;
                selectPhotoPopupWindow.initView(selectPhotoLayout);
                selectPhotoPopupWindow.showPopupWindow();
                selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(this);
                selectPhotoPopupWindow.getTakePhoto().setOnClickListener(this);
                break;
            case R.id.item_plan_bottom__pic_layout://查看相关图片
                startActivity(new Intent(WorkPlanActivity.this, WorkImageActivity.class)
                        .putExtra("id", id));
                break;
            case R.id.activity_plan_details__btn://开始工作
                if ("结束工作".equals(startWorkBtn.getText().toString().trim())) {
                    endWork();
                } else {
                    selectPhotoPopupWindow.initView(selectPhotoLayout);
                    selectPhotoPopupWindow.showPopupWindow();
                    selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(this);
                    selectPhotoPopupWindow.getTakePhoto().setOnClickListener(this);
                }
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(WorkPlanActivity.this, new PicassoLoader())
                        .steepToolBarColor(ContextCompat.getColor(getBaseContext(), R.color.black))
                        .titleBgColor(ContextCompat.getColor(getBaseContext(), R.color.black))
                        .titleSubmitTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                        .titleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                        .mutiSelect()
                        .mutiSelectMaxSize(6)
                        .pathList(path)
                        .filePath("/ImageSelector/Pictures")
//                        .showCamera()//显示拍摄按钮
                        .build();
                ImageSelector.open(imageConfig);
                selectPhotoPopupWindow.gonePopupWindow();
                break;
        }
    }
}
