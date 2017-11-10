package com.humming.pjmember.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.humming.pjmember.R;
import com.humming.pjmember.activity.BrowseImageViewActivity;
import com.humming.pjmember.viewutils.zoom.PhotoView;
import com.humming.pjmember.viewutils.zoom.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/5/13.
 */
public class BrowseImageAdapter extends PagerAdapter {
    private List<View> allImageView;
    private Context context;
    private List<String> imageUrl = new ArrayList<String>();


    public BrowseImageAdapter(Context context, List<View> allImageView,
                              List<String> allImageUrl) {
        this.context = context;
        this.allImageView = allImageView;
        this.imageUrl = allImageUrl;
    }

    /**
     * 获取滑动控件的数量
     */
    @Override
    public int getCount() {
        if (allImageView != null && allImageView.size() > 0) {
            return allImageView.size();
        } else {
            return 0;
        }

    }

    /**
     * 判断显示的是否同一张图片
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    /**
     * 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
     */
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    /**
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行网络初始化
     */
    @Override
    public Object instantiateItem(ViewGroup view, int position) {


        View v = allImageView.get(position);

        ImageView click = v.findViewById(R.id.item_browse_click);
        PhotoView imageView = v
                .findViewById(R.id.item_browse_image);
        Glide.with(v.getContext())
                .load(imageUrl.get(position).toString().trim())
//                .placeholder(R.mipmap.image_loading)
                .into(imageView);

        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                BrowseImageViewActivity.finsh();
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrowseImageViewActivity.finsh();
            }
        });
        view.addView(allImageView.get(position));
        return allImageView.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
