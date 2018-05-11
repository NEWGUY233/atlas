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
import com.atlas.crmapp.adapter.MyScoreLogDetailAdapter;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.util.ActionUriUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class IndexActiveAdapter extends RecyclerView.Adapter<IndexActiveAdapter.Holder>{
    Context c;
    private List<ResourceJson.ResourceMedia> list;
    public IndexActiveAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexActiveAdapter.Holder(LayoutInflater.from(c).inflate(R.layout.item_index_active,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ResourceJson.ResourceMedia bean = list.get(position);
        holder.tv_title.setText(bean.content);
        holder.tv_tag.setText("#" + bean.source);
        Glide.with(c).load(bean.url).apply(new RequestOptions().centerCrop().placeholder(R.drawable.product_thum)).into(holder.iv_bg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ActionUriUtils.getIntent(c,bean.actionUri,bean.url,bean.content);
                if (i != null)
                    c.startActivity(i);
            }
        });

        holder.tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<ResourceJson.ResourceMedia> getList() {
        return list;
    }

    public void setList(List<ResourceJson.ResourceMedia> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        ImageView iv_bg;
        TextView tv_tag;
        public Holder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }
}
