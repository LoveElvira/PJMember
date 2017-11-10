package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.adapter.ImageLookAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.humming.pjmember.viewutils.SpacesItemDecoration;
import com.pjqs.dto.work.WorkBean;
import com.pjqs.dto.work.WorkPictureRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/11/3.
 * 展示工作内容的图片
 */

public class WorkImageActivity extends BaseWorkActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private ImageLookAdapter adapter;

    private List<String> imageList;

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
        title.setText("图片列表");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listView.setLayoutManager(gridLayoutManager);
        listView.addItemDecoration(new SpacesItemDecoration(10));
        imageList = new ArrayList<>();
        adapter = new ImageLookAdapter(imageList, this);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);

        getWorkImage();

    }


    //获取与作业相关的图片
    private void getWorkImage() {
        progressHUD = ProgressHUD.show(WorkImageActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        OkHttpClientManager.postAsyn(Config.GET_WORK_PIC, new OkHttpClientManager.ResultCallback<WorkPictureRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(WorkPictureRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    if (response.getPictrues() != null && response.getPictrues().size() > 0) {
                        imageList.addAll(response.getPictrues());
                        adapter.setNewData(imageList);
                        adapter.loadMoreEnd();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, WorkPictureRes.class, BaseWorkActivity.class);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                WorkImageActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_image__image:
            case R.id.item_image__image_bg:
                Intent intent = new Intent(getBaseContext(), BrowseImageViewActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageUrl", (Serializable) imageList);
                startActivity(intent);
                break;
        }
    }
}
