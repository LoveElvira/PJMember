package com.humming.pjmember.base;

/**
 * Created by Elvira on 2017/6/1.
 * 网络连接路径
 */

public class Config {
    public static final String URL_SERVICE = "http://pjqs.humming-tech.com/cgi";//正式环境
    public static final String URL_SERVICE_UPLOAD = "http://pjqs.humming-tech.com/uploadFile";//上传文件

    public static final String USER_LOGIN = "newUser/newLogin";//登录
    //    public static final String USER_LOGIN = "user/login";//登录
    public static final String GET_EQUIPMENT_DETAILS = "equipment/queryDetail";//获取设备信息
    public static final String GET_WORK = "work/query";//获取作业信息
    public static final String GET_REPAIR_DETAILS = "equipment/repairDetail";//获取设备维修记录详情
    public static final String GET_MAINTAIN_DETAILS = "equipment/maintainDetail";//获取设备保养记录详情
    public static final String GET_ACCIDENT_DETAILS = "equipment/accidentDetail";//获取设备事故记录详情
//    public static final String GET_MAINTAIN_DETAILS = "equipment/maintainDetail";//获取设备维修记录详情
    public static final String GET_REPAIR_LOG = "equipment/repair";//获取设备维修记录详情
    public static final String GET_MAINTAIN_LOG = "equipment/maintain";//获取设备维保养详情
    public static final String GET_ACCIDENT_LOG = "equipment/accident";//获取设备事故记录详情
//    public static final String GET_REPAIR_LOG = "equipment/repair";//获取设备维修记录详情
}
