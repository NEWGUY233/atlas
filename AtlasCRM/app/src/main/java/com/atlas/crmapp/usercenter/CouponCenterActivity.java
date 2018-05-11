package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.BusinesseModel;
import com.atlas.crmapp.usercenter.fragment.CouponCenterFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CouponCenterActivity extends BaseStatusActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.coupon_center_viewPager)
    ViewPager mViewPager;

    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static int TAB_MARGIN_DIP = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_center);
        umengPageTitle = getString(R.string.t55);
        setTitle(umengPageTitle);
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        initTabViewpage(GlobalParams.getInstance().getBusinesses());
      /*  BizDataRequest.requestOnGetCommonBusinesses(this, false, statusLayout, new BizDataRequest.OnResponseGetBusinesses() {
            @Override
            public void onSuccess(List<BusinesseModel> businesseModels) {
                initTabViewpage(businesseModels);
            }
            @Override
            public void onError(DcnException error) {

            }
        });*/
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    //初始化 initTabViewpage
    private void initTabViewpage(List<BusinesseModel> businesseModels){

        if(businesseModels == null || businesseModels.size() == 0 ){
            statusLayout.showEmpty();
            return;
        }
        mTabTitles.add(getString(R.string.index_top_all));
        mFragments.add(CouponCenterFragment.newInstance(""));
        for(BusinesseModel businesseModel : businesseModels){
            mTabTitles.add(businesseModel.getName());
            mFragments.add(CouponCenterFragment.newInstance(String.valueOf(businesseModel.getCode())));
        }
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        MyCouponActivity.setIndicator(this, mTablayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            finish();
        }
        return false;
    }
}
