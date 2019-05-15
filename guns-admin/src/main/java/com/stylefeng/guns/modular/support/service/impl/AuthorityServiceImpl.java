package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.modular.support.dao.AuthorityMapper;
import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public List<Authority> getAuthority(String userId,String authorityType){
        return authorityMapper.getAuthority(userId,authorityType);
    }

    @Override
    public void addAuthority(Authority authority){
        authorityMapper.addAuthority(authority);
    }

    @Override
    public List<Map<String,Object>> getProject(String name){
        return authorityMapper.getProject(name);
    }

    @Override
    public void deleteAuthority(Authority authority){
        authorityMapper.deleteAuthority(authority);
    }

    @Override
    public void addUserAuthority(String userId){
        Map<String,Object> map = new HashMap<>();
        map.put("USERNUM",userId);
        map.put("OPTYPENUM","81");
        map.put("OPUSERROLE","1");
        //受理1, 扫描2, 复审3, 审核4, 配房5, 归档6
        //新需求为，派件给A，A拥有审批权限，A结束审批后权限重置，由boss重新派件配房权限，所以A可能没有配房权限
        //也就是说，初始的管理员都只有1-4的审批权限
        //TODO:sql语句有时间在优化
        for(int i = 1;i<7;i++){
            for(int y = 1;y<5;y++){
                map.put("OPPARTNUM","31"+i);
                map.put("OPUSERPOWER",""+y);
                authorityMapper.addUserAuthority(map);
            }
        }
    }

    @Override
    public void deleteUserAuthority(String userId){
        authorityMapper.deleteUserAuthority(userId);
    }
}
