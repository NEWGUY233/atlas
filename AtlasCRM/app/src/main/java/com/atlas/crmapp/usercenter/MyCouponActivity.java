package com.atlas.crmapp.usercenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.usercenter.fragment.MyCouponFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;

public class MyCouponActivity extends BaseStatusActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.coupon_viewPager)
    ViewPager mViewPager;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static int TAB_MARGIN_DIP = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        umengPageTitle = getString(R.string.my_coupon);
        setTitle(umengPageTitle);
        initData();
    }

    private void initData() {
        mTabTitles.add(getString(R.string.t71));
        mTabTitles.add(getString(R.string.t72));
        mTabTitles.add(getString(R.string.t73));
        mFragments.add(MyCouponFragment.newInstance(0));
        mFragments.add(MyCouponFragment.newInstance(1));
        mFragments.add(MyCouponFragment.newInstance(2));

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        mTablayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        setIndicator(this, mTablayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
    }

    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

}
