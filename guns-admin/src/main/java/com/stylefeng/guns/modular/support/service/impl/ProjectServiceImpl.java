package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.modular.support.dao.ProjectMapper;
import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.Project;
import com.stylefeng.guns.modular.support.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<Project> getProject(String name){
         return projectMapper.getProject(name);
    }

    @Override
    public void addProject(Project project){
        projectMapper.addProject(project);
    }

    @Override
    public void deleteProject(String id){
        projectMapper.deleteProject(id);
    }

    @Override
    public void updateProject(Project project){
        projectMapper.updateProject(project);
    }

    @Override
    public Project getProjectById(String id){
        return projectMapper.getProjectById(id);
    }

    @Override
    public List<Project> getProjectByAuthority(List<Project> projectList,List<Authority> lookAuthorityList,List<Authority> changeAuthorityList){
        //可查看的项目信息
        List<Project> newProjectList = new ArrayList<>();
        //根据权限过滤项目数据
        for (int y = 0;y<lookAuthorityList.size();y++){
            for(int i = 0;i<projectList.size();i++){
                if(projectList.get(i).getId().equals(lookAuthorityList.get(y).getProjectId())){
                    for(int z = 0;z<changeAuthorityList.size();z++){
                        //判断是否有编辑权限
                        if(projectList.get(i).getId().equals(changeAuthorityList.get(z).getProjectId())){
                            projectList.get(i).setCanChange(1);
                            break;
                        }
                    }
                    newProjectList.add(projectList.get(i));
                    break;
                }
            }
        }
        return newProjectList;
    }

    @Override
    public List<Build> getBuildProject(String name){
        return projectMapper.getBuildProject(name);
    }

    @Override
    public void bindingHouseTable(String projectId,String buildReceive){
        projectMapper.bindingHouseTable(projectId,buildReceive);
    }


}
