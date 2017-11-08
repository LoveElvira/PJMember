package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.humming.pjmember.requestdate.RequestParameter;
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
 * Created by Elvira on 2017/11/3.
 * 点击开始作业 展示所选择的图片 可进行修缮
 * 绑定作业相关图片
 * 结束作业
 */

public class StartOrEndWorkActivity extends BaseWorkActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private ImageAdapter adapter;
    private TextView confirmStart;

    //存放图片地址
    private List<Map<String, String>> list = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private String url = "";
    private boolean isEnd = false;
    private boolean isBind = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("选择图片");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        confirmStart = (TextView) findViewById(R.id.activity_start_work__btn);

        //缺陷图片
        listView = (RecyclerView) findViewById(R.id.activity_start_work__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        listView.addItemDecoration(new SpacesItemDecoration(10));
        list = (List<Map<String, String>>) getIntent().getSerializableExtra("photoLists");
        path = (ArrayList<String>) getIntent().getSerializableExtra("path");
        adapter = new ImageAdapter(list, this, 2);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.notifyDataSetChanged();

        String flag = getIntent().getStringExtra("flag");
        isEnd = false;
        isBind = false;
        if ("start".equals(flag)) {
            url = Config.WORK_START;
            confirmStart.setText("确认开始作业");
        } else if ("bind".equals(flag)) {
            url = Config.WORK_BIND_PIC;
            isBind = true;
            confirmStart.setText("绑定图片");
        } else if ("end".equals(flag)) {
            url = Config.WORK_END;
            isEnd = true;
            confirmStart.setText("确认结束作业");
        }

        leftArrow.setOnClickListener(this);
        confirmStart.setOnClickListener(this);
    }

    //开始作业
    private void startWork(List<String> imageList) {
        progressHUD = ProgressHUD.show(StartOrEndWorkActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        if (!isEnd) {
            parameter.setPictureUrls(imageList);
        }
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<SuccessResponse>() {
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
                        if (!isBind) {
                            setResult(Constant.CODE_RESULT, new Intent()
                                    .putExtra("startOrEndWorkSuccess", true));
                        }
                        StartOrEndWorkActivity.this.finish();
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


    private void initDate(List<Map<String, String>> list) {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == list.size() - 1 && list.get(position).get("isAdd").equals("1")) {
            selectPhotoPopupWindow.initView(selectPhotoLayout);
            selectPhotoPopupWindow.showPopupWindow();
            selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(StartOrEndWorkActivity.this);
            selectPhotoPopupWindow.getTakePhoto().setOnClickListener(StartOrEndWorkActivity.this);
        } else {
            Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("imageUrl", path);
            intent.putExtra("isShowDelete", "isShow");
            StartOrEndWorkActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_ONE);
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
            if (list.size() < 6) {
                initDate(list);//添加最后一个 add
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
                            initDate(list);//添加最后一个 add
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
                            initDate(list);//添加最后一个 add
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("startOrEndWorkSuccess", false));
                StartOrEndWorkActivity.this.finish();
                break;
            case R.id.activity_start_work__btn:
//                if (list.size() > 1) {
//                    handler = new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            super.handleMessage(msg);
//                            switch (msg.what) {
//                                case Constant.CODE_SUCCESS:
//                                    List<String> imageList = (List<String>) msg.obj;
//                                    break;
//                            }
//                        }
//                    };
//                    upLoadImage(list, handler);
//                } else {
//                    showShortToast("请选择图片");
//                }
                List<String> imageList = new ArrayList<>();
                imageList.add("aaa");
                startWork(imageList);
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(StartOrEndWorkActivity.this, new PicassoLoader())
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
