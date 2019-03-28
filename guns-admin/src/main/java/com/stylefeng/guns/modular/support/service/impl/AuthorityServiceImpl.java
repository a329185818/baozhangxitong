package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.modular.support.dao.AuthorityMapper;
import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        for(int i = 1;i<7;i++){
            for(int y = 1;y<7;y++){
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
