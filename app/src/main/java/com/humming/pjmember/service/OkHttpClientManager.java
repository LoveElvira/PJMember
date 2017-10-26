package com.humming.pjmember.service;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.google.gson.Gson;
import com.humming.pjmember.activity.LoginActivity;
import com.humming.pjmember.base.Application;
import com.humming.pjmember.base.Config;
import com.humming.pjmember.base.Constant;
import com.humming.pjmember.utils.SharePrefUtil;

import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Elvira on 2017/6/1.
 * 网络请求 okhttp3
 */

public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    private Call mCall;
    /**
     * 请求集合: key=Activity value=Call集合
     */
    private static Map<Class<?>, List<Call>> callsMap = new ConcurrentHashMap<Class<?>, List<Call>>();
    public static final MediaType JSON = MediaType.parse("application/json;CharSet=UTF-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
//                .cookieJar(new CookieJar() {
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        return null;
//                    }
//                })
                .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                .writeTimeout(60, TimeUnit.SECONDS)//设置写的超时时间
                .readTimeout(100, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存请求集合
     */
    private void putCall(Class<?> cls, Call call) {
        if (null != cls) {
            List<Call> callList = callsMap.get(cls);
            if (null == callList) {
                callList = new LinkedList<Call>();
                callList.add(call);
                callsMap.put(cls, callList);
            } else {
                callList.add(call);
            }
        }
    }

    //异步请求 返回类
    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Class<?> cls) {
        getInstance()._postAsyn(url, callback, requestMainDataData, resultVOClass, cls);
    }

    //上传图片或者视频
    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final TypeReference typeReference, Class<?> cls) {
        getInstance()._postAsyn(url, callback, requestMainDataData, files, typeReference, cls);
    }

    //异步请求 类(返回普通类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass, Class<?> cls) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, Constant.TOKEN, "", Application.getInstance().getCurrentActivity());
        String deviceId = SharePrefUtil.getString(Constant.FILE_NAME, "deviceId", "", Application.getInstance().getCurrentActivity());

        final RequestData requestData = new RequestData();
        if (!"".equals(token)) {
            requestData.setToken(token);
        }
        if (!"".equals(deviceId)) {
            requestData.setAppPushToken(deviceId);
        }
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        requestData.setAppVersion(Constant.AppVersion);
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        String url = Config.URL_SERVICE;
        Request request = buildPostRequest(url, requestData);
        mCall = mOkHttpClient.newCall(request);
        putCall(cls, mCall);
        deliveryResult(callback, request, resultVOClass);
    }

    //异步请求 类(返回普通类) 上传文件专用
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final List<File> files, final TypeReference typeReference, Class<?> cls) {
        String token = SharePrefUtil.getString(Constant.FILE_NAME, Constant.TOKEN, "", Application.getInstance().getCurrentActivity());
        String json = mGson.toJson(requestMainDataData);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单形式
        builder.addFormDataPart("cmd", cmd);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("parameters", json);
        for (int i = 0; i < files.size(); i++) {
            builder.addFormDataPart("files", files.get(i).getName(), RequestBody.create(null, files.get(i)));
            Log.i("ee", "files----" + files.get(i).getName());
//            builder.addFormDataPart("files", "pjmember" + i + ".jpg", RequestBody.create(null, files.get(i)));
        }
        String url = Config.URL_SERVICE_UPLOAD;
        RequestBody requestBody = builder.build();
        Log.e("ee", "buildPostRequest-----" + new Request.Builder().url(url).post(requestBody).build());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mCall = mOkHttpClient.newCall(request);
        putCall(cls, mCall);
        deliveryResult(callback, request, typeReference);
    }

    private <T> Request buildPostRequest(String url, RequestData requestData) {

        String json = mGson.toJson(requestData);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Log.e("ee", "url-----" + url);
        Log.v("xxxxxx", json);
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    //异步请求返回 处理
    private <T> void deliveryResult(final ResultCallback callback, final Request request, final Class<T> resultVOClass) {
        Log.i("ee", "---------------" + mCall.toString());
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("ee", "-------111--------" + call.toString());
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.i("ee", "---------------" + response.toString());

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        if (resultVOClass == ResponseData.class) {//只判断状态(只确定返回是否成功)
                            ResponseData responseData = new ResponseData();
                            responseData.setStatusCode(0);
                            sendSuccessResultCallback(responseData, callback);
                        } else {
                            JsonNode responseNode = node.get("response");
                            T data = mapper.treeToValue(responseNode, resultVOClass);
                            sendSuccessResultCallback(data, callback);
                        }
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }

        });
    }

    //上传图片和视频
    private <T> void deliveryResult(final ResultCallback callback, final Request request, final TypeReference typeReference) {
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    JsonNode node = mapper.readValue(string, JsonNode.class);

                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        T result = mapper.readValue(responseNode.toString(), typeReference);
                        sendSuccessResultCallback(result, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        if (401 == statusCode) {
                            showExitDialog(error.getInfo().toString());
                        }
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onOtherError(request, e);
            }
        });
    }

    private void sendErrorStringCallback(final Request request, final Error e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    public static abstract class ResultCallback<T> {

        public abstract void onError(Request request, Error info);

        public abstract void onResponse(T response);

        public abstract void onOtherError(Request request, Exception exception);
    }

    /**
     * 取消请求
     */
    public static void cancelCall(Class<?> cls) {
        List<Call> callList = callsMap.get(cls);
        if (null != callList) {
            for (Call call : callList) {
                if (!call.isCanceled())
                    call.cancel();
            }
            callsMap.remove(cls);
        }
    }

    private void showExitDialog(final String info) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builer = new AlertDialog.Builder(Application.getInstance().getCurrentActivity());
                // builer.setTitle(versionInfo.getAndrTitle());
                //  builer.setMessage(versionInfo.getAndrDescription().toString());
                // builer.setTitle(R.string.sure_log_out);
                builer.setMessage(info);
                builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Application.getInstance().finishAllActivity();
                        SharePrefUtil.putString(Constant.FILE_NAME, "token", "", Application.getInstance().getCurrentActivity());
                        SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, "", Application.getInstance().getCurrentActivity());
                        Intent intent = new Intent(Application.getInstance().getApplicationContext(), LoginActivity.class);
                        Application.getInstance().getCurrentActivity().startActivityForResult(intent, Constant.CODE_LOGIN);
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builer.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

}
