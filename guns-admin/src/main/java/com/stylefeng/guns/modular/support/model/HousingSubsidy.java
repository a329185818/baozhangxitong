package com.stylefeng.guns.modular.support.model;

import java.util.Date;

/**
 * 住房补贴实体类
 */
public class HousingSubsidy {
    //uuid   ID
    private String id;
    //----------------------
    //recyear    年     三字段(recyear + recnum + optypenum)为唯一查询数据id
    private Integer recyear;
    //recnum
    private Integer recnum;
    //optypenum
    private Integer optypenum;
    //start  开始时间
    private Date startTime;
    //end       结束时间
    private Date endTime;
    //补贴金额
    private double rent;
    //备注
    private String remark;
    //操作人
    private String oprationId;
    //操作时间
    private Date createTime;
    //解除住房补贴时间
    private Date relieveTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRecyear() {
        return recyear;
    }

    public void setRecyear(Integer recyear) {
        this.recyear = recyear;
    }

    public Integer getRecnum() {
        return recnum;
    }

    public void setRecnum(Integer recnum) {
        this.recnum = recnum;
    }

    public Integer getOptypenum() {
        return optypenum;
    }

    public void setOptypenum(Integer optypenum) {
        this.optypenum = optypenum;
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

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOprationId() {
        return oprationId;
    }

    public void setOprationId(String oprationId) {
        this.oprationId = oprationId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRelieveTime() {
        return relieveTime;
    }

    public void setRelieveTime(Date relieveTime) {
        this.relieveTime = relieveTime;
    }
}
