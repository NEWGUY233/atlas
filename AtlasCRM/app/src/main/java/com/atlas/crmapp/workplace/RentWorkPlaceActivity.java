package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.RentWorkPlaceListAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ContractProductJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.SaleContractProductsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RentWorkPlaceActivity extends BaseStatusActivity {

    @BindView(R.id.rv_rent_work_place)
    RecyclerView mRvRentWorkPlace;
    ImageView ivHeader;

    private RentWorkPlaceListAdapter adapter;
    int page = 0;
    int pagesize = 100;
    ArrayList<ContractProductJson> rows = new ArrayList<ContractProductJson>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_work_place);
        mRvRentWorkPlace.setLayoutManager(new LinearLayoutManager(this));
        mRvRentWorkPlace.addItemDecoration(new RecycleViewListViewDivider(this, LinearLayout.HORIZONTAL, UiUtil.dipToPx(RentWorkPlaceActivity.this, 1), getResources().getColor(R.color.divider_gray)));
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.vip_product));
        if (adapter == null) {
            adapter = new RentWorkPlaceListAdapter(RentWorkPlaceActivity.this, rows);
            mRvRentWorkPlace.setAdapter(adapter);
        }
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_rent_wp_rv_header,null,false);
        ivHeader = (ImageView) headerView.findViewById(R.id.iv_header_img);
        adapter.addHeaderView(headerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(RentWorkPlaceActivity.this, RentWorkPlaceDetailActivity.class).putExtra("id",rows.get(position).id));
            }
        });
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestSaleContractProducts(this, page, pagesize, getGlobalParams().getWorkplaceId(), statusLayout, new BizDataRequest.OnSaleContractProducts() {
                @Override
                public void onSuccess(SaleContractProductsJson saleContractProductsJson) {
                    List<ContractProductJson> r = saleContractProductsJson.rows;
                    rows.addAll(r);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(DcnException error) {

                }
            });


        BizDataRequest.requestResource(RentWorkPlaceActivity.this, String.valueOf(getGlobalParams().getAtlasId()), "app/workplace/ad/contract", new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if (resourceJson.rows != null && resourceJson.rows.size() > 0) {
                    List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(0).resourceMedias;
                    if (resList != null && resList.size() > 0) {
                        GlideUtils.loadCustomImageView(RentWorkPlaceActivity.this, R.drawable.product, LoadImageUtils.loadMiddleImage(resList.get(0).url), ivHeader);
                    }
                }
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
