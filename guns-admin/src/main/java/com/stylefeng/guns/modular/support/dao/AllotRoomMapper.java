package com.stylefeng.guns.modular.support.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AllotRoomMapper {

    List<Map<String,Object>> allotRoomQuery(Map param);//申请人信息（申请状态）查询
    int allotRoomQueryCount(Map param);//查询总条数

}
