package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.bean.TimeModel;

import java.util.List;

/**
 * Created by Elvira on 2017/9/20.
 */

public class TimeAdapter extends BaseQuickAdapter<TimeModel, BaseViewHolder> {
    public TimeAdapter(@Nullable List<TimeModel> data) {
        super(R.layout.item_time_top_, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeModel item) {

        helper.addOnClickListener(R.id.item_time_top__parent)
                .setBackgroundRes(R.id.item_time_top__num, R.drawable.bg_null)
                .setTextColor(R.id.item_time_top__num, ContextCompat.getColor(helper.getView(R.id.item_time_top__num).getContext(), R.color.white))
                .setText(R.id.item_time_top__num, item.getTime());

        if (item.isSelect()) {
            helper.setBackgroundRes(R.id.item_time_top__num, R.drawable.circle_white)
                    .setTextColor(R.id.item_time_top__num, ContextCompat.getColor(helper.getView(R.id.item_time_top__num).getContext(), R.color.black));
        }

    }

}
