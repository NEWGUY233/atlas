package com.atlas.crmapp.coffee.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CouponListRvAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CouponListFragment extends BaseFragment {


    @BindView(R.id.rv_coffee_product)
    RecyclerView mRv;

    public static CouponListFragment newInstance() {
        return new CouponListFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coffee_product_list;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        mRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getHoldingActivity());
        mRv.setLayoutManager(layoutManager);
        CouponListRvAdapter adapter = new CouponListRvAdapter();
        mRv.setAdapter(adapter);
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }


}
