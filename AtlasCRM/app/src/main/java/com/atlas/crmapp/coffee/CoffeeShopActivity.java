package com.atlas.crmapp.coffee;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.CoffeeShopRvAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoffeeShopActivity extends BaseActivity {


    @BindView(R.id.rv_coffee_shop)
    RecyclerView mRvCoffeeShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_shop);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        CoffeeShopRvAdapter adapter = new CoffeeShopRvAdapter();
        mRvCoffeeShop.setHasFixedSize(true);
        mRvCoffeeShop.setLayoutManager(new LinearLayoutManager(this));
        mRvCoffeeShop.setAdapter(adapter);
    }

    @OnClick(R.id.tb_iv_back)
    void onBackClick() {
        onBackPressed();
    }


}
