package com.atlas.crmapp.workplace;

import android.os.Bundle;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.adapter.navadapter.MeetingRoomNavAdapter;

import butterknife.ButterKnife;

public class MeetingRoomActivity extends NavFragmentsActivity {


    @Override
    protected FragmentNavigatorAdapter getNavAdapter() {
        return new MeetingRoomNavAdapter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.meeting_room_fl_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengPageTitle = getString(R.string.umeng_meeting_room_list);
        setContentView(R.layout.activity_meeting_room);
        ButterKnife.bind(this);
        initView();
    }



    private void initView() {
        mFragmentNavigator.setDefaultPosition(0);
        mFragmentNavigator.showFragment(0);
    }

}
