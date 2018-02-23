package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.flow.EquipmentApplyInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2018/2/9.
 */

public class AffairEquipmentAdapter extends BaseQuickAdapter<EquipmentApplyInfoBean, BaseViewHolder> {


    public AffairEquipmentAdapter(@Nullable List<EquipmentApplyInfoBean> data) {
        super(R.layout.item_affair_equipment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentApplyInfoBean item) {
        helper.setText(R.id.item_affair_equipment__num, item.getApplyNo())
                .setText(R.id.item_affair_equipment__people, item.getApplyUser())
                .setText(R.id.item_affair_equipment__department, item.getApplyDepartment())
                .addOnClickListener(R.id.item_affair_equipment__parent);
    }
}
