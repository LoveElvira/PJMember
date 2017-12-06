package com.humming.pjmember.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.humming.pjmember.activity.MapActivity;
import com.humming.pjmember.adapter.ImageAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.add.AddRepairParameter;
import com.humming.pjmember.requestdate.add.AddUserOilParameter;
import com.humming.pjmember.responsedate.SuccessResponse;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.GlideLoader;
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
 * 添加用油记录
 */

public class AddUseOilActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //用油时间标题
    private TextView timeTitle;
    //用油时间
    private TextView time;
    //加油卡号标题
    private LinearLayout cardLayout;
    private TextView cardTitle;
    //加油卡号
    private EditText card;
    //加油地点标题
    private LinearLayout addressLayout;
    private TextView addressTitle;
    //加油地点
    private EditText address;
    private ImageView addressImage;
    //用油内容标题
    private TextView contentTitle;
    //用油内容
    private EditText content;
    //用油金额标题
    private TextView priceTitle;
    //用油金额
    private EditText price;
    private TextView listViewTitle;
    //发票上传 listview
    //提交
    private TextView submit;

    private ImageAdapter adapter;

    private List<Map<String, String>> list = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();


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
        title.setText("添加用油记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        timeTitle = (TextView) findViewById(R.id.activity_add_log__time_title);
        time = (TextView) findViewById(R.id.activity_add_log__time);
        cardLayout = (LinearLayout) findViewById(R.id.activity_add_log__company_layout);
        cardTitle = (TextView) findViewById(R.id.activity_add_log__company_title);
        card = (EditText) findViewById(R.id.activity_add_log__company);
        addressLayout = (LinearLayout) findViewById(R.id.activity_add_log__address_layout);
        addressTitle = (TextView) findViewById(R.id.activity_add_log__address_title);
        address = (EditText) findViewById(R.id.activity_add_log__address);
        addressImage = (ImageView) findViewById(R.id.activity_add_log__address_image);
        contentTitle = (TextView) findViewById(R.id.activity_add_log__content_title);
        content = (EditText) findViewById(R.id.activity_add_log__content);
        priceTitle = (TextView) findViewById(R.id.activity_add_log__price_title);
        price = (EditText) findViewById(R.id.activity_add_log__price);
        listViewTitle = (TextView) findViewById(R.id.activity_add_log__listview_title);


        timeTitle.setText("加油时间");
        time.setHint("请选择加油时间");
        cardLayout.setVisibility(View.VISIBLE);
        cardTitle.setText("加油卡号");
        card.setHint("请输入加油卡号");
        addressLayout.setVisibility(View.VISIBLE);
        addressTitle.setText("加油地点");
        address.setHint("请输入加油地点");
        contentTitle.setText("加油量（L）");
        content.setHint("请输入加油量");
        content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceTitle.setText("加油金额");
        price.setHint("请输入加油金额");
        listViewTitle.setText("发票上传");


        listView = (RecyclerView) findViewById(R.id.activity_add_log__listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        listView.addItemDecoration(new SpacesItemDecoration(10));
        initData();
        adapter = new ImageAdapter(list, this, 1);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        submit = (TextView) findViewById(R.id.activity_add_log__submit);

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
        time.setOnClickListener(this);
        addressImage.setOnClickListener(this);
    }


    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    private boolean isNull(String time, String card, String address, String content, String price) {
        if (TextUtils.isEmpty(time)) {
            showShortToast("加油时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(card)) {
            showShortToast("加油卡号不能为空");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            showShortToast("加油地点不能为空");
            return false;
        } else if (TextUtils.isEmpty(content)) {
            showShortToast("加油量不能为空");
            return false;
        } else if (TextUtils.isEmpty(price)) {
            showShortToast("加油金额不能为空");
            return false;
        }
        return true;
    }

    //新增设备用油信息
    private void addUseOilLog(String time, String card, String address, String content, String price, List<String> imageList) {
        AddUserOilParameter parameter = new AddUserOilParameter();
        parameter.setEquipmentId(id);
        parameter.setMakeupOilTime(time);
        parameter.setMakeupOilCard(card);
        parameter.setMakeupOilAddr(address);
        parameter.setMakeupOilQuantity(content);
        parameter.setMoney(price);
        parameter.setOilImgs(imageList);

        OkHttpClientManager.postAsyn(Config.ADD_OIL_LOG, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        AddUseOilActivity.this.finish();
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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddUseOilActivity.this.finish();
                break;
            case R.id.activity_add_log__submit:
                final String timeStr = time.getText().toString().trim();
                final String cardStr = card.getText().toString().trim();
                final String addressStr = address.getText().toString().trim();
                final String contentStr = content.getText().toString().trim();
                final String priceStr = price.getText().toString().trim();
                if (isNull(timeStr, cardStr, addressStr, contentStr, priceStr)) {
                    if (list.size() > 0) {
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Constant.CODE_SUCCESS:
                                        List<String> imageList = (List<String>) msg.obj;
                                        addUseOilLog(timeStr, cardStr, addressStr, contentStr, priceStr, imageList);
                                        break;
                                }
                            }
                        };
                        upLoadImage(list, handler, "addUseOil");
                    } else {
                        showShortToast("请先选择图片");
                    }
                }
                break;
            case R.id.activity_add_log__address_image:
                startActivityForResult(new Intent(AddUseOilActivity.this, MapActivity.class), Constant.CODE_REQUEST_ONE);
                break;
            case R.id.activity_add_log__time:
                showPopWindowDatePicker(popupParent);
                break;
            case R.id.date_submit://获取时间
                time.setText(getDate());
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddUseOilActivity.this, new GlideLoader())
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
            AddUseOilActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_THREE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.CODE_RESULT) {

            if (requestCode == Constant.CODE_REQUEST_ONE) {
                String addressStr = data.getStringExtra("address");
                if (!"".equals(addressStr)) {
                    address.setText(addressStr);
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

            list.clear();
            //list.remove(list.size() - 1);//移除掉最后一个
            for (int i = 0; i < pathList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map.put("imagePath", pathList.get(i));
                map.put("isAdd", "0");
                list.add(map);
            }
            if (list.size() < 6) {
                initData();//添加最后一个 add
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
                            initData();//添加最后一个 add
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
                            initData();//添加最后一个 add
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
