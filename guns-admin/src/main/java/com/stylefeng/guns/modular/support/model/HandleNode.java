package com.stylefeng.guns.modular.support.model;

import com.stylefeng.guns.modular.system.model.User;

public class HandleNode {
    //类型RETURNTYPE
    private String returnType;
    //意见WORKOPINION
    private String workOpinion;
    //退件原因RETURNREASON
    private String returnReason;
    private int optypenum=101;
    private int recyear;
    private int recnum;

    private int RECWORKPHASENUM=0;

    private User approver;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getWorkOpinion() {
        return workOpinion;
    }

    public void setWorkOpinion(String workOpinion) {
        this.workOpinion = workOpinion;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public int getOptypenum() {
        return optypenum;
    }

    public void setOptypenum(int optypenum) {
        this.optypenum = optypenum;
    }

    public int getRecyear() {
        return recyear;
    }

    public void setRecyear(int recyear) {
        this.recyear = recyear;
    }

    public int getRecnum() {
        return recnum;
    }

    public void setRecnum(int recnum) {
        this.recnum = recnum;
    }

    public int getRECWORKPHASENUM() {
        return RECWORKPHASENUM;
    }

    public void setRECWORKPHASENUM(int RECWORKPHASENUM) {
        this.RECWORKPHASENUM = RECWORKPHASENUM;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
}
