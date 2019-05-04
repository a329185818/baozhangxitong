package com.stylefeng.guns.modular.support.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.support.model.HousingSubsidyVO;
import org.springframework.ui.Model;

import java.util.Map;

public interface IHousingSubsidyService {

    /**
     *  获取住房补贴设置界面相关数据
     * @param model
     * @param optypenum
     * @param recyear
     * @param recnum
     */
    void getHousingSubsidy(Model model, Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 设置住房补贴数据后保存
     * @param json
     * @return
     */
    Object saveHousingSubsidyInfo(String json);

    /**
     * 点击“已补贴”查看具体住房补贴信息
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    Map lookSubsidy(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 解除住房补贴
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    String relieveHousingSubsidy(Integer optypenum, Integer recyear, Integer recnum);

    /**
     * 住房补贴查询页面获取数据,显示所有已补贴的
     * @param param
     * @return
     */
    Page<HousingSubsidyVO> queryALLHousingSubsidyAop(Map param);

    /**
     * 获取个人补贴的所有数据(包含过期和有效)
     * @param optypenum
     * @param recyear
     * @param recnum
     * @return
     */
    Page<HousingSubsidyVO> getPersonAllHousingSubsidy(Integer optypenum, Integer recyear, Integer recnum);
}
