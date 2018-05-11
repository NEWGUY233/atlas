package com.atlas.crmapp.workplace.view;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.atlas.crmapp.util.DateUtil;
import com.hyphenate.util.DensityUtil;

import java.util.Date;

/**
 * Created by Harry on 2017-05-09.
 */

public class IndicatorView extends FrameLayout {

    public int totalWidth;
    public int totalMinutes;

    public IndicatorView(Context context, int color, String lastTime, int lastX, String startTime, String endTime) {
        super(context);
        setBackgroundColor(color);
        float p = (float)totalWidth / (float)totalMinutes;
        Date now = (new Date());
        String startTimeStr = DateUtil.dateToString(now) + " " + startTime + ":00";
        String endTimeStr = DateUtil.dateToString(now) + " " + endTime + ":00";
        String lastTimeStr = DateUtil.dateToString(now) + " " + lastTime + ":00";
        Date startDate = DateUtil.parse(startTimeStr);
        Date endDate = DateUtil.parse(endTimeStr);
        Date lastDate = DateUtil.parse(lastTimeStr);
        float interval = endDate.getTime() - startDate.getTime() / 1000 / 60;
        int width = Math.round(p) * Math.round(interval);
        float beginInterval = startDate.getTime() - lastDate.getTime() / 1000 / 60;
        int x = Math.round(p) * Math.round(beginInterval);


        LayoutParams layoutParams = new LayoutParams(DensityUtil.px2dip(context, width), ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams params = this.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            //基于View本身原有的布局参数对象
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
//        marginParams.setMargins(DensityUtil.px2dip(lastX)+DensityUtil.px2dip(x), topPx, rightPx, bottomPx);
//        this.setLayoutParams(marginParams);
//        lastX+x
//
//        this.set
    }


}
