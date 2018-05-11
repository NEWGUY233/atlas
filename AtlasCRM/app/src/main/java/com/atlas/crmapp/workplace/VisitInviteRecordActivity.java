package com.atlas.crmapp.workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.workplace.fragment.VisitInviteRecordFragment;

import java.util.ArrayList;

import butterknife.BindView;


public class VisitInviteRecordActivity extends BaseStatusActivity {

    @BindView(R.id.vp_visit_record_lock)
    ViewPager vpDoorLock;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;





    private ArrayList<String> tabList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_invite_record);
        tabList.add(getString(R.string.visit_not));
        tabList.add(getString(R.string.visit_complete));
        tabList.add(getString(R.string.visit_cancel));
        fragmentList.add(VisitInviteRecordFragment.newInstance(this, 0 ));
        fragmentList.add(VisitInviteRecordFragment.newInstance(this, 1 ));
        fragmentList.add(VisitInviteRecordFragment.newInstance(this, 2 ));
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, tabList);
        vpDoorLock.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, tabList));
        tabLayout.setupWithViewPager(vpDoorLock);
        vpDoorLock.setOffscreenPageLimit(1);
        setTitle(getString(R.string.visit_record));




    }



    public static void newInstance(Activity activity){
        activity.startActivity(new Intent(activity, VisitInviteRecordActivity.class));
    }
}
