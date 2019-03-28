package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.Authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuthorityMapper {

    List<Authority> getAuthority(@Param("userId")String userId,@Param("authorityType") String authorityType);

    void addAuthority(Authority authority);

    /**
     * 获取所有项目
     * @return
     */
    List<Map<String,Object>> getProject(@Param("name")String name);


    /**
     * 根据用户ID删除权限
     */
    void deleteAuthority(Authority authority);

    void addUserAuthority(Map<String,Object> map);

    void deleteUserAuthority(@Param("userId")String userId);
}
