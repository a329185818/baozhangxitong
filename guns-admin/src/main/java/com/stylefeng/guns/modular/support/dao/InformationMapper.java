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
    Tbbwimport findProject(String idNum);  //根据申请人身份证号查找申请人信息
    List<Tbbwimport> importQuery(Map map);//导入信息查询
    List<Tbbwimport> importQueryList(Map map);//导入信息分页查询
    Integer importQueryCount(Map map);//查询导入信息总数
    List findTbbwimportList(Map map);//查找检查信息信息
    int addTbbwimport(Tbbwimport tbbwimport);//添加申请记录
    int deleteProposer(String proposerId); //删除申请记录
    int alterProposer(Tbbwimport tbbwimport);//修改申请记录
    Tbbwimport findProposer(String proposerId);//根据id查找申请记录
    //批量导入申请人信息
    int insertTbbwimport(Tbbwimport tbbwimport);
    //导出申请人信息
    Tbbwimport findIdTbbwimport(@Param("id")String id);

}
