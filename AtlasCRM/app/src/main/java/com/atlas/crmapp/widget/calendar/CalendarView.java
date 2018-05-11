package com.atlas.crmapp.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.List;

/**
 * Created by A.Developer on 2017/3/23.
 */

public class CalendarView extends View {

    private int ROWS = 6;
    private int COLUMNS = 7;

    private int mRowHeight;
    private int mColumnWidth;

    // 画笔
    private Paint mPaint;
    // 日历背景颜色
    private int mBackgroundColor = Color.parseColor("#FFFFFF");
    // 本月日期的颜色
    private int mMonthDateColor = Color.parseColor("#000000");
    // 非本月日期的颜色
    private int mOtherDateColor = Color.parseColor("#AEAEAE");
    // 选中日期的颜色
    private int mSelectedDateColor = Color.parseColor("#FFDC00");
    // 日期字体大小
    private int mDateSize = 15;
    // 可选、选择日期的圆圈半径
    private float mCircleR;
    // DisplayMetrics对象
    private DisplayMetrics mDisplayMetrics;

    private List<CalendarBean> mDates;

    private Calendar mCalendar = Calendar.getInstance();

    public void setDate(List<CalendarBean> mDates){
        this.mDates = mDates;
        requestLayout();
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取DisplayMetrics实例
        mDisplayMetrics = getResources().getDisplayMetrics();
        //  默认当前年/月
        mDates = CalendarFactory.getMonthOfDayList(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 每一列宽度
        mColumnWidth = getWidth() / COLUMNS;
        // 每一行高度 + 1 是为了每个日期宽高相等
        mRowHeight = getHeight() / (ROWS + 1);

        if (mDates == null){
            return;
        }

        // new一个Paint实例(抗锯齿)
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 画背景颜色
        canvas.drawColor(mBackgroundColor);
        drawDate(canvas);
    }

    private void drawDate(Canvas canvas) {
        float x;
        float y;
        int column = 0;
        int row = 1;
        for (int i = 0; i < mDates.size(); i ++){
            x = column * mColumnWidth + mColumnWidth / 2;
            y = row * mRowHeight - mRowHeight / 2;
            column ++;
            if (column  == 7){
                column = 0;
                row += 1;
            }
            drawNumber(canvas, i, x, y);
        }
    }

    private void drawNumber(Canvas canvas, int i, float x, float y) {
        int flag = mDates.get(i).mothFlag;
        String text = mDates.get(i).day + "";
        mPaint.setTextSize(mDateSize * mDisplayMetrics.scaledDensity);
        Rect mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        drawSelectedBackground(canvas, x, y);
        mPaint.setColor(flag == 0 ? mMonthDateColor : mOtherDateColor);
        canvas.drawText(text, x - mBound.width() / 2, y + mBound.height() / 2, mPaint);
    }

    private void drawSelectedBackground(Canvas canvas, float x, float y){
        mPaint.setColor(mSelectedDateColor);
        float mCircleR = (float) (mColumnWidth / 2 * 0.7);
        canvas.drawCircle(x, y, mCircleR, mPaint);
    }

    private int downX, downY, upX, upY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getX();
                upY = (int) event.getY();
                if (Math.abs(downX - upX) < 10 && Math.abs(downY - upY) < 10){
                    performClick();
                    handleClick((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void handleClick(int x, int y) {
        // 获取行
        int row = y / mRowHeight;
        // 获取列
        int column = x / mColumnWidth;

        int dateIndex = row * ROWS + row + column;
        CalendarBean bean = mDates.get(dateIndex);
        if (bean.mothFlag == 0){
            if (l != null){
                l.OnCalendarClick(bean.year, bean.moth, bean.day);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getWidth() - getWidth() / 4;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = width / COLUMNS * ROWS;
        }
        setMeasuredDimension(width, height);
    }

    public interface onCalendarClick {
       void OnCalendarClick(int y, int m, int d);
    }

    private onCalendarClick l;

    public void setOnCalendarClick(onCalendarClick l) {
        this.l = l;
    }
}
