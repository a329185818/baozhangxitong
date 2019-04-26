package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.Contract;
import com.stylefeng.guns.modular.support.model.ContractUser;
import com.stylefeng.guns.modular.support.model.House;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractMapper {

    /**
     * 返回所有的信息
     * @return
     */
    List<ContractUser> queryAllAlreadyAllocatedRoom(@Param("name")String name);
    
    House queryHouseInfo(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    Contract getContract(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    Contract getEffectiveContract(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    void addContract(Contract contract);

    void updateContract(Contract contract);

    /**
     * 删除掉有效的合同信息,根据当前时间和截止时间来判断合同是都有效。（合同分为有效和失效）
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void deleteEffectiveContract(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

    ContractUser houseInfo(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);

}
