package com.stylefeng.guns.modular.support.service.impl;


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
import java.util.*;

@Service
public class ContractServiceImpl implements IContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Autowired
    private DicMapper dicMapper;


    @Override
    public List<ContractUser> queryAllAlreadyAllocatedRoom(String name) {
        return contractMapper.queryAllAlreadyAllocatedRoom(name);
    }

    @Override
    public House queryHouseInfo(Integer optypenum, Integer recyear, Integer recnum) {
        return contractMapper.queryHouseInfo(optypenum, recyear, recnum);
    }

    @Override
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

    @Override
    public Contract getContract(Integer optypenum, Integer recyear, Integer recnum) {
        return contractMapper.getContract(optypenum, recyear, recnum);
    }

    @Override
    public Contract getEffectiveContract(Integer optypenum, Integer recyear, Integer recnum) {
        return contractMapper.getEffectiveContract(optypenum, recyear, recnum);
    }

    @Override
    public void addContract(Contract contract) {
        contractMapper.addContract(contract);
    }

    @Override
    public void updateContract(Contract contract) {
        contractMapper.updateContract(contract);
    }

    @Override
    public void deleteEffectiveContract(Integer optypenum, Integer recyear, Integer recnum) {
        //先查询是否有合同数据，如果有合同数据，可能会存在多条（有效和失效）
        Contract contract = contractMapper.getContract(optypenum, recyear, recnum);
        if (contract != null) {
            contractMapper.deleteEffectiveContract(optypenum, recyear, recnum);
        }
    }

    @Override
    public void exportPdf(Model model, Integer optypenum, Integer recyear, Integer recnum) {
        Map param = new HashMap();
        param.put("iOptypenum", optypenum);
        param.put("iRecyear", recyear);
        param.put("iRecnum", recnum);
        //获取承租方信息：姓名和身份证号码
        FamilySurvey apply = houseProjectMapper.getFamilySurvey(optypenum, recyear, recnum);
        model.addAttribute("apply", apply);
        //获取房屋地址：小区地址+栋+室+房屋结构+建筑面积
        ContractUser houseInfo = contractMapper.houseInfo(optypenum, recyear, recnum);
        model.addAttribute("area",doubleTrans(houseInfo.getArchitArea()));
        model.addAttribute("houseInfo", houseInfo);
        //获取合同设置的时间：年审情况，合同租金/每平米·月，计算出月租金，月租金大写，半年租金，半年租金大写
        Contract contract = contractMapper.getEffectiveContract(optypenum, recyear, recnum);
        model.addAttribute("contract", contract);
        //获取合同开始和结束时间的年月日
        Date start = contract.getStartTime();
        Date end = contract.getEndTime();
        Map dateMap = dateTrans(start,end);
        model.addAttribute("dateInfo",dateMap);
        //获取合同结束的年月日
        //将租金信息 计算 月租金，月租金大写，半年租金，半年租金大写后放在map里
        double rent = contract.getPrice();
        double housingArea = houseInfo.getArchitArea();
        double monthRent = rent * housingArea;
        double halfYear = rent * housingArea * 6;
        String monthRentCapitalization = Convert.digitUppercase(monthRent);
        String halfYearCapitalization = Convert.digitUppercase(halfYear);
        String num = doubleTrans(100.2);
        Map map = new HashMap();
        map.put("rent",doubleTrans(contract.getPrice()));//租金/每平米·月
        map.put("monthRent", doubleTrans(monthRent));//月租金
        map.put("halfYear", doubleTrans(halfYear));//半年租金
        map.put("monthRentCapitalization", monthRentCapitalization);//大写月租金
        map.put("halfYearCapitalization", halfYearCapitalization);//大写半年租金
        model.addAttribute("rentInfo", map);
        //共同居住人情况
        List<JointApplicant> jointApplicantList = houseProjectMapper.selectJointApplicant(param);
        model.addAttribute("jointApplicantList", jointApplicantList);
        List<JointApplicant> jointApplicantListOther = new ArrayList<JointApplicant>();
        for (int i = 0; i < 5-jointApplicantList.size(); i++){
            jointApplicantListOther.add(new JointApplicant());
        }
        model.addAttribute("jointApplicantListOther",jointApplicantListOther);
    }

    /**
     * double类型如果小数点后为0,则显示整数，，否则保留, 返回string
     */
    private String doubleTrans(double d) {
        return String.valueOf(new DecimalFormat().format(d));
    }

    /**
     * 将时间转换成年月日，返回map
     * @param start
     * @param end
     * @return
     */
    private Map dateTrans(Date start,Date end){
        String startYear=String.format("%tY", start);
        String startMon=String .format("%tm", start);
        String startDay=String .format("%td", start);
        String endYear=String.format("%tY", end);
        String endMon=String .format("%tm", end);
        String endDay=String .format("%td", end);
        Map map = new HashMap();
        map.put("startYear",startYear);
        map.put("startMon",startMon);
        map.put("startDay",startDay);
        map.put("endYear",endYear);
        map.put("endMon",endMon);
        map.put("endDay",endDay);
        return map;
    }
}
