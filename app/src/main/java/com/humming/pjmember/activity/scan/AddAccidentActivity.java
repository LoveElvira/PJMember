package com.humming.pjmember.activity.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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
import com.humming.pjmember.adapter.ImageAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.bean.AccidentNatureModel;
import com.humming.pjmember.bean.AccidentTypeModel;
import com.humming.pjmember.requestdate.AddAccidentParameter;
import com.humming.pjmember.requestdate.AddMaintainParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.PicassoLoader;
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
 * Created by Elvira on 2017/9/3.
 * 添加事故记录
 */

public class AddAccidentActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //事故时间标题
    private TextView timeTitle;
    //事故时间
    private TextView time;
    //事故类型标题
    private TextView typeTitle;
    //事故类型
    private LinearLayout typeLayout;
    private EditText type;
    //事故性质标题
    private TextView natureTitle;
    //事故性质
    private LinearLayout natureLayout;
    private TextView nature;
    //事故内容标题
    private TextView contentTitle;
    //事故内容
    private EditText content;
    //事故金额标题
    private TextView priceTitle;
    //事故金额
    private EditText price;
    //listview title
    private TextView listViewTitle;
    //发票上传 listview

    //提交
    private TextView submit;

    private ImageAdapter adapter;

    private List<Map<String, String>> list = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private AccidentNatureModel natureModel;
    private AccidentTypeModel typeModel;


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
        title.setText("添加事故记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        timeTitle = (TextView) findViewById(R.id.activity_add_log__time_title);
        time = (TextView) findViewById(R.id.activity_add_log__time);
        typeLayout = (LinearLayout) findViewById(R.id.activity_add_log__company_layout);
        typeTitle = (TextView) findViewById(R.id.activity_add_log__company_title);
        type = (EditText) findViewById(R.id.activity_add_log__company);
        natureLayout = (LinearLayout) findViewById(R.id.activity_add_log__type_layout);
        natureTitle = (TextView) findViewById(R.id.activity_add_log__type_title);
        nature = (TextView) findViewById(R.id.activity_add_log__type_tv);
        contentTitle = (TextView) findViewById(R.id.activity_add_log__content_title);
        content = (EditText) findViewById(R.id.activity_add_log__content);
        priceTitle = (TextView) findViewById(R.id.activity_add_log__price_title);
        price = (EditText) findViewById(R.id.activity_add_log__price);
        listViewTitle = (TextView) findViewById(R.id.activity_add_log__listview_title);

        typeLayout.setVisibility(View.VISIBLE);
        natureLayout.setVisibility(View.VISIBLE);
        nature.setVisibility(View.VISIBLE);
        timeTitle.setText("事故时间");
        time.setHint("请选择事故时间");
        typeTitle.setText("事故类型");
        type.setHint("请选择事故类型");
        type.setFocusable(false);
        natureTitle.setText("事故性质");
        nature.setHint("请选择事故性质");
        contentTitle.setText("事故原因");
        content.setHint("请输入事故原因");
        priceTitle.setText("损失金额");
        price.setHint("请输入损失金额");
//        price.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        listViewTitle.setText("相关资料上传");

        listView = (RecyclerView) findViewById(R.id.activity_add_log__listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        listView.addItemDecoration(new SpacesItemDecoration(10));
        initDate();
        adapter = new ImageAdapter(list, this, 1);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        submit = (TextView) findViewById(R.id.activity_add_log__submit);

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
        type.setOnClickListener(this);
        nature.setOnClickListener(this);
        time.setOnClickListener(this);
    }


    private void initDate() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    //新增设备事故信息
    private void addAccidentLog(String time, String content, String price) {
        progressHUD = ProgressHUD.show(AddAccidentActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        AddAccidentParameter parameter = new AddAccidentParameter();
        parameter.setEquipmentId(id);
        parameter.setAccidentTime(time);
        parameter.setNature(natureModel.getNatureContent());//事故性质 1.轻微事故2.一般事故3.严重事故
        parameter.setType(typeModel.getTypeContent());//事故类型 1.追尾2.被追尾3.被撞
        parameter.setRemark(content);
        parameter.setLossFee(price);

        OkHttpClientManager.postAsyn(Config.ADD_ACCIDENT_LOG, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        AddAccidentActivity.this.finish();
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

    private boolean isNull(String time, String nature, String type, String content, String price) {
        if (TextUtils.isEmpty(time)) {
            showShortToast("事故时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(type)) {
            showShortToast("事故类型不能为空");
            return false;
        } else if (TextUtils.isEmpty(nature)) {
            showShortToast("事故性质不能为空");
            return false;
        } else if (TextUtils.isEmpty(price)) {
            showShortToast("事故金额不能为空");
            return false;
        } else if (TextUtils.isEmpty(content)) {
            showShortToast("事故原因不能为空");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddAccidentActivity.this.finish();
                break;
            case R.id.activity_add_log__submit:
                String timeStr = time.getText().toString().trim();
                String typeStr = type.getText().toString().trim();
                String natureStr = nature.getText().toString().trim();
                String contentStr = content.getText().toString().trim();
                String priceStr = price.getText().toString().trim();
                if (isNull(timeStr, natureStr, typeStr, contentStr, priceStr)) {
                    addAccidentLog(timeStr, contentStr, priceStr);
                }
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddAccidentActivity.this, new PicassoLoader())
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
            case R.id.activity_add_log__company://事故类型
                startActivityForResult(new Intent(AddAccidentActivity.this, AccidentTypeActivity.class), Constant.CODE_REQUEST_FIVE);
                break;
            case R.id.activity_add_log__type_tv://事故性质
                startActivityForResult(new Intent(AddAccidentActivity.this, AccidentNatureActivity.class), Constant.CODE_REQUEST_SIX);
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
            AddAccidentActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_THREE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.CODE_RESULT) {

            switch (requestCode) {
                case Constant.CODE_REQUEST_FIVE:
                    typeModel = (AccidentTypeModel) data.getSerializableExtra("typeModel");
                    type.setText(typeModel.getTypeContent());
                    return;
                case Constant.CODE_REQUEST_SIX:
                    natureModel = (AccidentNatureModel) data.getSerializableExtra("natureModel");
                    nature.setText(natureModel.getNatureContent());
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

            list.clear();
            //list.remove(list.size() - 1);//移除掉最后一个
            for (int i = 0; i < pathList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map.put("imagePath", pathList.get(i));
                map.put("isAdd", "0");
                list.add(map);
            }
            if (list.size() < 6) {
                initDate();//添加最后一个 add
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
                        if (list.size() < 6) {
                            initDate();//添加最后一个 add
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
                        if (list.size() < 6) {
                            initDate();//添加最后一个 add
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
