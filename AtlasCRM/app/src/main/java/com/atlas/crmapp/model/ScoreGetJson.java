package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/2/5.
 */

public class ScoreGetJson {
    /**
     * id : 1
     * createBy : 管理员,1
     * createTime : 1517816188000
     * points : 200
     * unitId : 1
     * startTime : 1517769351000
     * endTime : 1518624000000
     * state : CONFIRM
     * auditState : CONFIRM
     * enabled : true
     * userUnitId : 1
     * userTag :
     * name : 测试1
     * description : 测试所用
     */

    private int id;
    private String createBy;
    private long createTime;
    private int points;
    private int unitId;
    private long startTime;
    private long endTime;
    private String state;
    private String auditState;
    private boolean enabled;
    private int userUnitId;
    private String userTag;
    private String name;
    private String description;
    private String exchangeState;
    private boolean isOpen;

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getUserUnitId() {
        return userUnitId;
    }

    public void setUserUnitId(int userUnitId) {
        this.userUnitId = userUnitId;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(String exchangeState) {
        this.exchangeState = exchangeState;
    }
}
