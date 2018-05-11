package com.atlas.crmapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by A.Developer on 2017/3/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
