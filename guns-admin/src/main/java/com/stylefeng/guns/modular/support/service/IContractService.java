package com.stylefeng.guns.modular.support.service;

import com.stylefeng.guns.modular.support.model.Contract;
import com.stylefeng.guns.modular.support.model.ContractUser;
import com.stylefeng.guns.modular.support.model.FamilySurvey;
import com.stylefeng.guns.modular.support.model.House;
import org.springframework.ui.Model;

import java.util.List;

/**
 * 合同管理业务
 */
public interface IContractService {
    /**
     * 获取所有已经分配房的信息
     * a.optypenum = b.optypenum and a.recyear=b.recyear and a.recnum = b.recnum and a.people_status=3 and b.oppartnum=312
     * @return
     */
    List<ContractUser> queryAllAlreadyAllocatedRoom(String name);

    /**
     * 获取房屋信息
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    House queryHouseInfo(Integer optypenum,Integer recyear,Integer recnum);

    /**
     * 获取申请人信息
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    FamilySurvey getFamilySurvey(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 获取所有合同信息（有效+失效）
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    Contract getContract(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 只获取有效的合同
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    Contract getEffectiveContract(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 保存新合同数据
     * @param contract
     */
    void addContract(Contract contract);

    /**
     * 更新合同数据
     * @param contract
     */
    void updateContract(Contract contract);

    /**
     * 解除配房的时候删除有效合同数据（根据截止时间来过滤）
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    void  deleteEffectiveContract(Integer optypenum, Integer recyear, Integer recnum);

    /**
     *获取打印所需的数据
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void exportPdf(Model model,Integer optypenum, Integer recyear, Integer recnum);
}
