package com.humming.pjmember.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class ImageAdapter extends BaseQuickAdapter<Map<String, String>, BaseViewHolder> {
    private int width = 0;
    private Context context;
    private int type;// 1 ：正常 2 ：换图片 3 ：文字

    public ImageAdapter(List<Map<String, String>> data, Context context, int type) {
        super(R.layout.item_image, data);
        this.context = context;
        this.type = type;
        AppManager.getInstance().initWidthHeight(this.context);
        width = (AppManager.getInstance().getWidth() - 24) / 4;
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, String> item) {

        helper.addOnClickListener(R.id.item_image__image_bg)
                .addOnClickListener(R.id.item_image__image)
                .setImageResource(R.id.item_image__add, R.mipmap.add)
                .setVisible(R.id.item_image__text, false)
                .setVisible(R.id.item_image__add, true)
                .setVisible(R.id.item_image__image, false)
                /*.setImageResource(R.id.item_publish_image__image, R.drawable.bg_border_gray)*/;

        if (type == 2) {
            helper.setImageResource(R.id.item_image__add, R.mipmap.take_photo);
        } else if (type == 3) {
            helper.setVisible(R.id.item_image__text, true)
                    .setVisible(R.id.item_image__add, false);
        }

        helper.getConvertView().measure(0, 0);
        ViewGroup.LayoutParams params = helper.getConvertView().getLayoutParams();
//        params.width = width;
        params.height = width - 10;
        helper.getConvertView().setLayoutParams(params);

        if (item.get("isAdd").equals("1"))
            return;

        Log.e("ee", "--" + item.get("imagePath"));
        helper.setVisible(R.id.item_image__add, false);
        ImageView image = helper.getView(R.id.item_image__image);
        image.setVisibility(View.VISIBLE);
        try {
            image.setImageBitmap(BitmapUtils.revitionImageSize(item.get("imagePath")));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
