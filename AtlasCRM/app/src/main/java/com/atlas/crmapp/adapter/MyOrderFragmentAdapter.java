package com.atlas.crmapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * Created by A.Developer on 2017/3/19.
 */

public class MyOrderFragmentAdapter extends RecyclerView.Adapter<MyOrderFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<ResponseOpenOrderJson> data;

    public MyOrderFragmentAdapter(Context c, List<ResponseOpenOrderJson> data) {
        this.mContext = c;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_order, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

       final ResponseOpenOrderJson order = data.get(position);
        holder.orderDate.setText(DateUtil.timesMin(data.get(position).getCreateTime()));
   //     holder.orderStatus.setText(data.get(position).getStatus() == 1 ? "已完成" : "待付款");
        holder.orderStatus.setText(FormatCouponInfo.getOrderState(data.get(position).getState()));
        List<ResponseOpenOrderJson.OrderItem> items = data.get(position).getItems();

        int productNum = 0;
        for(ResponseOpenOrderJson.OrderItem item :items){
            productNum = productNum +  item.getCount() ;
        }
        holder.orderNumberBottom.setText(Html.fromHtml(mContext.getString(R.string.order_num_product, productNum)));
        holder.orderPriceBottom.setText(FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(data.get(position).getActualAmount(), 2));
        holder.linearLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderConfirmActivity(order);
            }
        });

        long id = data.get(position).getId();

        if(items != null && items.size() >0){
            int size = items.size();
            Logger.d(" id  " + id + " size  " + size);



            StringBuffer sb = new StringBuffer();
            for (ResponseOpenOrderJson.OrderItem item : items){
                sb.append(item.getProductName());
                sb.append("/");
            }
            String orderName = sb.toString();
            if(StringUtils.isNotEmpty(orderName)){
                int orderNameLength = orderName.length();
                if( orderNameLength>0){
                    orderName = orderName.substring(0, orderNameLength -1);
                }
                holder.orderName.setText(orderName);
            }
            int itemsSize = items.size();


                if(itemsSize > 0){
                    GlideUtils.loadCustomImageView(mContext, R.drawable.ic_meeting_pruduct, LoadImageUtils.loadSmallImage(items.get(0).getThumbnail()), holder.ivProductLogo1);
                }

                if( itemsSize > 1){
                    GlideUtils.loadCustomImageView(mContext, R.drawable.ic_meeting_pruduct, LoadImageUtils.loadSmallImage(items.get(1).getThumbnail()), holder.ivProductLogo2);
                    holder.ivProductLogo2.setVisibility(View.VISIBLE);
                }else {
                    holder.ivProductLogo2.setVisibility(View.INVISIBLE);
                }

                if( itemsSize > 2){
                    GlideUtils.loadCustomImageView(mContext, R.drawable.ic_meeting_pruduct, LoadImageUtils.loadSmallImage(items.get(2).getThumbnail()), holder.ivProductLogo3);
                    holder.ivProductLogo3.setVisibility(View.VISIBLE);
                }else{
                    holder.ivProductLogo3.setVisibility(View.INVISIBLE);
                }

                if( itemsSize > 3){
                    GlideUtils.loadCustomImageView(mContext, R.drawable.ic_meeting_pruduct, LoadImageUtils.loadSmallImage(items.get(3).getThumbnail()), holder.ivProductLogo4);
                    holder.ivProductLogo4.setVisibility(View.VISIBLE);
                }else{
                    holder.tvProductNum.setVisibility(View.INVISIBLE);
                    holder.ivProductLogo4.setVisibility(View.INVISIBLE);
                }

                if(itemsSize > 4){
                    holder.tvProductNum.setVisibility(View.VISIBLE);
                    holder.tvProductNum.setText( "+" +(itemsSize - 3));
                }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate,orderName;
        TextView orderStatus;
        TextView orderNumberBottom;
        TextView orderPriceBottom;
        LinearLayout linearLayoutMain;
        ImageView ivProductLogo1, ivProductLogo2, ivProductLogo3, ivProductLogo4;
        TextView tvProductNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderDate = (TextView) itemView.findViewById(R.id.order_date);
            orderStatus = (TextView) itemView.findViewById(R.id.order_status);
            orderNumberBottom = (TextView) itemView.findViewById(R.id.order_number_bottom);
            orderPriceBottom = (TextView) itemView.findViewById(R.id.order_price_bottom);
            orderName = (TextView) itemView.findViewById(R.id.order_product_name);
            linearLayoutMain =(LinearLayout) itemView.findViewById(R.id.linear_main) ;
            ivProductLogo1 = (ImageView) itemView.findViewById(R.id.iv_product_logo_1);
            ivProductLogo2 = (ImageView) itemView.findViewById(R.id.iv_product_logo_2);
            ivProductLogo3 = (ImageView) itemView.findViewById(R.id.iv_product_logo_3);
            ivProductLogo4 = (ImageView) itemView.findViewById(R.id.iv_product_logo_4);
            tvProductNum = (TextView) itemView.findViewById(R.id.tv_product_4);

        }
    }


    private void gotoOrderConfirmActivity(ResponseOpenOrderJson mOrder) {
        Intent intent = new Intent(mContext, OrderConfirmActivity.class);
        intent.putExtra("type", mOrder.getBizCode());
        intent.putExtra("confirmOrder", (Serializable) mOrder);
        if(mOrder.getState().equals("COMPLETE") || mOrder.getState().equals("REDRAW")) {
            intent.putExtra("isComplete", true);
        }
        ((Activity)mContext).startActivityForResult(intent, 999);
    }
}
