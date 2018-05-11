package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2017/6/2.
 */

public class VersionInfoJson {
    private double version ;
    private String downlaodUrl;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getDownlaodUrl() {
        return downlaodUrl;
    }

    public void setDownlaodUrl(String downlaodUrl) {
        this.downlaodUrl = downlaodUrl;
    }

/*  {"errorCode":0,"data":{"version":"1.00","downlaodUrl":"http://testassets.crm.atlasoffice.cn/atlas/apk/atlasCRM.apk","description":""},*/
}
