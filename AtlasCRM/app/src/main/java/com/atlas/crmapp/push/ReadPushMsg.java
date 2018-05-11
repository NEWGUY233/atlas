package com.atlas.crmapp.push;

/**
 * Created by hoda on 2017/7/19.
 */

public class ReadPushMsg {
    private String type;
    private int unReadNum;
    private boolean haveUnRead;//是否有未读。

    public ReadPushMsg(String type, int unReadNum, boolean haveUnRead) {
        this.type = type;
        this.unReadNum = unReadNum;
        this.haveUnRead = haveUnRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public boolean isHaveUnRead() {
        return haveUnRead;
    }

    public void setHaveUnRead(boolean haveUnRead) {
        this.haveUnRead = haveUnRead;
    }
}
