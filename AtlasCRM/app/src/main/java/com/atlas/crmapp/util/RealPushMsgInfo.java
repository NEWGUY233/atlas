package com.atlas.crmapp.util;

import android.content.Context;

import com.atlas.crmapp.push.RealPushMsgModel;

/**
 * Created by hoda on 2017/7/19.
 */

public class RealPushMsgInfo {

    private static String KEY_HOUSE_lOCATION_INFO = "KEY_REAL_PUSH_MSG";
    private static RealPushMsgInfo instance = new RealPushMsgInfo();
    private Context context;

    public static RealPushMsgInfo getInstance(){
        return instance;
    }

    public void init(Context context){
        this.context = context;
    }
    //存定位信息
    public void setRealPushModel(RealPushMsgModel info)
    {
        SpUtil.setGson(context, KEY_HOUSE_lOCATION_INFO, info);
    }

    public RealPushMsgModel getRealPushModel()
    {
        if(null == SpUtil.getGson(context, KEY_HOUSE_lOCATION_INFO, RealPushMsgModel.class))
        {
            return new RealPushMsgModel();
        }
        return SpUtil.getGson(context, KEY_HOUSE_lOCATION_INFO, RealPushMsgModel.class);
    }
}
