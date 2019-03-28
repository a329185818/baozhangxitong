package com.stylefeng.guns.modular.support.model;

import java.util.Date;

public class Project {
    private String id;                          //项目ID
    private String constructionUnit;          //建设单位
    private String contractor;                 //承建单位
    private String name;                        //项目名称
    private String address;                     //项目地址
    private Date startTime;                     //开工时间
    private Date endTime;                       //竣工时间
    private int houseNumber;                   //建设套数
    private int area;                           //建设总面积
    private String planningPermit;             //规划许可证
    private String constructionPermit;         //施工许可证
    private String plotRatio;                   //容积率
    private String constructionScale;          //建设规模
    private long buildReceive;                 //楼盘表关联
    private int canChange;                      //编辑权限

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(String constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getPlanningPermit() {
        return planningPermit;
    }

    public void setPlanningPermit(String planningPermit) {
        this.planningPermit = planningPermit;
    }

    public String getConstructionPermit() {
        return constructionPermit;
    }

    public void setConstructionPermit(String constructionPermit) {
        this.constructionPermit = constructionPermit;
    }

    public String getPlotRatio() {
        return plotRatio;
    }

    public void setPlotRatio(String plotRatio) {
        this.plotRatio = plotRatio;
    }

    public String getConstructionScale() {
        return constructionScale;
    }

    public void setConstructionScale(String constructionScale) {
        this.constructionScale = constructionScale;
    }

    public long getBuildReceive() {
        return buildReceive;
    }

    public void setBuildReceive(long buildReceive) {
        this.buildReceive = buildReceive;
    }

    public int getCanChange() {
        return canChange;
    }

    public void setCanChange(int canChange) {
        this.canChange = canChange;
    }
}
