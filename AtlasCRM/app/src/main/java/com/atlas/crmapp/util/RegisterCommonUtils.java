package com.atlas.crmapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.commonactivity.WelcomeActivity;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.huanxin.Constant;
import com.atlas.crmapp.huanxin.HuanXinManager;
import com.atlas.crmapp.huanxin.IMHelper;
import com.atlas.crmapp.model.BusinesseModel;
import com.atlas.crmapp.model.CenterUnitJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.UnitBizJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegisterActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.stat.StatConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by hoda on 2017/8/5.
 */

public class RegisterCommonUtils {

    public static void loginRegister(Context context, PersonInfoJson personInfoJson){
        setPushAndCrashInfo(context, personInfoJson);
//        regHuanXinIM(context, personInfoJson);
        uploadDevice(context, true);

//        getTimSig(context,personInfoJson.getId() + "");
    }

    private static void getTimSig(Context c, final String id){
        BizDataRequest.getTimSig(c, id, new BizDataRequest.OnRequestTimSig() {
            @Override
            public void onSuccess(String string) {
                loginTim(string,id);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private static void loginTim(String sig,String id){
        String count = id;
        String psw = sig.replaceAll("\"","");
//        String count = "86-18223175029";
//        String psw = Constant.SINGE;
        Log.i("TIMManager_login","count = " + count + " ; psw = " + psw);
        SpUtil.putString(ContextUtil.getUtil().getContext(),SpUtil.TIM,count);
        TIMManager.getInstance().login(count,psw,new TIMCallBack(){

            @Override
            public void onError(int i, String s) {
                Log.i("TIMManager_login","s = " + s + " ; i = " + i );
                SpUtil.putBoolean(ContextUtil.getUtil().getContext(), "isLogin", false);
            }

            @Override
            public void onSuccess() {
//                FriendshipInfo.getInstance().refresh();
                SpUtil.putBoolean(ContextUtil.getUtil().getContext(), "isLogin", true);
//                RegisterCommonUtils.loginRegister(context, activity.mPersonInfoJson);
//                GlobalParams.getInstance().setPersonInfoJson(activity.mPersonInfoJson);
////            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                Intent intent = new Intent(context, IndexActivity.class);
//                context.startActivity(intent);
//                activity.finish();
            }
        });
    }

    /**
     * 设置 推送 标签以及别名， 设置bugly 用户信息
     * @param context
     * @param personInfoJson
     */
    public static void setPushAndCrashInfo(Context context, PersonInfoJson personInfoJson){
        String nameAndPhone = "";
        TagAliasCallback tagAliasCallback = new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                Logger.e("TagAliasCallback--" +"responseCode:"+responseCode+",alias:"+alias+",tags:"+tags);
            }
        };
        if(StringUtils.isNotEmpty(personInfoJson.nick)){
            nameAndPhone = personInfoJson.nick;
            JPushInterface.setAlias(context, personInfoJson.nick, tagAliasCallback);
        }
        if(StringUtils.isNotEmpty(personInfoJson.mobile)){
            nameAndPhone = nameAndPhone + " "+ personInfoJson.mobile;
            StatConfig.setCustomUserId(context, personInfoJson.mobile); // MTA 账号统计
        }
        CrashReport.setUserId(context,  nameAndPhone);//用于记录奔溃时用户信息


        Set<String > jPushTagsSet = new HashSet<String>();
        if(StringUtils.isNotEmpty(personInfoJson.company)){
            jPushTagsSet.add(personInfoJson.company);
        }
        if(jPushTagsSet.size() > 0){
            JPushInterface.setTags(context, jPushTagsSet, tagAliasCallback);
        }
    }


    /**
     * 注册IM
     * @param context
     * @param personInfoJson
     */
    public static void regHuanXinIM(final Context context, final PersonInfoJson personInfoJson){

        if(personInfoJson == null){
            Logger.e("personInfoJson - is null");
            return;
        }
        BizDataRequest.requestRegisterIM(context, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {

                EMClient.getInstance().login(personInfoJson.uid, personInfoJson.easemobPwd, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        // 登陆成功，保存用户昵称与头像URL
                        SpUtil.putString(context, "name", personInfoJson.nick);
                        SpUtil.putString(context, "logoUrl", personInfoJson.avatar);

                        // 将自己服务器返回的环信账号、昵称和头像URL设置到帮助类中。
                        IMHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(personInfoJson.nick);
                        IMHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(personInfoJson.avatar);
                        IMHelper.getInstance().setCurrentUserName(personInfoJson.uid); // 环信Id

                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        getAllEaseContacts(context);
                        Log.i("requestRegisterIM","登录聊天服务器成功");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.e("requestRegisterIM","登录聊天服务器失败");

                    }
                });
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private static void getAllEaseContacts(final Context context){
        try {
            IMHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
                @Override
                public void onSuccess(List<String> strings) {
                    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
                    broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                }
                @Override
                public void onError(int i, String s) {

                }
            });
        }catch (Exception e) {

        }
    }

    /**
     * 上传 极光推送 RegistrationID
     * @param context
     */
    public static void uploadDevice(Context context,boolean isLogin){
        String registrationId = "";
        if(isLogin){
            registrationId = JPushInterface.getRegistrationID(context);

        }
        Logger.d("JPush registrationId  " + registrationId);
        BizDataRequest.requestUploadDevice(context, registrationId, new BizDataRequest.OnResponseUploadDevice() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    // 更新个人信息
    public static void updateInfo(Context context){
        BizDataRequest.requestModifyUserInfo(context, new HashMap(), new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }




    

    //初始化 中心数据
    //==============================//
    public static void initUnits(final Context context, final GlobalParams globalParams, CenterUnitJson centerUnitJson, final boolean isWelcomeFirstLoad, final OnDownLoadResImageListener onDownLoadResImageListener, final OnResponseListener onResponseListener ) {
        SpUtil.setGson(context, Constants.SpKey.KEY_CENTER_INFO, centerUnitJson);
        globalParams.setAtlasId(centerUnitJson.id);
        globalParams.setAtlasName(centerUnitJson.name);
        globalParams.setKeyfree(centerUnitJson.keyfree);
        requestCommonBusinesses(context);
        if(isWelcomeFirstLoad){
            if (SpUtil.getBoolean(context, Constants.SpKey.KEY_IS_FIRST_INSTALL ,true)) {
                updateInfo(context);
            }
        }

        BizDataRequest.requestUnits(context, globalParams.getAtlasId(), new BizDataRequest.OnRequestUnitBiz() {
            @Override
            public void onSuccess(UnitBizJson unitBizJson) {
                globalParams.getBizCodes().clear();
                //ArrayList<BizCode> bizCodes = new ArrayList<BizCode>();
                for (int i=0; i<unitBizJson.rows.size(); i++) {
                    BizCode bizCode = new BizCode();
                    bizCode.setUnitId(unitBizJson.rows.get(i).id);
                    bizCode.setBizName(unitBizJson.rows.get(i).name);
                    bizCode.setBizCode(unitBizJson.rows.get(i).bizCode);
                    globalParams.getBizCodes().add(bizCode);
                }

                BizDataRequest.requestLoginUserInfo(context, new BizDataRequest.OnLoginUserInfo() {
                    @Override
                    public void onSuccess(final PersonInfoJson personInfoJson) {
                        globalParams.setIsLogin(true);
                        /*Intent intent = new Intent(Constants.Action.RECEIVER_LOGIN);
                        intent.putExtra("personInfoJson", personInfoJson);
                        context.sendBroadcast(intent);*/
                        if(isWelcomeFirstLoad){
                            RegisterCommonUtils.loginRegister(context, personInfoJson);
                        }

//                        getTimSig(context,personInfoJson.getId() + "");
                        BizDataRequest.requestGetVisibleCompanys(context,false , null, new BizDataRequest.OnVisibleCompanysRequestResult()  {
                            @Override
                            public void onSuccess(VisibleCompanysJson visibleCompanysJson) {
                                if (visibleCompanysJson.rows.size() > 0) {
                                    globalParams.setHasContract(true);
                                } else {
                                    globalParams.setHasContract(false);
                                }
                            }
                            @Override
                            public void onError(DcnException error) {
                                globalParams.setIsLogin(false);
                                onResponseError(onResponseListener, error);
                            }
                        });
                        //写入 用户信息
                        if(isWelcomeFirstLoad){
                            Utils.storeAccountToken(context,  globalParams.getAccessToken(), globalParams.getRefreshToken(), personInfoJson);
                        }
                        onDownLoadResImageListener.needDownLoadImage(null);
                    }

                    @Override
                    public void onError(DcnException error) {
                        if(isWelcomeFirstLoad){
                            globalParams.setIsLogin(false);
                            RegisterCommonUtils.uploadDevice(context, false);
                        }
                        onResponseError(onResponseListener, error);
                    }
                });

                if (StringUtils.isEmpty(GlobalParams.getInstance().getAccessToken()))
                    onDownLoadResImageListener.needDownLoadImage(null);
            }

            @Override
            public void onError(DcnException error) {
                Logger.e( error.getMessage());
                onResponseError(onResponseListener, error);
            }
        });
    }



    public static void initCoffeeData(Context context, final GlobalParams globalParams, final boolean isWelcomeFirstLoad, final OnDownLoadResImageListener onDownLoadResImageListener , final OnResponseListener onResponseListener){
        BizDataRequest.requestResource(context, String.valueOf(globalParams.getAtlasId()), Constants.ResourceConfigUrl.resAllUrl, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if(resourceJson.rows!=null&&resourceJson.rows.size()>0) {
                    ArrayList<ResourceJson.ResourceMedia> resList = new ArrayList<ResourceJson.ResourceMedia>();
                    ArrayList<ResourceJson.ResourceMedia> titleList = new ArrayList<ResourceJson.ResourceMedia>();
                    ArrayList<ResourceJson.ResourceMedia> contentList = new ArrayList<ResourceJson.ResourceMedia>();
                    ArrayList<ResourceJson.ResourceMedia> maskList = new ArrayList<ResourceJson.ResourceMedia>();
                    ArrayList<ResourceJson.ResourceMedia> adList = new ArrayList<ResourceJson.ResourceMedia>();
                    for(int i=0;i<resourceJson.rows.size();i++){
                        ResourceJson.Row row = resourceJson.rows.get(i);
                        if(row.resourceMedias!=null && row.resourceMedias.size()>0) {
                            ResourceJson.ResourceMedia media = row.resourceMedias.get(0);
                            if(row.uri.contains(Constants.ResourceConfigUrl.resUri)){
                                media.url = LoadImageUtils.loadMiddleImage(media.url);
                                resList.add(media);
                            }else if(row.uri.contains(Constants.ResourceConfigUrl.titleUri)){
                                titleList.add(media);
                            }else if(row.uri.contains(Constants.ResourceConfigUrl.contentUri)){
                                contentList.add(media);
                            }else if(row.uri.contains(Constants.ResourceConfigUrl.maskUri)){
                                media.url = LoadImageUtils.middleWebP(media.url);
                                maskList.add(media);
                            }else if(Constants.ResourceConfigUrl.adUri.equals(row.uri)){
                                media.url = LoadImageUtils.middleWebP(media.url);
                                adList.add(media);
                            }
                        }
                    }
                    globalParams.setResList(resList);
                    globalParams.setmTitleList(titleList);
                    globalParams.setmContentList(contentList);
                    globalParams.setmMaskList(maskList);
                    globalParams.setmAdList(adList);


                    onDownLoadResImageListener.needDownLoadImage(resList);
                    // 固定资源，onDownLoadResImageListener.needDownLoadImage(maskList);
                    if(isWelcomeFirstLoad){
                        onDownLoadResImageListener.needDownLoadImage(adList);
                    }
/*
                    downloadResImage(resList);
                    downloadResImage(maskList);*/
                }
            }

            @Override
            public void onError(DcnException error) {
                onResponseError(onResponseListener, error);
            }
        });
    }

    public static void requestCommonBusinesses(Context context){
        BizDataRequest.requestOnGetCommonBusinesses(context, false, null, new BizDataRequest.OnResponseGetBusinesses() {
            @Override
            public void onSuccess(List<BusinesseModel> businesseModels) {
                GlobalParams.getInstance().setBusinesses(businesseModels);
            }
            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public interface OnDownLoadResImageListener{
        void needDownLoadImage(ArrayList<ResourceJson.ResourceMedia> resList);
    }

    public interface OnResponseListener{
        void onError(DcnException error);
    }

    private static void onResponseError(OnResponseListener onResponseListener, DcnException error){
        if(onResponseListener!= null){
            onResponseListener.onError(error);
        }
    }



    //==============================//



    public static void logout(Context context){
        logout(context ,true);

    }


    public static void logout(Context context, boolean isToRegisterActivity){

        try{
            if(context instanceof Activity){
                RegisterCommonUtils.uploadDevice(((Activity) context), false);
                ((Activity) context).finish();
            }
            GlobalParams.getInstance().setIsLogin(false);
//            HuanXinManager.logOut();//退出环信
            TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,"sport_admin");
            TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,"workplace_admin");
            TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,"kitchen_admin");
            TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,"notice_admin");

            TIMManager.getInstance().logout(new TIMCallBack() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess() {

                }
            });
            PushMsgHepler.removeAllMsg();
            EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.COUPON_BIND_MSG, 0, false));
            Utils.clearAccountToken(context);
            SpUtil.putBoolean(context, "isLogin", false);

            if(isToRegisterActivity)
            {
                if (StringUtils.isEmpty(SpUtil.getString(context,SpUtil.PHONE,"")))
                    context.startActivity(new Intent(context, RegisterActivity.class));
                else
                    context.startActivity(new Intent(context, RecordLoginActivity.class));

            }
        }catch (Exception e){
            Logger.d(e.getMessage());
        }


    }








}
