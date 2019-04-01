package com.stylefeng.guns.modular.support.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.Dic;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.model.HouseModel;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.IBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/build")
public class BuildController extends BaseController {
    @Autowired
    private IBuildService buildService;

    @Autowired
    private DicService dicService;

    private static final String PREFIX = "/housesmanage/";

    /**
     * 配房界面
     * @param model
     * @return
     */
    @RequestMapping("/choose_people")
    public String chooseHouseTable(Model model,String id){
        model.addAttribute("houseId",id);
        return PREFIX + "choose_people.html";
    }

    @RequestMapping("")
    public String index(Model model){
        //房屋地区字典值
        List<Dic> regionList = dicService.getRegion();
        model.addAttribute("regionList",regionList);
        return PREFIX + "build_mgr.html";
    }

    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.buildService.tree();
        return tree;
    }

    @RequestMapping(value = "/create_project")
    @ResponseBody
    public Object createProject(String projectName,String sitnumgather,String projectId,String region) {
        Map<String,Object> map = this.buildService.createProject(projectName,sitnumgather,projectId,region);
        if(projectId == null || projectId == ""){
            return "CREATE";
        }else{
            return "UPDATE";
        }

    }

    @RequestMapping(value = "/create_ridgepole")
    @ResponseBody
    public Object createRidgepole(Build build) {
        this.buildService.createRidgepole(build);
        return SUCCESS_TIP;
    }


    @RequestMapping(value = "/delete_project")
    @ResponseBody
    public Object deleteProject(@RequestParam String projectId) {
        try{
            buildService.deleteProject(projectId);
            return SUCCESS_TIP;
        }catch (Exception e){
            return ERROR;
        }
    }

    @RequestMapping(value = "/open_add")
    public String createBuildPage(String buildId, Model model) {

        List<House> houseList = buildService.houseList(buildId);
        String houseListJson = JSONObject.toJSONString(houseList);
        model.addAttribute("houseList",houseListJson);
        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureList",buildingStructureList);

        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeList",houseTypeList);

        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageList",houseuSageList);

        return PREFIX + "build_add.html";
    }

    @RequestMapping(value = "/open_detail")
    public String detailBuildPage(String buildId, Model model) {
        Build build = buildService.getbuildByBuildId(buildId);
        List<House> houseList = buildService.houseList(buildId);
        List<Map<String,Object>> holderList = new ArrayList<>();
        for(House house:houseList){
            if(!("0").equals(house.getHouseCode())){
                Map<String,Object> holder = buildService.searchByHouseId(house.getHouseId());
                holderList.add(holder);
            }
        }
        String houseListJson = JSONObject.toJSONString(houseList);
        model.addAttribute("build",build);
        model.addAttribute("houseList",houseListJson);
        //已配房人员列表
        model.addAttribute("holderList",JSONObject.toJSONString(holderList));
        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureList",buildingStructureList);
        model.addAttribute("buildingStructureListJson",JSON.toJSONString(buildingStructureList));

        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("houseTypeListJson",JSON.toJSONString(houseTypeList));

        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageList",houseuSageList);
        model.addAttribute("houseuSageListJson",JSON.toJSONString(houseuSageList));

        return PREFIX + "build_detail.html";
    }

    @RequestMapping(value = "/build_list")
    @ResponseBody
    public Object buildList(String projectId,String condition){
        Page<Build> page = buildService.buildList(projectId,condition);
        return super.packForBT(page);
    }

    @RequestMapping(value = "/delete_build")
    @ResponseBody
    public Object deleteBuild(@RequestParam String buildId) {
        try{
            buildService.deleteBuild(buildId);
            return SUCCESS_TIP;
        }catch (Exception e){
            return ERROR;
        }
    }

    @RequestMapping(value = "/create_house")
    @ResponseBody
    public Object createHouse(@RequestBody List<House> floorUnitRoomNum) {
        this.buildService.createHouse(floorUnitRoomNum);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/update_house")
    @ResponseBody
    public Object updateHouse(@RequestParam String json,@RequestParam String chooseId) {

        House house =  JSON.parseObject(json,House.class);
        buildService.updateHouse(house,chooseId);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/delete_house")
    @ResponseBody
    public Object deleteHouse(@RequestParam String chooseId) {
        buildService.deleteHouse(chooseId);
        return SUCCESS_TIP;
    }


}
