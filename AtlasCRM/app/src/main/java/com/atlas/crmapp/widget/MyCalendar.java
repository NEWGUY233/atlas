package com.atlas.crmapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"DrawAllocation", "SimpleDateFormat"})
public class MyCalendar extends View {

    private String TAG = MyCalendar.class.getSimpleName();

    public static final int SUNDAY = 1, MONDAY = 2, TUESDAY = 3, WEDNESDAY = 4, THURSDAY = 5, FRIDAY = 6, SATURDAY = 7;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    //out parm
    private Calendar mCalendar = Calendar.getInstance();
    private int mStartDay = 1;
    //data
    private Map<String, MyDay> mDays = new HashMap<String, MyDay>();
    private MyDay mCurrentDay;
    private Context mContext;
    //view parm
    private Canvas mCanvas;
    private int mParentWidth, mParentHeight;
    //iner parm
    private boolean isInit = true;
    private int mIndexFirstMonth;
    private int white = Color.WHITE;
    private int blue70 = Color.parseColor("#B2B2B2");
    private int grey70 = Color.parseColor("#2184C6");

    public MyCalendar(Context context) {
        super(context);
        mContext = context;
    }

    public MyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setInit(Boolean isInit) {
        this.isInit = isInit;
    }

    public void setCurrentDay(Calendar calendar) {
        mCurrentDay = mDays.get(dateFormat.format(calendar.getTime()));
        isInit = false;
        invalidate();
    }

    public void setTime(Calendar calendar) {
        mCalendar = calendar;
        initDays();
        invalidate();
    }

    private void initDays() {
        mDays.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        int _day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 1 - _day);
        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            mDays.put(dateFormat.format(calendar.getTime()), new MyDay(calendar));
            calendar.add(Calendar.DATE, 1);
        }
    }

    public Map<String, MyDay> getDays() {
        return mDays;
    }

    public String getMonth() {
        SimpleDateFormat sFormat = new SimpleDateFormat("MMMM");
        return sFormat.format(mCalendar.getTime());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthSize, height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int weekNumber = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
            height = width / 7 * weekNumber;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mParentWidth = canvas.getWidth();
        mParentHeight = canvas.getHeight();
        final float itemWidth = (float) mParentWidth / 7;
        final float itemHeight = (float) mParentWidth / 7;

        //TODO get first drawn at month
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        int _day = calendar.get(Calendar.DAY_OF_MONTH);
        //first day of month
        calendar.add(Calendar.DATE, 1 - _day);
        int weekIndex = mIndexFirstMonth = calendar.get(Calendar.DAY_OF_WEEK) - mStartDay;
        float firstLeft = 0 + itemWidth * weekIndex;
        float firstTop = 0;
        drawDay(canvas, calendar, firstLeft, firstTop);
        updateDay(calendar, firstLeft, firstTop, firstLeft, firstTop);

        float _left = firstLeft, _top = firstTop;
        for (int i = 1; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.add(Calendar.DATE, 1);
            weekIndex++;
            if (weekIndex == 7) {
                weekIndex = 0;
                _left = 0;
                _top = _top + itemHeight;
            } else {
                _left = _left + itemWidth;
            }
            drawDay(canvas, calendar, _left, _top);
            updateDay(calendar, firstLeft, firstTop, _left, _top);
        }
        isInit = false;
    }

    private void updateDay(Calendar calendar, float firstLeft, float firstTop, float _left, float _top) {
        if (mDays.get(dateFormat.format(calendar.getTime())) == null) {
            MyDay day = new MyDay(_left, _top, calendar);
            mDays.put(day.getDate(), day);
        } else {
            MyDay day = mDays.get(dateFormat.format(calendar.getTime()));
            day.setmClendar(calendar);
            day.setmLeft(firstLeft);
            day.setmTop(firstTop);
        }
    }

    private void drawDay(Canvas pCanvas, Calendar pCalendar, float pLeft, float pTop, int pCircleColor, int pTextColor, int pMiniColor) {
        Paint paint = new Paint();
        //TODO draw cricle
        paint.setColor(pCircleColor);
        paint.setAntiAlias(true);
        final float itemWidth = (float) mParentWidth / 7;
        float cx = pLeft + itemWidth / 2;
        float radius = itemWidth / 4;
        if (pCircleColor == white) radius += 1;
        float cy = pTop + itemWidth * 5 / 12;
        pCanvas.drawCircle(cx, cy, radius, paint);
        //TODO draw text
        paint.setColor(pTextColor);
        paint.setTextSize(itemWidth / 5);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        float textY = cy + offY;
        pCanvas.drawText("" + pCalendar.get(Calendar.DAY_OF_MONTH), cx, textY, paint);
        //TODO draw mini cricle
        paint.setColor(pMiniColor);
        float cricleY = pTop + itemWidth * 5 / 6;
        float miniRadius = itemWidth / 15;
        pCanvas.drawCircle(cx, cricleY, miniRadius, paint);
    }

    private void drawDay(Canvas pCanvas, Calendar pCalendar, float pLeft, float pTop) {
        int circleColor, textColor, minColor;
        circleColor = getCircleColor(pCalendar);
        textColor = getTestColor(pCalendar);
        minColor = getMinColor(pCalendar);
        drawDay(pCanvas, pCalendar, pLeft, pTop, circleColor, textColor, minColor);
    }

    private int getMinColor(Calendar pCalendar) {
        MyDay myDay = mDays.get(dateFormat.format(pCalendar.getTime()));
        if (myDay != null) {
            if (myDay.getMiniColor() == 0) {
                return white;
            } else {
                return myDay.getMiniColor();
            }
        }
        return white;
    }

    private int getTestColor(Calendar pCalendar) {
        int textColor;
        String dateString = dateFormat.format(pCalendar.getTime());
        if (mCurrentDay != null) {
            if (dateString.equals(mCurrentDay.getDate())) {
                return white;
            }
        } else if (dateFormat.format(Calendar.getInstance().getTime()).equals(dateString) && isInit) {
            return white;
        }
        int dayOfWeek = pCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7
                || (pCalendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                && pCalendar.get(Calendar.YEAR) < Calendar.getInstance().get(Calendar.YEAR))) {
            textColor = blue70;
        } else {
            textColor = Color.BLACK;
        }
        return textColor;
    }

    private int getCircleColor(Calendar pCalendar) {
        String dateString = dateFormat.format(pCalendar.getTime());
        if (mCurrentDay != null)
            Log.e(TAG, dateString + "   " + mCurrentDay.getDate());
        else
            Log.e(TAG, "        " + dateString + "");
        if (mCurrentDay != null) {
            if (dateString.equals(mCurrentDay.getDate())) return grey70;
        } else if (dateFormat.format(Calendar.getInstance().getTime()).equals(dateString) && isInit) {
            return grey70;
        }
        return Color.WHITE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float _x = event.getX();
            float _y = event.getY();
            float itemWidth = (float) mParentWidth / 7;
            float itemHeight = (float) mParentWidth / 7;
            int rowIndex = 0, columnIndex = 0;
            while (_x > 0) {
                columnIndex++;
                _x -= itemWidth;
            }
            while (_y > 0) {
                rowIndex++;
                _y -= itemHeight;
            }
            int dayOfMonth = (rowIndex - 1) * 7 + columnIndex - mIndexFirstMonth;
            if (dayOfMonth <= 0 || dayOfMonth > mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                return false;
            //Toast.makeText(mContext, mDays.get(indexOfMonth).toString(), 1000).show();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCalendar.getTime());
            calendar.add(Calendar.DATE, dayOfMonth - calendar.get(Calendar.DAY_OF_MONTH));
            MyDay day = mDays.get(dateFormat.format(calendar.getTime()));
            setCurrentDay(day.getmClendar());
            if (onDayChangeListener != null) onDayChangeListener.onDayChange(day, this);
            postInvalidate();
        }
        return true;
    }

    private OnDayChangeListener onDayChangeListener;

    public void setOnDayChangeListnter(OnDayChangeListener onDayChangeListener) {
        this.onDayChangeListener = onDayChangeListener;
    }

    public interface OnDayChangeListener {
        public void onDayChange(MyDay day, MyCalendar view);
    }
}
