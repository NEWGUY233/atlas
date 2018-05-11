package com.atlas.crmapp.widget.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A.Developer on 2017/3/25.
 */

public class CalendarViewPager extends ViewPager {

//    private ArrayList<CalendarView> views;

    private final int PAGE_COUNT = 1000;

    private Map<Integer, CalendarView> map = new HashMap<>();

    public CalendarViewPager(Context context) {
        this(context, null);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

//        views = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            views.add(new CalendarView(context));
//        }

        setAdapter(new CalendarViewAdapter());
        setCurrentItem(PAGE_COUNT / 2);
        addOnPageChangeListener(new SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                CalendarView view = map.get(position);

            }
        });
    }

    private class CalendarViewAdapter extends PagerAdapter implements CalendarView.onCalendarClick {

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            map.remove(position);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CalendarView view = new CalendarView(container.getContext());
            view.setOnCalendarClick(this);
            map.put(position, view);
            container.addView(view);
            return view;
        }

        @Override
        public void OnCalendarClick(int y, int m, int d) {

        }
    }
}
