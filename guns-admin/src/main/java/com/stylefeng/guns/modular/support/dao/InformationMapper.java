package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InformationMapper {

    /**
     * 保障房项目列表
     * @param param
     * @return
     */
    Map<String, Object> infoList(Map param);
    RecOwner findProject(String idNum);  //根据申请人身份证号查找申请人信息

}
