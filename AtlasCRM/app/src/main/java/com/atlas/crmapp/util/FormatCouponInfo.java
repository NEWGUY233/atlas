package com.atlas.crmapp.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.AddedValueJson;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.SKUJson;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Harry on 2017-04-15.
 */

public class FormatCouponInfo {

    public static interface OnFormatCouponInfoDone {

        public void onFormatCouponInfoDone(String price,  String remark);
    }

    public static void formatCouponInfo(String type, double value1, double value2, final OnFormatCouponInfoDone onFormatCouponInfoDone) {

        if (Constants.CouponType.REACH.equals(type)) {
            String price = new DecimalFormat(getYuanStr() + "#").format(value2);
            String remark = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s41)).format(value1);
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } else if (Constants.CouponType.DISCOUNT.equals(type)) {
            String price = new DecimalFormat("#.0").format(value1*10.0);
            if(StringUtils.isNotEmpty(price)){
                if(price.contains(".0")){
                    price = price.replace(".0", "");
                }
                price  = price + ContextUtil.getUtil().getContext().getString(R.string.s42);
            }
            String remark = "";
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } else if (Constants.CouponType.FIX.equals(type)) {
            String price = "";
            if(value1 < 1){
                price = ContextUtil.getUtil().getContext().getString(R.string.s43);
            }else{
                price = formatDoublePrice(value1, 2) + ContextUtil.getUtil().getContext().getString(R.string.s44);
            }
            String remark = "";
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        }/* else if (Constants.CouponType.equals("NXMY")) {
            String price = new DecimalFormat("增#.00").format(value2);
            String remark = new DecimalFormat("买#.00").format(value1);
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } */
        else if (Constants.CouponType.BXGY.equals(type)){
            String price = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s45)).format(value1);
            String remark = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s46)).format(value2);
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        }
    }

    public static void formatCouponInfo(String type, double value1, double value2, final OnFormatCouponInfoDone onFormatCouponInfoDone, TextView tvUnit) {

        if (type.equals("REACH")) {
            tvUnit.setVisibility(View.VISIBLE);
            String price = new DecimalFormat("#.00").format(value2);
            String remark = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s47)).format(value1);
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } else if (type.equals("DISCOUNT")) {
            tvUnit.setVisibility(View.GONE);
            String price = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s48)).format(value1*10);
            String remark = "";
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } else if (type.equals("FIX")) {
            tvUnit.setVisibility(View.VISIBLE);
            String price = ContextUtil.getUtil().getContext().getString(R.string.s49);
            String remark = "";
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        } else if (type.equals("NXMY")) {
            tvUnit.setVisibility(View.GONE);
            String price = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s50)).format(value2);
            String remark = new DecimalFormat(ContextUtil.getUtil().getContext().getString(R.string.s51)).format(value1);
            onFormatCouponInfoDone.onFormatCouponInfoDone(price, remark);
        }
    }

    public static String formatVaildDate(long startDate, long endDate) {
        String sd = longToDateStr("yyyy.MM.dd", startDate);
        String ed =longToDateStr("yyyy.MM.dd", endDate);
        //return String.format("有效期从:%s-%s", sd, ed);
        return String.format("%s - %s", sd, ed);
    }

    private static String longToDateStr(String dateFormat, Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }


    public static String formatDoubleKeepTwo(double value){
        return String.format("%.2f", value);
    }


    /**
     *
     * @param price 价格
     * @param keeydigit 保留的位数
     * @return  格式化后的值
     */
    public static String formatDoublePrice(double price, int keeydigit){
        String formatPrice = String.format("%."+keeydigit+ "f", price);
        if(StringUtils.isEmpty(formatPrice)){
            formatPrice = "";
        }
        return formatPrice;
    }

    public static String formatDoublePriceToYuan(double price, int keeydigit){
        return String.format("%."+keeydigit+ "f", price) +ContextUtil.getUtil().getContext().getString(R.string.yuan);
    }

    /**
     *
     * @param unit  分钟数
     * @return
     */
    public static String formatUnitTimePrice(double price, int unit){

        if (unit >= 60) {
            int y = unit % 60;
            int z = unit / 60;
            if (y > 0) {
                return String.format(ContextUtil.getUtil().getContext().getString(R.string.s52), price, z, y);
            } else {
                if (z <= 1) {
                    return String.format(ContextUtil.getUtil().getContext().getString(R.string.s53), price);
                } else {
                    return String.format(ContextUtil.getUtil().getContext().getString(R.string.s54), price, z);
                }
            }
        } else {
            if (unit <= 1) {
                return String.format(ContextUtil.getUtil().getContext().getString(R.string.s55), price);
            } else {
                return String.format(ContextUtil.getUtil().getContext().getString(R.string.s56), price, unit);
            }
        }
        //return unit == 60 ?"元/小时":"元/分钟";
    }


    public static String formatUnitTimePrice(double price){
        return formatDoublePrice(price,0) +ContextUtil.getUtil().getContext().getString(R.string.s57);
    }

    public static  String getHZSsx(String gender){
        if(TextUtils.isEmpty(gender)){
            return null;
        }
        if(gender.equals(Constants.Sex.MALE)){
            return ContextUtil.getUtil().getContext().getString(R.string.male);
        }else if(gender.equals(Constants.Sex.FEMALE)){
            return ContextUtil.getUtil().getContext().getString(R.string.female);
        }else{
            return null;
        }
    }

    public static String getFormatDate(int year,int month, int day, int hour, int mins){
        return year+"-"+ String.format("%02d", month) +"-" + String.format("%02d", day) +" "+ String.format("%02d", hour)+":"+ String.format("%02d", mins) ;
    }
    public static String getFormatDate(int year,int month, int day){
        return year+"-"+ String.format("%02d", month) +"-" + String.format("%02d", day) ;
    }

    public static String getFormatTime(int hour, int mins){
        return  String.format("%02d", hour)+":"+ String.format("%02d" ,mins);

    }



    public static boolean isBeyondLimtDay(int limtDay, String curDate)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date cur = null;
        try {
            cur = sdf.parse(curDate);
            Logger.d( "cur.getTime()----"+ cur.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long districtTime = cur.getTime()- System.currentTimeMillis();
        BigDecimal limtTimes =  new BigDecimal(24 * 60 * 60 * 1000).multiply(new BigDecimal(limtDay));
        long limtTime = limtTimes.longValue();
        return districtTime > limtTime || districtTime < 0 ;
    }

    public static String getTransactionsType(String type){
        if(Constants.TransactionType.ADJUST.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s58);
        }else if(Constants.TransactionType.ORDER.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s59);
        }else if(Constants.TransactionType.RECHARGE.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s60);
        }else if(Constants.TransactionType.REDRAW.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s61);
        }else if(Constants.TransactionType.REDRAW_R.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s62);
        }else {
            return  "";
        }
    }


    public static int getTransactionsTypeTextColor(Context context,String type ){

         if(Constants.TransactionType.ORDER.equals(type)){
            return ContextCompat.getColor(context, R.color.orange);
        }else if(Constants.TransactionType.RECHARGE.equals(type)){
            return ContextCompat.getColor(context, R.color.green);
        }else if(Constants.TransactionType.REDRAW.equals(type) || Constants.TransactionType.REDRAW_R.equals(type)){
            return ContextCompat.getColor(context, R.color.text_dark);
        }else {
            return ContextCompat.getColor(context, R.color.text_dark);
        }
    }

    public static int getCouponItemType (String msgType){
        if (Constants.PushMsgTpye.COUPON_BIND_MSG.equals(msgType)){
            return 0;
        }else{
            return 1;
        }
    }


    //获取预约会议的时间 间隔为15 分钟
    public static String  getMeetingStartTime(boolean isStartTime ){
        String strTime = DateUtil.formatTime(System.currentTimeMillis(), "HH:mm");
        String[] strTimes = strTime.split(":");
        int hour = Integer.valueOf(strTimes[0]);
        int minute = Integer.valueOf(strTimes[1]);
        if(minute < 15){
            strTimes[1] = "15";
        }else if(minute < 30){
            strTimes[1] = "30";
        }else if(minute < 45){
            strTimes[1] = "45";
        }else {
            if(hour < 9){
                strTimes[0] = "0"+ hour +1 ;
            }else{
                strTimes[0] = hour + 1 +"";
            }
            strTimes[1] = "00";
        }
        if(!isStartTime){
            String startHour = strTimes[0];
            int endTime = Integer.valueOf(startHour) + 1;
            if(endTime < 9){
                strTimes[0] = "0"+ endTime ;
            }else{
                strTimes[0] = endTime + "";
            }
        }
        return strTimes[0] + ":" + strTimes[1];
    }


    public static String getAppointmentTitle(String type){
        if(Constants.AppointmentType.MEETING.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s63);
        }else if(Constants.AppointmentType.FITNESS_L.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s64);
        }else if(Constants.AppointmentType.ACTIVITY.equals(type)){
            return ContextUtil.getUtil().getContext().getString(R.string.s65);
        }else {
            return "";
        }
    }


    public static String getFitContractOrAllowancePrice(double price, int keepDigit,  boolean isFitContractOrAllowance){
        String priceStr = null;
        if(isFitContractOrAllowance){
            priceStr =ContextUtil.getUtil().getContext().getString(R.string.s66);
        }else{
            priceStr = getYuanStr() +FormatCouponInfo.formatDoublePrice(price, keepDigit);
        }
        return  priceStr;
    }

    public static String getYuanStr(){
        //"￥"
        char rmb = 165;
        return  String.valueOf(rmb);
    }

    public static String getYuanStr(String acount){
        //"￥"
        char rmb = 165;
        return  String.valueOf(rmb) + acount;
    }

    public static String getMeetingEndTIme(String startTIme){
        String endTime = "";
        if(StringUtils.isNotEmpty(startTIme)){
            if(startTIme.contains(":")){
                String[] time = startTIme.split(":");
                int hour = Integer.valueOf(time[0]);
                hour = hour+ 1;
                if (hour < 10){
                    endTime = "0" + String.valueOf(hour) + ":"+ time[1];
                }else{
                    endTime = String.valueOf(hour) + ":" + time[1];
                }
            }
        }
        return endTime;
    }


    /**
     * 移除重复的时间
     * @param times
     * @return
     */
    public static List<MeetingRoomJson.OccupyTime> removeOverTime(List<MeetingRoomJson.OccupyTime> times){
        int timesSize = times.size();
        int s = -1;//代表
        if(timesSize > 1){
            MeetingRoomJson.OccupyTime currTime = null;
            MeetingRoomJson.OccupyTime preTime = null;
            List<MeetingRoomJson.OccupyTime> removeIndexs = new ArrayList<>();
            for(int i = 0; i < timesSize; i++ ){
                currTime = times.get(i);
               // Logger.d("---------" +  currTime.startTime + " "  + currTime.endTime);
                if(i > 0){
                    preTime = times.get(i -1);
                    if(preTime != null){
                        if(preTime.endTimestamp >= currTime.startTimestamp){
                            times.get(s).endTime = currTime.endTime;
                            //preTime.endTime = currTime.endTime;
                            removeIndexs.add(currTime);
                        }else{
                            s = i;
                        }
                        //Logger.d("i -------"+ i +" "+ preTime.startTime +" "+ preTime.endTime +" -- "+ currTime.startTime +" "+ currTime.endTime);

                    }
                }else {
                    s = i;
                }
            }

            for (MeetingRoomJson.OccupyTime index : removeIndexs){
                times.remove(index);
            }
        }
        return times;
    }


    public static List<MeetingRoomJson.OccupyTime> removeOverTimes(List<MeetingRoomJson.OccupyTime> times){
        int timesSize = times.size();
        List<MeetingRoomJson.OccupyTime> removeIndexs = new ArrayList<>();

        if(timesSize > 1){
            MeetingRoomJson.OccupyTime currTime = null;
            MeetingRoomJson.OccupyTime preTime = null;
            for(int i = 0; i <timesSize; i++ ){
                currTime = times.get(i);
                if(i > 0){
                    preTime = times.get(i -1);
                }
                if(preTime != null){
                    if(preTime.endTimestamp > currTime.startTimestamp){
                        preTime.endTime = currTime.endTime;
                        //removeIndexs.add(currTime);
                        Logger.d("i -------"+ i +" "+ preTime.startTime +" "+ preTime.endTime +" -- "+ currTime.startTime +" "+ currTime.endTime);
                    }
                    removeIndexs.add(currTime);

                }
            }
        }
        return removeIndexs;
    }


    public static String getOrderState(String state){
        if(Constants.ORDER_STATE.COMPLETE.equals(state)){
            return ContextUtil.getUtil().getContext().getString(R.string.s67);
        }else if(Constants.ORDER_STATE.CONFIRM.equals(state) || Constants.ORDER_STATE.OPEN.equals(state)){
            return ContextUtil.getUtil().getContext().getString(R.string.s68);
        }else if(Constants.ORDER_STATE.REDRAW.equals(state)){
            return ContextUtil.getUtil().getContext().getString(R.string.s69);
        }else {
            return "";
        }
    }


    public static void setLeftRightToGrayBlack(View view){
        if(view instanceof  TextView){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_gray_simple);
            ((TextView) view).setTextColor(Color.parseColor("#FF2B3039"));
        }else if(view instanceof Button){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_gray_simple);
            ((Button) view).setTextColor(Color.parseColor("#FF2B3039"));
        }
    }

    public static void setLeftRightToGrayWhite(View view){
        if(view instanceof  TextView){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_gray_simple);
            ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
        }else if(view instanceof Button){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_gray_simple);
            ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
        }
    }


    public static void setLeftRightToYellowWhite(View view){
        if(view instanceof  TextView){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_yellow);
            ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
        }else if(view instanceof Button){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_yellow);
            ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public static void setLeftRightToYellowBlack(View view){
        if(view instanceof  TextView){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_yellow);
            ((TextView) view).setTextColor(Color.parseColor("#FF2B3039"));
        }else if(view instanceof Button){
            view.setBackgroundResource(R.drawable.bg_left_right_circle_yellow);
            ((Button) view).setTextColor(Color.parseColor("#FF2B3039"));
        }
    }

    /**
     * 设置 透明度
     * @param view
     * @param isToTransparent
     */
    public static void setViewToTransparent(View view, boolean isToTransparent){
        if(view instanceof View  ){
            if(isToTransparent){
                view.setAlpha(0.5f);
                view.setClickable(false);
            }else{
                view.setAlpha(1.0f);
                view.setClickable(true);
            }
        }
    }



    //设置用户信息
    public static void setUserHeadAndNameAndCompany(Context context,TextView tvName, TextView tvCompany, ImageView ivHeader){
        PersonInfoJson personInfoJson = GlobalParams.getInstance().getPersonInfoJson();
        if(personInfoJson != null){
            String name = personInfoJson.getNick();
            if(tvName != null){
                tvName.setText(StringUtils.isNotEmpty(name) ? name : "");
            }

            if(tvCompany != null){
                String company = personInfoJson.getCompany();
                tvCompany.setText(StringUtils.isNotEmpty(company )? company :"");
            }

            if(ivHeader != null){
                GlideUtils.loadCustomImageView(context, R.drawable.header_icon, personInfoJson.getAvatar(), ivHeader);
            }

        }

    }


    /**
     * 获取有虎拳业态类型
     * @param bizCode
     * @return
     */
    public static String getCouponTypeName(String bizCode){
        String title ;
        if(StringUtils.isEmpty(bizCode)){
            title =ContextUtil.getUtil().getContext().getString(R.string.s70);
        }else{
            title = GlobalParams.getInstance().getBizName(bizCode);
        }
        return  title;
    }


    /**
     * 获取设备名称
     * @param featureList
     * @return
     */
    public static String getFeatureName(List<MeetingRoomJson.Feature> featureList, String addStr){
        String featureName = "";
        for(MeetingRoomJson.Feature feature : featureList){
            featureName = feature.name + addStr;
        }
        if(StringUtils.isNotEmpty(featureName)){
            featureName = featureName.substring(0 , featureName.length() - addStr.length());
        }
        return  featureName;
    }



    public static int getCouponBg(String bizCode){
        GlobalParams gp = GlobalParams.getInstance();
         if(StringUtils.isNotEmpty(bizCode)){
            if(bizCode.equals(gp.getCoffeeCode())){
                return  0xFFF57546;
            }else if(bizCode.equals(gp.getStudioCode())){
                return 0xFFD84667;
            }else if(bizCode.equals(gp.getFitnessCode())){
                return 0xFF464749;
            }else if(bizCode.equals(gp.getGogreenCode())){
                return  0xFF45BD61;
            }else if(bizCode.equals(gp.getKitchenCode())){
                return 0xFFFED505;
            }else if (bizCode.equals(gp.getWorkplaceCode())){
                return 0xFF227AD7;
            }
         }
          return 0xFFD84667;
    }


    public static int getCouponBgIcon(String bizCode){
        GlobalParams gp = GlobalParams.getInstance();
        if(StringUtils.isNotEmpty(bizCode)){
            if(bizCode.equals(gp.getCoffeeCode())){
                return  R.drawable.icon_coupon_coffee;
            }else if(bizCode.equals(gp.getStudioCode())){
                return  R.drawable.icon_coupon_studio;
            }else if(bizCode.equals(gp.getFitnessCode())){
                return  R.drawable.icon_coupon_fitness;
            }else if(bizCode.equals(gp.getGogreenCode())){
                return  R.drawable.icon_coupon_gogreen;
            }else if(bizCode.equals(gp.getKitchenCode())){
                return  R.drawable.icon_coupon_kitchen;
            }else if (bizCode.equals(gp.getWorkplaceCode())){
                return  R.drawable.icon_coupon_workplace;
            }
        }
        return  R.drawable.icon_coupon_no_limit;
    }



    //获取默认价格产品
    public static SKUJson getDefaultSKUJson(List<SKUJson> productSkus){
        SKUJson map = null;
        for(SKUJson skuJson : productSkus ){
            if(skuJson.defaultFlag ){
                map = skuJson ;
                break;
            }
        }

        if(map == null ){
            if(productSkus != null && productSkus.size() > 0)
            map = productSkus.get(0);
        }
        return  map;
    }


    public static String getActivityValidTimeMin(long startTime , long endTime){
        return DateUtil.timesMin(startTime) + " - " + DateUtil.timesMin(endTime);
    }



    public static String getActivityValidTime(long startTime , long endTime){
        return DateUtil.timesMin(startTime) + " 至 " + DateUtil.timesMin(endTime);
    }


    public static String getActivityValidTime(long startTime , long endTime, String insertText){
        return DateUtil.times(startTime) + " " + insertText  +" " + DateUtil.times(endTime);
    }



    /**
     *  1、获取main在窗体的可视区域
     *  2、获取main在窗体的不可视区域高度
     *  3、判断不可视区域高度
     *      1、大于100：键盘显示  获取Scroll的窗体坐标
     *                           算出main需要滚动的高度，使scroll显示。
     *      2、小于100：键盘隐藏
     *
     * @param main 根布局
     * @param scroll 需要显示的最下方View
     */
    public static void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }


    /**
     * 获得所有增值服务 名
     * @param addedValueJsons
     * @return
     */
    public static String getAddedValueText(List<AddedValueJson> addedValueJsons){
        if(addedValueJsons != null && addedValueJsons.size() >0){
            int size = 0;
            for(AddedValueJson addedValueJson : addedValueJsons){
                if(addedValueJson.isSelect()){
                    size = size + 1;
                }
            }
            StringBuffer sb = new StringBuffer();
            int appendSize = 0;
            for(AddedValueJson addedValueJson : addedValueJsons){
                if(addedValueJson.isSelect()){
                    sb.append(addedValueJson.getName());
                    appendSize = appendSize + 1;
                    if(appendSize < size ){
                        sb.append("\n");
                    }
                }
            }
            return sb.toString();
        }
        return  "";
    }


    /**
     * 剩余名额：%1$d(已使用：%2$d)
     * @param context
     * @param consumed
     * @return
     */
    public static Spanned getReminConsumedText(Context context, int quantity, int consumed){
        return  Html.fromHtml(context.getString(R.string.activity_remain_and_used_num, quantity - consumed, consumed));
    }


    public static String  getOnBuglyUpdateVersion(){
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if(upgradeInfo == null){
            return "";
        }else{
            return upgradeInfo.versionName;
        }
    }




}
