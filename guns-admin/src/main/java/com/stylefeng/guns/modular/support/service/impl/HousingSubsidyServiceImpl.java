package com.stylefeng.guns.modular.support.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.support.dao.DicMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.dao.HousingSubsidyMapper;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.HousingSubsidy;
import com.stylefeng.guns.modular.support.model.HousingSubsidyVO;
import com.stylefeng.guns.modular.support.service.IHousingSubsidyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HousingSubsidyServiceImpl implements IHousingSubsidyService {

    @Autowired
    private HouseProjectMapper houseProjectMapper;
    @Autowired
    private DicMapper dicMapper;
    @Autowired
    private HousingSubsidyMapper housingSubsidyMapper;


    @Override
    public void getHousingSubsidy(Model model, Integer optypenum, Integer recyear, Integer recnum) {
        //获取申请人数据
        FamilySurvey apply = getFamilySurvey(optypenum, recyear, recnum);
        model.addAttribute("apply", apply);
        //获取住房补贴数据,根据解除时间是否有值来判断是否为有效数据
        HousingSubsidy subsidy = housingSubsidyMapper.getHousingSubsidy(optypenum, recyear, recnum);
        if (subsidy == null) {
            subsidy = new HousingSubsidy();
        }
        model.addAttribute("subsidy", subsidy);
    }

    @Transactional
    @Override
    public Object saveHousingSubsidyInfo(String json) {
        HousingSubsidy housingSubsidy = JSON.parseObject(json, HousingSubsidy.class);
        //查找是否已分房，分房就不能再享受住房补贴
        Integer optypenum = housingSubsidy.getOptypenum();
        Integer recyear = housingSubsidy.getRecyear();
        Integer recnum = housingSubsidy.getRecnum();
        String houseId = houseProjectMapper.getHouseReceive(optypenum,recyear,recnum);
        if (houseId == null) {
            if (housingSubsidy != null) {
                try {
                    //获取uuid设置到合同id里
                    housingSubsidy.setId(StaticClass.getUUID());
                    housingSubsidy.setOprationId(ShiroKit.getUser().getId());
                    housingSubsidy.setCreateTime(new Date());
                    housingSubsidyMapper.addHousingSubsidy(housingSubsidy);
                    //修改state
                    Map<String,Object> param = new HashMap<>();
                    param.put("iOpTypeNum", optypenum);
                    param.put("iRecYear",recyear);
                    param.put("iRecNum",recnum);
                    param.put("status","4");//2为轮候，3位已配房，4位住房补贴
                    houseProjectMapper.updatePeopleStatus(param);
                    return "Success";
                } catch (Exception e) {
                    return "Error";
                }
            } else {
                return "Error";
            }
        } else {
            return "Already";
        }
    }

    @Override
    public Map lookSubsidy(Integer optypenum, Integer recyear, Integer recnum) {
        Map<Object,Object > subsidyMap=new HashMap<>();
        //获取申请人数据
        FamilySurvey apply = getFamilySurvey(optypenum, recyear, recnum);
        subsidyMap.put("apply", apply);
        //获取住房补贴数据,根据解除时间是否有值来判断是否为有效数据
        HousingSubsidy subsidy = housingSubsidyMapper.getHousingSubsidy(optypenum, recyear, recnum);
        if (subsidy == null) {
            subsidy = new HousingSubsidy();
        }
        subsidyMap.put("subsidy", subsidy);
        return subsidyMap;
    }

    @Transactional
    @Override
    public String relieveHousingSubsidy(Integer optypenum, Integer recyear, Integer recnum) {
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum", optypenum);
        param.put("iRecYear",recyear);
        param.put("iRecNum",recnum);
        param.put("status","2");//2为轮候，3位已配房，4位住房补贴
        houseProjectMapper.updatePeopleStatus(param);
        housingSubsidyMapper.updateHousingSubsidy(optypenum,recyear,recnum,ShiroKit.getUser().getId());
        return "SUCCESS";
    }

    @Override
    public Page<HousingSubsidyVO> queryALLHousingSubsidy(int iStart, int iEnd, String name) {
        Page<HousingSubsidyVO> page = new Page<HousingSubsidyVO>();
        List<HousingSubsidyVO> list = housingSubsidyMapper.queryALLHousingSubsidy(iStart, iEnd, name);
        if (list.size() > 0) {
            page.setRecords(list);
            page.setTotal(list.get(0).getTotal());
            page.setCurrent(iStart / (iEnd - iStart) + 1);
        }
        return page;
    }

    @Override
    public Page<HousingSubsidyVO> getPersonAllHousingSubsidy(Integer optypenum, Integer recyear, Integer recnum) {
        Page<HousingSubsidyVO> page = new PageFactory().defaultPage();
        List<HousingSubsidyVO> list = housingSubsidyMapper.getPersonAllHousingSubsidy(page, optypenum, recyear, recnum);
        page.setRecords(list);
        return page;
    }

    /**
     * 获取申请人信息
     *
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    public FamilySurvey getFamilySurvey(Integer optypenum, Integer recyear, Integer recnum) {
        FamilySurvey apply = houseProjectMapper.getFamilySurvey(optypenum, recyear, recnum);
        //获取证件类型
        String cardIdType = apply.getOWNERCERTTYPECODE();
        //通过字典获取类型中文名
        String name = dicMapper.getNameByIdCard(cardIdType);
        //设置到apply
        apply.setOWNERCERTTYPECODE(name);
        return apply;
    }

}
