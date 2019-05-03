package com.stylefeng.guns.modular.support.model;

import java.util.Date;

public class HousingSubsidyVO {
    //住房补贴显示的界面数据------------------
    //申请人姓名
    private String name;
    //身份证号
    private String idCard;
    //电话
    private String telphone;
    //总条数，用于分页显示
    private int total;

    //住房补贴查询-查看：显示个人详细信息界面数据-----------------
    private double rent;
    //start  开始时间
    private Date startTime;
    //end       结束时间
    private Date endTime;
    //备注
    private String remark;
    //操作人
    private String oprationName;
    //解除操作人
    private String relieveOprationName;
    //解除住房补贴时间
    private Date relieveTime;

    //三个字段为唯一标识(类似表id)可以获取数据------------------
    private int optypenum;
    private int recyear;
    private int recnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOprationName() {
        return oprationName;
    }

    public void setOprationName(String oprationName) {
        this.oprationName = oprationName;
    }

    public String getRelieveOprationName() {
        return relieveOprationName;
    }

    public void setRelieveOprationName(String relieveOprationName) {
        this.relieveOprationName = relieveOprationName;
    }

    public Date getRelieveTime() {
        return relieveTime;
    }

    public void setRelieveTime(Date relieveTime) {
        this.relieveTime = relieveTime;
    }

    public int getOptypenum() {
        return optypenum;
    }

    public void setOptypenum(int optypenum) {
        this.optypenum = optypenum;
    }

    public int getRecyear() {
        return recyear;
    }

    public void setRecyear(int recyear) {
        this.recyear = recyear;
    }

    public int getRecnum() {
        return recnum;
    }

    public void setRecnum(int recnum) {
        this.recnum = recnum;
    }
}
