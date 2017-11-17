package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.contract.ContractInfoBean;
import com.pjqs.dto.project.ProjectInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/4.
 */

public class ProjectAdapter extends BaseQuickAdapter<ProjectInfoBean, BaseViewHolder> {

    private int type;//1 合同

    public ProjectAdapter(@Nullable List<ProjectInfoBean> data, int type) {
        super(R.layout.item_affair, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectInfoBean item) {
        helper.setText(R.id.item_affair__title, item.getProName())
                .setText(R.id.item_affair__people_name, item.getCrtUserName())
                .setText(R.id.item_affair__time, item.getTime())
                .addOnClickListener(R.id.item_affair__parent);

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
