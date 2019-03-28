package com.stylefeng.guns.modular.support.model;


import java.util.List;
import java.util.Map;

public class HouseModel {
    private List<House> house;

    private String buildId;

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public List<House> getHouse() {
        return house;
    }

    public void setHouse(List<House> house) {
        this.house = house;
    }
}
