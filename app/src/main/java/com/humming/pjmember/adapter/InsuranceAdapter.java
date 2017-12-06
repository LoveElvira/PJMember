package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.equipment.EquipmentInsuranceInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/12/6.
 */

public class InsuranceAdapter extends BaseQuickAdapter<EquipmentInsuranceInfo, BaseViewHolder> {

    public InsuranceAdapter(@Nullable List<EquipmentInsuranceInfo> data) {
        super(R.layout.item_log, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipmentInsuranceInfo item) {
        helper.setText(R.id.item_log__time, item.getStartTime())
                .setText(R.id.item_log__content, initHtml("保险名称", item.getInsuranceName()))
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
