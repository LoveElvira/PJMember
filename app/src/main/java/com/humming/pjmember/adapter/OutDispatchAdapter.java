package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.flow.DisFileInfoBean;
import com.pjqs.dto.flow.RecFileInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2018/2/26.
 */

public class OutDispatchAdapter extends BaseQuickAdapter<DisFileInfoBean, BaseViewHolder> {

    public OutDispatchAdapter(@Nullable List<DisFileInfoBean> data) {
        super(R.layout.item_dispatch, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DisFileInfoBean item) {
        helper.setText(R.id.item_dispatch__title, item.getTitle())
                .setText(R.id.item_dispatch__content, "拟办部门：" + item.getImitateOrgName()
                        + " 编号：" + item.getNumberCode()
                        + " 封发日期：" + item.getDispatchDate())
                .addOnClickListener(R.id.item_dispatch__parent);

    }
}

