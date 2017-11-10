package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.emergency.EmergencyInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class NotifyAdapter extends BaseQuickAdapter<EmergencyInfoBean, BaseViewHolder> {
    public NotifyAdapter(@Nullable List<EmergencyInfoBean> data) {
        super(R.layout.item_notify, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmergencyInfoBean item) {
        helper.setText(R.id.item_notify__time, item.getHappensTime() + " " + item.getEmerPlace())
                .setText(R.id.item_notify__warning, item.getTitle())
                .setText(R.id.item_notify__content, item.getEmerDesc())
                .setImageResource(R.id.item_notify__dot, R.mipmap.weather_notify_orange)
                .setBackgroundRes(R.id.item_notify__time, R.drawable.bg_rectangle_orange_radius_10);
    }
}
