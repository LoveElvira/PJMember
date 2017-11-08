package com.humming.pjmember.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.humming.pjmember.R;
import com.humming.pjmember.base.AppManager;
import com.humming.pjmember.utils.BitmapUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2017/9/5.
 */

public class ImageLookAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int width = 0;
    private Context context;

    public ImageLookAdapter(List<String> data, Context context) {
        super(R.layout.item_image, data);
        this.context = context;
        AppManager.getInstance().initWidthHeight(this.context);
        width = (AppManager.getInstance().getWidth() - 24) / 4;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.addOnClickListener(R.id.item_image__image_bg)
                .addOnClickListener(R.id.item_image__image)
                .setImageResource(R.id.item_image__add, R.mipmap.add)
                .setVisible(R.id.item_image__text, false)
                .setVisible(R.id.item_image__add, false)
                .setVisible(R.id.item_image__image, true)
                /*.setImageResource(R.id.item_publish_image__image, R.drawable.bg_border_gray)*/;

        helper.getConvertView().measure(0, 0);
        ViewGroup.LayoutParams params = helper.getConvertView().getLayoutParams();
//        params.width = width;
        params.height = width - 10;
        helper.getConvertView().setLayoutParams(params);

        ImageView image = helper.getView(R.id.item_image__image);

        Glide.with(helper.getConvertView().getContext())
                .load(item)
                .into(image);


    }
}
