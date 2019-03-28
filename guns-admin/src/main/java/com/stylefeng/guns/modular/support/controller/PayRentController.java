package com.stylefeng.guns.modular.support.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.Dic;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.model.PayRent;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.PayRentService;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayRentController extends BaseController {
    private String PREFIX = "/payRent/";

    @Autowired
    private PayRentService payRentService;

    @Autowired
    private BuildMapper buildMapper;

    @Autowired
    private DicService dicService;
    /**
     * 派件界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model){
        return PREFIX + "index.html";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public Object search(Integer offset,Integer limit,String houseId,String month){
        Map<String,Object> param = new HashMap();
        param.put("houseId",houseId);
        param.put("month",month);
        List<PayRent> payRentList = payRentService.search(param);
        List<Map<String,Object>> mapArrayList = new ArrayList<Map<String,Object>>();
        int i = 1;
        //构建新的数据结构
        for(PayRent payRent : payRentList){
            Map<String,Object> map = new HashMap<>();
            map.put("IROW",i);
            map.put("houseId",payRent.getHouse().getHouseId());
            map.put("rent",payRent.getRent());
            map.put("payTime",payRent.getPayTime());
            mapArrayList.add(map);
            i++;
        }
        Page page = new Page();
        page.setTotal(i-1);
        page.setRecords(mapArrayList);
        page.setCurrent(offset / limit + 1);
        return super.packForBT(page);
    }

    @RequestMapping(value = "/detail")
    public String detail(Model model,String houseId){
        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureListJson", JSON.toJSONString(buildingStructureList));
        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeListJson",JSON.toJSONString(houseTypeList));
        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageListJson",JSON.toJSONString(houseuSageList));
        House house = buildMapper.getHouse(houseId);
        model.addAttribute("house",house);
        return PREFIX + "houseDaily.html";
    }
}
