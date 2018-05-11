package com.atlas.crmapp.model;

/**
 * Created by Alex on 2017/5/30.
 */

public class PayInfoJson {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlipayPartner() {
        return alipayPartner;
    }

    public void setAlipayPartner(String alipayPartner) {
        this.alipayPartner = alipayPartner;
    }

    public String getAlipaySeller() {
        return alipaySeller;
    }

    public void setAlipaySeller(String alipaySeller) {
        this.alipaySeller = alipaySeller;
    }

    public String getAlipayRsaPrivate() {
        return alipayRsaPrivate;
    }

    public void setAlipayRsaPrivate(String alipayRsaPrivate) {
        this.alipayRsaPrivate = alipayRsaPrivate;
    }

    public String getAlipayRsaPublic() {
        return alipayRsaPublic;
    }

    public void setAlipayRsaPublic(String alipayRsaPublic) {
        this.alipayRsaPublic = alipayRsaPublic;
    }

    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    public void setAlipayNotifyUrl(String alipayNotifyUrl) {
        this.alipayNotifyUrl = alipayNotifyUrl;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getWxMchid() {
        return wxMchid;
    }

    public void setWxMchid(String wxMchid) {
        this.wxMchid = wxMchid;
    }

    public String getWxKey() {
        return wxKey;
    }

    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }

    public String getWxNotifyUrl() {
        return wxNotifyUrl;
    }

    public void setWxNotifyUrl(String wxNotifyUrl) {
        this.wxNotifyUrl = wxNotifyUrl;
    }

    private long id;
    private String alipayPartner;
    private String alipaySeller;
    private String alipayRsaPrivate;
    private String alipayRsaPublic;
    private String alipayNotifyUrl;
    private String wxAppid;
    private String wxMchid;
    private String wxKey;
    private String wxNotifyUrl;
}
