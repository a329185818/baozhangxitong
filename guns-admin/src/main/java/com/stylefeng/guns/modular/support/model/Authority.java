package com.stylefeng.guns.modular.support.model;

import java.util.List;

/**
 * 权限实体类
 */
public class Authority {
    private String userId;
    private String authorityType;    //权限
    private String projectId;        //项目ID

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
