package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.common.commonadapter.BaseFragmentPagerAdapter;
import com.atlas.crmapp.model.ProductCategoryJson;
import com.atlas.crmapp.workplace.fragment.ServiceProductListFragment;

import java.util.ArrayList;

/**
 * Created by Alex on 2017/4/21.
 */

public class ServiceCategoryTabAdapter extends BaseFragmentPagerAdapter {

    // private static final String[] TAB_TITLES = {"商品分类", "商品分类", "商品分类", "商品分类", "商品分类"};
    private Context context;
    private ArrayList<ProductCategoryJson.RowsBean> rows;

    public ServiceCategoryTabAdapter(FragmentManager fm, String[] tabTitle, ArrayList<ProductCategoryJson.RowsBean> rows, Context context) {
        super(fm, tabTitle);
        this.context = context;
        this.rows = rows;
    }

    @Override
    public Fragment getItem(int position) {
        long id = rows.get(position).getId();
        return ServiceProductListFragment.newInstance(id);
    }
}