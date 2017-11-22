package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.conference.ConferenceInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/7.
 */

public class MeetingAdapter extends BaseQuickAdapter<ConferenceInfoBean, BaseViewHolder> {
    public MeetingAdapter(@Nullable List<ConferenceInfoBean> data) {
        super(R.layout.item_meeting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConferenceInfoBean item) {
        helper.setText(R.id.item_meeting__people, initHtml("人员", item.getUsers()))
                .setText(R.id.item_meeting__time, item.getStartTime() + "~" + item.getEndTime())
                .setText(R.id.item_meeting__title, item.getConferenceName())
                .setText(R.id.item_meeting__address, item.getSite())
                .addOnClickListener(R.id.item_meeting__parent);
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
