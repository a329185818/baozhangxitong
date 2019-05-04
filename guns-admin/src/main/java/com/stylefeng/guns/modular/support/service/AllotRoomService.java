package com.stylefeng.guns.modular.support.service;


import com.baomidou.mybatisplus.plugins.Page;

import java.util.Map;

public interface AllotRoomService {
    Page<Map<String,Object>> allotRoomQueryAop(Map param);//配房人员信息查询查询
    Map findHouse(String OpTypeNum, String RecYear, String RecNum);//获取房屋信息
    String relieveHouse(String OpTypeNum, String RecYear, String RecNum);//解除配房

    }
