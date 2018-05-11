package com.atlas.crmapp.widget.calendar;

import com.atlas.crmapp.R;
import com.atlas.crmapp.util.ContextUtil;

/**
 * Created by A.Developer on 2017/3/22.
 */

public class CalendarBean {

    public int year;
    public int moth;
    public int day;
    public int week;

    //-1,0,1
    public int mothFlag;

    //显示
    public String chinaMonth;
    public String chinaDay;

    public CalendarBean(int year, int moth, int day) {
        this.year = year;
        this.moth = moth;
        this.day = day;
    }

    public String getDisplayWeek(){
        String s="";
        switch(week){
            case 1:
                s= ContextUtil.getUtil().getContext().getString(R.string.w7);
                break;
            case 2:
                s=ContextUtil.getUtil().getContext().getString(R.string.w1);
                break;
            case 3:
                s=ContextUtil.getUtil().getContext().getString(R.string.w2);
                break;
            case 4:
                s=ContextUtil.getUtil().getContext().getString(R.string.w3);
                break;
            case 5:
                s=ContextUtil.getUtil().getContext().getString(R.string.w4);
                break;
            case 6:
                s=ContextUtil.getUtil().getContext().getString(R.string.w5);
                break;
            case 7:
                s=ContextUtil.getUtil().getContext().getString(R.string.w6);
                break;

        }
        return s ;
    }

    @Override
    public String toString() {
//        String s=year+"/"+moth+"/"+day+"\t"+getDisplayWeek()+"\t农历"+":"+chinaMonth+"/"+chinaDay;
        String s=year+"/"+moth+"/"+day+" - "+mothFlag;
        return s;
    }
}
