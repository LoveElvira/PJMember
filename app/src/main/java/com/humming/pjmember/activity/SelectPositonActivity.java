package com.humming.pjmember.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.MainActivity;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.SelectPositionAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.utils.SharePrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/14.
 * 选择所属职位
 */

public class SelectPositonActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private SelectPositionAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("选择职位");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        listView = (RecyclerView) findViewById(R.id.activity_select_position__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        listView.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        list.add("一线\n人员");
        list.add("业务\n人员");
        list.add("领导\n人员");

        adapter = new SelectPositionAdapter(list);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                SelectPositonActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_select_position__parent:
                if (list.get(position).equals("一线\n人员")) {
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "1", SelectPositonActivity.this);
                } else if (list.get(position).equals("业务\n人员")) {
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "2", SelectPositonActivity.this);
                } else {
                    SharePrefUtil.putString(Constant.FILE_NAME, Constant.POSITION, "3", SelectPositonActivity.this);
                }
                startActivity(MainActivity.class);
                SelectPositonActivity.this.finish();
                break;
        }
    }
}
