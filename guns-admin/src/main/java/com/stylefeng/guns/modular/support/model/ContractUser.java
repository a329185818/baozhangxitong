package com.stylefeng.guns.modular.support.model;

/**
 * 显示合同界面或者打印显示用到的实体类
 */
public class ContractUser {
    //申请人姓名
    private String name;
    //身份证号
    private String idCard;
    //电话
    private String telphone;
    // 登记编号
    private String recnumgather;
    //申请类型
    private String oppartnum;

    //打印用到：tb.ARCHITAREA,tb.SITUNITNUM,tb.SITDOORNUM,tb.architStructcode,bzf.address
    //建筑面积
    private double architArea;
    //所在的栋
    private String buildNum;
    //所在单元
    private String unitNum;
    //房号
    private String roomNum;
    //建筑结构
    private String architStructcode;
    //项目名称(同心家园三十期)
    private String projectName;
    //楼房地址(三亚市吉阳区荔枝沟社区三亚木材厂西侧同心家园三十期)
    private String projectAddress;

    //三个字段为唯一标识(类似表id)可以获取数据
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

    public String getRecnumgather() {
        return recnumgather;
    }

    public void setRecnumgather(String recnumgather) {
        this.recnumgather = recnumgather;
    }

    public String getOppartnum() {
        return oppartnum;
    }

    public void setOppartnum(String oppartnum) {
        this.oppartnum = oppartnum;
    }

    public double getArchitArea() {
        return architArea;
    }

    public void setArchitArea(double architArea) {
        this.architArea = architArea;
    }

    public String getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(String buildNum) {
        this.buildNum = buildNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getArchitStructcode() {
        return architStructcode;
    }

    public void setArchitStructcode(String architStructcode) {
        this.architStructcode = architStructcode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
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
