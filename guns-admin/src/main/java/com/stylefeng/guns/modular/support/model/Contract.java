package com.stylefeng.guns.modular.support.model;

import java.util.Date;

/**
 * 合同实体类
 */
public class Contract {
    //a.recyear=b.recyear and a.recnum = b.recnum and a.people_status=3 and b.oppartnum=312
    //uuid   ID
    private String id;
    //----------------------
    //recyear    年     三字段(recyear + recnum + optypenum)为唯一查询申请人数据id
    private Integer recyear;
    //recnum
    private Integer recnum;
    //optypenum
    private Integer optypenum;
    //----------------------
    //oppartnum   编号，公租房为312，目前只有公租房，等以后有需要的时候在修改
    private Integer oppartnum = 312;
    //start  开始时间
    private Date startTime;
    //end       结束时间
    private Date endTime;
    //Square meter / month   每平米/月的价格, 月租=每平米/月的价格*房屋面积
    private double price;
   //Annual review: 年审情况
   private String annualReview;
   //N月交一次房租,合同目前暂定半年，如有需要在修改
    private Integer numMonth = 6;
    //房屋id
    private String houseId;
    //操作人
    private String oprationId;
    //操作时间
    private Date createTime;

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

    public Integer getOppartnum() {
        return oppartnum;
    }

    public void setOppartnum(Integer oppartnum) {
        this.oppartnum = oppartnum;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAnnualReview() {
        return annualReview;
    }

    public void setAnnualReview(String annualReview) {
        this.annualReview = annualReview;
    }

    public Integer getNumMonth() {
        return numMonth;
    }

    public void setNumMonth(Integer numMonth) {
        this.numMonth = numMonth;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
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

}
