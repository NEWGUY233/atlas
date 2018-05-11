package com.atlas.crmapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class DynamicSuccessBean {
    /**
     * id : 5adef8790cf27908e81af9e7
     * content : 里面每一位
     * imgs : ["http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/7910301a5f.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/9d9f77432b.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/1e6b0c3cbd.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/56003c835f.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/9470f55530.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/3d74c5e504.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/4f874c677e.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/f82c0baf0b.jpg","http://testassets.crm.atlasoffice.cn/atlas/avata/20180424/d864a11822.jpg"]
     * appuserId : 6019
     * unitId : 4
     * type : NORMAL
     * createTime : 1524562041414
     * state : NORMAL
     * commentQuantity : 0
     * praiseQuantity : 0
     * readQuantity : 0
     * updateTime : 1524562041414
     */

    private String id;
    private String content;
    private int appuserId;
    private int unitId;
    private String type;
    private long createTime;
    private String state;
    private int commentQuantity;
    private int praiseQuantity;
    private int readQuantity;
    private long updateTime;
    private List<String> imgs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(int appuserId) {
        this.appuserId = appuserId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCommentQuantity() {
        return commentQuantity;
    }

    public void setCommentQuantity(int commentQuantity) {
        this.commentQuantity = commentQuantity;
    }

    public int getPraiseQuantity() {
        return praiseQuantity;
    }

    public void setPraiseQuantity(int praiseQuantity) {
        this.praiseQuantity = praiseQuantity;
    }

    public int getReadQuantity() {
        return readQuantity;
    }

    public void setReadQuantity(int readQuantity) {
        this.readQuantity = readQuantity;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
