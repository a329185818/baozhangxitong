package com.stylefeng.guns.modular.support.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class RecOwner implements Serializable{
    private String OWNERNUM;
    private String OpTypeNum;
    private String RecYear;
    private String RecNum;


    private String OWNERNAME;  //申请人姓名
    private String OWNERCERTNUM; //申请人身份证号
    private String chengjiandw;
    private String PROJNAME;
    private String PROJSIT;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date STARTDATE;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date ENDDATE;
    private Integer CELLCOUNT;
    private Integer ARCHITAREA;
    private String GUIHUACERTNUM;
    private String SHIGONGCERTNUM;


    private String RONGJILV;
    private String JIANZHUGUIMO;
    private String OWNERNAME_PEIOU;  //配偶姓名
    private String OWNERCERTNUM_PEIOU;  //配偶身份证号

    public String getOWNERNUM() {
        return OWNERNUM;
    }

    public void setOWNERNUM(String OWNERNUM) {
        this.OWNERNUM = OWNERNUM;
    }

    public String getOpTypeNum() {
        return OpTypeNum;
    }

    public void setOpTypeNum(String opTypeNum) {
        OpTypeNum = opTypeNum;
    }

    public String getRecYear() {
        return RecYear;
    }

    public void setRecYear(String recYear) {
        RecYear = recYear;
    }

    public String getRecNum() {
        return RecNum;
    }

    public void setRecNum(String recNum) {
        RecNum = recNum;
    }

    public String getOWNERNAME() {
        return OWNERNAME;
    }

    public void setOWNERNAME(String OWNERNAME) {
        this.OWNERNAME = OWNERNAME;
    }

    public String getChengjiandw() {
        return chengjiandw;
    }

    public void setChengjiandw(String chengjiandw) {
        this.chengjiandw = chengjiandw;
    }

    public String getPROJNAME() {
        return PROJNAME;
    }

    public void setPROJNAME(String PROJNAME) {
        this.PROJNAME = PROJNAME;
    }

    public String getPROJSIT() {
        return PROJSIT;
    }

    public void setPROJSIT(String PROJSIT) {
        this.PROJSIT = PROJSIT;
    }

    public Date getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(Date STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public Date getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(Date ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public Integer getCELLCOUNT() {
        return CELLCOUNT;
    }

    public void setCELLCOUNT(Integer CELLCOUNT) {
        this.CELLCOUNT = CELLCOUNT;
    }

    public Integer getARCHITAREA() {
        return ARCHITAREA;
    }

    public void setARCHITAREA(Integer ARCHITAREA) {
        this.ARCHITAREA = ARCHITAREA;
    }

    public String getGUIHUACERTNUM() {
        return GUIHUACERTNUM;
    }

    public void setGUIHUACERTNUM(String GUIHUACERTNUM) {
        this.GUIHUACERTNUM = GUIHUACERTNUM;
    }

    public String getSHIGONGCERTNUM() {
        return SHIGONGCERTNUM;
    }

    public void setSHIGONGCERTNUM(String SHIGONGCERTNUM) {
        this.SHIGONGCERTNUM = SHIGONGCERTNUM;
    }

    public String getRONGJILV() {
        return RONGJILV;
    }

    public void setRONGJILV(String RONGJILV) {
        this.RONGJILV = RONGJILV;
    }

    public String getJIANZHUGUIMO() {
        return JIANZHUGUIMO;
    }

    public void setJIANZHUGUIMO(String JIANZHUGUIMO) {
        this.JIANZHUGUIMO = JIANZHUGUIMO;
    }
    public String getOWNERNAME_PEIOU() {
        return OWNERNAME_PEIOU;
    }

    public void setOWNERNAME_PEIOU(String OWNERNAME_PEIOU) {
        this.OWNERNAME_PEIOU = OWNERNAME_PEIOU;
    }

    public String getOWNERCERTNUM_PEIOU() {
        return OWNERCERTNUM_PEIOU;
    }

    public void setOWNERCERTNUM_PEIOU(String OWNERCERTNUM_PEIOU) {
        this.OWNERCERTNUM_PEIOU = OWNERCERTNUM_PEIOU;
    }
    public String getOWNERCERTNUM() {
        return OWNERCERTNUM;
    }

    public void setOWNERCERTNUM(String OWNERCERTNUM) {
        this.OWNERCERTNUM = OWNERCERTNUM;
    }

    @Override
    public String toString() {
        return "RecOwner{" +
                "OWNERNUM='" + OWNERNUM + '\'' +
                ", OpTypeNum='" + OpTypeNum + '\'' +
                ", RecYear='" + RecYear + '\'' +
                ", RecNum='" + RecNum + '\'' +
                ", OWNERNAME='" + OWNERNAME + '\'' +
                ", OWNERCERTNUM='" + OWNERCERTNUM + '\'' +
                ", chengjiandw='" + chengjiandw + '\'' +
                ", PROJNAME='" + PROJNAME + '\'' +
                ", PROJSIT='" + PROJSIT + '\'' +
                ", STARTDATE='" + STARTDATE + '\'' +
                ", ENDDATE='" + ENDDATE + '\'' +
                ", CELLCOUNT='" + CELLCOUNT + '\'' +
                ", ARCHITAREA='" + ARCHITAREA + '\'' +
                ", GUIHUACERTNUM='" + GUIHUACERTNUM + '\'' +
                ", SHIGONGCERTNUM='" + SHIGONGCERTNUM + '\'' +
                ", RONGJILV='" + RONGJILV + '\'' +
                ", JIANZHUGUIMO='" + JIANZHUGUIMO + '\'' +
                ", OWNERNAME_PEIOU='" + OWNERNAME_PEIOU + '\'' +
                ",OWNERCERTNUM_PEIOU='" + OWNERCERTNUM_PEIOU + '\'' +
                '}';
    }
}
