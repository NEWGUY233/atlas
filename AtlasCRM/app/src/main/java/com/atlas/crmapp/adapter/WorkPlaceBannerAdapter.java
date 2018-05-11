package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/15
 *         Description :
 */

public class WorkPlaceBannerAdapter extends BaseRvAdapter<WorkPlaceBannerAdapter.ViewHolder> {

    private Context mContext;

    public WorkPlaceBannerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_banner_coffee_fragment;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivBg.setImageResource(R.drawable.product);
        holder.tvTitle.setText(R.string.text_88);
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.banner_iv_bg)
        ImageView ivBg;
        @BindView(R.id.banner_tv_title)
        TextView tvTitle;



        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
