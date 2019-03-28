package com.stylefeng.guns.modular.support.service;

import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.HandleNode;
import com.stylefeng.guns.modular.support.model.JointApplicant;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface IHouseProjectService {

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

    /**
     * 详细页数据
     * @param OpTypeNum
     * @param RecYear
     * @param RecNum
     * @return
     */
    Object projectData(String OpTypeNum,String RecYear,String RecNum);

    /**
     * 解锁
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    Integer unlock(String iOptypenum,String iRecyear,String iRecnum);

    /**
     * 检查是否有权限创建项目
     * @param iOpTypeNum
     * @return
     */
    boolean checkCreatePermission(String iOpTypeNum);

    /**
     * 生成新项目
     * @param iOpTypeNum
     * @param iOpPartNum
     */
    Map<String,Object> createNewRec(String iOpTypeNum, String iOpPartNum);

    /**
     * 获取要打开的页面链接
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    String getPage(String iOpTypeNum,String iRecYear,String iRecNum);

    /**
     * 至下阶段
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    Integer nextState(String iOpTypeNum,String iRecYear,String iRecNum,HandleNode handleNode);

    /**
     * 至转交箱
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    Integer toTransferBox(String iOpTypeNum,String iRecYear,String iRecNum);

    /**
     * 退回
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    Integer upState(String iOpTypeNum,String iRecYear,String iRecNum,HandleNode handleNode,String checkNum);

    /**
     * 保存家庭信息
     * @param familySurvey
     */
    void saveFamilySurvey(FamilySurvey familySurvey);

    /**
     * 保存共同申请人
     * @param jointApplicant
     */
    void addJointApplicant(JointApplicant jointApplicant);

    /**
     * 选择房屋
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @param houseCode
     */
    Object checkHouse(String iOpTypeNum,String iRecYear,String iRecNum,String checkHouseId,String houseCode);

    Object houseSearch(String iOpTypeNum,String iRecYear,String iRecNum,String idCard);

    /**
     * 更新配偶信息
     * @param familySurvey
     */
    void updateFamilySurveySpouse(FamilySurvey familySurvey);

    void savePicture(byte[] avatar, String OpTypeNum, String RecYear, String RecNum, String judge, String FamilyID);

    /**
     * 退件
     * @param param
     */
    void refund( Map<String,Object> param);
}
