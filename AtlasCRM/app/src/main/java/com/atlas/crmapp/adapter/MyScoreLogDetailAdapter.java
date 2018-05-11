package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ScoreDetailJson;
import com.atlas.crmapp.model.ScoreGetDetailJson;
import com.atlas.crmapp.util.DateUtil;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class MyScoreLogDetailAdapter extends RecyclerView.Adapter<MyScoreLogDetailAdapter.Holder> {

    Context c;
    private List<ScoreGetDetailJson.RowsBean> list;
    public MyScoreLogDetailAdapter(Context context){
        this.c = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_score_log_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ScoreGetDetailJson.RowsBean bean = list.get(position);
        holder.tv_score.setText(bean.getPoints() + c.getString(R.string.text_score));
        holder.tv_time.setText(DateUtil.timesStampToDate_(bean.getCreateTime()));
        holder.tv_title.setText(bean.getDescription());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<ScoreGetDetailJson.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addLit(List<ScoreGetDetailJson.RowsBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_time;
        TextView tv_score;
        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
        }
    }
}
