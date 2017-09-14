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

public class DefectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DefectAdapter(@Nullable List<String> data) {
        super(R.layout.item_defect, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        String str = "<font color='#ADADAD'>" + "设备机器损坏，运转不灵记录文字，设备机器损坏，运转不灵。" + "</font>";

        helper.addOnClickListener(R.id.item_defect__parent)
                .setText(R.id.item_defect__content, Html.fromHtml("<font color='#888888'>" + "缺陷描述：" + "</font>"
                + str));
    }
}
