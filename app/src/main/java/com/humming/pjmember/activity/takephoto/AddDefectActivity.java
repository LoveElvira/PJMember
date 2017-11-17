package com.humming.pjmember.activity.takephoto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.activity.MapActivity;
import com.humming.pjmember.activity.scan.AddAccidentActivity;
import com.humming.pjmember.adapter.ImageAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.bean.FacilityInfoModel;
import com.humming.pjmember.requestdate.AddDefectParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.GlideLoader;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.humming.pjmember.viewutils.selectpic.ImageConfig;
import com.humming.pjmember.viewutils.selectpic.ImageSelector;
import com.humming.pjmember.viewutils.selectpic.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 添加缺陷
 */

public class AddDefectActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //设施名称
    private LinearLayout nameLayout;
    //名称
    private TextView name;
    //工作名称
    private EditText workName;
    //缺陷描述
    private EditText content;
    //选择位置
    private LinearLayout addressLayout;
    private ImageView addressImage;
    //位置
    private EditText address;
    //详细位置描述
    private EditText addressDes;

    //提交
    private TextView submit;

    //缺陷
    private ImageAdapter defectAdapter;

    //存放图片地址
    private List<Map<String, String>> defectList = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    //设施
    private FacilityInfoModel facilityModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_defect);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("添加缺陷");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        //缺陷图片
        listView = (RecyclerView) findViewById(R.id.activity_add_defect__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        workName = (EditText) findViewById(R.id.activity_add_defect__work_name);
        nameLayout = (LinearLayout) findViewById(R.id.activity_add_defect__name_layout);
        name = (TextView) findViewById(R.id.activity_add_defect__name);
        content = (EditText) findViewById(R.id.activity_add_defect__content);
        addressLayout = (LinearLayout) findViewById(R.id.activity_add_defect__address_layout);
        addressImage = (ImageView) findViewById(R.id.activity_add_defect__address_image);
        address = (EditText) findViewById(R.id.activity_add_defect__address);
        addressDes = (EditText) findViewById(R.id.activity_add_defect__address_des);

        submit = (TextView) findViewById(R.id.activity_add_defect__submit);

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
//        addressLayout.setOnClickListener(this);
        addressImage.setOnClickListener(this);
        nameLayout.setOnClickListener(this);

        listView.addItemDecoration(new SpacesItemDecoration(10));
        defectList = (List<Map<String, String>>) getIntent().getSerializableExtra("photoLists");
        path = (ArrayList<String>) getIntent().getSerializableExtra("path");
        defectAdapter = new ImageAdapter(defectList, this, 2);
        listView.setAdapter(defectAdapter);
        defectAdapter.setOnItemChildClickListener(this);
        defectAdapter.notifyDataSetChanged();

    }

    private void initDate(List<Map<String, String>> list) {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    //添加缺陷
    private void addDefectWork(String workName, String content, String address, List<String> imageList) {
        AddDefectParameter parameter = new AddDefectParameter();
        parameter.setWorkName(workName);
        parameter.setFacilityId(Long.parseLong(facilityModel.getFacilityId()));
        parameter.setLocation(address);
        parameter.setPictureUrls(imageList);
        parameter.setRemark(content);
        OkHttpClientManager.postAsyn(Config.ADD_WORK, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo().toString());
                Log.e("onError", info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(SuccessResponse response) {
                progressHUD.dismiss();
                if (response != null) {
                    showShortToast(response.getMsg());
                    if (response.getCode() == 1) {
                        setResult(Constant.CODE_RESULT, new Intent()
                                .putExtra("addSuccess", true));
                        AddDefectActivity.this.finish();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, AddAccidentActivity.class);

    }

    private boolean isNull(String workName, String name, String content, String address) {
        if (TextUtils.isEmpty(workName)) {
            showShortToast("作业名称不能为空");
            return false;
        } else if (TextUtils.isEmpty(name)) {
            showShortToast("请选择设施名称");
            return false;
        } else if (TextUtils.isEmpty(content)) {
            showShortToast("缺陷内容不能为空");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            showShortToast("请选择对应地址");
            return false;
        }
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == defectList.size() - 1 && defectList.get(position).get("isAdd").equals("1")) {
            selectPhotoPopupWindow.initView(selectPhotoLayout);
            selectPhotoPopupWindow.showPopupWindow();
            selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(AddDefectActivity.this);
            selectPhotoPopupWindow.getTakePhoto().setOnClickListener(AddDefectActivity.this);
        } else {
            Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("imageUrl", path);
            intent.putExtra("isShowDelete", "isShow");
            AddDefectActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_THREE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("addSuccess", false));
                AddDefectActivity.this.finish();
                break;
            case R.id.activity_add_defect__submit://提交缺陷
                final String workNameStr = workName.getText().toString().trim();
                String nameStr = name.getText().toString().trim();//设施名称
                final String workContent = content.getText().toString().trim();
                final String addressStr = address.getText().toString().trim();
                final String addressDesStr = addressDes.getText().toString().trim();

                if (isNull(workNameStr, nameStr, workContent, addressStr)) {

                    if (defectList.size() > 0) {
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.CODE_SUCCESS:
                                        List<String> imageList = (List<String>) msg.obj;
                                        addDefectWork(workNameStr, workContent, addressStr, imageList);
                                        break;
                                }
                            }
                        };
                        upLoadImage(defectList, handler, "addDefect");
                    } else {
                        showShortToast("请先选择图片");
                    }

                }
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddDefectActivity.this, new GlideLoader())
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
            case R.id.activity_add_defect__address_layout:
            case R.id.activity_add_defect__address_image:
                startActivityForResult(new Intent(AddDefectActivity.this, MapActivity.class), Constant.CODE_REQUEST_ONE);
                break;
            case R.id.activity_add_defect__name_layout:
                startActivityForResult(new Intent(AddDefectActivity.this, FacilityActivity.class), Constant.CODE_REQUEST_FOUR);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){
            path.clear();
            defectList.clear();
            return;
        }
        if (resultCode == Constant.CODE_RESULT) {

            if (requestCode == Constant.CODE_REQUEST_ONE) {
                String addressStr = data.getStringExtra("address");
                if (!"".equals(addressStr)) {
                    address.setText(addressStr);
                }
                return;
            } else if (requestCode == Constant.CODE_REQUEST_FOUR) {
                facilityModel = (FacilityInfoModel) data.getSerializableExtra("facilityModel");
                if (facilityModel != null) {
                    name.setText(facilityModel.getFacilityName());
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

            path.clear();
            path.addAll(pathList);

            defectList.clear();
            //list.remove(list.size() - 1);//移除掉最后一个
            for (int i = 0; i < pathList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map.put("imagePath", pathList.get(i));
                map.put("isAdd", "0");
                defectList.add(map);
            }
            if (defectList.size() < 6) {
                initDate(defectList);//添加最后一个 add
            }
            defectAdapter.notifyDataSetChanged();

        }

        if (resultCode != RESULT_OK) {
            return;
        }
        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case Constant.CODE_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        path.add(0, mPublishPhotoPath);
                        defectList.remove(defectList.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        defectList.add(0, map);
                        if (defectList.size() < 6) {
                            initDate(defectList);//添加最后一个 add
                        }
                        defectAdapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
            }
        } else {//高于4.4
            switch (requestCode) {
                case Constant.CODE_REQUEST_ONE:
                    if (resultCode == RESULT_OK) {
                        path.add(0, mPublishPhotoPath);
                        defectList.remove(defectList.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        defectList.add(0, map);
                        if (defectList.size() < 6) {
                            initDate(defectList);//添加最后一个 add
                        }
                        defectAdapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
            }
        }
    }

}
