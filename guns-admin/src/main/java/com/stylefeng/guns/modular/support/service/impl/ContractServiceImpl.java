package com.stylefeng.guns.modular.support.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.ContractMapper;
import com.stylefeng.guns.modular.support.dao.DicMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.*;
import com.stylefeng.guns.modular.support.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements IContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Autowired
    private DicMapper dicMapper;


    @Override
    public Page<ContractVO> queryAllAlreadyAllocatedRoom(int iStart, int iEnd, String name) {
        Page<ContractVO> page = new Page<ContractVO>();
        List<ContractVO> contractList = contractMapper.queryAllAlreadyAllocatedRoom(iStart, iEnd, name);
        if (contractList.size() > 0) {
            page.setRecords(contractList);
            page.setTotal(contractList.get(0).getTotal());
            page.setCurrent(iStart / (iEnd - iStart) + 1);
        }
        return page;
    }

    /**
     * 获取申请人信息
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

    public void addContract(Contract contract) {
        //获取uuid设置到合同id里
        contract.setId(StaticClass.getUUID());
        contract.setOprationId(ShiroKit.getUser().getId());
        contract.setCreateTime(new Date());
        contractMapper.addContract(contract);
    }

    @Override
    public void exportPdf(Model model, Integer optypenum, Integer recyear, Integer recnum, Date endTime) {
        Map param = new HashMap();
        param.put("iOptypenum", optypenum);
        param.put("iRecyear", recyear);
        param.put("iRecnum", recnum);
        //获取承租方信息：姓名和身份证号码
        FamilySurvey apply = houseProjectMapper.getFamilySurvey(optypenum, recyear, recnum);
        model.addAttribute("apply", apply);
        //获取房屋地址：小区地址+栋+室+房屋结构+建筑面积
        ContractVO houseInfo = contractMapper.houseInfo(optypenum, recyear, recnum);
        model.addAttribute("area", doubleTrans(houseInfo.getArchitArea()));
        model.addAttribute("houseInfo", houseInfo);
        //获取合同设置的时间：年审情况，合同租金/每平米·月，计算出月租金，月租金大写，半年租金，半年租金大写
        Contract contract = contractMapper.getEffectiveContractForPrint(optypenum, recyear, recnum, endTime);
        model.addAttribute("contract", contract);
        //获取合同开始和结束时间的年月日
        Date start = contract.getStartTime();
        Date end = contract.getEndTime();
        Map dateMap = dateTrans(start, end);
        model.addAttribute("dateInfo", dateMap);
        //获取合同结束的年月日
        //将租金信息 计算 月租金，月租金大写，半年租金，半年租金大写后放在map里
        double rent = contract.getPrice();
        double housingArea = houseInfo.getArchitArea();
        double monthRent = rent * housingArea;
        double halfYear = rent * housingArea * 6;
        String monthRentCapitalization = Convert.digitUppercase(monthRent);
        String halfYearCapitalization = Convert.digitUppercase(halfYear);
        Map map = new HashMap();
        map.put("rent", doubleTrans(contract.getPrice()));//租金/每平米·月
        map.put("monthRent", doubleTrans(monthRent));//月租金
        map.put("halfYear", doubleTrans(halfYear));//半年租金
        map.put("monthRentCapitalization", monthRentCapitalization);//大写月租金
        map.put("halfYearCapitalization", halfYearCapitalization);//大写半年租金
        model.addAttribute("rentInfo", map);
        //共同居住人情况
        List<JointApplicant> jointApplicantList = houseProjectMapper.selectJointApplicant(param);
        model.addAttribute("jointApplicantList", jointApplicantList);
        model.addAttribute("jointApplicantListOther", 5 - jointApplicantList.size());
    }

    @Override
    public Page<ContractVO> getPersonAllContract(Integer optypenum, Integer recyear, Integer recnum) {
        Page<ContractVO> page = new PageFactory().defaultPage();
        List<ContractVO> contractList = contractMapper.getPersonAllContract(page, optypenum, recyear, recnum);
        page.setRecords(contractList);
        return page;
    }

    @Override
    public void contractDetail(Model model, Integer optypenum, Integer recyear, Integer recnum) {
        //获取申请人数据
        FamilySurvey apply = getFamilySurvey(optypenum, recyear, recnum);
        //通过houseID获取到房屋信息
        House house = contractMapper.queryHouseInfo(optypenum, recyear, recnum);
        //获取合同信息,过期的合同不返回,只有有效的合同才有数据
        Contract contract = contractMapper.getEffectiveContract(optypenum, recyear, recnum);
        if (contract == null) {
            contract = new Contract();
        }
        model.addAttribute("apply", apply);
        model.addAttribute("house", house);
        model.addAttribute("contract", contract);
    }

    @Override
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
                        contractMapper.updateContract(contract);
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
     * double类型如果小数点后为0,则显示整数，，否则保留, 返回string
     */
    private String doubleTrans(double d) {
        return String.valueOf(new DecimalFormat().format(d));
    }

    /**
     * 将时间转换成年月日，返回map
     *
     * @param start
     * @param end
     * @return
     */
    private Map dateTrans(Date start, Date end) {
        String startYear = String.format("%tY", start);
        String startMon = String.format("%tm", start);
        String startDay = String.format("%td", start);
        String endYear = String.format("%tY", end);
        String endMon = String.format("%tm", end);
        String endDay = String.format("%td", end);
        Map map = new HashMap();
        map.put("startYear", startYear);
        map.put("startMon", startMon);
        map.put("startDay", startDay);
        map.put("endYear", endYear);
        map.put("endMon", endMon);
        map.put("endDay", endDay);
        return map;
    }
}
