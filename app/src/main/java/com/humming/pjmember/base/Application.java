package com.humming.pjmember.base;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baidu.mapapi.SDKInitializer;
import com.humming.pjmember.service.LocationService;

import java.util.Stack;

/**
 * Created by Elvira on 2017/5/31.
 */

public class Application extends android.app.Application {

    private static Application sInstance;
    /*当前activity*/
    private Activity currentActivity;
    /*管理当前打开的所有activity*/
    private static Stack<Activity> activityStack;
    public CloudPushService pushService;
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Hiragino_Sans_GB_W3.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        /**
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.getCoordType();

        initCloudChannel(this);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
//        MobclickAgent.setScenarioType(applicationContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("push", "init cloudchannel success");

            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e("push", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
        // 初始化小米通道，自动判断是否支持小米系统推送，如不支持会跳过注册
//        MiPushRegister.register(applicationContext, "小米AppID", "小米AppKey");
        // 初始化华为通道，自动判断是否支持华为系统推送，如不支持会跳过注册
//        HuaWeiRegister.register(applicationContext);

    }

    public static synchronized Application getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
        addActivity(currentActivity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
