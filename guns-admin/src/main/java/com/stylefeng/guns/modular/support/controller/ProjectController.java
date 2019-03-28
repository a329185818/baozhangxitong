package com.stylefeng.guns.modular.support.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.api.ExcelUtil;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.model.Build;
import com.stylefeng.guns.modular.support.model.Dic;
import com.stylefeng.guns.modular.support.model.Project;
import com.stylefeng.guns.modular.support.service.AuthorityService;
import com.stylefeng.guns.modular.support.service.DicService;
import com.stylefeng.guns.modular.support.service.ProjectService;
import com.stylefeng.guns.modular.system.warpper.UserWarpper;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
    private String PREFIX = "/project/";

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private DicService dicService;

    /**
     * 项目界面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return PREFIX + "index.html";
    }

    /**
     * 项目详情界面
     * @param model
     * @return
     */
    @RequestMapping("/project_detail")
    public String projectDetail(Model model,String id,String num){
        Project project = projectService.getProjectById(id);
        if(project == null){
            project = new Project();
        }
        model.addAttribute("project", project);
        model.addAttribute("num", num);
        return PREFIX + "project_detail.html";
    }

    /**
     * 项目界面
     * @param model
     * @return
     */
    @RequestMapping("/choose_houseTable")
    public String chooseHouseTable(Model model,String id){
        //房屋地区字典值
        List<Dic> regionList = dicService.getRegion();
        model.addAttribute("regionList",JSON.toJSONString(regionList));
        model.addAttribute("projectId",id);
        return PREFIX + "choose_houseTable.html";
    }

    /**
     * 查询项目
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(Integer offset,Integer limit,@RequestParam(required = false) String name) {
        //获取所有的项目信息
        List<Project> projectList = projectService.getProject(name);
        //获取个人ID
        String userId = ShiroKit.getUser().getId();
        //项目查看权限
        List<Authority> lookAuthorityList = authorityService.getAuthority(userId,"查看");
        //项目编辑权限
        List<Authority> changeAuthorityList = authorityService.getAuthority(userId,"编辑");
        projectList = projectService.getProjectByAuthority(projectList,lookAuthorityList,changeAuthorityList);
        //分页数据
        List<Project> newProjectList = new ArrayList<>();
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

    /**
     * 保存项目信息
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object saveProject(String json){
        Project project =  JSON.parseObject(json,Project.class);
        //判断ID是否为空，为空则新增，不为空则修改
        if(project != null){
            try {
                if(project.getId() == null || ("").equals(project.getId())){
                    //若为添加，则默认赋予编辑和查看权限
                    project.setId(StaticClass.getUUID());
                    //添加项目
                    projectService.addProject(project);
                    //实例化权限
                    Authority authority = new Authority();
                    //设置用户ID
                    authority.setUserId(ShiroKit.getUser().getId());
                    //设置项目ID
                    authority.setProjectId(project.getId());
                    //设置查看权限
                    authority.setAuthorityType("查看");
                    authorityService.addAuthority(authority);
                    //设置修改权限
                    authority.setAuthorityType("编辑");
                    authorityService.addAuthority(authority);
                }else{
                    projectService.updateProject(project);
                }
                return "Success";
            }catch (Exception e){
                return "Error";
            }
        }else{
            return "Error";
        }
    }

    /**
     * 删除项目信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteProject(String id){
        //判断ID是否为空，为空则新增，不为空则修改
        if(!("").equals(id)){
            try {
                String[] idStr = id.split(",");
                for(int i = 0;i<idStr.length;i++){
                    projectService.deleteProject(idStr[i]);
                }
                return "Success";
            }catch (Exception e){
                return "Error";
            }
        }else{
            return "Error";
        }
    }

    /**
     *
     * @param offset
     * @param limit
     * @param name
     * @return
     */
    @RequestMapping("/table_list")
    @ResponseBody
    public Object houseTableList(Integer offset,Integer limit,@RequestParam(required = false) String name){
        //获取所有的项目信息
        List<Build> projectList = projectService.getBuildProject(name);
        //分页数据
        List<Build> newProjectList = new ArrayList<>();
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

    /**
     * 绑定楼盘表
     * @return
     */
    @RequestMapping("/choose")
    @ResponseBody
    public Object bindingHouseTable(String projectId,String buildReceive){
        projectService.bindingHouseTable(projectId,buildReceive);
        return "Success";
    }
}
