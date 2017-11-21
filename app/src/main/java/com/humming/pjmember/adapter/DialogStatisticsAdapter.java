package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.utils.StringUtils;
import com.pjqs.dto.statistics.StatisticsCostTypeBean;

import java.util.List;

/**
 * Created by Elvira on 2017/11/21.
 */

public class DialogStatisticsAdapter extends BaseQuickAdapter<StatisticsCostTypeBean, BaseViewHolder> {

    int color = 0;

    public DialogStatisticsAdapter(@Nullable List<StatisticsCostTypeBean> data, int color) {
        super(R.layout.item_dialog_statistics, data);
        this.color = color;
    }

    @Override
    protected void convert(BaseViewHolder helper, StatisticsCostTypeBean item) {
        helper.setText(R.id.item_dialog_statistics__name, item.getType())
                .setText(R.id.item_dialog_statistics__num, StringUtils.saveTwoDecimal(item.getCost()))
                .setBackgroundColor(R.id.item_dialog_statistics__image, color);

    }
}
