package com.atlas.crmapp.push;

import android.content.Context;

import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.util.SpUtil;

/**
 * Created by hoda on 2017/7/19.
 */

public class PushMsgManage {
    public static void setAcquireCouponUnread(Context context, boolean unread){
        SpUtil.putBoolean(context, Constants.SpKey.ACQUIRE_COUPON_UNREAD, unread);
    }

    public static boolean haveAcquireCouponUnread(Context context){
        return SpUtil.getBoolean(context, Constants.SpKey.ACQUIRE_COUPON_UNREAD, false);
    }

    public static void setCouponUnread(Context context, boolean unread){
        SpUtil.putBoolean(context, Constants.SpKey.COUPON_UNREAD, unread);
    }

    public static boolean haveCouponUnread(Context context){
        return SpUtil.getBoolean(context, Constants.SpKey.COUPON_UNREAD, false);
    }

}
