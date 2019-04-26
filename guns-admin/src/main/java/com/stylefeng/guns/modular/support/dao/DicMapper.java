package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.Dic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DicMapper {
    //查找身份类型字典表
    public List<Dic> getIdCard();

    //根据身份证类型code查找名称
    public String getNameByIdCard(@Param("code")String code);

    //查找建筑结构字典表
    public List<Dic> getBuildingStructure();

    //查找区域字典表
    public List<Dic> getRegion();

    //查找房屋户型字典表
    public List<Dic> getHouseType();

    //查找材料类型字典表
    public List<Dic> getMaterial();

    //查找退件类型字典表
    public List<Dic> getRefund();

    //查找房屋用途字典表
    public List<Dic> getHouseuSage();

    //查找家庭关系字典表
    public List<Dic> getFamilyType();
}
