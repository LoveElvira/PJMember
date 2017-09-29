package com.humming.pjmember.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.AccidentNatureAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.bean.AccidentNatureModel;
import com.humming.pjmember.utils.SharePrefUtil;
import com.pjqs.dto.equipment.EquipmentAcctidentNature;
import com.pjqs.dto.equipment.EquipmentTypeRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/9/25.
 * 选择事故性质
 */

public class AccidentNatureActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private AccidentNatureAdapter adapter;
    private EquipmentTypeRes response;
    private List<AccidentNatureModel> natureList;

    private AccidentNatureModel nature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        response = SharePrefUtil.getObject(Constant.FILE_NAME, Constant.ACCIDENT_TYPE, null, AccidentNatureActivity.this);

        natureList = new ArrayList<>();
        for (int i = 0; i < response.getNatureList().size(); i++) {
            AccidentNatureModel model = new AccidentNatureModel();
            model.setNatureContent(response.getNatureList().get(i).getNatureContent());
            model.setNatureId(response.getNatureList().get(i).getNatureId());
            model.setSelected(false);
            natureList.add(model);
        }

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("事故性质");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        rightText = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightText.setText("确定");

        listView = (RecyclerView) findViewById(R.id.comment_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);

        adapter = new AccidentNatureAdapter(natureList);
        listView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);

        leftArrow.setOnClickListener(this);
        rightText.setOnClickListener(this);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_accident_type__parent:
                if (!natureList.get(position).isSelected()) {
                    nature = natureList.get(position);
                    for (int i = 0; i < natureList.size(); i++) {
                        natureList.get(i).setSelected(false);
                    }
                    natureList.get(position).setSelected(true);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                AccidentNatureActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("natureModel", nature));
                AccidentNatureActivity.this.finish();
                break;
        }
    }

}
