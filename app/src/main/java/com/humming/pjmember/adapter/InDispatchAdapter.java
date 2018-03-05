package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.flow.RecFileInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2018/2/26.
 */

public class InDispatchAdapter extends BaseQuickAdapter<RecFileInfoBean, BaseViewHolder> {

    public InDispatchAdapter(@Nullable List<RecFileInfoBean> data) {
        super(R.layout.item_dispatch, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecFileInfoBean item) {
        helper.setText(R.id.item_dispatch__title, item.getFileName())
                .setText(R.id.item_dispatch__content, "来文机关：" + item.getRecOrgName()
                        + " 收文文号：" + item.getRecPrefix() + item.getRecTypenumber()
                        + " 收文日期：" + item.getRecTime())
                .addOnClickListener(R.id.item_dispatch__parent);

    }
}

