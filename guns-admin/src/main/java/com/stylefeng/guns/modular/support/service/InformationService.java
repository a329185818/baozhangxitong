package com.stylefeng.guns.modular.support.service;

import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.HandleNode;
import com.stylefeng.guns.modular.support.model.JointApplicant;

import java.util.Map;

public interface InformationService {

    /**
     * 请求详细页
     *
     * @param sRecNumGather
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    Object projectDetail(String sRecNumGather,String iOpTypeNum,String iRecYear,String iRecNum);

}
