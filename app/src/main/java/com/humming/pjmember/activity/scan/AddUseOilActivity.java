package com.humming.pjmember.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.utils.PicassoLoader;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.humming.pjmember.viewutils.selectpic.ImageConfig;
import com.humming.pjmember.viewutils.selectpic.ImageSelector;
import com.humming.pjmember.viewutils.selectpic.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2017/9/3.
 * 添加用油记录
 */

public class AddUseOilActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //维修时间标题
    private TextView timeTitle;
    //维修时间
    private TextView time;
    //设备名称
//    private EditText name;
    //设备编号
//    private EditText num;
    //维修内容标题
    private TextView contentTitle;
    //维修内容
    private EditText content;
    //维修金额标题
    private TextView priceTitle;
    //维修金额
    private EditText price;
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
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("添加用油记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        timeTitle = (TextView) findViewById(R.id.activity_add_log__time_title);
        time = (TextView) findViewById(R.id.activity_add_log__time);
//        name = (EditText) findViewById(R.id.activity_add_repair__name);
//        num = (EditText) findViewById(R.id.activity_add_repair__num);
        contentTitle = (TextView) findViewById(R.id.activity_add_log__content_title);
        content = (EditText) findViewById(R.id.activity_add_log__content);
        priceTitle = (TextView) findViewById(R.id.activity_add_log__price_title);
        price = (EditText) findViewById(R.id.activity_add_log__price);

        timeTitle.setText("用油时间");
        time.setHint("2017-08-04 16:58");
        contentTitle.setText("加油数（L）");
        content.setHint("请输入加油数");
        content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceTitle.setText("加油金额");
        price.setHint("请输入加油金额");


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
    }


    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddUseOilActivity.this.finish();
                break;
            case R.id.activity_add_log__submit:
                AddUseOilActivity.this.finish();
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddUseOilActivity.this, new PicassoLoader())
                        .steepToolBarColor(ContextCompat.getColor(getBaseContext(),R.color.black))
                        .titleBgColor(ContextCompat.getColor(getBaseContext(),R.color.black))
                        .titleSubmitTextColor(ContextCompat.getColor(getBaseContext(),R.color.white))
                        .titleTextColor(ContextCompat.getColor(getBaseContext(),R.color.white))
                        .mutiSelect()
                        .mutiSelectMaxSize(1)
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
                        if (list.size() < 1) {
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
                        if (list.size() < 1) {
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
