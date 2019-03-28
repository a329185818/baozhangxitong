package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.modular.support.dao.PayRentMapper;
import com.stylefeng.guns.modular.support.model.PayRent;
import com.stylefeng.guns.modular.support.service.PayRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PayRentServiceImpl implements PayRentService{

    @Autowired
    private PayRentMapper payRentMapper;

    public List<PayRent> search(Map map){
        return payRentMapper.search(map);
    }
}
