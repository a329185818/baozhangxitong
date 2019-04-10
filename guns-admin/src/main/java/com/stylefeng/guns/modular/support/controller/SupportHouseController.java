package com.stylefeng.guns.modular.support.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.Result;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.BuildMapper;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.*;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.IBuildService;
import com.stylefeng.guns.modular.support.service.IHouseProjectService;
import com.stylefeng.guns.modular.support.service.file_yuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/support")
public class SupportHouseController extends BaseController {
    private String PREFIX = "/supporthouse/";

    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Autowired
    private BuildMapper buildMapper;

    @Autowired
    private IHouseProjectService houseProjectService;

    @Autowired
    private file_yuService file_yuService;

    @Autowired
    private DicService dicService;

    @Autowired
    private IBuildService buildService;

    /**
     * 项目和申请列表
     * @param model
     * @param opgroupcode
     * @return
     */
    @RequestMapping("/project/{opgroupcode}")
    public String index(Model model,@PathVariable Integer opgroupcode){
        Map<String, Object> params = new HashMap();
        Integer isuccess = 0;
        List<Map<String,Object>> businessType = houseProjectMapper.getFirstBusinessType(opgroupcode);
        params.put("userList", new ArrayList<Map<String, Object>>());
        params.put("isuccess",isuccess);
        houseProjectMapper.getRecipients(params);
        model.addAttribute("businessType",businessType);
        model.addAttribute("userList",params.get("userList"));
        return PREFIX + "index.html";
    }

    /**
     * 获取二级业务类型
     * @param pNum
     * @return
     */
    @RequestMapping("/second_type")
    @ResponseBody
    public Object getSecondTypeByPNum(Integer pNum){
        List<Map<String,Object>> typeList = houseProjectMapper.getSecondBusinessType(pNum);
        return typeList;
    }

    /**
     * 获取请求页面
     * @param sRecNumGather
     * @param iOpTypeNum
     * @param iRecYear
     * @param iRecNum
     * @return
     */
    @RequestMapping("/get_page_info")
    @ResponseBody
    public Object projectDetailPage(String sRecNumGather,String iOpTypeNum,String iRecYear,String iRecNum){
        return houseProjectService.projectDetail(sRecNumGather,iOpTypeNum,iRecYear,iRecNum);
    }

    /**
     *项目详细
     */
    @RequestMapping("/detail")
    public String projectDetail(String page,String OpTypeNum,String RecYear,String RecNum,Model model,String info){
        Map param = new HashMap();
        param.put("iOptypenum",OpTypeNum);
        param.put("iRecyear",RecYear);
        param.put("iRecnum",RecNum);
        param.put("rcList",new ArrayList<Map<String,Object>>());
        //节点列表
        houseProjectMapper.getNodeList(param);
        //退件类型
        List<Dic> refundList = dicService.getRefund();
        model.addAttribute("refundList",refundList);

        List<Dic> idCardList = dicService.getIdCard();
        model.addAttribute("idCardList",idCardList);

        //查询材料类型字典值
        List<Dic> materialList = dicService.getMaterial();
        model.addAttribute("materialList",materialList);
        model.addAttribute("materialListJson",JSON.toJSONString(materialList));
        //查询家庭类型字典值
        List<Dic> familyType = dicService.getFamilyType();
        model.addAttribute("familyType",familyType);
        model.addAttribute("familyTypeJson",JSON.toJSONString(familyType));
        //房屋建筑结构字典值
        List<Dic> buildingStructureList = dicService.getBuildingStructure();
        model.addAttribute("buildingStructureListJson",JSON.toJSONString(buildingStructureList));
        //房屋户型字典值
        List<Dic> houseTypeList = dicService.getHouseType();
        model.addAttribute("houseTypeListJson",JSON.toJSONString(houseTypeList));
        //房屋用途字典值
        List<Dic> houseuSageList = dicService.getHouseuSage();
        model.addAttribute("houseuSageListJson",JSON.toJSONString(houseuSageList));

        //基本信息
        if("80".equals(OpTypeNum)){
            Object project = houseProjectService.projectData(OpTypeNum,RecYear,RecNum);
            model.addAttribute("project",project);
        }else if("81".equals(OpTypeNum)){
            //查找该业务的数据
            FamilySurvey apply = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            //判断点击的是否为公租房公务员
            if("1".equals(apply.getCivilServant().toString())){
                model.addAttribute("civilServant","1");
            }else{
                model.addAttribute("civilServant","0");
            }
            Map<String,Object> arch = new HashMap<>();
            arch.put("iOpTypeNum",OpTypeNum);
            arch.put("iRecYear",RecYear);
            arch.put("iRecNum",RecNum);
            arch.put("IsReadOnly",0);
            arch.put("curRec",new ArrayList<>());
            //获取房屋Id
            String houseId = houseProjectMapper.getHouseReceive(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            if(houseId != null && !("").equals(houseId)){
                //分配房了则获取房屋信息
                House house = buildMapper.getHouse(houseId);
                apply.setHouse(house);
                model.addAttribute("houseJson",JSON.toJSONString(apply.getHouse()));
            }else{
                //未配房
                apply.setHouse(null);
                model.addAttribute("houseJson","");
            }
            houseProjectMapper.getArchList(arch);
            model.addAttribute("apply",apply);
            ArrayList<HashMap> arrayList = (ArrayList)arch.get("curRec");
            //获取文件材料最大的数值，便于前端展示
            BigDecimal neWrecmattum = new BigDecimal("1");
            BigDecimal recmattum = new BigDecimal("0");
            for(int i = 0;i<arrayList.size();i++){
                recmattum = (BigDecimal)arrayList.get(i).get("RECMATNUM");
                if(neWrecmattum.compareTo(recmattum)< 1){
                    neWrecmattum = recmattum;
                }
            }
            model.addAttribute("RECMATNUM",neWrecmattum);
            model.addAttribute("archList",arch.get("curRec"));

            //获取共同申请人列表
            List<JointApplicant> jointApplicantList = houseProjectMapper.selectJointApplicant(param);
            //共同申请人为配偶
            JointApplicant spouse = new JointApplicant();
            //获取配偶信息
            for(int i = 0;i<jointApplicantList.size();i++){
                if(jointApplicantList.get(i).getJointApplicantRelation().equals("2") || jointApplicantList.get(i).getJointApplicantRelation().equals("9")){
                    //提取配偶信息
                    spouse = jointApplicantList.get(i);
                    //移除
                    jointApplicantList.remove(i);
                    break;
                }
            }
            model.addAttribute("spouse",spouse);
            model.addAttribute("jointApplicantList",jointApplicantList);
            model.addAttribute("jointApplicantListSize",jointApplicantList.size());
        }else if("82".equals(OpTypeNum)){//腾退业务
            FamilySurvey apply = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            Map<String,Object> arch = new HashMap<>();
            arch.put("iOpTypeNum",OpTypeNum);
            arch.put("iRecYear",RecYear);
            arch.put("iRecNum",RecNum);
            arch.put("IsReadOnly",0);
            arch.put("curRec",new ArrayList<>());
            houseProjectMapper.getArchList(arch);
            model.addAttribute("apply",apply);
            ArrayList<HashMap> arrayList = (ArrayList)arch.get("curRec");
            //获取文件材料最大的数值，便于前端展示
            BigDecimal neWrecmattum = new BigDecimal("1");
            BigDecimal recmattum = new BigDecimal("0");
            for(int i = 0;i<arrayList.size();i++){
                recmattum = (BigDecimal)arrayList.get(i).get("RECMATNUM");
                if(neWrecmattum.compareTo(recmattum)< 1){
                    neWrecmattum = recmattum;
                }
            }
            model.addAttribute("RECMATNUM",neWrecmattum);
            model.addAttribute("archList",arch.get("curRec"));
        }

        if (("1").equals(info)){
            model.addAttribute("judgeInfo",1);
        }else{
            model.addAttribute("judgeInfo",0);
        }

        model.addAttribute("nodeList",param.get("rcList"));
        List<Map<String,Object>> n = (ArrayList)param.get("rcList");
        List<Map<String,Object>> node =  (List)param.get("rcList");
        model.addAttribute("lastNode",node.get(node.size()-1).get("OPFLOWPHASENAME").toString());
        if(node.get(node.size()-1).get("OPFLOWPHASENAME").toString().equals("配房")){
            model.addAttribute("isPeiFang","1");
        }else{
            model.addAttribute("isPeiFang","0");
        }
        return page;
    }


    /**
     * 创建新项目和申请
     * @param iOpTypeNum
     * @param iOpPartNum
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object create(String iOpTypeNum,String iOpPartNum,String isCivilServant){
        boolean isHavePermission = houseProjectService.checkCreatePermission(iOpTypeNum);
        if(isHavePermission){
            Map<String,Object> newRec = houseProjectService.createNewRec(iOpTypeNum,iOpPartNum);

            String iYear = String.valueOf(newRec.get("iYear"));
            String iMaxRecNum = String.valueOf(newRec.get("iMaxRecNum"));
            String page = houseProjectService.getPage(iOpTypeNum,iYear,iMaxRecNum);
            if( "".equals(page) ){
                return Result.error(502,"该业务没有配置相应的输入界面!");
            }
            if(("1").equals(isCivilServant)){
                houseProjectMapper.setCivilServant(iOpTypeNum,iYear,iMaxRecNum);
            }
            newRec.put("sPage",page);
            return Result.success("操作成功",newRec);
        }else {
            return Result.error(505,"无权限");
        }
    }

    /**
     * 保存记录
     * @param recOwner
     * @return
     */
    @RequestMapping("/save_recowner")
    @ResponseBody
    public Object saveRecOwner(RecOwner recOwner){
        houseProjectMapper.saveRecOwner(recOwner);
        return Result.success("保存成功！");
    }

    /**
     * 解锁
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    @RequestMapping("/unlock")
    @ResponseBody
    public Integer unlock(String iOptypenum,String iRecyear,String iRecnum){
        Integer isSuccess = houseProjectService.unlock(iOptypenum,iRecyear,iRecnum);
        return isSuccess;
    }

    /**
     * 节点列表
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @param model
     * @return
     */
    @RequestMapping("/node_list")
    public String nodeList(String iOptypenum,String iRecyear,String iRecnum,Model model){
        Map param = new HashMap();
        param.put("iOptypenum",iOptypenum);
        param.put("iRecyear",iRecyear);
        param.put("iRecnum",iRecnum);
        param.put("rcList",new ArrayList<Map<String,Object>>());
        houseProjectMapper.getNodeList(param);
        model.addAttribute("nodeList",param.get("rcList"));

        return PREFIX + "node.html";
    }

    /**
     * 图片上传页面
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @param iRecMatnum
     * @param model
     * @return
     */
    @RequestMapping("/open_upload")
    public String uploadPage(String iOptypenum,String iRecyear,String iRecnum,String iRecMatnum,Model model){

        model.addAttribute("iOptypenum",iOptypenum);
        model.addAttribute("iRecyear",iRecyear);
        model.addAttribute("iRecnum",iRecnum);
        model.addAttribute("iRecMatnum",iRecMatnum);
        return PREFIX + "upload_page.html";
    }

    /**
     * 至下阶段
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    @RequestMapping("/to_next")
    @ResponseBody
    public Object sendRecToNext(String iOptypenum,String iRecyear,String iRecnum,String json){
        HandleNode handleNode =  JSON.parseObject(json,HandleNode.class);

        Integer state = houseProjectService.nextState(iOptypenum,iRecyear,iRecnum,handleNode);

        /*黄辅湘*/
        Map param = new HashMap();
        param.put("iOptypenum",iOptypenum);
        param.put("iRecyear",iRecyear);
        param.put("iRecnum",iRecnum);
        param.put("rcList",new ArrayList<Map<String,Object>>());
        //节点列表
        houseProjectMapper.getNodeList(param);
        List<Map<String,Object>> node =  (List)param.get("rcList");
        if(node.get(node.size()-1).get("OPFLOWPHASENAME").toString().equals("配房")){
            //修改人员状态
            param.put("status","2");
            param.put("iOpTypeNum",Convert.toInt(iOptypenum));
            param.put("iRecYear",Convert.toInt(iRecyear));
            param.put("iRecNum",Convert.toInt(iRecnum));
            houseProjectMapper.updatePeopleStatus(param);
        }else{

        }

        return state;
    }

    /**
     * 至转交箱
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    @RequestMapping("/to_transfer_box")
    @ResponseBody
    public Object toTransferBox(String iOptypenum,String iRecyear,String iRecnum){
        Integer state = houseProjectService.toTransferBox(iOptypenum,iRecyear,iRecnum);
        return state;
    }

    /**
     * 退回节点列表
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @param model
     * @return
     */
    @RequestMapping("/return_node")
    public String returnNodeList(String iOptypenum,String iRecyear,String iRecnum,Model model){
        Map<String,Object> recSendTarge = new HashMap<>();
        recSendTarge.put("iOpTypeNum",Convert.toInt(iOptypenum));
        recSendTarge.put("iRecYear",Convert.toInt(iRecyear));
        recSendTarge.put("iRecNum",Convert.toInt(iRecnum));
        recSendTarge.put("iSendType",2);
        recSendTarge.put("iState",-1);
        recSendTarge.put("iOPSENDTARGETTYPE",-1);
        recSendTarge.put("sRECNUMGATHER","");
        recSendTarge.put("RCRecSendTarget",new ArrayList<Map<String,Object>>());
        houseProjectMapper.getRecSendTarget(recSendTarge);
        model.addAttribute("nodeList",recSendTarge.get("RCRecSendTarget"));

        model.addAttribute("iOptypenum",iOptypenum);
        model.addAttribute("iRecyear",iRecyear);
        model.addAttribute("iRecnum",iRecnum);
        return PREFIX + "return_node.html";
    }

    /**
     * 退回
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @return
     */
    @RequestMapping("/to_up")
    @ResponseBody
    public Object backUp(String iOptypenum,String iRecyear,String iRecnum,String json,String checkNum){
        HandleNode handleNode =  JSON.parseObject(json,HandleNode.class);

        Integer state = houseProjectService.upState(iOptypenum,iRecyear,iRecnum,handleNode,checkNum);
        return state;
    }

    /**
     * 图片展示页面
     * @param iOptypenum
     * @param iRecyear
     * @param iRecnum
     * @param iRecMatnum
     * @param model
     * @return
     */
    @RequestMapping("/show_image")
    public String showImagePage(String iOptypenum,String iRecyear,String iRecnum,String iRecMatnum,Model model){

        file_yuVo file = new file_yuVo();
        file.setOptypenum(Convert.toInt(iOptypenum));
        file.setRecyear(Convert.toInt(iRecyear));
        file.setRecnum(Convert.toInt(iRecnum));
        file.setRecmatnum(Convert.toInt(iRecMatnum));
        List<Integer> imageIdList = houseProjectMapper.getMatInfo(file);
        model.addAttribute("imageIdList",imageIdList);
        return PREFIX + "show_image.html";
    }

    /**
     * 项目和申请列表
     * @param offset
     * @param limit
     * @param sOPGROUPCODE
     * @param sOPTYPENUM
     * @param sOPPARTNUM
     * @param sRecNumGather
     * @param iBATCHNUM
     * @param StartDate
     * @param EndDate
     * @param iFilterMode
     * @param iRecUserNum
     * @param IsEspacial
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object projectList(Integer offset,Integer limit,String sOPGROUPCODE,String sOPTYPENUM,String sOPPARTNUM,String sRecNumGather ,Integer iBATCHNUM ,String StartDate,String EndDate,Integer iFilterMode,Integer iRecUserNum,Integer IsEspacial){

        String userid = ShiroKit.getUser().getId();

        List<Map<String,Object>> projectList = new ArrayList<Map<String,Object>>();
        Map<String,Object> param = new HashMap<>();
        param.put("sUserNum",userid);//用户ID
        param.put("sOPGROUPCODE",sOPGROUPCODE);//项目 30，保障房申请 31
        param.put("sOPTYPENUM",sOPTYPENUM);//业务类型编号
        param.put("sOPPARTNUM",sOPPARTNUM);//业务细项编号
        param.put("sBarCode",null);//条形码，默认 NULL
        param.put("sRecNumGather",sRecNumGather);//综合条件
        param.put("iBATCHNUM",iBATCHNUM);//批号，默认 NULL
        param.put("StartDate",StartDate);//开始日期
        param.put("EndDate",EndDate);//结束日期
        param.put("iFilterMode",iFilterMode);//1,待办件，esle，所有件
        param.put("iRecUserNum",iRecUserNum);//收件人编号
        param.put("ischecked",null);//是否签收，NULL
        param.put("sRECSTATECODE",1);//案卷状态，1
        param.put("iStart",offset);//0
        param.put("iEnd",limit + offset);//NULL
        param.put("IsEspacial",IsEspacial);//转交箱复选框
        param.put("iStep",null);//阶段

        param.put("bSuccess",null);//NULL
        param.put("iCount",0);//转交箱复选框
        param.put("RCSurface",projectList);//阶段

        houseProjectMapper.projectList(param);
        projectList = (List)param.get("RCSurface");
        Page page = new Page();
        page.setRecords(projectList);
        page.setTotal(Convert.toInt(param.get("iCount"),0));
        page.setCurrent(offset / limit + 1);
        return super.packForBT(page);
    }

    /**
     * 保存附件方法
     * @param file_yuVoList
     * @return
     */
    @RequestMapping(value = "/save_file_yu")
    @ResponseBody
    public Object saveFileYu(@RequestBody List<file_yuVo> file_yuVoList) {

            sbjbxx_yuVo sbjbxxYuVo = new sbjbxx_yuVo();
            sbjbxxYuVo.setOptypenum(file_yuVoList.get(0).getOptypenum());
            sbjbxxYuVo.setRecnum(file_yuVoList.get(0).getRecnum());
            sbjbxxYuVo.setRecyear(file_yuVoList.get(0).getRecyear());
            List<file_yuVo> file_yuVos = file_yuService.selectFileInfo(sbjbxxYuVo);
            //循环判断该数据是否已存进数据库
            for(file_yuVo file_yuVo:file_yuVoList){
                //用于判断是否存在
                boolean isExist = false;
                for(file_yuVo oleFile_yuVo:file_yuVos){
                    //如果两个相同，则说明是同一条数据，采用更新方法，并从数据库查询出的list移除此数据
                    if(oleFile_yuVo.getRecmatnum() == file_yuVo.getRecmatnum()){
                        file_yuService.updateMat(file_yuVo);
                        file_yuVos.remove(oleFile_yuVo);
                        isExist = true;
                        break;
                    }
                }
                //若没有相同的，则插入该数据
                if(!isExist){
                    file_yuService.insertMat(file_yuVo);
                }
            }
            //把之前保存，现在没有的数据删除
            for(file_yuVo oleFile_yuVo:file_yuVos){
                file_yuService.deleteMat(oleFile_yuVo);
            }
            return SUCCESS_TIP;


    }

    /**
     * 保存调查表
     * @param json
     * @return
     */
    @RequestMapping(value = "/save_family")
    @ResponseBody
    public Object saveFamily(String json) {
        FamilySurvey familySurvey =  JSON.parseObject(json,FamilySurvey.class);
        houseProjectService.saveFamilySurvey(familySurvey);
        return SUCCESS_TIP;
    }

    /**
     * 保存调查表
     * @param json
     * @return
     */
    @RequestMapping(value = "/save_handle")
    @ResponseBody
    public Object saveHandleNode(String json) {
        HandleNode handleNode =  JSON.parseObject(json,HandleNode.class);
        handleNode.setRECWORKPHASENUM(0);
        houseProjectMapper.saveHandleNode(handleNode);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/save_joint_applicant")
    @ResponseBody
    public Object saveJointApplicant(@RequestBody List<JointApplicant> jointApplicantList) {
        Map<String,Object> param = new HashMap<>();
        //删除之前保存的，避免保存重复
        if(jointApplicantList.size() != 0){

            param.put("iOptypenum",jointApplicantList.get(0).getOPTYPENUM());
            param.put("iRecyear",jointApplicantList.get(0).getRECYEAR());
            param.put("iRecnum",jointApplicantList.get(0).getRECNUM());
            //获取旧的共同申请人
            List<JointApplicant> jointApplicants = houseProjectMapper.selectJointApplicant(param);
            //删除旧的共同申请人，避免保存重复
            houseProjectMapper.deleteJointApplicant(param);
            for(int i = 0;i<jointApplicantList.size();i++){
                if(jointApplicantList.get(i).getJointApplicantCard() != null && !("").equals(jointApplicantList.get(i).getJointApplicantCard())){
                    houseProjectService.addJointApplicant(jointApplicantList.get(i));
                    //判断为配偶或者媳妇，则保存信息
                    if(jointApplicantList.get(i).getJointApplicantRelation().equals("2") || jointApplicantList.get(i).getJointApplicantRelation().equals("9")){
                        FamilySurvey familySurvey = new FamilySurvey();
                        familySurvey.setOPTYPENUM((int)param.get("iOptypenum"));
                        familySurvey.setRECYEAR((int)param.get("iRecyear"));
                        familySurvey.setRECNUM((int)param.get("iRecnum"));
                        //姓名
                        familySurvey.setOWNERNAME_PEIOU(jointApplicantList.get(i).getJointApplicantName());
                        //性别
                        familySurvey.setOWNERSEXCODE_PEIOU(jointApplicantList.get(i).getJointApplicantSex());
                        //证件号码
                        familySurvey.setOWNERCERTNUM_PEIOU(jointApplicantList.get(i).getJointApplicantCard());
                        //证件类型
                        familySurvey.setOWNERCERTTYPECODE_PEIOU("1");
                        houseProjectService.updateFamilySurveySpouse(familySurvey);
                    }
                    //遍历旧的共同申请人
                    for(JointApplicant jointApplicant:jointApplicants){
                        //判断旧的共同申请人是否存有头像，若有
                        if(jointApplicant.getMY_PHOTO() != null){
                            //根据身份证号码，判断旧的和新的是否为同一个人
                            if(jointApplicant.getJointApplicantCard().equals(jointApplicantList.get(i).getJointApplicantCard())){
                                //覆盖头像
                                houseProjectService.savePicture(jointApplicant.getMY_PHOTO(),jointApplicant.getOPTYPENUM().toString(), jointApplicant.getRECYEAR().toString(),
                                        jointApplicant.getRECNUM().toString(),"0",jointApplicantList.get(i).getJointApplicantId());
                                break;
                            }
                        }
                    }
                }
            }
        }


        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/save_applicant_reason")
    @ResponseBody
    public Object saveApplicantReason(String json) {
        ApplicantReason applicantReason =  JSON.parseObject(json,ApplicantReason.class);
        houseProjectMapper.saveOtherFamilySurvey(applicantReason);
        return SUCCESS_TIP;
    }

    /**
     * 分配房屋时查询的栋号
     * @param buildName
     * @param projectName
     * @return
     */
    @RequestMapping(value = "/build_list")
    @ResponseBody
    public List<Build> buildList(String buildName,String projectName){
        Map<String,Object> param = new HashMap<>();
        param.put("buildName",buildName);
        param.put("projectName",projectName);
        if(buildName == null || ("").equals(buildName)){
            List<Build> buildProjectList = houseProjectMapper.buildProjectList(param);
            List<Build> buildList = new ArrayList<>();
            for(int i = 0;i<buildProjectList.size();i++){
                //获取项目ID
                param.put("projectId",buildProjectList.get(i).getProjectId());
                //通过项目ID查找栋
                List<Build> newBuildList = houseProjectMapper.buildList(param);
                //添加到数组里
                buildList.addAll(newBuildList);
            }
            return buildList;
        }else{
            //根据栋名查找栋
            List<Build> buildList = houseProjectMapper.buildList(param);
            if(projectName == null || ("").equals(projectName)){
                //如果项目名为空则直接返回
                return buildList;
            }else{
                //若不为空，则查询项目
                List<Build> buildProjectList = houseProjectMapper.buildProjectList(param);
                boolean judgeHave;
                //遍历栋
                for(int i = 0;i<buildList.size();i++){
                    judgeHave = false;
                    //遍历项目
                    for(int y = 0;y<buildProjectList.size();y++){
                        //判断查询栋是否在查询的项目中
                        if(buildList.get(i).getProjectId().equals(buildProjectList.get(y).getProjectId())){
                            judgeHave = true;
                            //拼接项目名和栋名
                            buildList.get(i).setBuildNum(buildProjectList.get(y).getBuildNum()+"-"+buildList.get(i).getBuildNum());
                            break;
                        }
                    }
                    //若不在则移除该栋
                    if(!judgeHave){
                        buildList.remove(i);
                        i--;
                    }
                }
                return buildList;
            }
        }



    }

    /**
     * 展示楼盘表
     * @param buildId
     * @return
     */
    @RequestMapping(value = "/show_detail")
    @ResponseBody
    public Map<String,Object> detailBuildPage(String buildId) {
        try{
            Map<String,Object> param = new HashMap<>();
            List<House> houseList = buildService.houseList(buildId);
            param.put("houseList",houseList);
            return param;
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping(value = "/check_house")
    @ResponseBody
    public Object checkHouse(String OPTYPENUM,String RECYEAR,String RECNUM,String checkHouseId,String houseCode) {

        return houseProjectService.checkHouse(OPTYPENUM,RECYEAR,RECNUM,checkHouseId,houseCode);
    }

    @RequestMapping(value = "/house_search")
    @ResponseBody
    public Object houseSearch(String OPTYPENUM,String RECYEAR,String RECNUM,String idCard) {

        return houseProjectService.houseSearch(OPTYPENUM,RECYEAR,RECNUM,idCard);
    }

    @RequestMapping(value = "/save_picture")
    @ResponseBody
    public Object savePicture(MultipartFile avatar, String OpTypeNum, String RecYear, String RecNum, String judge,String FamilyID,HttpServletRequest request) throws IOException {
        if (!avatar.isEmpty()) {
            try {
                houseProjectService.savePicture(avatar.getBytes(),OpTypeNum,RecYear,RecNum,judge,FamilyID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping("/getPhoto")
    @ResponseBody
    public String getPhoto(String page,String OpTypeNum,String RecYear,String RecNum, String judge,String FamilyID,Model model,String info,HttpServletRequest request,HttpServletResponse response){
        byte[] data;
        if(("1").equals(judge)){
            FamilySurvey apply = houseProjectMapper.getFamilySurvey(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum));
            data=apply.getMY_PHOTO();
        }else{
            JointApplicant apply = houseProjectMapper.getJointApplicant(Convert.toInt(OpTypeNum),Convert.toInt(RecYear),Convert.toInt(RecNum),Convert.toInt(FamilyID));
            data=apply.getMY_PHOTO();
        }

        response.setContentType("img/jpeg");
        response.setCharacterEncoding("utf-8");
        try {

            OutputStream outputStream=response.getOutputStream();
            InputStream in=new ByteArrayInputStream(data);
            int len=0;
            byte[]buf=new byte[1024];
            while((len=in.read(buf,0,1024))!=-1){
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "test";
    }

    /**
     * 退件
     * @param OPTYPENUM
     * @param RECYEAR
     * @param RECNUM
     * @return
     */
    @RequestMapping(value = "/refund")
    @ResponseBody
    public Object refund(String OPTYPENUM,String RECYEAR,String RECNUM) {
        Map<String,Object> param = new HashMap<>();
        param.put("iOptypenum",OPTYPENUM);
        param.put("iRecyear",RECYEAR);
        param.put("iRecnum",RECNUM);

        houseProjectService.refund(param);
        return SUCCESS_TIP;
    }

    /**
     * 根据人员状态过滤人员
     * @param name
     * @param status
     * @return
     */
    @RequestMapping(value = "/people_list")
    @ResponseBody
    public Object peopleList(Integer offset, Integer limit, @RequestParam(required = false)String name,@RequestParam(required = false) Integer status){
        Map<String,Object> param = new HashMap<>();
        param.put("name",name);
        param.put("status",status);
        //获取所有的项目信息
        List<FamilySurvey> projectList = houseProjectMapper.peopleList(param);
        //分页数据
        List<FamilySurvey> newProjectList = new ArrayList<>();
        //项目总数
        int listNum = projectList.size();
        //当前分页最大数
        int pagingNum = limit * (offset / limit + 1);
        //取两数最小值
        int num;
        if(listNum>pagingNum){
            num = pagingNum;
        }else{
            num = listNum;
        }

        for(int i = offset;i<num;i++){
            newProjectList.add(projectList.get(i));
        }
        Page page = new Page();
        page.setRecords(newProjectList);
        page.setTotal(listNum);
        page.setCurrent(offset / limit + 1);
        return super.packForBT(page);
    }
}
