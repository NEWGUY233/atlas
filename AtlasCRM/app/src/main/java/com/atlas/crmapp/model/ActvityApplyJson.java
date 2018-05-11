package com.atlas.crmapp.model;

import java.io.Serializable;

/**活动确定报名
 *
 * Created by hoda on 2017/12/27.
 */

public class ActvityApplyJson implements Serializable{

    private static final long serialVersionUID = 158050419034492290L;
    //channel为参与的渠道：ANDROID:安桌，IOS，WECHAT
    private long activityId;
    private long comboId;
    private String channel = "ANDROID";
    private ActivitesUserInfoJson userInfo;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getComboId() {
        return comboId;
    }

    public void setComboId(long comboId) {
        this.comboId = comboId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public ActivitesUserInfoJson getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ActivitesUserInfoJson userInfo) {
        this.userInfo = userInfo;
    }
}
