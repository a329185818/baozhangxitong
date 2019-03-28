package com.stylefeng.guns.modular.support.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.dao.InformationMapper;
import com.stylefeng.guns.modular.support.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/info")
public class InformationController extends BaseController {
    private String PREFIX = "/informationQuery/";

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private InformationService informationService;
    /**
     * 项目和申请列表
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
}
