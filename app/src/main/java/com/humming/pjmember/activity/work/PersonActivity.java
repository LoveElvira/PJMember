package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.PersonAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.user.UserInfoBean;
import com.pjqs.dto.user.UserRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/22.
 * 人员列表
 */

public class PersonActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private PersonAdapter adapter;
    private List<UserInfoBean> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("人员列表");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        adapter = new PersonAdapter(list);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
        getPersonList();
    }

    //获取人员列表
    private void getPersonList() {

        progressHUD = ProgressHUD.show(PersonActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        OkHttpClientManager.postAsyn(Config.GET_WORK_PERSON, new OkHttpClientManager.ResultCallback<UserRes>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(UserRes response) {
                progressHUD.dismiss();
                if (response != null) {
                    if ("1".equals(response.getCode())) {
                        userList = response.getUsers();
                        if (userList != null && userList.size() > 0) {
                            adapter = new PersonAdapter(userList);
                            listView.setAdapter(adapter);
                            adapter.setOnItemChildClickListener(PersonActivity.this);
                        }
                    } else {
                        showShortToast(response.getMessage());
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, UserRes.class, PersonActivity.class);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                PersonActivity.this.finish();
                break;
        }
    }
}
