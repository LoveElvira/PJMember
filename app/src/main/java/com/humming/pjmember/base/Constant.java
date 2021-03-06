package com.humming.pjmember.base;

import android.os.Build;

/**
 * Created by Elvira on 2017/6/1.
 */

public class Constant {
    public static final String NICKNAME = "nickname";
    public static final String HEADURL = "headurl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    public static final String POSITION = "positon";//职位
    public static final String COMPANY = "company";//所属公司
    public static final String TOKEN = "token";
    public static final String APP_VERSION = "app_version";
    public static final String DEVICE_TYPE = "android";
    public static final String ACCIDENT_TYPE = "accidentType";
    public static final String FILE_NAME = "Humming_PJMember";
    public static final String AUTHORITY = "com.humming.pjmember.fileprovider";
    //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
    public static final String AppVersion = Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID;

    public static final int CODE_SUCCESS = 0X03;
    public static final int CODE_REQUEST_ONE = 0X04;
    public static final int CODE_REQUEST_TWO = 0X05;
    public static final int CODE_REQUEST_THREE = 0X06;
    public static final int CODE_REQUEST_FOUR = 0X07;
    public static final int CODE_REQUEST_FIVE = 0X08;
    public static final int CODE_REQUEST_SIX = 0X09;
    public static final int CODE_LOGIN = 0X98;
    public static final int CODE_RESULT = 0X99;//结果值


}
