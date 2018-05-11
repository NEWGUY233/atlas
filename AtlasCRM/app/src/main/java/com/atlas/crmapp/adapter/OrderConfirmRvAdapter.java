package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.commonactivity.CouponActivity;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.util.FormatCouponInfo;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/18
 *         Description :
 */

public class OrderConfirmRvAdapter extends RecyclerView.Adapter<BaseRvViewHolder> {

    public static final int ITEM_SELECTION = 0;
    public static final int ITEM_COUPON = 1;
    public static final int ITEM_PRICE = 2;
    private ResponseOpenOrderJson data;
    private Context mContext;

    public OrderConfirmRvAdapter(Context context, ResponseOpenOrderJson orderJson) {
        mContext = context;
        this.data = orderJson;
    }

    MyViewHolder holder1;
    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId = R.layout.item_order_confirm_selection;

//        switch (viewType) {
//            case ITEM_SELECTION:
//                layoutId = R.layout.item_order_confirm_selection;
//                break;
//            case ITEM_COUPON:
//                layoutId = R.layout.item_order_confirm_coupon;
//                break;
//            default:
//                layoutId = R.layout.item_order_confirm_price_info;
//                break;
//        }

        View root = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        holder1=new MyViewHolder(root);
        return holder1;
    }
    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {

        ListViewAdapter adapter = new ListViewAdapter();
        holder1.mRvOrderList.setAdapter(adapter);
        holder1.tvTitle.setText(data.getBriefing());
        holder1.tvPrice.setText(FormatCouponInfo.formatDoublePriceToYuan(data.getAmount(), 2));
        holder1.tvNum.setText("");
        if (getItemViewType(position) == ITEM_COUPON) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CouponActivity.class));
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return 1;
    }


    @Override
    public int getItemViewType(int position) {
        //TODO 测试显示模式

        return position;

    }

    class MyViewHolder extends BaseRvViewHolder {
        com.atlas.crmapp.widget.MyListView mRvOrderList;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvNum;
        public MyViewHolder(View itemView) {
            super(itemView);
            mRvOrderList = (com.atlas.crmapp.widget.MyListView) itemView.findViewById(R.id.order_listview);
            tvTitle =(TextView) itemView.findViewById(R.id.tv_name);
            tvPrice =(TextView) itemView.findViewById(R.id.tv_price);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);

        }
    }

    class ListViewAdapter extends BaseAdapter {



        public ListViewAdapter() {

        }

        @Override
        public int getCount() {
            return data.getItems().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewHolder h;
            if (convertView == null) {
                h = new ListViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.ordercon_list, null);
                h.orderTitle = (TextView)convertView.findViewById(R.id.tv_ordertitle);
                h.orderRealPrice = (TextView) convertView.findViewById(R.id.tv_real_price);
                convertView.setTag(h);
            } else {
                h = (ListViewHolder) convertView.getTag();
            }

            h.orderTitle.setText(data.getItems().get(position).getProductName()+" x"+data.getItems().get(position).getCount());
            h.orderRealPrice.setText(FormatCouponInfo.formatDoublePrice(data.getItems().get(position).getAmount(), 2)+ mContext.getString(R.string.text_yuan));

            return convertView;
        }

        class ListViewHolder {
            TextView orderTitle;
            TextView orderRealPrice;

        }
    }
}


