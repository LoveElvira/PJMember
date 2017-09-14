package com.humming.pjmember.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

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
    private Bitmap bitmap;


    public BrowseImageAdapter(Context context, List<View> allImageView,
                              List<String> allImageUrl) {
        this.context = context;
        this.allImageView = allImageView;
        this.imageUrl = allImageUrl;
//        for (int i=0;i<9;i++) {
//            imageUrl.add(""+i);
//        }
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
        //View v = LayoutInflater.from(context).inflate(R.layout.item_browse_image,null);
        PhotoView imageView = (PhotoView) v
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

//        imageView.setImageResource(R.drawable.p2);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        // 获取这个图片的宽和高
//        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl.get(position),
//                options); // 此时返回bm为空
//        options.inJustDecodeBounds = false;
//        // 计算缩放比
//        int be = (int) (options.outHeight / (float) 200);
//        if (be <= 0)
//            be = 1;
//        options.inSampleSize = be;
//        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
//        bitmap = BitmapFactory.decodeFile(imageUrl.get(position), options);
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        System.out.println(w + "" + h);
//        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
//        imageView.setImageBitmap(resizeBmp);
        view.addView(allImageView.get(position));
        return allImageView.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}