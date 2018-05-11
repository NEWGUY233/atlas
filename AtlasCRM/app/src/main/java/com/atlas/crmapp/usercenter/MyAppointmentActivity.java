package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.usercenter.fragment.MyAppointmentFragment;
import com.atlas.crmapp.workplace.MyMeetingCalendarActivity;

import java.util.ArrayList;

import butterknife.BindView;


public class MyAppointmentActivity extends BaseStatusActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.appointment_viewPager)
    ViewPager mViewPager;

    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        umengPageTitle = getString(R.string.my_appointment);
        setTitle(umengPageTitle);

        setTopRightButton("", R.drawable.ic_appoint_right, new OnClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(MyAppointmentActivity.this, MyMeetingCalendarActivity.class));
            }
        });

        mTabTitles.add(getString(R.string.t66));
        mTabTitles.add(getString(R.string.t67));
        mFragments.add(MyAppointmentFragment.newInstance(MyAppointmentFragment.APPOINTMENT_STATE_ONGOING));
        mFragments.add(MyAppointmentFragment.newInstance(MyAppointmentFragment.APPOINTMENT_STATE_COMPLETE));
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        mTablayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
