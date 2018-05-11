package com.atlas.crmapp.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.coffee.fragment.CoffeeProductListFragment;
import com.atlas.crmapp.common.commonadapter.BaseFragmentPagerAdapter;
import com.atlas.crmapp.model.ProductInfoJson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CoffeeCategoryTabAdapter extends BaseFragmentPagerAdapter {

    // private static final String[] TAB_TITLES = {"商品分类", "商品分类", "商品分类", "商品分类", "商品分类"};

    private Activity mActivity;
    private List<CoffeeProductListFragment> fragments;

    public CoffeeCategoryTabAdapter(FragmentManager fm, List<CoffeeProductListFragment> fragments, String[] TAB_TITLES) {
        super(fm, TAB_TITLES);
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    //刷新产品fragment 数据
    public void updateProductInfo(long productId, int num, long unitId) {
        for (int i=0; i<fragments.size(); i++) {
            CoffeeProductListFragment listFragment = fragments.get(i);
            ArrayList<ProductInfoJson> alProductInfo = listFragment.adapter.data;
            for (int j=0; j<alProductInfo.size(); j++) {
                if (alProductInfo.get(j).id == productId && alProductInfo.get(j).unitId == unitId) {
                    alProductInfo.get(j).number = num;
                    listFragment.adapter.notifyDataSetChanged();
                    return;
                }
            }

        }
    }

    public ProductInfoJson updateShowProduct(long productId, long unitId, int num) {
        for (int i=0; i<fragments.size(); i++) {
            CoffeeProductListFragment listFragment = fragments.get(i);
            ArrayList<ProductInfoJson> alProductInfo = listFragment.adapter.data;
            for (int j=0; j<alProductInfo.size(); j++) {
                if (alProductInfo.get(j).id == productId && alProductInfo.get(j).unitId == unitId) {
                    alProductInfo.get(j).number = num;
                    listFragment.adapter.notifyDataSetChanged();
                    return alProductInfo.get(j);
                }
            }

        }
        return null;
    }
}
