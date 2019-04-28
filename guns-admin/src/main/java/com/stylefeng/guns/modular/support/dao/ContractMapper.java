package com.stylefeng.guns.modular.support.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.support.model.Contract;
import com.stylefeng.guns.modular.support.model.ContractVO;
import com.stylefeng.guns.modular.support.model.House;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ContractMapper {

    /**
     * 返回所有的信息
     * @return
     */
    List<ContractVO> queryAllAlreadyAllocatedRoom(@Param("iStart")int iStart,@Param("iEnd")int iEnd,@Param("name")String name);
    
    House queryHouseInfo(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    Contract getEffectiveContract(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    /**
     * 用于打印，增加合同结束时间判断应该获取到的是哪个合同数据
     * @param optypenum
     * @param recyear
     * @param recnum
     * @param endTime
     * @return
     */
    Contract getEffectiveContractForPrint(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum, @Param("endTime") Date endTime);

    void addContract(Contract contract);

    void updateContract(Contract contract);

    /**
     * 删除掉有效的合同信息,根据当前时间和截止时间来判断合同是都有效。（合同分为有效和失效）
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void deleteEffectiveContract(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    ContractVO houseInfo(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    List<ContractVO> getPersonAllContract(Page page, @Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);
}
