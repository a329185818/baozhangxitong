package com.stylefeng.guns.modular.support.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.page.PageInfoBT;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.model.HousingSubsidyVO;
import com.stylefeng.guns.modular.support.service.IHousingSubsidyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 住房补贴
 */
@Controller
@RequestMapping("/housingSubsidy")
public class HousingSubsidyController extends BaseController {

    @Autowired
    IHousingSubsidyService housingSubsidyService;

    private String PREFIX = "/housingSubsidy/";

    /**
     * 获取住房补贴设置界面
     *
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping("/housing_subsidy")//optypenum,recyear,recnum,recnumgather
    public String getHousingSubsidy(Model model, String optypenum, String recyear, String recnum, String recnumgather) {
        model.addAttribute("recnumgather",recnumgather);
        housingSubsidyService.getHousingSubsidy(model, Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        return PREFIX + "housing_subsidy.html";
    }

    /**
     * 点击“已补贴”查看住房补贴信息
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping("/look_subsidy")
    @ResponseBody
    public Map lookSubsidy(String optypenum, String recyear, String recnum){
        return housingSubsidyService.lookSubsidy(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
    }

    /**
     * 设置住房补贴数据后保存
     * @param json
     * @return
     */
    @RequestMapping("/save_subsidy")
    @ResponseBody
    public Object saveHousingSubsidyInfo(String json){
        return housingSubsidyService.saveHousingSubsidyInfo(json);
    }

    @RequestMapping("/relieve_subsidy")
    @ResponseBody
    public String relieveHousingSubsidy(String optypenum, String recyear, String recnum){
        return housingSubsidyService.relieveHousingSubsidy(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
    }

    /**
     * 住房补贴查询页面
     *
     * @return
     */
    @RequestMapping("/query")
    public String subsidyQueryView() {
        return PREFIX + "housing_query.html";
    }

    /**
     * 住房补贴查询页面获取数据,显示所有已补贴的
     *
     * @param offset
     * @param limit
     * @param name
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageInfoBT subsidyList(@RequestParam(required = false) String name) {
        //获取所有已补贴的申请人
        Map<String,Object> param = new HashMap<>();
        param.put("name",name);
        Page<HousingSubsidyVO> page = housingSubsidyService.queryALLHousingSubsidyAop(param);
        return super.packForBT(page);
    }

    @RequestMapping("/look_person")
    public String lookAndPrint(Model model, String optypenum, String recyear, String recnum) {
        model.addAttribute("optypenum",optypenum);
        model.addAttribute("recyear",recyear);
        model.addAttribute("recnum",recnum);
        return PREFIX + "housingSubsidy_lookPerson.html";
    }

    /**
     * 获取个人补贴的所有数据(包含过期和有效)
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping("/person_list")
    @ResponseBody
    public Object personSubsidyList(String optypenum, String recyear, String recnum){
        Page<HousingSubsidyVO> page = housingSubsidyService.getPersonAllHousingSubsidy(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        return super.packForBT(page);
    }

}
