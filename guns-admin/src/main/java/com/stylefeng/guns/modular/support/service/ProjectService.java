package com.stylefeng.guns.modular.support.service;

import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface ProjectService {

    /**
     * 返回所有的项目信息
     * @return
     */
    List<Project> getProject(String name);

    /**
     * 添加项目
     * @param project
     */
    void addProject(Project project);

    /**
     * 根据用户ID删除权限
     */
    void deleteProject(String id);

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
    Project getProjectById(String id);

    /**
     * 根据权限表筛选项目
     * @param projectList
     * @param lookAuthorityList
     * @param changeAuthorityList
     * @return
     */
    List<Project> getProjectByAuthority(List<Project> projectList,List<Authority> lookAuthorityList,List<Authority> changeAuthorityList);

    /**
     * 返回楼盘表项目
     * @param name
     * @return
     */
    List<Build> getBuildProject(String name);

    /**
     * 绑定楼盘表
     * @param projectId
     * @param buildReceive
     */
    void bindingHouseTable(String projectId,String buildReceive);

}
