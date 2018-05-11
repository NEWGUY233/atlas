package com.atlas.crmapp.model;

/**
 * Created by Alex on 2017/4/27.
 */

public class ResponseUpdateAppJson {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownlaodUrl() {
        return downlaodUrl;
    }

    public void setDownlaodUrl(String downlaodUrl) {
        this.downlaodUrl = downlaodUrl;
    }

    private String version;
    private String downlaodUrl;
}
