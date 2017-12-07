package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.project.ProjectAccessoryCheckInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/12/6.
 */

public class ProjectFileAdapter extends BaseQuickAdapter<ProjectAccessoryCheckInfoBean, BaseViewHolder> {

    public ProjectFileAdapter(@Nullable List<ProjectAccessoryCheckInfoBean> data) {
        super(R.layout.item_affair, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectAccessoryCheckInfoBean item) {
        helper.setText(R.id.item_affair__title, item.getProjectName())
                .setText(R.id.item_affair__people_title, "创建人：")
                .setText(R.id.item_affair__people_name, item.getCrtUserName())
                .setText(R.id.item_affair__time, item.getTime())
                .addOnClickListener(R.id.item_affair__parent);

    }
}
