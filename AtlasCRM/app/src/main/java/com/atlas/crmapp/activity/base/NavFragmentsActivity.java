package com.atlas.crmapp.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/14
 *         Description :
 */

public abstract class NavFragmentsActivity extends BaseActivity {


    protected FragmentNavigator mFragmentNavigator;

    protected abstract FragmentNavigatorAdapter getNavAdapter();

    protected abstract int getFragmentContainerId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), getNavAdapter(), getFragmentContainerId());
        mFragmentNavigator.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentNavigator.onSaveInstanceState(outState);
    }
}
