package com.atlas.crmapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ContractProductJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class RentWorkPlaceListAdapter extends BaseQuickAdapter<ContractProductJson,BaseViewHolder> {
    private Context context;

    public RentWorkPlaceListAdapter(Context context, List<ContractProductJson> data) {
        super(R.layout.item_rent_wp_rv_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ContractProductJson item) {
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_price, FormatCouponInfo.formatDoublePrice(item.price,  0))
                .setText(R.id.tv_sub_title, item.description);
        GlideUtils.loadCustomImageView(context, R.drawable.ic_product_thum, LoadImageUtils.loadSmallImage(item.thumbnail),(ImageView) helper.getView(R.id.shape_iv) );
    }
}
