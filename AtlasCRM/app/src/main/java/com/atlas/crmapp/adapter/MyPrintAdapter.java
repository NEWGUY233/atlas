package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.PrintDataJson;
import com.atlas.crmapp.util.DateUtil;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class MyPrintAdapter extends RecyclerView.Adapter<MyPrintAdapter.Holder> {
    public final static int DY = 0X11;
    public final static int FY = 0X12;
    public final static int SM = 0X13;

    //状态：OPEN-未付款，PAID-正在校验，PARTLY-部分成功，CANCEL-失败，CONFIRM-成功
    public final static String STATE_OPEN = "OPEN";
    public final static String STATE_PAID = "PAID";
    public final static String STATE_PARTLY = "PARTLY";
    public final static String STATE_CANCEL = "CANCEL";
    public final static String STATE_CONFIRM = "CONFIRM";

    Context c;
    int type;
    public MyPrintAdapter(Context context,int type){
        this.c = context;
        this.type = type;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_user_print,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        PrintDataJson.RowsBean bean = list.get(position);
        if (bean == null) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }
        holder.itemView.setVisibility(View.VISIBLE);
        switch (type){
            case DY:
                holder.rl_title.setVisibility(View.VISIBLE);
                holder.tv_check_.setVisibility(View.GONE);
                holder.tv_page_order.setText(c.getResources().getString(R.string.print_page_page) + bean.getTotalPages());
                break;
            case FY:
            case SM:
                holder.rl_title.setVisibility(View.GONE);
                holder.tv_check_.setVisibility(View.VISIBLE);
                holder.tv_page_order.setText(c.getResources().getString(R.string.print_page_order) + bean.getOrderId());
                break;
        }
        holder.tv_r_page.setText(c.getResources().getString(R.string.print_page) + bean.getFinishedPages());

        holder.tv_order_num.setText(c.getResources().getString(R.string.print_page_order) + bean.getOrderId());

        //状态：OPEN-未付款，PAID-正在校验，PARTLY-部分成功，CANCEL-失败，CONFIRM-成功
        holder.tv_check.setTextColor(c.getResources().getColor(R.color.print_grey));
        holder.tv_check_.setTextColor(c.getResources().getColor(R.color.print_grey));
        if (STATE_OPEN.equals(bean.getState())){
            holder.tv_check.setText(c.getResources().getString(R.string.print_state_1));
            holder.tv_check_.setText(c.getResources().getString(R.string.print_state_1));
        }else if (STATE_PAID.equals(bean.getState())){
            holder.tv_check.setText(c.getResources().getString(R.string.print_state_2));
            holder.tv_check_.setText(c.getResources().getString(R.string.print_state_2));
        }else if (STATE_PARTLY.equals(bean.getState())){
            holder.tv_check.setText(c.getResources().getString(R.string.print_state_3));
            holder.tv_check_.setText(c.getResources().getString(R.string.print_state_3));
        }else if (STATE_CANCEL.equals(bean.getState())){
            holder.tv_check.setText(c.getResources().getString(R.string.print_state_4));
            holder.tv_check_.setText(c.getResources().getString(R.string.print_state_4));
        }else if (STATE_CONFIRM.equals(bean.getState())){
            holder.tv_check.setTextColor(c.getResources().getColor(R.color.print_green));
            holder.tv_check_.setTextColor(c.getResources().getColor(R.color.print_green));
            holder.tv_check.setText(c.getResources().getString(R.string.print_state_5));
            holder.tv_check_.setText(c.getResources().getString(R.string.print_state_5));
        }else{
            holder.tv_check.setText("");
            holder.tv_check_.setText("");
        }

        holder.tv_title.setText(bean.getDocumentName());

        holder.tv_type.setText(bean.getPageSize() + ("BLACKANDWHITE".equals(bean.getColor())?
                c.getResources().getString(R.string.print_blackandwhite):c.getResources().getString(R.string.print_color)));

        holder.tv_time.setText(DateUtil.tim(bean.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));

    }

    private List<PrintDataJson.RowsBean> list;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<PrintDataJson.RowsBean> getList() {
        return list;
    }

    public void setList(List<PrintDataJson.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void updateList(List<PrintDataJson.RowsBean> list) {
        if (list == null)
            this.list = list;
        else
            this.list.addAll(list);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        RelativeLayout rl_title;
        TextView tv_order_num;
        TextView tv_check;
        TextView tv_title;
        TextView tv_check_;
        TextView tv_type;
        TextView tv_time;
        TextView tv_r_page;
        TextView tv_page_order;


        public Holder(View itemView) {
            super(itemView);
            rl_title = (RelativeLayout) itemView.findViewById(R.id.rl_title);
            tv_check = (TextView) itemView.findViewById(R.id.tv_check);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_check_ = (TextView) itemView.findViewById(R.id.tv_check_);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_r_page = (TextView) itemView.findViewById(R.id.tv_r_page);
            tv_page_order = (TextView) itemView.findViewById(R.id.tv_page_order);

        }
    }

}
