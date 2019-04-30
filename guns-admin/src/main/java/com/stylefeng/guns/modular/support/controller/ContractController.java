package com.stylefeng.guns.modular.support.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.page.PageInfoBT;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.modular.support.model.ContractVO;
import com.stylefeng.guns.modular.support.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 当前项目的逻辑都在Controller里，保证项目一致性，所以逻辑处理也放在Controller里.
 * 另外没有找到事务管理.
 */
@Controller
@RequestMapping("/contract")
public class ContractController extends BaseController {

    private String PREFIX = "/contract/";
    @Autowired
    private IContractService contractService;

    /**
     * 合同页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String contractView() {
        return PREFIX + "index.html";
    }

    /**
     * 合同页面获取数据,显示所有已分配好房子的
     *
     * @param offset
     * @param limit
     * @param name
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageInfoBT contractList(Integer offset, Integer limit, @RequestParam(required = false) String name) {
        //获取所有已分配好房子的申请人
        int iEnd = limit + offset;
        Page<ContractVO> page = contractService.queryAllAlreadyAllocatedRoom(offset,iEnd,name);
        return super.packForBT(page);
    }

    /**
     * 设置合同
     *
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping("/contract_detail")//optypenum,recyear,recnum,recnumgather
    public String contractDetail(Model model, String optypenum, String recyear, String recnum) {
        contractService.contractDetail(model, Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        return PREFIX + "contract_detail.html";
    }

    /**
     * 保存合同信息
     *
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object saveContract(String json) {
        return contractService.saveContract(json);
    }

    @RequestMapping("/lookAndPrint")
    public String lookAndPrint(Model model, String optypenum, String recyear, String recnum) {
        model.addAttribute("optypenum",optypenum);
        model.addAttribute("recyear",recyear);
        model.addAttribute("recnum",recnum);
        return PREFIX + "contract_lookAndPrint.html";
    }

    @RequestMapping("/all_list")
    @ResponseBody
    public Object personContractList(String optypenum, String recyear, String recnum){
        Page<ContractVO> page = contractService.getPersonAllContract(Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum));
        return super.packForBT(page);
    }


    /**
     * 打印
     *
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    @RequestMapping(value = "/export_pdf")
    public String exportPdf(Model model, String optypenum, String recyear, String recnum,Date endTime) {
        //获取承租方信息,拿到姓名和身份证号码
        //获取房屋地址，拿到小区地址+栋+室+房屋结构+建筑面积
        //获取合同设置的时间，年申情况，合同租金/每平米·月，计算出月租金，月租金大写，半年租金，半年租金大写
        //共同居住人情况
        contractService.exportPdf(model, Convert.toInt(optypenum), Convert.toInt(recyear), Convert.toInt(recnum),endTime);
        return PREFIX + "contract_pdf.html";
    }

}
