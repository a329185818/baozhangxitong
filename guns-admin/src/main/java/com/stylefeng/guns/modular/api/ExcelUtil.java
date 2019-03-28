package com.stylefeng.guns.modular.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        Font font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("宋体");
        style.setFont(font);
        //设置自动换行
        style.setWrapText(true);

        //设置自动换行
        HSSFCellStyle contentStyle = wb.createCellStyle();
        contentStyle.setWrapText(true);
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
            if(i == 0){
                sheet.setColumnWidth(i, 8 * 256);
            }else if(i == 2 || i == 4 || i == 11){
                sheet.setColumnWidth(i, 20 * 256);
            }else if(i == 15){
                sheet.setColumnWidth(i, 50 * 256);
            }else{
                sheet.setColumnWidth(i, 15 * 256);  //设置列宽，15个字符宽
            }
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellValue(values[i][j]);
                cell.setCellStyle(contentStyle);
            }
        }
        return wb;
    }

    /**
     * 读取 .xls内容
     * @param file
     * @return
     */
    public static List<ArrayList<String>> readXls (MultipartFile file,int rowNum)  {
        List<ArrayList<String>> list = new ArrayList<>();

        //创建输入流
        InputStream input = null;
        //创建文档
        HSSFWorkbook wb = null;

        try {
            input = file.getInputStream();
            //创建文档
            wb = new HSSFWorkbook(input);

            ArrayList<String> rowList = null;
            int totoalRows = 0;//总行数
            int totalCells = 0;//总列数
            //读取sheet(页)
            for (int sheetIndex = 0 ; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet hssfSheet = wb.getSheetAt(sheetIndex);
                if (hssfSheet == null) {
                    continue;
                }
                totoalRows = hssfSheet.getLastRowNum();
                //读取row
                for (int rowIndex = rowNum; rowIndex <= totoalRows; rowIndex++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowIndex);

                    if (hssfRow == null) {
                        continue;
                    }
                    rowList = new ArrayList<>();
                    totalCells = hssfRow.getLastCellNum();

                    //读取列
                    for (int cellIndex = 0; cellIndex < totalCells; cellIndex++) {
                        HSSFCell hssfCell = hssfRow.getCell(cellIndex);
                        if (hssfCell == null) {
                            rowList.add("");
                        } else {
                            hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            rowList.add(String.valueOf(hssfCell.getStringCellValue()));
                        }
                    }
                    list.add(rowList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    /**
     * 读取.xlsx 内容
     * @param file
     * @return
     */
    public static List<ArrayList<String>> readXlsx (MultipartFile file,int rowNum) {
        List<ArrayList<String>> list = new ArrayList<>();
        InputStream input = null;
        XSSFWorkbook wb = null;
        try {
            input = file.getInputStream();
            //创建文档
            wb = new XSSFWorkbook(input);
            ArrayList<String> rowList = null;
            int totoalRows = 0;//总行数
            int totalCells = 0;//总列数
            //读取sheet(页)
            for (int sheetIndex = 0 ; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                XSSFSheet xssfSheet = wb.getSheetAt(sheetIndex);

                if (xssfSheet == null) {
                    continue;
                }
                totoalRows = xssfSheet.getLastRowNum();
                //读取row
                for (int rowIndex = rowNum; rowIndex <= totoalRows; rowIndex++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowIndex);

                    if (xssfRow == null) {
                        continue;
                    }
                    rowList = new ArrayList<>();
                    totalCells = xssfRow.getLastCellNum();

                    //读取列
                    for (int cellIndex = 0; cellIndex < totalCells; cellIndex++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                        if (xssfCell == null) {
                            rowList.add("");
                        } else {
                            xssfCell.setCellType(Cell.CELL_TYPE_STRING);
                            rowList.add(String.valueOf(xssfCell.getStringCellValue()));
                        }
                    }
                    list.add(rowList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {

            }
        }

        return list;
    }

    /**
     * 获取文件类型
     * @param path
     * @return
     */
    public static String getPostfix (String path) {
        if (StringUtils.isBlank(path) || !path.contains(".")) {
            return null;
        }
        return path.substring(path.lastIndexOf(".") + 1, path.length()).trim();
    }
}
