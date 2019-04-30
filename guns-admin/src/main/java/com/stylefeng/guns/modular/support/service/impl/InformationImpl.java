package com.stylefeng.guns.modular.support.service.impl;

import com.stylefeng.guns.core.common.Result;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class InformationImpl implements InformationService {
    @Autowired
    private HouseProjectMapper houseProjectMapper;

    @Override
    public Object projectDetail(String sRecNumGather,String iOpTypeNum,String iRecYear,String iRecNum){

        String userId = ShiroKit.getUser().getId();
        Integer icount = -1;
        Map<String,Object> param = new HashMap();
        param.put("sRecNumGather",sRecNumGather);
        param.put("iUserNum",userId);
        param.put("icount",icount);
        houseProjectMapper.IsHavePowerOpenRec(param);

        //ICOUNT =0 "提示", "此案卷已转至下一阶段,请在信息查询中查看"
        ////ICOUNT >0 调用 up_LogicId_Disp，判断该业务打开哪个界面
        Map<String,Object> openPagePrama = new HashMap<>();
        openPagePrama.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        openPagePrama.put("iRecYear",Convert.toInt(iRecYear));
        openPagePrama.put("iRecNum",Convert.toInt(iRecNum));
        openPagePrama.put("sPage","");
        openPagePrama.put("sMsg","");
        houseProjectMapper.openPage(openPagePrama);
        if(ToolUtil.isEmpty(openPagePrama.get("sPage"))){
            return Result.error(502,"该业务没有配置相应的输入界面!");
        }

        //如果页面存在
        Map<String,Object> lockRecOrNotParam = new HashMap<>();
        lockRecOrNotParam.put("iOpTypeNum",Convert.toInt(iOpTypeNum));
        lockRecOrNotParam.put("iRecYear",Convert.toInt(iRecYear));
        lockRecOrNotParam.put("iRecNum",Convert.toInt(iRecNum));
        lockRecOrNotParam.put("iUserNum",userId);
        lockRecOrNotParam.put("iType",1);
        lockRecOrNotParam.put("sMsg","");
        lockRecOrNotParam.put("iSuccess",-3);
        houseProjectMapper.lockRecOrNot(lockRecOrNotParam);

        if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == 0 ){
            //锁定案卷失败
            return Result.error(503,"锁定案卷失败!");
        }else if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == 1 || Convert.toInt(lockRecOrNotParam.get("iSuccess")) == -1){
            Map<String,Object> recListCurrentPrama = new HashMap<>();
            recListCurrentPrama.put("sRecNumGather",sRecNumGather);
            recListCurrentPrama.put("bSuccess",-1);
            recListCurrentPrama.put("RCSurface",new ArrayList<Map<String,Object>>());
            houseProjectMapper.recListCurrent(recListCurrentPrama);

            //获取案卷信息失败
            if(recListCurrentPrama.size() == 0){
                return Result.error(504,"案卷当前信息获取失败!");
            }

            if(Convert.toInt(lockRecOrNotParam.get("iSuccess")) == -1){
                //没有编辑权限
                openPagePrama.put("readonly",true);
            }else{
                openPagePrama.put("readonly",false);
            }

            return Result.success("success",openPagePrama);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
