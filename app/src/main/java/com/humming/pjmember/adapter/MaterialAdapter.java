package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.material.MaterialBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/22.
 */

public class MaterialAdapter extends BaseQuickAdapter<MaterialBean, BaseViewHolder> {
    public MaterialAdapter(@Nullable List<MaterialBean> data) {
        super(R.layout.item_plan_material, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialBean item) {

        helper.setText(R.id.item_plan_material__name, item.getMaterialName())
                .setText(R.id.item_plan_material__use, item.getMaterialDosage() + item.getMaterialUnit())
                .setText(R.id.item_plan_material__remark, item.getRemark());
    }
}
