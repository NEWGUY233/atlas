package com.atlas.crmapp.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.wheel.PhoneWheelAdapter;
import com.atlas.crmapp.bean.IndexTopBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.register.RegisterActivity;
import com.atlas.crmapp.tim.model.Conversation;
import com.atlas.crmapp.tim.model.SystemBean;
import com.atlas.crmapp.tim.model.SystemConversation;
import com.atlas.crmapp.view.wheel.WheelView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.com.reformer.rfBleService.BleDevContext;
import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by Administrator on 2017/6/2.
 */

public class AppUtil {

    /**
     * 安装应用
     *
     * @param context
     * @param filename
     *            apk路径
     * @return true成功 false失败
     */
    public static boolean installApp(Context context, String filename) {
        try {
            File fapps = new File(filename);
            if (!fapps.exists()) {
                throw new Exception("找不到" + filename + "文件");
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + filename),
                    "application/vnd.android.package-archive");
            // 或者
            // intent.setDataAndType(Uri.fromFile(new File(fileName)),
            // "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据包名获取对应应用的版本号
     *
     * (对比版本号可判断应用是否安装,当返回为null时,该包名对应的应用未安装)
     *
     * @param context
     * @param packageName
     * @return 返回对应的版本号,当对应的应用不存在则返回null
     */
    public static String getInstallPkgVersionByPackageName(Context context, String packageName) {
        String version = null;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            // e.printStackTrace();
        }
        if (packageInfo == null) {
            System.out.println("not installed");
        } else {
            System.out.println("is installed");
            version = packageInfo.versionName;
        }
        return version;
    }


    /**
     * 获取版本名称
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
        //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
        //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* 获取渠道名
     * @param context 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("CHANNEL_NAME: " , channelName);
        return channelName;
    }



    //判断app 是否处于后台
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }



    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 友盟测试 设备
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, android.Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }



    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     *
     * 渠道标志为：
     * 1，andriod（a）
     *
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        //deviceId.append("a");
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!StringUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return deviceId.toString();
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!StringUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!StringUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);

                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!StringUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);

                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context){
        String uuid = SpUtil.getString(context, Constants.SpKey.KEY_DEVICE_ID, "");
        if(StringUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            SpUtil.putString(context, Constants.SpKey.KEY_DEVICE_ID, uuid);
        }
        return uuid;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    public static HashMap getInfoParams(PersonInfoJson bean,String key,Object values){
        HashMap hashMap = new HashMap();
        if (bean == null) {
            hashMap.put(key, values);
            return hashMap;
        }

        if (!"job".equals(key)){
            hashMap.put("job",bean.getJob());
        }

        if (!"skill".equals(key)){
            hashMap.put("skill",bean.getSkill());
        }

        if (!"interestIdList".equals(key)){
            if (bean.getInterestList() != null && bean.getInterestList().size() > 0)
                hashMap.put("interestIdList",initInterest(bean.getInterestList()));
        }

        if (!"otherIndustry".equals(key)){
            if (!StringUtils.isEmpty(bean.getOtherIndustry()))
                hashMap.put("otherIndustry",bean.getOtherIndustry());
        }

        if (!"industryId".equals(key)){
            if (bean.getIndustry() != null && !StringUtils.isEmpty(bean.getIndustry().getName()))
                hashMap.put("industryId",bean.getIndustry().getIdX());
        }

        hashMap.put(key,values);
        return hashMap;
    }

    private static int[] initInterest(List<PersonInfoJson.InterestListBean> list){
        int[] interest = new int[list.size()];
        for (int i = 0 ; i < list.size() ; i++)
            interest[i] = list.get(i).getIdX();
        return interest;
    }

    public static void initBlueToothList(ArrayList<BleDevContext> list){
        if (list == null || list.size() == 0)
            return;
        for(int i =0;i < list.size() - 1;i++)
        {
            for(int j = 0;j <  list.size() - 1-i;j++)// j开始等于0，
            {
                if(list.get(j).rssi < list.get(j+1).rssi)
                {
                    BleDevContext bean = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,bean);
                }
            }
        }

    }

    public static List<String> getAreaList(){
        List<String> list = new ArrayList<>();
        list.add("中国 +86");

        return list;
    }

    public static void initConversationList(List<Conversation> conversationList){
        if (conversationList == null)
            return;
        SystemConversation conversation;
        SystemBean bean;
        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_contacts);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.contact));
        bean.setIdentify("");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);

        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_fitness_notice);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.chat_sports));
        bean.setIdentify("sport_admin");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);

        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_office_notice);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.chat_workplace));
        bean.setIdentify("workplace_admin");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);

        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_kitchen_notice);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.chat_kitchen));
        bean.setIdentify("kitchen_admin");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);

        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_notice);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.chat_notice));
        bean.setIdentify("notice_admin");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);

        bean = new SystemBean();
        bean.setIcon(R.mipmap.im_icon_mes_friend_notice);
        bean.setName(ContextUtil.getUtil().getContext().getString(R.string.chat_dynamic));
        bean.setIdentify("admin");
        conversation = new SystemConversation(bean);
        conversationList.add(conversation);





    }

}
