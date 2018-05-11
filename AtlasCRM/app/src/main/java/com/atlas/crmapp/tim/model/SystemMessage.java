package com.atlas.crmapp.tim.model;

import com.tencent.imsdk.TIMMessage;

/**
 * Created by Administrator on 2018/4/18.
 */

public class SystemMessage extends Message {
    TIMMessage message;
    public SystemMessage(TIMMessage message){
        this.message = message;
    }
    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public void save() {

    }
}
