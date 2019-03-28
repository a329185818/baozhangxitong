package com.stylefeng.guns.modular.support.model;

import java.util.Date;

/**
 * 租金实体类
 */
public class PayRent {
    House house;    //绑定房屋
    Double rent;    //租金
    Date payTime;  //交租时间

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
