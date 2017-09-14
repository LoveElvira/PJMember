package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class SelectPositionAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SelectPositionAdapter(@Nullable List<String> data) {
        super(R.layout.item_select_positon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.addOnClickListener(R.id.item_select_position__parent)
                .setText(R.id.item_select_position__text, item);
    }
}
