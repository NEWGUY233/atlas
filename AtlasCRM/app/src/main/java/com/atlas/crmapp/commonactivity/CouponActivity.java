package com.atlas.crmapp.commonactivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.CouponTabAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        String[] tabsTitle = {getString(R.string.text_ky), getString(R.string.text_bky)};

        CouponTabAdapter tabAdapter = new CouponTabAdapter(getSupportFragmentManager(), tabsTitle);
        mViewpager.setAdapter(tabAdapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @OnClick(R.id.tb_iv_back)
    void onBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.tv_confirm)
    void onConfirmClick() {
        //TODO
    }
}
