package com.stylefeng.guns.modular.support.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.service.IBuildService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BuildServiceImpl implements IBuildService{
    @Resource
    private BuildMapper buildMapper;

    @Override
    public List<ZTreeNode> tree() {
        return this.buildMapper.tree();
    }
    @Override
    public Map<String,Object> createProject(String name,String sitnumgather,String projectId,String region){
        Map<String,Object> param = new HashMap();
        if(projectId == null || projectId == ""){
            param.put("sdistrictcode",region);
            param.put("sPROJECTNAME",name);
            param.put("ssitnumgather",sitnumgather);
            param.put("sMsg","");
            param.put("iSuccess",0);
            buildMapper.createProject(param);
        }else{
            buildMapper.updateProject(name,sitnumgather,projectId,region);
        }
        return param;
    }

    @Override
    public Page<Build> buildList(String projectId,String condition){
        Page<Build> page = new PageFactory().defaultPage();
        List<Build> buildList = buildMapper.buildList(page,projectId,condition);
        page.setRecords(buildList);
        return page;
    }

    @Override
    public void createRidgepole(Build build){
        String roadCode = buildMapper.getBulidInfo(build.getProjectId());
        build.setRoadCode(roadCode);
        //获得ID
        Map<String,Object> param = new HashMap();
        param.put("onlyId","");
        param.put("itype",1);
        param.put("idistrict",roadCode);
        buildMapper.getOnlyId(param);
        //赋值ID
        build.setBuildId(param.get("onlyId").toString());
        buildMapper.createRidgepole(build);
    }

    @Override
    public List<House> houseList(String buildId){
        return buildMapper.houseList(buildId);
    }

    @Override
    public void createHouse(List<House> houseList){
        String roadCode = "";
        String projectAddress = "";
        String buildNum = "";
        Map<String,Object> param = new HashMap();
        if(houseList.size()>0){
            Map<String,Object> map = buildMapper.getProjectAddress(houseList.get(0).getBuildId());
            roadCode = map.get("DISTRICTCODE").toString();
            projectAddress = map.get("DISTRICTNAME").toString()+map.get("BUILDSITNUMGATHER")+map.get("BUILDSITBUILDNUM");
            buildNum = map.get("HOUSESITBUILDNUM").toString();

            //获得ID
            param.put("onlyId","");
            param.put("itype",2);
            param.put("idistrict",roadCode);

        }
        for(House house:houseList){
            buildMapper.getOnlyId(param);
            house.setHouseId(param.get("onlyId").toString());
            String sitnumGather = projectAddress + buildNum + house.getUnitNum() + "单元" + house.getRoomNum();//projectaddress,buildnum,unitnum,roomnum
            house.setSitnumGather(sitnumGather);
            buildMapper.createHouse(house);
        }
    }

    @Override
    public void updateHouse(House house,String chooseId){
        //拆分ID
        String[] houseId = chooseId.split(",");
        //如果是编辑单个，则可以修改房号
        if(houseId.length == 1){
            house.setHouseId(houseId[0]);
            buildMapper.updateHouseOne(house);
        }else { //编辑多个则不可修改房号
            for(String id:houseId){
                house.setHouseId(id);
                buildMapper.updateHouse(house);
            }
        }
    }
    @Override
    public void deleteHouse(String chooseId){
        //拆分ID
        String[] houseId = chooseId.split(",");
        for(String id:houseId){
            buildMapper.deleteHouse(id);
        }

    }

    @Override
    public void deleteBuild(String buildId){
        //拆分ID
        String[] buildIdstr = buildId.split(",");
        for(String id:buildIdstr){
            //删除栋
            buildMapper.deleteBuild(id);
            //删除一栋的房屋
            buildMapper.deleteHouseByBuildId(id);
        }
    }

    @Override
    public void deleteProject(String projectId) throws Exception{
        Page<Build> page = new Page<>();
        List<Build> buildList = buildMapper.buildList(page,projectId,"");
        if(buildList.size()>0){
            throw new Exception("该项目存在栋，不能删除");
        }else{
            buildMapper.deleteProject(projectId);
        }
    }

    private String getBulidInfo(String projectId){
        return buildMapper.getBulidInfo(projectId);
    }

    @Override
    public Map<String,Object> searchByHouseId(String houseId){
        return buildMapper.searchByHouseId(houseId);
    }

    @Override
    public Build getbuildByBuildId(String buildId) {
        return buildMapper.getbuildByBuildId(buildId);
    }
}
