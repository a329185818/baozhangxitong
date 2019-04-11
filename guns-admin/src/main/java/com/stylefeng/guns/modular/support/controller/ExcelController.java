package com.stylefeng.guns.modular.support.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.StaticClass;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.api.ExcelUtil;
import com.stylefeng.guns.modular.support.dao.HouseProjectMapper;
import com.stylefeng.guns.modular.support.dao.InformationMapper;
import com.stylefeng.guns.modular.support.model.ApplicantReason;
import com.stylefeng.guns.modular.support.model.RecOwner;
import com.stylefeng.guns.modular.support.model.Tbbwimport;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/excel")
public class ExcelController extends BaseController {

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private HouseProjectMapper houseProjectMapper;

//    @RequestMapping(value = "/apply_approval")
//    @ResponseBody
//    public void exportData(String[][] content,HttpServletRequest request, HttpServletResponse response) throws Exception {
//        //excel标题
//        String[] title = {"序号","申请人姓名","申请人身份证号","配偶","配偶身份证号","申报状态","保障房类型","房号","建筑面积","套内面积","合同份数"
//                ,"合同签定日期","联系电话","项目地址","项目名称","房源情况"};
//
//        //excel文件名
//        String fileName = "人员信息"+System.currentTimeMillis()+".xls";
//
//        //sheet名
//        String sheetName = "人员信息表";
//
////        String[][] content = JSON.parseObject(json, String[][].class);
//
//        //创建HSSFWorkbook
//        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
//
//        //响应到客户端
//        try {
//            this.setResponseHeader(response, fileName);
//            OutputStream os = response.getOutputStream();
//            wb.write(os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 检查数据
     * @num 1为导入，2为检查
     * @return
     */
    @RequestMapping(value = "/import_excel")
    @ResponseBody
    public Object savePicture(MultipartFile excelExport, HttpServletRequest request,String num,HttpServletResponse response) throws IOException {
        List<ArrayList<String>> readResult = null;//总行记录
        if (excelExport != null) {
            //判断文件大小
            long size = excelExport.getSize();
            String name = excelExport.getOriginalFilename();
            if (StringUtils.isBlank(name) || size == 0) {
                return "上传的文件为空";
            }
            //获取文件后缀
            String postfix = ExcelUtil.getPostfix(name);
            //读取文件内容
            if (StringUtils.equals("xlsx", postfix)) {
                readResult = ExcelUtil.readXlsx(excelExport,2);
            } else if (StringUtils.equals("xls", postfix)) {
                readResult = ExcelUtil.readXls(excelExport,2);
            } else {
                return "上传的文件类型错误";
            }
            if (readResult == null || readResult.size() == 0) {
                return "上传的文件无数据";
            }
            //若为检查
            if(("2").equals(num)){
                for (ArrayList<String> arr : readResult) {
                    Tbbwimport tbbwimport=new Tbbwimport();
                    String idCard = arr.get(2);
                    tbbwimport=houseProjectMapper.findIdTbbwimport(idCard);
                    if (tbbwimport!=null){
                        arr.add(1,isNull(tbbwimport.getCol1()));
                        arr.add(2,isNull(tbbwimport.getCol2()));
                        arr.add(3,isNull(tbbwimport.getCol3()));
                        arr.add(4,isNull(tbbwimport.getCol4()));
                        arr.add(5,isNull(tbbwimport.getCol5()));
                        arr.add(6,isNull(tbbwimport.getCol6()));
                        arr.add(7,isNull(tbbwimport.getCol7()));
                        arr.add(8,isNull(tbbwimport.getCol8()));
                        arr.add(9,isNull(tbbwimport.getCol9()));
                        arr.add(10,isNull(tbbwimport.getCol10()));
                        arr.add(11,isNull(tbbwimport.getCol11()));
                        arr.add(12,isNull(tbbwimport.getCol12()));
                        arr.add(13,isNull(tbbwimport.getCol13()));
                        arr.add(14,isNull(tbbwimport.getCol14()));
                        arr.add(15,isNull(tbbwimport.getCol15()));
                        arr.add(16,isNull(tbbwimport.getCol17()));
                    }

                }
//                for (ArrayList<String> arr : readResult) {
//                    String idCard = arr.get(2);
//                    String status = houseProjectMapper.getStatusByCard(idCard);
//                    if(("1").equals(status)){
//                        arr.add("申报中");
//                    }else if(("2").equals(status)){
//                        arr.add("轮候");
//                    }else if(("3").equals(status)){
//                        arr.add("已配房");
//                        Map<String,Object> map = houseProjectMapper.getInfomation(idCard);
//                        if(("313").equals(isNull(map.get("保障房类型")))){
//                            arr.add("限价房");
//                        }else if(("312").equals(isNull(map.get("保障房类型")))){
//                            arr.add("公租房");
//                        }else{
//                            arr.add("其他房");
//                        }
//                        arr.add(isNull(map.get("房号")));
//                        arr.add(isNull(map.get("建筑面积")));
//                        arr.add(isNull(map.get("套内面积")));
//                        arr.add(isNull(map.get("合同份数")));
//                        arr.add(isNull(map.get("合同签定日期")));
//                        arr.add(isNull(map.get("联系电话")));
//                        arr.add(isNull(map.get("项目地址")));
//                        arr.add(isNull(map.get("项目名称")));
//                        arr.add(isNull(map.get("房源情况")));
//                    }else{
//                        arr.add("不合格");
//                    }
//                }
                //转换成字符串二维数组
                String[][] content = new String[readResult.size()][];
                for (int i = 0; i < readResult.size(); i++) {
                    ArrayList<String> arrayList = readResult.get(i);
                    content[i] = new String[arrayList.size()];
                    for(int y = 0;y<arrayList.size();y++){
                        if(y==0){
                            content[i][y]=String.valueOf(i+1);
                        }
                        else if(y<16){
                            content[i][y] = arrayList.get(y);}
                    }
                }

                String[] title = {"序号","申请人姓名","申请人身份证号","配偶","配偶身份证号","申报状态","保障房类型","房号","建筑面积","套内面积","合同份数"
                        ,"合同签定日期","联系电话","项目地址","项目名称","房源情况"};
                /*hfx*/
                //excel文件名
                String fileName = "人员信息"+System.currentTimeMillis()+".xls";
                fileName = new String(fileName.getBytes(),"iso8859-1");
                OutputStream output=response.getOutputStream();

                response.setContentType("application/octet-stream;charset=iso8859-1");
                response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                //创建HSSFWorkbook
                HSSFWorkbook wkb = ExcelUtil.getHSSFWorkbook("人员信息表", title, content, null);
                wkb.write(output);
                output.close();
                return null;
            }//若为导入
            else{
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                for (ArrayList<String> arr : readResult) {
                    Tbbwimport tbbwimport = new Tbbwimport();
                    tbbwimport.setId(StaticClass.getUUID());

                    tbbwimport.setCol1(isNull(arr.get(1)));
                    tbbwimport.setCol2(isNull(arr.get(2)));
                    tbbwimport.setCol3(isNull(arr.get(3)));
                    tbbwimport.setCol4(isNull(arr.get(4)));
                    tbbwimport.setCol5(isNull(arr.get(5)));
                    tbbwimport.setCol6(isNull(arr.get(6)));
                    tbbwimport.setCol7(isNull(arr.get(7)));
                    tbbwimport.setCol8(isNull(arr.get(8)));
                    tbbwimport.setCol9(isNull(arr.get(9)));
                    tbbwimport.setCol10(isNull(arr.get(10)));
                    tbbwimport.setCol11(isNull(arr.get(11)));
                    tbbwimport.setCol12(isNull(arr.get(12)));
                    tbbwimport.setCol13(isNull(arr.get(13)));
                    tbbwimport.setCol14(isNull(arr.get(14)));
                    tbbwimport.setCol15(isNull(arr.get(15)));
                    tbbwimport.setCol16(df.format(new Date()));
                    houseProjectMapper.insertTbbwimport(tbbwimport);
                }
                return "导入成功";
            }
        }else{
            return "上传文件为空";
        }
    }

    private String isNull(Object o){
        if(o == null){
            return "";
        }else{
            return o.toString();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //导出
    @RequestMapping("/exportQueryList")
    @ResponseBody
    public Object exportQueryList(HttpServletRequest request, HttpServletResponse response,String application,String idCard,String beginTime,String endTime) throws Exception {

        List<Tbbwimport> exportQueryList = new ArrayList<Tbbwimport>();
        Map<String,Object> param = new HashMap<>();
        param.put("beginTime",beginTime);
        param.put("endTime",endTime);
        param.put("name",application);
        param.put("idCard",idCard);//
        exportQueryList =informationMapper.importQuery(param);
        //转换成字符串二维数组
        String[][] content = new String[exportQueryList.size()][];
        for (int i = 0; i < exportQueryList.size(); i++) {
            content[i] = new String[17];
            content[i][0]=String.valueOf(i+1);
            content[i][1] = exportQueryList.get(i).getCol1();
            content[i][2] = exportQueryList.get(i).getCol2();
            content[i][3] = exportQueryList.get(i).getCol3();
            content[i][4] = exportQueryList.get(i).getCol4();
            content[i][5] = exportQueryList.get(i).getCol5();
            content[i][6] = exportQueryList.get(i).getCol6();
            content[i][7] = exportQueryList.get(i).getCol7();
            content[i][8] = exportQueryList.get(i).getCol8();
            content[i][9] = exportQueryList.get(i).getCol9();
            content[i][10] = exportQueryList.get(i).getCol10();
            content[i][11] = exportQueryList.get(i).getCol11();
            content[i][12] = exportQueryList.get(i).getCol12();
            content[i][13] = exportQueryList.get(i).getCol13();
            content[i][14] = exportQueryList.get(i).getCol14();
            content[i][15] = exportQueryList.get(i).getCol15();
            content[i][16] = exportQueryList.get(i).getCol17();
        }
        String[] title = {"序号","申请人姓名","申请人身份证号","配偶","配偶身份证号","申报状态","保障房类型","房号","建筑面积","套内面积","合同份数"
                ,"合同签定日期","联系电话","项目地址","项目名称","房源情况"};
        /*hfx*/
        //excel文件名
        String fileName = "人员信息"+System.currentTimeMillis()+".xls";
        fileName = new String(fileName.getBytes(),"iso8859-1");
        OutputStream output=response.getOutputStream();

        response.setContentType("application/octet-stream;charset=iso8859-1");
        response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        //创建HSSFWorkbook
        HSSFWorkbook wkb = ExcelUtil.getHSSFWorkbook("人员信息表", title, content, null);
        wkb.write(output);
        output.close();
        return null;
    }
}
