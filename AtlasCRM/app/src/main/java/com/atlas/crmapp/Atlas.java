package com.atlas.crmapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.coffee.CouponSuccessActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.MyActivityLifecycleCallbacks;
import com.atlas.crmapp.dagger.component.AppComponent;
import com.atlas.crmapp.dagger.component.DaggerAppComponent;
import com.atlas.crmapp.dagger.module.AppModule;
import com.atlas.crmapp.db.hepler.GreenDaoManager;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.db.model.PushMsg;
import com.atlas.crmapp.huanxin.IMHelper;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.SpUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMFriendshipSettings;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMLogListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public class Atlas extends android.support.multidex.MultiDexApplication implements  MyActivityLifecycleCallbacks.IAppBackToGroundForeGround{

    private AppComponent component;
    private static Context context;
    private MyActivityLifecycleCallbacks lifecycleCallbacks = new MyActivityLifecycleCallbacks();

    public GlobalParams getGlobalParams() {
        return GlobalParams.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("Atlas Application onCreate-------" );
        context = this;
        ContextUtil.getUtil().setContext(this);
        try {
            GreenDaoManager.getInstance();
            initBugly();
            initUMeng();
            initOKGO();
            initLogger();
//            initLeak();
            initJpush();
            initMTA();
            initIM();
            initLanguage();
            QbSdk.initX5Environment(getApplicationContext(),  null);//x5内核初始化接口
        }catch (Exception e){
            Logger.d(e.getMessage());
        }

        registerActivityLifecycleCallbacks(lifecycleCallbacks);
        lifecycleCallbacks.setiAppBackToGroundForeGround(this);

        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //热修复 配置
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }

    private void initBugly(){
        //wifi 时自动下载
        //Beta.autoDownloadOnWifi = true;
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Logger.d("补丁下载地址");
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplication(),
                        String.format(Locale.getDefault(), "%s %d%%",
                                Beta.strNotificationDownloading,
                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Logger.d("补丁下载成功");
            }

            @Override
            public void onDownloadFailure(String msg) {
                Logger.d("补丁下载失败");
            }

            @Override
            public void onApplySuccess(String msg) {
                Logger.d("补丁应用成功");
            }

            @Override
            public void onApplyFailure(String msg) {
                Logger.d("补丁应用失败");
            }

            @Override
            public void onPatchRollback() {

            }
        };

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
       // Bugly.setIsDevelopmentDevice(getApplication(), true);
        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        //Bugly.init(getApplication(), "900029763", true);


        CrashReport.setIsDevelopmentDevice(this, !GlobalParams.production);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(AppUtil.getChannelName(this));
        String buglyKey = "33184ae2c5";
        if(!GlobalParams.production){
            buglyKey = "7aad9c1768";
            Bugly.setIsDevelopmentDevice(getApplication(), true);
        }else {
            Bugly.setIsDevelopmentDevice(getApplication(), false);
        }
        Bugly.init(getApplicationContext(), buglyKey, !GlobalParams.production, strategy);//hoda bugly key 7aad9c1768, atlaskey 33184ae2c5
    }

    private void initUMeng(){
        PlatformConfig.setWeixin("wx03265a3e5e061e25", "591ac3d9b27b0a1005000270");
        UMShareAPI.get(this);
        MobclickAgent.setScenarioType(this , MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式，这样将不会再自动统计Activity。
        if(!getGlobalParams().production){
            MobclickAgent.setDebugMode(true);
            String json = AppUtil.getDeviceInfo(this);
            Logger.d("umeng test json : " + json);

        }

    }

    private void initJpush(){
        JPushInterface.setDebugMode(!GlobalParams.production);
        JPushInterface.init(this);
        JPushInterface.setAlias(this, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), null);
        Logger.d("JPushInterface uuid : " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
    }


    private void initLogger() {
        if (!GlobalParams.production){//!GlobalParams.production
            Logger.init("Atlas_log"); // for debug, print all log
            OkGo.getInstance().debug("Atlas_OkGo", Level.INFO, true);
        }
        else {
            Logger.init().logLevel(LogLevel.NONE);
        }
    }

    private void initLeak(){
        if (!GlobalParams.production){
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

    private void initOKGO(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //必须调用初始化
        OkGo.init(this);
        try {
            IMHelper.getInstance().init(context);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Test",e.getMessage());
        }
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(3600 * 1000 * 6)//CacheEntity.CACHE_NEVER_EXPIRE

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates()                         //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

                    //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

                    //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })
                    //这两行同上，不需要就不要加入
                    .addCommonHeaders(headers);  //设置全局公共头
                    //.addCommonParams(params);   //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void initMTA(){
        // 请在初始化时调用，参数为Application或Activity或Service
        StatisticsDataAPI.instance(this);
        StatService.setContext(this);
        //  初始化MTA配置
        initMTAConfig(!GlobalParams.production);
        // 注册Activity生命周期监控，自动统计时长
        StatService.registerActivityLifecycleCallbacks(this);
        // 初始化MTA的Crash模块，可监控java、native的Crash，以及Crash后的回调
        //MTACrashModule.initMtaCrashModule(this);
    }


    /**
     * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
     *
     * @param isDebugMode
     *            根据调试或发布条件，配置对应的MTA配置
     */
    private void initMTAConfig(boolean isDebugMode) {

        // StatConfig.setEnableSmartReporting(false);
        // Thread.setDefaultUncaughtExceptionHandler(new
        // UncaughtExceptionHandler() {
        //
        // @Override
        // public void uncaughtException(Thread thread, Throwable ex) {
        // logger.error("setDefaultUncaughtExceptionHandler");
        // }
        // });
        // 调试时，使用实时发送
        // StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
        // // 是否按顺序上报
        // StatConfig.setReportEventsByOrder(false);
        // // 缓存在内存的buffer日志数量,达到这个数量时会被写入db
        // StatConfig.setNumEventsCachedInMemory(30);
        // // 缓存在内存的buffer定期写入的周期
        // StatConfig.setFlushDBSpaceMS(10 * 1000);
        // // 如果用户退出后台，记得调用以下接口，将buffer写入db
        // StatService.flushDataToDB(getApplicationContext());

        // StatConfig.setEnableSmartReporting(false);
        // StatConfig.setSendPeriodMinutes(1);
        // StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);


         // 发布时，建议设置的开关状态，请确保以下开关是否设置合理

        // 会话时间
        StatConfig.setSessionTimoutMillis(10);
        // 禁止MTA打印日志
        StatConfig.setDebugEnable(isDebugMode);
        // 根据情况，决定是否开启MTA对app未处理异常的捕获
        //StatConfig.setAutoExceptionCaught(true);
        // 选择默认的上报策略
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        // 10分钟上报一次的周期
        StatConfig.setSendPeriodMinutes(10);
        StatCrashReporter.getStatCrashReporter(this).setJavaCrashHandlerStatus(isDebugMode);
        StatConfig.setTLinkStatus(true);

    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        super.onTerminate();
    }

    /**
     * onCreate是一个回调接口，android系统会在应用程序启动的时候，在任何应用程序组件（activity、服务、
     * 广播接收器和内容提供者）被创建之前调用这个接口。
     * 需要注意的是，这个方法的执行效率会直接影响到启动Activity等的性能，因此此方法应尽快完成。
     * 最后在该方法中，一定要记得调用super.onCreate()，否则应用程序将会报错。
     */
    @Override
    public void isBgToFg(boolean isBgToFg, Activity currActivity) {
        Logger.d("isBgToFg---" + isBgToFg);
        //后台恢复前台时 若是置于后台时有收到优惠券则弹出。
        if(isBgToFg && GlobalParams.getInstance().isLogin()){
            List<PushMsg> atlerMsgs = PushMsgHepler.selectAlterMsgs();
            if(atlerMsgs.size() > 0){
                Intent intentCouponSuccess = new Intent(Atlas.this, CouponSuccessActivity.class);
                intentCouponSuccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必须添加 解决在非Activity中使用startActivity
                startActivity(intentCouponSuccess);
                for(PushMsg pushMsg :atlerMsgs){
                    pushMsg.setAction("");
                }
                PushMsgHepler.updateAlterMsgs(atlerMsgs);
            }
        }
    }

    public static String getCurrentActivityName(){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();              //完整类名
        String packageName = info.topActivity.getPackageName();          //包名
        Logger.d("getCurrentActivityName " + shortClassName);
        return  shortClassName;
    }

    public AppComponent getComponent() {
        return component;
    }

    private static int sdkAPI_produce = 1400078247;
    private static int sdkAPI_test = 	1400083557;

    private static String tag = 	"TIMManager";
    private void initIM(){
        int sdkAPI = GlobalParams.production ? sdkAPI_produce : sdkAPI_test;
        TIMSdkConfig config = new TIMSdkConfig(sdkAPI);
        config.enableCrashReport(false)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");

        config.setLogListener(new TIMLogListener() {
            @Override
            public void log(int i, String s, String s1) {
//                Log.i("TIMLOGLITENER","tag = " + s + " content = " + s1);
            }
        });
        TIMManager.getInstance().init(this,config);

        //基本用户配置
        TIMUserConfig userConfig = new TIMUserConfig()
                //设置群组资料拉取字段
//                .setGroupSettings(呢我)
//                //设置资料关系链拉取字段
                .setFriendshipSettings(initFriendshipSettings())
                //设置用户状态变更事件监听器
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
//                        Log.i(tag, "onForceOffline");
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新 userSig 重新登录 SDK
//                        Log.i(tag, "onUserSigExpired");
                    }

                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
//                        Log.i(tag, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
//                        Log.i(tag, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
//                        Log.i(tag, "onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
//                        Log.i(tag, "onGroupTipsEvent, type: " + elem.getTipsType());
                    }
                })
                //设置会话刷新监听器
                .setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        Log.i(tag, "onRefresh");
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> conversations) {
//                        Log.i(tag, "onRefreshConversation, conversation size: " + conversations.size());
                    }
                });

//消息扩展用户配置
        userConfig = new TIMUserConfigMsgExt(userConfig)
                //禁用消息存储
                .enableStorage(false)
                //开启消息已读回执
                .enableReadReceipt(true);

//资料关系链扩展用户配置
        userConfig = new TIMUserConfigSnsExt(userConfig)
                //开启资料关系链本地存储
                .enableFriendshipStorage(true)
                //设置关系链变更事件监听器
                .setFriendshipProxyListener(new TIMFriendshipProxyListener() {
                    @Override
                    public void OnAddFriends(List<TIMUserProfile> users) {
//                        Log.i(tag, "OnAddFriends");
                    }

                    @Override
                    public void OnDelFriends(List<String> identifiers) {
//                        Log.i(tag, "OnDelFriends");
                    }

                    @Override
                    public void OnFriendProfileUpdate(List<TIMUserProfile> profiles) {
//                        Log.i(tag, "OnFriendProfileUpdate");
                    }

                    @Override
                    public void OnAddFriendReqs(List<TIMSNSChangeInfo> reqs) {
//                        Log.i(tag, "OnAddFriendReqs");
                    }

//                    @Override
//                    public void OnAddFriendGroups(List<TIMFriendGroup> friendgroups) {
//                        Log.i(tag, "OnAddFriendGroups");
//                    }
//
//                    @Override
//                    public void OnDelFriendGroups(List<String> names) {
//                        Log.i(tag, "OnDelFriendGroups");
//                    }
//
//                    @Override
//                    public void OnFriendGroupUpdate(List<TIMFriendGroup> friendgroups) {
//                        Log.i(tag, "OnFriendGroupUpdate");
//                    }
                });

//群组管理扩展用户配置
        userConfig = new TIMUserConfigGroupExt(userConfig)
                //开启群组资料本地存储
                .enableGroupStorage(true)
                //设置群组资料变更事件监听器
                .setGroupAssistantListener(new TIMGroupAssistantListener() {
                    @Override
                    public void onMemberJoin(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                    }

                    @Override
                    public void onMemberQuit(String groupId, List<String> members) {
                    }

                    @Override
                    public void onMemberUpdate(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                    }

                    @Override
                    public void onGroupAdd(TIMGroupCacheInfo groupCacheInfo) {
                    }

                    @Override
                    public void onGroupDelete(String groupId) {
                    }

                    @Override
                    public void onGroupUpdate(TIMGroupCacheInfo groupCacheInfo) {
                    }
                });

//将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);
        FriendshipInfo.getInstance();
    }

    private TIMFriendshipSettings initFriendshipSettings(){
        TIMFriendshipSettings settings = new TIMFriendshipSettings();

        long flags = 0;
        flags |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE
                | TIMFriendshipManager.TIM_PROFILE_FLAG_FACE_URL
                | TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        settings.setFlags(flags);


        return settings;
    }

    private void initLanguage(){

        String locale = Locale.getDefault().toString();
        int type = (int) SpUtil.getLong(getAppContext(),SpUtil.LANGUAGE,-1);
        Log.i("initLanguage","locale = " + locale + " ; =" + Locale.ENGLISH.toString() + "; type = " + type);
        //获取当前资源对象
        Resources resources = getResources();
        //获取设置对象
        Configuration configuration = resources.getConfiguration();
        //获取屏幕参数
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (type == -1)
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)){
                type = 0;
            }else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale) || "zh-rHK".equals(locale) ){
                type = 1;
            }else if (locale.startsWith("en")){
                type = 2;
            }else{
                type = 0;
            }
        Locale locale_ = Locale.SIMPLIFIED_CHINESE;;
                //发送结束所有activity的广播
        switch (type){
            case 0:
                locale_ = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                locale_ = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                locale_ = Locale.ENGLISH;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale_);
        } else {
            configuration.locale = locale_;
        }

        resources.updateConfiguration(configuration, displayMetrics);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        XXXX(); // 调用读取语言设置且更换APP语言的方法
    }

}
