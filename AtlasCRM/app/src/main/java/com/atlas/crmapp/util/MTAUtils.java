package com.atlas.crmapp.util;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;

/**
 * Created by hoda on 2018/1/6.
 */

public class MTAUtils {

    //【次数统计】带任意参数的事件
    public static void trackCustomEvent(Context context, String eventId){
        StatService.trackCustomEvent(context, eventId);
    }




    // androidManifest.xml指定本activity最先启动
    // 因此，MTA的初始化工作需要在本onCreate中进行
    // 在startStatService之前调用StatConfig配置类接口，使得MTA配置及时生
    public static void startMAT(Context context){

        String appkey = AppUtil.getAppMetaData(context, "TA_APPKEY" );
        if(StringUtils.isEmpty(appkey)){
            appkey = "ABJ3JS38EP4R";
        }

        // 初始化并启动MTA
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(context, appkey, com.tencent.stat.common.StatConstants.VERSION);
            Logger.d("MTA初始化成功");
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Logger.d("MTA初始化失败"+e);
        }
    }


}
