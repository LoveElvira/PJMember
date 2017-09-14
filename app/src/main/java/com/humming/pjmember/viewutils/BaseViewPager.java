package com.humming.pjmember.viewutils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Elvira on 2017/8/31.
 */

public class BaseViewPager extends ViewPager {
    private boolean enabled = false;

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(arg0);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (this.enabled) {
            return super.onTouchEvent(arg0);
        }
        return false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 切换不平移
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
