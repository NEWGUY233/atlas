package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/2/27.
 */

public class PrintLogin {
    /**
     * errcode : 0
     * errmsg : 信息验证成功
     * userId : 4517
     * entId : 1
     * entkey : 704325
     * ent_name : 南京信安宝
     * tissue_code :
     */

    private String errcode;
    private String errmsg;
    private String userId;
    private String entId;
    private String entkey;
    private String ent_name;
    private String tissue_code;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getEntkey() {
        return entkey;
    }

    public void setEntkey(String entkey) {
        this.entkey = entkey;
    }

    public String getEnt_name() {
        return ent_name;
    }

    public void setEnt_name(String ent_name) {
        this.ent_name = ent_name;
    }

    public String getTissue_code() {
        return tissue_code;
    }

    public void setTissue_code(String tissue_code) {
        this.tissue_code = tissue_code;
    }
}
