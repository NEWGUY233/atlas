package com.atlas.crmapp.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.coffee.CouponSuccessActivity;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.usercenter.MyScoreActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hoda on 2017/7/19.
 */


public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPushReceiver";
    private NotificationManager nm;
    private RealPushMsgModel realPushMsg;
    private String myTitle;
    @Override
    public void onReceive(Context context, Intent intent) {


        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
       // Logger.d(TAG + "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));
        receivingNotification(context, intent);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Logger.d(TAG + "JPush用户注册成功" + "  regId："+ regId );
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
           Logger.d(TAG +"接受到推送下来的自定义消息");
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG +"接受到推送下来的通知");
            if(realPushMsg != null && GlobalParams.getInstance().isLogin()){
                if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(realPushMsg.getType())){
                    if(!AppUtil.isApplicationBroughtToBackground(context)){//判断应用是否置于前台。
                        Intent intentCouponSuccess = new Intent(context, CouponSuccessActivity.class);
                        intentCouponSuccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentCouponSuccess);
                    }
                    EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.COUPON_BIND_MSG, 1 , true));
                }else{
                    EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.NORMAl, 1 , true));
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.d(TAG + "用户点击打开了通知");
            openNotification(context, bundle);
        } else {
            Logger.d(TAG + "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d(TAG +" title : " + title+  " message : " + message +" extras : " + extras);
        if(StringUtils.isNotEmpty(extras)){
            Gson gson = new Gson();
            realPushMsg = gson.fromJson(extras, RealPushMsgModel.class);
            if(realPushMsg != null){
                if(extras.contains("environmentType")){// 如果是 正式环境， 版本是debug 的话忽略消息。
                    if(Constants.PUSH_ENVIRONMENT.DEBUG.equalsIgnoreCase(realPushMsg.getEnvironmentType()) && GlobalParams.getInstance().production){
                        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                        JPushInterface.clearNotificationById(context, notificationId);
                        realPushMsg = null;
                    }
                }
            }
        }

        if(realPushMsg != null && GlobalParams.getInstance().isLogin()){
            myTitle = title;
            if(StringUtils.isNotEmpty(message)){
                if(!message.equals(realPushMsg.getContent())){
                    myTitle = message;
                }
            }


            //防止产生两条数据
            if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()) || JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
                if(AppUtil.isApplicationBroughtToBackground(context) && Constants.PushMsgTpye.COUPON_BIND_MSG.equals(realPushMsg.getType())){
                    PushMsgHepler.insertPushMsg(myTitle, realPushMsg.getContent(), realPushMsg.getType(), Constants.PushMsgTpye.ATLER, Constants.PushMsgTpye.UNREADER);
                }else{
                    PushMsgHepler.insertPushMsg(myTitle, realPushMsg.getContent(), realPushMsg.getType(), realPushMsg.getActionUri(), Constants.PushMsgTpye.UNREADER);
                }
            }
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        if (realPushMsg != null) {
            Intent intent = null;
            if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(realPushMsg.getType())){
                if (AppUtil.isApplicationBroughtToBackground(context)){  //后台时 打开 APP ，弹出恭喜弹层
                    intent = new Intent(context, IndexActivity.class);
                }
            }else {
                if (!StringUtils.isEmpty(realPushMsg.getActionUri())) {
                    intent = ActionUriUtils.getIntent(context, realPushMsg.getActionUri(), "", StringUtils.isNotEmpty(myTitle) ? myTitle : realPushMsg.getContent());
                }else {
                    if ("bonuspoints".equals(realPushMsg.getType())){
                        intent = new Intent();
                        intent.setClass(context, MyScoreActivity.class);
                    }else if ("print-job".equals(realPushMsg.getType())){
                        try {
                            intent = new Intent();
                            intent.setClass(context, OrderConfirmActivity.class);
                            intent.putExtra("KEY_ORDER_ID", Long.valueOf(realPushMsg.getOrderId()));
//                            OrderConfirmActivity.newInstance(context,Long.);
                        }catch (Exception e){

                        }
                    }else{

                    }
                }
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必须
                context.startActivity(intent);
            }
        } else {
            Logger.d("realPushMsg is null");
        }
    }
}