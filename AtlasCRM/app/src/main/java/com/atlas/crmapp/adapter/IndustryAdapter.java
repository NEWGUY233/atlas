package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.HabitsJson;
import com.atlas.crmapp.model.IndustryJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;

import java.util.List;

/**
 * Created by Leo on 2018/2/1.
 */

public class IndustryAdapter extends RecyclerView.Adapter<IndustryAdapter.Holder> {

    Context c;
    private List<IndustryJson> list;
    public IndustryAdapter(Context context){
        this.c = context;
    }

    private String name = "";
    private IndustryJson bean;
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_industry,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (list == null || position  ==  list.size()){
            holder.llo.setVisibility(View.VISIBLE);
            holder.ll_n.setVisibility(View.GONE);

            holder.llo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCheck != null)
                        onCheck.onCheck();
                }
            });
            holder.tv_other.setText(StringUtils.isEmpty(other) ? c.getString(R.string.text_12) : other);
            if (StringUtils.isEmpty(other)){
                holder.iv_rs_.setVisibility(View.GONE);
            }else {
                holder.iv_rs_.setVisibility(View.VISIBLE);
            }
        }else {
            holder.llo.setVisibility(View.GONE);
            holder.ll_n.setVisibility(View.VISIBLE);

            final IndustryJson bean = list.get(position);
            if (name.equals(bean.getName())) {
                holder.iv_rs.setVisibility(View.VISIBLE);
                this.bean = bean;
            }else
                holder.iv_rs.setVisibility(View.GONE);

            holder.tv_title.setText(bean.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = bean.getName();
                    other = "";
                    IndustryAdapter.this.bean = bean;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size() + 1;
    }

    public void setList(List<IndustryJson> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<IndustryJson> getList(){
        return this.list;
    }

    public void addLit(List<IndustryJson> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    private String other;

    public void setOnCheck(OnCheck onCheck) {
        this.onCheck = onCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IndustryJson getBean() {
        return bean;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        if (StringUtils.isEmpty(other))
            return;
        this.other = other;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView iv_rs;
        ImageView iv_rs_;
        TextView tv_title;
        TextView tv_other;
        LinearLayout ll_n;
        LinearLayout llo;
        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_text);
            tv_other = (TextView) itemView.findViewById(R.id.tv_other);
            iv_rs = (ImageView) itemView.findViewById(R.id.iv_check);
            iv_rs_ = (ImageView) itemView.findViewById(R.id.iv_check_);
            ll_n = (LinearLayout) itemView.findViewById(R.id.ll_n);
            llo = (LinearLayout) itemView.findViewById(R.id.ll_o);
        }
    }

    private OnCheck onCheck;
    public interface OnCheck{
        void onCheck();
    }
}
