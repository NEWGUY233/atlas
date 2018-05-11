package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.util.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class AATagHeadAdapter extends RecyclerView.Adapter<AATagHeadAdapter.Holder> {
    Context c;
    private List<TagBean> list;
    public AATagHeadAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_aat_tag_head,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final TagBean bean = list.get(position);

        holder.name.setText(bean.getName());
        GlideUtils.loadImageView(c,bean.getThumbnail(),holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c, TagCentreActivity.class).putExtra("data",bean));
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : list.size();
    }

    public List<TagBean> getList() {
        return list;
    }

    public void setList(List<TagBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        public Holder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
