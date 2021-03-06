package com.stylefeng.guns.modular.support.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.annotion.Permission;

import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;

import com.stylefeng.guns.modular.support.dao.InformationMapper;
import com.stylefeng.guns.modular.support.model.Tbbwimport;
import com.stylefeng.guns.modular.support.service.InformationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.NoPermissionException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/info")
public class InformationController extends BaseController {
    private String PREFIX = "/informationQuery/";

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private InformationService informationService;
    /**
     * 信息查询列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    public String index(Model model){
        return PREFIX + "index.html";
    }

    /**
     * 项目和申请列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object infoList(Integer offset,Integer limit,Integer iIsSelfExec,Integer IsPrecise_queryVal,String registerNum,String application,String idCard,String archivesNum,String first){
        if(!"1".equals(first)){
            String scondtext = "";
            boolean judgeBoolean = true;
            if(IsPrecise_queryVal == null || IsPrecise_queryVal == 0){//勾选了
                if(registerNum != null && registerNum != ""){
                    scondtext = "(RWRECINFO.RECNUMGATHER like '%"+registerNum+"%'";
                    judgeBoolean = false;
                }
                if(application != null && application != ""){
                    if(judgeBoolean){
                        scondtext = "(rwrecowner.ownername like '%"+application+"%'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or rwrecowner.ownername like '%"+application+"%'";
                    }
                }
                if(idCard != null && idCard != ""){
                    if(judgeBoolean){
                        scondtext = "(rwrecowner.OWNERCERTNUM like '%"+idCard+"%'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or rwrecowner.OWNERCERTNUM like '%"+idCard+"%'";
                    }
                }
                if(archivesNum != null && archivesNum != ""){
                    if(judgeBoolean){
                        scondtext = "(TBBWARCHALLINFO.ARCHNUM like '%"+archivesNum+"%'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or TBBWARCHALLINFO.ARCHNUM like '%"+archivesNum+"%'";
                    }
                }
            }else{//没有勾选
                if(registerNum != null && registerNum != ""){
                    scondtext = "(RWRECINFO.RECNUMGATHER = '"+registerNum+"'";
                    judgeBoolean = false;
                }
                if(application != null && application != ""){
                    if(judgeBoolean){
                        scondtext = "(rwrecowner.ownername = '"+application+"'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or rwrecowner.ownername = '"+application+"'";
                    }
                }
                if(idCard != null && idCard != ""){
                    if(judgeBoolean){
                        scondtext = "(rwrecowner.OWNERCERTNUM = '"+idCard+"'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or rwrecowner.OWNERCERTNUM = '"+idCard+"'";
                    }
                }
                if(archivesNum != null && archivesNum != ""){
                    if(judgeBoolean){
                        scondtext = "(TBBWARCHALLINFO.ARCHNUM = '"+archivesNum+"'";
                        judgeBoolean = false;
                    }else{
                        scondtext += " or TBBWARCHALLINFO.ARCHNUM = '"+archivesNum+"'";
                    }
                }
            }
            if(!("").equals(scondtext)){
                scondtext += ")";
            }
            String userid = ShiroKit.getUser().getId();

            List<Map<String,Object>> projectList = new ArrayList<Map<String,Object>>();
            Map<String,Object> param = new HashMap<>();
            param.put("scondtext",scondtext);//查询条件
            param.put("iStart",offset);//0
            param.put("iEnd",limit + offset);//
            param.put("iUserNum",userid);//
            param.put("itype",0);//
            param.put("iIsSelfExec",iIsSelfExec);//本人经办

            param.put("iCount",0);
            param.put("RCQry",projectList);//

            informationMapper.infoList(param);

            projectList = (List)param.get("RCQry");
            Page page = new Page();
            page.setTotal(projectList.size());
            page.setCurrent(1);
            page.setRecords(projectList);
            return super.packForBT(page);
        }else{
            return null;
        }
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
        return informationService.projectDetail(sRecNumGather,iOpTypeNum,iRecYear,iRecNum);
    }
    /**
     * 导入信息查询列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/importQuery")
    public String importQuery(Model model){
        return PREFIX + "importQuery.html";
    }

    @RequestMapping("/importQueryList")
    @ResponseBody
    public Object importQueryList(Integer offset,Integer limit,String condition ,String beginTime,String endTime){
            List<Tbbwimport> projectList = new ArrayList<Tbbwimport>();
            Map<String,Object> param = new HashMap<>();
            param.put("iStart",offset);//0
            param.put("iEnd",limit + offset);//
            param.put("beginTime",beginTime);
            param.put("endTime",endTime);
            param.put("condition",condition);
            projectList =informationMapper.importQueryList(param);
        //分页数据
        List<Tbbwimport> newProjectList = new ArrayList<>();
        //查询总数
        int listNum = informationMapper.importQueryCount(param);
        //当前分页最大数
        int pagingNum = limit * (offset / limit + 1);

        int num=projectList.size();
        for(int i = 0;i<num;i++){
            newProjectList.add(projectList.get(i));
        }
        Page page = new Page();
        page.setRecords(newProjectList);
        page.setTotal(listNum);
        page.setCurrent(offset / limit + 1);
        return super.packForBT(page);
    }
    /**
     * 跳转到添加申请记录的页面
     */
    @RequestMapping("/add_proposer")
    public String addView() {
        return PREFIX + "add_proposer.html";
    }

    /**
     * 添加申请记录
     */
    @RequestMapping("/addProposer")
    @ResponseBody
    public Tip add(Tbbwimport tbbwimport) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        // 获得日期
        tbbwimport.setId(StaticClass.getUUID());
        tbbwimport.setCol16(df.format(new Date()));
        informationMapper.addTbbwimport(tbbwimport);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到编辑申请记录页面
     */
    @RequestMapping("/proposer_edit")
    public String userEdit(String proposerId, Model model) {

        Tbbwimport tbbwimport  = informationMapper.findProposer(proposerId);
        model.addAttribute(tbbwimport);
        LogObjectHolder.me().set(tbbwimport);
        return PREFIX + "edit_proposer.html";
    }
    /**
     * 修改申请记录信息
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/proposer_alter")
    @ResponseBody
    public Tip edit(Tbbwimport tbbwimport){
        informationMapper.alterProposer(tbbwimport);
        return SUCCESS_TIP;
    }

    /**
     * 删除申请记录（逻辑删除）
     */
    @RequestMapping("/deleteProposer")
    @ResponseBody
    public Tip delete(String proposerIds) {
        String [] ids=proposerIds.split(",");
        for(int i=0;i<ids.length;i++) {
            informationMapper.deleteProposer(ids[i]);
        }
        return SUCCESS_TIP;
    }
}
