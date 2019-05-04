package com.stylefeng.guns.modular.support.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.AllotRoomMapper;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.dao.ContractMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.service.AllotRoomService;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.IHouseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllotRoomServiceImpl implements AllotRoomService {

    @Autowired
    private AllotRoomMapper allotRoomMapper;
    @Autowired
    private HouseProjectMapper houseProjectMapper;
    @Autowired
    private BuildMapper buildMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private DicService dicService;
    @Autowired
    private IHouseProjectService houseProjectService;

    /**
     * 配房人员信息查询查询
     * 方法参数为Map,使用aop切入设置userId：详情查看CurUserAspect,方法名称后缀为Aop
     *
     * @param param
     * @return
     */
    @Override
    public Page<Map<String, Object>> allotRoomQueryAop(Map param) {
        Page<Map<String, Object>> page = new PageFactory().defaultPage();
        List<Map<String, Object>> list = allotRoomMapper.allotRoomQuery(page,param);
        page.setRecords(list);
        return page;
    }

    /**
     * 获取房屋信息
     *
     * @param OpTypeNum
     * @param RecYear
     * @param RecNum
     * @return
     */
    @Override
    public Map findHouse(String OpTypeNum, String RecYear, String RecNum) {
        Map<Object, Object> houseMap = new HashMap<>();
        //获取房屋Id
        String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum), Convert.toInt(RecYear), Convert.toInt(RecNum));
        if (houseId != null && !("").equals(houseId)) {
            //分配房了则获取房屋信息
            House house = buildMapper.getHouse(houseId);
            FamilySurvey familySurvey = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum), Convert.toInt(RecYear), Convert.toInt(RecNum));
            houseMap.put("house", house);
            houseMap.put("obligee", familySurvey.getApplicantName());//获取权利人
            return houseMap;

        } else {
            return new HashMap<>();
        }
    }

    /**
     * 解除配房
     * @param OpTypeNum
     * @param RecYear
     * @param RecNum
     * @return
     */
    @Override
    public String relieveHouse(String OpTypeNum, String RecYear, String RecNum) {
        Map<String, Object> param = new HashMap<>();
        param.put("iOpTypeNum", Convert.toInt(OpTypeNum));
        param.put("iRecYear", Convert.toInt(RecYear));
        param.put("iRecNum", Convert.toInt(RecNum));
        //获取房屋Id
        String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum), Convert.toInt(RecYear), Convert.toInt(RecNum));
        if (houseId == null && ("").equals(houseId)) {
            return "EXIT";
        } else {
            contractMapper.deleteEffectiveContract(Convert.toInt(OpTypeNum), Convert.toInt(RecYear), Convert.toInt(RecNum));
            param.put("checkHouseId", houseId);
            //修改房屋状态,0：空房
            param.put("houseCode", 0);
            houseProjectMapper.updateHouseCode(param);
            //修改人员状态，2：轮候
            param.put("status", "2");
            houseProjectMapper.updatePeopleStatus(param);
            //清除houseId，置为null
            param.put("checkHouseId", null);
            houseProjectMapper.allotHouse(param);
            return "SUCCESS";
        }
    }
}
