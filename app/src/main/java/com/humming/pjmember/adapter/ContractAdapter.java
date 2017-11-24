package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.contract.ContractInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class ContractAdapter extends BaseQuickAdapter<ContractInfoBean, BaseViewHolder> {

    public ContractAdapter(@Nullable List<ContractInfoBean> data) {
        super(R.layout.item_affair, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContractInfoBean item) {
        helper.setText(R.id.item_affair__title, item.getConName())
                .setText(R.id.item_affair__people_name, item.getCrtUserName())
                .setText(R.id.item_affair__time, item.getCrtTime())
                .addOnClickListener(R.id.item_affair__parent)
                .setImageResource(R.id.item_affair__image, R.mipmap.affair_contract_small);

    }
}
