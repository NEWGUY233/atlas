package com.atlas.crmapp.util;

/** 
* @author: Hoda_fang
* @date: 2017/6/19 15:58
* @desc:
*/

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.atlas.crmapp.model.PrintJson;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
//            .compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    private final static Pattern phone = Pattern
            .compile("^1[3-9][0-9]\\d{8}$");
    private final static Pattern phone_hk = Pattern
            .compile("[4|5|6|7|8|9][0-9]{7}");

    //^1[3-9][0-9]\d{8}$
    //^((13[0-9])|170|(15[^4,\D])|(18[0,5-9]))\d{8}$

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * 毫秒值转换为mm:ss
     *
     * @author kymjs
     * @param ms
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        String biz = SpUtil.getString(ContextUtil.getUtil().getContext(),SpUtil.AREA,"86");

        if (!"86".equals(biz))
            return phone_hk.matcher(phoneNum).matches();
        else
            return phone.matcher(phoneNum).matches();
    }

    public static boolean isPhone(String phoneNum,String location){
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        if ("86".equals(location)){
            return phone.matcher(phoneNum).matches();
        }else if ("852".equals(location)){
            return phone_hk.matcher(phoneNum).matches();
        }
        return phone.matcher(phoneNum).matches();
    }

    public static String getBizCode(String phoneNum){
        if (phone_hk.matcher(phoneNum).matches())
            return "852";
        if (phone.matcher(phoneNum).matches())
            return "86";
        return "86";
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * KJ加密
     */
    public static String KJencrypt(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c + 5));
        }
        return hex.toString();
    }

    /**
     * KJ解密
     */
    public static String KJdecipher(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c - 5));
        }
        return hex.toString();
    }



    public static boolean isBlank(String str) {
         int strLen;
        if (str == null || (strLen = str.length()) == 0) {
              return true;
           }
        for (int i = 0; i < strLen; i++) {
             if ((Character.isWhitespace(str.charAt(i)) == false)) {
                   return false;
                }
          }
        return true;
     }


    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        // 到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return source;// 如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }

    /**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(!StringUtils.isBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }


    //QQ号最短5位，微信号最短是6位最长20位
    public static boolean isQQOrWX(String qqorwx) {
        if(TextUtils.isEmpty(qqorwx)){
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(qqorwx);
        return m.matches();
    }

    //获取登陆打印机信息
    public static PrintJson getLoginInfo(String url){
        PrintJson bean = new PrintJson();
        bean.setCode(url.substring(url.indexOf("code=")+5,url.length()));
        Log.i("getLoginInfo","code = " + bean.getCode());
        bean.setType(url.substring(url.indexOf("type=")+5,url.indexOf("&code")));
        Log.i("getLoginInfo","type = " + bean.getType());
        return bean;
    }


    //解锁打印机
    public static String unLockPrint(String url){
        String[] str = url.split("&");
        for (String c : str){
            if (c.contains("stationId")){
                return c.substring(c.indexOf("stationId=") + "stationId=".length() ,c.length());
            }
        }
        return "";
    }

    //检查数目
    public static String numberCheck(String num){
        num = num.replace(".0","");
        try {
            long n = Long.valueOf(num);
            if (n > 1000){
                return n/1000 + "k";
            }else {
                return n + "";
            }

        }catch (Exception e){
            return num;
        }

    }

    public static String numberCheck(int num){
        if (num > 1000){
            return num/1000 + "k";
        }else {
            return num + "";
        }
    }

    public static String numberCheck(long num){
        if (num > 1000){
            return num/1000 + "k";
        }else {
            return num + "";
        }
    }

    public static String unicode2String(String s) {
        List<String> list =new ArrayList<String>();
        String zz="\\\\u[0-9,a-z,A-Z]{4}";

        //正则表达式用法参考API
        Pattern pattern = Pattern.compile(zz);
        Matcher m = pattern.matcher(s);
        while(m.find()){
            list.add(m.group());
        }
        for(int i=0,j=2;i<list.size();i++){
            String st = list.get(i).substring(j, j+4);

            //将得到的数值按照16进制解析为十进制整数，再強转为字符
            char ch = (char) Integer.parseInt(st, 16);
            //用得到的字符替换编码表达式
            s = s.replace(list.get(i), String.valueOf(ch));
        }
        return s;
    }
}