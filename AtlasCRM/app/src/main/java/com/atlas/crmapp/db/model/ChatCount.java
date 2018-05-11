package com.atlas.crmapp.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/9.
 */

@Entity
public class ChatCount {
    @Id(autoincrement = true)
    private Long id;
    private String identify;
    private String userId;
    private long unRead;

    @Generated(hash = 2030779592)
    public ChatCount(Long id, String identify, String userId, long unRead) {
        this.id = id;
        this.identify = identify;
        this.userId = userId;
        this.unRead = unRead;
    }

    @Generated(hash = 906217830)
    public ChatCount() {
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public long getUnRead() {
        return unRead;
    }

    public void setUnRead(long unRead) {
        this.unRead = unRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
