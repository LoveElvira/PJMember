package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.work.WorkInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/1.
 */

public class WorkAdapter extends BaseQuickAdapter<WorkInfoBean, BaseViewHolder> {
    public WorkAdapter(@Nullable List<WorkInfoBean> data) {
        super(R.layout.item_work, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkInfoBean item) {
//        String str = "<font color='#ADADAD'>" + "S26各收费站机点供电系统、收费系统、监控系统、通信系统保修故障维修。" + "</font>";

        helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_plan)
                .addOnClickListener(R.id.item_work__parent)
                .setText(R.id.item_work__time, item.getWorkStartDate())
                .setText(R.id.item_work__facility_name, item.getFacilityName())
                .setText(R.id.item_work__content, initHtml("今日作业", item.getWorkContent()));

        if (item.getWorkNature() == 2) {//缺陷作业
            helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_defect)
                    .setText(R.id.item_work__content, initHtml("缺陷描述", item.getWorkContent()));

        } else if (item.getWorkNature() == 3) {//抢修作业
            helper.setImageResource(R.id.item_work__tip, R.mipmap.work_tip_repair)
                    .setText(R.id.item_work__content, initHtml("缺陷描述", item.getWorkContent()));
        }
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
