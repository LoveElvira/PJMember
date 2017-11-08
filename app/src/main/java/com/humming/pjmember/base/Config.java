package com.humming.pjmember.base;

/**
 * Created by Elvira on 2017/6/1.
 * 网络连接路径
 */

public class Config {
    public static final String URL_SERVICE = "http://pjqs.humming-tech.com/cgi";//正式环境
    //    public static final String URL_SERVICE_UPLOAD = "http://pjqs.humming-tech.com/uploadFile";//上传文件
    public static final String URL_SERVICE_UPLOAD = "http://192.168.1.70:8080/uploadFile";//上传文件

    public static final String USER_LOGIN = "newUser/newLogin";//登录
    //    public static final String USER_LOGIN = "user/login";//登录
    public static final String UPDATE_IMAGE = "file/uploadImage";//上传图片
    public static final String GET_EQUIPMENT_DETAILS = "equipment/queryDetail";//获取设备信息
    public static final String GET_WORK = "work/query";//获取作业信息
    public static final String GET_WORK_DETAILS = "work/detail";//获取作业信息详情
    public static final String GET_REPAIR_DETAILS = "equipment/repairDetail";//获取设备维修记录详情
    public static final String GET_MAINTAIN_DETAILS = "equipment/maintainDetail";//获取设备保养记录详情
    public static final String GET_ACCIDENT_DETAILS = "equipment/accidentDetail";//获取设备事故记录详情
    public static final String GET_OIL_DETAILS = "equipment/oilDetail";//获取设备用油记录详情
    public static final String GET_REPAIR_LOG = "equipment/repair";//获取设备维修记录
    public static final String GET_MAINTAIN_LOG = "equipment/maintain";//获取设备保养记录
    public static final String GET_ACCIDENT_LOG = "equipment/accident";//获取设备事故记录
    public static final String GET_OIL_LOG = "equipment/oil";//获取设备用油记录
    public static final String ADD_REPAIR_LOG = "equipment/addRepair";//新增设备维修信息
    public static final String ADD_MAINTAIN_LOG = "equipment/addMaintain";//新增设备保养信息
    public static final String ADD_ACCIDENT_LOG = "equipment/addAccident";//新增设备事故信息
    public static final String GET_WORK_PERSON = "user/byWork";//获取作业人员信息
    public static final String GET_ACCIDENT_TYPE = "equipment/allAccidentType";//获取全部事故类型和性质
    public static final String GET_WORK_EQUIPMENT = "equipment/byWork";//获作业设备列表
    public static final String GET_WORK_MATERIAL = "material/byWork";//获作业材料列表
    public static final String GET_USER_LEVEL = "user/level";//获取用户职位
    public static final String GET_DEFECT = "emergency/all";//获取突发事件列表
    public static final String CONFIRM_SAFETY = "work/safety";//确认安全交底
    public static final String GET_WORK_PERSON_INFO = "user/find";//查询人员信息
    public static final String ADD_WORK_PERSON = "work/addUser";//作业中添加用户接口
    public static final String GET_WEATHER = "weather/now";//获取当日天气
    public static final String GET_FACILITY = "work/facility";//获取设施列表
    public static final String ADD_WORK = "work/addWork";//添加缺陷
    public static final String GET_DEFECT_WORK = "work/defect";//获取缺陷作业列表
    public static final String WORK_START = "work/start";//开始工作
    public static final String WORK_BIND_PIC = "work/picture";//绑定上传图片
    public static final String WORK_END = "work/end";//绑定上传图片
    public static final String GET_WORK_PIC = "work/findWorkPicture";//获取作业图片
}
