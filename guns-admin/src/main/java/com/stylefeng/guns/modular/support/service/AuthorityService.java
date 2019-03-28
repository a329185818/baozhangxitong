package com.stylefeng.guns.modular.support.service;

import com.stylefeng.guns.modular.support.model.Authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuthorityService {

    /**
     * 根据用户ID查找其项目权限
     * @param userId
     * @return
     */
    List<Authority> getAuthority(String userId,String authorityType);

    /**
     * 添加权限
     * @param authority
     */
    void addAuthority(Authority authority);

    /**
     * 获取所有项目
     * @return
     */
    List<Map<String,Object>> getProject(String name);

    /**
     * 根据用户ID删除权限
     */
    void deleteAuthority(Authority authority);

    /**
     * 添加审批权限
     */
    void addUserAuthority(String userId);

    /**
     * 添加审批权限
     */
    void deleteUserAuthority(String userId);
}
