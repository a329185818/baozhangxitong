package com.stylefeng.guns.modular.support.model;

import java.util.Date;

public class House {
    //房屋id
    private String houseId;
    //所在栋id
    private String buildId;
    //所在楼层
    private Integer floorNum;
    //所在单元
    private Integer unitNum;
    //房号
    private String roomNum;
    //房屋用途
    private String usage;
    //房屋状态
    private String stateCode;
    //创建时间
    private Date createTime;
    //房屋性质
    private String houseProp;
    //规划用途
    private String layout;
    //户型
    private String houseType;
    //建筑结构
    private String architStructcode;
    //建筑面积
    private String architArea;
    //套内面积
    private String roomArea;
    //分摊面积
    private String apportArea;
    //申报价格
    private String bargainTotalprice;
    //房屋坐落
    private String sitnumGather;
    //其他
    private String otherprop;
    //房屋编码
    private String houseCode;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public Integer getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(Integer floorNum) {
        this.floorNum = floorNum;
    }

    public Integer getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(Integer unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHouseProp() {
        return houseProp;
    }

    public void setHouseProp(String houseProp) {
        this.houseProp = houseProp;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getArchitStructcode() {
        return architStructcode;
    }

    public void setArchitStructcode(String architStructcode) {
        this.architStructcode = architStructcode;
    }

    public String getArchitArea() {
        return architArea;
    }

    public void setArchitArea(String architArea) {
        this.architArea = architArea;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public String getApportArea() {
        return apportArea;
    }

    public void setApportArea(String apportArea) {
        this.apportArea = apportArea;
    }

    public String getBargainTotalprice() {
        return bargainTotalprice;
    }

    public void setBargainTotalprice(String bargainTotalprice) {
        this.bargainTotalprice = bargainTotalprice;
    }

    public String getSitnumGather() {
        return sitnumGather;
    }

    public void setSitnumGather(String sitnumGather) {
        this.sitnumGather = sitnumGather;
    }

    public String getOtherprop() {
        return otherprop;
    }

    public void setOtherprop(String otherprop) {
        this.otherprop = otherprop;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseId='" + houseId + '\'' +
                ", buildId='" + buildId + '\'' +
                ", floorNum=" + floorNum +
                ", unitNum=" + unitNum +
                ", roomNum='" + roomNum + '\'' +
                ", usage='" + usage + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", createTime=" + createTime +
                ", houseProp='" + houseProp + '\'' +
                ", layout='" + layout + '\'' +
                ", houseType='" + houseType + '\'' +
                '}';
    }
}
