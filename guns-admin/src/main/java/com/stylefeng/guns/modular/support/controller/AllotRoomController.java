package com.stylefeng.guns.modular.support.controller;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.AllotRoomMapper;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.*;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.IHouseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/allotRoom")
public class AllotRoomController extends BaseController{

        private String PREFIX = "/allotRoom/";

        @Autowired
        private AllotRoomMapper allotRoomMapper;
        @Autowired
        private HouseProjectMapper houseProjectMapper;
        @Autowired
        private BuildMapper buildMapper;
        @Autowired
        private DicService dicService;
    @Autowired
    private IHouseProjectService houseProjectService;

    /**
     * 信息查询列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model){

        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureListJson",JSON.toJSONString(buildingStructureList));
        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeListJson",JSON.toJSONString(houseTypeList));
        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageListJson",JSON.toJSONString(houseuSageList));
        return PREFIX + "index.html";

    }

        /**
         * 配房界面
         * @param
         * @return
         */
        @RequestMapping(value = "/index2")
        @ResponseBody
        public Object index(Integer offset,Integer limit,String condition ,int status){
            Map<String,Object> param = new HashMap<>();
            List<Map<String,Object>> projectList = new ArrayList<Map<String,Object>>();
            param.put("iStart",offset);//0
            param.put("iEnd",limit + offset);//

            param.put("status",status);//状态 2：轮候，3：已配房
            param.put("condition",condition);
            //获取个人ID
            String userId = ShiroKit.getUser().getId();

            List<String> roleNames = ShiroKit.getUser().getRoleNames();
            for(String name:roleNames){
                if(name.equals("高级管理员")){
                    userId = null;
                    break;
                }
            }
            param.put("userId",userId);
            projectList =(List)allotRoomMapper.allotRoomQuery(param);
            //分页数据
            List<Tbbwimport> newProjectList = new ArrayList<>();
            //查询总数
            int listNum = allotRoomMapper.allotRoomQueryCount(param);
            Page page = new Page();
            page.setRecords(projectList);
            page.setTotal(listNum);
            page.setCurrent(offset / limit + 1);
            return super.packForBT(page);
        }


    /**
     * 获取房屋信息
     * @return
     */
    @RequestMapping(value = "/findHouse")
    @ResponseBody
        public House findHouse(String OpTypeNum,String RecYear,String RecNum){
            //获取房屋Id
            String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            if(houseId != null && !("").equals(houseId)){
                //分配房了则获取房屋信息
                House house = buildMapper.getHouse(houseId);
                return house;

            }else{
                return new House();
            }

        }

    /**
     *项目详细
     */
    @RequestMapping("/detail")
    public String projectDetail(String page,String OpTypeNum,String RecYear,String RecNum,Model model,String info){
        Map param = new HashMap();
        param.put("iOptypenum",OpTypeNum);
        param.put("iRecyear",RecYear);
        param.put("iRecnum",RecNum);
        param.put("rcList",new ArrayList<Map<String,Object>>());
        //节点列表
        houseProjectMapper.getNodeList(param);
        //退件类型
        List<Dic> refundList = dicService.getRefund();
        model.addAttribute("refundList",refundList);

        List<Dic> idCardList = dicService.getIdCard();
        model.addAttribute("idCardList",idCardList);

        //查询材料类型字典值
        List<Dic> materialList = dicService.getMaterial();
        model.addAttribute("materialList",materialList);
        model.addAttribute("materialListJson",JSON.toJSONString(materialList));
        //查询家庭类型字典值
        List<Dic> familyType = dicService.getFamilyType();
        model.addAttribute("familyType",familyType);
        model.addAttribute("familyTypeJson",JSON.toJSONString(familyType));
        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureListJson",JSON.toJSONString(buildingStructureList));
        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeListJson",JSON.toJSONString(houseTypeList));
        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageListJson",JSON.toJSONString(houseuSageList));

        //基本信息
        if("80".equals(OpTypeNum)){
            Object project = houseProjectService.projectData(OpTypeNum,RecYear,RecNum);
            model.addAttribute("project",project);
        }else if("81".equals(OpTypeNum)){
            //查找该业务的数据
            FamilySurvey apply = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            //判断点击的是否为公租房公务员
            if("1".equals(apply.getCivilServant().toString())){
                model.addAttribute("civilServant","1");
            }else{
                model.addAttribute("civilServant","0");
            }
            Map<String,Object> arch = new HashMap<>();
            arch.put("iOpTypeNum",OpTypeNum);
            arch.put("iRecYear",RecYear);
            arch.put("iRecNum",RecNum);
            arch.put("IsReadOnly",0);
            arch.put("curRec",new ArrayList<>());
            //获取房屋Id
            String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            if(houseId != null && !("").equals(houseId)){
                //分配房了则获取房屋信息
                House house = buildMapper.getHouse(houseId);
                apply.setHouse(house);
                model.addAttribute("houseJson",JSON.toJSONString(apply.getHouse()));
            }else{
                //未配房
                apply.setHouse(null);
                model.addAttribute("houseJson","");
            }
            houseProjectMapper.getArchList(arch);
            model.addAttribute("apply",apply);
            ArrayList<HashMap> arrayList = (ArrayList)arch.get("curRec");
            //获取文件材料最大的数值，便于前端展示
            BigDecimal neWrecmattum = new BigDecimal("1");
            BigDecimal recmattum = new BigDecimal("0");
            for(int i = 0;i<arrayList.size();i++){
                recmattum = (BigDecimal)arrayList.get(i).get("RECMATNUM");
                if(neWrecmattum.compareTo(recmattum)< 1){
                    neWrecmattum = recmattum;
                }
            }
            model.addAttribute("RECMATNUM",neWrecmattum);
            model.addAttribute("archList",arch.get("curRec"));

            //获取共同申请人列表
            List<JointApplicant> jointApplicantList = houseProjectMapper.selectJointApplicant(param);
            //共同申请人为配偶
            JointApplicant spouse = new JointApplicant();
            //获取配偶信息
            for(int i = 0;i<jointApplicantList.size();i++){
                if(jointApplicantList.get(i).getJointApplicantRelation().equals("2") || jointApplicantList.get(i).getJointApplicantRelation().equals("9")){
                    //提取配偶信息
                    spouse = jointApplicantList.get(i);
                    //移除
                    jointApplicantList.remove(i);
                    break;
                }
            }
            model.addAttribute("spouse",spouse);
            model.addAttribute("jointApplicantList",jointApplicantList);
            model.addAttribute("jointApplicantListSize",jointApplicantList.size());
        }else if("82".equals(OpTypeNum)){//腾退业务
            FamilySurvey apply = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            Map<String,Object> arch = new HashMap<>();
            arch.put("iOpTypeNum",OpTypeNum);
            arch.put("iRecYear",RecYear);
            arch.put("iRecNum",RecNum);
            arch.put("IsReadOnly",0);
            arch.put("curRec",new ArrayList<>());
            houseProjectMapper.getArchList(arch);
            model.addAttribute("apply",apply);
            ArrayList<HashMap> arrayList = (ArrayList)arch.get("curRec");
            //获取文件材料最大的数值，便于前端展示
            BigDecimal neWrecmattum = new BigDecimal("1");
            BigDecimal recmattum = new BigDecimal("0");
            for(int i = 0;i<arrayList.size();i++){
                recmattum = (BigDecimal)arrayList.get(i).get("RECMATNUM");
                if(neWrecmattum.compareTo(recmattum)< 1){
                    neWrecmattum = recmattum;
                }
            }
            model.addAttribute("RECMATNUM",neWrecmattum);
            model.addAttribute("archList",arch.get("curRec"));
        }

        if (("1").equals(info)){
            model.addAttribute("judgeInfo",1);
        }else{
            model.addAttribute("judgeInfo",0);
        }

        model.addAttribute("nodeList",param.get("rcList"));
        List<Map<String,Object>> n = (ArrayList)param.get("rcList");
        List<Map<String,Object>> node =  (List)param.get("rcList");
        model.addAttribute("lastNode",node.get(node.size()-1).get("OPFLOWPHASENAME").toString());

            model.addAttribute("isPeiFang","1");

        return PREFIX + "allotRoom.html";
    }

    /**
     * 解除配房
     * @return
     */
    @RequestMapping(value = "/relieve")
    @ResponseBody
    public String relieveHouse(String OpTypeNum, String RecYear, String RecNum){
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum",Convert.toInt(OpTypeNum));
        param.put("iRecYear",Convert.toInt(RecYear));
        param.put("iRecNum",Convert.toInt(RecNum));
        //获取房屋Id
        String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
        if(houseId == null && ("").equals(houseId)){
            return "EXIT";
        }else{

            param.put("checkHouseId",houseId);
            //修改房屋状态
            param.put("houseCode",0);
            houseProjectMapper.updateHouseCode(param);
            //修改人员状态
            param.put("status","2");
            houseProjectMapper.updatePeopleStatus(param);
            //清除houseId
            param.put("checkHouseId",null);
            houseProjectMapper.allotHouse(param);
            return "SUCCESS";
        }



    }
}
