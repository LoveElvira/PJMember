package com.humming.pjmember.activity.takephoto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.DefectAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.PicassoLoader;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.selectpic.ImageConfig;
import com.humming.pjmember.viewutils.selectpic.ImageSelector;
import com.humming.pjmember.viewutils.selectpic.ImageSelectorActivity;
import com.pjqs.dto.work.DefictBean;
import com.pjqs.dto.work.QueryDefictRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/4.
 * 拍一拍  缺陷管理
 */

public class DefectActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private DefectAdapter adapter;
    private ArrayList<String> path = new ArrayList<>();
    private List<Map<String, String>> list = new ArrayList<>();

    private List<DefictBean> workList;//每一页
    private List<DefictBean> workLists;//总的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("拍一拍");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);
        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("添加缺陷");

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        selectPhotoLayout = (LinearLayout) findViewById(R.id.popup_photo__parent);
        workLists = new ArrayList<>();
        adapter = new DefectAdapter(workLists);
        listView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, listView);
        adapter.setOnItemChildClickListener(this);
        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);

        pageable = "";
        getDefect(pageable);
    }


    //获取事故记录
    private void getDefect(final String pageable) {
        progressHUD = ProgressHUD.show(DefectActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setPagable(pageable);
        OkHttpClientManager.postAsyn(Config.GET_DEFECT_WORK, new OkHttpClientManager.ResultCallback<QueryDefictRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(QueryDefictRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    workList = response.getDeficts();
                    if (workList != null && workList.size() > 0) {
                        if ("".equals(pageable)) {
                            workLists.clear();
                            workLists.addAll(workList);
                            adapter.setNewData(workList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            DefectActivity.this.pageable = response.getPagable();
                        } else {
                            workLists.addAll(workList);

                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                DefectActivity.this.pageable = response.getPagable();
                                adapter.addData(workList);
                            } else {
                                adapter.addData(workList);
                                hasMore = false;
                                DefectActivity.this.pageable = "";
                            }
                        }
                        adapter.loadMoreComplete();
                    }

                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, QueryDefictRes.class, DefectActivity.class);
    }


    @Override
    public void onLoadMoreRequested() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {//没有数据了
                    adapter.loadMoreEnd(false);
                } else {
                    getDefect(pageable);
                }
            }
        }, delayMillis);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                DefectActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text://添加缺陷
                selectPhotoPopupWindow.initView(selectPhotoLayout);
                selectPhotoPopupWindow.showPopupWindow();
                selectPhotoPopupWindow.getSelectPhoto().setOnClickListener(this);
                selectPhotoPopupWindow.getTakePhoto().setOnClickListener(this);
                break;
            case R.id.popup_photo__take://拍摄
                getCamerePhoto();
                selectPhotoPopupWindow.gonePopupWindow();
                break;
            case R.id.popup_photo__select://选择图片
                ImageConfig imageConfig
                        = new ImageConfig.Builder(DefectActivity.this, new PicassoLoader())
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
        switch (view.getId()) {
            case R.id.item_defect__parent:
                startActivity(new Intent(DefectActivity.this, DefectDetailsActivity.class)
                        .putExtra("id", workLists.get(position).getWorkId()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constant.CODE_RESULT) {
            if (requestCode == Constant.CODE_REQUEST_ONE) {
                path.clear();
                list.clear();
                if (data.getBooleanExtra("addSuccess", false)) {
                    pageable = "";
                    getDefect(pageable);
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
//            adapter.notifyDataSetChanged();

            Intent intent = new Intent(DefectActivity.this, AddDefectActivity.class);
            intent.putExtra("photoLists", (Serializable) list);
            intent.putExtra("path", path);
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
                            initData();//添加最后一个 add
                        }
//                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
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
                            initData();//添加最后一个 add
                        }
//                        adapter.notifyDataSetChanged();
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
            }
        }

        Intent intent = new Intent(DefectActivity.this, AddDefectActivity.class);
        intent.putExtra("photoLists", (Serializable) list);
        intent.putExtra("path", path);
        startActivityForResult(intent, Constant.CODE_REQUEST_ONE);

    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("isAdd", "1");
        list.add(map);
    }

}
