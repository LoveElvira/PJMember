package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.equipment.EquipmentRunnintBean;

import java.util.List;

/**
 * Created by Elvira on 2017/12/6.
 */

public class RunningAdapter extends BaseQuickAdapter<EquipmentRunnintBean, BaseViewHolder> {

    public RunningAdapter(@Nullable List<EquipmentRunnintBean> data) {
        super(R.layout.item_running, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentRunnintBean item) {
        helper.setText(R.id.item_running__time, item.getDrawTime() + "~" + item.getDrawEndTime())
                .setText(R.id.item_running__mileage, "公里数(KM)：" + item.getMileage())
                .setText(R.id.item_running__remark, initHtml("备注", item.getRemark()))
                .addOnClickListener(R.id.item_log__parent);
    }

    private CharSequence initHtml(String header, String footer) {
        String str = "<font color='#888888'>" + header + "：" + "</font>"
                + "<font color='#ADADAD'>" + footer + "</font>";
        CharSequence charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(str);
        }
        return charSequence;
    }

}
