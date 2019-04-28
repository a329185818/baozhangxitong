package com.stylefeng.guns.modular.support.service;


import java.util.List;
import java.util.Map;

public interface AllotRoomService {
    List<Map<String,Object>> allotRoomQuery(Map param);//配房人员信息查询查询
    int allotRoomQueryCount(Map param);//查询总条数
    Map findHouse(String OpTypeNum, String RecYear, String RecNum);//获取房屋信息
    String relieveHouse(String OpTypeNum, String RecYear, String RecNum);//解除配房

    }
