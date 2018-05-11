package com.atlas.crmapp.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.BillingItemJson;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 2017/3/21.
 */

public class CorporationBillAdapter extends BaseAdapter{

    private List<BillingItemJson> billingItems;

    public CorporationBillAdapter(List<BillingItemJson> list) {
        billingItems = list;
    }

    @Override
    public int getCount() {
        return billingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<BillingItemJson> billDataList) {
        this.billingItems = billDataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CorporationBillAdapter.ItemViewHolder holder;
        final BillingItemJson bill = billingItems.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_corporation_bill, null);
            holder = new CorporationBillAdapter.ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CorporationBillAdapter.ItemViewHolder) convertView.getTag();
        }

        if(TextUtils.isEmpty(bill.userName)){
            holder.mTvName.setText(bill.refNo);
        }else{
            holder.mTvName.setText(bill.userName);
        }
        holder.mTvContent.setText(bill.type);
        holder.mTvDate.setText(DateUtil.times(bill.billDate, "yyyy.MM.dd HH:mm"));
        holder.mTvPrice.setText(FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(bill.amount, 2));

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class ItemViewHolder {

        @BindView(R.id.textViewName)
        TextView mTvName;
        @BindView(R.id.textViewContent)
        TextView mTvContent;
        @BindView(R.id.textViewDate)
        TextView mTvDate;
        @BindView(R.id.textViewPrice)
        TextView mTvPrice;

        public ItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
