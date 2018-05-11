package com.atlas.crmapp.adapter.navadapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.workplace.fragment.MeetingRoomFragment;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/15
 *         Description :
 */

public class MeetingRoomNavAdapter implements FragmentNavigatorAdapter {




    @Override
    public Fragment onCreateFragment(int position) {
        return MeetingRoomFragment.newInstance();
    }

    @Override
    public String getTag(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
