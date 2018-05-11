package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.adapter.EcosphereTabAdapter_;
import com.atlas.crmapp.bean.Test;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ActiveAndTagActivity extends BaseStatusActivity {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;


    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_tag);

        initView();
    }

    private void initView(){
        EcosphereTabAdapter_ adapter = new EcosphereTabAdapter_(getSupportFragmentManager());
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);

        setFinishedView(R.id.iv_back);

    }
}
