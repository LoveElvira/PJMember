package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.project.ScienceGradeBean;

import java.util.List;

/**
 * Created by Elvira on 2018/2/6.
 */

public class ScientificResearchAdapter extends BaseQuickAdapter<ScienceGradeBean, BaseViewHolder> {


    public ScientificResearchAdapter(@Nullable List<ScienceGradeBean> data) {
        super(R.layout.item_affair, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScienceGradeBean item) {
        helper.setText(R.id.item_affair__title, item.getProjectName())
                .setText(R.id.item_affair__people_name, item.getUserName())
                .setText(R.id.item_affair__time, item.getDate())
                .setVisible(R.id.item_affair__comment, false)
                .addOnClickListener(R.id.item_affair__parent)
                .setImageResource(R.id.item_affair__image, R.mipmap.affair_project_small);
    }
}
