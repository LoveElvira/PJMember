package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.utils.StringUtils;
import com.pjqs.dto.equipment.EquipmentOilRecInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class UseOilAdapter extends BaseQuickAdapter<EquipmentOilRecInfo, BaseViewHolder> {

    public UseOilAdapter(@Nullable List<EquipmentOilRecInfo> data) {
        super(R.layout.item_use_oil, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentOilRecInfo item) {
        helper.setText(R.id.item_use_oil__time, item.getDate())
                .setText(R.id.item_use_oil__oil, "加油量(L)：" + item.getOil())
                .setText(R.id.item_use_oil__price, "金额：" + StringUtils.saveTwoDecimal(item.getMoney()))
                .addOnClickListener(R.id.item_use_oil__parent);
    }
}
