package com.humming.pjmember.content.affair;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.humming.pjmember.R;
import com.humming.pjmember.adapter.ContractAdapter;
import com.humming.pjmember.base.BaseLinearLayout;

/**
 * Created by Elvira on 2017/9/7.
 * 收发文
 */

public class DispatchContent extends BaseLinearLayout implements BaseQuickAdapter.OnItemChildClickListener {

    private ContractAdapter adapter;

    public DispatchContent(Context context) {
        this(context, null);
    }

    public DispatchContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.common_listview, this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        listView = findViewById(R.id.common_listview__list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add("" + i);
//        }
//
//        adapter = new AffairAdapter(list, 1);
//        listView.setAdapter(adapter);
//        adapter.setOnItemChildClickListener(this);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_affair__parent:
//                startActivity(WorkPlanActivity.class);
                break;
        }
    }
}
