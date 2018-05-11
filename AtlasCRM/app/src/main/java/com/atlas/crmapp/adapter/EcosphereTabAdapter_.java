package com.atlas.crmapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.common.commonadapter.BaseFragmentPagerAdapter;
import com.atlas.crmapp.ecosphere.fragments.ActivitiesFragment;
import com.atlas.crmapp.ecosphere.fragments.TalkFragment;
import com.atlas.crmapp.huanxin.ConversationListFragment;
import com.atlas.crmapp.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class EcosphereTabAdapter_ extends BaseFragmentPagerAdapter{

    private static final String[] TAB_TITLES = {ContextUtil.getUtil().getContext().getString(R.string.e1)
            , ContextUtil.getUtil().getContext().getString(R.string.e2)};

    public EcosphereTabAdapter_(FragmentManager fm) {
        super(fm, TAB_TITLES);
        initList();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    List<Fragment> list;
    private void initList(){
        list = new ArrayList<>();
        list.add(ActivitiesFragment.newInstance(""));
        list.add(new AATagFragment());
    }
}
