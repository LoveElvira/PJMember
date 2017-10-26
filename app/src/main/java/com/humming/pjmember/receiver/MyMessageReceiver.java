package com.humming.pjmember.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.humming.pjmember.activity.scan.DeviceManageActivity;
import com.humming.pjmember.activity.scan.PersonnelInfoActivity;
import com.humming.pjmember.google.zxing.activity.CaptureActivity;

import java.util.Map;

/**
 * Created by Elvira on 2017/9/25.
 * 用于接收推送的通知和消息
 */

public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // 处理推送通知
        if (null != extraMap) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG, "@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Log.i(REC_TAG, "@收到通知 && 自定义消息为空");
        }
        Log.i(REC_TAG, "收到一条推送通知 ： " + title);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(REC_TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        try {

            Log.i(REC_TAG, "收到一条推送消息 ： " + cPushMessage.getTitle());

            // 持久化推送的消息到数据库
            //new MessageDao(context).add(new MessageEntity(cPushMessage.getMessageId().substring(6, 16), Integer.valueOf(cPushMessage.getAppId()), cPushMessage.getTitle(), cPushMessage.getContent(), new SimpleDateFormat("HH:mm:ss").format(new Date())));

            // 刷新下消息列表
            //ActivityBox.CPDMainActivity.initMessageView();
        } catch (Exception e) {
            Log.i(REC_TAG, e.toString());
        }
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//        cloudPushService.setNotificationSoundFilePath();
//        1.主题界面 MarkorhomeRehomeApp://tme/xxxx
//        2.我的心愿被完成 MarkorhomeRehomeApp://mwlc/xxxx
//        3.别人送我一个礼物 MarkorhomeRehomeApp://osptm/xxxx
//        4.商品详细 MarkorhomeRehomeApp://sgd/xxxx
//        5.订单详细 MarkorhomeRehomeApp://sod/xxxx
//        6、唤醒APP：MarkorhomeRehomeApp://
//        7、进入网页：MarkorhomeRehomeApp://w?u=http://kdfjkadfjasdfasj

//        Intent intent = null;


        if (extraMap.contains("PJMember://e/")) {
            int from = "PJMember://e/".length();
            String id = extraMap.substring(from, extraMap.length());
            context.startActivity(new Intent(context, DeviceManageActivity.class)
                    .putExtra("id", id));
//                                    CaptureActivity.this.finish();
        } else if (extraMap.contains("PJMember://m/")) {//人员Id
            int from = "PJMember://m/".length();
            String id = extraMap.substring(from, extraMap.length());
            context.startActivity(new Intent(context, PersonnelInfoActivity.class)
                    .putExtra("id", "")
                    .putExtra("userId", id)
                    .putExtra("addPerson", false));
        }

//        if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/tme\\/")) {//主题界面
//            Log.i(REC_TAG, "主题界面" + StringUtils.getId(extraMap));
//
//            intent = new Intent(context, HomeMallActivity.class);
//            intent.putExtra("theme", StringUtils.getId(extraMap));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/mwlc\\/")) {//我的心愿被完成
//            Log.i(REC_TAG, "我的心愿被完成" + StringUtils.getId(extraMap));
//
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/osptm\\/")) {//别人送我一个礼物
//            Log.i(REC_TAG, "别人送我一个礼物" + StringUtils.getId(extraMap));
//
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/sgd\\/")) {//商品详细
//            Log.i(REC_TAG, "商品详细" + StringUtils.getId(extraMap));
//            intent = new Intent(context, GiftDetailActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("goodId", Long.parseLong(StringUtils.getId(extraMap)));
//            context.startActivity(intent);
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/sod\\/")) {//订单详细
//            Log.i(REC_TAG, "订单详细" + StringUtils.getId(extraMap));
//            intent = new Intent(context, MyOrderInfoActivity.class);
//            intent.putExtra("orderId", Long.parseLong(StringUtils.getId(extraMap)));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("flag", 1);
//            context.startActivity(intent);
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/")) {//唤醒APP
//            Log.i(REC_TAG, "唤醒APP" + extraMap);
//
//        } else if (extraMap.contains("MarkorhomeRehomeApp:\\/\\/w?u=")) {//进入网页
//            int from = "MarkorhomeRehomeApp://w?u=".length();
//            String url = extraMap.toString().substring(from, extraMap.length());
//
//            Log.i(REC_TAG, "进入网页" + url);
//
//            intent = new Intent(context, WebviewActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("url", url);
//            context.startActivity(intent);
//        }

        Log.i(REC_TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
    }


    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
}
