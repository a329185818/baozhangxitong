package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface HouseProjectMapper {

    /**
     * 获取一级项目类型
     * @return
     */
    List<Map<String,Object>> getFirstBusinessType(Integer opgroupcode);

    /**
     * 二级项目类型
     * @return
     */
    List<Map<String,Object>> getSecondBusinessType(@Param("optypenum") Integer optypenum);

    /**
     * 收件人列表
     * @param param
     * @return
     */
    Map<String, Object> getRecipients(Map param);

    /**
     * 保障房项目列表
     * @param param
     * @return
     */
    Map<String, Object> projectList(Map param);

    /**
     * 判断当前用户是否有权限打开详细页
     * @param param.sRecNumGather 登记编号 默认null
     * @param param.iUserNum 用户编号
     * @param param.icount 0 "提示", "此案卷已转至下一阶段,请在信息查询中查看"，>0 调用 up_LogicId_Disp，判断该业务打开哪个界面
     * @return
     */
    void IsHavePowerOpenRec(Map param);

    /**
     * iOpTypeNum
     *iRecYear
     *iRecNum
     *sPage
     *sMsg
     * @param param
     */
    void openPage(Map param);

    /**
     * 判断案卷是否被锁定/解锁
     *iOpTypeNum
     *iRecYear
     *iRecNum
     *sUserNum
     *iType
     *sMsg
     *iSuccess
     * @param param
     */
    void lockRecOrNot(Map param);

    /**
     * 当前案卷基本信息
     *sRecNumGather
     *bSuccess
     *RCSurface
     * @param param
     */
    void recListCurrent(Map param);

    /**
     * 获取项目onwer详细
     * @param param
     * @return
     */
    Map<String,Object> getProjectDetail(Map param);

    /**
     * 检查用户创建项目权限
     * @param param
     * @return
     */
    Map<String,Object> checkCreatePermission(Map param);

    /**
     * 生成新项目
     */
    void createNewRec(Map param);

    /**
     * 保存项目细项
     * @param recOwner
     */
    void saveRecOwner(RecOwner recOwner);

    //获取办理节点列表
    List<Map<String,Object>> getNodeList(Map map);

    /**
     * 获取家庭情况调查表
     * @param OPTYPENUM
     * @param RECYEAR
     * @param RECNUM
     * @return
     */
    FamilySurvey getFamilySurvey(@Param("OPTYPENUM") Integer OPTYPENUM,@Param("RECYEAR") Integer RECYEAR,@Param("RECNUM") Integer RECNUM);

    /**
     * 材料列表
     * @param map
     */
    void getArchList(Map map);

    List<Integer> getMatInfo(file_yuVo file_yuVo);

    /**
     * 获取转交对象
     * @param map
     * @return
     */
    void getRecSendTarget(Map map);

    /**
     * 至下阶段
     * @param map
     */
    void sendRecToNext(Map map);

    /**
     * 至转交箱
     */
    void toTransferBox(Map map);

    //保存房屋信息
    void saveFamilySurvey(FamilySurvey familySurvey);

    //保存
    void saveHandleNode(HandleNode handleNode);

    //更新
    void updateOpion(HandleNode handleNode);

    //更新退回信息
    void updateReturnOpion(HandleNode handleNode);

    //保存共同申请人
    void addJointApplicant(JointApplicant jointApplicant);

    //删除共同申请人
    void deleteJointApplicant(Map param);

    //查找共同申请人
    List<JointApplicant> selectJointApplicant(Map param);

    //保存信息
    void saveOtherFamilySurvey(ApplicantReason applicantReason);

    //查找栋
    List<Build> buildList(Map param);

    //查找楼盘表项目
    List<Build> buildProjectList(Map param);

    //查找是否已分房
    String getHouseReceive(@Param("OPTYPENUM") Integer OPTYPENUM,@Param("RECYEAR") Integer RECYEAR,@Param("RECNUM") Integer RECNUM);

    //分配房屋
    void allotHouse(Map param);

    //修改房屋状态
    void updateHouseCode(Map param);

    //腾退房屋查询
    void houseSearch(Map param);

    //更新配偶信息
    void updateFamilySurveySpouse(FamilySurvey familySurvey);

    //保存信息
    void savePicture(Map<String, Object> param);

    void setCivilServant(@Param("OPTYPENUM") String OPTYPENUM,@Param("RECYEAR") String RECYEAR,@Param("RECNUM") String RECNUM);

    //保存共同申请人图像
    void saveJointPicture(Map<String, Object> param);

    //返回共同申请人
    JointApplicant getJointApplicant(@Param("OPTYPENUM") Integer OPTYPENUM,@Param("RECYEAR") Integer RECYEAR,@Param("RECNUM") Integer RECNUM,@Param("FamilyID") Integer FamilyID);

    /**
     * 退件
     * @param param
     */
    void refund(Map<String, Object> param);

    //根据人员状态过滤人员
    List<FamilySurvey> peopleList(Map<String, Object> param);

    /**
     * 修改人员状态
     * @param param
     */
    void updatePeopleStatus(Map<String, Object> param);

    //获得人员状态
    String getStatusByCard(@Param("idCard") String idCard);

    Map<String,Object> getInfomation(@Param("idCard") String idCard);

    //批量导入申请人信息
    int insertTbbwimport(Tbbwimport tbbwimport);
    //导出申请人信息
    Tbbwimport findIdTbbwimport(@Param("id")String id);
}
