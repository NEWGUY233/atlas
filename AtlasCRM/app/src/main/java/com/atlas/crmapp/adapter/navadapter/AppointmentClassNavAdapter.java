package com.atlas.crmapp.adapter.navadapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.fitness.AppointmentClassFragment;

/**
 * Created by Alex on 2017/4/27.
 */

public class AppointmentClassNavAdapter implements FragmentNavigatorAdapter {

    @Override
    public Fragment onCreateFragment(int position) {
        return AppointmentClassFragment.newInstance();
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