package com.atlas.crmapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.CoffeeDetailActivity;
import com.atlas.crmapp.coffee.CoffeeListActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.model.SKUJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import tangxiaolv.com.library.EffectiveShapeView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CoffeeProductRvAdapter extends BaseRvAdapter<CoffeeProductRvAdapter.ViewHolder> {
    public ArrayList<ProductInfoJson> data;
    Activity mActivity;
    private String type;

    public CoffeeProductRvAdapter(Activity activity, ArrayList<ProductInfoJson> data, String type) {
        this.data = data;
        this.mActivity = activity;
        this.type = type;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_coffee_product;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductInfoJson map = data.get(position);
        holder.tv_name.setText(map.name);
        List<SKUJson> skuJsons = map.productSkus;
        SKUJson sj = null;
        for(SKUJson skuJson : skuJsons){
            if(skuJson.defaultFlag){
                sj = skuJson;
                break;
            }
        }

        if(sj == null){
            sj = skuJsons.get(0);
        }
        String thumb = sj.thumbnail;
        GlideUtils.loadCustomImageView(mActivity, R.drawable.ic_product_thum, LoadImageUtils.loadSmallImage(thumb), holder.shape_iv);
        holder.tv_price.setText(FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(sj.onlinePrice, 2));
        if(TextUtils.isEmpty(map.description)){
            holder.tvDesc.setVisibility(View.GONE);
        }else{
            holder.tvDesc.setVisibility(View.VISIBLE);
            holder.tvDesc.setText(map.description);
        }

        if (map.number == 0) {
            holder.iv_sub.setVisibility(View.GONE);
            holder.tv_item_count.setVisibility(View.GONE);
            holder.tv_item_count.setText(map.number + "");
        } else {
            holder.iv_sub.setVisibility(View.VISIBLE);
            holder.tv_item_count.setText(map.number + "");
            holder.tv_item_count.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = map.id;
                mActivity.startActivityForResult(new Intent(mActivity, CoffeeDetailActivity.class)
                        .putExtra("productId", map.id)
                        .putExtra("unitId", ((CoffeeListActivity)mActivity).unitId)
                        .putExtra("bizCode", ((CoffeeListActivity)mActivity).type)
                        .putExtra("fromList", true), 1000);
            }
        });
        holder.iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.number--;
                holder.tv_item_count.setText(map.number + "");
                if (map.number <= 0) {
                    holder.iv_sub.setVisibility(View.GONE);
                    holder.tv_item_count.setVisibility(View.GONE);
                }
                ((CoffeeListActivity)mActivity).getIv_sub(map);
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.number++;
                holder.tv_item_count.setText(map.number + "");
                holder.iv_sub.setVisibility(View.VISIBLE);
                holder.tv_item_count.setVisibility(View.VISIBLE);
                ((CoffeeListActivity)mActivity).getIv_add(map);
                int[] loc = new int[2];
                v.getLocationInWindow(loc);
                ((CoffeeListActivity)mActivity).playAnimation(loc);
            }
        });
        if(Constants.BIZ_CODE.COFFEE.equalsIgnoreCase(type)){
            holder.iv_add.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends BaseRvViewHolder {
        tangxiaolv.com.library.EffectiveShapeView shape_iv;
        TextView tv_name, tv_price, tv_unit, tv_item_count, tvDesc;

        ImageView iv_sub, iv_add;

        public ViewHolder(View itemView) {
            super(itemView);
            shape_iv = (EffectiveShapeView) itemView.findViewById(R.id.shape_iv);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
            tv_item_count = (TextView) itemView.findViewById(R.id.tv_item_count);
            iv_sub = (ImageView) itemView.findViewById(R.id.iv_sub);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }
}
