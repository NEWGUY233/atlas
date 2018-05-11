package com.atlas.crmapp.usercenter.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CouponCenterFragmentAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.CouponModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by A.Developer on 2017/3/20.
 */

public class CouponCenterFragment extends BaseFragment {


    @BindView(R.id.coupon_center_recyclerView)
    RecyclerView recyclerView;

    private String mBizcode;

    private ArrayList<CouponModel> mCouponModelsList;

    public static CouponCenterFragment newInstance(String type) {
        CouponCenterFragment fragment = new CouponCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bizcode", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_center;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        prepareFragmentData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mBizcode = getArguments().getString("bizcode");
        } else {
            mBizcode = "";
        }
    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
            BizDataRequest.requestCouponsCenterWithToken(getHoldingActivity(),false, statusLayout, new BizDataRequest.OnCouponsCenterRequestResult() {
                @Override
                public void onSuccess(List<CouponModel> couponModelsList) {
                    if (mCouponModelsList == null) {
                        if (mBizcode.equals("")) {
                            mCouponModelsList = (ArrayList<CouponModel>) couponModelsList;
                        } else {
                            mCouponModelsList = new ArrayList<CouponModel>();
                            for (int i = 0; i < couponModelsList.size(); i++) {
                                if (couponModelsList.get(i).targetBizCode.equals(mBizcode)) {
                                    mCouponModelsList.add(couponModelsList.get(i));
                                }
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
                        CouponCenterFragmentAdapter adapter = new CouponCenterFragmentAdapter(getHoldingActivity(), mCouponModelsList);
                        recyclerView.setAdapter(adapter);
                        adapter.addFooterView(LayoutInflater.from(getHoldingActivity()).inflate(R.layout.view_top_bg, recyclerView, false));
                        if(mCouponModelsList.size() == 0){
                            adapter.setEmptyView(R.layout.view_product_null, recyclerView);
                        }
                    }
                }

                @Override
                public void onError(DcnException error) {
                    mCouponModelsList = new ArrayList<CouponModel>();
                }
            });

    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
