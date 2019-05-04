package com.stylefeng.guns.modular.support.dao;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

public interface AllotRoomMapper {

    List<Map<String,Object>> allotRoomQuery(Page page,Map param);//申请人信息（申请状态）查询

}
