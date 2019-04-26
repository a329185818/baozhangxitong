package com.stylefeng.guns.modular.support.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.model.sbjbxx_yuVo;
import com.stylefeng.guns.modular.support.service.PieService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.warpper.UserWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pie")
public class PieController extends BaseController {
    private String PREFIX = "/informationPie/";

    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private PieService pieService;
    /**
     * 派件界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model){
        Map<String, Object> params = new HashMap();
        Integer isuccess = 0;
        List<Map<String,Object>> businessType = houseProjectMapper.getFirstBusinessType(31);
        params.put("userList", new ArrayList<Map<String, Object>>());
        params.put("isuccess",isuccess);
        houseProjectMapper.getRecipients(params);
        model.addAttribute("businessType",businessType);
        model.addAttribute("userList",params.get("userList"));
        return PREFIX + "index.html";
    }

    /**
     * 选择派件人界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/choosePie")
    public String choosePie(Model model){
        List<Map<String, Object>> users = userService.selectUsers(null, "", "", "", "07fa9b825cb142f8a69cbf35e7043945");
        model.addAttribute("userList",users);
        return PREFIX + "choosePie.html";
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping(value = "/chooseName")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name) {
        List<Map<String, Object>> users = userService.selectUsers(null, name, "", "", "07fa9b825cb142f8a69cbf35e7043945");
        return new UserWarpper(users).warp();
    }

    @RequestMapping(value = "/pie_application")
    @ResponseBody
    public Object createHouse(@RequestBody List<sbjbxx_yuVo> result) {
        Map<String, Object> params = new HashMap();
        for(sbjbxx_yuVo sbjbxx_yuVo : result){
//            params.put("optypenum",sbjbxx_yuVo.getOptypenum()+"");
//            params.put("recyear",sbjbxx_yuVo.getRecnum()+"");
//            params.put("recnum",sbjbxx_yuVo.getRecyear()+"");
//            params.put("userId",sbjbxx_yuVo.getGxjxmmc());
            pieService.savePie(sbjbxx_yuVo);
        }
        return SUCCESS_TIP;
    }
}
