package com.atlas.crmapp.fitness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.FitnessAppointmentAdapter;
import com.atlas.crmapp.adapter.MeetingScheduleAdapter;
import com.atlas.crmapp.annotation.StateVariable;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.model.LessonsListJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.DateScheduleView;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by Alex on 2017/4/27.
 */

public class AppointmentClassFragment extends BaseFragment {


    /*@BindView(R.id.rv_date)
    RecyclerView mRvDate;*/

    @BindView(R.id.v_date_schedule)
    DateScheduleView dateScheduleView;
    @BindView(R.id.tv_top_month)
    TextView tvTopMonth;

    @BindView(R.id.rv_detail)
    RecyclerView mRvView;

    private FitnessAppointmentAdapter adapter;
    private boolean showProgreeDialog;

    private String date;

    public static AppointmentClassFragment fragment;

    public static AppointmentClassFragment newInstance() {
        AppointmentClassFragment fragment = new AppointmentClassFragment();
        return fragment;
    }

    @StateVariable
    ArrayList<LessonJson> lessons = new ArrayList<LessonJson>();

    @Override
    protected int setTopBar() {
        return R.layout.top_view_date_schedule;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_appointment;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        fragment = this;
        tvTopMonth.setText(DateUtil.dateToString(new Date(), "yyyy/MM"));
        dateScheduleView.updateView(new MeetingScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String date, int position, String formatDate) {
                AppointmentClassFragment.this.date = date;
                AppointmentClassFragment.this.prepareActivityData(date);
                tvTopMonth.setText(formatDate);
            }
        }, 7);
        date = dateScheduleView.getCurrentDate();
        mRvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvView.addItemDecoration(new RecycleViewListViewDivider(getActivity(), LinearLayout.HORIZONTAL, UiUtil.dipToPx(getActivity(), 1), Color.parseColor("#ebebeb")));
        adapter = new FitnessAppointmentAdapter(getHoldingActivity(), lessons);
        mRvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvView.setAdapter(adapter);
        adapter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(context, mRvView));
        prepareActivityData(date);

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData(date);
    }

    public void prepareActivityData(String date) {

        BizDataRequest.requestGetLessons(getActivity(), 0, 20, getGlobalParams().getFitnessId(), DateUtil.parse(date + " 00:00:00").getTime(), DateUtil.parse(date + " 23:59:59").getTime(), false, statusLayout, new BizDataRequest.OnLessonsRequestResult() {
            @Override
            public void onSuccess(LessonsListJson lessonsJsons) {
                lessons.clear();
                lessons.addAll(lessonsJsons.rows);
                adapter.notifyDataSetChanged();
                if(lessons.size() == 0){
                    adapter.setEmptyView(R.layout.view_product_null, mRvView);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        fragment = null;
        super.onDestroy();
    }


}