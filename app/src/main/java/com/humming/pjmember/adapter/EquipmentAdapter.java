package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.equipment.EquipmentInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/22.
 */

public class EquipmentAdapter extends BaseQuickAdapter<EquipmentInfoBean, BaseViewHolder> {
    public EquipmentAdapter(@Nullable List<EquipmentInfoBean> data) {
        super(R.layout.item_plan_equipment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentInfoBean item) {

        helper.setText(R.id.item_plan_equipment__num, item.getDeviceNo())
                .setText(R.id.item_plan_equipment__name, item.getDeviceName())
                .setText(R.id.item_plan_equipment__big_type, item.getDeviceBroadType())
                .setText(R.id.item_plan_equipment__small_type, item.getDeviceSubType())
                .setText(R.id.item_plan_equipment__plate_num, item.getLicenseTag());


    }
}
