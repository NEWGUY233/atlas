package com.atlas.crmapp.workplace.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.hyphenate.util.DensityUtil;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Harry on 2017-05-09.
 */

public class MeetingScheduleView extends LinearLayout {

    private Context context;
    private List<MeetingRoomJson.OccupyTime> mTimeList;
    private String mStartTime;
    private String mEndTime;
    private int green, red;
    private Map<String , RelativeLayout> relativeLayoutMap = new HashMap<>();

    public MeetingScheduleView(Context context) {
        this(context, null);
        initView(context);
    }

    public MeetingScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public MeetingScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        setClipChildren(false);
        green = ContextCompat.getColor(context, R.color.time_line_green);
        red = ContextCompat.getColor(context, R.color.time_line_gray);


//        green = ContextCompat.getColor(context, R.color.time_line_gray);
//        red = ContextCompat.getColor(context, R.color.time_line_green);

    }

    public void showScheduleView(List<MeetingRoomJson.OccupyTime> timeList, String startTime, String endTime) {
        for(MeetingRoomJson.OccupyTime occupyTime : timeList){
            String fakeDate = "2017-01-01" + " " + occupyTime.startTime + ":00";
            String endDate = "2017-01-01" + " " + occupyTime.endTime + ":00";
            occupyTime.startTimestamp = DateUtil.parse(fakeDate).getTime();
            occupyTime.endTimestamp = DateUtil.parse(endDate).getTime();
        }
        mTimeList = timeList;
        Collections.sort(mTimeList, new Comparator<MeetingRoomJson.OccupyTime>() {
            @Override
            public int compare(MeetingRoomJson.OccupyTime occupyTime, MeetingRoomJson.OccupyTime t1) {
                return  new Double(occupyTime.startTimestamp).compareTo(new Double(t1.startTimestamp));
            }
        });

        mTimeList = FormatCouponInfo.removeOverTime(mTimeList);
        mStartTime = startTime;
        mEndTime = endTime;
        post(new Runnable() {
            @Override
            public void run() {

                MeetingScheduleView.this.removeAllViews();
                Date now = (new Date());
                String startTimeStr = DateUtil.dateToString(now) + " " + mStartTime + ":00";
                String endTimeStr = DateUtil.dateToString(now) + " " + mEndTime + ":00";

                Date startDate = DateUtil.parse(startTimeStr);
                Date endDate = DateUtil.parse(endTimeStr);

                int totalWidth = MeetingScheduleView.this.getMeasuredWidth();
                int totalMinutes = (int)(endDate.getTime() / 1000 / 60 - startDate.getTime() / 1000 / 60);
                float p = (float)totalWidth / (float)totalMinutes;
                for (String key : relativeLayoutMap.keySet()){
                    removeView(relativeLayoutMap.get(key));;
                }
                int currentWidth = 0;
                if (mTimeList.size() == 0) {
                    currentWidth = totalWidth;
                    addBar(green, totalWidth, "first", mStartTime, mEndTime);
                }

                for (int i=0; i<mTimeList.size(); i++) {
                    String usedStartTimeStr = DateUtil.dateToString(now) + " " + checkData(mTimeList.get(i).startTime) + ":00";
                    String usedEndTimeStr = DateUtil.dateToString(now) + " " + checkData(mTimeList.get(i).endTime) + ":00";
                    Date usedStartTime = DateUtil.parse(usedStartTimeStr);
                    Date usedEndTime = DateUtil.parse(usedEndTimeStr);
                    if (i == 0) {
                        int fvWidth = getTimeWidth(p, getBarTimeInterval(startDate, usedStartTime ));
                        currentWidth = currentWidth + fvWidth;
                        addBar(green, fvWidth, "first" + "GREEN", mStartTime, "");

                        int vWidth = getTimeWidth(p, getBarTimeInterval(usedStartTime, usedEndTime));
                        currentWidth = currentWidth + vWidth;
                        addBar(red, vWidth, checkData(mTimeList.get(i).startTime) + mTimeList.get(i).endTime + "RED", checkData(mTimeList.get(i).startTime),checkData(mTimeList.get(i).endTime));

                    } else if (i > 0) {
                        String usedLastTimeStr = DateUtil.dateToString(now) + " " + mTimeList.get(i-1).endTime + ":00";
                        Date usedLastTime = DateUtil.parse(usedLastTimeStr);

                        int svWidth = getTimeWidth(p, getBarTimeInterval(usedLastTime, usedStartTime));
                        currentWidth = currentWidth + svWidth;
                        addBar(green, svWidth, "split"+"GREEN" + mTimeList.get(i).endTime, "", "");

                        int vWidth = getTimeWidth(p, getBarTimeInterval(usedStartTime, usedEndTime));
                        currentWidth = currentWidth + vWidth;
                        addBar(red, vWidth, checkData(mTimeList.get(i).startTime) + mTimeList.get(i).endTime +"RED", checkData(mTimeList.get(i).startTime), checkData(mTimeList.get(i).endTime));
                    }
                    if (i == mTimeList.size() - 1 && checkEndTime(mTimeList.get(i).endTime)) {
                        int lvWidth = totalWidth - currentWidth;
                        addBar(green, lvWidth, "last" +"GREEN" , "", mEndTime);
                    }
                }
            }
        });
        //handler.postDelayed(runnable, 10);
    }

    //only the parents is LL can use clipchildren, it can make text out of view
    private void addBar(int color, int width, String tag, String startStr, String endStr) {
        //DensityUtil.px2dip(mContext, width);
//        RelativeLayout relativeLayout = null;
        if(true){
//            relativeLayout = new RelativeLayout(getContext());
//            relativeLayout.setTag(tag);
//            LinearLayout.LayoutParams relLayoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
//            relativeLayout.setLayoutParams(relLayoutParams);
//
//            View imageView = new View(context);
//            int id = (int) System.currentTimeMillis();
//            imageView.setId(getId());
//            imageView.setTag(id);
//            RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 8));
//            imageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
//            imageView.setBackgroundColor(color);
//            imageView.setLayoutParams(imageViewLayoutParams);
//            relativeLayout.addView(imageView);
//
//            TextView startTextView = getTextView(startStr);
//            RelativeLayout.LayoutParams startTextLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            startTextLayoutParams.addRule(RelativeLayout.ABOVE, imageView.getId());
//            startTextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            startTextLayoutParams.bottomMargin = 2;
//            startTextView.setLayoutParams(startTextLayoutParams);
//            startTextView.setSingleLine(true);
//            relativeLayout.addView(startTextView);
//
//            TextView endTextView = getTextView(endStr);
//            RelativeLayout.LayoutParams endTextLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            endTextLayoutParams.addRule(RelativeLayout.BELOW, imageView.getId());
//            endTextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            endTextLayoutParams.topMargin = 2;
//            endTextView.setLayoutParams(endTextLayoutParams);
//            endTextView.setSingleLine(true);
//            relativeLayout.addView(endTextView);
//            relativeLayout.setTag(imageView.getId());
//            //relativeLayoutMap.put(tag, relativeLayout);
//            this.addView(relativeLayout);

            LinearLayout ll = new LinearLayout(getContext());
            ll.setTag(tag);
            LinearLayout.LayoutParams relLayoutParams1 = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            ll.setLayoutParams(relLayoutParams1);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView startTextView = getTextView(startStr);
            LinearLayout.LayoutParams startTextLayoutParams;
            if (measureText(startTextView) > width){
                startTextLayoutParams = new LinearLayout.LayoutParams(measureText(startTextView), RelativeLayout.LayoutParams.WRAP_CONTENT);
                startTextLayoutParams.gravity = Gravity.RIGHT;
            }else {
                startTextLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                startTextLayoutParams.gravity = Gravity.LEFT;
            }

//            startTextLayoutParams.addRule(RelativeLayout.ABOVE, imageView.getId());
//            startTextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            startTextLayoutParams.bottomMargin = 2;
            startTextView.setLayoutParams(startTextLayoutParams);
            startTextView.setSingleLine(true);
            ll.addView(startTextView);

            View imageView = new View(context);
            int id = (int) System.currentTimeMillis();
            imageView.setId(getId());
            imageView.setTag(id);
            LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 8));
//            imageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
            imageView.setBackgroundColor(color);
            imageView.setLayoutParams(imageViewLayoutParams);
            ll.addView(imageView);

            TextView endTextView = getTextView(endStr);
            LinearLayout.LayoutParams endTextLayoutParams;
            if (measureText(endTextView) > width){
                endTextLayoutParams = new LinearLayout.LayoutParams(measureText(endTextView), RelativeLayout.LayoutParams.WRAP_CONTENT);
                endTextLayoutParams.gravity = Gravity.LEFT;
            }else {
                endTextLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                endTextLayoutParams.gravity = Gravity.RIGHT;
            }
//            endTextLayoutParams.addRule(RelativeLayout.BELOW, imageView.getId());
//            endTextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            endTextLayoutParams.topMargin = 2;
            endTextView.setLayoutParams(endTextLayoutParams);
            endTextView.setSingleLine(true);
            ll.addView(endTextView);
            ll.setTag(imageView.getId());
            //relativeLayoutMap.put(tag, relativeLayout);
            this.addView(ll);
//            Log.i("measureText","width = " + measureText(endTextView) + " ; text = " + endTextView.getText() + " ;width = " + width + " ; m = " + endTextView.getMeasuredWidth());


        } else {
//            relativeLayout = relativeLayoutMap.get(tag);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int s = getWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


    }

    //获取时间段view长度
    private int getTimeWidth(float p , float timeInterval){
        BigDecimal bigDecimalP = new BigDecimal(p);
        BigDecimal bigDecimalWidth = new BigDecimal(timeInterval);
        BigDecimal bigDecimalResult = bigDecimalP.multiply(bigDecimalWidth);
        return  bigDecimalResult.intValue();
    }

    //获取时间间隔
    private  float getBarTimeInterval(Date startDate , Date endDate ){
        return  endDate.getTime() / 1000 / 60 - startDate.getTime() / 1000 / 60;
    }

    private TextView getTextView(String tvText){
        TextView tv = new TextView(context);
        if (StringUtils.isNotEmpty(tvText) && tvText.contains(":")) {
            String[] endStrs = tvText.split(":");
                if(tvText.contains("00")){
                    Logger.d( "endStrs[0]-  " + endStrs[0]);
                    tv.setText(endStrs[0]);
                }else{
                    tv.setText(tvText);
                }
        }
        tv.setTextColor(Color.DKGRAY);
        tv.setTextSize(8);
        return  tv;
    }

    private String checkData(String data){
        String time = "";
        try {
            int i = Integer.valueOf(data.substring(0,data.indexOf(":")));
            if (i < 9 ){
                time = "09:00";
            }else if (i > 18){
                time = "18:00";
            }else {
                time = data;
            }
        }catch (Exception e){
            time = data;
        }
        return time;
    }

    private boolean checkEndTime(String data){
        try {
            int i = Integer.valueOf(data.substring(0,data.indexOf(":")));
            if (i >= 18){
                return  false;
            }
        }catch (Exception e){
        }
        return true;
    }

    private int measureText(TextView tv){
        //使用画笔
        Paint mPaint = tv.getPaint();
        Rect mTextBound = new Rect();
        mPaint.getTextBounds(tv.getText().toString(), 0, tv.getText().toString().length(), mTextBound);
        //得到文本宽度
        return mTextBound.width();
    }
}
