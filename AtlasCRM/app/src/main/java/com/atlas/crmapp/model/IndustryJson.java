package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/2/24.
 */

public class IndustryJson {
    /**
     * id : 1
     * name : TMT (科技、媒体和通信)
     * createTime : 1517826592000
     * order : 1
     */

    private int id;
    private String name;
    private long createTime;
    private int order;

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
}
