package com.atlas.crmapp.widget;

import android.annotation.SuppressLint;

import java.util.Calendar;

@SuppressLint("SimpleDateFormat")
public class MyDay {

    private float mLeft , mTop ;
    private Calendar mClendar = Calendar.getInstance() ;
    private int miniColor ;
    
    public int getMiniColor() {
		return miniColor;
	}

	public void setMiniColor(int miniColor) {
		this.miniColor = miniColor;
	}

	public float getmLeft() {
		return mLeft;
	}

	public void setmLeft(float mLeft) {
		this.mLeft = mLeft;
	}

	public float getmTop() {
		return mTop;
	}

	public void setmTop(float mTop) {
		this.mTop = mTop;
	}

	public Calendar getmClendar() {
		return mClendar;
	}
	
	public String getDate(){
		return MyCalendar.dateFormat.format(mClendar.getTime()) ;
	}

	public void setmClendar(Calendar mClendar) {
		this.mClendar.setTime(mClendar.getTime());
	}
	
	public MyDay(Calendar calendar) {
		super();
		this.mClendar.setTime(calendar.getTime());
	}

	MyDay(float mLeft , float mTop , Calendar mClendar){
        this.mLeft = mLeft ;
        this.mTop = mTop ;
        this.mClendar.setTime(mClendar.getTime()); ;
    }

    @Override
    public String toString() {
        return mClendar.get(Calendar.DAY_OF_MONTH)+"";
    }
}
