package com.humming.pjmember.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.base.AppManager;
import com.pjqs.dto.certificate.CertificateBean;
import com.pjqs.dto.contract.ContractInfoBean;

import java.util.List;

/**
 * Created by Elvira on 2017/12/7.
 * 相关证书信息
 */

public class CertificateAdapter extends BaseQuickAdapter<CertificateBean, BaseViewHolder> {

    private int width = 0;
    private Context context;

    public CertificateAdapter(List<CertificateBean> data, Context context) {
        super(R.layout.item_certificate, data);
        this.context = context;
        AppManager.getInstance().initWidthHeight(this.context);
        width = (AppManager.getInstance().getWidth() - 24) / 4;
    }

    @Override
    protected void convert(BaseViewHolder helper, CertificateBean item) {

        helper.addOnClickListener(R.id.item_certificate__image)
                .setText(R.id.item_certificate__name, item.getCertificateName());

        helper.getConvertView().measure(0, 0);
        ViewGroup.LayoutParams params = helper.getConvertView().getLayoutParams();
//        params.width = width;
        params.height = width - 10;
        helper.getConvertView().setLayoutParams(params);

        ImageView image = helper.getView(R.id.item_certificate__image);

        Glide.with(helper.getConvertView().getContext())
                .load(item.getAccessory())
                .into(image);


    }
}
