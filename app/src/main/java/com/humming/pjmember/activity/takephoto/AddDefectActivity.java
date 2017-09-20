package com.humming.pjmember.activity.takephoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.activity.MapActivity;
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
 * Created by Elvira on 2017/9/4.
 * 添加缺陷
 */

public class AddDefectActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    //设施名称
    private LinearLayout nameLayout;
    //名称
    private TextView name;
    //通知主管
    private LinearLayout directorLayout;
    //主管
    private TextView director;
    //报修时间
    private EditText time;
    //缺陷描述
    private EditText content;
    //选择位置
    private LinearLayout addressLayout;
    //位置
    private TextView address;
    //详细位置描述
    private EditText addressDes;

    //提交
    private TextView submit;

    //缺陷
    private ImageAdapter defectAdapter;

    //存放图片地址
    private List<Map<String, String>> defectList = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();


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
        title.setText("缺陷管理");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);

        //缺陷图片
        listView = (RecyclerView) findViewById(R.id.activity_add_defect__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);

        nameLayout = (LinearLayout) findViewById(R.id.activity_add_defect__name_layout);
        name = (TextView) findViewById(R.id.activity_add_defect__name);
        directorLayout = (LinearLayout) findViewById(R.id.activity_add_defect__director_layout);
        director = (TextView) findViewById(R.id.activity_add_defect__director);
        time = (EditText) findViewById(R.id.activity_add_defect__time);
        content = (EditText) findViewById(R.id.activity_add_defect__content);
        addressLayout = (LinearLayout) findViewById(R.id.activity_add_defect__address_layout);
        address = (TextView) findViewById(R.id.activity_add_defect__address);
        addressDes = (EditText) findViewById(R.id.activity_add_defect__address_des);

        submit = (TextView) findViewById(R.id.activity_add_defect__submit);

        leftArrow.setOnClickListener(this);
        submit.setOnClickListener(this);
        addressLayout.setOnClickListener(this);

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
            AddDefectActivity.this.startActivityForResult(intent, Constant.CODE_REQUEST_ONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AddDefectActivity.this.finish();
                break;
            case R.id.activity_add_defect__submit://提交缺陷
                AddDefectActivity.this.finish();
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(AddDefectActivity.this, new PicassoLoader())
                        .steepToolBarColor(ContextCompat.getColor(getBaseContext(),R.color.black))
                        .titleBgColor(ContextCompat.getColor(getBaseContext(),R.color.black))
                        .titleSubmitTextColor(ContextCompat.getColor(getBaseContext(),R.color.white))
                        .titleTextColor(ContextCompat.getColor(getBaseContext(),R.color.white))
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
                startActivityForResult(new Intent(AddDefectActivity.this, MapActivity.class), Constant.CODE_REQUEST_ONE);
                break;
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
