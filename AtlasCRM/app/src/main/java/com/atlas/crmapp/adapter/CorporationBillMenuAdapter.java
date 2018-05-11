package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.BillingJson;
import com.atlas.crmapp.usercenter.CorporationBillMonthActivity;
import com.atlas.crmapp.util.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/23.
 */

public class CorporationBillMenuAdapter extends BaseQuickAdapter<BillingJson, BaseViewHolder> {

    private Context context;
    private List<BillingJson> data;
    private long contractId;

    public CorporationBillMenuAdapter(Context context, List<BillingJson> data, long contractId) {
        super(R.layout.item_bill_menu, data);
        this.context = context;
        this.data = data;
        this.contractId = contractId;
    }

    @Override
    protected void convert(BaseViewHolder helper, final BillingJson item) {
        helper.setText(R.id.tv_date, DateUtil.times(item.billingDate, context.getString(R.string.text_72)));
        helper.getView(R.id.separatorView).setVisibility(View.GONE);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CorporationBillMonthActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("name", DateUtil.times(item.billingDate, context.getString(R.string.text_72)) + context.getString(R.string.text_73));
                intent.putExtra("contractId", contractId);
                context.startActivity(intent);
            }
        });
    }
}