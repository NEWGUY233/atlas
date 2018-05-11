package com.atlas.crmapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseFragmentPagerAdapter;
import com.atlas.crmapp.ecosphere.fragments.ActivitiesFragment;
import com.atlas.crmapp.ecosphere.fragments.TalkFragment;
import com.atlas.crmapp.huanxin.ConversationListFragment;
import com.atlas.crmapp.util.ContextUtil;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class EcosphereTabAdapter extends BaseFragmentPagerAdapter{

    private static final String[] TAB_TITLES = {ContextUtil.getUtil().getContext().getString(R.string.e1)
            , ContextUtil.getUtil().getContext().getString(R.string.e2)
            , ContextUtil.getUtil().getContext().getString(R.string.e3)};

    public EcosphereTabAdapter(FragmentManager fm) {
        super(fm, TAB_TITLES);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return ActivitiesFragment.newInstance("");
            case 1:
                return TalkFragment.newInstance();
            case 2:
                return ConversationListFragment.newInstance();
        }
        return null;
    }
}
