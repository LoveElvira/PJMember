package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.work.DefictBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class DefectAdapter extends BaseQuickAdapter<DefictBean, BaseViewHolder> {
    public DefectAdapter(@Nullable List<DefictBean> data) {
        super(R.layout.item_defect, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DefictBean item) {
        String str = "设备机器损坏，运转不灵记录文字，设备机器损坏，运转不灵。";

        helper.addOnClickListener(R.id.item_defect__parent)
                .setText(R.id.item_defect__time, item.getCrtTime())
                .setText(R.id.item_defect__facility_name, item.getWorkName())
                .setText(R.id.item_defect__address, item.getLocation())
                .setText(R.id.item_defect__content, initHtml("缺陷描述", item.getRemark()));
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
