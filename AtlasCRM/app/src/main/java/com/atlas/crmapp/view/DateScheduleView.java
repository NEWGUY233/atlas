package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MeetingScheduleAdapter;
import com.atlas.crmapp.model.DateModl;
import com.atlas.crmapp.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by hoda on 2017/7/25.
 */

public class DateScheduleView extends RelativeLayout {
    private Context context;
    private RecyclerView mRvDate;
    private MeetingScheduleAdapter dateAdapter;
    private String date;


    public DateScheduleView(Context context) {
        super(context);
        initView(context);
    }

    public DateScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DateScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateScheduleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_date_schedule, this, true);
        mRvDate = (RecyclerView) findViewById(R.id.rv_date);
    }

    public void updateView(MeetingScheduleAdapter.OnItemClickListener onItemClickListener, int showSelectDatSum){
        mRvDate.setHasFixedSize(true);
        mRvDate.setItemAnimator(new DefaultItemAnimator());
        mRvDate.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<DateModl> al_date = DateUtil.test(showSelectDatSum);
        dateAdapter = new MeetingScheduleAdapter(context, mRvDate, al_date);
        mRvDate.setAdapter(dateAdapter);
        date = al_date.get(0).getYeardate();
        dateAdapter.setItemClickListener(onItemClickListener);

    }

    public  MeetingScheduleAdapter getMeetingScheduleAdapter(){
        return  dateAdapter;
    }

    public String getCurrentDate(){
        return  date;
    }
}
