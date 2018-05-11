package com.atlas.crmapp.workplace.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ServiceListAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.OnlineSaleProductsJson;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Alex on 2017/4/21.
 */

public class ServiceProductListFragment extends BaseFragment {
    ServiceListAdapter adapter;
    List<ProductInfoJson> proinfo;
    @BindView(R.id.rv_service_product)
    RecyclerView mRvCoffeeProduct;

    long categoryId;
    long mUnitId;
    public static ServiceProductListFragment fragment;

    int page = 0;
    int pagesize = 100;
    public ArrayList<ProductInfoJson> al_proinfojson = new ArrayList<>();

    public ServiceProductListFragment() {

    }
    @SuppressLint("ValidFragment")
    public ServiceProductListFragment(long id) {
        categoryId = id;
    }

    public static ServiceProductListFragment newInstance(long id) {
        return new ServiceProductListFragment(id);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_product_list;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment = null;
        
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        fragment = this;
        adapter = new ServiceListAdapter(getHoldingActivity(), al_proinfojson);
        mRvCoffeeProduct.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getHoldingActivity());
        mRvCoffeeProduct.setLayoutManager(layoutManager);
        mRvCoffeeProduct.setAdapter(adapter);
        //mRvCoffeeProduct.addItemDecoration(new RecycleViewListViewDivider(getActivity(), LinearLayout.HORIZONTAL, UiUtil.dipToPx(getActivity(),1 ), Color.parseColor("#ebebeb")));
        adapter.addHeaderView(GetCommonObjectUtils.getRvBgDivideItem(getActivity(), mRvCoffeeProduct));
        prepareFragmentData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        if (proinfo == null) {
            BizDataRequest.requestOnlineSaleProducts(getHoldingActivity(), page, pagesize, getGlobalParams().getWorkplaceId(), categoryId, statusLayout, new BizDataRequest.OnOnlineSaleProducts() {
                @Override
                public void onSuccess(OnlineSaleProductsJson onlineSaleProductsJson) {
                    if (page <= 0) {
                        proinfo = onlineSaleProductsJson.rows;
                        page++;
                        al_proinfojson.addAll(proinfo);
                    }
                    adapter.notifyDataSetChanged();
                    if(al_proinfojson.size() ==0){
                        adapter.setEmptyView(R.layout.view_product_null, mRvCoffeeProduct);
                    }
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }
}