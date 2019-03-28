package com.stylefeng.guns.modular.support.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.House;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼盘管理 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
public interface BuildMapper{

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    void createProject(Map map);

    /**
     * 获取栋列表
     * @param projectId
     * @return
     */
    List<Build> buildList(Page page,@Param("projectId") String projectId,@Param("condition") String condition);

    /**
     * 创建栋
     * @param build
     */
    void createRidgepole(Build build);

    List<House> houseList(@Param("buildId")String buildId);

    void createHouse(House house);

    /**
     * 更新多个房屋
     * @param house
     */
    void updateHouse(House house);

    /**
     * 更新一个房屋
     * @param house
     */
    void updateHouseOne(House house);

    /**
     * 删除一个房屋
     * @param houseId
     */
    void deleteHouse(@Param("houseId")String houseId);

    /**
     * 删除一栋
     * @param buildId
     */
    void deleteBuild(@Param("buildId")String buildId);

    /**
     * 删除一栋的房屋
     * @param buildId
     */
    void deleteHouseByBuildId(@Param("buildId")String buildId);

    /**
     * 删除一个项目
     * @param projectId
     */
    void deleteProject(@Param("projectId")String projectId);

    /**
     * 更新项目信息
     */
    void updateProject(@Param("projectName") String projectName,@Param("sitnumgather") String sitnumgather,@Param("projectId") String projectId,@Param("region") String region);

    /**
     * 查找一个项目的地址编号
     * @param projectId
     */
    String getBulidInfo(@Param("projectId")String projectId);

    //获取唯一ID
    void getOnlyId(Map param);

    House getHouse(@Param("houseId")String houseId);

    Map<String,Object> searchByHouseId(String houseId);
}