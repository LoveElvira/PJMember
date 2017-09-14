package com.humming.pjmember.viewutils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Elvira on 2017/8/31.
 * 存放tab的页面
 */

public class ContentAdapter extends PagerAdapter {

    private final List<View> listView;
    private final String[] titles;

    public ContentAdapter(List<View> viewList, String[] titles) {
        super();
        this.listView = viewList;
        this.titles = titles;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = listView.get(position);
        collection.addView(view, 0);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) listView.get(position));
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
