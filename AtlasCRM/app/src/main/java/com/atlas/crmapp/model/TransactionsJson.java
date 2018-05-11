package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by hoda on 2017/6/20.
 * 充值记录
 */

public class TransactionsJson implements Serializable{

    private int id;
    private String createBy;
    private String type;
    private String refNo;
    private String direction;
    private long createTime;
    private int accountId;
    private double amount;
    private String bizName;

    /*   type 有多个，根据 Type 不同进行不同显示：
                "ORDER"; 消费
    "ADJUST"; 系统
    "RECHARGE"; 充值
    "REDRAW"; 退款
    "REDRAW_R"; 撤销充值*/


    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



}
