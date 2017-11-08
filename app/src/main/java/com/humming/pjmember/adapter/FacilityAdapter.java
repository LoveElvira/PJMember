package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.bean.AccidentTypeModel;
import com.humming.pjmember.bean.FacilityInfoModel;

import java.util.List;

/**
 * Created by Elvira on 2017/9/29.
 */

public class FacilityAdapter extends BaseQuickAdapter<FacilityInfoModel, BaseViewHolder> {
    public FacilityAdapter(@Nullable List<FacilityInfoModel> data) {
        super(R.layout.item_accident_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FacilityInfoModel item) {

        helper.setText(R.id.item_accident_type__name, item.getFacilityName())
                .setVisible(R.id.item_accident_type__select, false)
                .addOnClickListener(R.id.item_accident_type__parent);

        if (item.isSelected()) {
            helper.setVisible(R.id.item_accident_type__select, true);
        }

    }
}
