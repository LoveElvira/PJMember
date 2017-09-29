package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.bean.AccidentNatureModel;

import java.util.List;

/**
 * Created by Elvira on 2017/9/29.
 */

public class AccidentNatureAdapter extends BaseQuickAdapter<AccidentNatureModel, BaseViewHolder> {
    public AccidentNatureAdapter(@Nullable List<AccidentNatureModel> data) {
        super(R.layout.item_accident_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccidentNatureModel item) {

        helper.setText(R.id.item_accident_type__name, item.getNatureContent())
                .setVisible(R.id.item_accident_type__select, false)
                .addOnClickListener(R.id.item_accident_type__parent);

        if (item.isSelected()) {
            helper.setVisible(R.id.item_accident_type__select, true);
        }

    }
}
