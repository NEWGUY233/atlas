package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/2/23.
 */

public class HabitsJson {
    /**
     * id : 1
     * name : 职场
     * createTime : 1517826592000
     * order : 1
     * img : ....
     */

    private int id;
    private String name;
    private long createTime;
    private int order;
    private String img;
    private boolean selected;

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
