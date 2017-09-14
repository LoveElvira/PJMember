package com.humming.pjmember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.adapter.BrowseImageAdapter;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/5/13.
 * 浏览图片
 */
public class BrowseImageViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private ViewPager myViewPager;
    private List<View> allViewPagerView = new ArrayList<View>();
    private LayoutInflater inflater;
    private static List<String> allImageUrl;
    private ImageView delete;
    private BrowseImageAdapter adapter;
    private int currentPosition;
    private TextView pos, num;
    private static BrowseImageViewActivity context;
    private LinearLayout mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);
        context = BrowseImageViewActivity.this;
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mask = (LinearLayout) findViewById(R.id.content_browse__mask);
        myViewPager = (ViewPager) findViewById(R.id.content_browse__viewpager);
        inflater = LayoutInflater.from(BrowseImageViewActivity.this);
        delete = (ImageView) findViewById(R.id.item_browse__delete);
        delete.setVisibility(View.GONE);
        if ("isShow".equals(getIntent().getStringExtra("isShowDelete"))) {
            delete.setVisibility(View.VISIBLE);
        }

        final List<String> allImageUrl = getIntent().getStringArrayListExtra("imageUrl");
        int position = getIntent().getExtras().getInt("position");
        currentPosition = position;
        this.allImageUrl = allImageUrl;
        for (int i = 0; i < allImageUrl.size(); i++) {
            View v = inflater.inflate(R.layout.item_browse_image, null);
            allViewPagerView.add(v);
        }

        num = (TextView) findViewById(R.id.item_browse__image_num);
        num.setText("" + allImageUrl.size());
        pos = (TextView) findViewById(R.id.item_browse__image_pager);
        pos.setText("" + (currentPosition + 1));
        delete.setOnClickListener(this);
        mask.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//没有用到
            float endX;
            float endY;//没有用到

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        if (startX == endX && startY == endY) {
                            Log.i("ee", "进入了触摸");
                            Log.i("ee", "mask");
                            setResult(Constant.CODE_RESULT, new Intent().putExtra("imagePath", (Serializable) allImageUrl));
                            BrowseImageViewActivity.this.finish();
                        }
                        break;
                }
                return false;

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new BrowseImageAdapter(BrowseImageViewActivity.this, allViewPagerView, allImageUrl);
        myViewPager.setAdapter(adapter);
        myViewPager.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(currentPosition);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        currentPosition = arg0;
        pos.setText("" + (currentPosition + 1));
    }

    public static void finsh() {
        Log.i("ee", "finsh");
        context.setResult(Constant.CODE_RESULT, new Intent().putExtra("imagePath", (Serializable) allImageUrl));
        context.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_browse__delete:
                allViewPagerView.remove(currentPosition);
                allImageUrl.remove(currentPosition);
                num.setText("" + allImageUrl.size());
                adapter.notifyDataSetChanged();
                if (allImageUrl.size() == 0) {
                    setResult(Constant.CODE_RESULT, new Intent().putExtra("imagePath", (Serializable) allImageUrl));
                    BrowseImageViewActivity.this.finish();
                }
                break;
        }

    }

}
