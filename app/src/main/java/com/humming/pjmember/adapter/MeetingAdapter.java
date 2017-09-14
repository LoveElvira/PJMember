package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;

import java.util.List;

/**
 * Created by Elvira on 2017/9/7.
 */

public class MeetingAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MeetingAdapter(@Nullable List<String> data) {
        super(R.layout.item_meeting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
