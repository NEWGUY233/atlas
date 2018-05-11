package com.atlas.crmapp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.TransactionsJson;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/18.
 */

public class RechargeAdapter extends BaseQuickAdapter<TransactionsJson ,BaseViewHolder> {

    private Context context;
    private List<TransactionsJson> data;
    public RechargeAdapter(Context context , List<TransactionsJson> data) {
        super(R.layout.item_recharge_record, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, TransactionsJson item) {
        String price = FormatCouponInfo.formatDoublePrice(item.getAmount(),2);
        if( !price.contains("-") && item.getAmount() > 0){
            price = "+" + price;
        }

        String bizName = item.getBizName();
        if(StringUtils.isEmpty(bizName)){
            bizName = "";
        }
        helper.setText(R.id.tv_type, FormatCouponInfo.getTransactionsType(item.getType()))
                .setText(R.id.tv_date, DateUtil.formatTime(item.getCreateTime(),"yyyy-MM-dd HH:mm:ss") + " " + bizName)
                .setText(R.id.tv_price,  price + context.getString(R.string.text_yuan));
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.setTextColor(FormatCouponInfo.getTransactionsTypeTextColor(context, item.getType()));

    }
}