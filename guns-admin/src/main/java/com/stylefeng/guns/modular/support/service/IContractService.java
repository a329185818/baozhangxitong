package com.stylefeng.guns.modular.support.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.support.model.Contract;
import com.stylefeng.guns.modular.support.model.ContractVO;
import org.springframework.ui.Model;

import java.util.Date;

/**
 * 合同管理业务
 */
public interface IContractService {
    /**
     * 获取所有已经分配房的信息1
     * a.optypenum = b.optypenum and a.recyear=b.recyear and a.recnum = b.recnum and a.people_status=3 and b.oppartnum=312
     * @return
     */
    Page<ContractVO> queryAllAlreadyAllocatedRoom(int iStart,int iEnd,String name);

    /**
     * 保存新合同数据
     * @param contract
     */
    void addContract(Contract contract);

    /**
     *获取打印所需的数据
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void exportPdf(Model model, Integer optypenum, Integer recyear, Integer recnum, Date endTime);

    /**
     * 获取个人所有的合同（有效和失效）
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    Page<ContractVO> getPersonAllContract(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 设置合同
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void contractDetail(Model model, Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 保存合同信息
     * @param json
     * @return
     */
    Object saveContract(String json);
}
