package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.usercenter.printfragment.PrintDYFragment;
import com.atlas.crmapp.usercenter.printfragment.PrintFYFragment;
import com.atlas.crmapp.usercenter.printfragment.PrintSMFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/2.
 */

public class MyPrintActivity extends BaseStatusActivity {
    @BindView(R.id.tab_bar)
    TabLayout tabBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public static final String PRINT = "PRINT";
    public static final String COPY = "COPY";
    public static final String SCAN = "SCAN";

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_print);
        ButterKnife.bind(this);

        initToolbar();
        setTitle(getString(R.string.user_center_print));

        mTabTitles.add(getString(R.string.user_center_print_dy));
        mTabTitles.add(getString(R.string.user_center_print_fy));
        mTabTitles.add(getString(R.string.user_center_print_sm));

        mFragments.add(new PrintDYFragment());
        mFragments.add(new PrintFYFragment());
        mFragments.add(new PrintSMFragment());

        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        tabBar.setupWithViewPager(viewpager);

    }
}
