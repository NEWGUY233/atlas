package com.atlas.crmapp.usercenter.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyOrderFragmentAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by A.Developer on 2017/3/19.
 */

public class MyOrderFragment extends BaseFragment {

    private List<ResponseOpenOrderJson> mResponseMyOrderJson = new ArrayList<>();
    private String bizCode;

    RecyclerView recyclerView;

    @BindView(R.id.ll_main)
    RelativeLayout llMain;

    @BindView(R.id.springview)
    SpringView springView;

    private MyOrderFragmentAdapter adapter;
    private RefreshFootView refreshFootView;
    private long lastId = 0;

    public static MyOrderFragment newInstance(String type) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bizcode", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
        EventBusFactory.getBus().register(this);
        if (getArguments() != null) {
            bizCode = getArguments().getString("bizcode");
        } else {
            bizCode = "";
        }
    }

    @Subscribe
    public void refreshOrderList(Event.EventObject orderComplete){
        if(Constants.EventType.ORDER_COMPLETE.equals(orderComplete.type)){
            lastId = 0;
            mResponseMyOrderJson.clear();
            prepareFragmentData();
        }
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflateView.findViewById(R.id.my_order_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
        adapter = new MyOrderFragmentAdapter(getHoldingActivity(), mResponseMyOrderJson);
        recyclerView.setAdapter(adapter);
        prepareFragmentData();


        refreshFootView = new RefreshFootView(getActivity());

        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                prepareFragmentData();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        });
    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        BizDataRequest.requestMyOrder(getActivity(), bizCode, lastId,  statusLayout, new BizDataRequest.OnResponseMyOrderJson() {
                @Override
                public void onSuccess(List<ResponseOpenOrderJson> responseMyOrderJson) {
                    mResponseMyOrderJson.addAll(responseMyOrderJson);
                    if(responseMyOrderJson != null && responseMyOrderJson.size() > 0){
                        lastId = responseMyOrderJson.get(responseMyOrderJson.size() -1).getId();
                    } else{
                        if(refreshFootView!= null){
                            refreshFootView.setLoadingFinish();
                        }
                    }
                    try {
                        if(mResponseMyOrderJson.size() == 0){
                            llMain.removeAllViews();
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_product_null, llMain);
                            llMain.addView(view);
                        }
                    } catch (Exception e) {
                        Log.e("Debug", "exception:"+e.getMessage());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(DcnException error) {
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
        EventBusFactory.getBus().unregister(this);
    }
}
