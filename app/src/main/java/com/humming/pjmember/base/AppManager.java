package com.humming.pjmember.base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Elvira on 2017/5/31.
 * 获取当前手机的屏幕宽高
 */

public class AppManager {
    private static AppManager mAppManager = null;
    /*屏幕的宽*/
    private static int width = 0;
    /*屏幕的高*/
    private static int height = 0;

    public static AppManager getInstance() {
        if (null == mAppManager) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 初始化屏幕宽高;
     */
    public AppManager initWidthHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        return getInstance();
    }

    /**
     * 获取屏幕宽度;
     */
    public int getWidth() {
        return width;
    }

    /**
     * 获取屏幕高度;
     */
    public int getHeight() {
        return height;
    }
}
