package com.atlas.crmapp.workplace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.MyMeetingAdapter;
import com.atlas.crmapp.model.ResponseMyAppointmentJson;
import com.atlas.crmapp.model.bean.MyMeetingModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMeetingCalendarActivity extends BaseActivity {


    @BindView(R.id.meeting_recylclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tb_tv_title)
    TextView mTbTvTitle;
    @BindView(R.id.compactcalendar_view)
    CompactCalendarView mCompactcalendarView;
    @BindView(R.id.tv_data_title)
    TextView mTvDataTitle;
    @BindView(R.id.view_cal_title_bg)
    View mViewCalTitleBg;


    private List<MyMeetingModel> models;
    private SimpleDateFormat dateFormatForMonth;
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meeting_calendar);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        dateFormatForMonth = new SimpleDateFormat(getString(R.string.s90));
        mTbTvTitle.setText(R.string.s91);
        mTvDataTitle.setText(dateFormatForMonth.format(mCompactcalendarView.getFirstDayOfCurrentMonth()));


        // Calendar view setting
        mCompactcalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        mCompactcalendarView.setUseThreeLetterAbbreviation(false);
        mCompactcalendarView.setDayColumnNames(new String[]{getString(R.string.w17), getString(R.string.w11)
                , getString(R.string.w12), getString(R.string.w13), getString(R.string.w14), getString(R.string.w15), getString(R.string.w16)});
        mCompactcalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                //initData(dateClicked);

                loadData( dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mTvDataTitle.setText(dateFormatForMonth.format(mCompactcalendarView.getFirstDayOfCurrentMonth()));
            }
        });

        // TODO For display sample only
        loadEvents();
        loadEventsForYear(2017);
        mCompactcalendarView.invalidate();
        loadData(new Date());
    }

    private void loadEvents() {
        addEvents(-1, -1);
        addEvents(Calendar.DECEMBER, -1);
        addEvents(Calendar.AUGUST, -1);
    }

    private void loadEventsForYear(int year) {
        addEvents(Calendar.DECEMBER, year);
        addEvents(Calendar.AUGUST, year);
    }

    private void addEvents(int month, int year) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        for (int i = 0; i < 6; i++) {
            currentCalender.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalender.set(Calendar.MONTH, month);
            }
            if (year > -1) {
                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalender.set(Calendar.YEAR, year);
            }
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            List<Event> events = getEvents(timeInMillis, i);

            mCompactcalendarView.addEvents(events);
        }
    }

    /*
     * event color 已经在源码中被 hardcode 成 主题黄 此处是复制sample，颜色设置无效
     *
     */
    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if (day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @OnClick(R.id.tb_iv_back)
    public void onBackClick() {
        onBackPressed();
    }

    @OnClick({R.id.ib_pre, R.id.ib_next})
    public void onChangeMonthClick(View view) {
        switch (view.getId()) {
            case R.id.ib_pre:
                mCompactcalendarView.showPreviousMonth();
                break;
            case R.id.ib_next:
                mCompactcalendarView.showNextMonth();
                break;
        }
    }

    private MyMeetingAdapter adapter;
    private List<ResponseMyAppointmentJson.MyAppointment> bookings = new ArrayList<>();
    //加载数据，strDate，如2017-04-01
    private void loadData(final Date selectDate) {
        String strDate = DateUtil.dateToString(selectDate);


        BizDataRequest.requestMyAppointment(MyMeetingCalendarActivity.this, strDate, "", new BizDataRequest.OnResponseMyAppointmentJson() {
            @Override
            public void onSuccess(ResponseMyAppointmentJson responseMyAppointmentJson) {
                bookings.clear();
                bookings.addAll(responseMyAppointmentJson.getRows());
                recyclerView.setLayoutManager(new LinearLayoutManager(MyMeetingCalendarActivity.this));
                if(adapter == null){
                    adapter = new MyMeetingAdapter(MyMeetingCalendarActivity.this, bookings);
                    adapter.setSelectDate(selectDate);
                    recyclerView.setAdapter(adapter);
                }else{
                    adapter.setSelectDate(selectDate);
                    adapter.notifyDataSetChanged();
                }
                if(bookings.size() == 0){
//                    adapter.setEmptyView(R.layout.view_product_null, recyclerView);
                }
            }
            @Override
            public void onError(DcnException error) {
                Toast.makeText(MyMeetingCalendarActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
