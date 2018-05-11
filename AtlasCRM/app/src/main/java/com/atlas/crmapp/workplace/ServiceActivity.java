package com.atlas.crmapp.workplace;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ServiceCategoryTabAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ProductCategoryJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends BaseStatusActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    public String type;
    public ArrayList<ProductCategoryJson.RowsBean> rows_al = new ArrayList<ProductCategoryJson.RowsBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        setTitle(getString(R.string.reserve_server));
        setUmengPageTitle(getString(R.string.reserve_server));
        prepareActivityData();
    }

    //获取分类

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();


    BizDataRequest.requestProductCategory(this, "workplace", statusLayout, new BizDataRequest.OnRequestProductCategory() {
            @Override
            public void onSuccess(ProductCategoryJson productCategoryJson) {
                List<ProductCategoryJson.RowsBean> rlrows = productCategoryJson.getRows();
                rows_al.addAll(rlrows);
                String[] strings = new String[rows_al.size()];
                for (int i = 0; i < rows_al.size(); i++) {
                    strings[i] = rows_al.get(i).getName();
                }
                ServiceCategoryTabAdapter tabAdapter = new ServiceCategoryTabAdapter(getSupportFragmentManager(), strings,rows_al ,ServiceActivity.this);
                mViewpager.setAdapter(tabAdapter);
                mTabLayout.setupWithViewPager(mViewpager);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }
}
