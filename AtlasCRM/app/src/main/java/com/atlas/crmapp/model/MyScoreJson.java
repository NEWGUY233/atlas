package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyScoreJson {

    /**
     * id : 2
     * createBy : Leo,5703
     * createTime : 1.517469340992E12
     * points : 0
     * appuserId : 5703
     */

    private int id;
    private String createBy;
    private double createTime;
    private int points;
    private int appuserId;

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

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(int appuserId) {
        this.appuserId = appuserId;
    }
}
