package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.util.FormatCouponInfo;

import java.util.List;

/**
 * Created by hoda on 2017/8/3.
 */

public class OrderConfirmCouponAdapter extends BaseAdapter{

    private List<UseableCouponsModel> couponsModels;
    private Context context;
    private OrderConfirmActivity confirmActivity;

    public OrderConfirmCouponAdapter(Context context, List<UseableCouponsModel> couponsModels) {
        this.couponsModels = couponsModels;
        this.context = context;
        confirmActivity = (OrderConfirmActivity) context;
    }

    @Override
    public int getCount() {
        return couponsModels.size();
    }

    @Override
    public Object getItem(int i) {
        return couponsModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_find_coupon, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCouponTitle = (TextView) convertView.findViewById(R.id.tv_coupon_name);
            viewHolder.tvCouponPrice = (TextView) convertView.findViewById(R.id.tv_coupon_price);
            viewHolder.tvCouponDate = (TextView) convertView.findViewById(R.id.tv_expired_date);
            viewHolder.tvCouponRemark = (TextView) convertView.findViewById(R.id.tv_coupon_remark);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UseableCouponsModel useableCouponsModel = couponsModels.get(position);

        viewHolder.tvCouponTitle.setText(useableCouponsModel.coupon.coupon.name);
        final ViewHolder finalViewHolder = viewHolder;
        FormatCouponInfo.formatCouponInfo(useableCouponsModel.coupon.coupon.type,
                useableCouponsModel.coupon.coupon.value1,
                useableCouponsModel.coupon.coupon.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
                    @Override
                    public void onFormatCouponInfoDone(String price, String remark) {
                        finalViewHolder.tvCouponPrice.setText(price);
                        finalViewHolder.tvCouponRemark.setText(remark);
                    }
                });

        viewHolder.tvCouponDate.setText(FormatCouponInfo.formatVaildDate(useableCouponsModel.bind.validStart, useableCouponsModel.bind.validEnd));
        ImageView ivRight = (ImageView) convertView.findViewById(R.id.cv_right_img);
        ivRight.setImageResource(R.drawable.iv_coupon_user);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener != null){
                    onClickListener.onClickListener(position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        ImageView ivCouponIcon;
        TextView tvCouponTitle;
        TextView tvCouponPrice;
        TextView tvCouponDate;
        TextView tvCouponRemark;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }



}
