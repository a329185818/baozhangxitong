package com.stylefeng.guns.modular.support.dao;

import com.stylefeng.guns.modular.support.model.PayRent;

import java.util.List;
import java.util.Map;

public interface PayRentMapper {
    List<PayRent> search(Map map);
}
