package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ScoreDetailJson;
import com.atlas.crmapp.util.DateUtil;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class MyScoreDetailAdapter extends RecyclerView.Adapter<MyScoreDetailAdapter.Holder> {

    Context c;
    private List<ScoreDetailJson.RowsBean> list;
    public MyScoreDetailAdapter(Context context){
        this.c = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_score_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ScoreDetailJson.RowsBean bean = list.get(position);

        holder.tv_title.setText(bean.getDescription());
        holder.tv_time.setText(DateUtil.timesStampToDate_(bean.getCreateTime()));



        if ("SPEND".equals(bean.getType())){
            holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_sub));
            holder.tv_money.setText("" + bean.getPoints());

        }else if ("GAIN".equals(bean.getType())){
            holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_add));
            holder.tv_money.setText("+ " + bean.getPoints());

        }else if ("ADJUST".equals(bean.getType())){
            if (bean.getPoints() >= 0){
                holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_add));
                holder.tv_money.setText("+ " + bean.getPoints());
            }else {
                holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_sub));
                holder.tv_money.setText("" + bean.getPoints());
            }

        }else {
            if (bean.getPoints() >= 0){
                holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_add));
                holder.tv_money.setText("+ " + bean.getPoints());
            }else {
                holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_sub));
                holder.tv_money.setText("" + bean.getPoints());
            }
        }


//        if (checkType(bean.getType())){
//            holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_add));
//            holder.tv_money.setText("+ " + bean.getPoints());
//        }else {
//            holder.tv_money.setTextColor(c.getResources().getColor(R.color.score_sub));
//            holder.tv_money.setText("" + bean.getPoints());
//        }





    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    public void setList(List<ScoreDetailJson.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addLit(List<ScoreDetailJson.RowsBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_time;
        TextView tv_money;

        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);

        }
    }

    private boolean checkType(String type){
        if ("SPEND".equals(type)) {
           return false;
        }else if ("GAIN".equals(type)) {
            return true;
        }
        return false;
    }

}
