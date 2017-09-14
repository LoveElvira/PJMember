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

public class AffairAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int type;//1 合同  2 项目  3 收发文

    public AffairAdapter(@Nullable List<String> data, int type) {
        super(R.layout.item_affair, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        String str = "<font color='#ADADAD'>" + "设备机器损坏，运转不灵记录文字，设备机器损坏，运转不灵。" + "</font>";

        helper.addOnClickListener(R.id.item_affair__parent);

        switch (type) {
            case 1:
                helper.setImageResource(R.id.item_affair__image, R.mipmap.affair_contract_small);
                break;
            case 2:
                helper.setImageResource(R.id.item_affair__image, R.mipmap.affair_project_small);
                break;
            case 3:
                break;
        }

    }
}
