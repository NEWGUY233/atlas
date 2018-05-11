package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 *
 * 活动联系人
 * Created by hoda on 2017/12/15.
 */

public class ActivitesUserInfoJson implements Serializable {
    private static final long serialVersionUID = -3319570044793020867L;


    /**
     *
     *
     * //获取到联系人的时候才传，否则不能传该参数id  appuserId
     * id : 111
     * name :
     * wechat :
     * appuserId : 111
     * phone :
     * unionId : 111
     * openId : 111
     * sex :
     * birthday :
     * remark :
     */

    private long id;
    private String name;
    private String wechat;
    private String appuserId;
    private String phone;
    private String unionId;
    private String openId;
    private String sex;
    private long birthday;
    private long createTime;
    private String remark;

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(String appuserId) {
        this.appuserId = appuserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getBirthday() {
        return birthday;
    }
}
