package com.stylefeng.guns.modular.support.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.page.PageInfoBT;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.model.*;
import com.stylefeng.guns.modular.support.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 当前项目的逻辑都在Controller里，保证项目一致性，所以逻辑处理也放在Controller里.
 * 另外没有找到事务管理.
 */
@Controller
@RequestMapping("/contract")
public class ContractController extends BaseController {

    private String PREFIX = "/contract/";
    @Autowired
    private IContractService contractService;

    /**
     * 合同页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String contractView() {
        return PREFIX + "index.html";
    }

    /**
     * 合同页面获取数据
     * @param offset
     * @param limit
     * @param name
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageInfoBT contractList(Integer offset, Integer limit, @RequestParam(required = false) String name) {
        //获取所有已分配好房子的申请人
        List<ContractUser> list = contractService.queryAllAlreadyAllocatedRoom(name);
        /**
         //是否需要权限才能操作?
         //获取个人ID
         String userId = ShiroKit.getUser().getId();
         //项目查看权限
         List<Authority> lookAuthorityList = authorityService.getAuthority(userId,"查看");
         //项目编辑权限
         List<Authority> changeAuthorityList = authorityService.getAuthority(userId,"编辑");
         projectList = projectService.getProjectByAuthority(projectList,lookAuthorityList,changeAuthorityList);
         */

        //分页数据
        List<ContractUser> newProjectList = new ArrayList<>();
        //项目总数
        int listNum = list.size();
        //当前分页最大数
        int pagingNum = limit * (offset / limit + 1);
        //取两数最小值
        int num;
        if (listNum > pagingNum) {
            num = pagingNum;
        } else {
            num = listNum;
        }

        for (int i = offset; i < num; i++) {
            newProjectList.add(list.get(i));
        }
        Page page = new Page();
        page.setRecords(newProjectList);
        page.setTotal(listNum);
        page.setCurrent(offset / limit + 1);
        return super.packForBT(page);
    }

    /**
     * 设置合同
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping("/contract_detail")//optypenum,recyear,recnum,recnumgather
    public String projectDetail(Model model,String optypenum, String recyear, String recnum) {
        //获取申请人数据
        FamilySurvey apply = contractService.getFamilySurvey(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        //通过houseID获取到房屋信息
        House house = contractService.queryHouseInfo(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        //获取合同信息,过期的合同不返回,只有有效的合同才有数据
        Contract contract = contractService.getEffectiveContract(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        if (contract == null) {
            contract = new Contract();
        }
        model.addAttribute("apply", apply);
        model.addAttribute("house", house);
        model.addAttribute("contract", contract);
        return PREFIX + "contract_detail.html";
    }

    /**
     * 保存合同信息
     *
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object saveContract(String json) {
        Contract contract = JSON.parseObject(json, Contract.class);
        //判断ID是否为空，为空则新增，不为空则修改
        if (contract != null) {
            try {
                if (contract.getId() == null || ("").equals(contract.getId())) {
                    addContract(contract);
                } else {
                    //如果当前时间在此合同开始-截止时间之间，那么此合同还在有效期内，直接更新数据
                    //如果当前时间在此合同时间之外，那么此合同过期失效，重新增加一条数据
                    Date curDate = new Date();
                    if (curDate.compareTo(contract.getEndTime()) >= 0) {
                        addContract(contract);//当前时间大于合同截止时间
                    } else {
                        contract.setOprationId(ShiroKit.getUser().getId());
                        contract.setCreateTime(new Date());
                        //更新合同数据
                        contractService.updateContract(contract);
                    }
                }
                return "Success";
            } catch (Exception e) {
                return "Error";
            }
        } else {
            return "Error";
        }
    }

    /**
     * 新增合同到数据库
     * @param contract
     */
    private void addContract(Contract contract) {
        //获取uuid设置到合同id里
        contract.setId(StaticClass.getUUID());
        contract.setOprationId(ShiroKit.getUser().getId());
        contract.setCreateTime(new Date());
        //添加新合同
        contractService.addContract(contract);
    }

    /**
     * 打印
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     * @param recnumgather
     * @return
     */
    @RequestMapping(value = "/export_pdf")
    public String exportPdf(Model model,String optypenum, String recyear, String recnum,String recnumgather) {
        //获取承租方信息,拿到姓名和身份证号码
        //获取房屋地址，拿到小区地址+栋+室+房屋结构+建筑面积
        //获取合同设置的时间，年申情况，合同租金/每平米·月，计算出月租金，月租金大写，半年租金，半年租金大写
        //共同居住人情况
        contractService.exportPdf(model,Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        return PREFIX + "contract_pdf.html";
    }

}
