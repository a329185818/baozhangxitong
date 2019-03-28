package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.modular.support.dao.PieMapper;
import com.stylefeng.guns.modular.support.model.sbjbxx_yuVo;
import com.stylefeng.guns.modular.support.service.PieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PieServiceImpl implements PieService {

    @Autowired
    private PieMapper pieMapper;

    @Override
    public void savePie(sbjbxx_yuVo sbjbxx_yuVo){
        pieMapper.savePie(sbjbxx_yuVo);
    }
}
