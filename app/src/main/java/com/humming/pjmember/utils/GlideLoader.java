package com.humming.pjmember.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.viewutils.selectpic.ImageLoader;

/**
 * Created by Elvira on 2016/12/17.
 * 社区发布  加载本地图片
 */
public class GlideLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Log.i("ee", path);
        Glide.with(context)
                .load(path)
                .into(imageView);

//        .placeholder(R.mipmap.image_loading).centerCrop()
    }
}
