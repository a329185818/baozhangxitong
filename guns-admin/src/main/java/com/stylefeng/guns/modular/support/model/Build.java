package com.stylefeng.guns.modular.support.model;

import java.util.Date;

public class Build {
    //项目id
    private String projectId;
    //栋id
    private String buildId;
    //栋号
    private String buildNum;
    //所在区域
    private String roadCode;
    //详细坐落
    private String sitnum;
    //总层数
    private Integer floorCount;
    //总单元数
    private Integer unitCount;
    //项目名称
    private String name;
    //创建时间
    private Date createTime;

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(String buildNum) {
        this.buildNum = buildNum;
    }

    public String getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(String roadCode) {
        this.roadCode = roadCode;
    }

    public String getSitnum() {
        return sitnum;
    }

    public void setSitnum(String sitnum) {
        this.sitnum = sitnum;
    }

    public Integer getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(Integer floorCount) {
        this.floorCount = floorCount;
    }

    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Build{" +
                "projectId=" + projectId +
                ", buildId='" + buildId + '\'' +
                ", buildNum=" + buildNum +
                ", roadCode='" + roadCode + '\'' +
                ", sitnum='" + sitnum + '\'' +
                ", floorCount=" + floorCount +
                ", unitCount=" + unitCount +
                ", createTime=" + createTime +
                '}';
    }
}
