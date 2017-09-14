package com.humming.pjmember.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.pjmember.utils.NetWorkUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Elvira on 2017/5/31.
 */

public class BaseLinearLayout extends LinearLayout implements View.OnClickListener {
    /*无网络*/
    protected LinearLayout noWifiLayout;
    /*网络类型*/
    private int netMobile;
    /*界面*/
    protected View view;
    //头部 左边的箭头
    protected ImageView leftArrow;
    protected LinearLayout titleLayout;
    //头部 中间的文字
    protected TextView title;
    //头部 中间的图片
    protected ImageView titleImage;
    //头部 左边的文字
    protected TextView rightText;
    //头部 右边的图标
    protected ImageView rightImage;

    //刷新
    protected SwipeRefreshLayout refresh;
    //列表
    protected RecyclerView listView;

    private Activity activity;

    /*软键盘管理者*/
    protected InputMethodManager imm;


    public BaseLinearLayout(Context context) {
        this(context, null);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = Application.getInstance().getCurrentActivity();
        imm = (InputMethodManager) this.activity.getSystemService(INPUT_METHOD_SERVICE);
//        initView();
    }

    protected void initView() {

    }

    /**
     * 短暂显示Toast提示(来自String)
     */
    protected void showShortToast(String msg) {
        Toast toast = Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity) {
        Intent intent = new Intent(this.activity, activity);
        this.activity.startActivity(intent);
    }

    protected void startActivity(Intent intent) {
        this.activity.startActivity(intent);
    }

    /**
     * 初始化时判断有没有网络
     */
    protected boolean inspectNet() {
        this.netMobile = NetWorkUtils.getNetWorkState(Application.getInstance().getCurrentActivity());
        return isNetConnect();
    }

    protected boolean isNetConnect() {
        if (netMobile == 1) {//连接wifi
            return true;
        } else if (netMobile == 0) {//连接移动数据
            return true;
        } else if (netMobile == -1) {//当前没有网络
            return false;

        }
        return false;
    }

    protected String initHtml(String header, String footer) {
        String str = "<font color='#888888'>" + header + "：" + "</font>"
                + "<font color='#ADADAD'>" + footer + "</font>";
        return str;
    }

    @Override
    public void onClick(View v) {

    }
}
