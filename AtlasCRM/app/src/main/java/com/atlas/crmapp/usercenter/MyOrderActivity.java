package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.BusinesseModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.fragment.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MyOrderActivity extends BaseStatusActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.order_viewPager)
    ViewPager mViewPager;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static int TAB_MARGIN_DIP = 10;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        umengPageTitle = getString(R.string.t84);
        setTitle(umengPageTitle);
        prepareActivityData();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestOnGetOrderBusinesses(this, false, statusLayout, new BizDataRequest.OnResponseGetBusinesses() {
            @Override
            public void onSuccess(List<BusinesseModel> businesseModels) {
                initTabViewpage(businesseModels);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    //初始化 initTabViewpage
    private void initTabViewpage(List<BusinesseModel> businesseModels){
        if(businesseModels == null || businesseModels.size() == 0 ){
            statusLayout.showEmpty();
            return;
        }
        mTabTitles.add(getString(R.string.index_top_all));
        mFragments.add(MyOrderFragment.newInstance(""));
        for(BusinesseModel businesseModel : businesseModels){
            mTabTitles.add(businesseModel.getName());
            mFragments.add(MyOrderFragment.newInstance(businesseModel.getCode()));
        }
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        MyCouponActivity.setIndicator(this, mTablayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999)
        {
           /* if (resultCode == 999) {
                mTabTitles.clear();
                mFragments.clear();
                prepareActivityData();
            }*/
        }
    }


}

