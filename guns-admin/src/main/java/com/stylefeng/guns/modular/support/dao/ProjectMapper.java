package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {

    /**
     * 返回所有的项目信息
     * @return
     */
    List<Project> getProject(@Param("name")String name);

    /**
     * 添加项目
     * @param project
     */
    void addProject(Project project);

    /**
     * 根据用户ID删除权限
     */
    void deleteProject(@Param("id")String id);

    /**
     * 更新项目
     * @param project
     */
    void updateProject(Project project);

    /**
     * 根据项目ID查找项目
     * @param id
     * @return
     */
    Project getProjectById(@Param("id")String id);

    /**
     * 返回楼盘表项目
     * @param name
     * @return
     */
    List<Build> getBuildProject(@Param("name")String name);

    /**
     * 绑定楼盘表
     * @param projectId
     * @param buildReceive
     */
    void bindingHouseTable(@Param("projectId")String projectId,@Param("buildReceive")String buildReceive);
}
