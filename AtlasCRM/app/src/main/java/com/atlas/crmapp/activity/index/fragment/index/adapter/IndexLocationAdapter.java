package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.bean.LocationBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class IndexLocationAdapter extends RecyclerView.Adapter<IndexLocationAdapter.Holder>{
    Context c;
    private List<LocationBean> list;
    public IndexLocationAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexLocationAdapter.Holder(LayoutInflater.from(c).inflate(R.layout.item_index_location,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final LocationBean bean = list.get(position);
        holder.tv_name.setText(bean.getName());
        if (position == list.size() - 1){
            holder.line.setVisibility(View.GONE);
        }else
            holder.line.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.onClick(bean.getId() + "",position,bean.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<LocationBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClick(OnItemClick click) {
        this.click = click;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv_name;
        View line;
        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            line =  itemView.findViewById(R.id.v_line);
        }
    }

    private OnItemClick click;
    public interface OnItemClick{
        void onClick(String uid,int position,String name);
    }
}
