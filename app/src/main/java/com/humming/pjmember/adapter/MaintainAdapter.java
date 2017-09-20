package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.equipment.EquipmentMaintainInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class MaintainAdapter extends BaseQuickAdapter<EquipmentMaintainInfoBean, BaseViewHolder> {

    public MaintainAdapter(@Nullable List<EquipmentMaintainInfoBean> data) {
        super(R.layout.item_log, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentMaintainInfoBean item) {
//        String str = "<font color='#ADADAD'>" + "设备机器损坏，运转不灵记录文字，设备机器损坏，运转不灵。" + "</font>";

        helper.setText(R.id.item_log__time, item.getMaintainTime())
                .setText(R.id.item_log__content, initHtml("保养内容", item.getContent()))
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
