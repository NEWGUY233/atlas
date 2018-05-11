package com.atlas.crmapp.push;

/**
 * Created by hoda on 2017/7/19.
 */

public class RealPushMsgModel {

    private String content;//推送标题以及内容
    private String type;//类型；
    private String actionUri;//跳转动作，同首页。
    private String environmentType; //环境
    private String orderId; //环境

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActionUri() {
        return actionUri;
    }

    public void setActionUri(String actionUri) {
        this.actionUri = actionUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
