package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/3/6.
 */

public class PrintStateJson {
    /**
     * id : 1
     * hpUserId : 5164
     * appuserId : 140
     * state : UNLOCK
     * entId : 1
     * createTime : 1519957245000
     * updateTime : 1520249963000
     */

    private int id;
    private String hpUserId;
    private int appuserId;
    private String state;
    private String entId;
    private long createTime;
    private long updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHpUserId() {
        return hpUserId;
    }

    public void setHpUserId(String hpUserId) {
        this.hpUserId = hpUserId;
    }

    public int getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(int appuserId) {
        this.appuserId = appuserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
