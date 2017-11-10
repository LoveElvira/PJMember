package com.humming.pjmember.activity.scan;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.RepairAdapter;
import com.humming.pjmember.adapter.UseOilAdapter;
import com.humming.pjmember.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/3.
 * 用油记录
 */

public class UseOilLogActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private UseOilAdapter adapter;

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
        title.setText("用油记录");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        adapter = new UseOilAdapter(list);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                UseOilLogActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_use_oil__parent:
                startActivity(RepairDetailsActivity.class);
                break;
        }
    }
}
