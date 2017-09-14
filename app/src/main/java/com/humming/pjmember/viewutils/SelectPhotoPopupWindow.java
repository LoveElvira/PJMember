package com.humming.pjmember.viewutils;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.utils.UtilAnim;

/**
 * Created by Elvira on 2017/8/14.
 * 拍照  选择图片
 */

public class SelectPhotoPopupWindow {

    private LinearLayout rootView;
    private ViewGroup linearLayout;
    private TextView takePhoto;
    private TextView selectPhoto;
    private boolean isShow;

    public SelectPhotoPopupWindow() {
    }

    public void initView(LinearLayout view) {
        this.rootView = view;
        initLayout();
    }

    public void initLayout() {

        linearLayout = rootView.findViewById(R.id.popup_photo__layout);

        takePhoto = rootView
                .findViewById(R.id.popup_photo__take);
        selectPhoto = rootView
                .findViewById(R.id.popup_photo__select);

        //当点击根布局时, 隐藏
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gonePopupWindow();
            }
        });

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //如果是显示状态那么隐藏视图
                if (keyCode == KeyEvent.KEYCODE_BACK && isShow) gonePopupWindow();
                return isShow;
            }
        });

    }

    /**
     * 弹出选项弹窗
     */
    public void showPopupWindow() {
        try {

            /*捕获当前activity的布局视图, 因为我们要动态模糊, 所以这个布局一定要是最新的,
            *这样我们把模糊后的布局盖到屏幕上时, 才能让用户感觉不出来变化*/
//            Bitmap bitmap = getDrawing();
//            if (bitmap != null) {
//                bgImage.setImageBitmap(bitmap);
//                blurImageView(blurNum, bgColor);
//            } else {
//                // 获取的Bitmap为null时，用半透明代替
//                bgImage.setBackgroundColor(bgColor);
//            }

            //这里就是使用WindowManager直接将我们处理好的view添加到屏幕最前端
            rootView.setVisibility(View.VISIBLE);

            // 打开弹窗
            UtilAnim.showToUp(linearLayout, rootView);

            //视图被弹出来时得到焦点, 否则就捕获不到Touch事件
            rootView.setFocusable(true);
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.requestFocusFromTouch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewGroup getLayout() {
        return linearLayout;
    }

    public TextView getTakePhoto() {
        return takePhoto;
    }

    public TextView getSelectPhoto() {
        return selectPhoto;
    }

    /**
     * popupwindow是否是显示状态
     */
    public boolean isShow() {
        return isShow;
    }

    /**
     * 关闭弹窗
     */
    public void gonePopupWindow() {
        UtilAnim.hideToDown(linearLayout, rootView);
        rootView.setVisibility(View.GONE);
        isShow = false;
    }

}
