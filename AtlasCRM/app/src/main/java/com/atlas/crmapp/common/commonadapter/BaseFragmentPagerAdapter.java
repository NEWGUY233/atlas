package com.atlas.crmapp.common.commonadapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabTitle;


    public BaseFragmentPagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);
        mTabTitle = tabTitle;
    }

    @Override
    public int getCount() {
        return mTabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

}
