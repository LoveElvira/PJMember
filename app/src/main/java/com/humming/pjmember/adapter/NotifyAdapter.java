package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class NotifyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public NotifyAdapter(@Nullable List<String> data) {
        super(R.layout.item_notify, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item.equals("0")) {
            helper.setText(R.id.item_notify__time, "08-12 01:34 南浦")
                    .setText(R.id.item_notify__warning, "浦西主引桥西向东")
                    .setText(R.id.item_notify__content, "01:34浦西主引桥西向东发生轿车单车事故，占道一根。无人员伤亡，防撞墙有擦伤，正在抢修当中。")
                    .setImageResource(R.id.item_notify__dot, R.mipmap.weather_notify_orange)
                    .setBackgroundRes(R.id.item_notify__time, R.drawable.bg_rectangle_orange_radius_10);
        }
    }
}
