package com.humming.pjmember.activity.work;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.pjmember.R;
import com.humming.pjmember.base.AppManager;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.requestdate.RequestParameter;
import com.humming.pjmember.service.Error;
import com.humming.pjmember.service.OkHttpClientManager;
import com.humming.pjmember.utils.DeviceUtils;
import com.humming.pjmember.utils.SharePrefUtil;
import com.humming.pjmember.viewutils.ObservableScrollView;
import com.humming.pjmember.viewutils.ProgressHUD;
import com.pjqs.dto.equipment.Response;
import com.pjqs.dto.work.WorkBean;

import okhttp3.Request;

/**
 * Created by Elvira on 2017/9/12.
 * 安全交底内容
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class WorkSafetyDisclosureActivity extends BaseWorkActivity implements ObservableScrollView.ScrollViewListener {

    //提示
    private TextView tip;
    //交底内容
    private TextView content;
    //交底Scrollview
    private ObservableScrollView scrollView;
    //确认按钮
    private TextView confirm;

    private LinearLayout scrollViewParent;

    private boolean isScrollBottom = false;

    private int scrollViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_safety_disclosure);
        AppManager.getInstance().initWidthHeight(this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        userId = SharePrefUtil.getString(Constant.FILE_NAME, Constant.USER_ID, "", WorkSafetyDisclosureActivity.this);
        position = getIntent().getIntExtra("position", 0);

        title = (TextView) findViewById(R.id.base_toolbar__title);
        title.setText("安全交底");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setImageResource(R.mipmap.left_arrow);

        tip = (TextView) findViewById(R.id.activity_work_safety__tip);
        scrollView = (ObservableScrollView) findViewById(R.id.activity_work_safety__scrollview);
        content = (TextView) findViewById(R.id.activity_work_safety__content);
        confirm = (TextView) findViewById(R.id.activity_work_safety__confirm);
        scrollViewParent = (LinearLayout) findViewById(R.id.activity_work_safety__scrollview_parent);
        confirm.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_rectangle_gray_radius_3));

        if (getIntent().getBooleanExtra("isLook", false)) {
            tip.setText("请认真阅读安全交底内容");
            confirm.setVisibility(View.VISIBLE);
        } else {
            tip.setText("安全交底内容：");
            confirm.setVisibility(View.GONE);
        }

        title.measure(0, 0);
        confirm.measure(0, 0);
        scrollViewParent.measure(0, 0);
        tip.measure(0, 0);
        scrollViewHeight = AppManager.getInstance().getHeight()
                - DeviceUtils.dip2px(this, title.getHeight())
                - DeviceUtils.dip2px(this, confirm.getHeight()) - DeviceUtils.dip2px(this, confirm.getPaddingBottom()) - DeviceUtils.dip2px(this, confirm.getPaddingTop())
                - DeviceUtils.dip2px(this, scrollViewParent.getPaddingBottom()) - DeviceUtils.dip2px(this, scrollViewParent.getPaddingTop())
                - DeviceUtils.dip2px(this, tip.getPaddingBottom()) - DeviceUtils.dip2px(this, 50);

        leftArrow.setOnClickListener(this);
        confirm.setOnClickListener(this);
        scrollView.setScrollViewListener(this);
        initHandler();
        if (workBean != null) {
            initData();
        } else {
            getWorkDetails();
        }
    }

    private void initHandler() {
        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.CODE_SUCCESS:
                        workBean = (WorkBean) msg.obj;
                        initData();
                        break;
                }
            }
        };
    }

    private void getWorkDetails() {
        progressHUD = ProgressHUD.show(WorkSafetyDisclosureActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });
        getWorkDetails(handler);
    }


    private void initData() {
        if (workBean != null) {
            content.setText(workBean.getSafetyContent());

            //measure方法的参数值都设为0即可
            content.measure(0, 0);
            //获取组件高度
            int contentHeight = content.getMeasuredHeight();

            Log.i("ee", contentHeight + "------" + scrollViewHeight);
            if (contentHeight <= scrollViewHeight) {
                isScrollBottom = true;
                confirm.setText("确认交底");
                confirm.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_rectangle_green_radius_3));
            }
        }
    }

    //确认安全交底
    private void confirmSafety() {

        progressHUD = ProgressHUD.show(WorkSafetyDisclosureActivity.this, getResources().getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressHUD.dismiss();
            }
        });

        RequestParameter parameter = new RequestParameter();
        parameter.setWorkId(id);
        parameter.setUserId(userId);
        OkHttpClientManager.postAsyn(Config.CONFIRM_SAFETY, new OkHttpClientManager.ResultCallback<Response>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("onError", info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                progressHUD.dismiss();
            }

            @Override
            public void onResponse(Response response) {
                progressHUD.dismiss();
                if (response != null) {
                    if ("1".equals(response.getCode())) {
                        startActivity(new Intent(WorkSafetyDisclosureActivity.this, WorkPlanActivity.class)
                                .putExtra("id", id)
                                .putExtra("position", position));
                        WorkSafetyDisclosureActivity.this.finish();
                    } else {
                        showShortToast(response.getMsg());
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("onError", exception.toString());
                progressHUD.dismiss();
            }
        }, parameter, Response.class, WorkSafetyDisclosureActivity.class);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.base_toolbar__left_image:
                setResult(Constant.CODE_RESULT, new Intent()
                        .putExtra("roadWorkState", workBean.getRoadWorkState())
                        .putExtra("roadWork", workBean.getRoadWork())
                        .putExtra("isSafety", workBean.getIsSafety())
                        .putExtra("position", position));
                WorkSafetyDisclosureActivity.this.finish();
                break;
            case R.id.activity_work_safety__confirm:
                if (isScrollBottom) {
                    confirmSafety();
                } else {
                    showShortToast("请认真阅读安全交底内容");
                }
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView.getScrollY() + scrollView.getHeight() /*- scrollView.getPaddingTop() - scrollView.getPaddingBottom()*/ == scrollView.getChildAt(0).getHeight()) {
            isScrollBottom = true;
            // 小心踩坑2: 这里不能是 >=
            // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
            confirm.setText("确认交底");
            confirm.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_rectangle_green_radius_3));
        }
    }
}
