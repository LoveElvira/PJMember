package com.humming.pjmember.activity.affair.more;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.BaseActivity;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.content.affair.dispatch.InDispatchContent;
import com.humming.pjmember.content.affair.dispatch.OutDispatchContent;
import com.humming.pjmember.viewutils.BaseViewPager;
import com.humming.pjmember.viewutils.ContentAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2018/2/6.
 * 收发文
 */

public class DispatchActivity extends BaseActivity {

    private BaseViewPager viewPager;
    private ContentAdapter adapter;
    private TabLayout tab;
    private int positions = 0;
    private final String[] titles = {"收入文", "发出文"};

    private InDispatchContent inDispatchContent;
    private OutDispatchContent outDispatchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        leftArrow = findViewById(R.id.activity_work__back);
        tab = findViewById(R.id.activity_work__tab);
        viewPager = findViewById(R.id.activity_work__viewpager);
        initTabDate();
        leftArrow.setOnClickListener(this);
    }

    private void initTabDate() {

        inDispatchContent = new InDispatchContent(this);
        outDispatchContent = new OutDispatchContent(this);
        List<View> list = new ArrayList<>();
        list.add(inDispatchContent);
        list.add(outDispatchContent);

        adapter = new ContentAdapter(list, titles);
        viewPager.setEnabled(true);
        viewPager.setAdapter(adapter);

        tab.post(new Runnable() {
            @Override
            public void run() {
                //拿到tabLayout的mTabStrip属性
                Field mTabStripField = null;
                try {
                    mTabStripField = tab.getClass().getDeclaredField("mTabStrip");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = null;
                try {
                    mTabStrip = (LinearLayout) mTabStripField.get(tab);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                float scale = Resources.getSystem().getDisplayMetrics().density;
                int dp10 = (int) (15 * scale + 0.5f);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性
                    Field mTextViewField = null;
                    try {
                        mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    mTextViewField.setAccessible(true);

                    TextView mTextView = null;
                    try {
                        mTextView = (TextView) mTextViewField.get(tabView);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    tabView.setPadding(0, 0, 0, 0);

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }
            }
        });
        tab.setupWithViewPager(viewPager);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                positions = tab.getPosition();
                switch (positions) {
                    case 0:
                        inDispatchContent.isInitFirst();
                        break;
                    case 1:
                        outDispatchContent.isInitFirst();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        inDispatchContent.isInitFirst();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Constant.CODE_RESULT)
            return;
        int position = data.getIntExtra("position", -1);
        switch (requestCode) {
            case Constant.CODE_REQUEST_ONE:
                inDispatchContent.updateData();
                break;
            case Constant.CODE_REQUEST_TWO:
                outDispatchContent.updateData();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_work__back:
                DispatchActivity.this.finish();
                break;
        }
    }
}
