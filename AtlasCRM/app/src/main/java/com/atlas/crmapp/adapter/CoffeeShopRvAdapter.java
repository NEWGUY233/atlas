package com.atlas.crmapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/18
 *         Description :
 */

public class CoffeeShopRvAdapter extends BaseRvAdapter{




    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_coffee_shop;
    }

    @Override
    protected BaseRvViewHolder getViewHolder(View root, int viewType) {
        return new BaseRvViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
