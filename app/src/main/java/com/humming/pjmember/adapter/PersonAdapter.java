package com.humming.pjmember.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.pjqs.dto.user.UserInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/9/22.
 */

public class PersonAdapter extends BaseQuickAdapter<UserInfoBean, BaseViewHolder> {

    public PersonAdapter(@Nullable List<UserInfoBean> data) {
        super(R.layout.item_plan_person, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBean item) {
        helper.setText(R.id.item_plan_peoson__num, item.getUserCode())
                .setText(R.id.item_plan_peoson__name, item.getName())
                .setText(R.id.item_plan_peoson__post, item.getWorkPlace())
                .setText(R.id.item_plan_peoson__type, item.getJobType());

        if ("1".equals(item.getSex())) {
            helper.setImageResource(R.id.item_plan_peoson__sex, R.mipmap.sex_boy_small);
        } else if ("2".equals(item.getSex())) {
            helper.setImageResource(R.id.item_plan_peoson__sex, R.mipmap.sex_girl_small);
        }
    }
}
