package com.atlas.crmapp.db.model;

import com.atlas.crmapp.util.FormatCouponInfo;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class PushMsg  implements MultiItemEntity{
    @Id(autoincrement =  true)
    public Long id;
    public String title;
    public String content;
    public long date;
    public String thumbnail;
    public String type;
    public String actionType;
    public String action;
    public int isRead;
    public long couponId;
    public String price;
    public String remark;
    public boolean have;

    @Generated(hash = 1265984901)
    public PushMsg(Long id, String title, String content, long date,
            String thumbnail, String type, String actionType, String action,
            int isRead, long couponId, String price, String remark, boolean have) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.thumbnail = thumbnail;
        this.type = type;
        this.actionType = actionType;
        this.action = action;
        this.isRead = isRead;
        this.couponId = couponId;
        this.price = price;
        this.remark = remark;
        this.have = have;
    }

    @Generated(hash = 868472971)
    public PushMsg() {
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getCouponId() {
        return this.couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getHave() {
        return this.have;
    }

    public void setHave(boolean have) {
        this.have = have;
    }

    @Override
    public int getItemType() {
        return FormatCouponInfo.getCouponItemType(type);
    }
}
