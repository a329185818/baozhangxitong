package com.stylefeng.guns.modular.support.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.support.model.HousingSubsidy;
import com.stylefeng.guns.modular.support.model.HousingSubsidyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HousingSubsidyMapper {

    /**
     * 返回有效的住房补贴数据，同一个唯一标识可能存在失效数据(根据解除时间是否有值来判断是否为有效数据)
     * @return
     */
    HousingSubsidy getHousingSubsidy(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);


    /**
     * 新增住房补贴数据
     * @param housingSubsidy
     */
    void addHousingSubsidy(HousingSubsidy housingSubsidy);

    void updateHousingSubsidy(@Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum, @Param("relieveOprationId") String relieveOprationId);

    List<HousingSubsidyVO> queryALLHousingSubsidy(Page page,Map parame);

    List<HousingSubsidyVO> getPersonAllHousingSubsidy(Page page, @Param("optypenum") Integer optypenum, @Param("recyear") Integer recyear, @Param("recnum") Integer recnum);
}
