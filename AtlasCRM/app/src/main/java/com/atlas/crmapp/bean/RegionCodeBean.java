package com.atlas.crmapp.bean;

/**
 * Created by Administrator on 2018/4/9.
 */

public class RegionCodeBean {
    /**
     * id : 2
     * zip_code : 852
     * region : 香港
     * createTime : 1521624735000
     */

    private int id;
    private String zip_code;
    private String region;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
