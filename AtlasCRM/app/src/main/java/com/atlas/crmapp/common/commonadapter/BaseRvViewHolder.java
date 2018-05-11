package com.atlas.crmapp.common.commonadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/17
 *         Description :
 */

public class BaseRvViewHolder extends RecyclerView.ViewHolder {



    public BaseRvViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
