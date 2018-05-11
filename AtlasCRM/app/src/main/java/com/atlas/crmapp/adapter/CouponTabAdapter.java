package com.atlas.crmapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.coffee.fragment.CouponListFragment;
import com.atlas.crmapp.common.commonadapter.BaseFragmentPagerAdapter;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CouponTabAdapter extends BaseFragmentPagerAdapter {

    public CouponTabAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm, tabTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return CouponListFragment.newInstance();
    }
}
