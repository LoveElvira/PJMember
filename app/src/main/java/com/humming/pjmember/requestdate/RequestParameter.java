package com.humming.pjmember.requestdate;

import com.humming.pjmember.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/9/8.
 */

public class RequestParameter implements IRequestMainData {

    //登录
    private String phone;
    private String pwd;

    //修改密码
    private String oldPwd;
    private String newPwd;

    //获取设备信息 ID  获取事故、维修 记录的ID
    private String equipmentId;

    //获取作 type: 1--今日作业  2--未完成作业  3--全部作业
    private String type;
    //第几页 获取第几页数据，首页不用传
    private String pagable;

    //获取作业详情  获取作业人员信息
    private String workId;

    //获取维修记录详情 id
    private String repairId;
    //获取保养记录详情 id
    private String maintainId;
    //获取事故记录详情 id
    private String accidentId;

    private String userId;

    private List<String> pictureUrls;

    //合同 合同类型 1：收入合同 2：支出合同
    private String conNature;

    //项目id
    private String projectId;

    //合同id
    private String contractId;

    //会议日期
    private String date;

    //会议ID
    private String conferenceId;


    public RequestParameter() {
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(String maintainId) {
        this.maintainId = maintainId;
    }

    public String getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(String accidentId) {
        this.accidentId = accidentId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getConNature() {
        return conNature;
    }

    public void setConNature(String conNature) {
        this.conNature = conNature;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }
}
