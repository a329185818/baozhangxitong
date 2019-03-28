package com.stylefeng.guns.modular.support.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.core.common.Result;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.HandleNode;
import com.stylefeng.guns.modular.support.model.House;
import com.stylefeng.guns.modular.support.model.JointApplicant;
import com.stylefeng.guns.modular.support.service.IHouseProjectService;
import com.stylefeng.guns.modular.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HouseProjectImpl implements IHouseProjectService {

    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Autowired
    private BuildMapper buildMapper;

    @Override
    public Object projectDetail(String sRecNumGather,String iOpTypeNum,String iRecYear,String iRecNum){

        String userId = ShiroKit.getUser().getId();
        Integer icount = -1;
        Map<String,Object> param = new HashMap();
        param.put("sRecNumGather",sRecNumGather);
        param.put("iUserNum",userId);
        param.put("icount",icount);
        houseProjectMapper.IsHavePowerOpenRec(param);

        //ICOUNT =0 "提示", "此案卷已转至下一阶段,请在信息查询中查看"
        ////ICOUNT >0 调用 up_LogicId_Disp，判断该业务打开哪个界面
        if(Convert.toInt(param.get("icount")) == 0){
            return Result.error(501,"此案卷已转至下一阶段,请在信息查询中查看");
        }else if(Convert.toInt(param.get("icount")) > 0){
            Map<String,Object> openPagePrama = new HashMap<>();
            openPagePrama.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
            openPagePrama.put("iRecYear",Convert.toInt(iRecYear));
            openPagePrama.put("iRecNum",Convert.toInt(iRecNum));
            openPagePrama.put("sPage","");
            openPagePrama.put("sMsg","");
            houseProjectMapper.openPage(openPagePrama);
            if(ToolUtil.isEmpty(openPagePrama.get("sPage"))){
                return Result.error(502,"该业务没有配置相应的输入界面!");
            }

            //如果页面存在
            Map<String,Object> lockRecOrNotParam = new HashMap<>();
            lockRecOrNotParam.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
            lockRecOrNotParam.put("iRecYear",Convert.toInt(iRecYear));
            lockRecOrNotParam.put("iRecNum",Convert.toInt(iRecNum));
            lockRecOrNotParam.put("iUserNum",userId);
            lockRecOrNotParam.put("iType",1);
            lockRecOrNotParam.put("sMsg","");
            lockRecOrNotParam.put("iSuccess",-3);
            houseProjectMapper.lockRecOrNot(lockRecOrNotParam);

            if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == 0 ){
                //锁定案卷失败
                return Result.error(503,"锁定案卷失败!");
            }else if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == 1 || Convert.toInt(lockRecOrNotParam.get("iSuccess")) == -1){
                Map<String,Object> recListCurrentPrama = new HashMap<>();
                recListCurrentPrama.put("sRecNumGather",sRecNumGather);
                recListCurrentPrama.put("bSuccess",-1);
                recListCurrentPrama.put("RCSurface",new ArrayList<Map<String,Object>>());
                houseProjectMapper.recListCurrent(recListCurrentPrama);

                //获取案卷信息失败
                if(recListCurrentPrama.size() == 0){
                    return Result.error(504,"案卷当前信息获取失败!");
                }

                if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == -1){
                    //没有编辑权限
                    openPagePrama.put("readonly",true);
                }else{
                    openPagePrama.put("readonly",false);
                }

                return Result.success("success",openPagePrama);
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Object projectData(String OpTypeNum,String RecYear,String RecNum){
        //获取项目详细
        Map<String,Object> projectDetailParam = new HashMap<>();
        projectDetailParam.put("OpTypeNum",OpTypeNum);
        projectDetailParam.put("RecYear",RecYear);
        projectDetailParam.put("RecNum",RecNum);
        Map<String,Object> project = houseProjectMapper.getProjectDetail(projectDetailParam);
        return project;
    }

    /**
     * 解锁
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    public Integer unlock(String iOptypenum,String iRecyear,String iRecnum){
        String userId = ShiroKit.getUser().getId();
        Map<String,Object> lockRecOrNotParam = new HashMap<>();
        lockRecOrNotParam.put("iOpTypeNum",Convert.toInt(iOptypenum));
        lockRecOrNotParam.put("iRecYear",Convert.toInt(iRecyear));
        lockRecOrNotParam.put("iRecNum",Convert.toInt(iRecnum));
        lockRecOrNotParam.put("iUserNum",userId);
        //解锁
        lockRecOrNotParam.put("iType",2);
        lockRecOrNotParam.put("sMsg","");
        lockRecOrNotParam.put("iSuccess",-1);
        houseProjectMapper.lockRecOrNot(lockRecOrNotParam);

        return Convert.toInt(lockRecOrNotParam.get("iSuccess"));
    }

    /**
     * 检查权限
     * @param iOpTypeNum
     * @return
     */
    public boolean checkCreatePermission(String iOpTypeNum){
        String userId = ShiroKit.getUser().getId();
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum",iOpTypeNum);
        param.put("iUserNum",userId);
        param.put("iUserOpPower",6);
        param.put("iSuccess",-1);
        houseProjectMapper.checkCreatePermission(param);
        if(Convert.toInt(param.get("iSuccess")) == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 生成新项目
     * @param iOpTypeNum
     * @param iOpPartNum
     */
    public Map<String,Object> createNewRec(String iOpTypeNum,String iOpPartNum){
        String userId = ShiroKit.getUser().getId();
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum",iOpTypeNum);
        param.put("iOpPartNum",iOpPartNum);
        param.put("iUserNum",userId);
        param.put("iYear",-1);
        param.put("iMaxRecNum",-1);
        houseProjectMapper.createNewRec(param);
        return param;
    }

    public String getPage(String iOpTypeNum,String iRecYear,String iRecNum){
        Map<String,Object> openPagePrama = new HashMap<>();
        openPagePrama.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        openPagePrama.put("iRecYear",Convert.toInt(iRecYear));
        openPagePrama.put("iRecNum",Convert.toInt(iRecNum));
        openPagePrama.put("sPage","");
        openPagePrama.put("sMsg","");
        houseProjectMapper.openPage(openPagePrama);
        return (String)openPagePrama.get("sPage");
    }

    /**
     * 至下阶段
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     */
    public Integer nextState(String iOpTypeNum,String iRecYear,String iRecNum,HandleNode handleNode){
        Map<String,Object> recSendTarge = new HashMap<>();
        Map<String,Object> sendRecToNext = new HashMap<>();
        recSendTarge.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        recSendTarge.put("iRecYear",Convert.toInt(iRecYear));
        recSendTarge.put("iRecNum",Convert.toInt(iRecNum));
        recSendTarge.put("iSendType",1);
        recSendTarge.put("iState",-1);
        recSendTarge.put("iOPSENDTARGETTYPE",-1);
        recSendTarge.put("sRECNUMGATHER","");
        recSendTarge.put("RCRecSendTarget",new ArrayList<Map<String,Object>>());
        houseProjectMapper.getRecSendTarget(recSendTarge);

        Map<String,Object> returnParam = ((List<Map<String,Object>>)(recSendTarge.get("RCRecSendTarget"))).get(0);
        sendRecToNext.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        sendRecToNext.put("iRecYear",Convert.toInt(iRecYear));
        sendRecToNext.put("iRecNum",Convert.toInt(iRecNum));
        sendRecToNext.put("iSendType",Convert.toInt(returnParam.get("OPSENDTARGETTYPE")));
        sendRecToNext.put("iOpFCNum",Convert.toInt(returnParam.get("OPFLOWCHARTNUM")));
        sendRecToNext.put("iOpFCVer",Convert.toInt(returnParam.get("OPFLOWCHARTVERSION")));
        sendRecToNext.put("iOpFlowPhNum",Convert.toInt(returnParam.get("OPFLOWPHASENUM")));
        sendRecToNext.put("iOPFuncNum",ToolUtil.isEmpty(returnParam.get("OPFUNCNUM")) ? 0 : Convert.toInt(returnParam.get("OPFUNCNUM")));
        sendRecToNext.put("iOPFuncIsUseProm",ToolUtil.isEmpty(returnParam.get("OPFUNCISUSEPROM")) ? 0 : Convert.toInt(returnParam.get("OPFUNCISUSEPROM")));
        sendRecToNext.put("iISRETURN",0);
        sendRecToNext.put("iLockUserNum", ToolUtil.isEmpty(returnParam.get("SENDTOUSERNUM")) ? 0 : Convert.toInt(returnParam.get("SENDTOUSERNUM")));
        sendRecToNext.put("iLoclUserNum",Convert.toInt(ShiroKit.getUser().getId()));
        sendRecToNext.put("iCurRecWorkPhaseNum",Convert.toInt(returnParam.get("CURRECWORKPHASENUM")));
        sendRecToNext.put("srecnumgather",returnParam.get("RECNUMGATHER") + "");
        sendRecToNext.put("iState",-1);

        houseProjectMapper.sendRecToNext(sendRecToNext);

        //设置审批流程编号
        handleNode.setRECWORKPHASENUM(Convert.toInt(returnParam.get("CURRECWORKPHASENUM")));
        //设置审批人
        User user = new User();
        user.setId(ShiroKit.getUser().getId());
        handleNode.setApprover(user);
        houseProjectMapper.updateOpion(handleNode);

        return Convert.toInt(sendRecToNext.get("iState"));
    }

    /**
     * 退回
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     */
    public Integer upState(String iOpTypeNum,String iRecYear,String iRecNum,HandleNode handleNode,String checkNum){
        Map<String,Object> recSendTarge = new HashMap<>();
        Map<String,Object> sendRecToUp = new HashMap<>();
        recSendTarge.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        recSendTarge.put("iRecYear",Convert.toInt(iRecYear));
        recSendTarge.put("iRecNum",Convert.toInt(iRecNum));
        recSendTarge.put("iSendType",2);
        recSendTarge.put("iState",-1);
        recSendTarge.put("iOPSENDTARGETTYPE",-1);
        recSendTarge.put("sRECNUMGATHER","");
        recSendTarge.put("RCRecSendTarget",new ArrayList<Map<String,Object>>());
        houseProjectMapper.getRecSendTarget(recSendTarge);

        for(Map<String,Object> returnParam : (ArrayList<Map<String,Object>>)recSendTarge.get("RCRecSendTarget")){
            if(Convert.toStr(returnParam.get("OPFLOWPHASENUM")).equals(checkNum)){
                sendRecToUp.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
                sendRecToUp.put("iRecYear",Convert.toInt(iRecYear));
                sendRecToUp.put("iRecNum",Convert.toInt(iRecNum));
                sendRecToUp.put("iSendType",2);
                sendRecToUp.put("iOpFCNum",Convert.toInt(returnParam.get("OPFLOWCHARTNUM")));
                sendRecToUp.put("iOpFCVer",Convert.toInt(returnParam.get("OPFLOWCHARTVERSION")));
                sendRecToUp.put("iOpFlowPhNum",Convert.toInt(returnParam.get("OPFLOWPHASENUM")));
                sendRecToUp.put("iOPFuncNum",ToolUtil.isEmpty(returnParam.get("OPFUNCNUM")) ? 0 : Convert.toInt(returnParam.get("OPFUNCNUM")));
                sendRecToUp.put("iOPFuncIsUseProm",ToolUtil.isEmpty(returnParam.get("OPFUNCISUSEPROM")) ? 0 : Convert.toInt(returnParam.get("OPFUNCISUSEPROM")));
                sendRecToUp.put("iLockUserNum", ToolUtil.isEmpty(returnParam.get("SENDTOUSERNUM")) ? 0 : Convert.toInt(returnParam.get("SENDTOUSERNUM")));
                sendRecToUp.put("iLoclUserNum",Convert.toInt(ShiroKit.getUser().getId()));
                sendRecToUp.put("iCurRecWorkPhaseNum",Convert.toInt(returnParam.get("CURRECWORKPHASENUM")));
                sendRecToUp.put("iISRETURN",1);
                sendRecToUp.put("srecnumgather",returnParam.get("RECNUMGATHER") + "");
                sendRecToUp.put("iState",-1);

                houseProjectMapper.sendRecToNext(sendRecToUp);

                //设置审批流程编号
                handleNode.setRECWORKPHASENUM(Convert.toInt(returnParam.get("CURRECWORKPHASENUM")));
                //设置审批人
                User user = new User();
                user.setId(ShiroKit.getUser().getId());
                handleNode.setApprover(user);
                houseProjectMapper.updateReturnOpion(handleNode);

                return Convert.toInt(sendRecToUp.get("iState"));
            }
        }




        return 0;
    }

    /**
     * 至转交箱
     */
    public Integer toTransferBox(String iOpTypeNum,String iRecYear,String iRecNum){
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        param.put("iRecYear",Convert.toInt(iRecYear));
        param.put("iRecNum",Convert.toInt(iRecNum));
        param.put("iSuccess",-1);
        houseProjectMapper.toTransferBox(param);
        return Convert.toInt(param.get("iSuccess"));
    }

    public void saveFamilySurvey(FamilySurvey familySurvey){
        houseProjectMapper.saveFamilySurvey(familySurvey);
    }

    @Override
    public void addJointApplicant(JointApplicant jointApplicant){
        houseProjectMapper.addJointApplicant(jointApplicant);
    }

    @Override
    public Object checkHouse(String iOpTypeNum, String iRecYear, String iRecNum, String checkHouseId, String houseCode) {
        Map<String,Object> param = new HashMap<>();
        param.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        param.put("iRecYear",Convert.toInt(iRecYear));
        param.put("iRecNum",Convert.toInt(iRecNum));
        FamilySurvey familySurvey = houseProjectMapper.getFamilySurvey(Convert.toInt(iOpTypeNum),Convert.toInt(iRecYear),Convert.toInt(iRecNum));
        if(familySurvey.getApplicantName() == null || ("").equals(familySurvey.getApplicantName())){
            return "NOEXIT";
        }
        //获取房屋Id
        String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(iOpTypeNum),Convert.toInt(iRecYear),Convert.toInt(iRecNum));
        if(houseId != null && !("").equals(houseId)){
            return "EXIT";
        }else{
            //recevie表添加houseId
            param.put("checkHouseId",checkHouseId);
            houseProjectMapper.allotHouse(param);
            //修改房屋状态
            param.put("houseCode",houseCode);
            houseProjectMapper.updateHouseCode(param);
            //修改人员状态
            param.put("status","3");
            houseProjectMapper.updatePeopleStatus(param);
            return "SUCCESS";
        }
    }

    @Override
    public Object houseSearch(String iOpTypeNum, String iRecYear, String iRecNum, String idCard) {
        Map<String,Object> param = new HashMap<>();
        param.put("iOptypenum",Convert.toInt(iOpTypeNum));
        param.put("iRecyear",Convert.toInt(iRecYear));
        param.put("iRecnum",Convert.toInt(iRecNum));
        param.put("sOWNERCERTNUM",idCard);
        param.put("iSuccess",0);
        param.put("sMsg","");
        houseProjectMapper.houseSearch(param);
        String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(iOpTypeNum),Convert.toInt(iRecYear),Convert.toInt(iRecNum));
        //分配房了则获取房屋信息
        House house = buildMapper.getHouse(houseId);

        return house;
    }

    @Override
    public void updateFamilySurveySpouse(FamilySurvey familySurvey){
        houseProjectMapper.updateFamilySurveySpouse(familySurvey);
    }

    @Override
    public void savePicture(byte[] avatar, String OpTypeNum, String RecYear, String RecNum, String judge, String FamilyID){
        try {
            // 文件保存路径
//               String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
//                       + avatar.getOriginalFilename();
            // 转存文件
//               avatar.transferTo(new File(filePath));
            Map<String, Object> insertMap = new HashMap<String, Object>();
            insertMap.put("byt", avatar);
            insertMap.put("OPTYPENUM", OpTypeNum);
            insertMap.put("RECYEAR", RecYear);
            insertMap.put("RECNUM", RecNum);
            if(("1").equals(judge)){
                houseProjectMapper.savePicture(insertMap);
            }else{
                insertMap.put("FamilyID", FamilyID);
                houseProjectMapper.saveJointPicture(insertMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refund( Map<String,Object> param){
        houseProjectMapper.refund(param);
    }
}
