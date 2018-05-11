package com.atlas.crmapp.activity.index;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.activity.index.fragment.communication.CommunicationFragment;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.livingspace.LivingSpaceFragment;
import com.atlas.crmapp.activity.index.fragment.workplace.WorkPlaceFragment;
import com.atlas.crmapp.bean.FragmentBean;
import com.atlas.crmapp.usercenter.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class IndexFragmentAdapter implements FragmentNavigatorAdapter {
    List<FragmentBean> list;

    public IndexFragmentAdapter(){
        initList();
    }

    @Override
    public Fragment onCreateFragment(int i) {
        return list.get(i).getFragment();
    }

    @Override
    public String getTag(int i) {
        return list.get(i).getName();
    }

    @Override
    public int getCount() {
        return list.size();
    }


    private void initList(){
        list = new ArrayList<>();
        FragmentBean bean;

        bean = new FragmentBean();
        bean.setFragment(new WorkPlaceFragment());
        bean.setName("WorkPlaceFragment");
        list.add(bean);

        bean = new FragmentBean();
        bean.setFragment(new LivingSpaceFragment());
        bean.setName("LivingSpaceFragment");
        list.add(bean);

        bean = new FragmentBean();
        bean.setFragment(new IndexFragment());
        bean.setName("IndexFragment");
        list.add(bean);

        bean = new FragmentBean();
        bean.setFragment(new CommunicationFragment());
        bean.setName("CommunicationFragment");
        list.add(bean);

        bean = new FragmentBean();
        bean.setFragment(MyFragment.newInstance("", ""));
        bean.setName("MineFragment");
        list.add(bean);
    }
}
