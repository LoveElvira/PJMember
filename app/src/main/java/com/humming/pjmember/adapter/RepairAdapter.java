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

public class RepairAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int type;//1 维修  2 保养  3 事故

    public RepairAdapter(@Nullable List<String> data, int type) {
        super(R.layout.item_log, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        String str = "<font color='#ADADAD'>" + "设备机器损坏，运转不灵记录文字，设备机器损坏，运转不灵。" + "</font>";

        helper.addOnClickListener(R.id.item_log__parent);

        switch (type) {
            case 1:
                helper.setText(R.id.item_log__content, Html.fromHtml("<font color='#888888'>" + "维修内容：" + "</font>"
                        + str));
                break;
            case 2:
                helper.setText(R.id.item_log__content, Html.fromHtml("<font color='#888888'>" + "保养内容：" + "</font>"
                        + str));
                break;
            case 3:
                helper.setText(R.id.item_log__content, Html.fromHtml("<font color='#888888'>" + "事故原因：" + "</font>"
                        + str));
                break;
        }

    }
}
