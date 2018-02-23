package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.flow.CostDetailInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2018/2/9.
 */

public class AffairPriceAdapter extends BaseQuickAdapter<CostDetailInfoBean, BaseViewHolder> {


    public AffairPriceAdapter(@Nullable List<CostDetailInfoBean> data) {
        super(R.layout.item_affair_price, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CostDetailInfoBean item) {
        helper.setText(R.id.item_affair_price__num, item.getCostNo())
                .setText(R.id.item_affair_price__department, item.getDecDepartment())
                .setText(R.id.item_affair_price__type, item.getCostType())
                .setText(R.id.item_affair_price__receivables, item.getReceiptDepartment())
                .addOnClickListener(R.id.item_affair_price__parent);
    }
}
