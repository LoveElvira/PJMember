package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2017/9/20.
 */

public class TimeAdapter extends BaseQuickAdapter<Map<String, String>, BaseViewHolder> {
    public TimeAdapter(@Nullable List<Map<String, String>> data) {
        super(R.layout.item_time_top_, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, String> item) {

        helper.addOnClickListener(R.id.item_time_top__parent)
                .setBackgroundRes(R.id.item_time_top__num, R.drawable.bg_null)
                .setTextColor(R.id.item_time_top__num, ContextCompat.getColor(helper.getView(R.id.item_time_top__num).getContext(), R.color.white))
                .setText(R.id.item_time_top__num, item.get("day"));

        if ("true".equals(item.get("isSelect"))) {
            helper.setBackgroundRes(R.id.item_time_top__num, R.drawable.circle_white)
                    .setTextColor(R.id.item_time_top__num, ContextCompat.getColor(helper.getView(R.id.item_time_top__num).getContext(), R.color.black));
        }

    }

}
