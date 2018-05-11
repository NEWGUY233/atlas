package com.atlas.crmapp.usercenter.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CorporationCouponFragmentAdapter;
import com.atlas.crmapp.commonactivity.CouponDetailActivity;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by Harry on 2017/5/26.
 */

@SuppressLint("ValidFragment")
public class CorporationCouponFragment extends BaseFragment {

    private Unbinder unbinder;

    @BindView(R.id.corporation_coupon_recyclerView)
    RecyclerView recyclerView;

    private CorporationCouponFragmentAdapter adapter;
    private List<UseableCouponsModel> mCouponList;
    private int index;
    private static String KEY_COUPON_LIST = "KEY_COUPON_LIST";
    private static String KEY_INDEX = "KEY_INDEX";

    public CorporationCouponFragment(){

    }

    public static CorporationCouponFragment newInstance(List<UseableCouponsModel> couponList, int index) {
        CorporationCouponFragment fragment = new CorporationCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_COUPON_LIST, (Serializable) couponList);
        bundle.putInt(KEY_INDEX, index);
        fragment.setArguments(bundle);
        if(couponList != null){
            int size = couponList.size();
            Logger.d("size : " + size);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBusFactory.getBus().register(this);

        Bundle bundle = getArguments();
        if(bundle != null){
            mCouponList = (List<UseableCouponsModel>) bundle.get(KEY_COUPON_LIST);
            index = (int) bundle.get(KEY_INDEX);
            if(mCouponList != null){
                int size = mCouponList.size();
                Logger.d("size mCouponList : " + size);
            }
        }
        Logger.d("onCreate ---");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_corporation_coupon;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareFragmentData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        if (mCouponList != null && mCouponList.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
            if (adapter == null) {
                adapter = new CorporationCouponFragmentAdapter(getHoldingActivity(), mCouponList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setNewData(mCouponList);
                adapter.notifyDataSetChanged();
            }
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    UseableCouponsModel item = mCouponList.get(position);
                    CouponDetailActivity.newInstance(getActivity(), item, index);

                }
            });

        }else{
            statusLayout.showEmpty();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Subscribe
    public void onStartOrderFinishThis(ResponseOpenOrderJson openOrderJson){
        if(openOrderJson!= null){
            prepareFragmentData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }
}
