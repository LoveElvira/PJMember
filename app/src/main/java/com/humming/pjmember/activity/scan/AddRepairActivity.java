package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.adapter.ImageAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.AddRepairParameter;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.requestdate.UploadParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.PicassoLoader;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.humming.pjmember.viewutils.selectpic.ImageConfig;
import com.humming.pjmember.viewutils.selectpic.ImageSelector;
import com.humming.pjmember.viewutils.selectpic.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/3.
 * 添加维修记录
 */

public class AddRepairActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //维修时间标题
    private TextView timeTitle;
    //维修时间
    private TextView time;
    //维修单位标题
    private TextView companyTitle;
    //维修单位
    private LinearLayout companyLayout;
    private EditText company;
    //维修内容标题
    private TextView contentTitle;
    //维修内容
    private EditText content;
    //维修金额标题
    private TextView priceTitle;
    //维修金额
    private EditText price;
    //listview title
    private TextView listViewTitle;
    //发票上传 listview
    //提交
    private TextView submit;

    private ImageAdapter adapter;

    private List<Map<String, String>> list = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");
        popupParent = View.inflate(getBaseContext(), R.layout.activity_add_log, null);

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("添加维修记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        timeTitle = (TextView) findViewById(R.id.activity_add_log__time_title);
        time = (TextView) findViewById(R.id.activity_add_log__time);
        companyLayout = (LinearLayout) findViewById(R.id.activity_add_log__company_layout);
        companyTitle = (TextView) findViewById(R.id.activity_add_log__company_title);
        company = (EditText) findViewById(R.id.activity_add_log__company);
        contentTitle = (TextView) findViewById(R.id.activity_add_log__content_title);
        content = (EditText) findViewById(R.id.activity_add_log__content);
        priceTitle = (TextView) findViewById(R.id.activity_add_log__price_title);
        price = (EditText) findViewById(R.id.activity_add_log__price);
        listViewTitle = (TextView) findViewById(R.id.activity_add_log__listview_title);

        companyLayout.setVisibility(View.VISIBLE);
        timeTitle.setText("维修时间");
        time.setHint("请选择维修时间");
        companyTitle.setText("维修单位");
        company.setHint("请输入维修单位");
        contentTitle.setText("维修内容");
        content.setHint("请输入维修内容");
        priceTitle.setText("维修金额");
        price.setHint("请输入维修金额");
        listViewTitle.setText("发票上传");

        listView = (RecyclerView) findViewById(R.id.activity_add_log__listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        listView.addItemDecoration(new SpacesItemDecoration(10));
        initAddImage();
        adapter = new ImageAdapter(list, this, 1);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        submit = (TextView) findViewById(R.id.activity_add_log__submit);

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
        time.setOnClickListener(this);
    }


    private void initAddImage() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    private boolean isNull(String time, String company, String content, String price) {
        if (TextUtils.isEmpty(time)) {
            showShortToast("维修时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(company)) {
            showShortToast("维修单位不能为空");
            return false;
        } else if (TextUtils.isEmpty(price)) {
            showShortToast("维修金额不能为空");
            return false;
        } else if (TextUtils.isEmpty(content)) {
            showShortToast("维修内容不能为空");
            return false;
        }
        return true;
    }

    //新增设备维修信息
    private void addRepairLog(String time, String company, String content, String price) {
        progressHUD = ProgressHUD.show(AddRepairActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        AddRepairParameter parameter = new AddRepairParameter();
        parameter.setEquipmentId(id);
        parameter.setRepairTime(time);
        parameter.setRepairDepartment(company);
        parameter.setReason(content);
        parameter.setRepairFee(price);

        OkHttpClientManager.postAsyn(Config.ADD_REPAIR_LOG, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        AddRepairActivity.this.finish();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, SuccessResponse.class, DeviceManageActivity.class);

    }

    private void upLoadImage(List<Map<String, String>> list) {
        images = new String[list.size() - 1];
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < list.size() - 1; i++) {
            File file = new File(list.get(i).get("imagePath"));
            files.add(file);
        }
        UploadParameter upload = new UploadParameter();
        upload.setType("addRepair");
        OkHttpClientManager.postAsyn(Config.URL_SERVICE_UPLOAD, new OkHttpClientManager.ResultCallback<List<String>>() {

            @Override
            public void onError(Request request, Error info) {
                showShortToast(info.getInfo().toString());
                Log.e("onError", info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(List<String> response) {
                for (int i = 0; i < response.size(); i++) {
                    images[i] = response.get(i);
//                    customerDialog.dismiss();
                }
                //sendSuggestion();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, upload, files, new TypeReference<List<String>>() {
        }, AddRepairActivity.class);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddRepairActivity.this.finish();
                break;
            case R.id.activity_add_log__submit:
                String timeStr = time.getText().toString().trim();
                String companyStr = company.getText().toString().trim();
                String contentStr = content.getText().toString().trim();
                String priceStr = price.getText().toString().trim();
                if (isNull(timeStr, companyStr, contentStr, priceStr)) {
                    addRepairLog(timeStr, companyStr, contentStr, priceStr);
                }
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddRepairActivity.this, new PicassoLoader())
                        .steepToolBarColor(ContextCompat.getColor(getBaseContext(), R.color.black))
                        .titleBgColor(ContextCompat.getColor(getBaseContext(), R.color.black))
                        .titleSubmitTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                        .titleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                        .mutiSelect()
                        .mutiSelectMaxSize(1)
                        .pathList(path)
                        .filePath("/ImageSelector/Pictures")
//                        .showCamera()//显示拍摄按钮
                        .build();
                ImageSelector.open(imageConfig);
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.activity_add_log__time://选择时间
                showPopWindowDatePicker(popupParent);
                break;
            case R.id.date_submit://获取时间
                time.setText(getDate());
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == list.size() - 1 && list.get(position).get("isAdd").equals("1")) {
            selectPhotoPopupWindow.initView(selectPhotoLayout);
            selectPhotoPopupWindow.showPopupWindow();
            selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(this);
            selectPhotoPopupWindow.getTakePhoto().setOnClickListener(this);
        } else {
            Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("imageUrl", path);
            intent.putExtra("isShowDelete", "isShow");
            AddRepairActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_THREE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.CODE_RESULT) {
            List<String> pathList = null;
            if (requestCode == Constant.CODE_REQUEST_THREE) {
                pathList = (List<String>) data.getSerializableExtra("imagePath");
            } else {
                pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            }
            Log.i("ee", "---------" + pathList.size());

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
            if (list.size() < 1) {
                initAddImage();//添加最后一个 add
            }
            adapter.notifyDataSetChanged();
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case Constant.CODE_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        path.add(0, mPublishPhotoPath);
                        list.remove(list.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        list.add(0, map);
                        if (list.size() < 1) {
                            initAddImage();//添加最后一个 add
                        }
                        adapter.notifyDataSetChanged();
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
                        list.remove(list.size() - 1);//移除掉最后一个
                        Map<String, String> map = new HashMap<>();
                        map.put("imagePath", mPublishPhotoPath);
                        map.put("isAdd", "0");
                        list.add(0, map);
                        if (list.size() < 1) {
                            initAddImage();//添加最后一个 add
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
            }
        }

    }
}
