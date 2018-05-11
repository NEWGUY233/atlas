package com.atlas.crmapp.util;

import android.util.Log;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.DateModl;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.alipay.sdk.util.l.c;

/**
 * Created by wu on 2017/4/4
 */

public class DateUtil {


    /**
     * 获取过去或者未来 任意天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static ArrayList<DateModl> test(int intervals ) {
      //  ArrayList<String> pastDaysList = new ArrayList<>();
        ArrayList<DateModl> fetureDaysList = new ArrayList<DateModl>();
        for (int i = 0; i <intervals; i++) {
          //  pastDaysList.add(getPastDate(i));
            DateModl modl=new DateModl();
            modl.setYeardate(getFetureDate(i));
            String day=  getFetureDate_dd(i);
            modl.setDayydate(day);
            String week=getFetureDate_week(i);
            modl.setWeek(week);
            fetureDaysList.add(modl);
        }
        return fetureDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Logger.e( result);
        return result;
    }

    /**
     *获取月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
       // Logger.e( result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static String getFetureDate_dd(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String result = format.format(today);
        return result;
    }


    /**
     * 获取未来 第 past 天的 星期
     * @param past
     * @return
     */
    public static String getFetureDate_week(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        String result = format.format(today);
        Logger.d( result);
        return result;
    }

    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static Date parse(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdr.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        String str = null;
        try {
            str = sdr.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getTodayDateTimes() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日",
                Locale.getDefault());
        return format.format(new Date());
    }

    public static String dateToString(Date date, String parse){
        SimpleDateFormat sdr = new SimpleDateFormat(parse);
        String str = null;
        try {
            str = sdr.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime_Today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(new java.util.Date());
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static long dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        Date date;
        long times=0;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            times = l;
//            String stf = String.valueOf(l);
//            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getTimestamp(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String timesStampToDate(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = time;
        String times = sdr.format(new Date(lcc));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String timesStampToDate_(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = time;
        String times = sdr.format(new Date(lcc));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16:09"）
     *
     * @param time
     * @return
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * @param time斜杠分开
     * @return
     */
    public static String timeslash(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd,HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * @param time斜杠分开
     * @return
     */
    public static String timeslashData(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//      int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

    /**
     * @param time斜杠分开
     * @return
     */
    public static String timeMinute(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");

        String times = sdr.format(new Date(time));
        return times;

    }

    public static String tim(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String time(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String times(long timeStamp, String format) {
        SimpleDateFormat sdr = new SimpleDateFormat(format);
        return sdr.format(new Date(timeStamp)).replaceAll("#",
                getWeek(timeStamp));

    }

    // 调用此方法输入所要转换的时间戳例如（1402733340）输出（"2014年06月14日16时09分00秒"）
    public static String times(long timeStamp) {
        return times(timeStamp, "yyyy.MM.dd");
    }

    public static String timesMin(long timeStamp) {
        return times(timeStamp, "yyyy.MM.dd HH:mm");
    }

    public static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s31);
        } else if (mydate == 2) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s32);
        } else if (mydate == 3) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s33);
        } else if (mydate == 4) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s34);
        } else if (mydate == 5) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s35);
        } else if (mydate == 6) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s36);
        } else if (mydate == 7) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s37);
        }
        return week;
    }

    // 并用分割符把时间分成时间数组
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
     *
     * @param time
     * @return
     */
    public String timesOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String timesTwo(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 并用分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public static String[] timestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(ContextUtil.getUtil().getContext().getString(R.string.s38));
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        String[] fenge = times.split(ContextUtil.getUtil().getContext().getString(R.string.s39));
        return fenge;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @param str
     * @param type
     *            例如：yy-MM-dd
     * @return
     */
    public static String getDateTimeByMillisecond(String str, String type) {

        Date date = new Date(Long.valueOf(str));

        SimpleDateFormat format = new SimpleDateFormat(type);

        String time = format.format(date);

        return time;
    }

    /**
     * 分割符把时间分成时间数组
     *
     * @param time
     * @return
     */
    public String[] division(String time) {

        String[] fenge = time.split("[年月日时分秒]");

        return fenge;

    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweek(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s31);
        } else if (mydate == 2) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s32);
        } else if (mydate == 3) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s33);
        } else if (mydate == 4) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s34);
        } else if (mydate == 5) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s35);
        } else if (mydate == 6) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s36);
        } else if (mydate == 7) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s37);
        }
        return week;

    }

    /**
     * 获取日期和星期　例如：２０１４－１１－１３　１１:００　星期一
     *
     * @param time
     * @param type
     * @return
     */
    public static String getDateAndWeek(String time, String type) {
        return getDateTimeByMillisecond(time + "000", type) + "  "
                + changeweekOne(time);
    }

    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String changeweekOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(times);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s31);
        } else if (mydate == 2) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s32);
        } else if (mydate == 3) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s33);
        } else if (mydate == 4) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s34);
        } else if (mydate == 5) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s35);
        } else if (mydate == 6) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s36);
        } else if (mydate == 7) {
            week = ContextUtil.getUtil().getContext().getString(R.string.s37);
        }
        return week;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new java.util.Date());
    }

    /**
     * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
     *
     * @param time
     * @return
     */
    public String week(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat( ContextUtil.getUtil().getContext().getString(R.string.s40));
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    /**
     * 输入日期如（2014-06-14-16-09-00）返回（星期数）
     *
     * @param time
     * @return
     */
    public String weekOne(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }

    public static Date parse(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date addSecond(Date date, int ammount) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.SECOND, ammount);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int ammount) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MINUTE, ammount);
        return calendar.getTime();
    }

    public static Date addHour(Date date, int ammount) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.HOUR, ammount);
        return calendar.getTime();
    }

    public static Date addDate(Date date, int ammount) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.DATE, ammount);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int ammount) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MONTH, ammount);
        return calendar.getTime();
    }

    public static String dateToStringWithoutSplit(Date date){
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd");
        String str = null;
        try {
            str = sdr.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String timesStampToDateTime(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = time;
        String times = sdr.format(new Date(lcc));
        return times;

    }


    /**
     *
     * @param time  时间戳
     * @param pattern  时间格式
     * @return
     */
    public static String formatTime(long time, String pattern){
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        String str = null;
        try {
            str = sdr.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取当月的 天数
     * */
    public static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     *计算时间间隔多少天，小于一天显示小时
     * @param endTime 时间
     * @return
     */
    public static long getInFutureDayNum(long endTime) {
        long oneDayTime = 1000 * 60 * 60 * 24;
        long currentTime = System.currentTimeMillis();
        long intervalTime  =  endTime - currentTime ;
        if (endTime <= 0){
            return 0;
        }
        if(intervalTime < 0){
            return 0;
        }/*else if(intervalTime < oneDayTime){
            int hour = (int) Math.floor(intervalTime / 1000 * 60 * 60.0);
            return hour + "小时";
        }*/else {
            int day = (int) (intervalTime / oneDayTime);
            if(day == 0){
                day = 1;
            }
            return day;
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s,String type){
        if (StringUtils.isEmpty(s) || StringUtils.isEmpty(type))
            return "";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String tim(String time,String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        String times = sdr.format(new Date(lcc));
        return times;
    }

    public static String tim(long time,String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type);
        @SuppressWarnings("unused")
        String times = sdr.format(new Date(time));
        return times;
    }

    /**
     *
     * @param time  时间戳
     * @return
     */
    public static String chatTime(long time){
        String type = "";
        long currentTime = Long.valueOf(dateToStamp(formatTime(System.currentTimeMillis()
                ,"yyyy-MM-dd-00-00-00"),"yyyy-MM-dd-HH-mm-ss")) /1000;

        long dev = time - currentTime;

        if (dev > 24 * 60 *60)
            type = "yyyy-MM-dd HH:mm";
        else if (dev <= 24 * 60 *60 && dev > 0)
            type = "HH:mm";
        else if (dev > - 24 * 60 *60 && dev < 0)
            type = ContextUtil.getUtil().getContext().getString(R.string.s100) + "HH:mm";
        else
            type = "yyyy-MM-dd HH:mm";

        SimpleDateFormat sdr = new SimpleDateFormat(type);
        String str = null;
        try {
            str = sdr.format(time * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
