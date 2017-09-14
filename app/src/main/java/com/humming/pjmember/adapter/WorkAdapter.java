package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;

import java.util.List;

/**
 * Created by Elvira on 2017/9/1.
 */

public class WorkAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public WorkAdapter(@Nullable List<String> data) {
        super(R.layout.item_work, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        String str = "<font color='#ADADAD'>" + "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。" + "</font>";

        helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_plan)
                .addOnClickListener(R.id.item_work__parent)
                .setText(R.id.item_work__content, Html.fromHtml("<font color='#888888'>" + "今日作业：" + "</font>"
                        + str));
        if (item.equals("1")) {
            helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_defect)
                    .setText(R.id.item_work__content, Html.fromHtml("<font color='#888888'>" + "缺陷描述：" + "</font>"
                            + str));
        } else if (item.equals("2")) {
            helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_repair)
                    .setText(R.id.item_work__content, Html.fromHtml("<font color='#888888'>" + "缺陷描述：" + "</font>"
                            + str));
        }
    }
}
