package com.stylefeng.guns.modular.support.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.model.HouseModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IBuildService {
    /**
     * 项目树
     * @return
     */
    List<ZTreeNode> tree();

    /**
     * 新建项目
     */
    Map<String,Object> createProject(String name , String sitnumgather, String projectId, String region);

    /**
     * 删除项目
     */
    void deleteProject(String projectId) throws Exception;


    void createRidgepole(Build build);

    Page<Build> buildList(String projectId,String condition);

    /**
     * 获取房屋列表
     */
    List<House> houseList(String buildId);

    /**
     * 创建房屋
     */
    void createHouse(List<House> houseList);

    /**
     * 更新房屋信息
     */
    void updateHouse(House house,String chooseId);

    /**
     * 删除房屋
     */
    void deleteHouse(String chooseId);

    /**
     * 删除栋
     */
    void deleteBuild(String buildId);

    /**
     * 根据房屋ID查所有者
     */
    Map<String,Object> searchByHouseId(String houseId);
}
