package com.stylefeng.guns.modular.support.controller;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.support.model.Authority;
import com.stylefeng.guns.modular.support.model.Project;
import com.stylefeng.guns.modular.support.service.AuthorityService;
import com.stylefeng.guns.modular.support.service.ProjectService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.warpper.UserWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authority")
public class AuthorityController extends BaseController {
    private String PREFIX = "/authority/";

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private ProjectService projectService;

    /**
     * 权限界面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return PREFIX + "index.html";
    }

    /**
     * 分配权限界面
     * @param model
     * @return
     */
    @RequestMapping("/authority_allot")
    public String authorityAllot(Model model,String userId,String authorityType){
        List<Authority> authorityList = authorityService.getAuthority(userId,authorityType);
        model.addAttribute("authorityList", JSON.toJSONString(authorityList));
        List<Project> projectList = projectService.getProject("");
        model.addAttribute("projectList",projectList);
        return PREFIX + "authorityAllot.html";
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name) {
        List<Map<String, Object>> users = userService.selectUsers(null, name, null, null, "07fa9b825cb142f8a69cbf35e7043945");
        return new UserWarpper(users).warp();
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/project_list")
    @ResponseBody
    public Object projectList(@RequestParam(required = false) String name) {
        List<Project> projectList = projectService.getProject(name);
        return projectList;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Object saveAuthority(@RequestBody List<Authority> authorityList) {
        if(authorityList.size() != 0){
            authorityService.deleteAuthority(authorityList.get(0));
            for(Authority authority : authorityList){
                if(authority.getAuthorityType() != null && !("").equals(authority.getAuthorityType()) && authority.getProjectId() != null){
                    authorityService.addAuthority(authority);
                }
            }
        }
        return "Success";
    }
}