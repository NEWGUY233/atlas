package com.atlas.crmapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class TagBean implements Serializable{
    /**
     * id : 17
     * name : 健身的话题
     * description : 关于健身的话题
     * thumbnail : http://assets.crm.atlasoffice.cn/atlas/forum/20170726/YZtJZN2ASj.png
     * unitId : 1
     * state : NORMAL
     */

    private int id;
    private String name;
    private String description;
    private String thumbnail;
    private int unitId;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
