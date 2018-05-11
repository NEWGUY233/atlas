package com.atlas.crmapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Harry on 2017/5/26.
 */

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter{

    private ArrayList<String> titles;
    private FragmentManager fragmentManager;

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, ArrayList<String> titles) {
        super(fm);
        fragmentManager = fm;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentManager.getFragments().get(position);
    }

    @Override
    public int getCount() {
        return fragmentManager.getFragments().size();
    }
}
